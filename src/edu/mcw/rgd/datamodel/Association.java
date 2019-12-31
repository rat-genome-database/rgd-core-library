package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mtutaj
 * @since 10/27/11
 * an association between two RGD objects; represents one row from RGD_ASSOCIATIONS table
 */
public class Association implements Dumpable, Cloneable, Serializable {

    private static final long serialVersionUID = 1L; // recommended for serializable classes

    private int assocKey;      // unique association key -- taken from RGD_ASSOCIATIONS_SEQ sequence
    private String assocType;  // mandatory type of association
    private String assocSubType; // optional association subtype
    private int masterRgdId;
    private int detailRgdId;
    private java.util.Date creationDate;
    private String srcPipeline;

    /**
     * dump object state to a string
     * @param delimiter delimiting character(s), like ' ', '\t', '|' etc
     * @return delimited dump of object state
     */
    public String dump(String delimiter) {

        return new Dumper(delimiter, true, true)
            .put("ASSOC_KEY", assocKey)
            .put("ASSOC_TYPE", assocType)
            .put("ASSOC_SUBTYPE", assocSubType)
            .put("MASTER_RGD_ID", masterRgdId)
            .put("DETAIL_RGD_ID", detailRgdId)
            .put("CREATION_DATE", creationDate)
            .put("SRC_PIPELINE", srcPipeline)
            .dump();
    }

    /**
     * two associations are equal, if they have same type, master rgd id, detail rgd id and src_pipeline
     * @param obj another Association object to compare for equality
     * @return true if given association object is equal to this object
     */
    @Override
    public boolean equals(Object obj) {

        Association assoc = (Association) obj;
        return  this.masterRgdId == assoc.masterRgdId
             && this.detailRgdId == assoc.detailRgdId
             && Utils.stringsAreEqual(this.assocType, assoc.assocType)
             && Utils.stringsAreEqual(this.srcPipeline, assoc.srcPipeline);
    }

    @Override
    public int hashCode() {
        return  this.masterRgdId
             ^ this.detailRgdId
             ^ Utils.defaultString(this.assocType).hashCode()
             ^ Utils.defaultString(this.srcPipeline).hashCode();
    }

    /**
     * two associations are equal with subtype, if they have same type, subtype, master rgd id, detail rgd id and src_pipeline
     * @param obj another Association object to compare for equality
     * @return true if given association object is equal to this object
     */
    public boolean equalsWithSubType(Object obj) {

        Association assoc = (Association) obj;
        return  this.masterRgdId == assoc.masterRgdId
             && this.detailRgdId == assoc.detailRgdId
             && Utils.stringsAreEqual(this.assocType, assoc.assocType)
             && Utils.stringsAreEqual(this.assocSubType, assoc.assocSubType)
             && Utils.stringsAreEqual(this.srcPipeline, assoc.srcPipeline);
    }

    @Override
    public String toString() {
        return dump("|");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getAssocKey() {
        return assocKey;
    }

    public void setAssocKey(int assocKey) {
        this.assocKey = assocKey;
    }

    public String getAssocType() {
        return assocType;
    }

    public void setAssocType(String assocType) {
        if( assocType!=null )
            this.assocType = assocType.toLowerCase();
        else
            this.assocType = assocType;
    }

    public void setAssocType2(String assocType) {
        this.assocType = assocType;
    }

    public String getAssocSubType() {
        return assocSubType;
    }

    public void setAssocSubType(String assocSubType) {
        this.assocSubType = assocSubType;
    }

    public int getMasterRgdId() {
        return masterRgdId;
    }

    public void setMasterRgdId(int masterRgdId) {
        this.masterRgdId = masterRgdId;
    }

    public int getDetailRgdId() {
        return detailRgdId;
    }

    public void setDetailRgdId(int detailRgdId) {
        this.detailRgdId = detailRgdId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSrcPipeline() {
        return srcPipeline;
    }

    public void setSrcPipeline(String srcPipeline) {
        if( srcPipeline!=null )
            this.srcPipeline = srcPipeline.toUpperCase();
        else
            this.srcPipeline = srcPipeline;
    }

    public void setSrcPipeline2(String srcPipeline) {
        this.srcPipeline = srcPipeline;
    }
}
