package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.skygen.*;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Created by IntelliJ IDEA. <br/>
 * User: jdepons <br/>
 * Date: Jan 17, 2008 <br/>
 * Time: 10:08:19 AM
 * <p>
 *     mapping sql query to work with lists of Gene objects
 * </p>
 */
public class PatientQuery extends MappingSqlQuery {

    public PatientQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Patient p = new Patient();
        p.setMrn(rs.getString("mrn"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setDateOfBirth(rs.getDate("dob"));
        p.setId(rs.getInt("patient_id"));

        return p;

    }

}
