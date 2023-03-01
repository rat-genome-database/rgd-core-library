package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 9, 2010
 * Time: 1:02:40 PM
 * represents a pipeline
 */
public class Pipeline {

    int pipelineKey; // unique pipeline key
    String name; // pipeline name
    int speciesTypeKey; // species type key
    String info; // description

    public int getPipelineKey() {
        return pipelineKey;
    }

    public void setPipelineKey(int pipelineKey) {
        this.pipelineKey = pipelineKey;
    }

    public String getName() {
        return name;
    }

    // full-name is a concatenation of pipeline name and species name
    public String getFullName() {
        return getName()+getSpecies();
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public String getSpecies() {
        return SpeciesType.getCommonName(speciesTypeKey);
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
