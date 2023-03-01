package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 3/5/12
 * Time: 2:46 PM
 * <p>
 * return a List of MapPair objects
 */
public class StringMapQuery extends MappingSqlQuery {

    public StringMapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MapPair(rs.getString(1), rs.getString(2));
    }

    public class MapPair {
        public String keyValue;
        public String stringValue;

        public MapPair(String key, String value) {
            keyValue = key;
            stringValue = value;
        }
    }

    public static List<MapPair> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        StringMapQuery q = new StringMapQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
