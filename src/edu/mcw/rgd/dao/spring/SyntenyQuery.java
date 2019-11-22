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
        sr.setMapKey1(rs.getInt("map_key1"));
        sr.setMapKey2(rs.getInt("map_key2"));
        sr.setChromosome1(rs.getString("chromosome1"));
        sr.setChromosome2(rs.getString("chromosome2"));
        sr.setStartPos1(rs.getInt("start_pos1"));
        sr.setStartPos2(rs.getInt("start_pos2"));
        sr.setStopPos1(rs.getInt("stop_pos1"));
        sr.setStopPos2(rs.getInt("stop_pos2"));
        sr.setOrientation(rs.getInt("orientation"));

        return sr;
    }

}
