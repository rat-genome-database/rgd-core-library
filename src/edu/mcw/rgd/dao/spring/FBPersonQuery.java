package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.FBPerson;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FBPersonQuery extends MappingSqlQuery {
    public FBPersonQuery(DataSource ds, String query) {
        super(ds, query);
    }
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        FBPerson fp = new FBPerson();
        fp.setPersonId(rs.getInt("PERSON_ID"));
        fp.setFirstName(rs.getString("FIRSTNAME"));
        fp.setLastName(rs.getString("LASTNAME"));
        fp.setEmail(rs.getString("EMAIL_ADDRESS"));
        fp.setPhoneNumber(rs.getInt("PHONE_NUMBER"));
        fp.setInstitute(rs.getString("INSTITUTE"));
        fp.setAddress(rs.getString("ADDRESS"));
        fp.setCity(rs.getString("CITY"));
        fp.setState(rs.getString("STATE"));
        fp.setZipCode(rs.getInt("ZIP_CODE"));
        fp.setCountry(rs.getString("COUNTRY"));
        return fp;
    }
    public static List<FBPerson> execute(AbstractDAO dao, DataSource src, String sql, Object... params) throws Exception {
        FBPersonQuery q = new FBPersonQuery(src, sql);
        return dao.execute(q, params);
    }
}
