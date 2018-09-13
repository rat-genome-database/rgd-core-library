package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Apr 16, 2009
 * Time: 9:04:03 AM
 */
public class Note implements Identifiable, Cloneable {

    private int key;
    private String notes;
    private Date creationDate;
    private int rgdId;
    private String notesTypeName;
    private String publicYN;
    private Integer refId;

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getNotesTypeName() {
        return notesTypeName;
    }

    public void setNotesTypeName(String notesTypeName) {
        this.notesTypeName = notesTypeName;
    }

    public String getPublicYN() {
        return publicYN;
    }

    public void setPublicYN(String publicYN) {
        this.publicYN = publicYN;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
