package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * return a List of MapPair<Integer,String> objects
 */
public class IntStringMapQuery extends MappingSqlQuery {

    public IntStringMapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MapPair(rs.getInt(1), rs.getString(2));
    }

    public class MapPair {
        public int keyValue;
        public String stringValue;

        public MapPair(int key, String value) {
            keyValue = key;
            stringValue = value;
        }
    }

    public static List<MapPair> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        IntStringMapQuery q = new IntStringMapQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
