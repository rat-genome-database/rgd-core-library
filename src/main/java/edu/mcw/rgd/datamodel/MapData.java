package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

/**
 * @author jdepons
 * @since Apr 7, 2009
 */
public class MapData implements Identifiable, Cloneable, Dumpable {

    private int key;
    private String fOrP;
    private String chromosome;
    private String fishBand;
    private Double absPosition;
    private Double lod;
    private String notes;
    private Integer mapKey;
    private int rgdId;
    private Integer rgdIdUp;
    private Integer rgdIdDown;
    private Integer startPos;
    private Integer stopPos;
    private String multipleChromosome;
    private String strand;
    private Integer mapsDataPositionMethodId;
    private String srcPipeline;

    /**
     * two MapsData objects are considered equal if their map keys and rgd_ids are the same
     * @param o MapData object to compare
     * @return
     */
    public boolean equals(Object o) {
        if( o==null )
            return false;
        MapData mapData = (MapData) o;
        return this.getRgdId()==mapData.getRgdId() &&
               this.getMapKey().equals(mapData.getMapKey());
    }

    @Override
    public int hashCode() {
        return this.rgdId ^ (this.getMapKey()==null ? 0 : this.getMapKey());
    }

    /**
     * returns a clone of this object
     * @return a clone of this object
     * @throws CloneNotSupportedException
     */
    public MapData clone() throws CloneNotSupportedException {
        return (MapData) super.clone();
    }

    /**
     * return true if both objects have the same genomic coordinates: map key, chromosome, start and stop positions and strand;
     * absolute position, fish band and map positioning method are also compared
     * @param md object to test for equality of genomic positions
     * @return true if both objects refer to same genomic range on same map
     */
    public boolean equalsByGenomicCoords(MapData md) {
        int mapKey1 = this.getMapKey();
        String chr1 = this.getChromosome()!=null ? this.getChromosome() : "";
        String fishBand1 = this.getFishBand()!=null ? this.getFishBand() : "";
        String strand1 = this.getStrand()!=null ? this.getStrand() : "";
        int startPos1 = this.getStartPos()!=null ? this.getStartPos() : 0;
        int stopPos1 = this.getStopPos()!=null ? this.getStopPos() : 0;
        int mapPosMethod1 = this.getMapsDataPositionMethodId()!=null ? this.getMapsDataPositionMethodId() : 0;

        int mapKey2 = md.getMapKey();
        String chr2 = md.getChromosome()!=null ? md.getChromosome() : "";
        String fishBand2 = md.getFishBand()!=null ? md.getFishBand() : "";
        String strand2 = md.getStrand()!=null ? md.getStrand() : "";
        int startPos2 = md.getStartPos()!=null ? md.getStartPos() : 0;
        int stopPos2 = md.getStopPos()!=null ? md.getStopPos() : 0;
        int mapPosMethod2 = md.getMapsDataPositionMethodId()!=null ? md.getMapsDataPositionMethodId() : 0;

        return mapKey1==mapKey2 && startPos1==startPos2 && stopPos1==stopPos2 && fishBand1.equals(fishBand2) &&
                chr1.equalsIgnoreCase(chr2) && strand1.equals(strand2) && compareAbsPos(md.getAbsPosition())==0
                && mapPosMethod1==mapPosMethod2;

    }

    /**
     * compare two doubles in context of being absolute position on the genomic map;
     * two absolute positions are identical if they are the same up to 5th decimal position;
     * 2.300004 and 2.299996 are considered equal (they represent absPos of 2.30000)
     * @param absPosition2 another absolute position to compare
     * @return 0 if equal, -1 if less, 1 if greater than another abs position
     */
    public int compareAbsPos(Double absPosition2) {

        long pos1 = absPosition==null ? 0 : Math.round(absPosition*10000);
        long pos2 = absPosition2==null ? 0 : Math.round(absPosition2*10000);
        return (int)(pos1 - pos2);
    }

