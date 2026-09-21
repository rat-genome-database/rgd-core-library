package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.CountQuery;
import edu.mcw.rgd.dao.spring.DocumentEmbeddingSummaryQuery;
import edu.mcw.rgd.dao.spring.ReportObjectQuery;
import edu.mcw.rgd.dao.spring.ReportPositionQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.DocumentEmbeddingSummary;
import edu.mcw.rgd.datamodel.ReportObjectDE;
import edu.mcw.rgd.datamodel.ReportPositionDE;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DocumentEmbeddingDAO extends AbstractDAO {

    @Override
    public DataSource getDataSource() throws Exception {
        return DataSourceFactory.getInstance().getRgdRagDataSource();
    }

    public List<DocumentEmbeddingSummary> getFileSummaries() throws Exception {
        String sql = "SELECT file_name, COUNT(*) as chunk_count, MIN(created_at) as uploaded_at " +
                "FROM document_embeddings " +
                "GROUP BY file_name " +
                "ORDER BY file_name";
        DocumentEmbeddingSummaryQuery query = new DocumentEmbeddingSummaryQuery(this.getDataSource(), sql);
        return execute(query);
    }

    public int deleteByFileName(String fileName) throws Exception {
        String sql = "DELETE FROM document_embeddings WHERE file_name = ?";
        return update(sql, fileName);
    }

    public int getTotalChunkCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM document_embeddings";
        return getCount(sql);
    }

    public int getChunkCountByFileName(String fileName) throws Exception {
        String sql = "SELECT COUNT(*) FROM document_embeddings WHERE file_name = ?";
        return getCount(sql, fileName);
    }

    public boolean fileExists(String fileName) throws Exception {
        return getChunkCountByFileName(fileName) > 0;
    }

    public List<DocumentEmbeddingSummary> getFileSummariesPaginated(String search, int limit, int offset) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT file_name, COUNT(*) as chunk_count, MIN(created_at) as uploaded_at FROM document_embeddings ");

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("WHERE file_name ILIKE ? ");
            params.add("%" + search.trim() + "%");
        }

        sql.append("GROUP BY file_name ORDER BY file_name LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        DocumentEmbeddingSummaryQuery query = new DocumentEmbeddingSummaryQuery(this.getDataSource(), sql.toString());
        return execute(query, params.toArray());
    }

    public int getFileCount(String search) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT file_name) FROM document_embeddings ");

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("WHERE file_name ILIKE ? ");
            params.add("%" + search.trim() + "%");
        }

        return getCount(sql.toString(), params.toArray());
    }

    public List<String> getChunksByFileName(String fileName) throws Exception {
        String sql = "SELECT chunk FROM document_embeddings WHERE file_name = ? ORDER BY id";
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);
        return execute(query, fileName);
    }

    public Set<String> getEmbeddedFileNames() throws Exception {
        String sql = "SELECT DISTINCT file_name FROM document_embeddings";
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);
        return new HashSet<>(execute(query));
    }

    // ------------------------------------------------------------------
    // document_embeddings: rgd_id + section
    // ------------------------------------------------------------------

    /** Chunks of one object's report, optionally narrowed to a single section. */
    public List<String> getChunksByRgdId(long rgdId, String section) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT chunk FROM document_embeddings WHERE rgd_id = ? ");
        List<Object> params = new ArrayList<>();
        params.add(rgdId);

        if (section != null && !section.trim().isEmpty()) {
            sql.append("AND section = ? ");
            params.add(section.trim());
        }
        sql.append("ORDER BY id");

        StringListQuery query = new StringListQuery(this.getDataSource(), sql.toString());
        return execute(query, params.toArray());
    }

    /**
     * Chunks for a batch of symbols, narrowed to one section — the exact-lookup path that
     * replaces running a vector search per symbol. Returns chunk text only; call
     * {@link #getReportObjectsBySymbols} alongside it when the symbols themselves are needed.
     */
    public List<String> getChunksBySymbols(Collection<String> symbols, String objectType,
                                           String section) throws Exception {
        if (symbols == null || symbols.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder(
                "SELECT e.chunk FROM report_object o " +
                "JOIN document_embeddings e ON e.rgd_id = o.rgd_id " +
                "WHERE o.symbol IN (" + placeholders(symbols.size()) + ") ");
        List<Object> params = new ArrayList<>(symbols);

        if (objectType != null && !objectType.trim().isEmpty()) {
            sql.append("AND o.object_type = ? ");
            params.add(objectType.trim());
        }
        if (section != null && !section.trim().isEmpty()) {
            sql.append("AND e.section = ? ");
            params.add(section.trim());
        }
        sql.append("ORDER BY o.symbol, e.id");

        StringListQuery query = new StringListQuery(this.getDataSource(), sql.toString());
        return execute(query, params.toArray());
    }

    /** Every distinct section present in the index — useful for tuning section filters. */
    public List<String> getDistinctSections() throws Exception {
        String sql = "SELECT DISTINCT section FROM document_embeddings " +
                "WHERE section IS NOT NULL ORDER BY section";
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);
        return execute(query);
    }

    public int deleteChunksByRgdId(long rgdId) throws Exception {
        String sql = "DELETE FROM document_embeddings WHERE rgd_id = ?";
        return update(sql, rgdId);
    }

    // ------------------------------------------------------------------
    // report_object
    // ------------------------------------------------------------------

    /** One object per species, or null when that RGD ID has not been indexed. */
    public ReportObjectDE getReportObject(long rgdId) throws Exception {
        String sql = "SELECT rgd_id, object_type, symbol, name, species, file_name " +
                "FROM report_object WHERE rgd_id = ?";
        ReportObjectQuery query = new ReportObjectQuery(this.getDataSource(), sql);
        List<ReportObjectDE> results = execute(query, rgdId);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Every object carrying this symbol, across species. With more than one species loaded
     * this is what answers "which species did you mean?" without guessing.
     */
    public List<ReportObjectDE> getReportObjectsBySymbol(String symbol) throws Exception {
        String sql = "SELECT rgd_id, object_type, symbol, name, species, file_name " +
                "FROM report_object WHERE LOWER(symbol) = LOWER(?) ORDER BY species, object_type";
        ReportObjectQuery query = new ReportObjectQuery(this.getDataSource(), sql);
        return execute(query, symbol);
    }

    /** Batch symbol lookup. Symbols that do not resolve are simply absent from the result. */
    public List<ReportObjectDE> getReportObjectsBySymbols(Collection<String> symbols,
                                                          String objectType) throws Exception {
        if (symbols == null || symbols.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder(
                "SELECT rgd_id, object_type, symbol, name, species, file_name FROM report_object " +
                "WHERE symbol IN (" + placeholders(symbols.size()) + ") ");
        List<Object> params = new ArrayList<>(symbols);

        if (objectType != null && !objectType.trim().isEmpty()) {
            sql.append("AND object_type = ? ");
            params.add(objectType.trim());
        }
        sql.append("ORDER BY symbol");

        ReportObjectQuery query = new ReportObjectQuery(this.getDataSource(), sql.toString());
        return execute(query, params.toArray());
    }

    /**
     * Objects of one type on one chromosome, in chromosomal order — the complete answer to
     * "what QTL are on chromosome 14", which a similarity search can only ever partly give.
     */
    public List<ReportObjectDE> getObjectsOnChromosome(String objectType, String assembly,
                                                       String chromosome) throws Exception {
        String sql = "SELECT o.rgd_id, o.object_type, o.symbol, o.name, o.species, o.file_name " +
                "FROM report_object o " +
                "JOIN report_position p ON p.rgd_id = o.rgd_id " +
                "WHERE o.object_type = ? AND p.assembly = ? AND p.chromosome = ? " +
                "ORDER BY p.start_pos";
        ReportObjectQuery query = new ReportObjectQuery(this.getDataSource(), sql);
        return execute(query, objectType, assembly, chromosome);
    }

    public int insertOrUpdateReportObject(ReportObjectDE o) throws Exception {
        String sql = "INSERT INTO report_object (rgd_id, object_type, symbol, name, species, file_name) " +
                "VALUES (?,?,?,?,?,?) " +
                "ON CONFLICT (rgd_id) DO UPDATE SET " +
                "object_type = EXCLUDED.object_type, symbol = EXCLUDED.symbol, " +
                "name = EXCLUDED.name, species = EXCLUDED.species, file_name = EXCLUDED.file_name";
        return update(sql, o.getRgdId(), o.getObjectType(), o.getSymbol(),
                o.getName(), o.getSpecies(), o.getFileName());
    }

    /** Removes the object and, by cascade of the caller's own ordering, should follow position deletion. */
    public int deleteReportObject(long rgdId) throws Exception {
        String sql = "DELETE FROM report_object WHERE rgd_id = ?";
        return update(sql, rgdId);
    }

    // ------------------------------------------------------------------
    // report_position
    // ------------------------------------------------------------------

    /** Every assembly this object has a position on. */
    public List<ReportPositionDE> getPositions(long rgdId) throws Exception {
        String sql = "SELECT rgd_id, assembly, chromosome, start_pos, stop_pos " +
                "FROM report_position WHERE rgd_id = ? ORDER BY assembly";
        ReportPositionQuery query = new ReportPositionQuery(this.getDataSource(), sql);
        return execute(query, rgdId);
    }

    /** This object's position on one assembly, or null when it has none there. */
    public ReportPositionDE getPosition(long rgdId, String assembly) throws Exception {
        String sql = "SELECT rgd_id, assembly, chromosome, start_pos, stop_pos " +
                "FROM report_position WHERE rgd_id = ? AND assembly = ?";
        ReportPositionQuery query = new ReportPositionQuery(this.getDataSource(), sql);
        List<ReportPositionDE> results = execute(query, rgdId, assembly);
        return results.isEmpty() ? null : results.get(0);
    }

    /** Positions on one chromosome in chromosomal order, matching {@link #getObjectsOnChromosome}. */
    public List<ReportPositionDE> getPositionsOnChromosome(String objectType, String assembly,
                                                           String chromosome) throws Exception {
        String sql = "SELECT p.rgd_id, p.assembly, p.chromosome, p.start_pos, p.stop_pos " +
                "FROM report_position p " +
                "JOIN report_object o ON o.rgd_id = p.rgd_id " +
                "WHERE o.object_type = ? AND p.assembly = ? AND p.chromosome = ? " +
                "ORDER BY p.start_pos";
        ReportPositionQuery query = new ReportPositionQuery(this.getDataSource(), sql);
        return execute(query, objectType, assembly, chromosome);
    }

    /** Objects whose span on the given assembly overlaps the requested window. */
    public List<ReportPositionDE> getPositionsInRange(String assembly, String chromosome,
                                                      long start, long stop) throws Exception {
        String sql = "SELECT rgd_id, assembly, chromosome, start_pos, stop_pos " +
                "FROM report_position " +
                "WHERE assembly = ? AND chromosome = ? AND start_pos <= ? AND stop_pos >= ? " +
                "ORDER BY start_pos";
        ReportPositionQuery query = new ReportPositionQuery(this.getDataSource(), sql);
        return execute(query, assembly, chromosome, stop, start);
    }

    public int insertOrUpdateReportPosition(ReportPositionDE p) throws Exception {
        String sql = "INSERT INTO report_position (rgd_id, assembly, chromosome, start_pos, stop_pos) " +
                "VALUES (?,?,?,?,?) " +
                "ON CONFLICT (rgd_id, assembly) DO UPDATE SET " +
                "chromosome = EXCLUDED.chromosome, start_pos = EXCLUDED.start_pos, " +
                "stop_pos = EXCLUDED.stop_pos";
        return update(sql, p.getRgdId(), p.getAssembly(), p.getChromosome(),
                p.getStartPos(), p.getStopPos());
    }

    /** Clear an object's positions before rewriting them, so a re-embed replaces rather than merges. */
    public int deletePositionsByRgdId(long rgdId) throws Exception {
        String sql = "DELETE FROM report_position WHERE rgd_id = ?";
        return update(sql, rgdId);
    }

    /** {@code ?,?,?} for an IN clause of the given size. */
    private static String placeholders(int count) {
        StringBuilder sb = new StringBuilder(count * 2);
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(',');
            sb.append('?');
        }
        return sb.toString();
    }
}
