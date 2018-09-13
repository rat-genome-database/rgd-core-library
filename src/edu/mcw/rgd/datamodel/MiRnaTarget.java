package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 5/4/15
 * Time: 11:53 AM
 * <p>
 * model class to represent a rows from MIRNA_TARGETS table
 * <p>
 * Note: as of July 2016, 'energy' field is no longer provided in data from mirgate,
 *       therefore we no longer use this field in comparisons;
 *       we keep the historical energy data where possible
 * </p>
 */
public class MiRnaTarget {

    private int key;
    private int geneRgdId;
    private int miRnaRgdId;
    private String targetType;

    private String miRnaSymbol;
    private String methodName;
    private String resultType;
    private String dataType;
    private String supportType;
    private String pmid;

    private String transcriptAcc;
    private String transcriptBioType;
    private String isoform;
    private String amplification;
    private Integer utrStart;
    private Integer utrEnd;
    private String targetSite;
    private Double score;
    private Double normalizedScore;
    private Double energy;

    private Date createdDate;
    private Date modifiedDate;

    private byte[] md5;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public int getMiRnaRgdId() {
        return miRnaRgdId;
    }

    public void setMiRnaRgdId(int miRnaRgdId) {
        this.miRnaRgdId = miRnaRgdId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getMiRnaSymbol() {
        return miRnaSymbol;
    }

    public void setMiRnaSymbol(String miRnaSymbol) {
        this.miRnaSymbol = miRnaSymbol;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getTranscriptAcc() {
        return transcriptAcc;
    }

    public void setTranscriptAcc(String transcriptAcc) {
        this.transcriptAcc = transcriptAcc;
    }

    public String getTranscriptBioType() {
        return transcriptBioType;
    }

    public void setTranscriptBioType(String transcriptBioType) {
        this.transcriptBioType = transcriptBioType;
    }

    public String getIsoform() {
        return isoform;
    }

    public void setIsoform(String isoform) {
        this.isoform = isoform;
    }

    public String getAmplification() {
        return amplification;
    }

    public void setAmplification(String amplification) {
        this.amplification = amplification;
    }

    public Integer getUtrStart() {
        return utrStart;
    }

    public void setUtrStart(Integer utrStart) {
        this.utrStart = utrStart;
    }

    public Integer getUtrEnd() {
        return utrEnd;
    }

    public void setUtrEnd(Integer utrEnd) {
        this.utrEnd = utrEnd;
    }

    public String getTargetSite() {
        return targetSite;
    }

    public void setTargetSite(String targetSite) {
        this.targetSite = targetSite;
    }

    /**
     * return string representation of 'score'
     * @return
     */
    public String printScore() {
        return score==null ? "" : String.format("%.6f", score);
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * return string representation of 'normalized score'
     * @return
     */
    public String printNormalizedScore() {
        return normalizedScore==null ? "" : String.format("%.3f", normalizedScore);
    }

    public Double getNormalizedScore() {
        return normalizedScore;
    }

    public void setNormalizedScore(Double normalizedScore) {
        this.normalizedScore = normalizedScore;
    }

    /**
     * return string representation of 'energy'
     * @return
     */
    public String printEnergy() {
        return energy==null ? "" : String.format("%.2f", energy);
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean equalsByContent(MiRnaTarget obj) {
        return Utils.stringsAreEqual(this.getMiRnaSymbol(), obj.getMiRnaSymbol()) &&
            Utils.stringsAreEqual(this.getMethodName(), obj.getMethodName()) &&
            Utils.stringsAreEqual(this.getResultType(), obj.getResultType()) &&
            Utils.stringsAreEqual(this.getDataType(), obj.getDataType()) &&
            Utils.stringsAreEqual(this.getSupportType(), obj.getSupportType()) &&
            Utils.stringsAreEqual(this.getPmid(), obj.getPmid()) &&

            Utils.stringsAreEqual(this.getTranscriptAcc(), obj.getTranscriptAcc()) &&
            Utils.stringsAreEqual(this.getTranscriptBioType(), obj.getTranscriptBioType()) &&
            Utils.stringsAreEqual(this.getIsoform(), obj.getIsoform()) &&
            Utils.stringsAreEqual(this.getAmplification(), obj.getAmplification()) &&
            Utils.stringsAreEqual(this.getTargetSite(), obj.getTargetSite()) &&
            Utils.intsAreEqual(this.getUtrEnd(), obj.getUtrEnd()) &&
            Utils.intsAreEqual(this.getUtrStart(), obj.getUtrStart()) &&
            Utils.doublesAreEqual(this.getScore(), obj.getScore(), 6) &&
            Utils.doublesAreEqual(this.getNormalizedScore(), obj.getNormalizedScore(), 6);

        // Note: as of July 2016, 'energy' field is no longer provided in data from mirgate,
        //   so we are no longer using this field when comparing two objects;
        //   this will result in dropping all of the rows and loading the same rows
        //   but with empty 'energy' field
    }

    /** same as equalsByContent() method, but highly optimized for many executions
     *
     * @param obj
     * @return
     */
    public boolean equalsByContent2(MiRnaTarget obj) {

        // to achieve better speedup, we compare md5 signatures of both objects
        //
        // ensure that both objects compared have an md5 signature, and if not, compute it
        this.computeMd5DigestIfNeeded();
        obj.computeMd5DigestIfNeeded();

        return Arrays.equals(this.md5, obj.md5);
    }

    void computeMd5DigestIfNeeded() {
        if( this.md5==null ) {
            try {
                this.md5 = MessageDigest.getInstance("MD5").digest(this.toString().getBytes());
            } catch(NoSuchAlgorithmException ignore) {
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(100);
        buf.append(this.getMiRnaSymbol()).append('|')
            .append(this.getMethodName()).append('|')
            .append(this.getResultType()).append('|')
            .append(this.getDataType()).append('|')
            .append(this.getSupportType()).append('|')
            .append(this.getPmid()).append('|')
            .append(this.getTranscriptAcc()).append('|')
            .append(this.getTranscriptBioType()).append('|')
            .append(this.getIsoform()).append('|')
            .append(this.getAmplification()).append('|')
            .append(this.getTargetSite()).append('|')
            .append(this.getUtrEnd()).append('|')
            .append(this.getUtrStart()).append('|')
            .append(this.printScore()).append('|')
            .append(this.printNormalizedScore());
        return buf.toString();
    }
}
