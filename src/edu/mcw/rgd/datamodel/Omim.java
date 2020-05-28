package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

import java.util.Date;

/**
 * @author mtutaj
 * @since Apr 30, 2019
 * Represents a row in OMIM table
 */
public class Omim {
    private String mimNumber;
    private String status; // 'live', etc
    private String mimType; // 'gene','phenotype', etc
    private String phenotype; // if available
    private String approvedGeneSymbols; // HGNC approved gene symbols
    private Date createdDate;
    private Date lastModifiedDate;

    @Override
    public boolean equals(Object obj) {
        Omim o = (Omim) obj;

        return Utils.stringsAreEqual(mimNumber, o.mimNumber)
            && Utils.stringsAreEqual(status, o.status)
            && Utils.stringsAreEqual(mimType, o.mimType)
            && Utils.stringsAreEqual(phenotype, o.phenotype)
            && Utils.stringsAreEqual(approvedGeneSymbols, o.approvedGeneSymbols);
    }

    @Override
    public int hashCode() {
        return Utils.defaultString(mimNumber).hashCode()
            ^ Utils.defaultString(status).hashCode()
            ^ Utils.defaultString(mimType).hashCode()
            ^ Utils.defaultString(phenotype).hashCode()
            ^ Utils.defaultString(approvedGeneSymbols).hashCode();
    }

    public String getMimNumber() {
        return mimNumber;
    }

    public void setMimNumber(String mimNumber) {
        this.mimNumber = mimNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMimType() {
        return mimType;
    }

    public void setMimType(String mimType) {
        this.mimType = mimType;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public String getApprovedGeneSymbols() {
        return approvedGeneSymbols;
    }

    public void setApprovedGeneSymbols(String approvedGeneSymbols) {
        this.approvedGeneSymbols = approvedGeneSymbols;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
