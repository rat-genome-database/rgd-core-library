package edu.mcw.rgd.datamodel.pheno;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 2/7/11
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class Experiment {

    private int id;
    private String name;
    private String notes;
    private int studyId;
    private int curationStatus = -1;
    private String lastModifiedBy;
    private String createdBy;


    public String getTraitOntId() {
        return traitOntId;
    }

    public void setTraitOntId(String traitOntId) {
        this.traitOntId = traitOntId;
    }

    private String traitOntId;

    public int getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(int curationStatus) {
        this.curationStatus = curationStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
