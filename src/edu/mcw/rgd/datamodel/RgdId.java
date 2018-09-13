package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * @author jdepons
 * @since Dec 20, 2007
 * <p>
 * Contains information about an RGD ID.  Data is stored in the RGD_IDS table.
 */
public class RgdId implements Identifiable, Speciated {

    // for your convenience: copied from RGD_OBJECTS table as of Feb 2014
    static public final int OBJECT_KEY_CDSS = 18;
    static public final int OBJECT_KEY_CELL_LINES = 11;
    static public final int OBJECT_KEY_CHROMOSOME = 22;
    static public final int OBJECT_KEY_EXONS = 15;
    static public final int OBJECT_KEY_EXPERIMENTS = 14;
    static public final int OBJECT_KEY_GENES = 1;
    static public final int OBJECT_KEY_INTRONS = 19;
    static public final int OBJECT_KEY_MAPS = 10;
    static public final int OBJECT_KEY_PROMOTERS = 16;
    static public final int OBJECT_KEY_PROTEINS = 8;
    static public final int OBJECT_KEY_PROTEIN_DOMAINS = 23;
    static public final int OBJECT_KEY_REFERENCES = 12;
    static public final int OBJECT_KEY_QTLS = 6;
    static public final int OBJECT_KEY_SEQUENCES = 9;
    static public final int OBJECT_KEY_SSLPS = 3;
    static public final int OBJECT_KEY_STRAINS = 5;
    static public final int OBJECT_KEY_TRANSCRIPTS = 21;
    static public final int OBJECT_KEY_3UTRS = 20;
    static public final int OBJECT_KEY_5UTRS = 17;
    static public final int OBJECT_KEY_VARIANTS = 7;



    private int rgdId;
    private int objectKey;
    private Date createdDate;
    private Date releasedDate;
    private Date lastModifiedDate;
    private String rgdFlag;
    private String notes;
    private String objectStatus;
    private int speciesTypeKey;

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(int objectKey) {
        this.objectKey = objectKey;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getRgdFlag() {
        return rgdFlag;
    }

    public void setRgdFlag(String rgdFlag) {
        this.rgdFlag = rgdFlag;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(String objectStatus) {
        this.objectStatus = objectStatus;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public RgdId(int id) {
        this.rgdId = id;
    }

    public RgdId(String id) {
        try {
            this.rgdId = Integer.parseInt(id);
        }catch (Exception ignored) {

        }
    }

    public boolean isValidId() {
        if (rgdId > 0) {
            return true;
        }else {
            return false;
        }
    }

    public int getValue() throws Exception{
        if (this.rgdId <= 0) throw new Exception("RGD ID Value not set");
                
        return this.rgdId;
    }

    public String toString() {
        return this.rgdId + "";
    }

    public String getObjectTypeName() {
        return getObjectTypeName(this.objectKey);
    }

    /**
     * return object type name given object key
     * @param objectKey object key
     * @return common name for given object key
     */
    static public String getObjectTypeName(int objectKey) {

        switch( objectKey ) {
            case OBJECT_KEY_GENES:      return "Gene";
            case OBJECT_KEY_QTLS:       return "QTL";
            case OBJECT_KEY_SSLPS:      return "Marker";
            case OBJECT_KEY_REFERENCES: return "Reference";
            case OBJECT_KEY_STRAINS:    return "Strain";
            case OBJECT_KEY_PROMOTERS:  return "Promoter";
            case OBJECT_KEY_CELL_LINES: return "Cell Line";
            case OBJECT_KEY_SEQUENCES:  return "Sequence";
            case OBJECT_KEY_CHROMOSOME: return "Chromosome";
            case OBJECT_KEY_VARIANTS:   return "Variant";
            case OBJECT_KEY_PROTEINS:   return "Protein";
            default:                    return objectKey + "";
        }
    }
}
