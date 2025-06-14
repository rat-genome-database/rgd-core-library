package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecord;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecordValue;
import edu.mcw.rgd.datamodel.pheno.Sample;

public class GeneExpression {
    private GeneExpressionRecord geneExpressionRecord;
    private GeneExpressionRecordValue geneExpressionRecordValue;
    private Sample sample;
    private Integer refRgdId;
    private Integer studyId;
    private String geoSeriesAcc;

    public String getGeoSeriesAcc() {
        return geoSeriesAcc;
    }

    public void setGeoSeriesAcc(String geoSeriesAcc) {
        this.geoSeriesAcc = geoSeriesAcc;
    }

    public GeneExpressionRecord getGeneExpressionRecord() {
        return geneExpressionRecord;
    }

    public void setGeneExpressionRecord(GeneExpressionRecord geneExpressionRecord) {
        this.geneExpressionRecord = geneExpressionRecord;
    }

    public GeneExpressionRecordValue getGeneExpressionRecordValue() {
        return geneExpressionRecordValue;
    }

    public void setGeneExpressionRecordValue(GeneExpressionRecordValue geneExpressionRecordValue) {
        this.geneExpressionRecordValue = geneExpressionRecordValue;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Integer getRefRgdId() {
        return refRgdId;
    }

    public void setRefRgdId(Integer refRgdId) {
        this.refRgdId = refRgdId;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }
}
