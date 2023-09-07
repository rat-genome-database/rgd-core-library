package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.mapping.MapManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ProjectDAO extends AbstractDAO{

//    public List<Project> executeProjectQuery(ProjectDAO projectDAO, String query) throws Exception{
//        return ProjectQuery.execute(this,query);
//    }
    public List<Project> getAllProjects() throws Exception {

        String query = "SELECT * FROM PROJECTS";
        return ProjectQuery.execute(this,query);
    }

    public List<Integer> getReferenceRgdIdsForProject(int projectRgdId) throws Exception {
        String query = "SELECT RGD_ID " +
                "FROM references " +
                "WHERE ref_key IN (" +
                "    SELECT ref_key " +
                "    FROM rgd_ref_rgd_id " +
                "    WHERE rgd_id = ?" +
                ")";
        return IntListQuery.execute(this, query, projectRgdId);
    }


    public List<Project> getProjectByRgdId(int id) throws Exception{
        String query = "SELECT * from PROJECTS WHERE RGD_ID=?";
        return ProjectQuery.execute(this,query,id);
    }

    public Project getProjectByRgdId1(int id1) throws Exception{
        String query = "SELECT * from PROJECTS WHERE RGD_ID=?";
        List<Project> pro = ProjectQuery.execute(this,query, id1);
        return pro.isEmpty() ? null : (Project)pro.get(0);

    }

    public Project getProject(int rgdId) throws Exception {
        Project pro = this.getProjectByRgdId1(rgdId);
        if (pro == null) {
            throw new Exception("Project " + rgdId + " not found");
        } else {
            return pro;
        }
    }

    public int insertProject(Project project) throws Exception {
        String sql = "INSERT INTO projects (rgd_id, project_name, project_desc) " +
                "VALUES (?, ?, ?)";
        return update(sql, project.getRgdId(), project.getName(), project.getDesc());
    }
    public int updateProject(Project project) throws Exception {
        String sql = "UPDATE projects SET project_name=?, project_desc=? WHERE rgd_id=?";
        return update(sql, project.getName(), project.getDesc(),project.getRgdId());
    }
//    private int upsertProject(Project project, String sql) throws Exception {
//        return update(sql, project.getRgdId(), project.getName(), project.getDesc());
//    }
}
