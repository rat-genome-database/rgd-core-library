package edu.mcw.rgd.datamodel;

public class MappedStrain {
    private Strain s;
    private long start;
    private long stop;
    private String chromosome;
    private int mapKey;
    private String strand;

    public Strain getStrain() {
        return s;
    }

    public void setStrain(Strain s) {
        this.s = s;
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

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }
}
