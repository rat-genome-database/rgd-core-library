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

    private String traitOntId;
    private String traitOntId2;
    private String traitOntId3;

<<<<<<< HEAD
    public String getTraitOntId2() {
        return traitOntId2;
    }

    public void setTraitOntId2(String traitOntId2) {
        this.traitOntId2 = traitOntId2;
    }
=======
//    public String getTraitOntId() throws Exception{
//        return traitOntId;
//        List<String> onts = this.getTraitOntIds();
//
//        if (onts.size()>0) {
//            return onts.get(0);
//        }else {
//            return "";
//        }
//    }
public String getTraitOntId() {
    return traitOntId;
}
public void setTraitOntId(String traitOntId) {
    this.traitOntId = traitOntId;
}

    private String traitOntId;
//    public void setTraitOntId(String ontId) throws Exception {
//        this.traitOntId = traitOntId;
//    }
//        ArrayList<String> onts = new ArrayList<String>();
//        onts.add(ontId);
//
//        PhenominerDAO pdao = new PhenominerDAO();
//        pdao.updateExperimentTraits(this.getId(),onts);
//    }
>>>>>>> aadc3f40bceac60213d326ecf3ceb067cb2cdf7c

    public String getTraitOntId3() {
        return traitOntId3;
    }

    public void setTraitOntId3(String traitOntId3) {
        this.traitOntId3 = traitOntId3;
    }

    public String getTraitOntId() {
        return traitOntId;
    }

    public void setTraitOntId(String traitOntId) {
        this.traitOntId = traitOntId;
    }

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
