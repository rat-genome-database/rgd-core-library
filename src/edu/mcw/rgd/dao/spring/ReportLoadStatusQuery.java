package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ReportLoadStatus;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportLoadStatusQuery extends MappingSqlQuery<ReportLoadStatus> {

    public ReportLoadStatusQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected ReportLoadStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReportLoadStatus r = new ReportLoadStatus();
        r.setReportLoadStatusId(rs.getInt("report_load_status_id"));
        r.setRgdId(rs.getInt("rgd_id"));
        r.setReportType(rs.getString("report_type"));
        r.setSpeciesKey(rs.getInt("species_key"));
        r.setMapKey(rs.getInt("map_key"));
        r.setSymbol(rs.getString("symbol"));
        r.setStatus(rs.getString("status"));
        r.setFileName(rs.getString("file_name"));
        r.setErrorMessage(rs.getString("error_message"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setUpdatedAt(rs.getTimestamp("updated_at"));
        return r;
    }
}
