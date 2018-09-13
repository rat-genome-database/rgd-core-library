package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.CytoBand;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Returns a row from MAPS_CYTOBANDS table
 */
public class CytoBandQuery extends MappingSqlQuery {

    public CytoBandQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        CytoBand obj = new CytoBand();
        obj.setMapKey(rs.getInt("map_key"));
        obj.setChromosome(rs.getString("chromosome"));
        obj.setBandName(rs.getString("band_name"));
        obj.setGiemsaStain(rs.getString("giemsa_stain"));
        obj.setStartPos(rs.getInt("start_pos"));
        obj.setStopPos(rs.getInt("stop_pos"));
        return obj;
    }

}