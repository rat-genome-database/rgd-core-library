package edu.mcw.rgd.datamodel.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 9/13/13
 * Time: 5:11 PM
 * represents rows of EVIDENCE_CODES table
 */
public class Evidence {

    private String evidence;
    private String name;
    private String description;
    private String manualCuration; // NULL, or 'same_species' or 'other_species'
    private String ecoId; // ECO ontology id

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManualCuration() {
        return manualCuration;
    }

    public void setManualCuration(String manualCuration) {
        this.manualCuration = manualCuration;
    }

    public String getEcoId() {
        return ecoId;
    }

    public void setEcoId(String ecoId) {
        this.ecoId = ecoId;
    }
}
