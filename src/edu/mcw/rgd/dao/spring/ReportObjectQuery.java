package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ReportObjectDE;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps rows of the RAG database's {@code report_object} table — one row per RGD object
 * per species, carrying the identity shared by every chunk of that object's report.
 */
public class ReportObjectQuery extends MappingSqlQuery<ReportObjectDE> {

    public ReportObjectQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected ReportObjectDE mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReportObjectDE o = new ReportObjectDE();
        o.setRgdId(rs.getLong("rgd_id"));
        o.setObjectType(rs.getString("object_type"));
        o.setSymbol(rs.getString("symbol"));
        o.setName(rs.getString("name"));
        o.setSpecies(rs.getString("species"));
        o.setFileName(rs.getString("file_name"));
        return o;
    }
}
