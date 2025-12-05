package edu.mcw.rgd.dao.impl.solr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.mcw.rgd.dao.AbstractDAO;

import edu.mcw.rgd.dao.spring.SolrDocQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.solr.SolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDocDB;
import org.apache.solr.common.SolrInputDocument;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SolrDocsDAO extends AbstractDAO {
    ObjectMapper mapper=new ObjectMapper();
    Gson gson=new Gson();
    public int addBatch(List<SolrDoc> solrDocs) throws Exception {
        String fields="SOLR_DOC_ID, "+getSolrDocFields().stream().collect(Collectors.joining(", "))+", last_update_date";
        String sql= "INSERT INTO SOLR_DOCS ("+ fields+") VALUES (" +
                "NEXTVAL('SOLR_DOC_SEQ'), " ;
        sql+=getSolrDocFields().stream().map(f->"?").collect(Collectors.joining(","));
        sql+=", NOW())";
        try(Connection connection=this.getPostgressConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            connection.setAutoCommit(false);

            for(SolrDoc solrDoc:solrDocs) {
                SolrDocDB doc = buildSolrDocDB(solrDoc);
                List<Object> params=docParams(doc);
                for(int i=0;i<params.size();i++){
                    Object value=params.get(i);
                    int paramIndex=i+1;
                    if(value instanceof java.sql.Date){
                        preparedStatement.setDate(paramIndex, (java.sql.Date) value);
                    }else if(value instanceof Integer){
                        preparedStatement.setInt(paramIndex, (Integer) value);
                    }else{
                        preparedStatement.setString(paramIndex, value!=null? value.toString():null);
                    }
                }
                 preparedStatement.addBatch();

            }
            int [] numUpdates=preparedStatement.executeBatch();
            for(int i=0; i<numUpdates.length;i++){
                if(numUpdates[i]==-2)
                System.out.println("Execution "+ i+ ": unknown number of rows updated");
//                else
                  //  System.out.println("Execution " + i+ " successful: "+ numUpdates[i]+" rows updated");
            }
            connection.commit();
           return 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    public List<Object> docParams(SolrDocDB doc){
        return Arrays.asList(
                doc.getGeneCount(),
                doc.getMpId(),
                doc.getDoiS(),
                doc.getChebiPos(),
                doc.getVtId(),
                doc.getBpTerm(),
                doc.getChebiTerm(),
                doc.getpDate(),          // java.sql.Date
                doc.getXcoTerm(),
                doc.getChebiCount(),
                doc.getRsTerm(),
                doc.getMpTerm(),
                doc.getRdoId(),
                doc.getNboPos(),
                doc.getGene(),
                doc.getRsId(),
                doc.getSoTerm(),
                doc.getMpCount(),
                doc.getVtCount(),
                doc.getBpId(),
                doc.getRgdObjCount(),
                doc.getVtPos(),
                doc.getpType(),
                doc.getNboCount(),
                doc.getXcoId(),
                doc.getpYear(),          // int
                doc.getXcoCount(),
                doc.getRdoCount(),
                doc.getTitle(),
                doc.getNboTerm(),
                doc.getVtTerm(),
                doc.getHpPos(),
                doc.getNboId(),
                doc.getSoCount(),
                doc.getHpTerm(),
                doc.getSoId(),
                doc.getRgdObjPos(),
                doc.getXcoPos(),
                doc.getRsPos(),
                doc.getHpId(),
                doc.getRdoPos(),
                doc.getRsCount(),
                doc.getRgdObjTerm(),
                doc.getPmid(),
                doc.getBpCount(),
                doc.getMpPos(),
                doc.getHpCount(),
                doc.getXdbId(),
                doc.getRgdObjId(),
                doc.getBpPos(),
                doc.getGenePos(),
                doc.getSoPos(),
                doc.getRdoTerm(),
                doc.getChebiId(),
                doc.getSolrDocId(),
                doc.get_abstract(),
                doc.getAuthors(),
                doc.getjDates(),
                doc.getCitation(),
                doc.getMeshTerms(),
                doc.getKeywords(),
                doc.getChemicals(),
                doc.getAffiliation(),
                doc.getIssn(),
                doc.getOrganismCommonName(),
                doc.getOrganismTerm(),
                doc.getOrganismNcbiId(),
                doc.getOrganismCount(),
                doc.getOrganismPos(),
                doc.getPmcId(),
                doc.getCcCount(),
                doc.getCcId(),
                doc.getCcTerm(),
                doc.getCcPos(),
                doc.getMfCount(),
                doc.getMfId(),
                doc.getMfPos(),
                doc.getMfTerm(),
                doc.getMtId(),
                doc.getMtTerm(),
                doc.getMtPos(),
                doc.getMtCount(),
                doc.getpSource(),
                doc.getZfaId(),
                doc.getZfaTerm(),
                doc.getZfaCount(),
                doc.getZfaPos(),
                doc.getMaId(),
                doc.getMaCount(),
                doc.getMaPos(),
                doc.getMaTerm(),
                doc.getMmoId(),
                doc.getMmoCount(),
                doc.getMmoPos(),
                doc.getMmoTerm(),
                doc.getPwId(),
                doc.getPwCount(),
                doc.getPwPos(),
                doc.getPwTerm(),
                doc.getCmoId(),
                doc.getCmoCount(),
                doc.getCmoPos(),
                doc.getCmoTerm(),
                doc.getUberonId(),
                doc.getUberonCount(),
                doc.getUberonPos(),
                doc.getUberonTerm(),
                doc.getClId(),
                doc.getClCount(),
                doc.getClPos(),
                doc.getClTerm(),
                doc.getEfoId(),
                doc.getEfoCount(),
                doc.getEfoTerm(),
                doc.getEfoPos()

        );
    }
    public List<Object> docMissingParams(SolrDocDB doc){
        return Arrays.asList(

                doc.getCcCount(),
                doc.getCcId(),
                doc.getCcTerm(),
                doc.getCcPos(),
                doc.getMfCount(),
                doc.getMfId(),
                doc.getMfPos(),
                doc.getMfTerm(),
                doc.getMtId(),
                doc.getMtTerm(),
                doc.getMtPos(),
                doc.getMtCount(),
                doc.getpSource(),
                doc.getZfaId(),
                doc.getZfaTerm(),
                doc.getZfaCount(),
                doc.getZfaPos(),
                doc.getMaId(),
                doc.getMaCount(),
                doc.getMaPos(),
                doc.getMaTerm(),
                doc.getMmoId(),
                doc.getMmoCount(),
                doc.getMmoPos(),
                doc.getMmoTerm(),
                doc.getPwId(),
                doc.getPwCount(),
                doc.getPwPos(),
                doc.getPwTerm(),
                doc.getCmoId(),
                doc.getCmoCount(),
                doc.getCmoPos(),
                doc.getCmoTerm(),
                doc.getUberonId(),
                doc.getUberonCount(),
                doc.getUberonPos(),
                doc.getUberonTerm(),
                doc.getClId(),
                doc.getClCount(),
                doc.getClPos(),
                doc.getClTerm(),
                doc.getEfoId(),
                doc.getEfoCount(),
                doc.getEfoTerm(),
                doc.getEfoPos()
        );
    }
    public boolean exists(String pmid) throws Exception {
        String sql="select pmid from solr_docs where pmid=?";
        StringListQuery query=new StringListQuery(this.getPostgressDataSource(), sql);
        List<String> pmids=execute(query, pmid);

        return pmids.size()>0;
    }

    /**
     * Batch check for existing PMIDs in the database
     * @param pmids List of PMIDs to check
     * @return Set of PMIDs that exist in the database
     * @throws Exception if database error occurs
     */
    public Set<String> getExistingPmids(List<String> pmids) throws Exception {
        if (pmids == null || pmids.isEmpty()) {
            return Collections.emptySet();
        }

        // Remove nulls and filter only valid pmids
        List<String> validPmids = pmids.stream()
                .filter(Objects::nonNull)
                .filter(p -> !p.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (validPmids.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> existingPmids = new HashSet<>();

        // Process in chunks of 1000 to avoid SQL query size limits
        int chunkSize = 1000;
        for (int i = 0; i < validPmids.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, validPmids.size());
            List<String> chunk = validPmids.subList(i, endIndex);

            // Build SQL with IN clause
            String placeholders = chunk.stream()
                    .map(p -> "?")
                    .collect(Collectors.joining(","));
            String sql = "SELECT pmid FROM solr_docs WHERE pmid IN (" + placeholders + ")";

            StringListQuery query = new StringListQuery(this.getPostgressDataSource(), sql);
            List<String> foundPmids = execute(query, chunk.toArray());

            existingPmids.addAll(foundPmids);
        }

        return existingPmids;
    }
//    public int insert(SolrDoc solrDoc) throws Exception {
//
//        String fields="SOLR_DOC_ID, "+getSolrDocFields().stream().collect(Collectors.joining(", "))+", last_update_date";
//        SolrDocDB doc=buildSolrDocDB(solrDoc);
//        String sql= "INSERT INTO SOLR_DOCS ("+ fields+") VALUES (" +
//                "NEXTVAL('SOLR_DOC_SEQ'), " +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
//                "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?, NOW()" +
//                ")";
//
//
//        return   updateSolrPostgress(sql,
//                doc.getGeneCount()	, doc.getMpId()	, doc.getDoiS()	, doc.getChebiPos()	, doc.getVtId()	,
//                doc.getBpTerm()	, doc.getChebiTerm()	, doc.getpDate()	, doc.getXcoTerm()	, doc.getChebiCount()	,
//                doc.getRsTerm()	, doc.getMpTerm()	, doc.getRdoId()	, doc.getNboPos()	, doc.getGene()	,
//                doc.getRsId()	, doc.getSoTerm()	, doc.getMpCount()	, doc.getVtCount()	, doc.getBpId()	,
//                doc.getRgdObjCount()	, doc.getVtPos()	, doc.getpType()	, doc.getNboCount()	, doc.getXcoId()	,
//                doc.getpYear()	, doc.getAuthors()	, doc.getXcoCount()	, doc.getRdoCount()	, doc.getTitle()	,
//                doc.getNboTerm()	, doc.getVtTerm()	, doc.getHpPos()	, doc.getNboId()	, doc.getSoCount()	,
//                doc.getHpTerm()	, doc.getSoId()	, doc.getRgdObjPos()	, doc.getXcoPos()	, doc.getRsPos()	,
//                doc.getHpId()	, doc.getRdoPos()	, doc.getRsCount()	, doc.getRgdObjTerm()	, doc.get_abstract()	,
//                doc.getPmid()	, doc.getMpCount()	, doc.getMpPos()	, doc.getHpCount()	, doc.getXdbId()	,
//                doc.getRgdObjId()	, doc.getBpPos()	, doc.getGenePos()	, doc.getSoPos()	, doc.getRdoTerm()	,
//                doc.getChebiId(), doc.getjDates(), doc.getCitation(), doc.getMeshTerms(), doc.getKeywords(),
//                doc.getChemicals(), doc.getAffiliation(), doc.getIssn(), doc.getOrganismCommonName(), doc.getOrganismTerm(),
//               doc.getOrganismNcbiId(), doc.getOrganismCount(), doc.getOrganismPos(), doc.getPmcId());
//
//    }
    public int update(SolrDoc solrDoc) throws Exception {
        SolrDocDB doc=buildSolrDocDB(solrDoc);
        String sql= "UPDATE SOLR_DOCS set" + getSolrDocFields().stream().collect(Collectors.joining("=?"))+"=?, set last_update_date=NOW() " +
                " where pmid=?";
        return   updateSolrPostgress(sql,
                doc.getGeneCount()	, doc.getMpId()	, doc.getDoiS()	, doc.getChebiPos()	, doc.getVtId()	,
                doc.getBpTerm()	, doc.getChebiTerm()	, doc.getpDate()	, doc.getXcoTerm()	, doc.getChebiCount()	,
                doc.getRsTerm()	, doc.getMpTerm()	, doc.getRdoId()	, doc.getNboPos()	, doc.getGene()	,
                doc.getRsId()	, doc.getSoTerm()	, doc.getMpCount()	, doc.getVtCount()	, doc.getBpId()	,
                doc.getRgdObjCount()	, doc.getVtPos()	, doc.getpType()	, doc.getNboCount()	, doc.getXcoId()	,
                doc.getpYear()	, doc.getAuthors()	, doc.getXcoCount()	, doc.getRdoCount()	, doc.getTitle()	,
                doc.getNboTerm()	, doc.getVtTerm()	, doc.getHpPos()	, doc.getNboId()	, doc.getSoCount()	,
                doc.getHpTerm()	, doc.getSoId()	, doc.getRgdObjPos()	, doc.getXcoPos()	, doc.getRsPos()	,
                doc.getHpId()	, doc.getRdoPos()	, doc.getRsCount()	, doc.getRgdObjTerm()	, doc.get_abstract()	,
                doc.getPmid()	, doc.getMpCount()	, doc.getMpPos()	, doc.getHpCount()	, doc.getXdbId()	,
                doc.getRgdObjId()	, doc.getBpPos()	, doc.getGenePos()	, doc.getSoPos()	, doc.getRdoTerm()	,
                doc.getChebiId(), doc.getPmid(),
                doc.getjDates(),
                doc.getCitation(),
                doc.getMeshTerms(),
                doc.getKeywords(),
                doc.getChemicals(),
                doc.getAffiliation(),
                doc.getIssn(),
                doc.getOrganismCommonName(),
                doc.getOrganismTerm(),
                doc.getOrganismNcbiId(),
                doc.getOrganismCount(),
                doc.getOrganismPos(),
                doc.getPmcId());


    }
    /**
     * Batch update existing PMID records in the database
     * @param solrDocs List of SolrDoc objects to update
     * @return number of records updated
     * @throws Exception if database error occurs
     */
    public int updateBatch(List<SolrDoc> solrDocs) throws Exception {
        if (solrDocs == null || solrDocs.isEmpty()) {
            return 0;
        }

        // Build UPDATE SQL with all fields except SOLR_DOC_ID
//        List<String> fields = getSolrDocFields();
        List<String> fields = getSolrDocMissingFields();
        // Remove last_update_date from fields list as we'll set it separately
        List<String> updateFields = fields.stream()
                .filter(f -> !f.equals("last_update_date"))
                .collect(Collectors.toList());

        String setClause = updateFields.stream()
                .map(f -> f + " = ?")
                .collect(Collectors.joining(", "));

        String sql = "UPDATE SOLR_DOCS SET " + setClause + ", last_update_date = NOW() WHERE pmid = ?";

        int totalUpdated = 0;
        try (Connection connection = this.getPostgressConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            for (SolrDoc solrDoc : solrDocs) {
                SolrDocDB doc = buildSolrDocDB(solrDoc);
                List<Object> params = docMissingParams(doc);

                // Set all field parameters
                for (int i = 0; i < params.size(); i++) {
                    Object value = params.get(i);
                    int paramIndex = i + 1;
                    if (value instanceof java.sql.Date) {
                        preparedStatement.setDate(paramIndex, (java.sql.Date) value);
                    } else if (value instanceof Integer) {
                        preparedStatement.setInt(paramIndex, (Integer) value);
                    } else {
                        preparedStatement.setString(paramIndex, value != null ? value.toString() : null);
                    }
                }

                // Set the WHERE clause parameter (pmid)
                preparedStatement.setString(params.size() + 1, doc.getPmid());

                preparedStatement.addBatch();
            }

            int[] numUpdates = preparedStatement.executeBatch();
            for (int i = 0; i < numUpdates.length; i++) {
                if (numUpdates[i] >= 0) {
                    totalUpdated += numUpdates[i];
                } else if (numUpdates[i] == -2) {
                    // Statement.SUCCESS_NO_INFO - assume 1 row updated
                    totalUpdated++;
                    System.out.println("Update execution " + i + ": unknown number of rows updated");
                }
            }
            connection.commit();
            System.out.println("Batch update completed. Total records updated: " + totalUpdated);
            return totalUpdated;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int updateMissingFields(SolrDoc solrDoc) throws Exception {
        SolrDocDB doc=buildSolrDocDB(solrDoc);
        String sql= "UPDATE SOLR_DOCS set" + getSolrDocMissingFields().stream().collect(Collectors.joining("=?"))+"=?, set last_update_date=NOW() " +
                " where pmid=?";
        return   updateSolrPostgress(sql,
                docMissingParams(doc),
                doc.getPmcId());


    }

    public SolrDocDB buildSolrDocDB(SolrDoc solrDoc) throws JsonProcessingException, ParseException {
        List<String> formattedValues=new ArrayList<>();
        Map<String, List<String>> docMap=mapper.readValue(gson.toJson(solrDoc), Map.class);
        Map<String, String> solrDocDBMap=new HashMap<>();
        java.sql.Date pDate=null;
        for(String key:docMap.keySet()){
            List<String> values=docMap.get(key);
            if(docMap.get(key)!=null) {
                if (key.equalsIgnoreCase("pDate")) {
                    String dateString=values.get(0);
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date=simpleDateFormat.parse(dateString);
                    pDate=new java.sql.Date(date.getTime());

                } else {
                    try {
                        if (values.size() > 1) {
                            String val = values.stream().collect(Collectors.joining(" | "));
                            solrDocDBMap.put(key, val);
                            formattedValues.add(val);
                        } else {
                            if(values.size()>0) {
                                formattedValues.add(String.valueOf(values.get(0)));
                                solrDocDBMap.put(key, String.valueOf(values.get(0)));
                            }else{
                                solrDocDBMap.put(key, null);
                            }
                        }
                    } catch (Exception e) {
                        solrDocDBMap.put(key, null);
                        e.printStackTrace();
                    }
                }
            }
        }
       SolrDocDB newSolrDocDB=mapper.readValue(gson.toJson(solrDocDBMap), SolrDocDB.class);
        newSolrDocDB.setpDate(pDate);
        return newSolrDocDB;
    }
//    public List<String> getSolrDocFields(){
//       List<String> fields= Arrays.asList(
//               "gene_count", "mp_id", "doi_s", "chebi_pos", "vt_id",
//                "bp_term", "chebi_term", "p_date", "xco_term", "chebi_count",
//               "rs_term", "mp_term", "rdo_id", "nbo_pos", "gene",
//                "rs_id", "so_term", "mp_count", "vt_count", "bp_id",
//                "rgd_obj_count", "vt_pos", "p_type", "nbo_count", "xco_id",
//                "p_year", "authors", "xco_count", "rdo_count", "title",
//                "nbo_term", "vt_term", "hp_pos", "nbo_id", "so_count",
//                "hp_term", "so_id", "rgd_obj_pos", "xco_pos", "rs_pos",
//               "hp_id", "rdo_pos", "rs_count", "rgd_obj_term", "abstract",
//                "pmid", "bp_count", "mp_pos", "hp_count", "xdb_id",
//                "rgd_obj_id", "bp_pos", "gene_pos", "so_pos", "rdo_term",
//                "chebi_id",
//               "j_date_s",
//               "citation",
//               "mesh_terms",
//               "keywords",
//               "chemicals",
//               "affiliation",
//               "issn",
//               "organism_common_name",
//               "organism_term",
//               "organism_ncbi_id",
//               "organism_count",
//               "organism_pos",
//               "pmc_id"
//        );
//       return fields;
//    }
    public List<String> getSolrDocFields(){
        return getSolrDBFields();
    }
    public List<String> getSolrDocMissingFields(){
        return Arrays.asList(  "cc_count",
                "cc_id",
                "cc_term",
                "cc_pos",
                "mf_count",
                "mf_id",
                "mf_pos",
                "mf_term",
                "mt_id",
                "mt_term",
                "mt_pos",
                "mt_count",
                "p_source",
                "zfa_id",
                "zfa_term",
                "zfa_count",
                "zfa_pos",
                "ma_id",
                "ma_count",
                "ma_pos",
                "ma_term",
                "mmo_id",
                "mmo_count",
                "mmo_pos",
                "mmo_term",
                "pw_id",
                "pw_count",
                "pw_pos",
                "pw_term",
                "cmo_id",
                "cmo_count",
                "cmo_pos",
                "cmo_term",
                "uberon_id",
                "uberon_count",
                "uberon_pos",
                "uberon_term",
                "cl_id",
                "cl_count",
                "cl_pos",
                "cl_term",
                "efo_id",
                "efo_count",
                "efo_term",
                "efo_pos");
    }

    public static List<String> getSolrDBFields() {
        return Arrays.asList(
                "gene_count",
                "mp_id",
                "doi_s",
                "chebi_pos",
                "vt_id",
                "bp_term",
                "chebi_term",
                "p_date",
                "xco_term",
                "chebi_count",
                "rs_term",
                "mp_term",
                "rdo_id",
                "nbo_pos",
                "gene",
                "rs_id",
                "so_term",
                "mp_count",
                "vt_count",
                "bp_id",
                "rgd_obj_count",
                "vt_pos",
                "p_type",
                "nbo_count",
                "xco_id",
                "p_year",
                "xco_count",
                "rdo_count",
                "title",
                "nbo_term",
                "vt_term",
                "hp_pos",
                "nbo_id",
                "so_count",
                "hp_term",
                "so_id",
                "rgd_obj_pos",
                "xco_pos",
                "rs_pos",
                "hp_id",
                "rdo_pos",
                "rs_count",
                "rgd_obj_term",
                "pmid",
                "bp_count",
                "mp_pos",
                "hp_count",
                "xdb_id",
                "rgd_obj_id",
                "bp_pos",
                "gene_pos",
                "so_pos",
                "rdo_term",
                "chebi_id",
                "solr_doc_id",
                "abstract",
                "authors",
                "j_date_s",
                "citation",
                "mesh_terms",
                "keywords",
                "chemicals",
                "affiliation",
                "issn",
                "organism_common_name",
                "organism_term",
                "organism_ncbi_id",
                "organism_count",
                "organism_pos",
                "pmc_id",
                "cc_count",
                "cc_id",
                "cc_term",
                "cc_pos",
                "mf_count",
                "mf_id",
                "mf_pos",
                "mf_term",
                "mt_id",
                "mt_term",
                "mt_pos",
                "mt_count",
                "p_source",
                "zfa_id",
                "zfa_term",
                "zfa_count",
                "zfa_pos",
                "ma_id",
                "ma_count",
                "ma_pos",
                "ma_term",
                "mmo_id",
                "mmo_count",
                "mmo_pos",
                "mmo_term",
                "pw_id",
                "pw_count",
                "pw_pos",
                "pw_term",
                "cmo_id",
                "cmo_count",
                "cmo_pos",
                "cmo_term",
                "uberon_id",
                "uberon_count",
                "uberon_pos",
                "uberon_term",
                "cl_id",
                "cl_count",
                "cl_pos",
                "cl_term",
                "efo_id",
                "efo_count",
                "efo_term",
                "efo_pos",
                "last_update_date"

        );
    }

    public List<SolrInputDocument> getSolrDocs() throws Exception {
        String sql="select * from solr_docs";
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return query.execute();
    }
    public List<SolrInputDocument> getSolrDocs(int year) throws Exception {
        String sql="select * from solr_docs where p_Year=?";
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return execute(query,year);
    }
    public List<SolrInputDocument> getSolrDocByPMID(String pmid) throws Exception {
        String sql="select * from solr_docs where pmid=?";
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return execute(query,pmid);
    }
    public List<SolrInputDocument> getSolrDocs(int year, int limit, int offset) throws Exception {
        String sql="select * from solr_docs where p_Year=? LIMIT ? OFFSET ? " ;
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return execute(query, year,limit, offset);
    }
    public List<SolrInputDocument> getSolrDocs(int year ,int limit) throws Exception {
        String sql="select * from solr_docs where p_year=?  limit "+ limit;
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return execute(query,year);
    }
    public List<SolrInputDocument> getLimitedSolrDocs(int limit) throws Exception {
        String sql="select * from solr_docs  limit "+ limit;
        SolrDocQuery query=new SolrDocQuery(this.getPostgressDataSource(), sql);
        return query.execute();
    }
    public String getJson(SolrDocDB doc) throws JsonProcessingException {
        //TODO
        return null;
    }
}
