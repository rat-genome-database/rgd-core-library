package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 6/26/14
 * Time: 3:06 PM
 * Represents a join of PROTEIN_STRUCTURES and PROTEIN_STRUCTURE_GENES tables
 */
public class ProteinStructure {

    private int key;
    private String name;
    private String modeller;
    private String proteinAccId;
    private String proteinAaRange;
    private int rgdId;
    private String videoUrl;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModeller() {
        return modeller;
    }

    public void setModeller(String modeller) {
        this.modeller = modeller;
    }

    public String getProteinAccId() {
        return proteinAccId;
    }

    public void setProteinAccId(String proteinAccId) {
        this.proteinAccId = proteinAccId;
    }

    public String getProteinAaRange() {
        return proteinAaRange;
    }

    public void setProteinAaRange(String proteinAaRange) {
        this.proteinAaRange = proteinAaRange;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
