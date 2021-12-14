package edu.mcw.rgd.datamodel.pheno;

/**
 * @author kthorat
 * @since Nov 11 2020
 */

public class phenominerEnumTable {

    private int type;
    private String label;
    private String value;
    private String description;

    //Getters
    public int getType() {
        return type;
    }
    public String getLabel() {
        return label;
    }
    public String getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }

    //Setters
    public void setType(int type) {
        this.type = type;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}
