package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.solr.PubmedSolrDoc;
import edu.mcw.rgd.datamodel.solr.SolrDocDB;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class SolrDocQuery extends MappingSqlQuery<SolrInputDocument> {
    public SolrDocQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected SolrInputDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
        PubmedSolrDoc doc=new PubmedSolrDoc();

        for(String field:getFields()){
            if(!field.equalsIgnoreCase("P_DATE") && !field.equalsIgnoreCase("P_YEAR") && !field.equalsIgnoreCase("ABSTRACT")) {
                String value=rs.getString(field);
                if(value!=null && !value.equals("")) {
                    List<String> values = List.of(value.split("\\|")).stream().map(v -> v.trim()).collect(Collectors.toList());
                    doc.addField(field, new ArrayList<>(values));
                }
            } else {
                    if (field.equalsIgnoreCase("P_DATE") ) {
                        if(rs.getDate(field)!=null && !rs.getDate(field).toString().equals(""))
                        {
//                            DateFormat PUB_DATE_DF = new SimpleDateFormat("yyyy/MM/dd" );
//                            String dateStr = rs.getDate(field).toString().replace("-","/");
//
//                            Date jDate;
//                            if (dateStr != null) {
//                                try {
//                                    jDate = new Date(PUB_DATE_DF.parse(dateStr).getTime());
//                                } catch (Exception e) {
//                                    try {
//                                        jDate = new Date(PUB_DATE_DF.parse("1800/01/01").getTime());
//                                    } catch (ParseException ex) {
//                                        throw new RuntimeException(ex);
//                                    }
//                                }
//                            } else {
//                                try {
//                                    jDate = new Date(PUB_DATE_DF.parse("1800/01/01").getTime());
//                                } catch (ParseException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
                            Date date= rs.getDate("p_date");
                            String jDate=convertToSolrDate(date);
                            doc.addField(field, jDate);

                        }
                        }
                    if (field.equalsIgnoreCase("P_YEAR") ) {
                        if(rs.getInt(field)!=0)
                        doc.addField(field, rs.getInt(field));
                    }
                    if (field.equalsIgnoreCase("ABSTRACT") ) {
                        if(rs.getString(field)!=null && !rs.getString(field).equals("")) {
                            doc.addField(field, rs.getString(field));
                        }
                    }
                }

        }
        return doc;
    }


        public static String convertToSolrDate(Date sqlDate) {
            // Convert to java.util.Date
            Instant instant = sqlDate.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
            return DateTimeFormatter.ISO_INSTANT.format(instant);
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
