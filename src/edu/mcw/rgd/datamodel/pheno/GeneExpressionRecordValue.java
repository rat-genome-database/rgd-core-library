package edu.mcw.rgd.datamodel.pheno;

/**
 * Created by mtutaj on 10/31/2018.
 */
public class GeneExpressionRecordValue {

    private int id; // GENE_EXPRESSION_VALUE_ID
    private int expressedObjectRgdId;
    private String expressedGeneSymbol;
    private String expressionMeasurementAccId;
    private String notes; // EXPRESSION_VALUE_NOTES
    private int geneExpressionRecordId; // GENE_EXPRESSION_EXP_RECORD_ID
    private Double expressionValue;
    private String expressionUnit;
    private int mapKey;
    private String expressionLevel;
    private Double tpmValue;

    public String getExpressedGeneSymbol() {
        return expressedGeneSymbol;
    }

    public void setExpressedGeneSymbol(String expressedGeneSymbol) {
        this.expressedGeneSymbol = expressedGeneSymbol;
    }

    public boolean deleteFlag; // if true, the value should be deleted

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpressedObjectRgdId() {
        return expressedObjectRgdId;
    }

    public void setExpressedObjectRgdId(int expressedObjectRgdId) {
        this.expressedObjectRgdId = expressedObjectRgdId;
    }

    public String getExpressionMeasurementAccId() {
        return expressionMeasurementAccId;
    }

    public void setExpressionMeasurementAccId(String expressionMeasurementAccId) {
        this.expressionMeasurementAccId = expressionMeasurementAccId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getGeneExpressionRecordId() {
        return geneExpressionRecordId;
    }

    public void setGeneExpressionRecordId(int geneExpressionRecordId) {
        this.geneExpressionRecordId = geneExpressionRecordId;
    }

    public Double getExpressionValue() {
        return expressionValue;
    }

    public void setExpressionValue(Double expressionValue) {
        this.expressionValue = expressionValue;
    }

    public String getExpressionUnit() {
        return expressionUnit;
    }

    public void setExpressionUnit(String expressionUnit) {
        this.expressionUnit = expressionUnit;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getExpressionLevel() {
        return expressionLevel;
    }

    public void setExpressionLevel(String expressionLevel) {
        this.expressionLevel = expressionLevel;
    }

    public Double getTpmValue() {
        return tpmValue;
    }

    public void setTpmValue(Double tpmValue) {
        this.tpmValue = tpmValue;
    }
}
