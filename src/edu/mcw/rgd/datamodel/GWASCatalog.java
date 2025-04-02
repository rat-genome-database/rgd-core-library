package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

import java.math.BigDecimal;
import java.math.MathContext;


public class GWASCatalog {
    private int gwasId;
    private String pmid;
    private String diseaseTrait;
    private String initialSample;
    private String replicateSample;
    private String region;
    private String chr;
    private String pos;
    private String reportedGenes;
    private String mappedGene;
    private String snpGeneId;
    private String strongSnpRiskallele;
    private String snps;
    private String curSnpId;
    private String context;
    private String riskAlleleFreq;
    private String pVal;
    private Double pValMlog;
    private String snpPassQc;
    private String mapTrait;
    private String studyAcc;
    private String efoId;
    private String orBeta;
    private int variantRgdId;
    private Integer qtlRgdId;
    private int mapKey;

    public GWASCatalog(){};

    public GWASCatalog(GWASCatalog gc, String chrom, String position, String riskAllele, String snp){
        pmid = gc.getPmid();
        diseaseTrait = gc.getDiseaseTrait();
        initialSample = gc.getInitialSample();
        replicateSample = gc.getReplicateSample();
        region = gc.getRegion();
        chr = chrom;
        pos = position;
        reportedGenes = gc.getReportedGenes();
        mappedGene = gc.getMappedGene();
        strongSnpRiskallele = riskAllele;
        snps = snp;
        curSnpId = gc.getCurSnpId();
        context = gc.getContext();
        riskAlleleFreq = gc.getRiskAlleleFreq();
        pVal = gc.getpValStr();
        pValMlog = gc.getpValMlog();
        snpPassQc = gc.getSnpPassQc();
        mapTrait = gc.getMapTrait();
        efoId = gc.getEfoId();
        studyAcc = gc.getStudyAcc();
        orBeta = gc.orBeta;
    }

    public String print(){return pmid+"|"+diseaseTrait+"|"+initialSample+"|"+replicateSample+"|"+region+"|"+chr+"|"+pos+"|"+reportedGenes+"|"+strongSnpRiskallele+"|"+snps+"|"+curSnpId+"|"+context+"|"+riskAlleleFreq+"|"+pVal+"|"+pValMlog+"|"+snpPassQc+"|"+mapTrait+"|"+efoId+"|"+studyAcc+"|"+variantRgdId;}

    public int getGwasId() {
        return gwasId;
    }

