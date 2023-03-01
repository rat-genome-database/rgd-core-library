package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * Represents a gene ortholog -- a row from table GENETOGENE_RGD_ID_RLT
 * - ortholog relation 
 */
public class Ortholog implements Cloneable, Dumpable {

    private int key;       // GENETOGENE_KEY
    private int srcRgdId;  // source gene rgd id
    private int srcSpeciesTypeKey; // species type key for source gene rgd id
    private int destRgdId; // destination gene rgd id
    private int destSpeciesTypeKey; // species type key for dest gene rgd id
    private Integer groupId;
    private String xrefDataSrc;
    private String xrefDataSet;
    private int orthologTypeKey;
    private Double percentHomology;
    private int createdBy;
    private java.util.Date createdDate;
    private int lastModifiedBy;
    private java.util.Date lastModifiedDate;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getSrcRgdId() {
        return srcRgdId;
    }

    public void setSrcRgdId(int srcRgdId) {
        this.srcRgdId = srcRgdId;
    }

    public int getSrcSpeciesTypeKey() {
        return srcSpeciesTypeKey;
    }

    public void setSrcSpeciesTypeKey(int srcSpeciesTypeKey) {
        this.srcSpeciesTypeKey = srcSpeciesTypeKey;
    }

    public int getDestRgdId() {
        return destRgdId;
    }

    public void setDestRgdId(int destRgdId) {
        this.destRgdId = destRgdId;
    }

    public int getDestSpeciesTypeKey() {
        return destSpeciesTypeKey;
    }

    public void setDestSpeciesTypeKey(int destSpeciesTypeKey) {
        this.destSpeciesTypeKey = destSpeciesTypeKey;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getXrefDataSrc() {
        return xrefDataSrc;
    }

    public void setXrefDataSrc(String xrefDataSrc) {
        this.xrefDataSrc = xrefDataSrc;
    }

    public String getXrefDataSet() {
        return xrefDataSet;
    }

    public void setXrefDataSet(String xrefDataSet) {
        this.xrefDataSet = xrefDataSet;
    }

    public int getOrthologTypeKey() {
        return orthologTypeKey;
    }

    public void setOrthologTypeKey(int orthologTypeKey) {
        this.orthologTypeKey = orthologTypeKey;
    }

    public Double getPercentHomology() {
        return percentHomology;
    }

    public void setPercentHomology(Double percentHomology) {
        this.percentHomology = percentHomology;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("KEY", key)
            .put("SRC_RGD_ID", srcRgdId)
            .put("SRC_SPECIES_TYPE_KEY", srcSpeciesTypeKey)
            .put("DEST_RGD_ID", destRgdId)
            .put("DEST_SPECIES_TYPE_KEY", destSpeciesTypeKey)
            .put("GROUP_ID", groupId)
            .put("XREF_DATA_SRC", xrefDataSrc)
            .put("XREF_DATA_SET", xrefDataSet)
            .put("ORTHOLOG_TYPE", orthologTypeKey)
            .put("PERCENT_HOMOLOGY", percentHomology)
            .put("CREATED_BY", createdBy)
            .put("CREATED_DATE", createdDate)
            .put("LAST_MODIFIED_BY", lastModifiedBy)
            .put("LAST_MODIFIED_DATE", lastModifiedDate)
            .dump();
    }
}
