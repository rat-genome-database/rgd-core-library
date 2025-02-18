package edu.mcw.rgd.datamodel.genomeInfo;

import java.util.Map;

public class GeneCounts {
    private int totalGenes;
    private int proteinCoding;
    private int ncrna;
    private int pseudo;
    private int tRna;
    private int snRna;
    private int rRna;
    private Map<String, Integer> mirnaTargets;
    private long transcripts;

    private int genesWithOrthologs;
    private int genesWithoutOrthologs;

    private  Map<String, Integer> orthologCountsMap;
    public GeneCounts(){}

    public int getTotalGenes() {
        return totalGenes;
    }

    public void setTotalGenes(int totalGenes) {
        this.totalGenes = totalGenes;
    }

    public int getProteinCoding() {
        return proteinCoding;
    }

    public void setProteinCoding(int proteinCoding) {
        this.proteinCoding = proteinCoding;
    }

    public int getNcrna() {
        return ncrna;
    }

    public void setNcrna(int ncrna) {
        this.ncrna = ncrna;
    }

    public int getPseudo() {
        return pseudo;
    }

    public void setPseudo(int pseudo) {
        this.pseudo = pseudo;
    }

    public int gettRna() {
        return tRna;
    }

    public void settRna(int tRna) {
        this.tRna = tRna;
    }

    public int getSnRna() {
        return snRna;
    }

    public void setSnRna(int snRna) {
        this.snRna = snRna;
    }

    public int getrRna() {
        return rRna;
    }

    public void setrRna(int rRna) {
        this.rRna = rRna;
    }

    public Map<String, Integer> getMirnaTargets() {
        return mirnaTargets;
    }

    public void setMirnaTargets(Map<String, Integer> mirnaTargets) {
        this.mirnaTargets = mirnaTargets;
    }

    public long getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(long transcripts) {
        this.transcripts = transcripts;
    }

    public int getGenesWithOrthologs() {
        return genesWithOrthologs;
    }

    public void setGenesWithOrthologs(int genesWithOrthologs) {
        this.genesWithOrthologs = genesWithOrthologs;
    }

    public int getGenesWithoutOrthologs() {
        return genesWithoutOrthologs;
    }

    public void setGenesWithoutOrthologs(int genesWithoutOrthologs) {
        this.genesWithoutOrthologs = genesWithoutOrthologs;
    }

    public Map<String, Integer> getOrthologCountsMap() {
        return orthologCountsMap;
    }

    public void setOrthologCountsMap(Map<String, Integer> orthologCountsMap) {
        this.orthologCountsMap = orthologCountsMap;
    }
}
