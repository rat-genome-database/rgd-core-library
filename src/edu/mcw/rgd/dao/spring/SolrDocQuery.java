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

public class SolrDocQuery extends MappingSqlQuery<PubmedSolrDoc> {
    public SolrDocQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected PubmedSolrDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
        PubmedSolrDoc doc=new PubmedSolrDoc();

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
