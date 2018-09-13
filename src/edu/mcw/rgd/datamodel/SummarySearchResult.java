package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: GKowalski
 * Date: May 24, 2010
 * Time: 2:01:00 PM
 */
public class SummarySearchResult {

    String geneName;
    int variantCount;
    long geneStart;
    long geneStop;
    String geneType;
    String geneStatus;

    public SummarySearchResult(String geneName, int variantCount) {

        this.geneName = geneName;
        this.variantCount = variantCount;
    }

    public SummarySearchResult(String geneName, int variantCount, long geneStart, long geneStop, String geneType, String geneStatus ) {
        this.geneName = geneName;
        this.variantCount = variantCount;
        this.geneStart = geneStart;
        this.geneStop= geneStop;
        this.geneType=geneType;
        this.geneStatus=geneStatus;
    }

    public long getGeneStart() {
        return geneStart;
    }

    public void setGeneStart(long geneStart) {
        this.geneStart = geneStart;
    }

    public long getGeneStop() {
        return geneStop;
    }

    public void setGeneStop(long geneStop) {
        this.geneStop = geneStop;
    }

    public String getGeneType() {
        return geneType;
    }

    public void setGeneType(String geneType) {
        this.geneType = geneType;
    }

    public String getGeneStatus() {
        return geneStatus;
    }

    public void setGeneStatus(String geneStatus) {
        this.geneStatus = geneStatus;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public int getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(int variantCount) {
        this.variantCount = variantCount;
    }
}
