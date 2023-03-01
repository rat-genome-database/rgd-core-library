package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.RGDUser;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the Alias table
 */
public class RGDUserQuery extends MappingSqlQuery {

    public RGDUserQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        RGDUser user = new RGDUser();
        user.setUserId(rs.getInt("user_id"));
        return user;
    }

}
