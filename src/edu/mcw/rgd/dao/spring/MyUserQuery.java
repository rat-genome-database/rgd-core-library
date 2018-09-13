package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.myrgd.MyUser;
import edu.mcw.rgd.datamodel.skygen.Patient;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
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
public class MyUserQuery extends MappingSqlQuery {

    public MyUserQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MyUser mu = new MyUser();
        mu.setUsername(rs.getString("username"));
        mu.setSendDigest(rs.getInt("email_digest"));
        mu.setEnabled(rs.getInt("enabled"));
        return mu;

    }

}
