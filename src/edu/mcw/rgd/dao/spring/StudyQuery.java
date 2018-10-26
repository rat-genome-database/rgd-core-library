package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Study;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * Returns a row from STUDY table
 */
public class StudyQuery extends MappingSqlQuery {

    public StudyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Study study = new Study();
        study.setId(rs.getInt("study_id"));
        study.setName(rs.getString("study_name"));
        study.setSource(rs.getString("study_source"));
        study.setType(rs.getString("study_type"));
        study.setRefRgdId(rs.getInt("ref_rgd_id"));
        study.setDataType(rs.getString("data_type"));
        study.setGeoSeriesAcc(rs.getString("geo_series_acc"));

        return study;
    }

}
