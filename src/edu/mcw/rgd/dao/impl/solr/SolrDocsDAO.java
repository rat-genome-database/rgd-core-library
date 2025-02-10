package edu.mcw.rgd.dao.impl.solr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.mcw.rgd.dao.AbstractDAO;

import edu.mcw.rgd.dao.spring.SolrDocQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.solr.PubmedSolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDocDB;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONObject;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SolrDocsDAO extends AbstractDAO {
    ObjectMapper mapper=new ObjectMapper();
    Gson gson=new Gson();
    public int addBatch(List<SolrDoc> solrDocs, Set<String> pmidsChunked) throws Exception {
        String fields="SOLR_DOC_ID, "+getSolrDocFields().stream().collect(Collectors.joining(", "))+", last_update_date";
        String sql= "INSERT INTO SOLR_DOCS ("+ fields+") VALUES (" +
                "NEXTVAL('SOLR_DOC_SEQ'), " +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?, NOW()" +
                ")";
        try(Connection connection=this.getPostgressConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            connection.setAutoCommit(false);
            int chunkedDataCount=0;
            boolean flag=false;
            for(SolrDoc solrDoc:solrDocs) {
                SolrDocDB doc = buildSolrDocDB(solrDoc);
               // if(!exists(doc.getPmid())) {
                preparedStatement.setString(1, doc.getGeneCount());
                preparedStatement.setString(2, doc.getMpId());
                preparedStatement.setString(3, doc.getDoiS());
                preparedStatement.setString(4, doc.getChebiPos());
                preparedStatement.setString(5, doc.getVtId());
                preparedStatement.setString(6, doc.getBpTerm());
                preparedStatement.setString(7, doc.getChebiTerm());
                preparedStatement.setDate(8, doc.getpDate());
                preparedStatement.setString(9, doc.getXcoTerm());
                preparedStatement.setString(10, doc.getChebiCount());
                preparedStatement.setString(11, doc.getRsTerm());
                preparedStatement.setString(12, doc.getMpTerm());
                preparedStatement.setString(13, doc.getRdoId());
                preparedStatement.setString(14, doc.getNboPos());
                preparedStatement.setString(15, doc.getGene());
                preparedStatement.setString(16, doc.getRsId());
                preparedStatement.setString(17, doc.getSoTerm());
                preparedStatement.setString(18, doc.getMpCount());
                preparedStatement.setString(19, doc.getVtCount());
                    preparedStatement.setString(20, doc.getBpId());
                    preparedStatement.setString(21, doc.getRgdObjCount());
                    preparedStatement.setString(22, doc.getVtPos());
                    preparedStatement.setString(23, doc.getpType());
                    preparedStatement.setString(24, doc.getNboCount());
                    preparedStatement.setString(25, doc.getXcoId());
                    preparedStatement.setInt(26, doc.getpYear());
                    preparedStatement.setString(27, doc.getAuthors());
                    preparedStatement.setString(28, doc.getXcoCount());
                    preparedStatement.setString(29, doc.getRdoCount());
                    preparedStatement.setString(30, doc.getTitle());
                    preparedStatement.setString(31, doc.getNboTerm());
                    preparedStatement.setString(32, doc.getVtTerm());
                    preparedStatement.setString(33, doc.getHpPos());
                    preparedStatement.setString(34, doc.getNboId());
                    preparedStatement.setString(35, doc.getSoCount());
                    preparedStatement.setString(36, doc.getHpTerm());
                    preparedStatement.setString(37, doc.getSoId());
                    preparedStatement.setString(38, doc.getRgdObjPos());
                    preparedStatement.setString(39, doc.getXcoPos());
                    preparedStatement.setString(40, doc.getRsPos());
                    preparedStatement.setString(41, doc.getHpId());
                    preparedStatement.setString(42, doc.getRdoPos());
                    preparedStatement.setString(43, doc.getRsCount());
                    preparedStatement.setString(44, doc.getRgdObjTerm());
                    preparedStatement.setString(45, doc.get_abstract());
                    preparedStatement.setString(46, doc.getPmid());
                    preparedStatement.setString(47, doc.getBpCount());
                    preparedStatement.setString(48, doc.getMpPos());
                    preparedStatement.setString(49, doc.getHpCount());
                    preparedStatement.setString(50, doc.getXdbId());
                    preparedStatement.setString(51, doc.getRgdObjId());
                    preparedStatement.setString(52, doc.getBpPos());

                    preparedStatement.setString(53, doc.getGenePos());

                    preparedStatement.setString(54, doc.getSoPos());
                    preparedStatement.setString(55, doc.getRdoTerm());

                    preparedStatement.setString(56, doc.getChebiId());
                    //####################################################################
                preparedStatement.setString(57,doc.getjDates());
                preparedStatement.setString(58, doc.getCitation());
                preparedStatement.setString(59,doc.getMeshTerms());
                preparedStatement.setString(60,doc.getKeywords());
                preparedStatement.setString(61,doc.getChemicals());
                preparedStatement.setString(62, doc.getAffiliation());
                preparedStatement.setString(63,doc.getIssn());
                preparedStatement.setString(64,doc.getOrganismCommonName());
                preparedStatement.setString(65,doc.getOrganismTerm());
                preparedStatement.setString(66,doc.getOrganismNcbiId());
                preparedStatement.setString(67,doc.getOrganismCount());
                preparedStatement.setString(68,doc.getOrganismPos());
                preparedStatement.setString(69,doc.getPmcId());


                    if(flag){
                        chunkedDataCount++;
                        pmidsChunked.add(doc.getPmid());
                    }
                    preparedStatement.addBatch();
               // }
            }
            int [] numUpdates=preparedStatement.executeBatch();
            for(int i=0; i<numUpdates.length;i++){
                if(numUpdates[i]==-2)
                System.out.println("Execution "+ i+ ": unknown number of rows updated");
//                else
                  //  System.out.println("Execution " + i+ " successful: "+ numUpdates[i]+" rows updated");
            }
            connection.commit();
            return chunkedDataCount;
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    public boolean exists(String pmid) throws Exception {
        String sql="select pmid from solr_docs where pmid=?";
        StringListQuery query=new StringListQuery(this.getPostgressDataSource(), sql);
        List<String> pmids=execute(query, pmid);

        return pmids.size()>0;
    }
    public int insert(SolrDoc solrDoc) throws Exception {

        String fields="SOLR_DOC_ID, "+getSolrDocFields().stream().collect(Collectors.joining(", "))+", last_update_date";
        SolrDocDB doc=buildSolrDocDB(solrDoc);
        String sql= "INSERT INTO SOLR_DOCS ("+ fields+") VALUES (" +
                "NEXTVAL('SOLR_DOC_SEQ'), " +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?," +
                "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?,"+ "?, NOW()" +
                ")";


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
                doc.getChebiId(), doc.getjDates(), doc.getCitation(), doc.getMeshTerms(), doc.getKeywords(),
                doc.getChemicals(), doc.getAffiliation(), doc.getIssn(), doc.getOrganismCommonName(), doc.getOrganismTerm(),
               doc.getOrganismNcbiId(), doc.getOrganismCount(), doc.getOrganismPos(), doc.getPmcId());

    }
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
    public List<String> getSolrDocFields(){
       List<String> fields= Arrays.asList(
               "gene_count", "mp_id", "doi_s", "chebi_pos", "vt_id",
                "bp_term", "chebi_term", "p_date", "xco_term", "chebi_count",
               "rs_term", "mp_term", "rdo_id", "nbo_pos", "gene",
                "rs_id", "so_term", "mp_count", "vt_count", "bp_id",
                "rgd_obj_count", "vt_pos", "p_type", "nbo_count", "xco_id",
                "p_year", "authors", "xco_count", "rdo_count", "title",
                "nbo_term", "vt_term", "hp_pos", "nbo_id", "so_count",
                "hp_term", "so_id", "rgd_obj_pos", "xco_pos", "rs_pos",
               "hp_id", "rdo_pos", "rs_count", "rgd_obj_term", "abstract",
                "pmid", "bp_count", "mp_pos", "hp_count", "xdb_id",
                "rgd_obj_id", "bp_pos", "gene_pos", "so_pos", "rdo_term",
                "chebi_id",
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
               "pmc_id"
        );
       return fields;
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
    public List<SolrInputDocument> getSolrDocs(int year ,int limit) throws Exception {
        String sql="select * from solr_docs where p_Year=?  limit "+ limit;
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
