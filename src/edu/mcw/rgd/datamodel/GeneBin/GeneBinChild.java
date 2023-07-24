package edu.mcw.rgd.datamodel.GeneBin;

public class GeneBinChild {
    private String childTermAcc;
    private String childTerm;

    public GeneBinChild(String childTermAcc, String childTerm) {
        this.childTermAcc = childTermAcc;
        this.childTerm = childTerm;
    }

    public String getChildTermAcc() {
        return childTermAcc;
    }

    public void setChildTermAcc(String childTermAcc) {
        this.childTermAcc = childTermAcc;
    }

    public String getChildTerm() {
        return childTerm;
    }

    public void setChildTerm(String childTerm) {
        this.childTerm = childTerm;
    }
}
