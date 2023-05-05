package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.GeneBin.GeneBin;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinCountGenes;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GeneBinCountGenesQuery extends MappingSqlQuery {
    public GeneBinCountGenesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GeneBinCountGenes geneBinCountGenes = new GeneBinCountGenes();

        geneBinCountGenes.setTermAcc(rs.getString("TERM_ACC"));
        geneBinCountGenes.setTotalGenes(rs.getInt("TOTAL_GENES"));
        return geneBinCountGenes;
    }
    public static List<GeneBinCountGenes> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneBinCountGenesQuery q = new GeneBinCountGenesQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
