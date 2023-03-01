package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * @author mtutaj
 * @since 2/12/14
 * <p>
 * represents a row from table CLINVAR
 * <p>
 * Note: Variant class represents a row from CLINVAR table RATCNDEV|RATCNPROD schema
 */
public class VariantInfo extends GenomicElement {

    private String clinicalSignificance;
    private Date dateLastEvaluated;
    private String reviewStatus;
    private String methodType;
    private String nucleotideChange;
    private String traitName;
    private String ageOfOnset;
    private String prevalence;
    private String molecularConsequence;
    private String submitter;

    public String getClinicalSignificance() {
        return clinicalSignificance;
    }

    public void setClinicalSignificance(String clinicalSignificance) {
        this.clinicalSignificance = clinicalSignificance;
    }

    public Date getDateLastEvaluated() {
        return dateLastEvaluated;
    }

    public void setDateLastEvaluated(Date dateLastEvaluated) {
        this.dateLastEvaluated = dateLastEvaluated;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getNucleotideChange() {
        return nucleotideChange;
    }

    public void setNucleotideChange(String nucleotideChange) {
        this.nucleotideChange = nucleotideChange;
    }

    public String getTraitName() {
        return traitName;
    }

    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public String getPrevalence() {
        return prevalence;
    }

    public void setPrevalence(String prevalence) {
        this.prevalence = prevalence;
    }

    public String getMolecularConsequence() {
        return molecularConsequence;
    }

    public void setMolecularConsequence(String molecularConsequence) {
        this.molecularConsequence = molecularConsequence;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    protected void populateDumper(Dumper dumper) {
        super.populateDumper(dumper);
        dumper
            .put("CLINICAL_SIGNIFICANCE", getClinicalSignificance())
            .put("LAST_EVALUATED", getDateLastEvaluated())
            .put("REVIEW_STATUS", getReviewStatus())
            .put("METHOD_TYPE", getMethodType())
            .put("NUCL_CHANGE", getNucleotideChange())
            .put("TRAIT", getTraitName())
            .put("SUBMITTER", getSubmitter())
            .put("AGE_OF_ONSET", getAgeOfOnset())
            .put("PREVALENCE", getPrevalence())
            .put("MOLECULAR_CONSEQUENCE", getMolecularConsequence());
    }
}
