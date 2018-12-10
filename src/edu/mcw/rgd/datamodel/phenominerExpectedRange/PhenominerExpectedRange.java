package edu.mcw.rgd.datamodel.phenominerExpectedRange;

import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.datamodel.pheno.Condition;
import edu.mcw.rgd.datamodel.pheno.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerExpectedRange implements Serializable {

    private int expectedRangeId;
    private String clinicalMeasurement;
    private String clinicalMeasurementOntId;
    private String expectedRangeName;
    private int strainGroupId;
    private String strainGroupName;
    private String trait;
    private String traitOntId;

    private int ageLowBound;
    private int ageHighBound;
    private String sex;
    private double min;
    private double max;
    private double range;
    private double rangeValue;
    private double rangeSD;
    private double rangeLow;
    private double rangeHigh;
    private Date createdDate;
    private Date modifiedDate;
    private List<Term> strains;
    private List<Term> methods;
    private List<Term> conditions;


    private List<Record> experimentRecords;
    private List<TraitObject> traitAncestors;
    private String units;

    public List<Term> getStrains() {
        return strains;
    }

    public void setStrains(List<Term> strains) {
        this.strains = strains;
    }

    public List<Term> getMethods() {
        return methods;
    }

    public void setMethods(List<Term> methods) {
        this.methods = methods;
    }

    public List<Term> getConditions() {
        return conditions;
    }

    public void setConditions(List<Term> conditions) {
        this.conditions = conditions;
    }

    public List<TraitObject> getTraitAncestors() {
        return traitAncestors;
    }

    public void setTraitAncestors(List<TraitObject> traitAncestors) {
        this.traitAncestors = traitAncestors;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getTraitOntId() {
        return traitOntId;
    }

    public void setTraitOntId(String traitOntId) {
        this.traitOntId = traitOntId;
    }

    public List<Record> getExperimentRecords() {
        return experimentRecords;
    }

    public void setExperimentRecords(List<Record> experimentRecords) {
        this.experimentRecords = experimentRecords;
    }

    public String getExpectedRangeName() {
        return expectedRangeName;
    }

    public void setExpectedRangeName(String expectedRangeName) {
        this.expectedRangeName = expectedRangeName;
    }




    public int getExpectedRangeId() {
        return expectedRangeId;
    }

    public void setExpectedRangeId(int expectedRangeId) {
        this.expectedRangeId = expectedRangeId;
    }

    public String getClinicalMeasurement() {
        return clinicalMeasurement;
    }

    public void setClinicalMeasurement(String clinicalMeasurement) {
        this.clinicalMeasurement = clinicalMeasurement;
    }

    public String getClinicalMeasurementOntId() {
        return clinicalMeasurementOntId;
    }

    public void setClinicalMeasurementOntId(String clinicalMeasurementOntId) {
        this.clinicalMeasurementOntId = clinicalMeasurementOntId;
    }

    public int getStrainGroupId() {
        return strainGroupId;
    }

    public void setStrainGroupId(int strainGroupId) {
        this.strainGroupId = strainGroupId;
    }

    public String getStrainGroupName() {
        return strainGroupName;
    }

    public void setStrainGroupName(String strainGroupName) {
        this.strainGroupName = strainGroupName;
    }



    public int getAgeLowBound() {
        return ageLowBound;
    }

    public void setAgeLowBound(int ageLowBound) {
        this.ageLowBound = ageLowBound;
    }

    public int getAgeHighBound() {
        return ageHighBound;
    }

    public void setAgeHighBound(int ageHighBound) {
        this.ageHighBound = ageHighBound;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(double rangeValue) {
        this.rangeValue = rangeValue;
    }

    public double getRangeSD() {
        return rangeSD;
    }

    public void setRangeSD(double rangeSD) {
        this.rangeSD = rangeSD;
    }

    public double getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(double rangeLow) {
        this.rangeLow = rangeLow;
    }

    public double getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(double rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
