package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.DocumentEmbeddingSummary;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentEmbeddingSummaryQuery extends MappingSqlQuery<DocumentEmbeddingSummary> {
    public DocumentEmbeddingSummaryQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected DocumentEmbeddingSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
        DocumentEmbeddingSummary summary = new DocumentEmbeddingSummary();
        summary.setFileName(rs.getString("file_name"));
        summary.setChunkCount(rs.getInt("chunk_count"));
        summary.setUploadedAt(rs.getTimestamp("uploaded_at"));
        return summary;
    }
}
