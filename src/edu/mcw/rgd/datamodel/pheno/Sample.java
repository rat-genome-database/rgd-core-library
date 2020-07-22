package edu.mcw.rgd.datamodel.pheno;

/**
 * @author jdepons
 * @since Feb 17, 2011
 */
public class Sample {

    private int id;
    private Integer ageDaysFromHighBound;
    private Integer ageDaysFromLowBound;
    private Integer numberOfAnimals = 0;
    private String notes;
    private String sex;
    private String strainAccId;
    private String tissueAccId;
    private String cellTypeAccId;
    private String cellLineId;
    private String geoSampleAcc;
    private String bioSampleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAgeDaysFromHighBound() {
        return ageDaysFromHighBound;
    }

    public void setAgeDaysFromHighBound(Integer ageDaysFromHighBound) {
        this.ageDaysFromHighBound = ageDaysFromHighBound;
    }

    public Integer getAgeDaysFromLowBound() {
        return ageDaysFromLowBound;
    }

    public void setAgeDaysFromLowBound(Integer ageDaysFromLowBound) {
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
}
