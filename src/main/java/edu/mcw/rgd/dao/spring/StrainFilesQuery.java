package edu.mcw.rgd.dao.spring;


import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.StrainFiles;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StrainFilesQuery extends MappingSqlQuery {

    public StrainFilesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        StrainFiles strainFile = new StrainFiles();
        strainFile.setStrainId(rs.getInt("STRAIN_ID"));
        strainFile.setFileType(rs.getString("FILE_TYPE"));
        strainFile.setFileData(rs.getBlob("FILE_DATA"));
        strainFile.setContentType(rs.getString("CONTENT_TYPE"));
        strainFile.setFileName(rs.getString("FILE_NAME"));
        strainFile.setLastModifiedDate(rs.getDate("LAST_MODIFIED_DATE"));
        strainFile.setModifiedBy(rs.getString("MODIFIED_BY"));

        return strainFile;
    }

    public static List<StrainFiles> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        StrainFilesQuery q = new StrainFilesQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}

