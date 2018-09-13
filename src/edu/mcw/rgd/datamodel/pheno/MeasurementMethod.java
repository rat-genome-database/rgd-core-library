package edu.mcw.rgd.datamodel.pheno;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 18, 2011
 * Time: 2:06:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeasurementMethod {

    private int id;
    private String duration;
    private String notes;
    private String accId;
    private String site;
    private String piType;
    private Integer piTimeValue;
    private String piTypeUnit;

    public String getSiteOntIds() {
        return siteOntIds;
    }

    public void setSiteOntIds(String siteOntIds) {
        this.siteOntIds = siteOntIds;
    }

    private String siteOntIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPiType() {
        return piType;
    }

    public void setPiType(String piType) {
        this.piType = piType;
    }

    public Integer getPiTimeValue() {
        return piTimeValue;
    }

    public void setPiTimeValue(Integer piTimeValue) {
        this.piTimeValue = piTimeValue;
    }

    public String getPiTypeUnit() {
        return piTypeUnit;
    }

    public void setPiTypeUnit(String piTypeUnit) {
        this.piTypeUnit = piTypeUnit;
    }
}
