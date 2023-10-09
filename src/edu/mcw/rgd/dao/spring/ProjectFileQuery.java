package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Project;
import edu.mcw.rgd.datamodel.ProjectFile;
import org.springframework.jdbc.object.MappingSqlQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectFileQuery extends MappingSqlQuery{
    public ProjectFileQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException{
        ProjectFile projFile = new ProjectFile();
        projFile.setFileKey(rs.getInt("file_key"));
        projFile.setRgdid(rs.getInt("rgd_id"));
        projFile.setProjectFileType(rs.getString("project_file_type"));
        projFile.setDownloadUrl(rs.getString("download_url"));
        projFile.setProtocol(rs.getString("protocol"));
        projFile.setProtocolName(rs.getString("protocol_name"));
        return projFile;
    }
    public static List<ProjectFile> execute(AbstractDAO dao, String sql, Object... params) throws  Exception {
        ProjectFileQuery q = new ProjectFileQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
