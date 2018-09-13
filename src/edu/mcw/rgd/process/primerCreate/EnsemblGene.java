package edu.mcw.rgd.process.primerCreate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 9/11/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnsemblGene {

    String geneId;
    String ensGeneName;
    String ensGeneId;
    String ensGeneSymbol;
    List<EnsemblTranscript> ensemblTranscripts;
    int eGStart;
    int egStop;
    String egChr;
    String eGStrand;
    String seqRegionId;


    public String getSeqRegionId() {
        return seqRegionId;
    }

    public void setSeqRegionId(String seqRegionId) {
        this.seqRegionId = seqRegionId;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public List<EnsemblTranscript> getEnsemblTranscripts() {
        return ensemblTranscripts;
    }

    public void setEnsemblTranscripts(List<EnsemblTranscript> ensemblTranscripts) {
        this.ensemblTranscripts = ensemblTranscripts;
    }

    public String getEnsGeneName() {
        return ensGeneName;
    }

    public void setEnsGeneName(String ensGeneName) {
        this.ensGeneName = ensGeneName;
    }

    public String getEnsGeneId() {
        return ensGeneId;
    }

    public void setEnsGeneId(String ensGeneId) {
        this.ensGeneId = ensGeneId;
    }

    public String getEnsGeneSymbol() {
        return ensGeneSymbol;
    }

    public void setEnsGeneSymbol(String ensGeneSymbol) {
        this.ensGeneSymbol = ensGeneSymbol;
    }

    public int geteGStart() {
        return eGStart;
    }

    public void seteGStart(int eGStart) {
        this.eGStart = eGStart;
    }

    public int getEgStop() {
        return egStop;
    }

    public void setEgStop(int egStop) {
        this.egStop = egStop;
    }

    public String geteGStrand() {
        return eGStrand;
    }

    public void seteGStrand(String eGStrand) {
        this.eGStrand = eGStrand;
    }

    public String getEgChr() {
        return egChr;
    }

    public void setEgChr(String egChr) {
        this.egChr = egChr;
    }



}
