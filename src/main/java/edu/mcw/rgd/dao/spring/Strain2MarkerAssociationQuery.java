package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Strain2MarkerAssociation;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/21/15
 * Time: 12:33 PM
 */
public class Strain2MarkerAssociationQuery extends AssociationQuery {

    public Strain2MarkerAssociationQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Strain2MarkerAssociation assoc = new Strain2MarkerAssociation();
        mapRow(rs, assoc);
        assoc.setAssocType2(rs.getString("assoc_type"));
        assoc.setSrcPipeline2(rs.getString("src_pipeline"));
        return assoc;
    }
}
