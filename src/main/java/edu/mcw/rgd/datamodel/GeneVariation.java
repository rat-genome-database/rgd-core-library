package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 3/11/13
 * Time: 2:03 PM
 * <p>
 * represents a variation between parent gene object and a gene variation (splice, allele)
 */
public class GeneVariation implements Identifiable, Speciated {

    private int key; // gene key
    private int rgdId; // gene rgd id
    private String geneVariationType;
    private int variationKey; // gene variation key
    private int variationRgdId; // gene variation rgd id
    private String notes;
    private int speciesTypeKey;

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getGeneVariationType() {
        return geneVariationType;
    }

    public void setGeneVariationType(String geneVariationType) {
        this.geneVariationType = geneVariationType;
    }

    public int getVariationKey() {
        return variationKey;
    }

    public void setVariationKey(int variationKey) {
        this.variationKey = variationKey;
    }

    public int getVariationRgdId() {
        return variationRgdId;
    }

    public void setVariationRgdId(int variationRgdId) {
        this.variationRgdId = variationRgdId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
