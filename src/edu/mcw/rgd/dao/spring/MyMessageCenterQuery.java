package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MessageCenterMessage;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.io.BufferedReader;
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
