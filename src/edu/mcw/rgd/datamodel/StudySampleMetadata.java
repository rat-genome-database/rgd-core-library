package edu.mcw.rgd.datamodel;

public class StudySampleMetadata {

    private String geoSampleAcc;
    private String tissue;
    private String strain;
    private String sex;
    private String computedSex;
    private Double ageDaysFromDobLowBound;
    private Double ageDaysFromDobHighBound;
    private String lifeStage;
    private String experimentalConditions;
    private String cellType;
    private String expCondAssocUnits;
    private Double expCondAssocValueMin;
    private Double expCondAssocValueMax;
    private Double expCondDurSecLowBound;
    private Double expCondDurSecHighBound;
    private String expCondApplicationMethod;
    private String expCondNotes;

    public String getGeoSampleAcc() {
        return geoSampleAcc;
    }

    public void setGeoSampleAcc(String geoSampleAcc) {
        this.geoSampleAcc = geoSampleAcc;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getComputedSex() {
        return computedSex;
    }

    public void setComputedSex(String computedSex) {
        this.computedSex = computedSex;
    }

    public Double getAgeDaysFromDobLowBound() {
        return ageDaysFromDobLowBound;
    }

    public void setAgeDaysFromDobLowBound(Double ageDaysFromDobLowBound) {
        this.ageDaysFromDobLowBound = ageDaysFromDobLowBound;
    }

    public Double getAgeDaysFromDobHighBound() {
        return ageDaysFromDobHighBound;
    }

    public void setAgeDaysFromDobHighBound(Double ageDaysFromDobHighBound) {
        this.ageDaysFromDobHighBound = ageDaysFromDobHighBound;
    }

    public String getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(String lifeStage) {
        this.lifeStage = lifeStage;
    }

    public String getExperimentalConditions() {
        return experimentalConditions;
    }

    public void setExperimentalConditions(String experimentalConditions) {
        this.experimentalConditions = experimentalConditions;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public String getExpCondAssocUnits() {
        return expCondAssocUnits;
    }

    public void setExpCondAssocUnits(String expCondAssocUnits) {
        this.expCondAssocUnits = expCondAssocUnits;
    }

    public Double getExpCondAssocValueMin() {
        return expCondAssocValueMin;
    }

    public void setExpCondAssocValueMin(Double expCondAssocValueMin) {
        this.expCondAssocValueMin = expCondAssocValueMin;
    }

    public Double getExpCondAssocValueMax() {
        return expCondAssocValueMax;
    }

    public void setExpCondAssocValueMax(Double expCondAssocValueMax) {
        this.expCondAssocValueMax = expCondAssocValueMax;
    }

    public Double getExpCondDurSecLowBound() {
        return expCondDurSecLowBound;
    }

    public void setExpCondDurSecLowBound(Double expCondDurSecLowBound) {
        this.expCondDurSecLowBound = expCondDurSecLowBound;
    }

    public Double getExpCondDurSecHighBound() {
        return expCondDurSecHighBound;
    }

    public void setExpCondDurSecHighBound(Double expCondDurSecHighBound) {
        this.expCondDurSecHighBound = expCondDurSecHighBound;
    }

    public String getExpCondApplicationMethod() {
        return expCondApplicationMethod;
    }

    public void setExpCondApplicationMethod(String expCondApplicationMethod) {
        this.expCondApplicationMethod = expCondApplicationMethod;
    }

    public String getExpCondNotes() {
        return expCondNotes;
    }

    public void setExpCondNotes(String expCondNotes) {
        this.expCondNotes = expCondNotes;
    }
}
