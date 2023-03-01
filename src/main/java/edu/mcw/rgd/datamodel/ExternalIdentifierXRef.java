package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 23, 2008
 * Time: 10:22:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalIdentifierXRef implements Identifiable {

    private int rgdId;
    private String exId;
    private int speciesTypeKey;
    private String objectType;
    private String idType;

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getExId() {
        return exId;
    }

    public void setExId(String exId) {
        this.exId = exId;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}
