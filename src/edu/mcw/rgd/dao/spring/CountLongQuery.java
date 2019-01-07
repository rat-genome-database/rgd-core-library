package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 6/13/12
 * Utility class to return a single long integer value from a sql query.
 */
public class CountLongQuery extends MappingSqlQuery {

    public CountLongQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong(1);
    }

    /**
     * convenience wrapper to return a long integer from a parameter-less query
     * @return value of the count query
     * @throws Exception
     */
    public long getCount() throws Exception {
        return (Long) execute().get(0);
    }

    /**
     * convenience wrapper to return a long integer from a parameterized query
     * @return value of the count query
     * @throws Exception
     */
    public long getCount(Object[] params) throws Exception {
        return (Long) execute(params).get(0);
    }
}
