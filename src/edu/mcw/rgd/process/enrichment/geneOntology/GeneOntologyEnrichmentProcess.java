package edu.mcw.rgd.process.enrichment.geneOntology;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import edu.mcw.rgd.dao.impl.GeneEnrichmentDAO;
public class GeneOntologyEnrichmentProcess{



    public double calculatePValue(int inputGenes,int refGenes,String term,int inputAnnotGenes,int speciesTypeKey) throws Exception{

        GeneEnrichmentDAO dao = new GeneEnrichmentDAO();
        int refAnnotGenes = dao.getRefAnnotGeneCount(term,speciesTypeKey);
        HypergeometricDistribution hg =
                new HypergeometricDistribution(refGenes,refAnnotGenes,inputGenes);
        double pvalue = hg.probability(inputAnnotGenes);
        return pvalue;
    }
}