package edu.mcw.rgd.datamodel.search;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 11/8/12
 * Time: 8:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class Position {
    String chromosome = "";
    int start;
    int stop;

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
}
