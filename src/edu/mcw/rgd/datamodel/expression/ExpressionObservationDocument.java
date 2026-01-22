package edu.mcw.rgd.datamodel.expression;

public class ExpressionObservationDocument {

    private String gene;
    private int geneRgdId;
    private String sample;
    private String condition;
    private Double tpm;
    private String level;
    private long geneStart;
    private long geneStop;
    private String chr;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Double getTpm() {
        return tpm;
    }

    public void setTpm(Double tpm) {
        this.tpm = tpm;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getGeneStart() {
        return geneStart;
    }

    public void setGeneStart(long geneStart) {
        this.geneStart = geneStart;
    }

    public long getGeneStop() {
        return geneStop;
    }

    public void setGeneStop(long geneStop) {
        this.geneStop = geneStop;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }
}
