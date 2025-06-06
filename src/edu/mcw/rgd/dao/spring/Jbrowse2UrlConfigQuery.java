package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Jbrowse2UrlConfig;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Jbrowse2UrlConfigQuery extends MappingSqlQuery {
    public Jbrowse2UrlConfigQuery(DataSource ds, String query) {
        super(ds, query);
    }
    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Jbrowse2UrlConfig urlConfig = new Jbrowse2UrlConfig();
        urlConfig.setConfigKey(rs.getInt("config_key"));
        urlConfig.setMapKey(rs.getInt("map_key"));
        urlConfig.setObjectKey(rs.getInt("object_key"));
        urlConfig.setAssembly(rs.getString("assembly"));
        urlConfig.setTracks(rs.getString("tracks"));
        urlConfig.setChrPrefix(rs.getString("chr_prefix"));
        return urlConfig;
    }

    public static List<Jbrowse2UrlConfig> execute(AbstractDAO dao, String sql, Object... params) throws  Exception{
        Jbrowse2UrlConfigQuery q = new Jbrowse2UrlConfigQuery(dao.getDataSource(), sql);
        return dao.execute(q,params);
    }
}
