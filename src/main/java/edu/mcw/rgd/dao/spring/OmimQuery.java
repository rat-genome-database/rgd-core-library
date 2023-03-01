package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Omim;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mtutaj
 * @since Apr 30, 2019
 */
public class OmimQuery extends MappingSqlQuery  {

    public OmimQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Omim o = new Omim();

        o.setMimNumber(rs.getString("mim_number"));
        o.setStatus(rs.getString("status"));
        o.setMimType(rs.getString("mim_type"));
        o.setPhenotype(rs.getString("phenotype"));
        o.setCreatedDate(rs.getTimestamp("created_date"));
        o.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return o;
    }

    public static List<Omim> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        OmimQuery q = new OmimQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
