package edu.mcw.rgd.datamodel.variants;

import edu.mcw.rgd.process.Utils;

public class VariantMapData  {
    long id;
    String referenceNucleotide = "";
    String variantNucleotide = "";
    String rsId;
    String clinvarId;
    String variantType;
    int speciesTypeKey;
    String chromosome = "";
    String paddingBase;
    long startPos;
    long endPos;
    String genicStatus;
    int mapKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getPaddingBase() {
        return paddingBase;
    }

    public void setPaddingBase(String paddingBase) {
        this.paddingBase = paddingBase;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public String getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(String genicStatus) {
        this.genicStatus = genicStatus;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getReferenceNucleotide() {
        return referenceNucleotide;
    }

    public void setReferenceNucleotide(String referenceNucleotide) {
        this.referenceNucleotide = referenceNucleotide;
    }

    public String getVariantNucleotide() {
        return variantNucleotide;
    }

    public void setVariantNucleotide(String variantNucleotide) {
        this.variantNucleotide = variantNucleotide;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public String getClinvarId() {
        return clinvarId;
    }

    public void setClinvarId(String clinvarId) {
        this.clinvarId = clinvarId;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    @Override
    public boolean equals(Object obj) {
        VariantMapData v = (VariantMapData) obj;
        return Utils.stringsAreEqual(referenceNucleotide, v.getReferenceNucleotide()) && Utils.stringsAreEqual(variantNucleotide,v.getVariantNucleotide())
                && Utils.stringsAreEqual(chromosome,v.getChromosome()) && Utils.stringsAreEqual(rsId, v.getRsId()) && Utils.stringsAreEqual(clinvarId, v.getClinvarId())
                && Utils.stringsAreEqual(variantType, v.getVariantType()) && speciesTypeKey == v.getSpeciesTypeKey() && Utils.stringsAreEqual(paddingBase, v.getPaddingBase())
                && startPos == v.getStartPos() && endPos == v.getEndPos() && Utils.stringsAreEqual(genicStatus,v.getGenicStatus()) && mapKey == v.getMapKey();
    }
    @Override
    public int hashCode() {
        return Utils.defaultString(referenceNucleotide).hashCode() ^ Utils.defaultString(variantNucleotide).hashCode() ^ Utils.defaultString(rsId).hashCode()
                ^ Utils.defaultString(clinvarId).hashCode() ^ Utils.defaultString(variantType).hashCode() ^ speciesTypeKey ^ Utils.defaultString(chromosome).hashCode()
                ^ Utils.defaultString(paddingBase).hashCode() ^ Long.hashCode(startPos) ^ Long.hashCode(endPos) ^ Utils.defaultString(genicStatus).hashCode() ^ mapKey;
    }
}
