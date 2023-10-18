package edu.mcw.rgd.datamodel.prediction;


/**
 * Holds result of Polyphen result from POLYPHEN table. Not all fields are read in as
 * they are not needed for the UI
 * User: jdepons
 * Date: Aug 31, 2009
 * Time: 2:49:26 PM
 */
public class PolyPhenPrediction extends Prediction{

    private long id;
    private String prediction;
    private String basis;
    private String score1;
    private String score2;
    private String diff;
    private String numObserved;
    private String proteinId;
    private String invertedFlag;
    private String effect;
    private String site;
    private String numStructureFilt;
    private String pdbId;
    private int position;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getNumObserved() {
        return numObserved;
    }

    public void setNumObserved(String numObserved) {
        this.numObserved = numObserved;
    }

    public String getProteinId() {
        return proteinId;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }

    public String getInvertedFlag() {
        return invertedFlag;
    }

    public void setInvertedFlag(String invertedFlag) {
        this.invertedFlag = invertedFlag;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getNumStructureFilt() {
        return numStructureFilt;
    }

    public void setNumStructureFilt(String numStructureFilt) {
        this.numStructureFilt = numStructureFilt;
    }

    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }
}
