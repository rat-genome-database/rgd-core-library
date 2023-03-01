package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ExpressionData;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 2/22/12
 * class to work with lists of ExpressionData objects
 */
public class ExpressionDataQuery extends MappingSqlQuery {

   public ExpressionDataQuery(DataSource ds, String query) {
       super(ds, query);
   }

   protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

       ExpressionData obj = new ExpressionData();

       obj.setKey(rs.getLong("expression_data_key"));
       obj.setRgdId(rs.getInt("rgd_id"));
       obj.setTissue(rs.getString("tissue"));
       obj.setTranscripts(rs.getString("transcripts"));
       obj.setChipSeqReadDensity(rs.getDouble("chip_seq_read_density"));
       obj.setExperimentMethods(rs.getString("experiment_methods"));
       obj.setRegulation(rs.getString("regulation"));
       obj.setTissueTermAcc(rs.getString("tissue_term_acc"));
       obj.setStrainTermAcc(rs.getString("strain_term_acc"));
       obj.setSource(rs.getString("source"));
       obj.setNotes(rs.getString("notes"));
       return obj;
   }

}
