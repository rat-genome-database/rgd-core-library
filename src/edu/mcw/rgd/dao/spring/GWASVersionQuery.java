package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.GWASCatalog;
import edu.mcw.rgd.datamodel.GWASVersion;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GWASVersionQuery extends MappingSqlQuery {

    public GWASVersionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        GWASVersion gv = new GWASVersion();
        gv.setGwasId(rs.getInt("GWAS_ID"));
        gv.setVersion(rs.getString("VERSION"));
        return gv;
    }
    public static List<GWASVersion> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GWASVersionQuery q = new GWASVersionQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
