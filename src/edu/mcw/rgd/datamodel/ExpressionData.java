package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

/**
 * @author mtutaj
 * @since 2/22/12
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
    private String source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
            .put("SOURCE", source)
            .put("NOTES", notes)
            .dump();
    }

    @Override
    public boolean equals(Object obj) {
        ExpressionData o = (ExpressionData) obj;
        return rgdId==o.rgdId
            && Utils.stringsAreEqual(tissue, o.tissue)
            && Utils.stringsAreEqual(transcripts, o.transcripts)
            && Utils.doublesAreEqual(chipSeqReadDensity, o.chipSeqReadDensity, 2)
            && Utils.stringsAreEqual(experimentMethods, o.experimentMethods)
            && Utils.stringsAreEqual(regulation, o.regulation)
            && Utils.stringsAreEqual(tissueTermAcc, o.tissueTermAcc)
            && Utils.stringsAreEqual(strainTermAcc, o.strainTermAcc)
            && Utils.stringsAreEqual(source, o.source);

    }

    @Override
    public int hashCode() {
        int result = rgdId
            ^ (tissue != null ? tissue.hashCode() : 0)
            ^ (transcripts != null ? transcripts.hashCode() : 0)
            ^ (chipSeqReadDensity != null ? chipSeqReadDensity.hashCode() : 0)
            ^ (experimentMethods != null ? experimentMethods.hashCode() : 0)
            ^ (regulation != null ? regulation.hashCode() : 0)
            ^ (tissueTermAcc != null ? tissueTermAcc.hashCode() : 0)
            ^ (strainTermAcc != null ? strainTermAcc.hashCode() : 0)
            ^ (source != null ? source.hashCode() : 0);
        return result;
    }
}
