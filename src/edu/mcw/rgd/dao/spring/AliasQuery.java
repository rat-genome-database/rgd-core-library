package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Alias;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * Returns a row from the Alias table
 */
public class AliasQuery extends MappingSqlQuery {

    public AliasQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Alias alias = new Alias();
        alias.setKey(rs.getInt("alias_key"));
        alias.setNotes(rs.getString("notes"));
        alias.setRgdId(rs.getInt("rgd_id"));
        alias.setTypeName(rs.getString("alias_type_name_lc"));
        alias.setValue(rs.getString("alias_value"));

        try {
            alias.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored) {
        }
        return alias;
    }

    public static List<Alias> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        AliasQuery q = new AliasQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
