package edu.mcw.rgd.datamodel;

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
    private Date createdDate;
    private Date lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Omim omim = (Omim) o;

        if (!mimNumber.equals(omim.mimNumber)) return false;
        if (!status.equals(omim.status)) return false;
        if (!mimType.equals(omim.mimType)) return false;
        return phenotype.equals(omim.phenotype);

    }

    @Override
    public int hashCode() {
        int result = mimNumber.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + mimType.hashCode();
        result = 31 * result + phenotype.hashCode();
        return result;
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
