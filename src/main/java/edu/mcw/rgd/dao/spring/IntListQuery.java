package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * <p>
 * Returns a list of integers
 */
public class IntListQuery extends MappingSqlQuery {

    public IntListQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(1);
    }

    public static List<Integer> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        IntListQuery q = new IntListQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}