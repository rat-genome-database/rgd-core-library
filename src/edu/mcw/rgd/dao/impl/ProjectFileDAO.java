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

public class ProjectFileDAO extends AbstractDAO{

    public List<ProjectFile>getProjectFiles(int projectID) throws Exception{
        String query="select * from project_files where rgd_id=?";
        return ProjectFileQuery.execute(this,query,projectID);
    }
    public int insert(ProjectFile projectFile) throws Exception {
        String query = "INSERT INTO PROJECT_FILES (FILE_KEY, RGD_ID, PROJECT_FILE_TYPE, DOWNLOAD_URL, PROTOCOL, PROTOCOL_NAME) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return update(query, projectFile.getFileKey(), projectFile.getRgdid(), projectFile.getProjectFileType(), projectFile.getDownloadUrl(), projectFile.getProtocol(), projectFile.getProtocolName());
    }

    public int update(ProjectFile projectFile) throws Exception {
        String query = "UPDATE PROJECT_FILES SET PROJECT_FILE_TYPE=?, DOWNLOAD_URL=?, PROTOCOL=?, PROTOCOL_NAME=? WHERE FILE_KEY=?";
        return update(query, projectFile.getProjectFileType(), projectFile.getDownloadUrl(), projectFile.getProtocol(), projectFile.getProtocolName(), projectFile.getFileKey());
    }

    public int delete(int fileKey) throws Exception {
        String query = "DELETE FROM PROJECT_FILES WHERE FILE_KEY=?";
        return update(query, fileKey);
    }
    public ProjectFile getProjectFile(int fileKey) throws Exception {
        String query = "SELECT * FROM PROJECT_FILES WHERE FILE_KEY=?";
        List<ProjectFile> result = ProjectFileQuery.execute(this, query, fileKey);
        System.out.println(result.get(0));
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
