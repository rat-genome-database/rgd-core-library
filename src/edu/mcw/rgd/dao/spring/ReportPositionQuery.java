package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ReportPositionDE;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps rows of the RAG database's {@code report_position} table — one row per
 * (object, assembly), so an object keeps a single identity while carrying the
 * position it has on every assembly.
 */
public class ReportPositionQuery extends MappingSqlQuery<ReportPositionDE> {

    public ReportPositionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected ReportPositionDE mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReportPositionDE p = new ReportPositionDE();
        p.setRgdId(rs.getLong("rgd_id"));
        p.setAssembly(rs.getString("assembly"));
        p.setChromosome(rs.getString("chromosome"));
        p.setStartPos(readCoordinate(rs, "start_pos"));
        p.setStopPos(readCoordinate(rs, "stop_pos"));
        return p;
    }

    /**
     * Read a coordinate column, preserving SQL NULL as a Java null.
     *
     * <p>The columns are {@code bigint} while {@link ReportPositionDE} holds them as
     * {@link Integer}, so this narrows. That is safe for the values actually stored —
     * the largest rat chromosome is well under 300 Mbp, far inside int range — but
     * {@code getInt} alone would also turn a NULL coordinate into 0, which would read
     * as "position 0" rather than "unknown". Reading as long and checking
     * {@code wasNull} keeps the distinction.</p>
     */
    private Integer readCoordinate(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : (int) value;
    }
}
