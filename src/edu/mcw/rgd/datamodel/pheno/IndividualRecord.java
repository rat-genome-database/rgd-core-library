package edu.mcw.rgd.datamodel.pheno;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 10/13/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndividualRecord {

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
