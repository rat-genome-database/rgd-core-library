package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.ResultSet;

import edu.mcw.rgd.datamodel.MapData;

/**
 * @author jdepons
 * @since Apr 7, 2009
 */
public class MapDataQuery extends MappingSqlQuery {

    public MapDataQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MapData md = new MapData();

        md.setKey(rs.getInt("maps_data_key"));
        md.setFOrP(rs.getString("f_or_p"));
        md.setChromosome(rs.getString("chromosome"));
        md.setFishBand(rs.getString("fish_band"));
        md.setAbsPosition(rs.getDouble("abs_position"));
        if (rs.wasNull()) md.setAbsPosition(null);
        md.setLod(rs.getDouble("lod"));
        if (rs.wasNull()) md.setLod(null);
        md.setNotes(rs.getString("notes"));
        md.setMapKey(rs.getInt("map_key"));
        md.setRgdId(rs.getInt("rgd_id"));
        md.setRgdIdUp(rs.getInt("rgd_id_up"));
        if (rs.wasNull()) md.setRgdIdUp(null);
        md.setRgdIdDown(rs.getInt("rgd_id_dw"));
        if (rs.wasNull()) md.setRgdIdDown(null);
        md.setStartPos(rs.getInt("start_pos"));
        md.setStopPos(rs.getInt("stop_pos"));
        md.setMultipleChromosome(rs.getString("multiple_chromosome"));
        md.setStrand(rs.getString("strand"));
        md.setMapsDataPositionMethodId(rs.getInt("maps_data_position_method_id"));
        if (rs.wasNull()) md.setMapsDataPositionMethodId(null);
        md.setSrcPipeline(rs.getString("src_pipeline"));

        return md;
    }
}
