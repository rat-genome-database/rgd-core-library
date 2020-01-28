package edu.mcw.rgd.datamodel.pheno;

/**
 * @author hsnalabolu
 * @since 1/23/20
 */
public class Enumerable {

    private int type;
    private String label;
    private String value;
    private String description;
    private Integer ontId;
    private String valueInt;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOntId() {
        return ontId;
    }

    public void setOntId(Integer ontId) {
        this.ontId = ontId;
    }

    public String getValueInt() {
        return valueInt;
    }

    public void setValueInt(String valueInt) {
        this.valueInt = valueInt;
    }
}
