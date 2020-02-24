package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

/**
 * @author jdepons
 * @since Dec 21, 2007
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

        return rgdId == alias.rgdId
            && Utils.stringsAreEqual(typeName, alias.typeName)
            && Utils.stringsAreEqualIgnoreCase(value, alias.value);

        // note: due to DB constraint, no two synonyms can have the same lowercase value
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (value != null ? value.toLowerCase().hashCode() : 0);
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

        return new Dumper(delimiter, true, true)
            .put("ALIAS_KEY", getKey())
            .put("ALIAS_TYPE", getTypeName())
            .put("ALIAS_VALUE", getValue())
            .put("RGD_ID", getRgdId())
            .put("SPECIES_TYPE_KEY", getSpeciesTypeKey())
            .put("NOTES", getNotes())
            .dump();
    }
}
