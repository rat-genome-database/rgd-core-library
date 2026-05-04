package edu.mcw.rgd.dao.spring.gviewer;

import edu.mcw.rgd.dao.spring.MappingQuery;
import edu.mcw.rgd.datamodel.annotation.GViewerIndex;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GViewerIndexQuery extends MappingQuery<GViewerIndex> {
    public GViewerIndexQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected GViewerIndex mapRow(ResultSet rs, int rowNum) throws SQLException {
        GViewerIndex index=new GViewerIndex();
        index.setChromosome(rs.getString("chromosome"));
        index.setAnnotatedObjectRgdId(rs.getInt("rgd_id"));
        index.setStartPos(rs.getInt("start_pos"));
        index.setStopPos(rs.getInt("stop_pos"));
        index.setObjectSymbol(rs.getString("object_symbol"));
        index.setObjectType(rs.getString("object_type"));
        index.setTerm(rs.getString("term"));
        index.setTermAcc(rs.getString("term_acc"));

        return index;
    }
}
