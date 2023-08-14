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

}
