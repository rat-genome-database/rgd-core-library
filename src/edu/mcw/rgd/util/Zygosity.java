package edu.mcw.rgd.util;

import edu.mcw.rgd.datamodel.Variant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: GKowalski
 * Date: 8/12/11
 * Time: 2:07 PM
 * Class to calculate all Zygosity's of a Variant.
 */
public class Zygosity {

    public static String HOMOZYGOUS = "homozygous";
    public static String HETEROZYGOUS = "heterozygous";
    public static String POSSIBLY_HOMOZYGOUS = "possibly homozygous";
    public static String HEMIZYGOUS = "hemizygous";
    public static String PROBABLY_HEMIZYGOUS = "probably hemizygous";
    public static String POSSIBLY_HEMIZYGOUS = "possibly hemizygous";
    public static String TRUE = "Y";
    public static String FALSE = "N";
    public static int POSSIBLE_ERROR_PERCENT = 15;
    public static int POSSIBLY_HOMOZYGOUS_PERCENT = 85;
    public static int PROBABLY_HEMIZYGOUSE_PERCENT = 85;
    public static int HOMOZYGOUS_PERCENT = 100;


    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Returns one or more variants based on the reads of the variant passed in ,
     * setting the zygosity values on these variants.
     * <p>
     * Note: Used by legacy code of rat strain loading. New loader uses much simpler code.
     * </p>
     * @param scoreA
     * @param scoreC
     * @param scoreG
     * @param scoreT
     * @param gender
     * @return
     */
    @Deprecated
    public List<Variant> computeVariants(int scoreA, int scoreC, int scoreG, int scoreT, String gender, Variant originalVariant) {

        float totalDepth = scoreA + scoreC + scoreG + scoreT;
        List<Variant> returnList = new ArrayList<Variant>();
        Variant variantA, variantC, variantT, variantG;
        String origVarNuc = originalVariant.getVariantNucleotide();
        // percentage scores for every nucleotide: 0..100
        float scoreAPerc = (scoreA * 100) / totalDepth;
        float scoreCPerc = (scoreC * 100) / totalDepth;
        float scoreGPerc = (scoreG * 100) / totalDepth;
        float scoreTPerc = (scoreT * 100) / totalDepth;
        logger.info("Percents : " + scoreAPerc + ":" + scoreCPerc + ":" + scoreGPerc + ":" + scoreTPerc);
        // pick the biggest score and nucleotide with biggest score
        String refNucl = originalVariant.getReferenceNucleotide();
        String chr = originalVariant.getChromosome();
        //logger.info("refnuc1 " + refNucl);

        // first compare first two nucleotides
        if (scoreAPerc > 0 && (!refNucl.equals("A"))) {
            variantA = Variant.newInstance(originalVariant);     // get a copy of the original and
            if (!origVarNuc.equals("A")) {
                variantA.setId(0);   // net effect of doing this is to create a new variant vs update the existing one  when we save
            }
            variantA.setVariantNucleotide("A");
            computeZygosity(scoreAPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, variantA);
            returnList.add(variantA);
        }
        if (scoreCPerc > 0 && (!refNucl.equals("C"))) {
            variantC = Variant.newInstance(originalVariant);
            if (!origVarNuc.equals("C")) {
                variantC.setId(0);   // net effect of doing this is to create a new variant vs update the existing one  when we save
            }
            variantC.setVariantNucleotide("C");
            computeZygosity(scoreCPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, variantC);
            returnList.add(variantC);
        }
        if (scoreGPerc > 0 && (!refNucl.equals("G"))) {
            variantG = Variant.newInstance(originalVariant);
            if (!origVarNuc.equals("G")) {
                variantG.setId(0);   // net effect of doing this is to create a new variant vs update the existing one  when we save
            }
            variantG.setVariantNucleotide("G");
            computeZygosity(scoreGPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, variantG);
            returnList.add(variantG);
        }
        if (scoreTPerc > 0 && (!refNucl.equals("T"))) {
            variantT = Variant.newInstance(originalVariant);
            if (!origVarNuc.equals("T")) {
                variantT.setId(0);   // net effect of doing this is to create a new variant vs update the existing one  when we save
            }
            variantT.setVariantNucleotide("T");
            computeZygosity(scoreTPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, variantT);
            returnList.add(variantT);
        }

        return returnList.size() > 0 ? returnList : null;
    }

    /**
     * Set the zygosity values for the given variant and given variant nucleotide.
     *
     * @param scoreA read count for A nucleotide
     * @param scoreC read count for C nucleotide
     * @param scoreG read count for G nucleotide
     * @param scoreT read count for T nucleotide
     * @param gender
     * @return hit count for the variant; if 0, it is really problematic if this variant is a variant at all
     */
    public int computeVariant(int scoreA, int scoreC, int scoreG, int scoreT, String gender, Variant v) {

        float totalDepth = scoreA + scoreC + scoreG + scoreT;
        String varNuc = v.getVariantNucleotide();
        // percentage scores for every nucleotide: 0..100
        float scoreAPerc = (scoreA * 100) / totalDepth;
        float scoreCPerc = (scoreC * 100) / totalDepth;
        float scoreGPerc = (scoreG * 100) / totalDepth;
        float scoreTPerc = (scoreT * 100) / totalDepth;
        logger.info("Percents : " + scoreAPerc + ":" + scoreCPerc + ":" + scoreGPerc + ":" + scoreTPerc);
        // pick the biggest score and nucleotide with biggest score
        String chr = v.getChromosome();

        // first compare first two nucleotides
        if( varNuc.equals("A") ) {
            computeZygosity(scoreAPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, v);
            return scoreA;
        }
        if( varNuc.equals("C") ) {
            computeZygosity(scoreCPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, v);
            return scoreC;
        }
        if( varNuc.equals("G") ) {
            computeZygosity(scoreGPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, v);
            return scoreG;
        }
        if( varNuc.equals("T") ) {
            computeZygosity(scoreTPerc, scoreAPerc, scoreCPerc, scoreGPerc, scoreTPerc, chr, gender, v);
            return scoreT;
        }

        return 0;
    }

