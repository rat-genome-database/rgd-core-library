package edu.mcw.rgd.datamodel.search.elasticsearch;

public class MapInfo {
    private String map;
    private String chromosome;
    private long startPos;
    private long stopPos;
    private int rank;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getStopPos() {
        return stopPos;
    }

    public void setStopPos(long stopPos) {
        this.stopPos = stopPos;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
