package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PathwayRef;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 31, 2011
 * Time: 4:26:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayRefQuery extends MappingSqlQuery {
    public PathwayRefQuery(DataSource ds, String query){
        super(ds, query);
    }



    protected Object mapRow(ResultSet resultSet, int i) throws SQLException {

        PathwayRef pr = new PathwayRef();

        pr.setPwId(resultSet.getString("term_acc"));
        pr.setRefKey(resultSet.getString("ref_rgd_key"));

        return pr;
    }
}

