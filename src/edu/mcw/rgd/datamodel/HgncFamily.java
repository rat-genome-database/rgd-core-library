package edu.mcw.rgd.datamodel;

/**
 * Bean class for a gene family.
 * <p>
 * Data corresponds to the family table.
 */
public class HgncFamily {

    private int familyId;
    private String abbreviation;
    private String name;
    private String externalNote;
    private String pubmedIds;
    private String descComment;
    private String descLabel;
    private String descSource;
    private String descGo;
    private String typicalGene;

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalNote() {
        return externalNote;
    }

    public void setExternalNote(String externalNote) {
        this.externalNote = externalNote;
    }

    public String getPubmedIds() {
        return pubmedIds;
    }

    public void setPubmedIds(String pubmedIds) {
        this.pubmedIds = pubmedIds;
    }

    public String getDescComment() {
        return descComment;
    }

    public void setDescComment(String descComment) {
        this.descComment = descComment;
    }

    public String getDescLabel() {
        return descLabel;
    }

    public void setDescLabel(String descLabel) {
        this.descLabel = descLabel;
    }

    public String getDescSource() {
        return descSource;
    }

    public void setDescSource(String descSource) {
        this.descSource = descSource;
    }

    public String getDescGo() {
        return descGo;
    }

    public void setDescGo(String descGo) {
        this.descGo = descGo;
    }

    public String getTypicalGene() {
        return typicalGene;
    }

    public void setTypicalGene(String typicalGene) {
        this.typicalGene = typicalGene;
    }
}
