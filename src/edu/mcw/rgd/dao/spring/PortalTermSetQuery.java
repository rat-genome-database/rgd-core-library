package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PortalTermSet;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/27/11
 * Time: 12:44 PM
 * helper class to simplify querying of PORTAL_TERMSET1 table
 */
public class PortalTermSetQuery extends MappingSqlQuery {

    public PortalTermSetQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PortalTermSet p = new PortalTermSet();
        p.setPortalTermSetId(rs.getInt("portal_termset_id"));
        p.setPortalKey(rs.getInt("portal_key"));
        p.setParentTermSetId(rs.getInt("parent_termset_id"));
        p.setTermAcc(rs.getString("term_acc"));
        p.setOntTermName(rs.getString("ont_term_name"));
        return p;
    }
}
