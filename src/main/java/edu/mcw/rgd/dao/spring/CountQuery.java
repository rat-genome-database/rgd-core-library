package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * Utility class to return a single integer value from a sql query.
 */
public class CountQuery extends MappingSqlQuery {

    public CountQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(1);
    }

    /**
     * convenience wrapper to return an integer from a parameter-less query
     * @return value of the count query
     * @throws Exception
     */
    public int getCount() throws Exception {
        return (Integer) execute().get(0);
    }

    /**
     * convenience wrapper to return an integer from a parameterized query
     * @return value of the count query
     * @throws Exception
     */
    public int getCount(Object[] params) throws Exception {
        return (Integer) execute(params).get(0);
    }
}
