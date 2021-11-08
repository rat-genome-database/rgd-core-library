package edu.mcw.rgd.datamodel;

/**
 * User: jdepons
 * Date: 1/29/13
 */
public class SyntenicRegion {

    private int backboneMapKey;
    private String backboneChromosome;
    private int backboneStart;
    private int backboneStop;

    private int mapKey;
    private String chromosome;
    private int start;
    private int stop;
    private String orientation; // '+' or '-'
    private int chainLevel;
    private String chainType;

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

    public int getBackboneStart() {
        return backboneStart;
    }

    public void setBackboneStart(int backboneStart) {
        this.backboneStart = backboneStart;
    }

    public int getBackboneStop() {
        return backboneStop;
    }

    public void setBackboneStop(int backboneStop) {
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getChainLevel() {
        return chainLevel;
    }

    public void setChainLevel(int chainLevel) {
        this.chainLevel = chainLevel;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }
}
