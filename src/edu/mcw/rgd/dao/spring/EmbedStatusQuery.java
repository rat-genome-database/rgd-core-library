package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.EmbedStatus;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmbedStatusQuery extends MappingSqlQuery<EmbedStatus> {

    public EmbedStatusQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected EmbedStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmbedStatus e = new EmbedStatus();
        e.setId(rs.getInt("id"));
        e.setFilePath(rs.getString("file_path"));
        e.setFileName(rs.getString("file_name"));
        e.setStatus(rs.getString("status"));
        e.setErrorMessage(rs.getString("error_message"));
        e.setChunkCount(rs.getInt("chunk_count"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        return e;
    }
}
