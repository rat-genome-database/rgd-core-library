package edu.mcw.rgd.datamodel.pheno;

/**
 * @author kthorat
 * @since Dec 1 2020
 */


public class phenominerNoStdUnitTable {

    private String ontId;
    private String term;
    private String measurementUnit;

    public String getOntId() {
        return ontId;
    }
    public String getTerm() {
        return term;
    }
    public String getMeasurementUnit() {
        return measurementUnit;
    }


    public void setOntId(String ontId) {
        this.ontId = ontId;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
}
