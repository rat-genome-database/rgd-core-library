package edu.mcw.rgd.datamodel;

/**
 * @author jdepons
 * @since Jun 24, 2008
 */
public class Map implements Identifiable, ObjectWithName, Speciated {

    private int key;
    private String name;
    private String version;
    private String description;
    private String unit;
    private int rgdId;
    private int methodKey;
    private int speciesTypeKey;
    private String notes;
    private boolean primaryRefAssembly;
    private String dbsnpVersion;
    private int rank;
    private String ucscAssemblyId;
    private String refSeqAssemblyAcc; // GCF_xxxx
    private String refSeqAssemblyName;
    private String source;
    private String genBankAssemblyAcc; // GCA_xxxx

    public String getDbsnpVersion() {
        return dbsnpVersion;
    }

    public void setDbsnpVersion(String dbsnpVersion) {
        this.dbsnpVersion = dbsnpVersion;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        if (unit == null) {
            return "";
        }else {
            return unit;
        }
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getMethodKey() {
        return methodKey;
    }

    public void setMethodKey(int methodKey) {
        this.methodKey = methodKey;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPrimaryRefAssembly() {
        return primaryRefAssembly;
    }

    public void setPrimaryRefAssembly(boolean primaryRefAssembly) {
        this.primaryRefAssembly = primaryRefAssembly;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUcscAssemblyId() {
        return ucscAssemblyId;
    }

    public void setUcscAssemblyId(String ucscAssemblyId) {
        this.ucscAssemblyId = ucscAssemblyId;
    }

    public String getRefSeqAssemblyAcc() {
        return refSeqAssemblyAcc;
    }

    public void setRefSeqAssemblyAcc(String refSeqAssemblyAcc) {
        this.refSeqAssemblyAcc = refSeqAssemblyAcc;
    }

    public String getRefSeqAssemblyName() {
        return refSeqAssemblyName;
    }

    public void setRefSeqAssemblyName(String refSeqAssemblyName) {
        this.refSeqAssemblyName = refSeqAssemblyName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGenBankAssemblyAcc() {
        return genBankAssemblyAcc;
    }

    public void setGenBankAssemblyAcc(String genBankAssemblyAcc) {
        this.genBankAssemblyAcc = genBankAssemblyAcc;
    }
}
