package edu.mcw.rgd.datamodel.genomeInfo;

import java.util.Map;

public class GeneTypeCounts {
    private String geneTypeLc;
    private int count;
    private String chromosome;
    private int mapKey;
    private int speciesTypeKey;

    public String getGeneTypeLc() {
        return geneTypeLc;
    }

    public void setGeneTypeLc(String geneTypeLc) {
        this.geneTypeLc = geneTypeLc;
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
