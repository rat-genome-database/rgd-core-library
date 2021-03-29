package edu.mcw.rgd.datamodel;

/**
 * Created by jthota on 12/20/2019.
 */
public class GeneLoci {
    private String chromosome;
    private int mapKey;
    private long position;
    private String genicStatus;
    private String geneSymbols;

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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(String genicStatus) {
        this.genicStatus = genicStatus;
    }

    public String getGeneSymbols() {
        return geneSymbols;
    }

    public void setGeneSymbols(String geneSymbols) {
        this.geneSymbols = geneSymbols;
    }
}
