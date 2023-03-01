package edu.mcw.rgd.datamodel;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 20, 2007
 * Time: 9:26:59 AM
 */

/**
 * Bean class for Nomenclature Events.  Nomenclature events are recored any time
 * the nomenclature of an RGD object is changed.
 *
 * Data is currently contained in the nomen_events table.
 */

public class NomenclatureEvent implements Identifiable, ObjectWithName, ObjectWithSymbol {
    private int originalRGDId;
    private int rgdId;
    private String refKey;
    private String nomenStatusType;
    private String desc;
    private String notes;
    private int nomenEventKey;
    private Date eventDate;
    private String symbol;
    private String name;
    private String previousSymbol;
    private String previousName;


    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getOriginalRGDId() {
        return originalRGDId;
    }

    public void setOriginalRGDId(int originalRGDId) {
        this.originalRGDId = originalRGDId;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public String getNomenStatusType() {
        return nomenStatusType;
    }

    public void setNomenStatusType(String nomenStatusType) {
        this.nomenStatusType = nomenStatusType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getNomenEventKey() {
        return nomenEventKey;
    }

    public void setNomenEventKey(int nomenEventKey) {
        this.nomenEventKey = nomenEventKey;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviousSymbol() {
        return previousSymbol;
    }

    public void setPreviousSymbol(String previousSymbol) {
        this.previousSymbol = previousSymbol;
    }

    public String getPreviousName() {
        return previousName;
    }

    public void setPreviousName(String previousName) {
        this.previousName = previousName;
    }
}
