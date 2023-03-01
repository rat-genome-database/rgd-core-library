package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Nov 23, 2010
 * Time: 1:56:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeqPrimer {

    private int Key;
    private String primerName;
    private String primerDesc;
    private String forwardSeq;
    private String reverseSeq;
    private String notes;
    private String sequenceKey;
    private String expectedSize;
    

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        Key = key;
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

    public String getSequenceKey() {
        return sequenceKey;
    }

    public void setSequenceKey(String sequenceKey) {
        this.sequenceKey = sequenceKey;
    }

    public String getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(String expectedSize) {
        this.expectedSize = expectedSize;
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
}
