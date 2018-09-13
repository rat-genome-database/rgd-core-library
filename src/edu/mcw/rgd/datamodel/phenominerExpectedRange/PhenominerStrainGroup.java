package edu.mcw.rgd.datamodel.phenominerExpectedRange;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerStrainGroup {
    private int id;
    private String name;
    private String strain_ont_id;
    private String phenotype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrain_ont_id() {
        return strain_ont_id;
    }

    public void setStrain_ont_id(String strain_ont_id) {
        this.strain_ont_id = strain_ont_id;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }
}
