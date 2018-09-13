package edu.mcw.rgd.process.primerCreate;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 10/7/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnsemblExon {
    String ensExonId;
    int ensExonStart;
    int ensExonStop;
    String ensExonStrand;
    String ensExonName;
    String ensExonPhase;
    String exonNumber;
    String exonParentTranscriptAccId;
    String equalExon="";


    public String getEqualExon() {
        return equalExon;
    }

    public void setEqualExon(String equalExon) {
        this.equalExon = equalExon;
    }

    public String getExonParentTranscriptAccId() {
        return exonParentTranscriptAccId;
    }

    public void setExonParentTranscriptAccId(String exonParentTranscriptAccId) {
        this.exonParentTranscriptAccId = exonParentTranscriptAccId;
    }

    public String getExonNumber() {
        return exonNumber;
    }

    public void setExonNumber(String exonNumber) {
        this.exonNumber = exonNumber;
    }

    public String getEnsExonName() {
        return ensExonName;
    }

    public void setEnsExonName(String ensExonName) {
        this.ensExonName = ensExonName;
    }

    public String getEnsExonPhase() {
        return ensExonPhase;
    }

    public void setEnsExonPhase(String ensExonPhase) {
        this.ensExonPhase = ensExonPhase;
    }

    public String getEnsExonId() {
        return ensExonId;
    }

    public void setEnsExonId(String ensExonId) {
        this.ensExonId = ensExonId;
    }

    public int getEnsExonStart() {
        return ensExonStart;
    }

    public void setEnsExonStart(int ensExonStart) {
        this.ensExonStart = ensExonStart;
    }

    public int getEnsExonStop() {
        return ensExonStop;
    }

    public void setEnsExonStop(int ensExonStop) {
        this.ensExonStop = ensExonStop;
    }

    public String getEnsExonStrand() {
        return ensExonStrand;
    }

    public void setEnsExonStrand(String ensExonStrand) {
        this.ensExonStrand = ensExonStrand;
    }
}
