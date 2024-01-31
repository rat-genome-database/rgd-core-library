package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 7, 2018
 * Time: 8:24:35 AM
 * <p>
 * represents a row from Variants table
 */
public class RgdVariant implements Identifiable, Speciated, ObjectWithName {

    private int rgdId;
    private String name;
    private String description;
    private String type;
    private String refNuc;
    private String varNuc;
    private String notes;
    private int speciesTypeKey;
    private String soAccId;

    public String getSoAccId() {
        return soAccId;
    }

    public void setSoAccId(String soAccId) {
        this.soAccId = soAccId;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefNuc() {
        return refNuc;
    }

    public void setRefNuc(String refNuc) {
        this.refNuc = refNuc;
    }

    public String getVarNuc() {
        return varNuc;
    }

    public void setVarNuc(String varNuc) {
        this.varNuc = varNuc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }
}
