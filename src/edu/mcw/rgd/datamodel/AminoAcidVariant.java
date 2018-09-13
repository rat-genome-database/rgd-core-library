package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jul 21, 2010
 * Time: 4:59:55 PM
 */
public class AminoAcidVariant {

    String referenceAminoAcid = "";
    String variantAminoAcid = "";
    String geneSpliceStatus = "";
    String polyPhenStatus = "";
    long id; // variant_transcript_id
    int transcriptRGDId;
    long variantId;
    String synonymousFlag = "";
    String location = "";
    String transcriptSymbol = "";
    String nearSpliceSite = "";
    String tripletError = "";
    String aaSequence = "";
    String dnaSequence="";
    int aaPosition=-1;
    int dnaPosition=-1;


    public int getAaPosition() {
        return aaPosition;
    }

    public void setAaPosition(int aaPosition) {
        this.aaPosition = aaPosition;
    }

    public int getDnaPosition() {
        return dnaPosition;
    }

    public void setDnaPosition(int dnaPosition) {
        this.dnaPosition = dnaPosition;
    }

    public String getAASequence() {
        return aaSequence;
    }

    public void setAASequence(String aaSequence) {
        this.aaSequence = aaSequence;
    }

    public String getDNASequence() {
        return dnaSequence;
    }

    public void setDNASequence(String dnaSequence) {
        this.dnaSequence = dnaSequence;
    }

    public String getTranscriptSymbol() {
        return transcriptSymbol;
    }

    public void setTranscriptSymbol(String transcriptSymbol) {
        this.transcriptSymbol = transcriptSymbol;
    }

    public String getSynonymousFlag() {
        return synonymousFlag;
    }

    public void setSynonymousFlag(String synonymousFlag) {
        this.synonymousFlag = synonymousFlag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTranscriptRGDId() {
        return transcriptRGDId;
    }

    public void setTranscriptRGDId(int transcriptRGDId) {
        this.transcriptRGDId = transcriptRGDId;
    }

    public long getVariantId() {
        return variantId;
    }

    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    public String getReferenceAminoAcid() {
        return referenceAminoAcid;
    }

    public void setReferenceAminoAcid(String referenceAminoAcid) {
        this.referenceAminoAcid = referenceAminoAcid;
    }

    public String getVariantAminoAcid() {
        return variantAminoAcid;
    }

    public void setVariantAminoAcid(String variantAminoAcid) {
        this.variantAminoAcid = variantAminoAcid;
    }

    public String getPolyPhenStatus() {
        return polyPhenStatus;
    }

    public void setPolyPhenStatus(String polyPhenStatus) {
        this.polyPhenStatus = polyPhenStatus;
    }

    public String getGeneSpliceStatus() {
        return geneSpliceStatus;
    }

    public void setGeneSpliceStatus(String geneSpliceStatus) {
        this.geneSpliceStatus = geneSpliceStatus;
    }

    public String getNearSpliceSite() {
        return nearSpliceSite;
    }

    public void setNearSpliceSite(String nearSpliceSite) {
        this.nearSpliceSite = nearSpliceSite;
    }

    public String getTripletError() {

        if (tripletError == null) {
            return "F";
        }

        return tripletError;
    }

    public void setTripletError(String tripletError) {
        this.tripletError = tripletError;
    }
}
