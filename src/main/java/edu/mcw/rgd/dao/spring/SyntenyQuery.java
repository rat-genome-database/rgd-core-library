package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.SyntenicRegion;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jdepons
 * Date: Jan 17, 2008
 *
 * Returns a row from SYNTENY_USCS or SYNTENY_UCSC_GAPS table
 */
public class SyntenyQuery extends MappingSqlQuery {

    public SyntenyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        SyntenicRegion sr = new SyntenicRegion();
        sr.setBackboneChromosome(rs.getString("chromosome1"));
        sr.setBackboneMapKey(rs.getInt("map_key1"));
        sr.setBackboneStart(rs.getInt("start_pos1"));
        sr.setBackboneStop(rs.getInt("stop_pos1"));

        sr.setChromosome(rs.getString("chromosome2"));
        sr.setMapKey(rs.getInt("map_key2"));
        sr.setStart(rs.getInt("start_pos2"));
        sr.setStop(rs.getInt("stop_pos2"));

        sr.setOrientation(rs.getString("strand"));
        sr.setChainLevel(rs.getInt("chain_level"));
        sr.setChainType(rs.getString("chain_type"));
        return sr;
    }
}
