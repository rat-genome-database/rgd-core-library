package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

/**
 * @author mtutaj
 * @since 2/22/12
 * represents a row in GENOMIC_ELEMENTS table
 */
public class GenomicElement implements Identifiable, Speciated, ObjectWithName, ObjectWithSymbol, Dumpable {

    private int rgdId;
    private String objectType;
    private String symbol;
    private String name;
    private String description;
    private String source;
    private String soAccId; // sequence ontology accession id
    private String notes;
    private String genomicAlteration;

    private int speciesTypeKey;
    private int objectKey;
    private String objectStatus;

    // we exclude rgdId from equality checks because new incoming data does not have an rgdId assigned yet ...
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenomicElement that = (GenomicElement) o;

        return this.speciesTypeKey == that.speciesTypeKey
            && this.objectKey == that.objectKey
            && Utils.stringsAreEqual(this.objectType, that.objectType)
            && Utils.stringsAreEqual(this.symbol, that.symbol)
            && Utils.stringsAreEqual(this.name, that.name)
            && Utils.stringsAreEqual(this.description, that.description)
            && Utils.stringsAreEqual(this.source, that.source)
            && Utils.stringsAreEqual(this.soAccId, that.soAccId)
            && Utils.stringsAreEqual(this.notes, that.notes)
            && Utils.stringsAreEqual(this.genomicAlteration, that.genomicAlteration)
            && Utils.stringsAreEqual(this.objectStatus, that.objectStatus);
    }

    @Override
    public int hashCode() {
        return speciesTypeKey
            ^ objectKey
            ^ Utils.defaultString(objectType).hashCode()
            ^ Utils.defaultString(symbol).hashCode()
            ^ Utils.defaultString(name).hashCode()
            ^ Utils.defaultString(description).hashCode()
            ^ Utils.defaultString(source).hashCode()
            ^ Utils.defaultString(soAccId).hashCode()
            ^ Utils.defaultString(notes).hashCode()
            ^ Utils.defaultString(genomicAlteration).hashCode()
            ^ Utils.defaultString(objectStatus).hashCode();
    }

    /**
     * get key for genomic elements -- not supported
     * @deprecated
     * @return always 0
     */
    public int getKey() {
        return 0;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSoAccId() {
        return soAccId;
    }

    public void setSoAccId(String soAccId) {
        this.soAccId = soAccId;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(String objectStatus) {
        this.objectStatus = objectStatus;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(int objectKey) {
        this.objectKey = objectKey;
    }

    public String getGenomicAlteration() {
        return genomicAlteration;
    }

    public void setGenomicAlteration(String genomicAlteration) {
        this.genomicAlteration = genomicAlteration;
    }

    public String dump(String delimiter) {

        Dumper dumper = new Dumper(delimiter);
        populateDumper(dumper);
        return dumper.dump();
    }

    protected void populateDumper(Dumper dumper) {
        dumper
        .put("RGD_ID", rgdId)
        .put("OBJECT_TYPE", objectType)
        .put("SYMBOL", symbol)
        .put("NAME", name)
        .put("DESCRIPTION", description)
        .put("SOURCE", source)
        .put("SO_ACC_ID", soAccId)
        .put("NOTES", notes)
        .put("GENOMIC_ALTERATION", genomicAlteration)
        .put("SPECIES_TYPE_KEY", speciesTypeKey)
        .put("OBJECT_KEY", objectKey)
        .put("OBJECT_STATUS", objectStatus);
    }
}
