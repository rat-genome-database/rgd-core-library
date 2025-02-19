package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinAssignee;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GeneBinAssigneeQuery  extends MappingSqlQuery {
    public GeneBinAssigneeQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GeneBinAssignee geneBinAssignee = new GeneBinAssignee();

        geneBinAssignee.setTermAcc(rs.getString("TERM_ACC"));
        geneBinAssignee.setTerm(rs.getString("TERM"));
        geneBinAssignee.setAssignee(rs.getString("ASSIGNEE"));
        geneBinAssignee.setCompleted(rs.getInt("COMPLETED"));
        geneBinAssignee.setTotalGenes(rs.getInt("TOTAL_GENES"));
        geneBinAssignee.setIsParent(rs.getInt("PARENT"));
        geneBinAssignee.setSubsetNum(rs.getInt("SUBSET_NUM"));
        return geneBinAssignee;
    }

    public static List<GeneBinAssignee> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneBinAssigneeQuery q = new GeneBinAssigneeQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }

}
