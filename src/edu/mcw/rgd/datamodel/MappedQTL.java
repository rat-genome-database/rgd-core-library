package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: 2/13/20
 */
public class MappedQTL {

    private QTL q;
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

    public QTL getQTL() {
        return q;
    }

    public void setQTL(QTL g) {
        this.q = q;
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
