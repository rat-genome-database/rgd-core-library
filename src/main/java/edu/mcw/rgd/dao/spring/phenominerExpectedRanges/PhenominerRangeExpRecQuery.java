package edu.mcw.rgd.dao.spring.phenominerExpectedRanges;

import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerRangeExperimentRec;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 6/22/2018.
 */
public class PhenominerRangeExpRecQuery extends MappingSqlQuery {
    public PhenominerRangeExpRecQuery(DataSource ds, String query){super((javax.sql.DataSource) ds, query);}
    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhenominerRangeExperimentRec rec= new PhenominerRangeExperimentRec();
        rec.setExpected_range_id(rs.getInt("expected_range_id"));
        rec.setExperiment_record_id(rs.getInt("experiment_record_id"));
        return rec;
    }
}
