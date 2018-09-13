package edu.mcw.rgd.datamodel.phenominerExpectedRange;

import edu.mcw.rgd.datamodel.ontologyx.Term;

import java.util.List;
import java.util.Map;

/**
 * Created by jthota on 5/29/2018.
 */
public class PhenotypeObject {
    private String clinicalMeasurement;
    private String clinicalMeasurementOntId;
    private String normalRange;
    private int strainSpecifiedRecordCount;
    private int sexSpecifiedRecordCount;
    private int ageSpecifiedRecordCount;

    private List<PhenominerExpectedRange> ranges;

    private PhenominerExpectedRange normalAll;
    private PhenominerExpectedRange normalMale;
    private PhenominerExpectedRange normalFemale;

    private Map<String,Integer> strainGroupMap;
    private List<String> overAllConditions;
    private List<String> overAllMethods;

    private Map<String, String> studies;
    //    private Map<String, String> traits;
    private List<String> sex;
    private List<String> age;
    //   private String traitOntId;
//    private String trait;
    private List<Term> traits;
    private List<TraitObject> traitAncestors;

    public List<TraitObject> getTraitAncestors() {
        return traitAncestors;
    }

    public void setTraitAncestors(List<TraitObject> traitAncestors) {
        this.traitAncestors = traitAncestors;
    }

    public List<Term> getTraits() {
        return traits;
    }

    public void setTraits(List<Term> traits) {
        this.traits = traits;
    }

    public Map<String, Integer> getStrainGroupMap() {
        return strainGroupMap;
    }

    public void setStrainGroupMap(Map<String, Integer> strainGroupMap) {
        this.strainGroupMap = strainGroupMap;
    }

    public List<String> getAge() {
        return age;
    }

    public void setAge(List<String> age) {
        this.age = age;
    }

    public List<String> getSex() {
        return sex;
    }

    public void setSex(List<String> sex) {
        this.sex = sex;
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

    public List<String> getOverAllConditions() {
        return overAllConditions;
    }

    public void setOverAllConditions(List<String> overAllConditions) {
        this.overAllConditions = overAllConditions;
    }

    public List<String> getOverAllMethods() {
        return overAllMethods;
    }

    public void setOverAllMethods(List<String> overAllMethods) {
        this.overAllMethods = overAllMethods;
    }

    public Map<String, String> getStudies() {
        return studies;
    }

    public void setStudies(Map<String, String> studies) {
        this.studies = studies;
    }

 /*   public Map<String, String> getTraits() {
        return traits;
    }

    public void setTraits(Map<String, String> traits) {
        this.traits = traits;
    }*/

    public PhenominerExpectedRange getNormalAll() {
        return normalAll;
    }

    public void setNormalAll(PhenominerExpectedRange normalAll) {
        this.normalAll = normalAll;
    }

    public PhenominerExpectedRange getNormalMale() {
        return normalMale;
    }

    public void setNormalMale(PhenominerExpectedRange normalMale) {
        this.normalMale = normalMale;
    }

    public PhenominerExpectedRange getNormalFemale() {
        return normalFemale;
    }

    public void setNormalFemale(PhenominerExpectedRange normalFemale) {
        this.normalFemale = normalFemale;
    }



    public String getNormalRange() {
        return normalRange;
    }

    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    public int getStrainSpecifiedRecordCount() {
        return strainSpecifiedRecordCount;
    }

    public void setStrainSpecifiedRecordCount(int strainSpecifiedRecordCount) {
        this.strainSpecifiedRecordCount = strainSpecifiedRecordCount;
    }

    public int getSexSpecifiedRecordCount() {
        return sexSpecifiedRecordCount;
    }

    public void setSexSpecifiedRecordCount(int sexSpecifiedRecordCount) {
        this.sexSpecifiedRecordCount = sexSpecifiedRecordCount;
    }

    public int getAgeSpecifiedRecordCount() {
        return ageSpecifiedRecordCount;
    }

    public void setAgeSpecifiedRecordCount(int ageSpecifiedRecordCount) {
        this.ageSpecifiedRecordCount = ageSpecifiedRecordCount;
    }

    public List<PhenominerExpectedRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<PhenominerExpectedRange> ranges) {
        this.ranges = ranges;
    }
}
