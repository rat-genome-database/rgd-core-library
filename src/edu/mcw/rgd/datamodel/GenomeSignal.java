package edu.mcw.rgd.datamodel;

public class GenomeSignal {
    private String chromosome;
    private int signalValue;
    private int setId;
    private long position;

    public GenomeSignal(String chromosome, int signalValue, long position, int setId) {
        this.chromosome = chromosome;
        this.signalValue = signalValue;
        this.position = position;
        this.setId = setId;
    }


    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(int signalValue) {
        this.signalValue = signalValue;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }
}
