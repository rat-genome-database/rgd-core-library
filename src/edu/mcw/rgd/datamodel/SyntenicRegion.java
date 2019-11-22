package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 1/29/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyntenicRegion {

    private int mapKey1;
    private int mapKey2;
    private String chromosome1;
    private String chromosome2;
    private int startPos1;
    private int startPos2;
    private int stopPos1;
    private int stopPos2;
    private int orientation;

    public int getMapKey1() {
        return mapKey1;
    }

    public void setMapKey1(int mapKey1) {
        this.mapKey1 = mapKey1;
    }

    public int getMapKey2() {
        return mapKey2;
    }

    public void setMapKey2(int mapKey2) {
        this.mapKey2 = mapKey2;
    }

    public String getChromosome1() {
        return chromosome1;
    }

    public void setChromosome1(String chromosome1) {
        this.chromosome1 = chromosome1;
    }

    public String getChromosome2() {
        return chromosome2;
    }

    public void setChromosome2(String chromosome2) {
        this.chromosome2 = chromosome2;
    }

    public int getStartPos1() {
        return startPos1;
    }

    public void setStartPos1(int startPos1) {
        this.startPos1 = startPos1;
    }

    public int getStartPos2() {
        return startPos2;
    }

    public void setStartPos2(int startPos2) {
        this.startPos2 = startPos2;
    }

    public int getStopPos1() {
        return stopPos1;
    }

    public void setStopPos1(int stopPos1) {
        this.stopPos1 = stopPos1;
    }

    public int getStopPos2() {
        return stopPos2;
    }

    public void setStopPos2(int stopPos2) {
        this.stopPos2 = stopPos2;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
