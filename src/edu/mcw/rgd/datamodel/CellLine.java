package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
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
    private String caution;
    private String groups;
    private String citationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;

        CellLine that = (CellLine) o;

        return Utils.stringsAreEqual(this.origin, that.origin)
            && Utils.stringsAreEqual(this.researchUse, that.researchUse)
            && Utils.stringsAreEqual(this.availability, that.availability)
            && Utils.stringsAreEqual(this.gender, that.gender)
            && Utils.stringsAreEqual(this.characteristics, that.characteristics)
            && Utils.stringsAreEqual(this.phenotype, that.phenotype)
            && Utils.stringsAreEqual(this.germlineCompetent, that.germlineCompetent)
            && Utils.stringsAreEqual(this.srcPipeline, that.srcPipeline)
            && Utils.stringsAreEqual(this.caution, that.caution)
            && Utils.stringsAreEqual(this.groups, that.groups)
            && Utils.stringsAreEqual(this.citationId, that.citationId);
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
            ^ Utils.defaultString(srcPipeline).hashCode()
            ^ Utils.defaultString(caution).hashCode()
            ^ Utils.defaultString(groups).hashCode()
            ^ Utils.defaultString(citationId).hashCode();
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

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getCitationId() {
        return citationId;
    }

    public void setCitationId(String citationId) {
        this.citationId = citationId;
    }

    public String dump(String delimiter) {

        Dumper dumper = new Dumper(delimiter);
        super.populateDumper(dumper);
        this.populateDumper(dumper);
        return dumper.dump();
    }

    protected void populateDumper(Dumper dumper) {
        dumper
            .put("CITATION_ID", citationId)
            .put("ORIGIN", origin)
            .put("RESEARCH_USE", researchUse)
            .put("AVAILABILITY", availability)
            .put("GENDER", gender)
            .put("CHARACTERISTICS", characteristics)
            .put("PHENOTYPE", phenotype)
            .put("GERMLINE_COMPETENT", germlineCompetent)
            .put("SRC_PIPELINE", srcPipeline)
            .put("CAUTION", caution)
            .put("GROUPS", groups);
    }
}
