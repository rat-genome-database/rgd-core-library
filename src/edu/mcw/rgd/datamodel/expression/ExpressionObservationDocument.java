package edu.mcw.rgd.datamodel.expression;

public class ExpressionObservationDocument {

    private String gene;
    private String sample;
    private String condition;
    private Double tpm;
    private String level;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
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
}
