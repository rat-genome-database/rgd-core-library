package edu.mcw.rgd.datamodel;

import java.sql.Timestamp;

public class ReportLoadStatus {

    private int reportLoadStatusId;
    private int rgdId;
    private String reportType;
    private int speciesKey;
    private int mapKey;
    private String symbol;
    private String status;
    private String fileName;
    private String errorMessage;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public int getReportLoadStatusId() {
        return reportLoadStatusId;
    }

    public void setReportLoadStatusId(int reportLoadStatusId) {
        this.reportLoadStatusId = reportLoadStatusId;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getSpeciesKey() {
        return speciesKey;
    }

    public void setSpeciesKey(int speciesKey) {
        this.speciesKey = speciesKey;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
