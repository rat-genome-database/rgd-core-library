package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.SSLP;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 */
public class SSLPQuery extends MappingSqlQuery {

    public SSLPQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        SSLP sslp = new SSLP();
        sslp.setKey(rs.getInt("sslp_key"));
        sslp.setName(rs.getString("rgd_name"));
        sslp.setExpectedSize(rs.getInt("expected_size"));
        sslp.setNotes(rs.getString("notes"));
        sslp.setRgdId(rs.getInt("rgd_id"));
        sslp.setSslpType(rs.getString("sslp_type"));
        sslp.setSpeciesTypeKey(rs.getInt("species_type_key"));

        return sslp;
    }

}
