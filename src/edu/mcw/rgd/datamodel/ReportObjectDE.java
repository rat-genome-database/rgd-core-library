package edu.mcw.rgd.datamodel;

public class ReportObjectDE {
    private long rgdId;
    private String objectType;
    private String symbol;
    private String name;
    private String species;
    private String fileName;

    public long getRgdId() {
        return rgdId;
    }

    public void setRgdId(long rgdId) {
        this.rgdId = rgdId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
