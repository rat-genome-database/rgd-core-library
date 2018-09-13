package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:54:44 PM
 */
public class SSLP implements Identifiable, Speciated, ObjectWithName {
    private int key;
    private String name;
    private Integer expectedSize;
    private String notes;
    private int rgdId;
    private String sslpType;
    private int speciesTypeKey;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(Integer expectedSize) {
        this.expectedSize = expectedSize;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgd_id) {
        this.rgdId = rgd_id;
    }

    public String getSslpType() {
        return sslpType;
    }

    public void setSslpType(String sslpType) {
        this.sslpType = sslpType;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }
}
