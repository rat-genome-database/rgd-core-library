package edu.mcw.rgd.datamodel.GeneBin;

public class GeneBinAssignee {
    private String termAcc;
    private String term;
    private String assignee;
    private int completed;
    private int totalGenes;
    private int isParent;
    private int subsetNum;


    public GeneBinAssignee() {

    }
    public GeneBinAssignee(String termAcc, String term, String assignee) {
        this.termAcc = termAcc;
        this.term = term;
        this.assignee = assignee;
    }

    public GeneBinAssignee(String termAcc, String term, String assignee, int completed) {
        this.termAcc = termAcc;
        this.term = term;
        this.assignee = assignee;
        this.completed = completed;
    }

    public GeneBinAssignee(String termAcc, String term, String assignee, int completed, int totalGenes) {
        this.termAcc = termAcc;
        this.term = term;
        this.assignee = assignee;
        this.completed = completed;
        this.totalGenes = totalGenes;
    }

    public GeneBinAssignee(String termAcc, String term, String assignee, int completed, int totalGenes, int isParent) {
        this.termAcc = termAcc;
        this.term = term;
        this.assignee = assignee;
        this.completed = completed;
        this.totalGenes = totalGenes;
        this.isParent = isParent;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public int getTotalGenes() {
        return totalGenes;
    }

    public void setTotalGenes(int totalGenes) {
        this.totalGenes = totalGenes;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public int getSubsetNum() {
        return subsetNum;
    }

    public void setSubsetNum(int subsetNum) {
        this.subsetNum = subsetNum;
    }

    @Override
    public String toString() {
        return "GeneBinAssignee{" +
                "termAcc='" + termAcc + '\'' +
                ", term='" + term + '\'' +
                ", assignee='" + assignee + '\'' +
                ", completed=" + completed +
                '}';
    }
}
