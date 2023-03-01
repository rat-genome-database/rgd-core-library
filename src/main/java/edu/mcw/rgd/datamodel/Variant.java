package edu.mcw.rgd.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds data from a row of VARIANT table, RATCNDEV|RATCNPROD schema
 * User: GKowalski
 * Date: Aug 26, 2009
 * Time: 3:36:13 PM
 */
public class Variant implements Serializable {

    // genicStatus values
    public static final String GENIC = "GENIC";
    public static final String INTERGENIC = "INTERGENIC";

    long id;
    String chromosome = "";
    long startPos;
    long endPos;
    String referenceNucleotide = "";
    String variantNucleotide = "";
    int depth;
    int qualityScore;
    String speciesConservation = "";
    String zygosityStatus;
    int zygosityPercentRead;  // Also Know as just Percent Read
    String zygosityPossibleError;
    String zygosityRefAllele;
    int zygosityNumberAllele;
    int variantFrequency;
    String zygosityInPseudo;
    String hgvsName;
    int rgdId;
    String variantType;
    String paddingBase;

    String spliceSitePrediction = "";
    String regionName = "";
    int sampleId;
    public List<ConservationScore> conservationScore = new ArrayList<ConservationScore>();
    String genicStatus = "";
    String rsId;

    public Variant() {
    }

    public Variant(long id, String chromosome, long startPos, long endPos, String referenceNucleotide,
                   String variantNucleotide, int depth, int qualityScore,  String speciesConservation,
                   String zygosityStatus, int zygosityPercentRead, String zygosityPossibleError,
                   String zygosityRefAllele, int zygosityNumberAllele, String zygosityInPseudo,
                   String spliceSitePrediction, String regionName, int sampleId,
                   List<ConservationScore> conservationScore, String genicStatus, int variantFrequency,
                   String hgvsName, int rgdId, String variantType, String paddingBase, String rsId ) {
        this.id = id;
        this.chromosome = chromosome;
        this.startPos = startPos;
        this.endPos = endPos;
        this.referenceNucleotide = referenceNucleotide;
        this.variantNucleotide = variantNucleotide;
        this.depth = depth;
        this.qualityScore = qualityScore;
        this.speciesConservation = speciesConservation;
        this.zygosityStatus = zygosityStatus;
        this.zygosityPercentRead = zygosityPercentRead;
        this.zygosityPossibleError = zygosityPossibleError;
        this.zygosityRefAllele = zygosityRefAllele;
        this.zygosityNumberAllele = zygosityNumberAllele;
        this.zygosityInPseudo = zygosityInPseudo;
        this.spliceSitePrediction = spliceSitePrediction;
        this.regionName = regionName;
        this.sampleId = sampleId;
        this.conservationScore = conservationScore;
        this.genicStatus = genicStatus;
        this.variantFrequency = variantFrequency;
        this.hgvsName = hgvsName;
        this.rgdId = rgdId;
        this.variantType = variantType;
        this.paddingBase = paddingBase;
        this.rsId=rsId;
    }

    /**
     * Alternative style for a copy constructor, using a static newInstance
     * method.
     */
    public static Variant newInstance(Variant aVar) {
        List a = aVar.getConservationScore();
        List<ConservationScore> b = null;
        if (a != null) {
            b = new ArrayList<ConservationScore>(a);
        }

        return new Variant(aVar.getId(), aVar.getChromosome(), aVar.getStartPos(), aVar.getEndPos(), aVar.getReferenceNucleotide(),
                aVar.getVariantNucleotide(), aVar.getDepth(), aVar.getQualityScore(), aVar.getSpeciesConservation(),
                aVar.getZygosityStatus(), aVar.getZygosityPercentRead(), aVar.getZygosityPossibleError(),
                aVar.getZygosityRefAllele(), aVar.getZygosityNumberAllele(), aVar.getZygosityInPseudo(),
                aVar.getSpliceSitePrediction(), aVar.getRegionName(), aVar.getSampleId(),
                b, aVar.getGenicStatus(), aVar.getVariantFrequency(), aVar.getHgvsName(), aVar.getRgdId(), aVar.getVariantType(), aVar.getPaddingBase(), aVar.rsId);
    }

