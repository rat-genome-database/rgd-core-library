package edu.mcw.rgd.datamodel;

/**
 * @author mtutaj
 * @since Jun 25, 2010
 */
public class TranscriptFeature extends MapData {

    // genomic feature type of a transcript
    public enum FeatureType {EXON, INTRON, UTR5, UTR3, PROMOTER, CDS, TRANSCRIPT, UNKNOWN};

    int transcriptRgdId; // rgd id of transcript this feature is tied to
    FeatureType type; // feature type

    /**
     * returns a clone of this object
     * @return a clone of this object
     * @throws CloneNotSupportedException
     */
    public TranscriptFeature clone() throws CloneNotSupportedException {
        return (TranscriptFeature) super.clone();
    }

    /**
     * return object key for a given genomic feature
     * @param feature genomic feature in question
     * @return object key for the genomic feature or 0
     */
    static public int getObjectKey(FeatureType feature) {
        int objectKey;
        switch( feature ) {
            case EXON: objectKey = 15; break;
            case PROMOTER: objectKey = 16; break;
            case UTR5: objectKey = 17; break;
            case CDS: objectKey = 18; break;
            case INTRON: objectKey = 19; break;
            case UTR3: objectKey = 20; break;
            case TRANSCRIPT: objectKey = 21; break;
            default: objectKey = 0; break;
        }
        return objectKey;
    }

    /**
     * return genomic feature given object key
     * @param objectKey object key
     * @return genomic feature for given object key
     */
    static public FeatureType getFeatureType(int objectKey) {
        switch( objectKey ) {
            case 15: return FeatureType.EXON;
            case 16: return FeatureType.PROMOTER;
            case 17: return FeatureType.UTR5;
            case 18: return FeatureType.CDS;
            case 19: return FeatureType.INTRON;
            case 20: return FeatureType.UTR3;
            case 21: return FeatureType.TRANSCRIPT;
            default: return FeatureType.UNKNOWN;
        }
    }

    public TranscriptFeature() {
    }

    public TranscriptFeature( MapData md ) {
        this.setAbsPosition(md.getAbsPosition());
        this.setChromosome(md.getChromosome());
        this.setFishBand(md.getFishBand());
        this.setFOrP(md.getFOrP());
        this.setKey(md.getKey());
        this.setLod(md.getLod());
        this.setMapKey(md.getMapKey());
        this.setMapsDataPositionMethodId(md.getMapsDataPositionMethodId());
        this.setMultipleChromosome(md.getMultipleChromosome());
        this.setNotes(md.getNotes());
        this.setRgdId(md.getRgdId());
        this.setRgdIdDown(md.getRgdIdDown());
        this.setRgdIdUp(md.getRgdIdUp());
        this.setStartPos(md.getStartPos());
        this.setStopPos(md.getStopPos());
        this.setStrand(md.getStrand());
    }

    public int getTranscriptRgdId() {
        return transcriptRgdId;
    }

    public void setTranscriptRgdId(int transcriptRgdId) {
        this.transcriptRgdId = transcriptRgdId;
    }

    public FeatureType getFeatureType() {
        return type;
    }

    public void setFeatureType(FeatureType type) {
        this.type = type;
    }

    /**
     * get canonical name for the feature (name according to Sequence Ontology)
     * @return canonical name
     */
    public String getCanonicalName() {
       switch( getFeatureType() ) {
           case EXON: return "exon";
           case UTR3: return "three_prime_UTR";
           case UTR5: return "five_prime_UTR";
           case INTRON: return "intron";
           case CDS: return "CDS";
           case PROMOTER: return "promoter";
           case TRANSCRIPT: return "transcript";
           default: return "unknown";
       }
    }

    @Override
     public String toString() {
        return getFeatureType().toString()+' '+super.toString();
    }
}
