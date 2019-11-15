package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.mapping.MapManager;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jdepons
 * @since Dec 28, 2007
 * DAO for manipulating/accessing GENES table
 */
public class GeneDAO extends AbstractDAO {


    public List<Gene> getAnnotatedGenes(String accId) throws Exception{

        String query = "SELECT distinct g.*, r.species_type_key FROM full_annot a,rgd_ids r, genes g " +
            " WHERE term_acc=? AND annotated_object_rgd_id=r.rgd_id "+
            " AND object_status='ACTIVE' and rgd_object_key=1  and r.rgd_id=g.rgd_id order by upper(g.gene_symbol)";

        return executeGeneQuery(query, accId);
    }

    public List<Gene> getAnnotatedGenes(String accId, List<Integer> speciesTypeKeys, List<String> evidenceCodes) throws Exception{

        String speciesInClause = "("+Utils.buildInPhrase(speciesTypeKeys)+")";
        String evidenceInClause = Utils.buildInPhraseQuoted(evidenceCodes);

        String query = "SELECT distinct g.*, r.species_type_key FROM full_annot a,rgd_ids r, genes g " +
                " WHERE term_acc=? AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key IN " + speciesInClause;
        query += " AND object_status='ACTIVE' and rgd_object_key=1 " +
                " AND evidence in (" + evidenceInClause + ") AND r.rgd_id=g.rgd_id ORDER BY upper(g.gene_symbol)";

        return executeGeneQuery(query, accId);
    }

    public List<Gene> getAnnotatedGenesAndOrthologs(String accId, List<Integer> speciesTypeKeys, List<String> evidenceCodes) throws Exception{

        String speciesInClause = "(";
        boolean first = true;
        for (Integer species: speciesTypeKeys) {
            if (first) {
                speciesInClause += species;
                first=false;
            }else {
                speciesInClause += "," + species;
            }
        }
        speciesInClause += ")";

        String evidenceInClause = Utils.buildInPhraseQuoted(evidenceCodes);

        String query = "SELECT distinct g.*, r.species_type_key FROM full_annot a,rgd_ids r, genes g " +
            " WHERE term_acc=? AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key IN " + speciesInClause +
            " AND object_status='ACTIVE' and rgd_object_key=1 " +
            " AND evidence IN (" + evidenceInClause + ") AND r.rgd_id=g.rgd_id " +
            "UNION " +
            " SELECT distinct g.*, s.species_type_key " +
            "         FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d, genes g " +
            " WHERE o.src_rgd_id=s.rgd_id AND s.rgd_id=g.rgd_id and o.dest_rgd_id=d.rgd_id AND s.object_status='ACTIVE' " +
                " AND d.object_status='ACTIVE' and s.species_type_key in " + speciesInClause +
                " AND o.dest_rgd_id in ( " +
            "        SELECT a.annotated_object_rgd_id FROM full_annot a,rgd_ids r " +
                " WHERE term_acc=? AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key IN " + speciesInClause +
            " AND object_status='ACTIVE' AND rgd_object_key=1 AND evidence in (" + evidenceInClause + ") )";

        return executeGeneQuery(query, accId, accId);
    }


    /**
     *  get all ortholog genes (for active genes)
     *
     * @param rgdId rgd id
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveOrthologs(int rgdId) throws Exception{

        String query = "SELECT g.*, r.species_type_key "+
                "FROM genes g, rgd_ids r, genetogene_rgd_id_rlt l "+
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND l.dest_rgd_id=g.rgd_id AND l.src_rgd_id=? "+
                "ORDER BY r.species_type_key ";
        return executeGeneQuery(query, rgdId);
    }

    /**
     *  get all ortholog genes (for active genes)
     *
     * @param rgdId rgd id
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveOrthologs(int rgdId, List<Integer> speciesTypeKeys) throws Exception{

        String query = "SELECT g.*, r.species_type_key "+
                "FROM genes g, rgd_ids r, genetogene_rgd_id_rlt l "+
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND l.dest_rgd_id=g.rgd_id AND l.src_rgd_id=? ";

        if (speciesTypeKeys.size() > 0) {
            query += " AND r.species_type_key in ( ";

            boolean first = true;
            for (Integer key : speciesTypeKeys) {
                if (first) {
                    query += key;
                    first=false;
                } else {
                    query += "," + key;
                }
            }
            query += ") ";
        }

        query += "ORDER BY r.species_type_key ";
        return executeGeneQuery(query, rgdId);
    }

    /**
     *  get all ortholog genes (for active genes)
     *
     * @param rgdIds rgd id
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveOrthologs(List<Integer> rgdIds, int speciesTypeKey) throws Exception{

        String query = "SELECT g.*, r.species_type_key "+
            " FROM genes g, rgd_ids r, genetogene_rgd_id_rlt l "+
            " WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND l.dest_rgd_id=g.rgd_id AND l.src_rgd_id in ( "+
            Utils.buildInPhrase(rgdIds)+
            ")  AND r.species_type_key=? " +
            " ORDER BY r.species_type_key ";

        return executeGeneQuery(query, speciesTypeKey);
    }

    /**
     *  get all ortholog genes (for active genes)
     *
     * @param rgdIds rgd id
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveOrthologs(List<Integer> rgdIds) throws Exception{

        String query = "SELECT g.*, r.species_type_key "+
            " FROM genes g, rgd_ids r, genetogene_rgd_id_rlt l "+
            " WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND l.dest_rgd_id=g.rgd_id AND l.src_rgd_id in ( "+
            Utils.buildInPhrase(rgdIds)+
            ")  AND r.species_type_key in (1,2,3) " +
            " ORDER BY r.species_type_key ";

        return executeGeneQuery(query);
    }


    /** get list of stringent AGR orthologs;
     * Note 1: NOTES field returns AGR_ORTHOLOGS.METHODS_MATCHED;
     * Note 2: GENE_DESC field returns RGD_ACC_XDB.ACC_ID for xdb_key=63 (AGR_GENE);
     *
     * @param rgdId gene rgd id for a gene
     * @return list of genes being orthologous to a given gene, as listed by AGR
     * @throws Exception
     */
    public List<Gene> getAgrOrthologs(int rgdId) throws Exception{

        String query = "SELECT g.gene_key,g.gene_symbol,g.full_name,x.acc_id gene_desc,g.agr_desc,g.merged_desc,"+
                "a.methods_matched notes,g.rgd_id,g.gene_type_lc,g.nomen_review_date,g.refseq_status,g.gene_source,"+
                "g.ncbi_annot_status,r.species_type_key,g.ensembl_gene_symbol,g.ensembl_gene_type,g.ensembl_full_name " +
                "FROM agr_orthologs a, genes g, rgd_ids r, rgd_acc_xdb x " +
                "WHERE a.gene_rgd_id_1=? AND a.gene_rgd_id_2=g.rgd_id AND g.rgd_id=r.rgd_id " +
                " AND confidence='stringent' AND x.rgd_id(+) = g.rgd_id AND x.xdb_key(+) = 63";
        return executeGeneQuery(query, rgdId);
    }

