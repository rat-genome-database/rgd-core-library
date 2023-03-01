package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.util.Zygosity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Aug 10, 2011
 * Time: 11:57:20 AM
 */
public class VariantSearchBean {

    private long variantId = -1;

    protected final Log log = LogFactory.getLog(getClass());
    public List<Integer> sampleIds = new ArrayList<Integer>();
    public List<String> zygosity = new ArrayList<String>();
    public List<Integer> alleleCount = new ArrayList<Integer>();
    public List<String> genicStatus = new ArrayList<String>();
    public List<String> genes = new ArrayList<String>();

    private String chromosome;
    private long startPosition = -1;
    private long stopPosition = -1;

    public List<Integer> getSampleIds() {
        return sampleIds;
    }

    public void setSampleIds(List<Integer> sampleIds) {
        this.sampleIds = sampleIds;
    }

    private int minDepth = -1;
    private int maxDepth = -1;
    //
    private int minQualityScore = -1;
    private int maxQualityScore = -1;

    private int minPercentRead = -1;
    private int maxPercentRead = -1;

    private List<String> polyphen = new ArrayList<>();
    private List<String> location = new ArrayList<>();
    private List<String> variantType = new ArrayList<>();
    private List<String> clinicalSignificance = new ArrayList<>();

    public List<String> nearSpliceSite = new ArrayList<>();
    private String isPrematureStop;
    private String isReadthrough;
    private List<String> aaChange = new ArrayList<String>();


    private String isNovelDBSNP;
    private String isKnownDBSNP;
    private boolean hetDiffFromRef = false;
    private boolean excludePossibleError = false;
    private boolean onlyPseudoautosomal = false;
    private boolean excludePseudoautosomal = false;

    private HashMap geneMap=new HashMap();
    private HashMap termAccMap=new HashMap();

    private boolean showDifferences=false;

    private float minConservation = -1;
    private float maxConservation = -1;
    private String isProteinCoding;

    private String isFrameshift;

    private String connective = "OR";
    private int mapKey;

    private List<MappedGene> mappedGenes = new ArrayList<MappedGene>();

    public boolean isHuman() {
        return mapKey==17 || mapKey==38;
    }

    public boolean isDog() {
        return mapKey==631;
    }

    public List<MappedGene> getMappedGenes() {
        return mappedGenes;
    }

    public void setMappedGenes(List<MappedGene> mappedGenes) {
        this.mappedGenes = mappedGenes;
    }

    public HashMap getTermAccMap() {
        return termAccMap;
    }

    public void setTermAccMap(HashMap termAccMap) {
        this.termAccMap = termAccMap;
    }

    public HashMap getGeneMap() {
        return geneMap;
    }

    public void setGeneMap(HashMap geneMap) {
        this.geneMap = geneMap;
    }

    public VariantSearchBean (int mapKey) {
          this.mapKey=mapKey;
    }

    public boolean isShowDifferences() {
        return showDifferences;
    }

