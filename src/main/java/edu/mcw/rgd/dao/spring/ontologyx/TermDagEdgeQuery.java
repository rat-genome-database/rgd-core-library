package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.Relation;
import edu.mcw.rgd.datamodel.ontologyx.TermDagEdge;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/14/15
 * Time: 2:14 PM
 * <p>
 * to facilitate queries of ONT_DAG table
 */
public class TermDagEdgeQuery extends MappingSqlQuery {

    private int colParentTermName = -1; // column index for 'parent_term_name'
    private int colChildTermName = -1; // column index for 'child_term_name'
    private int colCreatedDate = -1; // column index for 'created_date'
    private int colLastModifiedDate = -1; // column index for 'last_modified_date'
    // Note: -1 = not initialized, 0 = column not available in result set

    public TermDagEdgeQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermDagEdge obj = new TermDagEdge();
        populateData(obj, rs);
        return obj;
    }

    protected void populateData(TermDagEdge obj, ResultSet rs) throws SQLException {
        obj.setParentTermAcc(rs.getString("parent_term_acc"));
        obj.setChildTermAcc(rs.getString("child_term_acc"));
        obj.setRel(Relation.getRelFromRelId(rs.getString("ont_rel_id")));

        // optional columns ("parent_term_name","child_term_name","created_date","last_modified_date")
        initOptionalColumns(rs);
        if( colParentTermName>0 )
            obj.setParentTermName(rs.getString(colParentTermName));
        if( colChildTermName>0 )
            obj.setChildTermName(rs.getString(colChildTermName));
        if( colCreatedDate>0 )
            obj.setCreatedDate(rs.getTimestamp(colCreatedDate));
        if( colLastModifiedDate>0 )
            obj.setLastModifiedDate(rs.getTimestamp(colLastModifiedDate));
    }

    void initOptionalColumns(ResultSet rs) {
        if( colParentTermName>=0 && colChildTermName>=0 && colCreatedDate>=0 && colLastModifiedDate>=0 ) {
            return;
        }

        try {
            colParentTermName = rs.findColumn("parent_term_name");
        } catch(Exception e) {
            colParentTermName = 0; // not available in result set
        }

        try {
            colChildTermName = rs.findColumn("child_term_name");
        } catch(Exception e) {
            colChildTermName = 0; // not available in result set
        }

        try {
            colCreatedDate = rs.findColumn("created_date");
        } catch(Exception e) {
            colCreatedDate = 0; // not available in result set
        }

        try {
            colLastModifiedDate = rs.findColumn("last_modified_date");
        } catch(Exception e) {
            colLastModifiedDate = 0; // not available in result set
        }
    }
}
