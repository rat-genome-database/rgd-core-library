package edu.mcw.rgd.datamodel.pheno;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 18, 2011
 * Time: 2:06:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClinicalMeasurement {

    private int id;
    private String notes;
    private String accId;
    private String formula;
    private String averageType;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteOntIds() {
        return siteOntIds;
    }

    public void setSiteOntIds(String siteOntIds) {
        this.siteOntIds = siteOntIds;
    }

    private String site;
    private String siteOntIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getAverageType() {
        return averageType;
    }

    public void setAverageType(String averageType) {
        this.averageType = averageType;
    }
}