    public void setShowDifferences(boolean showDifferences) {
        this.showDifferences = showDifferences;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    //if we are only looking for genic regions
    private boolean isGenic() {

        if (!this.getAAChangeSQL().isEmpty() ||
            !this.getPolyphenSQL().isEmpty() ||
            !this.getClinicalSignificanceSQL().isEmpty() ||
            !this.getIsPrematureStopSQL().isEmpty() ||
            !this.getIsReadthroughSQL().isEmpty()
            ) {
            return true;
        }

        for (String status: this.genicStatus) {
            log.debug("int status = " + status);

            if (status.equals("INTERGENIC")) {
                return false;
            }
        }

        return this.genicStatus.size() > 0;
    }

    public String getVariantTable() {

        if( isHuman() ) {
            return " variant_clinvar ";
        }
        else if( isDog() ) {
            return " variant_dog ";
        }
        else if( isGenic() ) {
            return " variant_genic ";
        }else {
            return " variant ";
        }
    }
    public String getVariantTable(int mapKey) {

       if(mapKey==17 && isHuman()){
          return  " variant_human";
       }
        if( isHuman() && mapKey!=17 ) {
            return " variant_clinvar ";
        }
        else if( isDog() ) {
            return " variant_dog ";
        }
        else if( isGenic() ) {
            return " variant_genic ";
        }else {
            return " variant ";
        }
    }

    public String getVariantTranscriptTable(int mapKey) {

        if(mapKey==17&& isHuman() ) {
            return " variant_transcript_human ";
        }
        if(mapKey!=17&& isHuman() ) {
            return " variant_transcript_clinvar ";
        }

        else if( isDog() ) {
            return " variant_transcript_dog ";
        }else {
            return " variant_transcript ";
        }
    }
    public String getVariantTranscriptTable() {

        if( isHuman() ) {
            return " variant_transcript_clinvar ";
        }
        else if( isDog() ) {
            return " variant_transcript_dog ";
        }else {
            return " variant_transcript ";
        }
    }

    public String getPolyphenTable() {
         if( isDog() ) {
            return " polyphen_dog ";
        }else {
            return " polyphen ";
        }
    }
    public String getPolyphenTable(int mapKey) {
        if( isDog() ) {
            return " polyphen_dog ";
        }else {
            if(mapKey==17 && isHuman()){
                return " polyphen_human ";
            }else
            return " polyphen ";
        }
    }

    public String getConScoreTable() {
        switch(this.getMapKey()) {
            case 60: // rn3.4
                if (this.isGenic()) {
                    return " CONSERVATION_SCORE_GENIC ";
                }else {
                    return " CONSERVATION_SCORE ";
                }
            case 70: // rn5
                return " CONSERVATION_SCORE_5 ";
            case 360: // rn6
                return " CONSERVATION_SCORE_6 ";
            case 17: // hg37
                return " B37_CONSCORE_PART_IOT ";
            case 38: // hg38
                return " CONSERVATION_SCORE_HG38 ";
            default:
                return " CONSERVATION_SCORE_6 ";
        }
    }

    //if we are limiting by anything more than position, conservation, genic status, location
    public boolean isLimited() {

        String sql = getDepthSQL()
            + getScoreSql()
            + getAAChangeSQL()
            + getZygositySQL()
            + getNearSpliceSiteSQL()
            + getIsPrematureStopSQL()
            + getIsReadthroughSQL()
            + getDBSNPSQL()
            + getPolyphenSQL()
            + getClinicalSignificanceSQL()
            + getAlleleCountSQL()
            + getPsudoautosomalSQL()
            + getReadDepthSql();

        return !sql.equals("");
    }

    public String getChromosome() {
        return this.chromosome;
    }

    public Long getStartPosition() {
        return this.startPosition;
    }

    public Long getStopPosition() {
        return this.stopPosition;
    }

    public String getConnective() {
        return connective;
    }

    public void setConnective(String connective) {
        this.connective = connective;
    }

    public long getVariantId() {
        return variantId;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    /**
     * Return true if any of the transcript related values are set
     *
     * @return
     */
    public boolean hasTranscript() {
       if (polyphen.size() > 0) {
            return true;
        }
        if (nearSpliceSite.size() > 0) {
            return true;
        }
        if (isTrue(isPrematureStop)) {
            return true;
        }
        if (isTrue(isReadthrough)) {
            return true;
        }
        if (aaChange.size() > 0) {
            return true;
        }
        if (location.size() > 0) {
            return true;
        }
        if (isTrue(isProteinCoding)) {
            return true;
        }
        if (isTrue(isFrameshift)) {
            return true;
        }
        return false;
    }
    /**
     * Return true if any of the transcript related values are set
     *
     * @return
     */
    public boolean hasOnlyTranscript() {
        if (nearSpliceSite.size() > 0) {
            return true;
        }
        if (isTrue(isPrematureStop)) {
            return true;
        }
        if (isTrue(isReadthrough)) {
            return true;
        }
        if (aaChange.size() > 0) {
            return true;
        }
        if (location.size() > 0) {
            return true;
        }
        if (isTrue(isProteinCoding)) {
            return true;
        }
        if (isTrue(isFrameshift)) {
            return true;
        }
        return false;
    }
    /*
     * Methods to check if the various types of data are being limited
     */

    public boolean hasDBSNP() {

        if ((this.isKnownDBSNP == null && this.isNovelDBSNP == null) || (isTrue(this.isKnownDBSNP) && isTrue(this.isNovelDBSNP))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasConScore() {

        if (this.minConservation < 0 && this.maxConservation < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasPolyphen() {
        return this.polyphen.size() != 0;
    }

    public boolean hasClinicalSignificance() {
        return !this.clinicalSignificance.isEmpty();
    }

    public boolean hasPosition() {
        return this.chromosome != null;
    }

    /*
     * getters and setters
     */

    public boolean getKnownDBSNP() {
        return isTrue(this.isKnownDBSNP);
    }

    public void setIsPrematureStop(String prematureStop) {
        isPrematureStop = prematureStop;
    }

    public void setIsReadthrough(String readthrough) {
        isReadthrough = readthrough;
    }

    public void setIsFrameshift(String frameshift) {
        isFrameshift = frameshift;
    }

    public boolean getNovelDBSNP() {
        return isTrue(this.isNovelDBSNP);
    }

    public void setPosition(String chr, String startPosition, String stopPosition) {

        if (chr != null && !chr.equals("")) {
            this.chromosome = chr;

            try {
                this.startPosition = Long.parseLong(startPosition);
            } catch (Exception ignored) {

            }
            try {
                this.stopPosition = Long.parseLong(stopPosition);
            } catch (Exception ignored) {

            }
        }

    }

    public void setChromosome(String chr) {
         this.chromosome = chr;
    }



    public void setDepth(String minStr, String maxStr) {
        int min = -1;
        int max = -1;

        try {
            min = Integer.parseInt(minStr);
        } catch (Exception ignored) {

        }

        try {
            max = Integer.parseInt(maxStr);
        } catch (Exception ignored) {

        }

        this.setDepth(min, max);
    }


    public void setDepth(int min, int max) {
        this.minDepth = min;
        this.maxDepth = max;
    }

    public void setAAChange(String synonymous, String nonSynonymous) {

        if (isTrue(synonymous)) {
            log.debug("setting aa change to  synonymous");
            this.aaChange.add("synonymous");
        }

        if (isTrue(nonSynonymous)) {
            log.debug("setting aa change to  non");
            this.aaChange.add("nonsynonymous");
        }
    }

    public void setDBSNPNovel(String novel, String known) {

        if (isTrue(novel)) {
            this.isNovelDBSNP = novel;
        }
        if (isTrue(known)) {
            this.isKnownDBSNP = known;
        }
    }

    public void setPseudoautosomal(String exclude, String only) {
        this.excludePseudoautosomal = isTrue(exclude);
        this.onlyPseudoautosomal = isTrue(only);

    }

    public String getPsudoautosomalSQL() {
        String sql = "";
        if (this.excludePseudoautosomal) {
            sql += " and v.zygosity_in_pseudo='N' ";
        }

        if (this.onlyPseudoautosomal) {
            sql += " and v.zygosity_in_pseudo='Y' ";
        }
        return sql;
    }

    /**
     * The parameters must containe the string "true"
     *
     * @param one
     * @param two
     * @param three
     * @param four
     */
    public void setAlleleCount(String one, String two, String three, String four) {

        if (isTrue(one)) {
            this.alleleCount.add(1);
        }
        if (isTrue(two)) {
            this.alleleCount.add(2);
        }
        if (isTrue(three)) {
            this.alleleCount.add(3);
        }
        if (isTrue(four)) {
            this.alleleCount.add(4);
        }

    }

    /**
     * Used when being called from Flex front end that passes in a comma separated String
     * of alleles to search for "1,2" or "1,2,4" , "3"  instead of seperate fields found in a web page.
     *
     * @param alleleCountString
     */
    public void setAlleleCountFromCommaList(String alleleCountString) {

        if (alleleCountString != null) {
            String[] alleleArray = alleleCountString.split(",");
            for (String alleleStr : alleleArray) {
                if (alleleStr.equals("1")) {
                    this.alleleCount.add(1);
                }
                if (alleleStr.equals("2")) {
                    this.alleleCount.add(2);
                }
                if (alleleStr.equals("3")) {
                    this.alleleCount.add(3);
                }
                if (alleleStr.equals("4")) {
                    this.alleleCount.add(4);
                }
            }
        }
    }


    public void setZygosity(String het, String hom, String possiblyHom, String hemi, String probablyHemi, String possiblyHemi, String excludePossibleError, String hetDiffFromRef) {

        if (isTrue(het)) {
            this.zygosity.add(Zygosity.HETEROZYGOUS);
        }

        if (isTrue(hemi)) {
            this.zygosity.add(Zygosity.HEMIZYGOUS);
        }

        if (isTrue(hom)) {
            this.zygosity.add(Zygosity.HOMOZYGOUS);
        }

        if (isTrue(possiblyHom)) {
            this.zygosity.add(Zygosity.POSSIBLY_HOMOZYGOUS);
        }

        if (isTrue(probablyHemi)) {
            this.zygosity.add(Zygosity.PROBABLY_HEMIZYGOUS);
        }
        if (isTrue(possiblyHemi)) {
            this.zygosity.add(Zygosity.POSSIBLY_HEMIZYGOUS);
        }

        this.excludePossibleError = isTrue(excludePossibleError);
        this.hetDiffFromRef = isTrue(hetDiffFromRef);

    }

    public void setVariantType(String snv, String ins, String del) {
        log.debug("setting variant");

        log.debug("snv = " + snv);
        log.debug("ins = " + ins);
        log.debug("del = " + del);


        if (isTrue(snv)) {
            this.variantType.add("snp");
            this.variantType.add("snv");
        }
        if (isTrue(ins)) {
            this.variantType.add("ins");
        }
        if (isTrue(del)) {
            this.variantType.add("del");
        }

    }

    public void setPolyphen(String benign, String possiblyDamaging, String probablyDamaging) {
        if (isTrue(benign)) {
            this.polyphen.add("benign");
        }
        if (isTrue(possiblyDamaging)) {
            this.polyphen.add("possibly damaging");
        }
        if (isTrue(probablyDamaging)) {
            this.polyphen.add("probably damaging");
        }
    }

    public void setPolyphen(String polyphenPrediction) {
        if (polyphenPrediction != null && !polyphenPrediction.equals("")) {
            this.polyphen.add(polyphenPrediction.toLowerCase());
        }

    }

    public void setClinicalSignificance(String pathogenic, String benign, String other) {
        if (isTrue(pathogenic)) {
            this.clinicalSignificance.add("pathogenic");
        }
        if (isTrue(benign)) {
            this.clinicalSignificance.add("benign");
        }
        if (isTrue(other)) {
            this.clinicalSignificance.add("conflicting");
            this.clinicalSignificance.add("uncertain");
        }
    }

    public void setNearSpliceSite(String nearSpliceSite) {

        if (isTrue(nearSpliceSite)) {
            this.nearSpliceSite.add("T");
        }
    }

    public void setGenicStatus(String genic, String intergenic) {
        if (isTrue(genic)) {
            this.genicStatus.add(Variant.GENIC);
        }
        if (isTrue(intergenic)) {
            this.genicStatus.add(Variant.INTERGENIC);
        }

    }

    public void setLocation(String intronic, String threePrimeUTR, String fivePrimeUTR, String proteinCoding) {


        // This is handled differently
        this.isProteinCoding = proteinCoding;

        if (isTrue(intronic)) {
            this.location.add("INTRON,NON-CODING");
            this.location.add("3UTRS,INTRON");
            this.location.add("5UTRS,INTRON");
            this.location.add("5UTRS,INTRON,NON-CODING");
            this.location.add("INTRON,NON-CODING");
            this.location.add("3UTRS,3UTRS,INTRON");
            this.location.add("INTRON");
            this.location.add("5UTRS,5UTRS,INTRON");
            this.location.add("3UTRS,INTRON,NON-CODING");
        }

        if (isTrue(threePrimeUTR)) {
            if (!isTrue(intronic)) {
                this.location.add("3UTRS,INTRON");
                this.location.add("3UTRS,3UTRS,INTRON");
                this.location.add("3UTRS,INTRON,NON-CODING");
            }
            this.location.add("3UTRS,EXON,NON-CODING");
        }

        if (isTrue(fivePrimeUTR)) {
            if (!isTrue(intronic)) {
                this.location.add("5UTRS,INTRON");
                this.location.add("5UTRS,INTRON,NON-CODING");
                this.location.add("5UTRS,5UTRS,INTRON");
            }
            this.location.add("5UTRS,EXON,NON-CODING");
        }

    }

    /**
     * Set Quality scores from strings
     *
     * @param minStr
     * @param maxStr
     */
    public void setScore(String minStr, String maxStr) {

        try {
            this.minQualityScore = Integer.parseInt(minStr);
        } catch (Exception ignored) {

        }

        try {
            this.maxQualityScore = Integer.parseInt(maxStr);
        } catch (Exception ignored) {

        }

    }

    /**
     * Set Percent read to search on . Looks that the VARINT.ZYGOSITY_PERCENT_READ
     *
     * @param min
     * @param max
     */
    public void setPercentRead(int min, int max) {
        this.minPercentRead = min;
        this.maxPercentRead = max;
    }

    /**
     * Set Quality Score  from ints
     *
     * @param min
     * @param max
     */
    public void setScore(int min, int max) {
        this.minQualityScore = min;
        this.maxQualityScore = max;
    }

    public void setConScore(float min, float max) {
        this.minConservation = min;
        this.maxConservation = max;
    }

    /*
     * Private Utility Methods
     */
    private String buildOr(String name, List values, boolean isString) {
        Iterator it = values.iterator();
        String sql = "( ";
        String qualifier = "";

        if (isString) {
            qualifier = "'";
        }

        if (it.hasNext()) {
            sql += name + "=" + qualifier + it.next() + qualifier;
        }

        while (it.hasNext()) {
            sql += " or " + name + "=" + qualifier + it.next() + qualifier;
        }

        sql += ") ";
        log.debug(sql);
        return sql;
    }

    private String buildOrLike(String name, List<String> values) {
        Iterator it = values.iterator();
        String sql = "( ";

        if (it.hasNext()) {
            sql += name + " like '%" + it.next() + "%'";
        }

        while (it.hasNext()) {
            sql += " or " + name + " like '%" + it.next() + "%'";
        }

        sql += ") ";
        log.debug(sql);
        return sql;
    }

    private String buildRange(String name, int min, int max) {

        if (min == -1 && max != -1) {
            return "(" + name + " <= " + max + ")";
        }
        if (min != -1 && max == -1) {
            return "(" + name + " >= " + min + ")";
        }

        return "(" + name + " >= " + min + " and " + name + " <= " + max + ")";

    }

    /*
     * SQL Generation Methods
     */

    public String getIsPrematureStopSQL() {
        if (this.isPrematureStop != null && isTrue(this.isPrematureStop)) {
            return " and (vt.ref_aa != vt.var_aa and vt.var_aa='*') ";
        } else {
            return "";
        }
    }

    public String getIsReadthroughSQL() {
        if (this.isReadthrough != null && isTrue(this.isReadthrough)) {
            return " and (vt.ref_aa != vt.var_aa and vt.ref_aa='*') ";
        } else {
            return "";
        }
    }

    public String getIsFrameshiftSQL() {
        if (this.isFrameshift != null && isTrue(this.isFrameshift)) {
            return " and vt.frameshift='T' ";
        } else {
            return "";
        }
    }

    public String getPositionSQL() {

        String sql = "";

        if (this.hasPosition()) {
            sql += " and ( v.chromosome='" + this.chromosome + "' ";

            if (this.startPosition != -1) {
                sql += " and v.start_pos >= " + this.startPosition;
            }

            if (this.stopPosition != -1) {
                sql += " and v.start_pos <=" + this.stopPosition;
            }

            sql += ") ";
        }

        return sql;
    }

    public String getMapsDataPositionSQL() {

        String sql = "";

        if (this.hasPosition()) {
            sql += " and ( md.chromosome='" + this.chromosome + "' ";

            if (this.startPosition != -1) {
                sql += "and  md.stop_pos >= " + this.startPosition;
            }

            if (this.stopPosition != -1) {
                sql += " and  md.start_pos <=" + this.stopPosition;
            }
            sql += ") ";
        }

        return sql;
    }

    public String getNearSpliceSiteSQL() {
        if (this.nearSpliceSite.size() == 0) {
            return "";
        } else {
            return " and " + buildOr("vt.near_splice_site", nearSpliceSite, true);
        }

    }

    public String getSampleSQLOverlap() {
        String sql = "";

        if (this.sampleIds.size() == 0) {
            return "";
        }

        sql += " and v.sample_id=" + this.sampleIds.get(0);

        for (int i = 0; i < this.sampleIds.size(); i++) {
            if (i == 0) continue;
            sql += " and (v.chromosome=v" + i + ".chromosome and v.var_nuc=v" + i + ".var_nuc and v.start_pos=v" + i + ".start_pos and v" + i + ".sample_id=" + this.sampleIds.get(i) + ")";
        }
        return sql;
    }


    public String getSampleSQL() {
        String sql = "";

        if (this.sampleIds.size() == 0) {
            return "";
        }

        if (this.connective.equals("AND")) {
            return getSampleSQLOverlap();
        }

        sql += " and (v.sample_id=" + this.sampleIds.get(0);

        for (int i = 1; i < this.sampleIds.size(); i++) {
//            if (i == 0) continue;
//            sql += " and (v.chromosome=v" + i + ".chromosome and v.var_nuc=v" + i + ".var_nuc and v.start_pos=v" + i + ".start_pos and v" + i + ".sample_id=" + this.sampleIds.get(i) + ")";

              sql += " or v.sample_id=" + this.sampleIds.get(i);

        }

        sql+= " )";

        return sql;
    }

    public String getDepthSQL() {
        if (this.minDepth == -1 && this.maxDepth == -1) {
            return "";
        } else {
            return " and " + this.buildRange("v.total_depth", this.minDepth, this.maxDepth);
        }
    }

    public String getAlleleCountSQL() {

        String sql = "";

        if (this.alleleCount.size() != 0) {
            sql += " and " + buildOr("v.zygosity_num_allele", this.alleleCount, false);
        }

        return sql;
    }

    public String getZygositySQL() {

        String sql = "";

        if (this.zygosity.size() != 0) {
            sql += " and " + buildOr("v.zygosity_status", this.zygosity, true);
        }

        if (this.hetDiffFromRef) {
            sql += " and (v.zygosity_status='heterozygous' and zygosity_ref_allele='N') ";
        }

        if (this.excludePossibleError) {
            sql += " and v.zygosity_poss_error='N' ";
        }

        return sql;
    }

    public String getAAChangeSQL() {
        if (this.aaChange.size() == 0) {
            return "";
        } else {
            return " and " + buildOr("vt.syn_status", this.aaChange, true);
        }

    }

    public String getPolyphenSQL() {
        if (this.polyphen.size() == 0) {
            return "";
        } else {
            return " and " + buildOr("p.prediction", this.polyphen, true);
        }
    }

    public String getClinicalSignificanceSQL() {
        if (this.clinicalSignificance.size() == 0) {
            return "";
        } else {
            return " and " + buildOrLike("cv.clinical_significance", this.clinicalSignificance);
        }
    }

    public String getVariantTypeSQL() {
        if (this.variantType.size() == 0) {
            return "";
        } else {
            return " and " + buildOr("v.variant_type", this.variantType, true);
        }
    }

    public String getGenicStatusSQL() {
        if (this.genicStatus.size() == 0) {
            return "";
        } else {
            return " and " + buildOr("v.genic_status", this.genicStatus, true);
        }
    }

    public String getVariantIdSql() {
        if (this.variantId == -1) {
            return "";
        } else {
            return " and v.variant_id=" + this.variantId;
        }
    }


    public String getLocationSQL() {
        String retString = "";
        log.debug("isProteinCoding = " + this.isProteinCoding);

        if (isTrue(isProteinCoding)) {
            retString += " AND vt.ref_aa is not null ";
        }
        if (this.location.size() == 0) {
            return retString;
        } else {
            return retString + " and " + buildOr("vt.location_name", this.location, true);
        }
    }

    /**
     * Quality Score field search
     *
     * @return
     */
    public String getScoreSql() {
        if (this.minQualityScore == -1 || this.maxQualityScore == -1) {
            return "";
        } else {
            return " and " + this.buildRange("v.QUALITY_SCORE", this.minQualityScore, this.maxQualityScore);
        }
    }

    public String getReadDepthSql() {
        if (this.minPercentRead == -1 || this.maxPercentRead == -1) {
            return "";
        } else {
            return " and " + this.buildRange("v.ZYGOSITY_PERCENT_READ", this.minPercentRead, this.maxPercentRead);
        }
    }

    public String getConScoreSQL() {
        if (this.hasConScore()) {
            if (this.minConservation ==0 && this.maxConservation==0) {
                return " and (cs.score=0) ";
            }else {
                return " and (cs.score>=" + this.minConservation + " and cs.score <= " + this.maxConservation + " )";
            }
        }
        return "";

    }

    public String getGeneMapSQL() {

        String sql = "";

        if (this.mappedGenes.size() > 0) {

            sql += " and gl.gene_symbols_lc in ( ";

            Iterator it = this.mappedGenes.iterator();

            boolean first = true;
            while(it.hasNext()) {
                MappedGene mg = (MappedGene) it.next();
                if (!first) {
                    sql +=",";
                }else {
                    first=false;
                }
                sql += "'" + mg.getGene().getSymbol().toLowerCase() + "'";

            }
            sql += ")";

        }

        return sql;
    }


    public String getGenesSQL() {

       if (true) {
           return "";
       }

        String sql = "";
        int count = 0;
        for (String symbol : this.genes) {
            if (symbol.trim().equals("")) {
                continue;
            }
            if (symbol.trim().toUpperCase().equals("ALL")) {
                return "";
            }
            if (symbol.equals("Intergenic")) {
                // We assume that is Intergenic comes through , there will only be intergenic with no genes also included
                sql += "  and ( v.GENIC_STATUS = '" + Variant.INTERGENIC + "'  ";
            } else {
                // Real search by Gene names here ...
                if (count == 0) {
                    sql += " and gene_symbols_lc in ('" + symbol.toLowerCase() + "' ";
                } else {
                    sql += ",'" + symbol.toLowerCase() + "' ";
                }
            }
            count++;
        }
        if (count > 0) {
            sql += ") ";
        }
        return sql;

    }

    /**
     * Returns the sql needed to limit the search results based on the DB_SNP query parameters.
     *
     * @return
     */
    public String getDBSNPSQL() {

        if (this.hasDBSNP()) {

            if (isTrue(this.isKnownDBSNP)) {
                return " AND dbs.SNP_NAME is not null \n";
            } else if (isTrue(this.isNovelDBSNP)) {
                return "   AND dbs.SNP_NAME is null  \n";
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * Generate the SQL needed for the table join.
     *
     * @param limit set true for a limited the search as is needed in summary search
     *              or false = unlimited search needed for full search
     * @return
     */
    public String[] getTableJoinSQL (String sqlFrom, boolean limit) {

        String vtTable = getVariantTranscriptTable(mapKey);
        String polyTable = getPolyphenTable(mapKey);
        String sql = "";


        if (limit) {

            if (this.hasOnlyTranscript()) {
                sql += " inner join "+vtTable+" vt on v.variant_id=vt.variant_id ";
                sqlFrom += ",vt.* ";
                sql += " inner join transcripts t on ( vt.transcript_rgd_id = t.transcript_rgd_id ) ";


            }
             if (this.hasPolyphen()) {
                    sql += " inner join "+polyTable+" p on (v.variant_id=p.variant_id and p.protein_status='100 PERC MATCH') ";
                    sqlFrom += ",p.* ";
                }

            if (this.hasDBSNP() ) {

                if (this.hasDBSNP() || this.getNovelDBSNP()) {
                    sql += " left outer JOIN sample s ON (v.sample_id=s.sample_id AND s.MAP_KEY=" + this.mapKey + ") " +

                        " left outer JOIN  db_snp dbs ON  ( v.START_POS = dbs.POSITION " +
                        "    AND v.CHROMOSOME = dbs.CHROMOSOME  " +
                        "    AND v.VAR_NUC = dbs.ALLELE  " +
                        "    AND dbs.SOURCE = s.DBSNP_SOURCE \n" +
                        "    AND dbs.MAP_KEY = s.MAP_KEY ) \n ";
                }
                sqlFrom += ",dbs.* , dbsa.* , dbsa.snp_name as MCW_DBSA_SNP_NAME, dbs.snp_name as MCW_DBS_SNP_NAME";
            }

            if (this.hasConScore()) {
                sql += " inner join  " + this.getConScoreTable() + " cs on (cs.chr=v.chromosome and cs.position = v.start_pos) ";
                sqlFrom += ",cs.* ";
            }

            if (this.hasClinicalSignificance()) {
                log.info(" getTableJoinSql hasClinicalSignificance");
                sql += " inner join clinvar cv on (v.rgd_id=cv.rgd_id) ";
                sqlFrom += ",cv.* ";
            }

        } else {

            sql += " left outer join "+vtTable+" vt on v.variant_id=vt.variant_id ";
            sqlFrom += ",vt.* ";

            sql += " left outer join transcripts t on vt.transcript_rgd_id = t.transcript_rgd_id   ";
            sqlFrom += ",t.* ";

            sql += " left outer join "+polyTable+" p on (vt.variant_transcript_id=p.variant_transcript_id and p.protein_status='100 PERC MATCH') ";
            sqlFrom += ",p.* ";

            sql += " left outer join clinvar cv on (v.rgd_id=cv.rgd_id) ";
            sqlFrom += ",cv.* ";

            sql += " left outer join " + this.getConScoreTable() + " cs on (cs.chr=v.chromosome and cs.position = v.start_pos) ";
            sqlFrom += ",cs.* ";

            sql += " left outer JOIN sample s ON (v.sample_id=s.sample_id AND s.MAP_KEY=" + this.mapKey + ") " +

                " left outer join db_snp dbs ON (  v.START_POS  = dbs.POSITION  " +
                "    AND v.CHROMOSOME = dbs.CHROMOSOME   " +
                "    AND v.VAR_NUC = dbs.ALLELE  " +
                "    AND dbs.SOURCE = s.DBSNP_SOURCE \n" +
                "    AND dbs.MAP_KEY = s.MAP_KEY ) \n ";

            sqlFrom += ",dbs.*  ";
        }
        sql += " where 1=1  ";
        return new String[]{sqlFrom, sql};
    }


    private boolean isTrue(String booleanString) {
        if (booleanString != null && booleanString.toLowerCase().equals("true")) {
            return true;
        } else {
            return false;
        }
    }


    /***************************************Added get methods 07/15/2019**********************************************/

    public Log getLog() {
        return log;
    }

    public List<String> getZygosity() {
        return zygosity;
    }

    public void setZygosity(List<String> zygosity) {
        this.zygosity = zygosity;
    }

    public List<Integer> getAlleleCount() {
        return alleleCount;
    }

    public void setAlleleCount(List<Integer> alleleCount) {
        this.alleleCount = alleleCount;
    }

    public List<String> getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(List<String> genicStatus) {
        this.genicStatus = genicStatus;
    }

    public List<String> getGenes() {
        return genes;
    }

    public void setGenes(List<String> genes) {
        this.genes = genes;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public void setStopPosition(long stopPosition) {
        this.stopPosition = stopPosition;
    }

    public int getMinDepth() {
        return minDepth;
    }

    public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMinQualityScore() {
        return minQualityScore;
    }

    public void setMinQualityScore(int minQualityScore) {
        this.minQualityScore = minQualityScore;
    }

    public int getMaxQualityScore() {
        return maxQualityScore;
    }

    public void setMaxQualityScore(int maxQualityScore) {
        this.maxQualityScore = maxQualityScore;
    }

    public int getMinPercentRead() {
        return minPercentRead;
    }

    public void setMinPercentRead(int minPercentRead) {
        this.minPercentRead = minPercentRead;
    }

    public int getMaxPercentRead() {
        return maxPercentRead;
    }

    public void setMaxPercentRead(int maxPercentRead) {
        this.maxPercentRead = maxPercentRead;
    }

    public List<String> getPolyphen() {
        return polyphen;
    }

    public void setPolyphen(List<String> polyphen) {
        this.polyphen = polyphen;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public List<String> getVariantType() {
        return variantType;
    }

    public void setVariantType(List<String> variantType) {
        this.variantType = variantType;
    }

    public List<String> getClinicalSignificance() {
        return clinicalSignificance;
    }

    public void setClinicalSignificance(List<String> clinicalSignificance) {
        this.clinicalSignificance = clinicalSignificance;
    }

    public List<String> getNearSpliceSite() {
        return nearSpliceSite;
    }

    public void setNearSpliceSite(List<String> nearSpliceSite) {
        this.nearSpliceSite = nearSpliceSite;
    }

    public String getIsPrematureStop() {
        return isPrematureStop;
    }

    public String getIsReadthrough() {
        return isReadthrough;
    }

    public List<String> getAaChange() {
        return aaChange;
    }

    public void setAaChange(List<String> aaChange) {
        this.aaChange = aaChange;
    }

    public String getIsNovelDBSNP() {
        return isNovelDBSNP;
    }

    public void setIsNovelDBSNP(String isNovelDBSNP) {
        this.isNovelDBSNP = isNovelDBSNP;
    }

    public String getIsKnownDBSNP() {
        return isKnownDBSNP;
    }

    public void setIsKnownDBSNP(String isKnownDBSNP) {
        this.isKnownDBSNP = isKnownDBSNP;
    }

    public boolean isHetDiffFromRef() {
        return hetDiffFromRef;
    }

    public void setHetDiffFromRef(boolean hetDiffFromRef) {
        this.hetDiffFromRef = hetDiffFromRef;
    }

    public boolean isExcludePossibleError() {
        return excludePossibleError;
    }

    public void setExcludePossibleError(boolean excludePossibleError) {
        this.excludePossibleError = excludePossibleError;
    }

    public boolean isOnlyPseudoautosomal() {
        return onlyPseudoautosomal;
    }

    public void setOnlyPseudoautosomal(boolean onlyPseudoautosomal) {
        this.onlyPseudoautosomal = onlyPseudoautosomal;
    }

    public boolean isExcludePseudoautosomal() {
        return excludePseudoautosomal;
    }

    public void setExcludePseudoautosomal(boolean excludePseudoautosomal) {
        this.excludePseudoautosomal = excludePseudoautosomal;
    }

    public float getMinConservation() {
        return minConservation;
    }

    public void setMinConservation(float minConservation) {
        this.minConservation = minConservation;
    }

    public float getMaxConservation() {
        return maxConservation;
    }

    public void setMaxConservation(float maxConservation) {
        this.maxConservation = maxConservation;
    }

    public String getIsProteinCoding() {
        return isProteinCoding;
    }

    public void setIsProteinCoding(String isProteinCoding) {
        this.isProteinCoding = isProteinCoding;
    }

    public String getIsFrameshift() {
        return isFrameshift;
    }
}

