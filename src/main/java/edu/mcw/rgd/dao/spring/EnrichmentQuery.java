package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.annotation.Enrichment;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the Alias table
 */
public class EnrichmentQuery extends MappingSqlQuery {

    public EnrichmentQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Enrichment e = new Enrichment();
        e.setAspect(rs.getString("aspect"));
        e.setEvidence(rs.getString("evidence"));
        e.setObjectId(rs.getInt("annotated_object_rgd_id"));
        e.setObjectSymbol(rs.getString("object_symbol"));
        e.setRoot(rs.getString("root"));
        e.setTerm(rs.getString("term"));
        e.setTerm_acc(rs.getString("term_acc"));
        e.setRoot_acc(rs.getString("root_acc"));
        return e;
    }

}