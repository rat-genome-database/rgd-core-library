package edu.mcw.rgd.datamodel.pheno;

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
}
