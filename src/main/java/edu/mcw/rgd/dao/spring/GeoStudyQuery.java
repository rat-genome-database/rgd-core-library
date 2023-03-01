package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Study;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GeoStudyQuery extends MappingSqlQuery {

    public GeoStudyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Study study = new Study();
        study.setName(rs.getString("study_title"));
        study.setSource("GEO");
        study.setId(rs.getInt("pubmed_id"));
        study.setGeoSeriesAcc(rs.getString("geo_accession_id"));

        return study;
    }

}
