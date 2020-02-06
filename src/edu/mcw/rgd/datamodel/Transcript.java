package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Jun 18, 2010
 * Time: 4:25:02 PM
 * represents a gene transcript; one gene may have multiple transcripts;
 * one transcript may be mapped onto multiple assemblies
 */
public class Transcript implements Identifiable, Dumpable {

    int rgdId; // transcript rgd id
    int geneRgdId; // rgd id of gene this transcript is associated with
    String accId; // transcript access id (nucleotide id from NCBI nucleotide database)
    java.util.Date dateCreated; // date this transcript have been created
    boolean isNonCoding;
    String refSeqStatus; // REFSEQ_STATUS for the current reference assembly
    String proteinAccId; // protein acc id
    String peptideLabel; // peptide label - contains information about isoforms and precursor preproteins
    String type;
    // genomic position of the transcript on one or more genomic maps
    List<MapData> genomicPositions = new ArrayList<MapData>();

    /**
     * two transcripts are equal if they have same acc_id, is_noncoding, refseq_status,protein_acc_id and peptide_label
     * @param obj another Transcript object
     * @return true if transcripts are equal
     */
    @Override
    public boolean equals(Object obj) {
        Transcript tr = (Transcript) obj;
        return Utils.stringsAreEqual(this.getAccId(), tr.getAccId()) &&
               this.isNonCoding()==tr.isNonCoding() &&
                Utils.stringsAreEqual(this.getRefSeqStatus(), tr.getRefSeqStatus()) &&
                Utils.stringsAreEqual(this.getProteinAccId(), tr.getProteinAccId()) &&
                Utils.stringsAreEqual(this.getPeptideLabel(), tr.getPeptideLabel());
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isNonCoding() {
        return isNonCoding;
    }

    public void setNonCoding(boolean nonCoding) {
        isNonCoding = nonCoding;
    }

    public List<MapData> getGenomicPositions() {
        return genomicPositions;
    }

    public void setGenomicPositions(List<MapData> genomicPositions) {
        this.genomicPositions = genomicPositions;
    }

    public String getRefSeqStatus() {
        return refSeqStatus;
    }

    public void setRefSeqStatus(String refSeqStatus) {
        this.refSeqStatus = refSeqStatus;
    }

    public String getProteinAccId() {
        return proteinAccId;
    }

    public void setProteinAccId(String proteinAccId) {
        this.proteinAccId = proteinAccId;
    }

    public String getPeptideLabel() {
        return peptideLabel;
    }

    public void setPeptideLabel(String proteinLabel) {
        this.peptideLabel = proteinLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("TRANSCRIPT_RGD_ID", getRgdId())
            .put("GENE_RGD_ID", getGeneRgdId())
            .put("ACC_ID", getAccId())
            .put("IS_NON_CODING", isNonCoding()?"Y":"N")
            .put("REF_SEQ_STATUS", getRefSeqStatus())
            .put("PROTEIN_ACC_ID", getProteinAccId())
            .put("PEPTIDE_LABEL", getPeptideLabel())
            .put("DATE_CREATED", getDateCreated())
            .dump();
    }
}
