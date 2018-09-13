package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MessageCenterMessage;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. <br/>
 * User: jdepons <br/>
 * Date: Jan 17, 2008 <br/>
 * Time: 10:08:19 AM
 * <p>
 *     mapping sql query to work with lists of Gene objects
 * </p>
 */
public class MyMessageCenterLiteQuery extends MappingSqlQuery {

    public MyMessageCenterLiteQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {


        MessageCenterMessage mcm = new MessageCenterMessage();
        mcm.setId(rs.getInt("message_id"));
        mcm.setUsername(rs.getString("username"));
        mcm.setTitle(rs.getString("title"));

        //Clob c = rs.getClob("msg");

        mcm.setMessage("Called Lite: Msg not included");
        mcm.setCreatedDate(rs.getDate("created_date"));


        return mcm;
    }

}
