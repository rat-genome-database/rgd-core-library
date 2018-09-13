package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.RGDUser;
import edu.mcw.rgd.datamodel.RGDUserList;
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
public class RGDUserListQuery extends MappingSqlQuery {

    public RGDUserListQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        RGDUserList lst = new RGDUserList();
        lst.setUserId(rs.getInt("user_id"));
        lst.setListId(rs.getInt("list_id"));
        lst.setObjectType(rs.getInt("object_type"));
        lst.setMapKey(rs.getInt("map_key"));
        lst.setListName(rs.getString("name"));
        lst.setCreatedDate(rs.getDate("created_date"));


        return lst;
    }

}
