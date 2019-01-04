package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.skygen.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * @author jdepons
 * @since Jan 17, 2008
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