    public List<Gene> getActiveOrthologs(int rgdId, int speciesTypeKey) throws Exception{

        String query = "SELECT g.*, r.species_type_key "+
                " FROM genes g, rgd_ids r, genetogene_rgd_id_rlt l "+
                " WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND l.dest_rgd_id=g.rgd_id AND l.src_rgd_id=? "+
                "  AND r.species_type_key=? " +
                " ORDER BY r.species_type_key ";
        return executeGeneQuery(query, rgdId, speciesTypeKey);
    }

    /**
     * get active gene homologs for given gene rgd id
     * @param rgdId gene rgd id
     * @return List of Gene objects being homolog to given gene; empty list if there are no homologs available or if gene rgd id is invalid
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getHomologs(int rgdId) throws Exception {

        // code same as getActiveOrthologs()!
        return getActiveOrthologs(rgdId);
    }

    /**
     * Update gene in the datastore based on rgdID
     *
     * @param gene Gene object
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void updateGene(Gene gene) throws Exception{

        String sql = "update GENES set GENE_KEY=?, GENE_SYMBOL=?, GENE_SYMBOL_LC=LOWER(?), "+
                "FULL_NAME=?, GENE_DESC=?, AGR_DESC=?, MERGED_DESC=?, NOTES=?, FULL_NAME_LC=LOWER(?), "+
                "GENE_TYPE_LC=LOWER(?), NOMEN_REVIEW_DATE=?, REFSEQ_STATUS=?, NCBI_ANNOT_STATUS=?, "+
                "GENE_SOURCE=?, ENSEMBL_GENE_SYMBOL=?, ENSEMBL_GENE_TYPE=?, ENSEMBL_FULL_NAME=? where RGD_ID=?";

        update(sql, gene.getKey(), gene.getSymbol(), gene.getSymbol(), gene.getName(), gene.getDescription(),
                gene.getAgrDescription(), gene.getMergedDescription(), gene.getNotes(), gene.getName(), gene.getType(),
                gene.getNomenReviewDate(), gene.getRefSeqStatus(), gene.getNcbiAnnotStatus(), gene.getGeneSource(),
                gene.getEnsemblGeneSymbol(), gene.getEnsemblGeneType(), gene.getEnsemblFullName(), gene.getRgdId());
    }

    /**
     * insert a new gene into GENES table; upon successful insert, 'gene' object will have 'key'
     * property set to the key assigned automatically
     * @param gene Gene object to be inserted
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void insertGene(Gene gene) throws Exception{

        String sql = "insert into GENES (GENE_KEY, GENE_SYMBOL, GENE_SYMBOL_LC, " +
                "FULL_NAME, GENE_DESC, AGR_DESC, MERGED_DESC, NOTES, FULL_NAME_LC, GENE_TYPE_LC, NOMEN_REVIEW_DATE, "+
                "REFSEQ_STATUS, NCBI_ANNOT_STATUS, RGD_ID, GENE_SOURCE, ENSEMBL_GENE_SYMBOL, ENSEMBL_GENE_TYPE, ENSEMBL_FULL_NAME) "+
                "VALUES (?,?,LOWER(?),?,?,?,?,?,LOWER(?),LOWER(?),?,?,?,?,?,?,?,?)";

        int key = this.getNextKey("genes","gene_key");
        gene.setKey(key);

        update(sql, key, gene.getSymbol(), gene.getSymbol(), gene.getName(), gene.getDescription(), gene.getAgrDescription(),
                gene.getMergedDescription(), gene.getNotes(), gene.getName(), gene.getType(), gene.getNomenReviewDate(),
                gene.getRefSeqStatus(), gene.getNcbiAnnotStatus(), gene.getRgdId(), gene.getGeneSource(), gene.getEnsemblGeneSymbol(),
                gene.getEnsemblGeneType(), gene.getEnsemblFullName());
    }

    /**
     * Returns all active genes. Results do not contain splices or alleles
     *
     * @param keyword keyword
     * @param speciesKey species type key
     * @return
     * @throws Exception
     */
    public List<Gene> getActiveGenes(String keyword, int speciesKey) throws Exception {

        // keyword is a number (gene rgd id)
        try {
            int rgdId = Integer.parseInt(keyword);
            List<Gene> list = new ArrayList<>(1);
            Gene gene = this.getGene(rgdId);
            if (gene.getSpeciesTypeKey() == speciesKey) {
                list.add(gene);
            }
            return list;
        }catch (Exception continueOn) {
        }

        String query = "select g.*, r.SPECIES_TYPE_KEY from GENES g, RGD_IDS r " +
                "WHERE r.object_status='ACTIVE' AND r.species_type_key=? "+
                " AND NVL(gene_type_lc,'*') NOT IN('splice','allele') "+
                " AND r.rgd_id=g.rgd_id AND (g.gene_symbol_lc LIKE '%" + keyword + "%' OR g.full_name_lc LIKE '%" + keyword + "%')";
        return GeneQuery.execute(this, query, speciesKey);
    }