    public void setGwasId(int gwasId) {
        this.gwasId = gwasId;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getDiseaseTrait() {
        return diseaseTrait;
    }

    public void setDiseaseTrait(String diseaseTrait) {
        this.diseaseTrait = diseaseTrait;
    }

    public String getInitialSample() {
        return initialSample;
    }

    public void setInitialSample(String initialSample) {
        this.initialSample = initialSample;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getReportedGenes() {
        return reportedGenes;
    }

    public void setReportedGenes(String reportedGenes) {
        this.reportedGenes = reportedGenes;
    }

    public String getMappedGene() {
        return mappedGene;
    }

    public void setMappedGene(String mappedGene) {
        this.mappedGene = mappedGene;
    }

    public String getSnpGeneId() {
        return snpGeneId;
    }

    public void setSnpGeneId(String snpGeneId) {
        this.snpGeneId = snpGeneId;
    }

    public String getStrongSnpRiskallele() {
        return strongSnpRiskallele;
    }

    public void setStrongSnpRiskallele(String strongSnpRiskallele) {
        this.strongSnpRiskallele = strongSnpRiskallele;
    }

    public String getSnps() {
        return snps;
    }

    public void setSnps(String snps) {
        this.snps = snps;
    }

    public String getCurSnpId() {
        return curSnpId;
    }

    public void setCurSnpId(String curSnpId) {
        this.curSnpId = curSnpId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRiskAlleleFreq() {
        return riskAlleleFreq;
    }

    public void setRiskAlleleFreq(String riskAlleleFreq) {
        this.riskAlleleFreq = riskAlleleFreq;
    }

    public BigDecimal getpVal() {
        BigDecimal b = new BigDecimal(pVal, MathContext.DECIMAL128);
        return b;
    }

    public String getpValStr(){
        return pVal;
    }

    public void setpVal(String pVal){
        this.pVal = pVal;
    }

    public Double getpValMlog() {
        return pValMlog;
    }

    public void setpValMlog(Double pValMlog) {
        this.pValMlog = pValMlog;
    }

    public String getSnpPassQc() {
        return snpPassQc;
    }

    public void setSnpPassQc(String snpPassQc) {
        this.snpPassQc = snpPassQc;
    }

    public String getMapTrait() {
        return mapTrait;
    }

    public void setMapTrait(String mapTrait) {
        this.mapTrait = mapTrait;
    }

    public String getStudyAcc() {
        return studyAcc;
    }

    public void setStudyAcc(String studyAcc) {
        this.studyAcc = studyAcc;
    }

    public String getReplicateSample() {
        return replicateSample;
    }

    public void setReplicateSample(String replicateSample) {
        this.replicateSample = replicateSample;
    }

    public String getEfoId() {
        return efoId;
    }

    public void setEfoId(String efoId) {
        this.efoId = efoId;
    }

    public void setOrBeta(String orBeta){
        this.orBeta = orBeta;
    }

    public String getOrBeta(){
        return orBeta;
    }

    @Override
    public boolean equals(Object obj) {
        GWASCatalog gc = (GWASCatalog) obj;
        return Utils.stringsAreEqual(pmid,gc.getPmid()) && Utils.stringsAreEqual(diseaseTrait, gc.getDiseaseTrait()) && Utils.stringsAreEqual(initialSample,gc.getInitialSample()) &&
                Utils.stringsAreEqual(replicateSample,gc.getReplicateSample()) && Utils.stringsAreEqual(region, gc.getRegion()) && Utils.stringsAreEqual(chr, gc.getChr()) &&
                Utils.stringsAreEqual(pos,gc.getPos()) && Utils.stringsAreEqual(reportedGenes, gc.getReportedGenes()) && Utils.stringsAreEqual(mappedGene,gc.getMappedGene()) &&
                Utils.stringsAreEqual(strongSnpRiskallele, gc.getStrongSnpRiskallele()) && Utils.stringsAreEqual(snps,gc.getSnps()) && Utils.stringsAreEqual(curSnpId,gc.getCurSnpId()) &&
                Utils.stringsAreEqual(context,gc.getContext()) && Utils.stringsAreEqual(riskAlleleFreq,gc.getRiskAlleleFreq()) && Utils.stringsAreEqual(pVal,gc.getpValStr()) &&
                pValMlog.equals(gc.getpValMlog()) && Utils.stringsAreEqual(snpPassQc,gc.getSnpPassQc()) && Utils.stringsAreEqual(mapTrait,gc.getMapTrait()) &&
                Utils.stringsAreEqual(efoId, gc.getEfoId()) && Utils.stringsAreEqual(studyAcc,gc.getStudyAcc()) && Utils.stringsAreEqual(orBeta,gc.getOrBeta()) && mapKey==gc.getMapKey();
    }

    @Override
    public int hashCode() {
        return Utils.defaultString(pmid).hashCode() ^ Utils.defaultString(diseaseTrait).hashCode() ^ Utils.defaultString(initialSample).hashCode() ^ Utils.defaultString(replicateSample).hashCode()
                ^ Utils.defaultString(region).hashCode() ^ Utils.defaultString(chr).hashCode() ^ Utils.defaultString(pos).hashCode() ^ Utils.defaultString(reportedGenes).hashCode() ^
                Utils.defaultString(mappedGene).hashCode() ^ Utils.defaultString(strongSnpRiskallele).hashCode() ^ Utils.defaultString(snps).hashCode() ^ Utils.defaultString(curSnpId).hashCode()
                ^ Utils.defaultString(context).hashCode() ^ Utils.defaultString(riskAlleleFreq).hashCode() ^ Utils.defaultString(pVal).hashCode() ^ pValMlog.hashCode()
                ^ Utils.defaultString(snpPassQc).hashCode() ^ Utils.defaultString(mapTrait).hashCode() ^ Utils.defaultString(efoId).hashCode() ^ Utils.defaultString(studyAcc).hashCode()
                ^ Utils.defaultString(orBeta).hashCode() ^ mapKey;
    }

    public int getVariantRgdId() {
        return variantRgdId;
    }

    public void setVariantRgdId(int variantRgdId) {
        this.variantRgdId = variantRgdId;
    }

    public Integer getQtlRgdId() {
        return qtlRgdId;
    }

    public void setQtlRgdId(Integer qtlRgdId) {
        this.qtlRgdId = qtlRgdId;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }
}
