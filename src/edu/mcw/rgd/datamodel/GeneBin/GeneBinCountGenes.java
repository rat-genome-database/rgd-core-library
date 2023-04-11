package edu.mcw.rgd.datamodel.GeneBin;

public class GeneBinCountGenes {
    private String termAcc;
    private int totalGenes;

    public GeneBinCountGenes(String termAcc, int totalGenes) {
        this.termAcc = termAcc;
        this.totalGenes = totalGenes;
    }

    public GeneBinCountGenes() {

    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public int getTotalGenes() {
        return totalGenes;
    }

    public void setTotalGenes(int totalGenes) {
        this.totalGenes = totalGenes;
    }
}
