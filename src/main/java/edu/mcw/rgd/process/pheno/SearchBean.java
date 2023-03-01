package edu.mcw.rgd.process.pheno;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 25, 2011
 * Time: 8:31:57 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class holds all information needed to search the phenominer data tables.  Backend methods take this
 * class as a parameter to return a search result.
 */
public class SearchBean {

   private String studyName;
    private String studyId="-1";
    private String studySource;
    private String studyType;
    private String reference;

    private String experimentId="-1";
    private String experimentName;

    private String recordId="-1";
    private String cmAccId;
    private String cmValue;
    private String cmUnits;
    private String cmSD;
    private String cmSEM;
    private String cmError;
    private String cmAveType;
    private String cmFormula;

    private String mmAccId;
    private String mmDuration;
    private String mmSite;
    private String mmPIType;
    private String mmPITime;
    private String mmPIUnit;

    private String sAccId;
    private String sAnimalCount;
    private String sMinAge;
    private String sMaxAge;
    private String sSex;

    private String cAccId;

    private String cValueMin;
    private String cValueMax;
    private String cUnits;
    private String cMinDuration;

    private String cMaxDuration;
    private String capplicationMethod;
    private String cordinality;




    public boolean clinicalMeasurementSet() {
        if (cmAccId !=null || cmValue != null || cmUnits != null
                || cmSD != null || cmSEM != null
                || cmError != null || cmAveType != null || cmFormula != null) {
            return true;
        }
        return false;
    }


    public boolean measurementMethodSet() {
        if (mmAccId !=null || mmDuration != null || mmSite != null || mmPIType != null || mmPITime != null || mmPIUnit != null) {
            return true;
        }
        return false;
    }

    public boolean conditionSet() {

        if (cAccId !=null || cValueMin != null ||cValueMax != null || cUnits != null || cMinDuration != null || cMaxDuration != null || capplicationMethod != null || cordinality != null) {
            return true;
        }
        return false;
    }

    public boolean sampleSet() {
        if (sAccId !=null || sAnimalCount != null || sMinAge != null || sMaxAge != null || sSex != null) {
            return true;
        }
        return false;
    }



    public String getCMaxDuration() {
        return cMaxDuration;
    }

    public void setCMaxDuration(String cMaxDuration) {
        this.cMaxDuration = cMaxDuration;
    }


    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudySource() {
        return studySource;
    }

    public void setStudySource(String studySource) {
        this.studySource = studySource;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getCmAccId() {
        return cmAccId;
    }

    public void setCmAccId(String cmAccId) {
        this.cmAccId = cmAccId;
    }

    public String getCmValue() {
        return cmValue;
    }

    public void setCmValue(String cmValue) {
        this.cmValue = cmValue;
    }

    public String getCmUnits() {
        return cmUnits;
    }

    public void setCmUnits(String cmUnits) {
        this.cmUnits = cmUnits;
    }

    public String getCmSD() {
        return cmSD;
    }

    public void setCmSD(String cmSD) {
        this.cmSD = cmSD;
    }

    public String getCmSEM() {
        return cmSEM;
    }

    public void setCmSEM(String cmSEM) {
        this.cmSEM = cmSEM;
    }

    public String getCmError() {
        return cmError;
    }

    public void setCmError(String cmError) {
        this.cmError = cmError;
    }

    public String getCmAveType() {
        return cmAveType;
    }

    public void setCmAveType(String cmAveType) {
        this.cmAveType = cmAveType;
    }

    public String getCmFormula() {
        return cmFormula;
    }

    public void setCmFormula(String cmFormula) {
        this.cmFormula = cmFormula;
    }

    public String getMmAccId() {
        return mmAccId;
    }

    public void setMmAccId(String mmAccId) {
        this.mmAccId = mmAccId;
    }

    public String getMmDuration() {
        return mmDuration;
    }

    public void setMmDuration(String mmDuration) {
        this.mmDuration = mmDuration;
    }

    public String getMmSite() {
        return mmSite;
    }

    public void setMmSite(String mmSite) {
        this.mmSite = mmSite;
    }

    public String getMmPIType() {
        return mmPIType;
    }

    public void setMmPIType(String mmPIType) {
        this.mmPIType = mmPIType;
    }

    public String getMmPITime() {
        return mmPITime;
    }

    public void setMmPITime(String mmPITime) {
        this.mmPITime = mmPITime;
    }

    public String getMmPIUnit() {
        return mmPIUnit;
    }

    public void setMmPIUnit(String mmPIUnit) {
        this.mmPIUnit = mmPIUnit;
    }

    public String getSAccId() {
        return sAccId;
    }

    public void setSAccId(String sAccId) {
        this.sAccId = sAccId;
    }

    public String getSAnimalCount() {
        return sAnimalCount;
    }

    public void setSAnimalCount(String sAnimalCount) {
        this.sAnimalCount = sAnimalCount;
    }

    public String getSMinAge() {
        return sMinAge;
    }

    public void setSMinAge(String sMinAge) {
        this.sMinAge = sMinAge;
    }

    public String getSMaxAge() {
        return sMaxAge;
    }

    public void setSMaxAge(String sMaxAge) {
        this.sMaxAge = sMaxAge;
    }

    public String getSSex() {
        return sSex;
    }

    public void setSSex(String sSex) {
        this.sSex = sSex;
    }

    public String getCAccId() {
        return cAccId;
    }

    public void setCAccId(String cAccId) {
        this.cAccId = cAccId;
    }

    public String getCValueMin() {
        return cValueMin;
    }

    public void setCValueMin(String cValueMin) {
        this.cValueMin = cValueMin;
    }

    public String getCValueMax() {
        return cValueMax;
    }

    public void setCValueMax(String cValueMax) {
        this.cValueMax = cValueMax;
    }

    public String getCUnits() {
        return cUnits;
    }

    public void setCUnits(String cUnits) {
        this.cUnits = cUnits;
    }

    public String getCMinDuration() {
        return cMinDuration;
    }

    public void setCMinDuration(String cMinDuration) {
        this.cMinDuration = cMinDuration;
    }

    public String getCapplicationMethod() {
        return capplicationMethod;
    }

    public void setCapplicationMethod(String capplicationMethod) {
        this.capplicationMethod = capplicationMethod;
    }

    public String getCordinality() {
        return cordinality;
    }

    public void setCordinality(String cordinality) {
        this.cordinality = cordinality;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getStudyId() {
        return this.studyId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentId() {
        return this.experimentId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return this.recordId;
    }
}
