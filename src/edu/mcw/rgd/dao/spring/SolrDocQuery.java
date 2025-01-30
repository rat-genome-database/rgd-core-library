package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.solr.SolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDocDB;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SolrDocQuery extends MappingSqlQuery<SolrDocDB> {
    public SolrDocQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected SolrDocDB mapRow(ResultSet rs, int rowNum) throws SQLException {
        SolrDocDB doc=new SolrDocDB();
        doc.setGeneCount(	rs.getString("GENE_COUNT"));
        doc.setMpId(	rs.getString("MP_ID"));
        doc.setDoiS(	rs.getString("DOI_S"));
        doc.setChebiPos(	rs.getString("CHEBI_POS"));
        doc.setVtId(	rs.getString("VT_ID"));
        doc.setBpTerm(	rs.getString("BP_TERM"));
        doc.setChebiTerm(	rs.getString("CHEBI_TERM"));
        doc.setpDate(	rs.getDate("P_DATE"));
        doc.setXcoTerm(	rs.getString("XCO_TERM"));
        doc.setChebiCount(	rs.getString("CHEBI_COUNT"));
        doc.setRsTerm(	rs.getString("RS_TERM"));
        doc.setMpTerm(	rs.getString("MP_TERM"));
        doc.setRdoId(	rs.getString("RDO_ID"));
        doc.setNboPos(	rs.getString("NBO_POS"));
        doc.setGene(	rs.getString("GENE"));
        doc.setRsId(	rs.getString("RS_ID"));
        doc.setSoTerm(	rs.getString("SO_TERM"));
        doc.setMpCount(	rs.getString("MP_COUNT"));
        doc.setVtCount(	rs.getString("VT_COUNT"));
        doc.setBpId(	rs.getString("BP_ID"));
        doc.setRgdObjCount(	rs.getString("RGD_OBJ_COUNT"));
        doc.setVtPos(	rs.getString("VT_POS"));
        doc.setpType(	rs.getString("P_TYPE"));
        doc.setNboCount(	rs.getString("NBO_COUNT"));
        doc.setXcoId(	rs.getString("XCO_ID"));
        doc.setpYear(	rs.getInt("P_YEAR"));
        doc.setAuthors(	rs.getString("AUTHORS"));
        doc.setXcoCount(	rs.getString("XCO_COUNT"));
        doc.setRdoCount(	rs.getString("RDO_COUNT"));
        doc.setTitle(	rs.getString("TITLE"));
        doc.setNboTerm(	rs.getString("NBO_TERM"));
        doc.setVtTerm(	rs.getString("VT_TERM"));
        doc.setHpPos(	rs.getString("HP_POS"));
        doc.setNboId(	rs.getString("NBO_ID"));
        doc.setSoCount(	rs.getString("SO_COUNT"));
        doc.setHpTerm(	rs.getString("HP_TERM"));
        doc.setSoId(	rs.getString("SO_ID"));
        doc.setRgdObjPos(	rs.getString("RGD_OBJ_POS"));
        doc.setXcoPos(	rs.getString("XCO_POS"));
        doc.setRsPos(	rs.getString("RS_POS"));
        doc.setHpId(	rs.getString("HP_ID"));
        doc.setRdoPos(	rs.getString("RDO_POS"));
        doc.setRsCount(rs.getString("RS_COUNT"));
        doc.setRgdObjTerm(	rs.getString("RGD_OBJ_TERM"));
        doc.set_abstract(	rs.getString("ABSTRACT"));
        doc.setPmid(	rs.getString("PMID"));
        doc.setBpCount(	rs.getString("BP_COUNT"));
        doc.setMpPos(	rs.getString("MP_POS"));
        doc.setMpCount(	rs.getString("HP_COUNT"));
        doc.setXdbId(	rs.getString("XDB_ID"));
        doc.setRgdObjId(	rs.getString("RGD_OBJ_ID"));
        doc.setBpPos(	rs.getString("BP_POS"));
        doc.setGenePos(	rs.getString("GENE_POS"));
        doc.setSoPos(	rs.getString("SO_POS"));
        doc.setRdoTerm(	rs.getString("RDO_TERM"));
        doc.setChebiId(	rs.getString("CHEBI_ID"));



        return doc;
    }
}
