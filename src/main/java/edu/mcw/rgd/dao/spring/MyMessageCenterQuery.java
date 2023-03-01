package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MessageCenterMessage;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
 */
public class MyMessageCenterQuery extends MappingSqlQuery {

    public MyMessageCenterQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {


        MessageCenterMessage mcm = new MessageCenterMessage();
        mcm.setId(rs.getInt("message_id"));
        mcm.setUsername(rs.getString("username"));
        mcm.setTitle(rs.getString("title"));

        Clob c = rs.getClob("message");

        StringBuffer str = new StringBuffer();
        String strng;
        BufferedReader bufferRead = new BufferedReader(c.getCharacterStream());
        try {

            while ((strng = bufferRead.readLine()) != null)
                str.append(strng);
        }catch(Exception e) {
            throw new SQLException("couldnt read clob");
        }

        mcm.setMessage(str.toString());
        mcm.setCreatedDate(rs.getTimestamp("created_date"));


        return mcm;
    }

}
