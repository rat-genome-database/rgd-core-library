package edu.mcw.rgd.datamodel;

public class Project implements Identifiable, ObjectWithName {

    private int rgdId;
    private String desc;
    private String name;

    private String sub_name;

    private String princi_name;

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdid) {
        this.rgdId = rgdid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

//   Returning 3 just to display the external resource
    public int getSpeciesTypeKey(){
        return 3;
    }

    public void setPrinci_name(String princi_name) {
        this.princi_name = princi_name;
    }

    public String getPrinci_name() {
        return princi_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }
}
