package edu.mcw.rgd.datamodel.genomeInfo;

public class ObjectTypeCounts {
    private String objectNameLc;
    private int count;
    private String chromosome;
    private int mapKey;
    private int speciesTypeKey;

    public String getObjectNameLc() {
        return objectNameLc;
    }

    public void setObjectNameLc(String objectNameLc) {
        this.objectNameLc = objectNameLc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }
}
