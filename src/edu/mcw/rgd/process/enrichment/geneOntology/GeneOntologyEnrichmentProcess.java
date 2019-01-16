package edu.mcw.rgd.process.enrichment.geneOntology;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.ontologyx.TermWithStats;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import edu.mcw.rgd.dao.impl.GeneEnrichmentDAO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GeneOntologyEnrichmentProcess{



    public BigDecimal calculatePValue(int inputGenes, int refGenes, String term, int inputAnnotGenes, int speciesTypeKey) throws Exception{


        OntologyXDAO odao = new OntologyXDAO();
        TermWithStats ts = odao.getTermWithStatsCached(term);
        int withChildren = 1;
        int refAnnotGenes = ts.getStat("annotated_object_count", speciesTypeKey, RgdId.OBJECT_KEY_GENES, withChildren);
        HypergeometricDistribution hg =
                new HypergeometricDistribution(refGenes,refAnnotGenes,inputGenes);
        BigDecimal pvalue = new BigDecimal(hg.probability(inputAnnotGenes));
        return pvalue;
    }

    public BigDecimal calculateBonferroni(BigDecimal pvalue,BigDecimal terms){
        BigDecimal bonferroni = pvalue.multiply(terms);
        if (bonferroni.compareTo(BigDecimal.ONE) == 1)
            bonferroni = BigDecimal.ONE;

        return bonferroni;
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