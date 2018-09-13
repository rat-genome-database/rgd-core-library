package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * <p>
 * Utility class to return a single integer value from a sql query.
 * <b>Usage:</b>
 * <pre>
 * String query = "SELECT count(*) from study";
 * CountQuery q = new CountQuery(this.getDataSource(), query);
 * q.compile();
 * int countValue = q.getCount();
 * </pre>
 * </p>
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
