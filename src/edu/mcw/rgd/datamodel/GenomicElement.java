package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 2/22/12
 * Time: 9:28 AM
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

    private int speciesTypeKey;
    private int objectKey;
    private String objectStatus;

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
        .put("SPECIES_TYPE_KEY", speciesTypeKey)
        .put("OBJECT_KEY", objectKey)
        .put("OBJECT_STATUS", objectStatus);
    }
}
