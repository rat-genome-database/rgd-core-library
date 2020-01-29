package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.CellLine;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 2/22/12
 * mapping sql query to work with lists of CellLine objects
 */
public class CellLineQuery extends GenomicElementQuery {

   public CellLineQuery(DataSource ds, String query) {
       super(ds, query);
   }

   protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

       CellLine obj = new CellLine();

       // read data from genomic elements table
       mapRowForGenomicElement(obj, rs);

       // read data from cell lines table
       obj.setAvailability(rs.getString("availability"));
       obj.setCharacteristics(rs.getString("characteristics"));
       obj.setGender(rs.getString("gender"));
       obj.setGermlineCompetent(rs.getString("germline_competent"));
       obj.setOrigin(rs.getString("origin"));
       obj.setPhenotype(rs.getString("phenotype"));
       obj.setResearchUse(rs.getString("research_use"));

       try {
           obj.setSrcPipeline(rs.getString("src_pipeline"));
       } catch( Exception ignore ) {}

       return obj;
   }

}
