package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.myrgd.MyList;
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
public class MyListQuery extends MappingSqlQuery {

    public MyListQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MyList myList = new MyList();
        myList.setId(rs.getInt("list_id"));
        myList.setName(rs.getString("list_name"));
        myList.setObjectType(rs.getInt("obj_type"));
        myList.setUsername(rs.getString("username"));
        myList.setLink(rs.getString("link"));
        myList.setDesc(rs.getString("description"));
        return myList;
    }



}
