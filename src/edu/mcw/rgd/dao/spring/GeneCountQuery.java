package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author jdepons
 * @since Jan 17, 2008
 */
public class GeneCountQuery extends MappingSqlQuery {

    public GeneCountQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        HashMap hm = new HashMap();
        hm.put(rs.getString("term_acc"), rs.getInt("tcount"));
        return hm;
    }
}