    /**
     * Set the zygosity status, zygosity-in-pseudo and zygosity possible error for the given variant score
     *
     * @param score allele score
     * @param depth total read depth
     * @param gender sex
     */
    public void computeZygosityStatus(int score, int depth, String gender, Variant v) {

        // percentage score: 0..100
        float scorePerc = depth==0 ? 0.0f : (score * 100.0f) / depth;
        v.setZygosityPercentRead((int)(scorePerc+0.5f));

        // first compare first two nucleotides
        computeZygosityStatusPseudoError(scorePerc, v.getChromosome(), gender, v);
    }

    /**
     * Computes the zygosity settings and sets them on the variant passed into this method.
     *
     * @param myScore
     * @param scoreAPerc
     * @param scoreCPerc
     * @param scoreGPerc
     * @param scoreTPerc
     * @param chr
     * @param gender
     * @param variant
     */
    private void computeZygosity(float myScore, float scoreAPerc, float scoreCPerc, float scoreGPerc,
                                 float scoreTPerc, String chr, String gender, Variant variant) {

        String refNuc = variant.getReferenceNucleotide();
        String varNuc = variant.getVariantNucleotide();

        computeZygosityStatusPseudoError(myScore, chr, gender, variant);

        // Check for Zygosity Number of alleles we've seen
        int scoreCount = 0;
        if (scoreAPerc > 0) scoreCount++;
        if (scoreCPerc > 0) scoreCount++;
        if (scoreGPerc > 0) scoreCount++;
        if (scoreTPerc > 0) scoreCount++;
        variant.setZygosityNumberAllele(scoreCount);

        // Do we have a reference Nucelotide ?
        variant.setZygosityRefAllele(FALSE);
        if (refNuc.equals("A") && scoreAPerc > 0) variant.setZygosityRefAllele(TRUE);
        if (refNuc.equals("C") && scoreCPerc > 0) variant.setZygosityRefAllele(TRUE);
        if (refNuc.equals("G") && scoreGPerc > 0) variant.setZygosityRefAllele(TRUE);
        if (refNuc.equals("T") && scoreTPerc > 0) variant.setZygosityRefAllele(TRUE);

        if (varNuc.equals("A")) variant.setZygosityPercentRead((int)(scoreAPerc+0.5f));
        if (varNuc.equals("C")) variant.setZygosityPercentRead((int)(scoreCPerc+0.5f));
        if (varNuc.equals("G")) variant.setZygosityPercentRead((int)(scoreGPerc+0.5f));
        if (varNuc.equals("T")) variant.setZygosityPercentRead((int)(scoreTPerc+0.5f));
    }

    /**
     * Computes the zygosity status, zygosity in pseudo, and zygosity possible error
     *
     * @param myScore
     * @param chr
     * @param gender
     * @param variant
     */
    private void computeZygosityStatusPseudoError(float myScore, String chr, String gender, Variant variant) {

        if ((gender.equals("M") || gender.equals("P")) // patient must be male  or a Pooled sample
                && ((chr.equals("X") || chr.equals("Y"))) // and chromosome is X or Y
                ) {

            PseudoAutosomalRegion pseudo = new PseudoAutosomalRegion();

            // check to see if we are in the Pseudoautozomal region
            if (pseudo.inPAR(variant.getChromosome(), variant.getStartPos())) {

                if (myScore == HOMOZYGOUS_PERCENT) {
                    variant.setZygosityStatus(HOMOZYGOUS);
                } else if (myScore >= POSSIBLY_HOMOZYGOUS_PERCENT) {
                    variant.setZygosityStatus(POSSIBLY_HOMOZYGOUS);
                } else {
                    variant.setZygosityStatus(HETEROZYGOUS);
                }
                variant.setZygosityInPseudo(TRUE);

            } else {

                if (myScore == HOMOZYGOUS_PERCENT) {
                    variant.setZygosityStatus(HEMIZYGOUS);
                } else if (myScore >= PROBABLY_HEMIZYGOUSE_PERCENT) {
                    variant.setZygosityStatus(PROBABLY_HEMIZYGOUS);
                } else {
                    variant.setZygosityStatus(POSSIBLY_HEMIZYGOUS);
                }
                variant.setZygosityInPseudo(FALSE);
            }

        } else {

            if (myScore == HOMOZYGOUS_PERCENT) {
                variant.setZygosityStatus(HOMOZYGOUS);
            } else if (myScore >= POSSIBLY_HOMOZYGOUS_PERCENT) {
                variant.setZygosityStatus(POSSIBLY_HOMOZYGOUS);
            } else {
                variant.setZygosityStatus(HETEROZYGOUS);
            }
            variant.setZygosityInPseudo(FALSE);
        }

        // Check for error condition
        if (myScore <= POSSIBLE_ERROR_PERCENT) {
            variant.setZygosityPossibleError(TRUE);
        } else {
            variant.setZygosityPossibleError(FALSE);
        }
    }
}
