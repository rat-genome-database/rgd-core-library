package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.SyntenicRegion;
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
public class SyntenyQuery extends MappingSqlQuery {

    public SyntenyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        SyntenicRegion sr = new SyntenicRegion();
        sr.setBackboneChromosome(rs.getString("backbone_chr"));
        sr.setBackboneMapKey(rs.getInt("backbone_map_key"));
        sr.setBackboneStart(rs.getLong("backbone_start_pos"));
        sr.setBackboneStop(rs.getLong("backbone_stop_pos"));

        sr.setChromosome(rs.getString("chr"));
        sr.setMapKey(rs.getInt("map_key"));
        sr.setStart(rs.getLong("start_pos"));
        sr.setStop(rs.getLong("stop_pos"));

        return sr;
    }

}
