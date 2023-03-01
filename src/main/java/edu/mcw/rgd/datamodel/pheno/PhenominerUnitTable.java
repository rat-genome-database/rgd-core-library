package edu.mcw.rgd.datamodel.pheno;

/**
 * @author kthorat
 * @since Nov 11 2020
 */

public class PhenominerUnitTable {

    private String ontId;
    private String stdUnit;
    private String unitFrom;
    private String unitTo;
    private float termSpecificScale;
    private int zeroOffset;

    //Getters
    public String getOntId() {
        return ontId;
    }
    public String getStdUnit() {
        return stdUnit;
    }
    public String getUnitFrom() {
        return unitFrom;
    }
    public String getUnitTo() {
        return unitTo;
    }
    public float getTermSpecificScale() {
        return termSpecificScale;
    }
    public int getZeroOffset() {
        return zeroOffset;
    }

    //Setters
    public void setOntId(String ontId) {
        this.ontId = ontId;
    }
    public void setStdUnit(String stdUnit) {
        this.stdUnit = stdUnit;
    }
    public void setUnitFrom(String unitFrom) {
        this.unitFrom = unitFrom;
    }
    public void setUnitTo(String unitTo) {
        this.unitTo = unitTo;
    }
    public void setTermSpecificScale(float termSpecificScale) {
        this.termSpecificScale = termSpecificScale;
    }
    public void setZeroOffset(int zeroOffset) {
        this.zeroOffset = zeroOffset;
    }
}