    public List<Gene> getActiveGenesSortedBySymbol(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "SELECT DISTINCT g.*, r.species_type_key \n" +
                "FROM genes g, rgd_ids r, maps_data md \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id \n"+
                " AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=? " +
                " order by g.gene_symbol";

        return GeneQuery.execute(this, query, chr, stopPos, startPos, mapKey);
    }


    /**
     * get active genes located within given chromosome range on a specific map
     * @param chr chromosome
     * @param startPos start pos
     * @param stopPos stop pos
     * @param mapKey map key
     * @return list of active genes located within given chromosome range on a specific map
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenes(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "SELECT DISTINCT g.*, r.species_type_key \n" +
                "FROM genes g, rgd_ids r, maps_data md \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id \n"+
                " AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=?";

        return GeneQuery.execute(this, query, chr, stopPos, startPos, mapKey);
    }

    public List<MappedGenePosition> getActiveMappedGenes(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "SELECT g.rgd_id as rgd_id, g.gene_symbol as symbol, r.species_type_key, md.* \n" +
                "FROM genes g, rgd_ids r, maps_data md \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id \n"+
                " AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=? \n"+
                "ORDER BY md.start_pos";

        return MappedGenePositionQuery.run(this, query, chr, stopPos, startPos, mapKey);
    }


    public List<MappedGene> getActiveMappedGenes(int mapKey, List<String> geneSymbols) throws Exception {

        if( geneSymbols.isEmpty() )
            return Collections.emptyList();
        int size = geneSymbols.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {
        if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
            if( i == (size - 1)) {
                if( i > 999)
                    j += 999;
                i += 1;
            } else j = i - 999;
            List<String> idList = geneSymbols.subList(j, i);
             query += "SELECT g.*, r.species_type_key, md.* \n" +
                "FROM genes g, rgd_ids r, maps_data md\n" +
                "WHERE r.object_status='ACTIVE' and r.RGD_ID=g.RGD_ID and md.rgd_id=g.rgd_id and md.map_key="+mapKey+
				" AND g.gene_symbol_lc IN ("+
                Utils.concatenate(",", idList, "toLowerCase", "'")+
                ") ";
                    if(i != size)
                        query += "UNION ";
                }
            }

        return MappedGeneQuery.run(this, query);
    }

    public List<MappedGene> getActiveMappedGenesByIds(int mapKey, List<Integer> rgdIds) throws Exception {

        if( rgdIds.isEmpty() )
            return Collections.emptyList();

        int size = rgdIds.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {

            for( i=0; i < size; i++ ) {

                if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                    if( i == (size - 1)) {
                        if( i > 999)
                        j += 999;
                        i += 1;
                    } else j = i - 999;
                    List<Integer> rgdIdsList = rgdIds.subList(j, i);
                    query += "SELECT g.*, r.species_type_key, md.* \n" +
                            "FROM genes g, rgd_ids r, maps_data md\n" +
                            "WHERE r.object_status='ACTIVE' and r.RGD_ID=g.RGD_ID and md.rgd_id=g.rgd_id and md.map_key=" + mapKey +
                            " AND g.rgd_id IN (";
                    boolean first = true;
                    for (Integer rgdId : rgdIdsList) {

                        if (first) {
                            query += rgdId;
                        } else {
                            query += "," + rgdId;
                        }
                        first = false;
                    }
                    query += ") ";
                    if(i != size)
                        query += "UNION ";
                }
            }
        }

        return MappedGeneQuery.run(this, query);
    }
    public List<MappedGene> getActiveMappedGenesByGeneList(int mapKey, List<Gene> genes) throws Exception {

        if( genes.isEmpty() )
            return Collections.emptyList();
        int size = genes.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {

            if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                if( i == (size - 1)) {
                    if( i > 999)
                    j += 999;
                    i += 1;
                } else j = i - 999;
                    List<Gene> idList = genes.subList(j, i);
                    query += "SELECT g.*, r.species_type_key, md.* \n" +
                            "from Genes g, RGD_IDS r , maps_data md\n" +
                            "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=g.RGD_ID and md.rgd_id=g.rgd_id and md.map_key="+mapKey +
                            " AND g.gene_symbol IN (" +
                            Utils.concatenate(",", idList, "getSymbol", "'") +
                            ") ";
                    if (i != size)
                        query += "UNION ";

            }
        }

        return MappedGeneQuery.run(this, query );
    }

    public List<MappedGene> getActiveMappedGenes(int mapKey) throws Exception {
        String query = "SELECT g.*, r.species_type_key, md.* \n" +
                "FROM genes g, rgd_ids r, maps_data md\n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id AND md.map_key=? "+
                "ORDER BY md.chromosome";

        return MappedGeneQuery.run(this, query, mapKey);
    }

    public List<MappedGene> getActiveMappedGenesBySpecies(int species, String chr) throws Exception {
        String query = "SELECT g.*, r.species_type_key, md.* \n" +
                " FROM genes g, rgd_ids r, maps_data md\n" +
                " WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id "+
                " AND md.map_key=?" +
                " and md.chromosome='" + chr + "'" +
                " ORDER BY md.start_pos";

        System.out.println(query);
        return MappedGeneQuery.run(this, query,  MapManager.getInstance().getReferenceAssembly(species).getKey());
    }




    public List<String> getGeneSymbolMapping(String dbsnpId, int mapKey, String dbSnpVersion) throws Exception{

        String query = "SELECT DISTINCT(gene_symbols) AS symbol FROM db_snp ds INNER JOIN gene_loci gl " +
                "on (ds.chromosome=gl.chromosome and ds.position = gl.pos and gl.map_key=?) " +
                "WHERE source=? AND ds.snp_name=?";

        return StringListQuery.execute(this, query, mapKey, dbSnpVersion, dbsnpId);
    }

    /**
     * Returns all active genes for given species. Results do not contain splices or alleles
     * @param speciesKey species type key
     * @return list of active genes for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenes(int speciesKey) throws Exception {
        String query = "SELECT g.*, r.species_type_key FROM genes g, rgd_ids r " +
                "WHERE r.object_status='ACTIVE' "+
                " AND r.species_type_key=? "+
                " AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') "+
                " AND r.rgd_id=g.rgd_id "+
                "ORDER BY g.gene_symbol_lc";

        return GeneQuery.execute(this, query, speciesKey);
    }

    /**
     * Returns all active genes for given species and gene symbol. Results do not contain splices or alleles
     * @param speciesKey species type key
     * @param symbol gene symbol
     * @return list of all active genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenes(int speciesKey, String symbol) throws Exception {

        String query = "SELECT g.*, r.species_type_key FROM genes g, rgd_ids r " +
                "WHERE r.object_status='ACTIVE' AND r.species_type_key=? "+
                "AND NVL(gene_type_lc,'*') NOT IN('splice','allele') AND r.rgd_id=g.rgd_id AND g.gene_symbol_lc=LOWER(?)";

        return GeneQuery.execute(this, query, speciesKey, symbol);
    }

    /**
     * get list of active genes of given type for given species
     * @param geneType gene type
     * @param speciesTypeKey  species type key
     * @return list of active genes of given type for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenesByType(String geneType, int speciesTypeKey) throws Exception {

        String query = "SELECT g.*,r.species_type_key FROM genes g, rgd_ids r "+
                "WHERE gene_type_lc=? AND g.rgd_id=r.rgd_id AND r.species_type_key=? AND object_status='ACTIVE'";

        return GeneQuery.execute(this, query, Utils.defaultString(geneType).trim().toLowerCase(), speciesTypeKey);
    }

    /**
     * get list of active genes excluding variants (splices and alleles)
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenes() throws Exception {

        String query = "SELECT g.*, r.species_type_key FROM genes g, rgd_ids r " +
                "WHERE r.object_status='ACTIVE' "+
                " AND NVL(gene_type_lc,'*') NOT IN('splice','allele') "+
                " AND r.rgd_id=g.rgd_id";

        return GeneQuery.execute(this, query);
    }

    /**
     * get list of all active genes including variants (splices and alleles) and genes without type
     * except 'zebrafish' genes
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getAllActiveGenes() throws Exception {

        String query = "select g.*, r.species_type_key from GENES g, RGD_IDS r " +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=g.RGD_ID "+
                "AND r.species_type_key<>8"; // exclude zebrafish genes

        return GeneQuery.execute(this, query);
    }

    /**
     * get list of all genes, active or not, including variants (splices and alleles) and genes without type
     * @return list of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getAllGenes(int speciesTypeKey) throws Exception {

        String query = "SELECT g.*, r.species_type_key FROM genes g, rgd_ids r " +
                "WHERE r.rgd_id=g.rgd_id AND species_type_key=?";

        return executeGeneQuery(query, speciesTypeKey);
    }

    /**
     * get list of genes with matching alias for given species
     * @param alias alias name
     * @param speciesTypeKey  species type key
     * @return list of genes with matching alias for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGenesByAlias(String alias, int speciesTypeKey) throws Exception {

        String query = "SELECT g.*,r.SPECIES_TYPE_KEY from GENES g, RGD_IDS r, ALIASES a "+
                "where a.ALIAS_VALUE_LC=LOWER(?) and g.RGD_ID=r.RGD_ID and r.SPECIES_TYPE_KEY=? and a.RGD_ID=g.RGD_ID";

        return executeGeneQuery(query, alias.trim(), speciesTypeKey);
    }

    public List<Gene> getGenesForAffyId(String affyId, int speciesTypeKey) throws Exception {

        String query = "select g.*, ri.species_type_key from aliases a, genes g, rgd_ids ri " +
                "where a.rgd_id=g.rgd_id  and a.rgd_id=ri.rgd_id and alias_type_name_lc like 'array_id_affy%' " +
                "and alias_value_lc=? and ri.object_status='ACTIVE' and ri.species_type_key=?";

        return executeGeneQuery(query, affyId.trim().toLowerCase(), speciesTypeKey);
    }

    /**
     * get one gene object by gene symbol and the species type key
     * @param geneSymbol gene symbol to be searched for
     * @param speciesKey species type key
     * @return a gene with exact matching symbol (other genes ignored, if any) or null if no match
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Gene getGenesBySymbol(String geneSymbol, int speciesKey) throws Exception {

        List<Gene> gk = getAllGenesBySymbol(geneSymbol, speciesKey);

        if( gk==null || gk.size()==0 ) {
            return null;
        }
        
        return gk.get(0);
    }

    /**
     * get all genes falling entirely or partially within given area
     * @param chr chromosome
     * @param start start position on chromosome
     * @param stop stop position on chromosome
     * @param mapKey key of map to be searched
     * @return gene list that fall entirely or partially within given area
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGenesByPosition(String chr, long start, long stop, int mapKey) throws Exception {

        String query = "SELECT g.*, ri.species_type_key FROM genes g, maps_data md, rgd_ids ri " +
                "WHERE g.rgd_id=ri.rgd_id AND md.rgd_id=g.rgd_id AND md.chromosome=? "+
                "AND md.start_pos <= ? AND md.stop_pos >= ? AND map_key=? AND ri.object_status='ACTIVE'";

        return executeGeneQuery(query, chr.toUpperCase(), stop, start, mapKey);
    }


    /**
     * get gene objects by gene symbol and the species type key;
     * note: non-active genes, as well as gene variants (splices, alleles) could be returned as well
     * @param geneSymbol gene symbol to be searched for
     * @param speciesKey species type key
     * @return list of all genes with exact matching symbol (empty list possible)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getAllGenesBySymbol(String geneSymbol, int speciesKey) throws Exception {

        if( geneSymbol == null)
            return null;

        String query = "SELECT * FROM genes g, rgd_ids r "+
                "WHERE g.gene_symbol_lc=? AND g.rgd_id=r.rgd_id AND r.species_type_key=? "+
                "ORDER BY r.object_status"; // active genes are returned first

        return GeneQuery.execute(this, query, geneSymbol.trim().toLowerCase(), speciesKey);
    }

    /**
     * get rgd ids for genes given gene symbol and species type key;
     * note: non-active genes, as well as gene variants (splices, alleles) could be returned as well
     * @param geneSymbol gene symbol to be searched for
     * @param speciesKey species type key
     * @return list of all rgd ids for genes with exact matching symbol (empty list possible)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getAllGeneRgdIdsBySymbol(String geneSymbol, int speciesKey) throws Exception {

        if( geneSymbol == null)
            return null;

        String query = "SELECT g.rgd_id FROM genes g, rgd_ids r "+
                "WHERE g.gene_symbol_lc=? AND g.rgd_id=r.rgd_id AND r.species_type_key=? "+
                "ORDER BY r.object_status"; // active genes are returned first

        return IntListQuery.execute(this, query, geneSymbol.trim().toLowerCase(), speciesKey);
    }
    /**
     * get active rgd ids for genes given gene symbols and species type key;
     * @param geneSymbols gene symbol to be searched for
     * @param speciesKey species type key
     * @return list of all rgd ids for genes with exact matching symbol (empty list possible)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getActiveGeneRgdIdsBySymbols(List<String> geneSymbols, int speciesKey) throws Exception {

        if( geneSymbols == null)
            return null;
        int size = geneSymbols.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {
            if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                if( i == (size - 1)) {
                   if( i > 999)
                        j += 999;
                    i += 1;
                } else j = i - 999;

            List<String> idList = geneSymbols.subList(j, i);
            query += "SELECT g.rgd_id FROM genes g, rgd_ids r "+
                "WHERE r.species_type_key="+ speciesKey + " AND g.gene_symbol_lc IN ("+
                Utils.concatenate(",", idList, "toLowerCase", "'")+
                ") AND g.rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";
                    if (i != size)
                        query += "UNION ";

            }
        }

        return IntListQuery.execute(this, query);
    }

    /**
     * get active rgd ids for genes given gene symbols and species type key;
     * @param geneSymbols gene symbol to be searched for
     * @param speciesKey species type key
     * @return list of all active genes with exact matching symbol (empty list possible)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenesBySymbols(List<String> geneSymbols, int speciesKey) throws Exception {

        if( geneSymbols == null)
            return null;
        int size = geneSymbols.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {
            if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                if( i == (size - 1)) {
                    if( i > 999)
                        j += 999;
                    i += 1;
                } else j = i - 999;

                List<String> idList = geneSymbols.subList(j, i);
         query += "SELECT * FROM genes g, rgd_ids r "+
                "WHERE r.species_type_key="+ speciesKey + " AND g.gene_symbol_lc IN ("+
                Utils.concatenate(",", idList, "toLowerCase", "'")+
                ") AND g.rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";
                    if (i != size)
                        query += "UNION ";
                }
            }


        return GeneQuery.execute(this, query);
    }
    /**
     * Returns a count of all active genes with nomenclature review date between given pair of dates.
     * Results do not contain splices or alleles.
     * @param speciesKey species type key
     * @param fromNomenclatureReview starting date of nomenclature review
     * @param toNomenclatureReview ending date of nomenclature review
     * @return count of active genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getActiveGeneCount(int speciesKey, java.util.Date fromNomenclatureReview, java.util.Date toNomenclatureReview) throws Exception {

        String query = "SELECT COUNT(*) FROM genes g, rgd_ids r " +
        "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND NVL(gene_type_lc,'*') NOT IN('splice','allele') " +
        " AND (g.nomen_review_date BETWEEN ? AND ? OR g.nomen_review_date IS NULL) "+
        " AND r.rgd_id=g.rgd_id ";

        return getCount(query, speciesKey, fromNomenclatureReview, toNomenclatureReview);
    }

    /**
     * Returns all active genes with nomenclature review date between given pair of dates.
     * Results do not contain splices or alleles.
     * @param speciesKey species type key
     * @param fromNomenclatureReview starting date of nomenclature review
     * @param toNomenclatureReview ending date of nomenclature review
     * @return count of active genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getActiveGenes(int speciesKey, java.util.Date fromNomenclatureReview, java.util.Date toNomenclatureReview) throws Exception {

        String query = "SELECT g.*, r.species_type_key FROM genes g, rgd_ids r " +
            "WHERE r.object_status='ACTIVE' and r.species_type_key=? AND NVL(gene_type_lc,'*') NOT IN('splice','allele') " +
            " AND (g.nomen_review_date BETWEEN ? AND ? OR g.nomen_review_date IS NULL) "+
            " AND r.rgd_id=g.rgd_id ORDER BY g.gene_symbol_lc";

        GeneQuery q = new GeneQuery(this.getDataSource(), query);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.TIMESTAMP));
        q.declareParameter(new SqlParameter(Types.TIMESTAMP));

        q.compile();
        return q.execute(new Object[]{speciesKey, fromNomenclatureReview, toNomenclatureReview});
    }

    /**
     * get genes associated with given rgd object
     * @param associatedObjectRgdId rgd id of associated object
     * @return List of Gene objects associated with another rgd object
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Gene> getAssociatedGenes(int associatedObjectRgdId) throws Exception {
        String query = "select g.*, ri.species_type_key from rgd_associations a, genes g, rgd_ids ri\n" +
                "where a.master_rgd_id=? and a.detail_rgd_id=g.rgd_id and g.rgd_id = ri.rgd_id";
        return executeGeneQuery(query, associatedObjectRgdId);
    }
    /**
     * Returns a Gene based on an rgd id
     * @param rgdId rgd id
     * @return Gene object for given rgd id
     * @throws Exception thrown when there is no gene with such rgd id
     */
    public Gene getGene(int rgdId) throws Exception {
        String query = "select g.*, r.SPECIES_TYPE_KEY from GENES g, RGD_IDS r where r.RGD_ID=g.RGD_ID and r.RGD_ID=?";

        List<Gene> genes = GeneQuery.execute(this, query, rgdId);
        if (genes.size() == 0) {
            throw new GeneDAOException("Gene " + rgdId + " not found");
        }
        return genes.get(0);
    }
      /**
     * Returns a Gene based on an rgd id
     * @param rgdIds rgd ids
     * @return List<Gene> object for given rgd id
     * @throws Exception thrown when there is no gene with such rgd id
     */
    public List<Gene> getGeneByRgdIds(List<Integer> rgdIds) throws Exception {

        int size = rgdIds.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {

            if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                if( i == (size - 1)) {
                    if( i > 999)
                        j += 999;
                    i += 1;
                } else j = i - 999;


        List<Integer> idList = rgdIds.subList(j, i);
        query += "select g.*, r.SPECIES_TYPE_KEY from GENES g, RGD_IDS r where r.RGD_ID=g.RGD_ID and r.RGD_ID in (";
        boolean first = true;
        for (Integer rgdId: idList) {

            if (first) {
                query += rgdId;
            }else {
                query += "," + rgdId;
            }
            first=false;
        }
        query += ") ";
        if (i != size)
            query += "UNION ";
        }
        }

