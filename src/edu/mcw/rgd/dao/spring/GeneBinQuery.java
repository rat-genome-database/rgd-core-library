package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.GeneBin.GeneBin;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GeneBinQuery  extends MappingSqlQuery {

    public GeneBinQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GeneBin geneBin = new GeneBin();

        geneBin.setRgdId(rs.getInt("RGD_ID"));
        geneBin.setGeneSymbol(rs.getString("GENE_SYMBOL"));
        geneBin.setTermAcc(rs.getString("TERM_ACC"));
        geneBin.setTerm(rs.getString("TERM"));
        return geneBin;
    }

    public static List<GeneBin> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneBinQuery q = new GeneBinQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
