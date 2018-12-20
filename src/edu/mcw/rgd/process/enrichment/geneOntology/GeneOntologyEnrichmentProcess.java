package edu.mcw.rgd.process.enrichment.geneOntology;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import edu.mcw.rgd.dao.impl.GeneEnrichmentDAO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GeneOntologyEnrichmentProcess{



    public double calculatePValue(int inputGenes,int refGenes,String term,int inputAnnotGenes,int speciesTypeKey) throws Exception{

        GeneEnrichmentDAO dao = new GeneEnrichmentDAO();
        int refAnnotGenes = dao.getRefAnnotGeneCount(term,speciesTypeKey);
        HypergeometricDistribution hg =
                new HypergeometricDistribution(refGenes,refAnnotGenes,inputGenes);
        double pvalue = hg.probability(inputAnnotGenes);
        return pvalue;
    }

    public float calculateBonferroni(float pvalue,int terms){
        float bonferroni = pvalue * terms;
        if (bonferroni > 1)
            bonferroni = 1;

        return bonferroni;
    }

    public float calculateHolmBonferroni(float pvalue,int terms,int rank){
        float HolmBonferroni = pvalue * (terms - rank);
        if (HolmBonferroni > 1)
            HolmBonferroni = 1;

        return HolmBonferroni;
    }

    public float calculateBenjamini(float pvalue,int terms,int rank){
        float HolmBenjamini = pvalue * (terms/rank);
        if (HolmBenjamini > 1)
            HolmBenjamini = 1;

        return HolmBenjamini;
    }
    public Map<String,Float> sortPvalues(HashMap<String,Float> pvalues){
        Comparator<String> comparator = new CustomComparator(pvalues);
        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.
        TreeMap<String, Float> result = new TreeMap<String, Float>(comparator);
        result.putAll(pvalues);
        return result;
    }
}
class CustomComparator implements Comparator<String>{

    HashMap<String, Float> map = new HashMap<String, Float>();

    public CustomComparator(HashMap<String, Float> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return 1;
        }else{
            return -1;
        }
    }
}