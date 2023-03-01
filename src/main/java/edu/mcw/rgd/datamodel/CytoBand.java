package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 2/2/15
 * Time: 10:43 AM
 * <p>
 * represents a row in MAPS_CYTOBANDS table
 */
public class CytoBand {

    private int mapKey;
    private String chromosome;
    private int startPos;
    private int stopPos;
    private String bandName;
    private String giemsaStain;

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

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getStopPos() {
        return stopPos;
    }

    public void setStopPos(int stopPos) {
        this.stopPos = stopPos;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getGiemsaStain() {
        return giemsaStain;
    }

    public void setGiemsaStain(String giemsaStain) {
        this.giemsaStain = giemsaStain;
    }
}
