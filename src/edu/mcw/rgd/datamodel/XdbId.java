package edu.mcw.rgd.datamodel;


import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 3, 2010
 * Time: 10:23:48 AM
 * external identifier, like OMIM ID, linked to an RGD object, identified by RGD-ID;
 * represents a row in a RGD_ACC_XDB table
 */
public class XdbId implements Identifiable, Dumpable {

    private int key;            // internal primary key
    private int xdbKey;         // external database key (3 for Entrez Gene)
    private String accId;       // external id value (f.e. Entrez Gene ID)
    private int rgdId;          // RGD-ID associated with given external id
    private String linkText;
    private String srcPipeline;
    private String notes;
    private java.util.Date creationDate;
    private java.util.Date modificationDate;

    // some commonly used XDB_KEYs (taken from RGD_XDB database)
    static public final int XDB_KEY_BIND = 19;
    static public final int XDB_KEY_CELLOSAURUS = 128;
    static public final int XDB_KEY_COSMIC = 45;
    static public final int XDB_KEY_ENSEMBL_GENES = 20;
    static public final int XDB_KEY_ENSEMBL_PROTEIN = 27;
    static public final int XDB_KEY_ENSEMBL_TRANSCRIPT = 42;
    static public final int XDB_KEY_ENTREZGENE = 3;
    static public final int XDB_KEY_GENE3D = 39;
    static public final int XDB_KEY_GENEBANKNU = 1;
    static public final int XDB_KEY_GENEBANKPROT = 7;
    static public final int XDB_KEY_GERMONLINE = 18;
    static public final int XDB_KEY_HGNC = 21;
    static public final int XDB_KEY_HOMOLOGENE = 22;
    static public final int XDB_KEY_HPRD = 24;
    static public final int XDB_KEY_IMAGECLONE = 40;
    static public final int XDB_KEY_INTERPRO = 29;
    static public final int XDB_KEY_KEGGPATHWAY = 23;
    static public final int XDB_KEY_KEGGREPORT = 17;
    static public final int XDB_KEY_MGCCLONE = 41;
    static public final int XDB_KEY_MGD = 5;
    static public final int XDB_KEY_MIRBASE = 58;
    static public final int XDB_KEY_NCBI_GENE = 3;
    static public final int XDB_KEY_NCBI_NU = 10;
    static public final int XDB_KEY_OMIM = 6;
    static public final int XDB_KEY_ORPHANET = 62;
    static public final int XDB_KEY_PANTHER = 34;
    static public final int XDB_KEY_PFAM = 30;
    static public final int XDB_KEY_PHARMGKB = 44;
    static public final int XDB_KEY_PID = 46; // Pathway Interaction Database
    static public final int XDB_KEY_PIRSF = 36;
    static public final int XDB_KEY_PRINTS = 31;
    static public final int XDB_KEY_PRODOM = 32;
    static public final int XDB_KEY_PROSITE = 33;
    static public final int XDB_KEY_PUBMED = 2;
    static public final int XDB_KEY_RZPD = 28;
    static public final int XDB_KEY_SMART = 35;
    static public final int XDB_KEY_SUPFAM = 38;
    static public final int XDB_KEY_TIGR = 15;
    static public final int XDB_KEY_TIGRFAMS = 37;
    static public final int XDB_KEY_TRANSPOSAGEN = 43;
    static public final int XDB_KEY_UNIGENE = 4;
    static public final int XDB_KEY_UNIPARC = 26;
    static public final int XDB_KEY_UNIPROT = 14;
    static public final int XDB_KEY_UNIPROT_SECONDARY = 60;
    static public final int XDB_KEY_UNISTS = 16;
    static public final int XDB_KEY_VGNC = 127;
    static public final int XDB_KEY_GWAS = 142;
    static public final int XDB_KEY_BIOCYC_PATHWAY = 147;
    
    /**
     * get unique key -- uniquely identifying a row in the db table (RGD_ACC_XDB.ACC_XDB_KEY)
     * @return unique key for this xdb id
     */
    public int getKey() {
        return key;
    }

    // set RGD_ACC_XDB.ACC_XDB_KEY
    public void setKey(int key) {
        this.key = key;
    }

    // get RGD_XDB.XDB_KEY
    public int getXdbKey() {
        return xdbKey;
    }

    // set RGD_XDB.XDB_KEY
    public void setXdbKey(int xdbKey) {
        this.xdbKey = xdbKey;
    }

    // get RGD_ACC_XDB.ACC_ID (f.e. Entrez Gene ID)
    public String getAccId() {
        return accId;
    }

    // set RGD_ACC_XDB.ACC_ID
    public void setAccId(String accId) {
        this.accId = accId;
    }

    // get RGD_ACC_XDB.RGD_ID
    public int getRgdId() {
        return rgdId;
    }

    // set RGD_ACC_XDB.RGD_ID
    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getSrcPipeline() {
        return srcPipeline;
    }

    public void setSrcPipeline(String srcPipeline) {
        this.srcPipeline = srcPipeline;
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * compares this object with another XdbId object for equality;
     * to be equal, they must have same XDB_KEY, RGD_ID, ACC_ID and SRC_PIPELINE
     * @param o object to be compared with this object for equality
     * @return true if this object is equal to 'o' object
     */
    public boolean equals(Object o) {
        XdbId xdbId = (XdbId) o;

		String accId1 = Utils.defaultString(this.getAccId());
		String accId2 = Utils.defaultString(xdbId.getAccId());

        String srcPipeline1 = Utils.defaultString(this.getSrcPipeline());
        String srcPipeline2 = Utils.defaultString(xdbId.getSrcPipeline());
        
        return this.getXdbKey()==xdbId.getXdbKey()
                && this.getRgdId()==xdbId.getRgdId()
                && accId1.equals(accId2)
                && srcPipeline1.equals(srcPipeline2);
    }

    /**
     * hash code computed from fields XDB_KEY, RGD_ID, ACC_ID and SRC_PIPELINE
     * @return unique hash code
     */
    @Override
    public int hashCode() {
        String accId = Utils.defaultString(this.getAccId());
        String srcPipeline = Utils.defaultString(this.getSrcPipeline());

        return this.getXdbKey() ^ this.getRgdId() ^ accId.hashCode() ^ srcPipeline.hashCode();
    }

    /**
     * get a short description given xdb key
     * @return string with description
     */
    public String getXdbKeyAsString() {

        Xdb xdb = null;
        try { xdb = XDBIndex.getInstance().getXDB(xdbKey);
        } catch(Exception e) {}

        return xdb==null ? null : xdb.getName();
    }

    public String dump(String delimiter) {

        String xdbName = null;
        try {
            xdbName = getXdbKeyAsString();
        }
        catch(Exception ignore) {}

        return new Dumper(delimiter)
            .put("ACC_XDB_KEY", key)
            .put("XDB_KEY", xdbKey)
            .put("XDB_NAME", xdbName)
            .put("ACC_ID", accId)
            .put("RGD_ID", rgdId)
            .put("SRC_PIPELINE", srcPipeline)
            .put("LINK_TEXT", linkText)
            .put("NOTES", notes)
            .put("CREATION_DATE", creationDate)
            .put("MODIFICATION_DATE", modificationDate)
            .dump();
    }

    @Override
    public String toString() {
        return "RGDID:"+rgdId+" "+getXdbKeyAsString()+"("+xdbKey+"), ACCID:"+accId+", SRC:"+srcPipeline;
    }
}
