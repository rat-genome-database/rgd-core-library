package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MessageCenterMessage;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
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

        mcm.setMessage("Called Lite: Msg not included");
        mcm.setCreatedDate(rs.getDate("created_date"));

        return mcm;
    }

}
