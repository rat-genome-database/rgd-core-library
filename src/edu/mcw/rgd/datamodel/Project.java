package edu.mcw.rgd.datamodel;

public class Project {

    private int rgdid;
    private String desc;
    private String name;

    public int getRgdid() {
        return rgdid;
    }

    public void setRgdid(int rgdid) {
        this.rgdid = rgdid;
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

    public int getSpeciesTypeKey(){
        return 0;
    }

}
