package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:26:35 PM
 * <p>
 * represents a row from QTLS table
 */
public class QTL implements Identifiable, Speciated, ObjectWithName, ObjectWithSymbol {

    private Integer key;
    private String symbol;
    private String name;
    private Integer peakOffset;
    private String chromosome;
    private Double lod;
    private Double pValue;
    private Double variance;
    private String notes;
    private Integer rgdId;
    private Integer flank1RgdId;
    private Integer flank2RgdId;
    private Integer peakRgdId;
    private String inheritanceType;
    private String lodImage;
    private String linkageImage;
    private String sourceUrl;
    private String mostSignificantCmoTerm;
    private Integer speciesTypeKey;
    private String peakRsId;
    private String flank1RsId;
    private String flank2RsId;
    private Double pValueMlog;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPeakOffset() {
        return peakOffset;
    }

    public void setPeakOffset(Integer peakOffset) {
        this.peakOffset = peakOffset;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Double getLod() {
        return lod;
    }

    public void setLod(Double lod) {
        this.lod = lod;
    }

    public Double getPValue() {
        return pValue;
    }

    public void setPValue(Double pValue) {
        this.pValue = pValue;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public Integer getFlank1RgdId() {
        return flank1RgdId;
    }

    public void setFlank1RgdId(Integer flank1RgdId) {
        this.flank1RgdId = flank1RgdId;
    }

    public Integer getFlank2RgdId() {
        return flank2RgdId;
    }

    public void setFlank2RgdId(Integer flank2RgdId) {
        this.flank2RgdId = flank2RgdId;
    }

    public Integer getPeakRgdId() {
        return peakRgdId;
    }

    public void setPeakRgdId(Integer peakRgdId) {
        this.peakRgdId = peakRgdId;
    }

    public String getInheritanceType() {
        return inheritanceType;
    }

    public void setInheritanceType(String inheritanceType) {
        this.inheritanceType = inheritanceType;
    }

    public String getLodImage() {
        return lodImage;
    }

    public void setLodImage(String lodImage) {
        this.lodImage = lodImage;
    }

    public String getLinkageImage() {
        return linkageImage;
    }

    public void setLinkageImage(String linkageImage) {
        this.linkageImage = linkageImage;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getMostSignificantCmoTerm() {
        return mostSignificantCmoTerm;
    }

    public void setMostSignificantCmoTerm(String mostSignificantCmoTerm) {
        this.mostSignificantCmoTerm = mostSignificantCmoTerm;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(Integer speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getPeakRsId() {
        return peakRsId;
    }

    public void setPeakRsId(String peakRsId) {
        this.peakRsId = peakRsId;
    }

    public Double getpValueMlog() {
        return pValueMlog;
    }

    public void setpValueMlog(Double pValueMlog) {
        this.pValueMlog = pValueMlog;
    }

    public String getFlank1RsId() {
        return flank1RsId;
    }

    public void setFlank1RsId(String flank1RsId) {
        this.flank1RsId = flank1RsId;
    }

    public String getFlank2RsId() {
        return flank2RsId;
    }

    public void setFlank2RsId(String flank2RsId) {
        this.flank2RsId = flank2RsId;
    }

    public boolean hasPeakFlankRgdID(){
        return ((peakRgdId!=null && peakRgdId!=0) || (flank1RgdId!=null && flank1RgdId!=0) || (flank2RgdId!=null && flank2RgdId!=0));
    }
}
