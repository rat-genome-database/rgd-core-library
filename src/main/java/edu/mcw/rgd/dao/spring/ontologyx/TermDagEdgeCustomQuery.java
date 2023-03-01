package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.TermDagEdgeCustom;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/14/15
 * Time: 2:20 PM
 * <p>
 * to facilitate queries of ONT_DAG_CUSTOM table
 */
public class TermDagEdgeCustomQuery extends TermDagEdgeQuery {

    public TermDagEdgeCustomQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermDagEdgeCustom obj = new TermDagEdgeCustom();
        populateData(obj, rs);
        obj.setOperation(rs.getString("operation"));
        return obj;
    }
}
