package edu.mcw.rgd.datamodel;

public class Project implements Identifiable, ObjectWithName {

    private int rgdId;
    private String desc;
    private String name;

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
}
