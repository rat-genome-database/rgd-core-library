package edu.mcw.rgd.process.primerCreate;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 9/11/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnsemblVariation {
    String varName;
    String vid;
    String vSeq_region_id;
    String vChr;
    int vstart;
    int vstop;
    String vSource;
    String vStrand;
    String refNuc;
    String varAllele;
    String alleleString;
    String consequenceType;
    String inExonNumber;


    public String getInExonNumber() {
        return inExonNumber;
    }

    public void setInExonNumber(String inExonNumber) {
        this.inExonNumber = inExonNumber;
    }

    public String getConsequenceType() {
        return consequenceType;
    }

    public void setConsequenceType(String consequenceType) {
        this.consequenceType = consequenceType;
    }

    public String getAlleleString() {
        return alleleString;
    }

    public void setAlleleString(String alleleString) {
        this.alleleString = alleleString;
    }

    public String getRefNuc() {
        return refNuc;
    }

    public void setRefNuc(String refNuc) {
        this.refNuc = refNuc;
    }

    public String getVarAllele() {
        return varAllele;
    }

    public void setVarAllele(String varAllele) {
        this.varAllele = varAllele;
    }

    public String getvSeq_region_id() {
        return vSeq_region_id;
    }

    public void setvSeq_region_id(String vSeq_region_id) {
        this.vSeq_region_id = vSeq_region_id;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getvChr() {
        return vChr;
    }

    public void setvChr(String vChr) {
        this.vChr = vChr;
    }

    public int getVstart() {
        return vstart;
    }

    public void setVstart(int vstart) {
        this.vstart = vstart;
    }

    public int getVstop() {
        return vstop;
    }

    public void setVstop(int vstop) {
        this.vstop = vstop;
    }

    public String getvSource() {
        return vSource;
    }

    public void setvSource(String vSource) {
        this.vSource = vSource;
    }

    public String getvStrand() {
        return vStrand;
    }

    public void setvStrand(String vStrand) {
        this.vStrand = vStrand;
    }
}
