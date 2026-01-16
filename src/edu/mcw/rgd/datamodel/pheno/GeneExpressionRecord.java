package edu.mcw.rgd.datamodel.pheno;

import edu.mcw.rgd.process.Utils;
import java.util.Date;
import java.util.List;

/**
 * Created by mtutaj on 10/30/2018.
 */
public class GeneExpressionRecord {

    private int id; // GENE_EXPRESSION_EXP_RECORD_ID
    private int experimentId;
    private int sampleId;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private int curationStatus;
    private int speciesTypeKey;
    private Integer clinicalMeasurementId;
    private String traitTerm;
    private String measurementTerm;
    private String experimentCondition;
    private String conditionAccId;


    private List<GeneExpressionRecordValue> values;
    private List<Condition> conditions;
    private List<MeasurementMethod> measurementMethods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(int curationStatus) {
        this.curationStatus = curationStatus;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public List<GeneExpressionRecordValue> getValues() {
        return values;
    }

    public void setValues(List<GeneExpressionRecordValue> values) {
        this.values = values;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<MeasurementMethod> getMeasurementMethods() {
        return measurementMethods;
    }

    public void setMeasurementMethods(List<MeasurementMethod> measurementMethods) {
        this.measurementMethods = measurementMethods;
    }

    public Integer getClinicalMeasurementId() {
        return clinicalMeasurementId;
    }

    public void setClinicalMeasurementId(Integer clinicalMeasurementId) {
        this.clinicalMeasurementId = clinicalMeasurementId;
    }
    public String getTraitTerm() {
        return traitTerm;
    }

    public void setTraitTerm(String traitTerm) {
        this.traitTerm = traitTerm;
    }

    public String getMeasurementTerm() {
        return measurementTerm;
    }

    public void setMeasurementTerm(String measurementTerm) {
        this.measurementTerm = measurementTerm;
    }

    public String getExperimentCondition() {
        return experimentCondition;
    }

    public void setExperimentCondition(String experimentCondition) {
        this.experimentCondition = experimentCondition;
    }

    public String getConditionAccId() {
        return conditionAccId;
    }

    public void setConditionAccId(String conditionAccId) {
        this.conditionAccId = conditionAccId;
    }

    @Override
    public boolean equals(Object obj) {
        GeneExpressionRecord r = (GeneExpressionRecord) obj;
        return experimentId==r.getExperimentId() && sampleId==r.getSampleId() && Utils.stringsAreEqual(lastModifiedBy,r.getLastModifiedBy()) && lastModifiedDate.equals(r.getLastModifiedDate())
                && curationStatus==r.curationStatus && speciesTypeKey==r.getSpeciesTypeKey() && clinicalMeasurementId==r.getClinicalMeasurementId();
    }

    @Override
    public int hashCode() {
        return experimentId ^ sampleId ^ Utils.defaultString(lastModifiedBy).hashCode() ^ lastModifiedDate.hashCode() ^ curationStatus ^ speciesTypeKey ^ clinicalMeasurementId;
    }
}
