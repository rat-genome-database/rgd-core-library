package edu.mcw.rgd.process;

import edu.mcw.rgd.datamodel.ReportPositionDE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pulls the structured facts out of a generated report so they can be stored as columns
 * instead of only as prose.
 *
 * <p>Everything here is recovered from text the generators already produce — the
 * {@code <!-- file_name: ... -->} display name, the heading breadcrumb
 * the report chunker prepends to every chunk,
 * and the Genomic Position table. Nothing needs re-querying Oracle.</p>
 *
 * <p>Lives in rgdcore so the pipeline and the chatbot share one parser: both ingest the
 * same reports, and the chunker they each hold is already a hand-synced copy — a second
 * duplicated file would be one more thing to keep in step.</p>
 */
public final class ReportMetadata {

    private ReportMetadata() {}

    /** "RGD Gene Report - A2m (Rat) (2004)" — symbol may contain spaces, slashes and carets. */
    private static final Pattern DISPLAY_WITH_SPECIES = Pattern.compile(
            "^RGD\\s+(\\S+)\\s+Report\\s+-\\s+(.+?)\\s+\\(([^()]+)\\)\\s+\\((\\d+)\\)$");

    /** Same, for the handful of reports whose display name carries no species. */
    private static final Pattern DISPLAY_NO_SPECIES = Pattern.compile(
            "^RGD\\s+(\\S+)\\s+Report\\s+-\\s+(.+?)\\s+\\((\\d+)\\)$");

    /**
     * A data row of the Genomic Position table: {@code | assembly | chr | start | stop |}.
     *
     * <p>Requiring digits in the start and stop cells is what skips the
     * {@code | Assembly | Chromosome | Start | Stop |} header and any {@code |---|}
     * separator, so neither needs special-casing. The first two cells accept anything but a
     * pipe or newline because assembly names are not all bare tokens — gene reports carry
     * entries like {@code mRatBN7.2 Ensembl} and {@code UTH_Rnor_SHR_Utx}. Matching stops at
     * the fourth cell, so the extra {@code Strand} column gene reports add is simply
     * ignored rather than breaking the row.</p>
     */
    private static final Pattern POSITION_ROW = Pattern.compile(
            "\\|\\s*([^|\\n]+?)\\s*\\|\\s*([^|\\n]+?)\\s*\\|\\s*(\\d+)\\s*\\|\\s*(\\d+)\\s*\\|");

    /** The section prefix whose chunks carry coordinates. */
    private static final String GENOMIC_POSITION = "## Genomic Position";

    /** Identity of the object a report describes, as encoded in its display name. */
    public static final class Identity {
        public final String objectType;
        public final String symbol;
        public final String species;
        public final long rgdId;

        Identity(String objectType, String symbol, String species, long rgdId) {
            this.objectType = objectType;
            this.symbol = symbol;
            this.species = species;
            this.rgdId = rgdId;
        }
    }

    /**
     * Parse the display name into object type, symbol, species and RGD ID.
     *
     * @return null when the name does not carry a numeric RGD ID — ontology reports are
     *         keyed by an accession such as {@code DOID:2841}, which has no place in a
     *         {@code bigint} column. Their chunks still embed; they just get no
     *         {@code report_object} row.
     */
    public static Identity parseDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        String name = displayName.trim();

        Matcher m = DISPLAY_WITH_SPECIES.matcher(name);
        if (m.matches()) {
            return new Identity(m.group(1), m.group(2).trim(), m.group(3).trim(),
                    Long.parseLong(m.group(4)));
        }

        m = DISPLAY_NO_SPECIES.matcher(name);
        if (m.matches()) {
            return new Identity(m.group(1), m.group(2).trim(), null,
                    Long.parseLong(m.group(3)));
        }
        return null;
    }

    /**
     * The section path a chunk belongs to, taken from the heading breadcrumb the chunker
     * puts on its first line — everything after the H1 title, e.g.
     * {@code "## Genomic Position"} or {@code "## Annotation > ### Gene Ontology Annotations"}.
     *
     * @return null for a chunk with no breadcrumb, or one sitting directly under the title
     */
    public static String sectionOf(String chunk) {
        if (chunk == null || chunk.isEmpty()) {
            return null;
        }
        int newline = chunk.indexOf('\n');
        String first = (newline < 0 ? chunk : chunk.substring(0, newline)).trim();
        if (!first.startsWith("#")) {
            return null;
        }
        int sep = first.indexOf(" > ");
        return (sep < 0) ? null : first.substring(sep + 3).trim();
    }

    /**
     * The object's descriptive name, read from the parenthesised text in the H1 title.
     *
     * <p>Title shapes differ by report type: a gene reads
     * {@code "# Gene: RGD:1560404 1700001E04Rikl (RIKEN cDNA 1700001E04 gene like)"} and a
     * QTL the same with a trailing species, but a strain reads
     * {@code "# Strain: RGD:61112 13M (Rat)"} — where the parentheses hold the species, not
     * a name. Comparing against the known species is what keeps "Rat" out of the name
     * column; strains simply get a null name.</p>
     */
    public static String nameFromTitle(String chunk, String species) {
        if (chunk == null) {
            return null;
        }
        int newline = chunk.indexOf('\n');
        String first = newline < 0 ? chunk : chunk.substring(0, newline);

        // Trim the breadcrumb tail so a section heading's parentheses are never read as a name.
        int sep = first.indexOf(" > ");
        if (sep >= 0) {
            first = first.substring(0, sep);
        }

        int open = first.indexOf('(');
        if (open < 0) {
            return null;
        }
        int close = first.indexOf(')', open);
        if (close < 0) {
            return null;
        }
        String candidate = first.substring(open + 1, close).trim();
        if (candidate.isEmpty()) {
            return null;
        }
        if (species != null && candidate.equalsIgnoreCase(species)) {
            return null;   // strain titles put the species here
        }
        return candidate;
    }

    /**
     * Every assembly position for an object, read from its Genomic Position chunks.
     *
     * <p>One object keeps one identity and carries a row per assembly, so a report listing
     * six assemblies yields six {@link ReportPositionDE} rows rather than six copies of the
     * object. Large sections can be split across chunks, so all of them are scanned and
     * results de-duplicated on assembly, first row winning.</p>
     */
    public static List<ReportPositionDE> parsePositions(long rgdId, List<String> chunks) {
        Map<String, ReportPositionDE> byAssembly = new LinkedHashMap<>();
        if (chunks == null) {
            return new ArrayList<>();
        }

        for (String chunk : chunks) {
            String section = sectionOf(chunk);
            if (section == null || !section.startsWith(GENOMIC_POSITION)) {
                continue;
            }
            Matcher m = POSITION_ROW.matcher(chunk);
            while (m.find()) {
                String assembly = m.group(1);
                if (byAssembly.containsKey(assembly)) {
                    continue;
                }
                ReportPositionDE p = new ReportPositionDE();
                p.setRgdId(rgdId);
                p.setAssembly(assembly);
                p.setChromosome(m.group(2));
                p.setStartPos(parseCoordinate(m.group(3)));
                p.setStopPos(parseCoordinate(m.group(4)));
                byAssembly.put(assembly, p);
            }
        }
        return new ArrayList<>(byAssembly.values());
    }

    /**
     * Coordinates are held as {@link Integer} while the column is {@code bigint}. Rat
     * coordinates sit far inside int range, but a value that would overflow is dropped
     * rather than silently wrapped to a negative position.
     */
    private static Integer parseCoordinate(String text) {
        try {
            long value = Long.parseLong(text);
            return (value > Integer.MAX_VALUE) ? null : (int) value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
