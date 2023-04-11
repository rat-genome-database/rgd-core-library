package edu.mcw.rgd.datamodel.GeneBin;

public class GeneBin {
    private int rgdId;
    private String geneSymbol;
    private String termAcc;
    private String term;

    @Override
    public String toString() {
        return "GeneBin{" +
                "rgdId=" + rgdId +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", termAcc='" + termAcc + '\'' +
                ", term='" + term + '\'' +
                '}';
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
