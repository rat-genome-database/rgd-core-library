package edu.mcw.rgd.datamodel;

import java.io.Serializable;

/**
 * @author jdepons
 * Date: Aug 31, 2009
 */
public class Sample implements Serializable {

    private int id;
    private String analystDate;
    private String analysisName;
    private String description;
    private String directory;
    private String sequencer;

    private int patientId;
    private String gender;

    private String grantNumber;
    private String sequencedBy;
    private String whereBred;
    private String secondaryAnalysisSoftware;
    private int mapKey;
    private String dbSnpSource;
    private int strainRgdId;
    private int refRgdId;

    public String getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber) {
        this.grantNumber = grantNumber;
    }

    public String getSequencedBy() {
        return sequencedBy;
    }

    public void setSequencedBy(String sequencedBy) {
        this.sequencedBy = sequencedBy;
    }

    public String getWhereBred() {
        return whereBred;
    }

    public void setWhereBred(String whereBred) {
        this.whereBred = whereBred;
    }

    public String getSecondaryAnalysisSoftware() {
        return secondaryAnalysisSoftware;
    }

    public void setSecondaryAnalysisSoftware(String secondaryAnalysisSoftware) {
        this.secondaryAnalysisSoftware = secondaryAnalysisSoftware;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getAnalystDate() {
        return analystDate;
    }

    public void setAnalystDate(String analystDate) {
        this.analystDate = analystDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getDbSnpSource() {
        return dbSnpSource;
    }

    public void setDbSnpSource(String dbSnpSource) {
        this.dbSnpSource = dbSnpSource;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getSequencer() {
        return sequencer;
    }

    public void setSequencer(String sequencer) {
        this.sequencer = sequencer;
    }

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }

    public int getRefRgdId() {
        return refRgdId;
    }

    public void setRefRgdId(int refRgdId) {
        this.refRgdId = refRgdId;
    }
}