        List<Gene> genes = GeneQuery.execute(this, query);
        return genes;
    }
    /**
     * get gene object by gene key
     * @param key gene key
     * @return Gene object
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Gene getGeneByKey(int key) throws Exception {

        String query = "select g.*, r.SPECIES_TYPE_KEY from GENES g, RGD_IDS r where r.RGD_ID=g.RGD_ID and g.gene_key=?";

        List<Gene> genes = GeneQuery.execute(this, query, key);

        if (genes.size() == 0) {
            throw new GeneDAOException("Gene key " + key + " not found");
        }
        return genes.get(0);
    }

    /**
     * return active gene variants (alleles and splices)
     * @param geneRgdId gene rgd id
     * @return list of Gene objects of type 'allele' or 'splice'; could be empty list
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getVariantFromGene(int geneRgdId) throws Exception {
        String query = "SELECT gr.*,r.species_type_key FROM GENES gr, RGD_IDs r, GENES_VARIATIONS v, GENES g " +
                "WHERE r.rgd_id=gr.rgd_id AND gr.gene_key=v.variation_key AND v.gene_key=g.gene_key AND g.rgd_id=? AND r.object_status='ACTIVE'";
        return GeneQuery.execute(this, query, geneRgdId);
    }

    /**
     * return active gene variations (alleles and splices) for given gene
     * @param geneRgdId gene rgd id
     * @return list of GeneVariation objects of type 'allele' or 'splice'; could be empty list
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<GeneVariation> getGeneVariations(int geneRgdId) throws Exception {
        String query = "SELECT v.*,g.rgd_id,r.species_type_key,gv.rgd_id variation_rgd_id "+
            "FROM genes_variations v,genes g,rgd_ids r,genes gv " +
            "WHERE v.gene_key=g.gene_key AND g.rgd_id=r.rgd_id AND gv.gene_key=variation_key AND r.rgd_id=?";
        GeneVariationQuery q = new GeneVariationQuery(this.getDataSource(), query);
        return execute(q, geneRgdId);
    }

    /**
     * return genes from variants (alleles and splices)
     * @param variantRgdId gene rgd id
     * @return list of Gene objects of type 'allele' or 'splice'; could be empty list
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGeneFromVariant(int variantRgdId) throws Exception {
        String query = "SELECT gr.*,r.species_type_key FROM genes gr, rgd_ids r, genes_variations v, genes g " +
                "WHERE r.rgd_id=gr.rgd_id AND g.gene_key=v.variation_key AND v.gene_key=gr.gene_key AND g.rgd_id=?";
        return GeneQuery.execute(this, query, variantRgdId);
    }


    /**
     * return active gene variants of given type (alleles and splices)
     * @param geneKey gene rgd id
     * @param varType variant type; currently one of 'allele' or 'splice'
     * @return list of Gene variant objects of given type; could be empty list
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getVariantsForGene(int geneKey, String varType) throws Exception {
        String query = "SELECT g.*,r.species_type_key FROM genes_variations gv, genes g, rgd_ids r "+
                "WHERE gv.gene_key=? AND gv.variation_key=g.gene_key AND r.rgd_id=g.rgd_id AND gv.gene_variation_type=? AND r.object_status='ACTIVE'";
        return GeneQuery.execute(this, query, geneKey, varType);
    }

    /**
     * insert a gene variation (gene variation is a gene too)
     * @param variationGeneRgdId rgd id for gene variation
     * @param geneRgdId rgd id of gene
     * @param variationType variation type: one of 'allele','splice'
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertVariation(int variationGeneRgdId, int geneRgdId, String variationType) throws Exception {

        String sql =
            "INSERT INTO genes_variations (variation_key,gene_key,gene_variation_type) "+
            "SELECT v.gene_key,g.gene_key,? FROM genes v,genes g " +
            "WHERE v.rgd_id=? AND g.rgd_id=?";

        return update(sql, variationType, variationGeneRgdId, geneRgdId);
    }

    /**
     * update gene variation (type and notes)
     * @param var GeneVariation object to be updated
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateGeneVariation(GeneVariation var) throws Exception {

        String sql = "UPDATE genes_variations "+
            "SET gene_variation_type=?,notes=? " +
            "WHERE gene_key=? AND variation_key=?";

        return update(sql, var.getGeneVariationType(), var.getNotes(), var.getKey(), var.getVariationKey());
    }

    /**
     * delete gene variation (gene_key and variation_key must be set)
     * @param var GeneVariation object to be deleted
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteGeneVariation(GeneVariation var) throws Exception {

        String sql =
            "DELETE FROM genes_variations "+
            "WHERE gene_key=? AND variation_key=?";

        return update(sql, var.getKey(), var.getVariationKey());
    }

    /**
     * @deprecated
     * @see AliasDAO#insertAlias(edu.mcw.rgd.datamodel.Alias) insertAlias
     */
    public void createAlias(Alias alias) throws Exception {

        // note: the unique key on ALIASES table will take care of duplicates

        // now insert the alias
        new AliasDAO().insertAlias(alias);
    }

     /**
      * @deprecated
      * @see AliasDAO#deleteAlias(int, java.util.List) deleteAlias
      */
     public void deleteAlias(int rgdId, List valueList) throws Exception{

         new AliasDAO().deleteAlias(rgdId, valueList);
     }




    /**
     * get list of all available gene types
     * @return list of all genes types
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<String> getTypes() throws Exception{

        String query = "SELECT gene_type_lc FROM gene_types";
        return StringListQuery.execute(this, query);
    }

    /**
     * return true if given geneType is in GENE_TYPES table
     * @param geneType gene type
     * @return true if given geneType is in GENE_TYPES table
     * @throws Exception
     */
    public boolean existsGeneType(String geneType) throws Exception {

        // sanity check
        int exists = getCount("SELECT COUNT(gene_type_lc) FROM gene_types WHERE gene_type_lc=?", geneType);
        return exists>0;
    }

    public List<Gene> getActiveAnnotatedGenes(String termAcc, int speciesTypeKey) throws Exception {

        String query = "select * from genes g, rgd_ids ri where g.rgd_id=ri.rgd_id and g.rgd_id in ( " +
                " select distinct(annotated_object_rgd_id) from full_annot_index fai,  full_annot fa " +
                " where fai.full_annot_key=fa.full_annot_key and fai.term_acc=? ) " +
                " and ri.object_status='ACTIVE' and ri.species_type_key=?";

        return GeneQuery.execute(this, query, termAcc, speciesTypeKey);
    }
    public List<Gene> getActiveAnnotatedGenes(List<Integer> rgdIds,String termAcc, int speciesTypeKey) throws Exception {

        int size = rgdIds.size();
        int i,j=0;
        String query = "";
        for( i=0; i < size; i++ ) {

            if (( i % 999 == 0 && i != 0 )|| (i == (size - 1))) {
                if( i == (size - 1)) {
                    if( i > 999)
                    j += 999;
                    i += 1;
                } else j = i - 999;
                    List<Integer> idList = rgdIds.subList(j, i);
        query += "select * from genes g, rgd_ids ri where g.rgd_id=ri.rgd_id and g.rgd_id in ( " +
                " select distinct(annotated_object_rgd_id) from full_annot_index fai,  full_annot fa " +
                " where fai.full_annot_key=fa.full_annot_key and fai.term_acc='"+termAcc+"') " +
                " and ri.object_status='ACTIVE' and ri.species_type_key=" +speciesTypeKey+
                " and g.rgd_id in ("+Utils.concatenate(idList,",") +") ";
                    if (i != size)
                        query += "UNION ";
                }
            }

        return GeneQuery.execute(this, query);
    }
    public List<Gene> getGeneDataWithinRange(int lowerRange, int higherRange, String chr, int mapKey) throws Exception{

        String query = "SELECT g.*, r.species_type_key from MAPS_DATA m, RGD_IDS r, GENES g where " +
                "m.CHROMOSOME=? " +
                "and m.MAP_KEY=? " +
                "and m.STOP_POS>=? " +
                "and m.START_POS<=? " +
                "and m.RGD_ID=r.RGD_ID " +
                "and g.RGD_ID=r.RGD_ID " +
                "and r.OBJECT_KEY=1 " +
                "and r.OBJECT_STATUS='ACTIVE' " +
                "order by g.GENE_SYMBOL_LC";

        return executeGeneQuery(query, chr, mapKey, lowerRange, higherRange);
    }

    /**
     * insert new gene type into GENE_TYPES table
     * @param geneType new gene type
     * @param geneTypeDesc gene type description
     * @param geneDescPublic gene public description
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void createGeneType(String geneType, String geneTypeDesc, String geneDescPublic) throws Exception {

        // sanity check
        if( geneType==null || geneType.trim().length()==0 )
            throw new GeneDAOException("cannot insert empty gene type");

        String sql = "insert into GENE_TYPES (GENE_TYPE_LC, GENE_TYPE_DESC, GENE_DESC_PUBLIC) values(LOWER(?),?,?)";
        update(sql, geneType.trim(), geneTypeDesc, geneDescPublic);
    }

    /**
     * GeneDAOException should be thrown by GeneDAO methods to differentiate between ours and the framework's exceptions
     */
    public class GeneDAOException extends Exception {

        public GeneDAOException(String msg) {
            super(msg);
        }
    }

    /// Gene query implementation helper
    public List<Gene> executeGeneQuery(String query, Object ... params) throws Exception {
        return GeneQuery.execute(this, query, params);
    }

    public int getRgdId(String geneSymbol) throws Exception{
        String sql = "select rgd_id from genes where gene_symbol_lc=LOWER(?)";
        List<Integer> rgdIds = IntListQuery.execute(this, sql, geneSymbol);
        return rgdIds.size()==0 ? 0 : rgdIds.get(0);
    }

    public List<Gene> getGenes(String geneSymbol)throws Exception{
        String sql= "select * from genes where gene_symbol_lc=?";
        return GeneQuery.execute(this, sql, geneSymbol);
    }

    public List<Gene> getAllGenesBySubSymbol(String geneSymbol, int speciesKey) throws Exception {

        if( geneSymbol == null)
            return null;

        String query = "SELECT * FROM genes g, rgd_ids r "+
                "WHERE g.gene_symbol_lc like ? AND g.rgd_id=r.rgd_id AND r.species_type_key=? AND NVL(gene_type_lc,'*') NOT IN('splice','allele') "+
                "ORDER BY r.object_status"; // active genes are returned first

        return GeneQuery.execute(this, query, (geneSymbol.trim().toLowerCase())+'%', speciesKey);
    }

    /// get list of protein domains associated with a given gene (possibly empty)
    public List<Gene> getGenesForProteinDomain(int proteinDomainRgdId) throws Exception {

        String sql = "SELECT g.*,i.species_type_key\n" +
                "FROM genes g,rgd_ids i \n" +
                "WHERE g.rgd_id=i.rgd_id AND g.rgd_id IN(\n" +
                "  SELECT a2.detail_rgd_id FROM rgd_associations a1,rgd_associations a2\n" +
                "  WHERE a1.assoc_type='protein_to_domain' AND a2.assoc_type='protein_to_gene' AND a1.master_rgd_id=a2.master_rgd_id AND a1.detail_rgd_id=?\n" +
                ")";

        return GeneQuery.execute(this, sql, proteinDomainRgdId);
    }
}
