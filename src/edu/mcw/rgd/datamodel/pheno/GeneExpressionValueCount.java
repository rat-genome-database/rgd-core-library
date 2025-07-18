package edu.mcw.rgd.datamodel.pheno;

import java.util.Date;

public class GeneExpressionValueCount {
    private int valueCnt;
    private int expressedRgdId;
    private String termAcc;
    private String unit;
    private String level;
    private Date lastModifiedDate;

    public int getValueCnt() {
        return valueCnt;
    }

    public void setValueCnt(int valueCnt) {
        this.valueCnt = valueCnt;
    }

    public int getExpressedRgdId() {
        return expressedRgdId;
    }

    public void setExpressedRgdId(int expressedRgdId) {
        this.expressedRgdId = expressedRgdId;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
