package edu.mcw.rgd.datamodel.ontology;

import java.util.HashMap;

/**
 * @author mtutaj
 * @since 3/24/2017
 * Corresponds to data object required by AGR for DAF annotations
 */
public class DafAnnotation {

    private String taxon;
    private String dbObjectType;
    private String db;
    private String dbObjectID;
    private String dbObjectSymbol;
    private String inferredGeneAssociation;
    private String geneProductFormID;
    private String associationType;
    private String qualifier;
    private String doId;
    private String withInfo;
    private String evidenceCode;
    private String dbReference;
    private String createdDate;
    // map of data providers: {data-provider-name, provider-type}
    // data-provider-name: 'RGD','OMIM', ...
    // provider-type: 'curated','loaded'
    private HashMap<String,String> dataProviders;

    public String getTaxon() {
        return taxon;
    }

    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }

    public String getDbObjectType() {
        return dbObjectType;
    }

    public void setDbObjectType(String dbObjectType) {
        this.dbObjectType = dbObjectType;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getDbObjectID() {
        return dbObjectID;
    }

    public void setDbObjectID(String dbObjectID) {
        this.dbObjectID = dbObjectID;
    }

    public String getDbObjectSymbol() {
        return dbObjectSymbol;
    }

    public void setDbObjectSymbol(String dbObjectSymbol) {
        this.dbObjectSymbol = dbObjectSymbol;
    }

    public String getInferredGeneAssociation() {
        return inferredGeneAssociation;
    }

    public void setInferredGeneAssociation(String inferredGeneAssociation) {
        this.inferredGeneAssociation = inferredGeneAssociation;
    }

    public String getGeneProductFormID() {
        return geneProductFormID;
    }

    public void setGeneProductFormID(String geneProductFormID) {
        this.geneProductFormID = geneProductFormID;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getDoId() {
        return doId;
    }

    public void setDoId(String doId) {
        this.doId = doId;
    }

    public String getWithInfo() {
        return withInfo;
    }

    public void setWithInfo(String withInfo) {
        this.withInfo = withInfo;
    }

    public String getEvidenceCode() {
        return evidenceCode;
    }

    public void setEvidenceCode(String evidenceCode) {
        this.evidenceCode = evidenceCode;
    }

    public String getDbReference() {
        return dbReference;
    }

    public void setDbReference(String dbReference) {
        this.dbReference = dbReference;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public HashMap<String, String> getDataProviders() {
        return dataProviders;
    }

    public void setDataProviders(HashMap<String, String> dataProviders) {
        this.dataProviders = dataProviders;
    }
}
