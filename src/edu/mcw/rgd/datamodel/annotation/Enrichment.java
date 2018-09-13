package edu.mcw.rgd.datamodel.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 4/5/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Enrichment {

    private String aspect;
    private String term;
    private String term_acc;
    private String root;
    private String root_acc;
    private String objectSymbol;
    private int objectId;
    private String evidence;

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getObjectSymbol() {
        return objectSymbol;
    }

    public void setObjectSymbol(String objectSymbol) {
        this.objectSymbol = objectSymbol;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getTerm_acc() {
        return term_acc;
    }

    public void setTerm_acc(String term_acc) {
        this.term_acc = term_acc;
    }

    public String getRoot_acc() {
        return root_acc;
    }

    public void setRoot_acc(String root_acc) {
        this.root_acc = root_acc;
    }
}
