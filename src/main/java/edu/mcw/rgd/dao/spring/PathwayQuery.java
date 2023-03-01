package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Pathway;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 28, 2011
 * Time: 4:31:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayQuery extends MappingSqlQuery {
    public PathwayQuery(DataSource ds, String query){
        super(ds, query);
    }



    protected Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Pathway pw = new Pathway();
        pw.setId(resultSet.getString("term_acc"));
        pw.setName(resultSet.getString("pathway_name"));
        pw.setDescription(resultSet.getString("pathway_desc"));
        pw.setHasAlteredPath(resultSet.getString("has_altered_pathway"));

        return pw;
    }
}
