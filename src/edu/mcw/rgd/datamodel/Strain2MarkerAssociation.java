package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/21/15
 * Time: 11:23 AM
 * <p>
 * represents strain2sslp associations stored in table RGD_STRAINS_RGD
 */
public class Strain2MarkerAssociation extends Association {

    public int getStrainRgdId() {
        return getMasterRgdId();
    }

    public void setStrainRgdId(int strainRgdId) {
        setMasterRgdId(strainRgdId);
    }

    public int getMarkerRgdId() {
        return getDetailRgdId();
    }

    public void setMarkerRgdId(int markerRgdId) {
        setDetailRgdId(markerRgdId);
    }

    public String getMarkerType() {
        return getAssocType();
    }

    public void setMarkerType(String markerType) {
        setAssocType2(markerType);
    }

    public String getRegionName() {
        return getAssocSubType();
    }

    public void setRegionName(String regionName) {
        setAssocSubType(regionName);
    }

    public String getMarkerSymbol() {
        return getSrcPipeline();
    }

    public void setMarkerSymbol(String markerSymbol) {
        setSrcPipeline2(markerSymbol);
    }
}
