package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ExpressionData;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 2/22/12
 * Time: 9:31 AM
 * <p>
 *     mapping sql query to work with lists of ExpressionData objects
 * </p>
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
       obj.setAbsCall(rs.getString("abs_call"));
       obj.setGeoAccId(rs.getString("geo_acc_id"));
       obj.setNotes(rs.getString("notes"));
       return obj;
   }

}
