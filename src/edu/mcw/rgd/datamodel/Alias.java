package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 21, 2007
 * Time: 9:28:50 AM
 * <p>
 * Bean class for an RGD Alias.
 * <p>
 * Data is currently contained in the aliases table
 */
public class Alias implements Identifiable, Speciated, Dumpable {

    private int key;
    private String typeName;
    private String value;
    private String notes;
    private int rgdId;
    private int speciesTypeKey; // available in most API methods

    /**
     * two aliases are equal if they have some alias type, alias value and rgd_id
     * @param o another Alias object
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alias)) return false;

        Alias alias = (Alias) o;

        if (rgdId != alias.rgdId) return false;
        if (typeName != null ? !typeName.equals(alias.typeName) : alias.typeName != null) return false;
        if (value != null ? !value.equals(alias.value) : alias.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + rgdId;
        return result;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    /**
     * dumps object attributes as pipe delimited string
     * @return pipe-delimited String of object attributes
     */
    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("ALIAS_KEY", getKey())
            .put("ALIAS_TYPE", getTypeName())
            .put("ALIAS_VALUE", getValue())
            .put("RGD_ID", getRgdId())
            .put("SPECIES_TYPE_KEY", getSpeciesTypeKey())
            .put("NOTES", getNotes())
            .dump();
    }
}
