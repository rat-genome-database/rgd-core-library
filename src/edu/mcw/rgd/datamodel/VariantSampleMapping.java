package edu.mcw.rgd.datamodel;



/**
 * @author jdepons
 * @since Dec 21, 2007
 * Bean class for an RGD Alias.
 * <p>
 * Data is currently contained in the aliases table
 */
public class VariantSampleMapping {

    private int strainRgdId;
    private String groupName;
    private String subGroupName;

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }
}
