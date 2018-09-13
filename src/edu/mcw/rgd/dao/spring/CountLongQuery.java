package edu.mcw.rgd.dao.spring;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 6/13/12
 * Time: 2:13 PM
 */
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Utility class to return a single integer value from a sql query.
 * <b>Usage:</b>
 * <pre>
 * String query = "SELECT count(*) from study";
 * CountLongQuery q = new CountLongQuery(this.getDataSource(), query);
 * q.compile();
 * long countValue = q.getCount();
 * </pre>
 * </p>
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
