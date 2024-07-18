package edu.mcw.rgd.datamodel;

public class HrdpPortalAvailability {

    private int primaryStrainId;
    private String availableStrainId;

    private String subGroupName;

    public int getPrimaryStrainId() {
        return primaryStrainId;
    }

    public void setPrimaryStrainId(int primaryStrainId) {
        this.primaryStrainId = primaryStrainId;
    }

    public String getAvailableStrainId() {
        return availableStrainId;
    }

    public void setAvailableStrainId(String availableStrainId) {
        this.availableStrainId = availableStrainId;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }
}
