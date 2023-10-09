package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Project;
import org.springframework.jdbc.object.MappingSqlQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectQuery extends MappingSqlQuery {
    public ProjectQuery(DataSource ds, String query) {
        super(ds, query);
    }
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Project proj = new Project();
        proj.setRgdId(rs.getInt("rgd_id"));
        proj.setName(rs.getString("project_name"));
        proj.setDesc(rs.getString("project_desc"));
        proj.setSubmitterName(rs.getString("submitter_name"));
        proj.setPiName(rs.getString("PRINCIPAL_INVESTIGATOR"));
        return proj;
    }

    public static List<Project> execute(AbstractDAO dao, String sql, Object... params) throws  Exception {
        ProjectQuery q = new ProjectQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
