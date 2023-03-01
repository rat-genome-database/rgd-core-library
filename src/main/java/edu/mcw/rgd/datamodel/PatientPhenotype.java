package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 5/12/14
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatientPhenotype {
    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    private String mrn;

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    private String phenotype;
}
