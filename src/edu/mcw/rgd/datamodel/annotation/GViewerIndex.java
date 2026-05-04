package edu.mcw.rgd.datamodel.annotation;

public class GViewerIndex {
    private int annotatedObjectRgdId;
    private String objectSymbol;
    private String chromosome;
    private int startPos;
    private int stopPos;
    private String objectType;
    private String term;
    private String termAcc;

    public int getAnnotatedObjectRgdId() {
        return annotatedObjectRgdId;
    }

    public void setAnnotatedObjectRgdId(int annotatedObjectRgdId) {
        this.annotatedObjectRgdId = annotatedObjectRgdId;
    }

    public String getObjectSymbol() {
        return objectSymbol;
    }

    public void setObjectSymbol(String objectSymbol) {
        this.objectSymbol = objectSymbol;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getStopPos() {
        return stopPos;
    }

    public void setStopPos(int stopPos) {
        this.stopPos = stopPos;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }
}