    /**
     * returns description of map positioning method
     * @return description of map positioning method
     */
    public String getMapPositionMethod() {
        if( mapsDataPositionMethodId==null )
            return "";
        switch( mapsDataPositionMethodId ) {
            case 1: return "1 - by flanking markers";
            case 2: return "2 - by one flank and peak markers";
            case 3: return "3 - by peak only";
            case 4: return "4 - by one flank marker only";
            case 5: return "5 - by peak with adj size";
            case 6: return "6 - imported from external source";
            default: return Integer.toString(mapsDataPositionMethodId);
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getFOrP() {
        return fOrP;
    }

    public void setFOrP(String fOrP) {
        this.fOrP = fOrP;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getFishBand() {
        return fishBand;
    }

    public void setFishBand(String fishBand) {
        this.fishBand = fishBand;
    }

    public Double getAbsPosition() {
        return absPosition;
    }

    public void setAbsPosition(Double absPosition) {
        this.absPosition = absPosition;
    }

    public Double getLod() {
        return lod;
    }

    public void setLod(Double lod) {
        this.lod = lod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getMapKey() {
        return mapKey;
    }

    public void setMapKey(Integer mapKey) {
        this.mapKey = mapKey;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public Integer getRgdIdUp() {
        return rgdIdUp;
    }

    public void setRgdIdUp(Integer rgdIdUp) {
        this.rgdIdUp = rgdIdUp;
    }

    public Integer getRgdIdDown() {
        return rgdIdDown;
    }

    public void setRgdIdDown(Integer rgdIdDown) {
        this.rgdIdDown = rgdIdDown;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public Integer getStopPos() {
        return stopPos;
    }

    public void setStopPos(Integer stopPos) {
        this.stopPos = stopPos;
    }

    public String getMultipleChromosome() {
        return multipleChromosome;
    }

    public void setMultipleChromosome(String multipleChromosome) {
        this.multipleChromosome = multipleChromosome;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public Integer getMapsDataPositionMethodId() {
        return mapsDataPositionMethodId;
    }

    public void setMapsDataPositionMethodId(Integer mapsDataPositionMethodId) {
        this.mapsDataPositionMethodId = mapsDataPositionMethodId;
    }

    public String getSrcPipeline() {
        return srcPipeline;
    }

    public void setSrcPipeline(String srcPipeline) {
        this.srcPipeline = srcPipeline;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(64);
        buf.append("map:").append(getMapKey());
        buf.append(" chr:").append(getChromosome());
        if( getFishBand()!=null )
            buf.append(" fishband:").append(getFishBand());
        if( getStartPos()!=null )
            buf.append(' ').append(getStartPos()).append("..").append(getStopPos());
        if( getStrand()!=null && getStrand().length()>0 )
            buf.append('(').append(getStrand()).append(')');
        if( getAbsPosition()!=null )
            buf.append(" abspos:").append(getAbsPosition());
        if( getMapsDataPositionMethodId()!=null )
            buf.append(" mapPosMethod:").append(getMapsDataPositionMethodId());
        return buf.toString();
    }

    public String dump(String delimiter) {

        // we are skipping null fields and zero int fields
        return new Dumper(delimiter, true, true)
            .put("MAPS_DATA_KEY", key)
            .put("F_OR_P", fOrP)
            .put("CHROMOSOME", chromosome)
            .put("FISH_BAND", fishBand)
            .put("ABS_POSITION", absPosition)
            .put("LOD", lod)
            .put("NOTES", notes)
            .put("MAP_KEY", mapKey)
            .put("RGD_ID", rgdId)
            .put("RGD_ID_UP", rgdIdUp)
            .put("RGD_ID_DW", rgdIdDown)
            .put("START_POS", startPos)
            .put("STOP_POS", stopPos)
            .put("STRAND", strand)
            .put("SRC_PIPELINE", srcPipeline)
            .put("MULTIPLE_CHROMOSOME", multipleChromosome)
            .put("MAPS_DATA_POSITION_METHOD_ID", mapsDataPositionMethodId)
            .dump();
    }
}
