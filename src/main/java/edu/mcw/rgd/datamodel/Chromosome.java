package edu.mcw.rgd.datamodel;

/**
 * @author mtutaj
 * @since 12/4/14
 * represents a row from CHROMOSOMES table
 */
public class Chromosome {
    private int mapKey;
    private String chromosome;
    private String refseqId;
    private String genbankId;
    private int seqLength;
    private int gapLength;
    private int gapCount;
    private int contigCount;

    /**
     * get ordinal number for a chromosome, to facilitate chromosome sorting (ordering);
     * the order is defined as follows: 1 2 3 4 .. 20 21 22  X=50 Y=51 MT=52 (M=52);
     * note: 'chr' parameter could be given in lowercase ('x'), uppercase ('Y'), or mixed
     * @param chr chromosome
     * @return chromosome ordinal number
     */
    public static int getOrdinalNumber(String chr) {
        if( chr==null || chr.isEmpty() )
            return 0;
        int ordinal = 0;
        for( int i=0; i<chr.length(); i++ ) {
            char c = chr.charAt(i);
            if( Character.isDigit(c) ) {
                ordinal = 10*ordinal + Character.getNumericValue(c);
            } else if( c=='x' || c=='X' ) {
                ordinal = 50;
            } else if( c=='y' || c=='Y' ) {
                ordinal = 51;
            } else if( c=='m' || c=='M' || c=='t' || c=='T' ) {
                ordinal = 52;
            } else {
                ordinal = 10*ordinal + Character.getNumericValue(c);
            }
        }
        return ordinal;
    }

    /**
     * get ordinal number for a chromosome, to facilitate chromosome sorting (ordering);
     * the order is defined as follows: 1 2 3 4 .. 20 21 22  X=50 Y=51 MT=52 (M=52);
     * @return chromosome ordinal number
     */
    public int getOrdinalNumber() {
        return getOrdinalNumber(this.chromosome);
    }


    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getRefseqId() {
        return refseqId;
    }

    public void setRefseqId(String refseqId) {
        this.refseqId = refseqId;
    }

    public String getGenbankId() {
        return genbankId;
    }

    public void setGenbankId(String genbankId) {
        this.genbankId = genbankId;
    }

    public int getSeqLength() {
        return seqLength;
    }

    public void setSeqLength(int seqLength) {
        this.seqLength = seqLength;
    }

    public int getGapLength() {
        return gapLength;
    }

    public void setGapLength(int gapLength) {
        this.gapLength = gapLength;
    }

    public int getGapCount() {
        return gapCount;
    }

    public void setGapCount(int gapCount) {
        this.gapCount = gapCount;
    }

    public int getContigCount() {
        return contigCount;
    }

    public void setContigCount(int contigCount) {
        this.contigCount = contigCount;
    }
}
