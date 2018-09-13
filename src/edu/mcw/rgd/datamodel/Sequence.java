package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Nov 23, 2010
 * Time: 1:56:13 PM
 */
public class Sequence implements Identifiable, Dumpable {

    private int rgdId; // rgd id of the sequence
    private int seqKey; // sequence key
    private int seqTypeKey; // sequence type key
    private String seqDesc; // sequence description
    private String notes; // optional notes about sequence
    private int primerKey;
    private String primerName;
    private String primerDesc;
    private String forwardSeq;
    private String reverseSeq;
    private String seqPrimerNotes;
    private int expectedSize;

    // optional for sequence clones
    private String cloneSeq;


    // optional for primer_pair-type sequences

    public int getPrimerKey() {
        return primerKey;
    }

    public void setPrimerKey(int primerKey) {
        this.primerKey = primerKey;
    }





    public String getSeqPrimerNotes() {
        return seqPrimerNotes;
    }

    public void setSeqPrimerNotes(String seqPrimerNotes) {
        this.seqPrimerNotes = seqPrimerNotes;
    }


    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(int seqKey) {
        this.seqKey = seqKey;
    }

    public String getPrimerName() {
            return primerName;
    }

    public void setPrimerName(String primerName) {
        this.primerName = primerName;
    }

    public String getPrimerDesc() {
        return primerDesc;
    }

    public void setPrimerDesc(String primerDesc) {
        this.primerDesc = primerDesc;
    }

    public int getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    public int getSeqTypeKey() {
        return seqTypeKey;
    }

    public void setSeqTypeKey(int seqTypeKey) {
        this.seqTypeKey = seqTypeKey;
    }

    public String getSeqDesc() {
        return seqDesc;
    }

    public void setSeqDesc(String seqDesc) {
        this.seqDesc = seqDesc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getForwardSeq() {
        return forwardSeq;
    }

    public void setForwardSeq(String forwardSeq) {
        this.forwardSeq = forwardSeq;
    }

    public String getReverseSeq() {
        return reverseSeq;
    }

    public void setReverseSeq(String reverseSeq) {
        this.reverseSeq = reverseSeq;
    }

    public String getCloneSeq() {
        return cloneSeq;
    }

    public void setCloneSeq(String cloneSeq) {
        this.cloneSeq = cloneSeq;
    }

    /// returns sequence data
    public String getSeq() {
        return cloneSeq;
    }

    public String dump(String delimiter) {

        // we are skipping null fields
        return new Dumper(delimiter, true, true)
            .put("RGD_ID", rgdId)
            .put("SEQ_KEY", seqKey)
            .put("SEQ_TYPE_KEY", seqTypeKey)
            .put("SEQ_DESC", seqDesc)
            .put("NOTES", notes)
            .put("PRIMER_KEY", primerKey)
            .put("PRIMER_NAME", primerName)
            .put("PRIMER_DESC", primerDesc)
            .put("FORWARD_SEQ", forwardSeq)
            .put("REVERSE_SEQ", reverseSeq)
            .put("SEQ_PRIMER_NOTES", seqPrimerNotes)
            .put("EXPECTED_SIZE", expectedSize)
            .put("CLONE_SEQ", cloneSeq)
            .dump();
    }
}
