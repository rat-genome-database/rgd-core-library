package edu.mcw.rgd.process.enrichment.geneOntology;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.ontologyx.TermWithStats;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import edu.mcw.rgd.dao.impl.GeneEnrichmentDAO;
import org.apache.commons.math3.util.FastMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GeneOntologyEnrichmentProcess{

    protected final Log logger = LogFactory.getLog(getClass());

    public String calculatePValue(int inputGenes, int refGenes, int inputAnnotGenes, int refAnnotGenes) throws Exception{

        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(2);
        MathContext mc = new MathContext(3);

        try {
            HypergeometricDistribution hg =
                    new HypergeometricDistribution(refGenes, refAnnotGenes, inputGenes);

            BigDecimal pvalue = new BigDecimal(hg.probability(inputAnnotGenes), mc);
            String p = formatter.format(pvalue);

            return p;
        }catch(Exception e) {
            logger.info("Ref: " +refGenes + " Ref Annotated Genes: "+refAnnotGenes+ " Input: "+inputGenes+ " Input Annotated Genes: "+inputAnnotGenes +"\n");
            logger.info(e.getMessage());
            return null;
        }
    }

    public String calculateBonferroni(String pvalue,BigDecimal terms){
        BigDecimal val = new BigDecimal(pvalue);
        BigDecimal bonferroni = val.multiply(terms);
        if (bonferroni.compareTo(BigDecimal.ONE) == 1)
            bonferroni = BigDecimal.ONE;

        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(2);
        String p = formatter.format(bonferroni);

        return p;
    }

    public BigDecimal calculateHolmBonferroni(BigDecimal pvalue,BigDecimal terms,BigDecimal rank){
        BigDecimal HolmBonferroni = pvalue.multiply (terms.subtract(rank));
        if (HolmBonferroni.compareTo(BigDecimal.ONE) == 1)
            HolmBonferroni = BigDecimal.ONE;

        return HolmBonferroni;
    }

    public BigDecimal calculateBenjamini(BigDecimal pvalue,BigDecimal terms,BigDecimal rank){
        BigDecimal HolmBenjamini = pvalue.multiply(new BigDecimal(terms.intValue()/rank.intValue()));
        if (HolmBenjamini.compareTo(BigDecimal.ONE) == 1)
            HolmBenjamini = BigDecimal.ONE;

        return HolmBenjamini;
    }
    public Map<String,BigDecimal> sortPvalues(HashMap<String,BigDecimal> pvalues){
        Comparator<String> comparator = new CustomComparator(pvalues);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, BigDecimal> result = new TreeMap<String, BigDecimal>(comparator);
        result.putAll(pvalues);
        return result;
    }
}
class CustomComparator implements Comparator<String>{

    HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

    public CustomComparator(HashMap<String, BigDecimal> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if( map.get(s1).compareTo(map.get(s2)) == 0)
            return s1.compareTo(s2);
        else
            return map.get(s1).compareTo(map.get(s2));
    }
}