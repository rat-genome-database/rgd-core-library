package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

public class Eva {
    private int evaId;
    private String chromosome;
    private int pos;
    private String rsId;
    private String refNuc;
    private String varNuc;
    private String padBase;
    private String soTerm;
    private int mapKey;

    public Eva(){}

    public int getEvaId(){ return evaId; }
    public String getChromosome(){ return chromosome; }
    public int getPos(){ return pos; }
    public String getRsId(){ return rsId; }
    public String getRefNuc(){ return refNuc; }
    public String getVarNuc(){ return varNuc; }
    public String getPadBase(){ return padBase; }
    public String getSoTerm(){ return soTerm; }
    public int getMapkey(){ return mapKey; }

    public void setEvaid(int evaId) {this.evaId = evaId;}
    public void setChromosome(String chromosome) {this.chromosome = chromosome;}
    public void setPos(int pos) {this.pos = pos;}
    public void setRsid(String rsid) {this.rsId = rsid;}
    public void setRefnuc(String refNuc) {this.refNuc = refNuc;}
    public void setVarnuc(String varNuc) {this.varNuc = varNuc;}
    public void setPadBase(String padBase) {this.padBase = padBase;}
    public void setSoterm(String soTerm) {this.soTerm = soTerm;}
    public void setMapkey(int mapKey) {this.mapKey = mapKey;}

    public String dump(String delimeter)
    {
        return (new Dumper(delimeter).put("CHROMOSOME", this.chromosome).put("POS", this.pos).put("RS_ID",this.rsId).put("REF_NUC", this.refNuc).put("VAR_NUC", this.varNuc).put("PADDING_BASE", this.padBase).put("SO_TERM_ACC", this.soTerm).put("MAP_KEY",this.mapKey).dump());
    }

    @Override
    public boolean equals(Object obj) {
        Eva e = (Eva)obj;
        return Utils.stringsAreEqual(chromosome, e.getChromosome()) && pos==e.getPos()
                && Utils.stringsAreEqual(rsId,e.getRsId()) && Utils.stringsAreEqual(refNuc,e.getRefNuc())
                && Utils.stringsAreEqual(varNuc, e.getVarNuc()) && Utils.stringsAreEqual(padBase, e.padBase)
                && Utils.stringsAreEqualIgnoreCase(soTerm,e.soTerm);
    }

    @Override
    public int hashCode() {
        return getPos() ^ Utils.defaultString(chromosome).hashCode() ^ Utils.defaultString(rsId).hashCode()
                ^ Utils.defaultString(refNuc).hashCode() ^ Utils.defaultString(varNuc).hashCode() ^
                Utils.defaultString(padBase).hashCode() ^ Utils.defaultString(soTerm).hashCode();
    }
}
