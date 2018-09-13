package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.HgvsName;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 3/12/14
 * <p>
 *     mapping sql query to work with lists of HgvsName objects
 * </p>
 */
public class HgvsNameQuery extends MappingSqlQuery {

   public HgvsNameQuery(DataSource ds, String query) {
       super(ds, query);
   }

   protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

       HgvsName obj = new HgvsName();

       // read data from cell lines table
       obj.setRgdId(rs.getInt("rgd_id"));
       obj.setType(rs.getString("hgvs_name_type"));
       obj.setName(rs.getString("hgvs_name"));

       return obj;
   }

}
