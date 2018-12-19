package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdVariantQuery;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.datamodel.RgdVariant;

import java.util.*;

import edu.mcw.rgd.datamodel.RgdId;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 17, 2018
 * Time: 10:14:52 AM
 * <p>
 * API to manipulate data in Variants table
 */
public class GeneEnrichmentDAO extends AbstractDAO {

    /**
     * Returns a count of all active genes in rgd
     * @param speciesKey species type key
     * @return count of active genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getReferenceGeneCount(int speciesKey) throws Exception {

        String query = "SELECT COUNT(*) FROM genes g, rgd_ids r " +
                "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND NVL(gene_type_lc,'*') NOT IN('splice','allele') " +
                " AND r.rgd_id=g.rgd_id ";

        return getCount(query, speciesKey);
    }

    /**
     * Returns a count of all annotated genes for a term
     * @param term ontology team
     * @return count of annotated genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRefAnnotGeneCount(String term) throws Exception {

        String query = "SELECT GENE_COUNT FROM GENE_ANNOT_COUNT " +
                "WHERE TERM_ACC = ?";

        return getCount(query, term);
    }

    public List<StringMapQuery.MapPair> getAnnotatedGOTermsForGenes(List<String> genes,String aspect,int speciesTypeKey) throws Exception{

        String geneFilter = null;
        for(String gene:genes){
            if(geneFilter == null)
                geneFilter = " where gene_symbol_lc in ('" + gene.toLowerCase() + "' ";
            else
                geneFilter += ",'" + gene.toLowerCase() + "' ";
        }
        String query ="select distinct(fai.term_acc),count(distinct fa.annotated_object_rgd_id)" +
                " from full_annot fa,full_annot_index fai\n" +
                "where fa.annotated_object_rgd_id in ( select r.RGD_ID from GENES g,RGD_IDS r \n" +
                geneFilter + ") and r.rgd_id=g.rgd_id and r.object_status = 'ACTIVE' " +
                "and r.species_type_key ="+speciesTypeKey +") \n" +
                " and aspect='"+aspect+"' and fa.full_annot_key = fai.full_annot_key\n" +
                "and RGD_OBJECT_KEY = 1 GROUP BY fai.TERM_ACC\n";

        return StringMapQuery.execute(this,query);
    }
}