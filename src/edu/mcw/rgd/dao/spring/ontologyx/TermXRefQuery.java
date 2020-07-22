package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.TermXRef;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since Apr 13, 2011
 * query to browse ONT_XREFS table
 */
public class TermXRefQuery extends MappingSqlQuery {

    public TermXRefQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermXRef obj = new TermXRef();
        obj.setKey(rs.getInt("xref_key"));
        obj.setTermAcc(rs.getString("term_acc"));
        obj.setXrefValue(rs.getString("xref_value"));
        obj.setXrefDescription(rs.getString("xref_description"));
        return obj;
    }
}
