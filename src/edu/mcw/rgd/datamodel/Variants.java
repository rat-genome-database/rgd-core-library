package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 7, 2018
 * Time: 8:24:35 AM
 * <p>
 * represents a row from Variants table
 */
public class Variants implements Identifiable, Speciated, ObjectWithName {

    private int rgdId;
    private String name;
    private String description;
    private String type;
    private String ref_nuc;
    private String var_nuc;
    private String notes;
    private String last_modified_date;
    private int speciesTypeKey;

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

    public String getRef_nuc() {
        return ref_nuc;
    }

    public void setRef_nuc(String ref_nuc) {
        this.ref_nuc = ref_nuc;
    }

    public String getVar_nuc() {
        return var_nuc;
    }

    public void setVar_nuc(String var_nuc) {
        this.var_nuc = var_nuc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }
}
