package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

/**
 * @author mtutaj
 * @since 1/28/14
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
    private String srcPipeline;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellLine)) return false;
        if (!super.equals(o)) return false;

        CellLine cellLine = (CellLine) o;

        if (origin != null ? !origin.equals(cellLine.origin) : cellLine.origin != null) return false;
        if (researchUse != null ? !researchUse.equals(cellLine.researchUse) : cellLine.researchUse != null)
            return false;
        if (availability != null ? !availability.equals(cellLine.availability) : cellLine.availability != null)
            return false;
        if (gender != null ? !gender.equals(cellLine.gender) : cellLine.gender != null) return false;
        if (characteristics != null ? !characteristics.equals(cellLine.characteristics) : cellLine.characteristics != null)
            return false;
        if (phenotype != null ? !phenotype.equals(cellLine.phenotype) : cellLine.phenotype != null) return false;
        if (germlineCompetent != null ? !germlineCompetent.equals(cellLine.germlineCompetent) : cellLine.germlineCompetent != null)
            return false;
        return srcPipeline != null ? srcPipeline.equals(cellLine.srcPipeline) : cellLine.srcPipeline == null;
    }

    @Override
    public int hashCode() {
        return super.hashCode()
            ^ Utils.defaultString(origin).hashCode()
            ^ Utils.defaultString(researchUse).hashCode()
            ^ Utils.defaultString(availability).hashCode()
            ^ Utils.defaultString(gender).hashCode()
            ^ Utils.defaultString(characteristics).hashCode()
            ^ Utils.defaultString(phenotype).hashCode()
            ^ Utils.defaultString(germlineCompetent).hashCode()
            ^ Utils.defaultString(srcPipeline).hashCode();
    }

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

    public String getSrcPipeline() {
        return srcPipeline;
    }

    public void setSrcPipeline(String srcPipeline) {
        this.srcPipeline = srcPipeline;
    }
}
