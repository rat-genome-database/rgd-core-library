package edu.mcw.rgd.datamodel.variants;

public class VariantSSId {
    private int variantRgdId;
    private String ssId;
    private int strainRgdId;

    public int getVariantRgdId() {
        return variantRgdId;
    }

    public void setVariantRgdId(int variantRgdId) {
        this.variantRgdId = variantRgdId;
    }

    public String getSSId() {
        return ssId;
    }

    public void setSSId(String ssId) {
        this.ssId = ssId;
    }

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }
}
