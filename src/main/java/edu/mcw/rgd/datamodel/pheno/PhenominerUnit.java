package edu.mcw.rgd.datamodel.pheno;

/**
 * @author kthorat
 * @since Oct 23 2020
 */
public class PhenominerUnit {
    private String cmoId;
    private String pTerm;
    private String measurementUnit;
    private int experimentId;
    private int experimentRecordId;
    private int studyId;
    private Integer pRefRgdId;
    private String stdUnit;

    //Getters
    public String getCmoId() {
        return cmoId;
    }

    public String getpTerm() {
        return pTerm;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public int getStudyId() {
        return studyId;
    }

    public Integer getpRefRgdId() {
        return pRefRgdId;
    }

    public String getStdUnit() {
        return stdUnit;
    }

    public int getExperimentRecordId() { return experimentRecordId; }


    //Setters
    public void setCmoId(String cmoId) {
        this.cmoId = cmoId;
    }

    public void setpTerm(String pTerm) {
        this.pTerm = pTerm;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public void setpRefRgdId(Integer pRefRgdId) {
        this.pRefRgdId = pRefRgdId;
    }

    public void setStdUnit(String stdUnit) {
        this.stdUnit = stdUnit;
    }

    public void setExperimentRecordId(int experimentRecordId) {
        this.experimentRecordId = experimentRecordId;
    }
}