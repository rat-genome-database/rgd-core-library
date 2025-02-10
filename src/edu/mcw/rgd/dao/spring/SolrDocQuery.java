package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.solr.PubmedSolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDocDB;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SolrDocQuery extends MappingSqlQuery<SolrInputDocument> {
    public SolrDocQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected SolrInputDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
        SolrInputDocument doc=new SolrInputDocument();
//        doc.setGeneCount(	rs.getString("GENE_COUNT"));
//        doc.setMpId(	rs.getString("MP_ID"));
//        doc.setDoiS(	rs.getString("DOI_S"));
//        doc.setChebiPos(	rs.getString("CHEBI_POS"));
//        doc.setVtId(	rs.getString("VT_ID"));
//        doc.setBpTerm(	rs.getString("BP_TERM"));
//        doc.setChebiTerm(	rs.getString("CHEBI_TERM"));
//        doc.setpDate(	rs.getDate("P_DATE"));
//        doc.setXcoTerm(	rs.getString("XCO_TERM"));
//        doc.setChebiCount(	rs.getString("CHEBI_COUNT"));
//        doc.setRsTerm(	rs.getString("RS_TERM"));
//        doc.setMpTerm(	rs.getString("MP_TERM"));
//        doc.setRdoId(	rs.getString("RDO_ID"));
//        doc.setNboPos(	rs.getString("NBO_POS"));
//        doc.setGene(	rs.getString("GENE"));
//        doc.setRsId(	rs.getString("RS_ID"));
//        doc.setSoTerm(	rs.getString("SO_TERM"));
//        doc.setMpCount(	rs.getString("MP_COUNT"));
//        doc.setVtCount(	rs.getString("VT_COUNT"));
//        doc.setBpId(	rs.getString("BP_ID"));
//        doc.setRgdObjCount(	rs.getString("RGD_OBJ_COUNT"));
//        doc.setVtPos(	rs.getString("VT_POS"));
//        doc.setpType(	rs.getString("P_TYPE"));
//        doc.setNboCount(	rs.getString("NBO_COUNT"));
//        doc.setXcoId(	rs.getString("XCO_ID"));
//        doc.setpYear(	rs.getInt("P_YEAR"));
//        doc.setAuthors(	rs.getString("AUTHORS"));
//        doc.setXcoCount(	rs.getString("XCO_COUNT"));
//        doc.setRdoCount(	rs.getString("RDO_COUNT"));
//        doc.setTitle(	rs.getString("TITLE"));
//        doc.setNboTerm(	rs.getString("NBO_TERM"));
//        doc.setVtTerm(	rs.getString("VT_TERM"));
//        doc.setHpPos(	rs.getString("HP_POS"));
//        doc.setNboId(	rs.getString("NBO_ID"));
//        doc.setSoCount(	rs.getString("SO_COUNT"));
//        doc.setHpTerm(	rs.getString("HP_TERM"));
//        doc.setSoId(	rs.getString("SO_ID"));
//        doc.setRgdObjPos(	rs.getString("RGD_OBJ_POS"));
//        doc.setXcoPos(	rs.getString("XCO_POS"));
//        doc.setRsPos(	rs.getString("RS_POS"));
//        doc.setHpId(	rs.getString("HP_ID"));
//        doc.setRdoPos(	rs.getString("RDO_POS"));
//        doc.setRsCount(rs.getString("RS_COUNT"));
//        doc.setRgdObjTerm(	rs.getString("RGD_OBJ_TERM"));
//        doc.set_abstract(	rs.getString("ABSTRACT"));
//        doc.setPmid(	rs.getString("PMID"));
//        doc.setBpCount(	rs.getString("BP_COUNT"));
//        doc.setMpPos(	rs.getString("MP_POS"));
//        doc.setMpCount(	rs.getString("HP_COUNT"));
//        doc.setXdbId(	rs.getString("XDB_ID"));
//        doc.setRgdObjId(	rs.getString("RGD_OBJ_ID"));
//        doc.setBpPos(	rs.getString("BP_POS"));
//        doc.setGenePos(	rs.getString("GENE_POS"));
//        doc.setSoPos(	rs.getString("SO_POS"));
//        doc.setRdoTerm(	rs.getString("RDO_TERM"));
//        doc.setChebiId(	rs.getString("CHEBI_ID"));
//
//        doc.setjDates(rs.getString("j_date_s"));
//        doc.setMesh_terms(rs.getString("mesh_terms"));
//        doc.setCitation(rs.getString("citation"));
//        doc.setKeywords(rs.getString("keywords"));
//        doc.setChemicals(rs.getString("chemicals"));
//        doc.setAffiliation(rs.getString("affiliation"));
//        doc.setIssn(rs.getString("issn"));
//        doc.setOrganismCommonName(rs.getString("organism_common_name"));
//        doc.setOrganismCount(rs.getString("organism_count"));
//        doc.setOrganismTerm(rs.getString("organism_term"));
//        doc.setOrganismNcbiId(rs.getString("organism_ncbi_id"));
//        doc.setOrganismPos(rs.getString("organism_pos"));

        for(String field:getFields()){
            if(rs.getString(field)!=null) {
                if(!field.equalsIgnoreCase("P_DATE") && !field.equalsIgnoreCase("P_YEAR")) {
                    String value=rs.getString(field);

                    doc.addField(field, Arrays.stream( value.split("\\|")).collect(Collectors.toList()));
                }
                else {
                    if (field.equalsIgnoreCase("P_DATE"))
                        doc.addField(field, rs.getDate(field));
                    if (field.equalsIgnoreCase("P_YEAR"))
                        doc.addField(field, rs.getInt(field));
                }
            }
        }
        return doc;
    }
    public List<String> getFields(){
        return Arrays.asList(
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
                /*******************************/
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
                "authors",
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
                "abstract",
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
                "chebi_id"
        );

    }
}
