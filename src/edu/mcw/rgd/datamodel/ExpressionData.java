package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 2/22/12
 * Time: 9:28 AM
 * represents a row in EXPRESSION_DATA table
 */
public class ExpressionData implements Identifiable, Dumpable {

    private long key;
    private int rgdId;
    private String tissue;
    private String transcripts;
    private Double chipSeqReadDensity;
    private String experimentMethods;
    private String regulation;
    private String tissueTermAcc; // MA acc id
    private String strainTermAcc; // RS acc id
    private String absCall; // the call in an absolute analysis that indicates if the transcript was present (P), absent (A), marginal (M), or no call (NC)
    private String geoAccId;
    private String notes;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(String transcripts) {
        this.transcripts = transcripts;
    }

    public Double getChipSeqReadDensity() {
        return chipSeqReadDensity;
    }

    public void setChipSeqReadDensity(Double chipSeqReadDensity) {
        this.chipSeqReadDensity = chipSeqReadDensity;
    }

    public String getExperimentMethods() {
        return experimentMethods;
    }

    public void setExperimentMethods(String experimentMethods) {
        this.experimentMethods = experimentMethods;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public String getTissueTermAcc() {
        return tissueTermAcc;
    }

    public void setTissueTermAcc(String tissueTermAcc) {
        this.tissueTermAcc = tissueTermAcc;
    }

    public String getStrainTermAcc() {
        return strainTermAcc;
    }

    public void setStrainTermAcc(String strainTermAcc) {
        this.strainTermAcc = strainTermAcc;
    }

    public String getAbsCall() {
        return absCall;
    }

    public void setAbsCall(String absCall) {
        this.absCall = absCall;
    }

    public String getGeoAccId() {
        return geoAccId;
    }

    public void setGeoAccId(String geoAccId) {
        this.geoAccId = geoAccId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("RGD_ID", rgdId)
            .put("TISSUE", tissue)
            .put("TRANSCRIPTS", transcripts)
            .put("CHIP_SEQ_READ_DENSITY", chipSeqReadDensity)
            .put("EXPERIMENT_METHODS", experimentMethods)
            .put("REGULATION", regulation)
            .put("TISSUE_TERM_ACC", tissueTermAcc)
            .put("STRAIN_TERM_ACC", strainTermAcc)
            .put("ABS_CALL", absCall)
            .put("GEO_ACC_ID", geoAccId)
            .put("NOTES", notes)
            .dump();
    }
}
