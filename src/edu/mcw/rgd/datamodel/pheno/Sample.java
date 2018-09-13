package edu.mcw.rgd.datamodel.pheno;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 17, 2011
 * Time: 2:51:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sample {

    private int id;
    private Integer ageDaysFromHighBound;
    private Integer ageDaysFromLowBound;
    private Integer numberOfAnimals = new Integer(0);
    private String notes;
    private String sex;
    private String strainAccId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAgeDaysFromHighBound() {
        return ageDaysFromHighBound;
    }

    public void setAgeDaysFromHighBound(Integer ageDaysFromHighBound) {
        this.ageDaysFromHighBound = ageDaysFromHighBound;
    }

    public Integer getAgeDaysFromLowBound() {
        return ageDaysFromLowBound;
    }

    public void setAgeDaysFromLowBound(Integer ageDaysFromLowBound) {
        this.ageDaysFromLowBound = ageDaysFromLowBound;
    }

    public Integer getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(Integer numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStrainAccId() {
        return strainAccId;
    }

    public void setStrainAccId(String strainAccId) {
        this.strainAccId = strainAccId;
    }
}
