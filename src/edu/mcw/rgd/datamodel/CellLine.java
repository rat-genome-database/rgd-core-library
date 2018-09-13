package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 1/28/14
 * Time: 3:33 PM
 * <p>
 * data model for cell line, a genomic element object
 */
public class CellLine extends GenomicElement {

    private String origin;
    private String researchUse;
    private String availability;
    private String gender;
    private String characteristics;
    private String phenotype;
    private String germlineCompetent;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getResearchUse() {
        return researchUse;
    }

    public void setResearchUse(String researchUse) {
        this.researchUse = researchUse;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public String getGermlineCompetent() {
        return germlineCompetent;
    }

    public void setGermlineCompetent(String germlineCompetent) {
        this.germlineCompetent = germlineCompetent;
    }
}
