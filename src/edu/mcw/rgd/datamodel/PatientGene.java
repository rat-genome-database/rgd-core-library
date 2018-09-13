package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 5/12/14
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatientGene {
    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    private String mrn;
    private String gene;

}
