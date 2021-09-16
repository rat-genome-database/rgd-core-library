package edu.mcw.rgd.datamodel;

public class DamagingVariant {

    private int rgdId;
    private String refNuc;
    private String variantType;
    private String varNuc;
    private String rsId;
    private String clinvarId;
    private int speciesTypeKey;
    private String geneSymbol;
    private String chromosome;
    private String paddingBase;
    private int startPos;
    private int endPos;
    private String genicStatus;
    private int mapKey;
    private int sampleId;
    private int variantFrequency;
    private int depth;
    private int qualityScore;
    private String zygosityStatus;
    private String zygosityInPseudo;
    private int zygosityNumberAllele;
    private int zygosityPercentRead;
    private String zygosityPossibleError;
    private String zygosityRefAllele;


    public DamagingVariant() {}

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getRefNuc() {
        return refNuc;
    }

    public void setRefNuc(String refNuc) {
        this.refNuc = refNuc;
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

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
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

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public String getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(String genicStatus) {
        this.genicStatus = genicStatus;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public int getVariantFrequency() {
        return variantFrequency;
    }

    public void setVariantFrequency(int variantFrequency) {
        this.variantFrequency = variantFrequency;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getZygosityStatus() {
        return zygosityStatus;
    }

    public void setZygosityStatus(String zygosityStatus) {
        this.zygosityStatus = zygosityStatus;
    }

    public String getZygosityInPseudo() {
        return zygosityInPseudo;
    }

    public void setZygosityInPseudo(String zygosityInPseudo) {
        this.zygosityInPseudo = zygosityInPseudo;
    }

    public int getZygosityNumberAllele() {
        return zygosityNumberAllele;
    }

    public void setZygosityNumberAllele(int zygosityNumberAllele) {
        this.zygosityNumberAllele = zygosityNumberAllele;
    }

    public int getZygosityPercentRead() {
        return zygosityPercentRead;
    }

    public void setZygosityPercentRead(int zygosityPercentRead) {
        this.zygosityPercentRead = zygosityPercentRead;
    }

    public String getZygosityPossibleError() {
        return zygosityPossibleError;
    }

    public void setZygosityPossibleError(String zygosityPossibleError) {
        this.zygosityPossibleError = zygosityPossibleError;
    }

    public String getZygosityRefAllele() {
        return zygosityRefAllele;
    }

    public void setZygosityRefAllele(String zygosityRefAllele) {
        this.zygosityRefAllele = zygosityRefAllele;
    }
}
