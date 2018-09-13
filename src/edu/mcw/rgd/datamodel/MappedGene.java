package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 10/2/12
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MappedGene {

    private Gene g;
    private long start;
    private long stop;
    private String chromosome;
    private int mapKey;
    private String strand;

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public Gene getGene() {
        return g;
    }

    public void setGene(Gene g) {
        this.g = g;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStop() {
        return stop;
    }

    public void setStop(long stop) {
        this.stop = stop;
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

}
