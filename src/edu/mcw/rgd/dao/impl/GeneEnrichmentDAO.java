package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdVariantQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
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
    public int getRefAnnotGeneCount(String term,int speciesTypeKey) throws Exception {

        String query = "SELECT GENE_COUNT FROM GENE_ANNOT_COUNT " +
                "WHERE TERM_ACC = ? and SPECIES_TYPE_KEY = ?";

        return getCount(query, term, speciesTypeKey);
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

    public int getAnnotatedGeneCount(String term,int speciesTypeKey) throws Exception{
        String query = "select count(distinct(fa.annotated_object_rgd_id)) as gene_count\n" +
                "from full_annot fa\n" +
                "inner join full_annot_index fai on fa.full_annot_key = fai.full_annot_key \n" +
                "inner join rgd_ids r on (r.rgd_id = fa.annotated_object_rgd_id and r.species_type_key = ?)\n" +
                "where aspect in ('D','W','N','C','F','P','E')  and RGD_OBJECT_KEY = 1 " +
                " and fai.term_acc = ? ";

        return getCount(query,speciesTypeKey,term);

    }

    public List<String> getAnnotatedNewTerms(int speciesTypeKey) throws Exception{
        String query = "select fai.term_acc from full_annot fa\n" +
                "inner join full_annot_index fai on fa.full_annot_key = fai.full_annot_key \n" +
                "inner join rgd_ids r on (r.rgd_id = fa.annotated_object_rgd_id and r.species_type_key = ?)\n" +
                "where aspect in ('D','W','N','C','F','P','E') and RGD_OBJECT_KEY = 1  \n" +
                "MINUS\n" +
                "select term_acc from gene_annot_count where species_type_key = ?";
        return StringListQuery.execute(this,query,speciesTypeKey,speciesTypeKey);
    }

    public void insertAnnotGeneCount(int speciesTypeKey) throws Exception{
        String query= "INSERT INTO GENE_ANNOT_COUNT(TERM_ACC,GENE_COUNT,SPECIES_TYPE_KEY)\n" +
                "(select fai.term_acc, count(distinct(fa.annotated_object_rgd_id)) as gene_count,r.species_type_key\n" +
                "from full_annot fa\n" +
                "inner join full_annot_index fai on fa.full_annot_key = fai.full_annot_key \n" +
                "inner join rgd_ids r on (r.rgd_id = fa.annotated_object_rgd_id and r.species_type_key = ?)\n" +
                "where aspect in ('D','W','N','C','F','P','E')  and RGD_OBJECT_KEY = 1  and fai.term_acc not in ( select term_acc from gene_annot_count where species_type_key = ? )\n" +
                "GROUP BY fai.term_acc,species_type_key )";

        update(query,speciesTypeKey,speciesTypeKey);
    }

    public void updateAnnotGeneCount(int count,String term,int speciesTypeKey) throws Exception{
        String query = "UPDATE GENE_ANNOT_COUNT SET GENE_COUNT = ? where term_acc = ? and species_type_key = ?";

        update(query,count,term,speciesTypeKey);
    }
}