package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.GeneBin.GeneBin;
import edu.mcw.rgd.datamodel.GenomeSignal;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenomeSignalQuery extends MappingSqlQuery {

    public GenomeSignalQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GenomeSignal gs = new GenomeSignal(rs.getString("chr"),
                rs.getInt("signal_value"),
                rs.getInt("position"),
                rs.getInt("set_id"));

        return gs;
    }

    public static List<GenomeSignal> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GenomeSignalQuery q = new GenomeSignalQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
