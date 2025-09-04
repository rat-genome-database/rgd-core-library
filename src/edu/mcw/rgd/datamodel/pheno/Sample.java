package edu.mcw.rgd.datamodel.pheno;

/**
 * @author jdepons
 * @since Feb 17, 2011
 */
public class Sample {

    private int id;
    private Double ageDaysFromHighBound;
    private Double ageDaysFromLowBound;
    private Integer numberOfAnimals = 0;
    private String notes;
    private String sex;
    private String strainAccId;
    private String tissueAccId;
    private String cellTypeAccId;
    private String strainTerm;
    private String tissueTerm;
    private String cellTypeTerm;
    private String traitTerm;
    private String measurementTerm;
    private String experimentCondition;
    private String cellLineId;
    private String geoSampleAcc;
    private String bioSampleId;
    private String lastModifiedBy;
    private String createdBy;
    private String lifeStage;
    private String curatorNotes;
    private Integer cultureDur;
    private String cultureDurUnit;
    private String computedSex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAgeDaysFromHighBound() {
        return ageDaysFromHighBound;
    }

    public void setAgeDaysFromHighBound(Double ageDaysFromHighBound) {
        this.ageDaysFromHighBound = ageDaysFromHighBound;
    }

    public Double getAgeDaysFromLowBound() {
        return ageDaysFromLowBound;
    }

    public void setAgeDaysFromLowBound(Double ageDaysFromLowBound) {
        this.ageDaysFromLowBound = ageDaysFromLowBound;
    }

    public Integer getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStrainAccId() {
        return strainAccId;
    }

    public void setStrainAccId(String strainAccId) {
        this.strainAccId = strainAccId;
    }

    public String getTissueAccId() {
        return tissueAccId;
    }

    public void setTissueAccId(String tissueAccId) {
        this.tissueAccId = tissueAccId;
    }

    public String getCellTypeAccId() {
        return cellTypeAccId;
    }

    public void setCellTypeAccId(String cellTypeAccId) {
        this.cellTypeAccId = cellTypeAccId;
    }

    public String getCellLineId() {
        return cellLineId;
    }

    public void setCellLineId(String cellLineId) {
        this.cellLineId = cellLineId;
    }

    public String getGeoSampleAcc() {
        return geoSampleAcc;
    }

    public void setGeoSampleAcc(String geoSampleAcc) {
        this.geoSampleAcc = geoSampleAcc;
    }

    public String getBioSampleId() {
        return bioSampleId;
    }

    public void setBioSampleId(String bioSampleId) {
        this.bioSampleId = bioSampleId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(String lifeStage) {
        this.lifeStage = lifeStage;
    }

    public String getCuratorNotes() {
        return curatorNotes;
    }

    public void setCuratorNotes(String curatorNotes) {
        this.curatorNotes = curatorNotes;
    }

    public Integer getCultureDur() {
        return cultureDur;
    }

    public void setCultureDur(Integer cultureDur) {
        this.cultureDur = cultureDur;
    }

    public String getCultureDurUnit() {
        return cultureDurUnit;
    }

    public void setCultureDurUnit(String cultureDurUnit) {
        this.cultureDurUnit = cultureDurUnit;
    }

    public String getComputedSex() {
        return computedSex;
    }

    public void setComputedSex(String computedSex) {
        this.computedSex = computedSex;
    }

    public String getStrainTerm() {
        return strainTerm;
    }

    public void setStrainTerm(String strainTerm) {
        this.strainTerm = strainTerm;
    }

    public String getTissueTerm() {
        return tissueTerm;
    }

    public void setTissueTerm(String tissueTerm) {
        this.tissueTerm = tissueTerm;
    }

    public String getCellTypeTerm() {
        return cellTypeTerm;
    }

    public void setCellTypeTerm(String cellTypeTerm) {
        this.cellTypeTerm = cellTypeTerm;
    }

    public String getTraitTerm() {
        return traitTerm;
    }

    public void setTraitTerm(String traitTerm) {
        this.traitTerm = traitTerm;
    }

    public String getMeasurementTerm() {
        return measurementTerm;
    }

    public void setMeasurementTerm(String measurementTerm) {
        this.measurementTerm = measurementTerm;
    }

    public String getExperimentCondition() {
        return experimentCondition;
    }

    public void setExperimentCondition(String experimentCondition) {
        this.experimentCondition = experimentCondition;
    }
}
