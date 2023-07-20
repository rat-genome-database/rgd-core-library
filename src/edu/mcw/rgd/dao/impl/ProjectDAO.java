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
}
