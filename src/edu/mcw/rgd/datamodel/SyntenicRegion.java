package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 1/29/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyntenicRegion {

    private int backboneMapKey;
    private String backboneChromosome;
    private long backboneStart;
    private long backboneStop;

    private int mapKey;
    private String chromosome;
    private long start;
    private long stop;

    public int getBackboneMapKey() {
        return backboneMapKey;
    }

    public void setBackboneMapKey(int backboneMapKey) {
        this.backboneMapKey = backboneMapKey;
    }

    public String getBackboneChromosome() {
        return backboneChromosome;
    }

    public void setBackboneChromosome(String backboneChromosome) {
        this.backboneChromosome = backboneChromosome;
    }

    public long getBackboneStart() {
        return backboneStart;
    }

    public void setBackboneStart(long backboneStart) {
        this.backboneStart = backboneStart;
    }

    public long getBackboneStop() {
        return backboneStop;
    }

    public void setBackboneStop(long backboneStop) {
        this.backboneStop = backboneStop;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
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
}
