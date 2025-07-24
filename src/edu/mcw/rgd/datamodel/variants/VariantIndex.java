package edu.mcw.rgd.datamodel.variants;

import edu.mcw.rgd.datamodel.search.elasticsearch.IndexObject;

import java.util.List;

public class VariantIndex extends IndexObject {
    private String category;
    private long variant_id;
    private String rsId;
    private String clinvarId;
    private String chromosome;
    private String  paddingBase;
    private long endPos;
    private String refNuc;
    private int sampleId;
    private long startPos;
    private int totalDepth;
    private int varFreq;
    private String variantType;
    private String varNuc;
    private String zygosityStatus;
    private String genicStatus;
    private double zygosityPercentRead;
    private String zygosityPossError;
    private String zygosityRefAllele;
    private int zygosityNumAllele;
    private String zygosityInPseudo;
    private int qualityScore;
    private String HGVSNAME;
    private List<String> regionName;
    private List<String> regionNameLc;
    /*****************Sample******************/
    private String  analysisName;
    private int mapKey;
    private List<VariantTranscript> variantTranscripts;
    private List<String> conScores;
    private String dbsSnpName;
    private String clinicalSignificance;
    private String strand;
    private int geneRgdId;
    private String geneSymbol;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(long variant_id) {
        this.variant_id = variant_id;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public String getClinvarId() {
        return clinvarId;
    }

    public void setClinvarId(String clinvarId) {
        this.clinvarId = clinvarId;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getPaddingBase() {
        return paddingBase;
    }

    public void setPaddingBase(String paddingBase) {
        this.paddingBase = paddingBase;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public String getRefNuc() {
        return refNuc;
    }

    public void setRefNuc(String refNuc) {
        this.refNuc = refNuc;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public int getTotalDepth() {
        return totalDepth;
    }

    public void setTotalDepth(int totalDepth) {
        this.totalDepth = totalDepth;
    }

    public int getVarFreq() {
        return varFreq;
    }

    public void setVarFreq(int varFreq) {
        this.varFreq = varFreq;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public String getVarNuc() {
        return varNuc;
    }

    public void setVarNuc(String varNuc) {
        this.varNuc = varNuc;
    }

    public String getZygosityStatus() {
        return zygosityStatus;
    }

    public void setZygosityStatus(String zygosityStatus) {
        this.zygosityStatus = zygosityStatus;
    }

    public String getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(String genicStatus) {
        this.genicStatus = genicStatus;
    }

    public double getZygosityPercentRead() {
        return zygosityPercentRead;
    }

    public void setZygosityPercentRead(double zygosityPercentRead) {
        this.zygosityPercentRead = zygosityPercentRead;
    }

    public String getZygosityPossError() {
        return zygosityPossError;
    }

    public void setZygosityPossError(String zygosityPossError) {
        this.zygosityPossError = zygosityPossError;
    }

    public String getZygosityRefAllele() {
        return zygosityRefAllele;
    }

    public void setZygosityRefAllele(String zygosityRefAllele) {
        this.zygosityRefAllele = zygosityRefAllele;
    }

    public int getZygosityNumAllele() {
        return zygosityNumAllele;
    }

    public void setZygosityNumAllele(int zygosityNumAllele) {
        this.zygosityNumAllele = zygosityNumAllele;
    }

    public String getZygosityInPseudo() {
        return zygosityInPseudo;
    }

    public void setZygosityInPseudo(String zygosityInPseudo) {
        this.zygosityInPseudo = zygosityInPseudo;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getHGVSNAME() {
        return HGVSNAME;
    }

    public void setHGVSNAME(String HGVSNAME) {
        this.HGVSNAME = HGVSNAME;
    }

    public List<String> getRegionName() {
        return regionName;
    }

    public void setRegionName(List<String> regionName) {
        this.regionName = regionName;
    }

    public List<String> getRegionNameLc() {
        return regionNameLc;
    }

    public void setRegionNameLc(List<String> regionNameLc) {
        this.regionNameLc = regionNameLc;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public List<VariantTranscript> getVariantTranscripts() {
        return variantTranscripts;
    }

    public void setVariantTranscripts(List<VariantTranscript> variantTranscripts) {
        this.variantTranscripts = variantTranscripts;
    }

    public List<String> getConScores() {
        return conScores;
    }

    public void setConScores(List<String> conScores) {
        this.conScores = conScores;
    }

    public String getDbsSnpName() {
        return dbsSnpName;
    }

    public void setDbsSnpName(String dbsSnpName) {
        this.dbsSnpName = dbsSnpName;
    }

    public String getClinicalSignificance() {
        return clinicalSignificance;
    }

    public void setClinicalSignificance(String clinicalSignificance) {
        this.clinicalSignificance = clinicalSignificance;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }
}
