package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/27/11
 * Time: 5:12 PM
 * represents a row in PORTAL_TERMSET1 table
 */
public class PortalTermSet {

    private int portalTermSetId;
    private int portalKey;
    private int parentTermSetId;
    private String termAcc;
    private String ontTermName;

    public int getPortalTermSetId() {
        return portalTermSetId;
    }

    public void setPortalTermSetId(int portalTermSetId) {
        this.portalTermSetId = portalTermSetId;
    }

    public int getPortalKey() {
        return portalKey;
    }

    public void setPortalKey(int portalKey) {
        this.portalKey = portalKey;
    }

    public int getParentTermSetId() {
        return parentTermSetId;
    }

    public void setParentTermSetId(int parentTermSetId) {
        this.parentTermSetId = parentTermSetId;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public String getOntTermName() {
        return ontTermName;
    }

    public void setOntTermName(String ontTermName) {
        this.ontTermName = ontTermName;
    }
}
