package edu.mcw.rgd.dao.spring;


import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.GWASCatalog;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionValueCount;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GeneExpressionValueCountsQuery extends MappingSqlQuery {

    public GeneExpressionValueCountsQuery(DataSource ds, String query) {
        super(ds, query);
    }
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        GeneExpressionValueCount vc = new GeneExpressionValueCount();
        vc.setValueCnt(rs.getInt("VALUE_COUNT"));
        vc.setExpressedRgdId(rs.getInt("EXPRESSED_OBJECT_RGD_ID"));
        vc.setTermAcc(rs.getString("TERM_ACC"));
        vc.setUnit(rs.getString("EXPRESSION_UNIT"));
        vc.setLevel(rs.getString("EXPRESSION_LEVEL"));
        vc.setLastModifiedDate(rs.getDate("LAST_MODIFIED_DATE"));
        return vc;
    }

    public static List<GeneExpressionValueCount> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneExpressionValueCountsQuery q = new GeneExpressionValueCountsQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
