package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * Represents a gene ortholog --
 * - ortholog relation 
 */
public class MappedOrtholog  {

    private int key;       // GENETOGENE_KEY
    private int srcRgdId;  // source gene rgd id
    private int srcSpeciesTypeKey; // species type key for source gene rgd id
    private int destRgdId; // destination gene rgd id
    private int destSpeciesTypeKey; // species type key for dest gene rgd id
    private String srcGeneSymbol;
    private String destGeneSymbol;
    private String srcChromosome;
    private String destChromosome;
    private long srcStartPos ;
    private long destStartPos;
    private long srcStopPos;
    private long destStopPos;
    private String srcStrand;
    private String destStrand;


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getSrcRgdId() {
        return srcRgdId;
    }

    public void setSrcRgdId(int srcRgdId) {
        this.srcRgdId = srcRgdId;
    }

    public int getSrcSpeciesTypeKey() {
        return srcSpeciesTypeKey;
    }

    public void setSrcSpeciesTypeKey(int srcSpeciesTypeKey) {
        this.srcSpeciesTypeKey = srcSpeciesTypeKey;
    }

    public int getDestRgdId() {
        return destRgdId;
    }

    public void setDestRgdId(int destRgdId) {
        this.destRgdId = destRgdId;
    }

    public int getDestSpeciesTypeKey() {
        return destSpeciesTypeKey;
    }

    public void setDestSpeciesTypeKey(int destSpeciesTypeKey) {
        this.destSpeciesTypeKey = destSpeciesTypeKey;
    }

    public String getSrcGeneSymbol() {
        return srcGeneSymbol;
    }

    public void setSrcGeneSymbol(String srcGeneSymbol) {
        this.srcGeneSymbol = srcGeneSymbol;
    }

    public String getDestGeneSymbol() {
        return destGeneSymbol;
    }

    public void setDestGeneSymbol(String destGeneSymbol) {
        this.destGeneSymbol = destGeneSymbol;
    }

    public String getSrcChromosome() {
        return srcChromosome;
    }

    public void setSrcChromosome(String srcChromosome) {
        this.srcChromosome = srcChromosome;
    }

    public long getSrcStartPos() {
        return srcStartPos;
    }

    public String getDestChromosome() {
        return destChromosome;
    }

    public void setDestChromosome(String destChromosome) {
        this.destChromosome = destChromosome;
    }

    public void setSrcStartPos(long srcStartPos) {
        this.srcStartPos = srcStartPos;
    }

    public long getDestStartPos() {
        return destStartPos;
    }

    public void setDestStartPos(long destStartPos) {
        this.destStartPos = destStartPos;
    }

    public long getSrcStopPos() {
        return srcStopPos;
    }

    public void setSrcStopPos(long srcStopPos) {
        this.srcStopPos = srcStopPos;
    }

    public long getDestStopPos() {
        return destStopPos;
    }

    public void setDestStopPos(long destStopPos) {
        this.destStopPos = destStopPos;
    }

    public String getSrcStrand() {
        return srcStrand;
    }

    public void setSrcStrand(String srcStrand) {
        this.srcStrand = srcStrand;
    }

    public String getDestStrand() {
        return destStrand;
    }

    public void setDestStrand(String destStrand) {
        this.destStrand = destStrand;
    }
}
