package edu.mcw.rgd.datamodel;

public class ReportPositionDE {
    private long rgdId;
    private String assembly;
    private String chromosome;
    private Integer startPos;
    private Integer stopPos;

    public long getRgdId() {
        return rgdId;
    }

    public void setRgdId(long rgdId) {
        this.rgdId = rgdId;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public Integer getStopPos() {
        return stopPos;
    }

    public void setStopPos(Integer stopPos) {
        this.stopPos = stopPos;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }
}
