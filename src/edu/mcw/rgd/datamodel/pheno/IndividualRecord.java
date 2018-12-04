package edu.mcw.rgd.datamodel.pheno;


/**
 * @author WLiu
 * Date: 10/13/11
 */
public class IndividualRecord {

    private int id;
    private int recordId;
    private String animalId;

    private String measurementValue;

    private Record record = new Record();

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record value) {
        this.record = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String value) {
        animalId = value;
    }

    public String getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(String measurementValue) {
        this.measurementValue = measurementValue;
    }
}
