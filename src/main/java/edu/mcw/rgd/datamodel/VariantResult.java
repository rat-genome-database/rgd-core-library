package edu.mcw.rgd.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jdepons
 * Date: Sep 22, 2009
 * Time: 8:44:50 AM
 */
public class VariantResult {

    private Variant variant;

    private GeneAssociation geneAssociation;

    private String heterozygosity = "";

    public List<TranscriptResult> transcriptResults = new ArrayList<TranscriptResult>();

    private String speciesConservation;

    private VariantInfo clinvarInfo;

    //  Start DB SNP Information ( Data comes from DB_SNP proper and the Thousand Genome Project
    public List<String> dbSNPIds = new ArrayList();
    // These fields filled in from DB_SNP.MAF_FREQUENCY and DB_SNP.MAF_SAMPLE_SIZE
    // Default of -1 for any of these fields means value is not found ( NULL ) in the database
    // These are MAF: Minor Allele Frequency
    private float  dbSnpMAFFrequence = -1;
    private int    dbSnpMAFSampleSize = -1;
    private String dbSnpAllele = "";
    private String dbSnpMAFAllele = "";
    private float  dbSnpStdError = -1;
    private float  dbSnpAvgHetroScore = -1;
    private String dbSnpQuestionable = null;

    // End of DB SNP Information

    public List<TranscriptResult> getTranscriptResults() {
        return transcriptResults;
    }

    public void setTranscriptResults(List<TranscriptResult> transcriptResults) {
        this.transcriptResults = transcriptResults;
    }

    public String getHeterozygosity() {
        return heterozygosity;
    }

    public void setHeterozygosity(String heterozygosity) {
        this.heterozygosity = heterozygosity;
    }

    public String getNovelDBSNP() {
        if (this.dbSNPIds.size() > 0) {
            return "No";
        } else {
            return "Yes";
        }

    }


    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public List<String> getDbSNPIds() {
        return dbSNPIds;
    }

    public void setDbSNPIds(List<String> dbSNPIds) {
        this.dbSNPIds = dbSNPIds;
    }

    public GeneAssociation getGeneAssociation() {
        return geneAssociation;
    }

    public void setGeneAssociation(GeneAssociation geneAssociation) {
        this.geneAssociation = geneAssociation;
    }

    public String getSpeciesConservation() {
        return speciesConservation;
    }

    public void setSpeciesConservation(String speciesConservation) {
        this.speciesConservation = speciesConservation;
    }

    public float getDbSnpMAFFrequence() {
        return dbSnpMAFFrequence;
    }

    public void setDbSnpMAFFrequence(float dbSnpMAFFrequence) {
        this.dbSnpMAFFrequence = dbSnpMAFFrequence;
    }

    public int getDbSnpMAFSampleSize() {
        return dbSnpMAFSampleSize;
    }

    public void setDbSnpMAFSampleSize(int dbSnpMAFSampleSize) {
        this.dbSnpMAFSampleSize = dbSnpMAFSampleSize;
    }

    public String getDbSnpAllele() {
        return dbSnpAllele;
    }

    public void setDbSnpAllele(String dbSnpAllele) {
        this.dbSnpAllele = dbSnpAllele;
    }

    public String getDbSnpMAFAllele() {
        return dbSnpMAFAllele;
    }

    public void setDbSnpMAFAllele(String dbSnpMAFAllele) {
        this.dbSnpMAFAllele = dbSnpMAFAllele;
    }

    public float getDbSnpStdError() {
        return dbSnpStdError;
    }

    public void setDbSnpStdError(float dbSnpStdError) {
        this.dbSnpStdError = dbSnpStdError;
    }

    public float getDbSnpAvgHetroScore() {
        return dbSnpAvgHetroScore;
    }

    public void setDbSnpAvgHetroScore(float dbSnpAvgHetroScore) {
        this.dbSnpAvgHetroScore = dbSnpAvgHetroScore;
    }

    public String getDbSnpQuestionable() {
        return dbSnpQuestionable;
    }

    public void setDbSnpQuestionable(String dbSnpQuestionable) {
        this.dbSnpQuestionable = dbSnpQuestionable;
    }

    public VariantInfo getClinvarInfo() {
        return clinvarInfo;
    }

    public void setClinvarInfo(VariantInfo clinvarInfo) {
        this.clinvarInfo = clinvarInfo;
    }
}
