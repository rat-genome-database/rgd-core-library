package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.prediction.PolyPhenPrediction;

public class Polyphen extends PolyPhenPrediction {
    private int transcriptRgdid;

    public int getTranscriptRgdid() {
        return transcriptRgdid;
    }

    public void setTranscriptRgdid(int transcriptRgdid) {
        this.transcriptRgdid = transcriptRgdid;
    }
}
