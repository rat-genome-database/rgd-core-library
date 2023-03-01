package edu.mcw.rgd.process.describe;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/25/13
 * Time: 4:41 PM
 * light-weight version of Annotation datamodel class
 */
public class AnnotationDG {

    private String termAcc;
    private String term;
    private String qualifier;
    private String aspect;
    private String evidence;
    private int eQualifier;

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public int geteQualifier() {
        return eQualifier;
    }

    public void seteQualifier(int eQualifier) {
        this.eQualifier = eQualifier;
    }
}