    public List<ConservationScore> getConservationScore() {
        return conservationScore;
    }

    public void setConservationScore(List<ConservationScore> conservationScore) {
        this.conservationScore = conservationScore;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getSpeciesConservation() {
        return speciesConservation;
    }

    public void setSpeciesConservation(String speciesConservation) {
        this.speciesConservation = speciesConservation;
    }

    public String getSpliceSitePrediction() {
        return spliceSitePrediction;
    }

    public void setSpliceSitePrediction(String spliceSitePrediction) {
        this.spliceSitePrediction = spliceSitePrediction;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
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

    public void setConservationScores(List<ConservationScore> conservationScoreList) {
        this.conservationScore = conservationScoreList;
    }

    public String getZygosityStatus() {
        return zygosityStatus;
    }

    public void setZygosityStatus(String zygosityStatus) {
        this.zygosityStatus = zygosityStatus;
    }

    public int getZygosityPercentRead() {
        return zygosityPercentRead;
    }

    public void setZygosityPercentRead(int zygosityPercentRead) {
        this.zygosityPercentRead = zygosityPercentRead;
    }

    public String getZygosityPossibleError() {
        return zygosityPossibleError;
    }

    public void setZygosityPossibleError(String zygosityPossibleError) {
        this.zygosityPossibleError = zygosityPossibleError;
    }

    public String getZygosityRefAllele() {
        return zygosityRefAllele;
    }

    public void setZygosityRefAllele(String zygosityRefAllele) {
        this.zygosityRefAllele = zygosityRefAllele;
    }

    public int getZygosityNumberAllele() {
        return zygosityNumberAllele;
    }

    public void setZygosityNumberAllele(int zygosityNumberAllele) {
        this.zygosityNumberAllele = zygosityNumberAllele;
    }

    public String getZygosityInPseudo() {
        return zygosityInPseudo;
    }

    public void setZygosityInPseudo(String zygosityInPseudo) {
        this.zygosityInPseudo = zygosityInPseudo;
    }

    public String getGenicStatus() {
        return genicStatus;
    }

    public void setGenicStatus(String genicStatus) {
        this.genicStatus = genicStatus;
    }

    public int getVariantFrequency() {
        return variantFrequency;
    }

    public void setVariantFrequency(int variantFrequency) {
        this.variantFrequency = variantFrequency;
    }

    public String getHgvsName() {
        return hgvsName;
    }

    public void setHgvsName(String hgvsName) {
        this.hgvsName = hgvsName;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public String getPaddingBase() {
        return paddingBase;
    }

    public void setPaddingBase(String paddingBase) {
        this.paddingBase = paddingBase;
    }
    @Override
    public boolean equals(Object obj){
            Variant v= (Variant) obj;
            return (Objects.equals(chromosome, v.getChromosome())) &&
                    ( startPos==v.startPos ) &&
                    (endPos==v.getEndPos()) &&
                    (Objects.equals(referenceNucleotide, v.getReferenceNucleotide())) &&
                    (depth==v.getDepth()) &&
                    (qualityScore==v.getQualityScore()) &&
                   /* (zygosityPercentRead==v.getZygosityPercentRead())&&
                    (zygosityNumberAllele==v.getZygosityNumberAllele())&&
                    (Objects.equals(zygosityRefAllele, v.getZygosityRefAllele()))&&
                    (variantFrequency==v.getVariantFrequency()) &&*/
                    (Objects.equals(hgvsName, v.getHgvsName())) &&
                    (Objects.equals(variantType, v.getVariantType())) &&
                    (Objects.equals(paddingBase, v.getPaddingBase()));


    }

}
