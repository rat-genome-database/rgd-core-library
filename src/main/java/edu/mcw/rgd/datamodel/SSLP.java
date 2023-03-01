package edu.mcw.rgd.datamodel;

/**
 * @author jdepons
 * @since May 19, 2008
 */
public class SSLP implements Identifiable, Speciated, ObjectWithName {
    private int key;
    private String name;
    private Integer expectedSize;
    private String notes;
    private int rgdId;
    private String sslpType;
    private int speciesTypeKey;
    private String templateSeq;
    private String forwardSeq;
    private String reverseSeq;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(Integer expectedSize) {
        this.expectedSize = expectedSize;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgd_id) {
        this.rgdId = rgd_id;
    }

    public String getSslpType() {
        return sslpType;
    }

    public void setSslpType(String sslpType) {
        this.sslpType = sslpType;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getTemplateSeq() {
        return templateSeq;
    }

    public void setTemplateSeq(String templateSeq) {
        this.templateSeq = templateSeq;
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
