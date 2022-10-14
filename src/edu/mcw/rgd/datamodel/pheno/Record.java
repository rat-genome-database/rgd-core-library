package edu.mcw.rgd.datamodel.pheno;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 2/7/11
 * Time: 9:47 AM
 */
public class Record {

    private int id;
    private int clinicalMeasurementId;
    private int experimentId;
    private String experimentName;
    private String experimentNotes;
    private int measurementMethodId;
    private int sampleId;
    private int studyId;
    private String studyName;
    private String measurementSD;
    private String measurementSem;
    private String measurementUnits;
    private String measurementValue;
    private String measurementError;
    private Sample sample = new Sample();
    private MeasurementMethod measurementMethod = new MeasurementMethod();
    private ClinicalMeasurement clinicalMeasurement = new ClinicalMeasurement();
    private List<Condition> conditions = new ArrayList<>();
    private boolean hasIndividualRecord = false;
    private String conditionDescription = "";
    private int refRgdId;

    public int getRefRgdId() {
        return refRgdId;
    }

    public void setRefRgdId(int refRgdId) {
        this.refRgdId = refRgdId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentNotes() {
        return experimentNotes;
    }

    public void setExperimentNotes(String experimentNotes) {
        this.experimentNotes = experimentNotes;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public int getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(int curationStatus) {
        this.curationStatus = curationStatus;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    private int curationStatus = 20;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String createdBy;

    public String getRatIdValues() {
        return ratIdValues;
    }

    public void setRatIdValues(String ratIdValues) {
        this.ratIdValues = ratIdValues;
    }

    private String ratIdValues;

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public MeasurementMethod getMeasurementMethod() {
        return measurementMethod;
    }

    public void setMeasurementMethod(MeasurementMethod measurementMethod) {
        this.measurementMethod = measurementMethod;
    }

    public ClinicalMeasurement getClinicalMeasurement() {
        return clinicalMeasurement;
    }

    public void setClinicalMeasurement(ClinicalMeasurement clinicalMeasurement) {
        this.clinicalMeasurement = clinicalMeasurement;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getConditionDescription() {
        return this.conditionDescription;
    }

    public String setConditionDescription(String conditionDescription) {
        return this.conditionDescription = conditionDescription;
    }

    public void setConditions(List<Condition> conditions) {

        this.conditions = conditions;

    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public int getClinicalMeasurementId() {
        return clinicalMeasurementId;
    }

    public void setClinicalMeasurementId(int clinicalMeasurementId) {
        this.clinicalMeasurementId = clinicalMeasurementId;
    }

    public int getMeasurementMethodId() {
        return measurementMethodId;
    }

    public void setMeasurementMethodId(int measurementMethodId) {
        this.measurementMethodId = measurementMethodId;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

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


    public String getMeasurementSD() {
        return measurementSD;
    }

    public void setMeasurementSD(String measurementSD) {
        this.measurementSD = measurementSD;
    }

    public String getMeasurementSem() {
        return measurementSem;
    }

    public void setMeasurementSem(String measurementSem) {
        this.measurementSem = measurementSem;
    }

    public String getMeasurementUnits() {
        return measurementUnits;
    }

    public void setMeasurementUnits(String measurementUnits) {
        this.measurementUnits = measurementUnits;
    }

    public String getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(String measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getMeasurementError() {
        return measurementError;
    }

    public void setMeasurementError(String measurementError) {
        this.measurementError = measurementError;
    }

    public boolean getHasIndividualRecord() {
        return hasIndividualRecord;
    }

    public void setHasIndividualRecord(boolean value) {
        hasIndividualRecord = value;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
