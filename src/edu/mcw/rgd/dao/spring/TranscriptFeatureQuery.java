package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MapData;
import edu.mcw.rgd.datamodel.TranscriptFeature;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Jun 25, 2010
 * Time: 1:22:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TranscriptFeatureQuery extends MapDataQuery {

    public TranscriptFeatureQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TranscriptFeature tr = new TranscriptFeature((MapData) super.mapRow(rs, rowNum));

        tr.setTranscriptRgdId(rs.getInt("transcript_rgd_id"));
        tr.setFeatureType(TranscriptFeature.getFeatureType(rs.getInt("object_key")));
        return tr;
    }
}
