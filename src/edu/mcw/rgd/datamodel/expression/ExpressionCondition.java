package edu.mcw.rgd.datamodel.expression;

/**
 * An experiment condition as the expression tool indexes it: the ontology term the condition points at, plus the
 * ordinality the curator gave it within its record. Conditions are indexed in this shape rather than as
 * edu.mcw.rgd.datamodel.pheno.Condition so that the documents carry only what the tool displays and filters on.
 */
public class ExpressionCondition {

    private String accId;
    private String term;
    private int obsolete;
    private Integer ordinality;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getObsolete() {
        return obsolete;
    }

    public void setObsolete(int obsolete) {
        this.obsolete = obsolete != 0 ? 1 : 0;
    }

    public Integer getOrdinality() {
        return ordinality;
    }

    public void setOrdinality(Integer ordinality) {
        this.ordinality = ordinality;
    }
}
