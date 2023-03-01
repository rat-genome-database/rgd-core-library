package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.datamodel.prediction.PolyPhenPrediction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jul 21, 2010
 * Time: 3:51:13 PM
 */
public class TranscriptResult {


    private String transcriptId;
    public List<PolyPhenPrediction> polyPhenPrediction = new ArrayList<PolyPhenPrediction>();
    private String location = "";
    private AminoAcidVariant aminoAcidVariant;

    public AminoAcidVariant getAminoAcidVariant() {
        return aminoAcidVariant;
    }

    public void setAminoAcidVariant(AminoAcidVariant aminoAcidVariant) {
        this.aminoAcidVariant = aminoAcidVariant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<PolyPhenPrediction> getPolyPhenPrediction() {
        return polyPhenPrediction;
    }

    public void setPolyPhenPrediction(List<PolyPhenPrediction> polyPhenPrediction) {
        this.polyPhenPrediction = polyPhenPrediction;
    }

    public String getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(String transcriptId) {
        this.transcriptId = transcriptId;
    }
}
