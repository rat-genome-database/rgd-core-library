package edu.mcw.rgd.process.primerCreate;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 9/12/13
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnsemblTranscript {

    String ensTranscriptName;
    String ensTranscriptId;
    String ensGeneId;
    String trChr;
    int trStart;
    int trStop;
    String ccdsId;
    ArrayList<EnsemblExon> exonList = new ArrayList<EnsemblExon>();


    public String getCcdsId() {
        return ccdsId;
    }

    public void setCcdsId(String ccdsId) {
        this.ccdsId = ccdsId;
    }

    public ArrayList<EnsemblExon> getExonList() {
        return exonList;
    }

    public void setExonList(ArrayList<EnsemblExon> exonList) {
        this.exonList = exonList;
    }

    public String getTrChr() {
        return trChr;
    }

    public void setTrChr(String trChr) {
        this.trChr = trChr;
    }

    public int getTrStart() {
        return trStart;
    }

    public void setTrStart(int trStart) {
        this.trStart = trStart;
    }

    public int getTrStop() {
        return trStop;
    }

    public void setTrStop(int trStop) {
        this.trStop = trStop;
    }

    public String getEnsGeneId() {
        return ensGeneId;
    }

    public void setEnsGeneId(String ensGeneId) {
        this.ensGeneId = ensGeneId;
    }

    public String getEnsTranscriptName() {
        return ensTranscriptName;
    }

    public void setEnsTranscriptName(String ensTranscriptName) {
        this.ensTranscriptName = ensTranscriptName;
    }

    public String getEnsTranscriptId() {
        return ensTranscriptId;
    }

    public void setEnsTranscriptId(String ensTranscriptId) {
        this.ensTranscriptId = ensTranscriptId;
    }

}
