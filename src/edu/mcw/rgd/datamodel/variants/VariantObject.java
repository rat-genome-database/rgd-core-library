package edu.mcw.rgd.datamodel.variants;

import java.math.BigDecimal;

public class VariantObject {
    long id;
    String referenceNucleotide = "";
    String variantNucleotide = "";
    String rsId;
    String clinvarId;
    String variantType;
    int speciesTypeKey;
    String chromosome = "";
    String paddingBase;
    long startPos;
    long endPos;
    String genicStatus;
    int mapKey;

    //========================
    private long variantId;
    private int transcriptRgdId;
    private String refAA;
    private String varAA;
    private String genespliceStatus;
    private String polyphenStatus;
    private String synStatus;
    private String locationName;
    private String nearSpliceSite;
    private int fullRefNucSeqKey;
    private Integer fullRefNucPos;
    private int fullRefAASeqKey;
    private Integer fullRefAAPos;
    private String tripletError;
    private String frameShift;

    //===========================

    private BigDecimal conScore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReferenceNucleotide() {
        return referenceNucleotide;
    }

    public void setReferenceNucleotide(String referenceNucleotide) {
        this.referenceNucleotide = referenceNucleotide;
    }

    public String getVariantNucleotide() {
        return variantNucleotide;
    }

    public void setVariantNucleotide(String variantNucleotide) {
        this.variantNucleotide = variantNucleotide;
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

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
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

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
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

    public long getVariantId() {
        return variantId;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    public int getTranscriptRgdId() {
        return transcriptRgdId;
    }

    public void setTranscriptRgdId(int transcriptRgdId) {
        this.transcriptRgdId = transcriptRgdId;
    }

    public String getRefAA() {
        return refAA;
    }

    public void setRefAA(String refAA) {
        this.refAA = refAA;
    }

    public String getVarAA() {
        return varAA;
    }

    public void setVarAA(String varAA) {
        this.varAA = varAA;
    }

    public String getGenespliceStatus() {
        return genespliceStatus;
    }

    public void setGenespliceStatus(String genespliceStatus) {
        this.genespliceStatus = genespliceStatus;
    }

    public String getPolyphenStatus() {
        return polyphenStatus;
    }

    public void setPolyphenStatus(String polyphenStatus) {
        this.polyphenStatus = polyphenStatus;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getNearSpliceSite() {
        return nearSpliceSite;
    }

    public void setNearSpliceSite(String nearSpliceSite) {
        this.nearSpliceSite = nearSpliceSite;
    }

    public int getFullRefNucSeqKey() {
        return fullRefNucSeqKey;
    }

    public void setFullRefNucSeqKey(int fullRefNucSeqKey) {
        this.fullRefNucSeqKey = fullRefNucSeqKey;
    }

    public Integer getFullRefNucPos() {
        return fullRefNucPos;
    }

    public void setFullRefNucPos(Integer fullRefNucPos) {
        this.fullRefNucPos = fullRefNucPos;
    }

    public int getFullRefAASeqKey() {
        return fullRefAASeqKey;
    }

    public void setFullRefAASeqKey(int fullRefAASeqKey) {
        this.fullRefAASeqKey = fullRefAASeqKey;
    }

    public Integer getFullRefAAPos() {
        return fullRefAAPos;
    }

    public void setFullRefAAPos(Integer fullRefAAPos) {
        this.fullRefAAPos = fullRefAAPos;
    }

    public String getTripletError() {
        return tripletError;
    }

    public void setTripletError(String tripletError) {
        this.tripletError = tripletError;
    }

    public String getFrameShift() {
        return frameShift;
    }

    public void setFrameShift(String frameShift) {
        this.frameShift = frameShift;
    }

    public BigDecimal getConScore() {
        return conScore;
    }

    public void setConScore(BigDecimal conScore) {
        this.conScore = conScore;
    }
}
