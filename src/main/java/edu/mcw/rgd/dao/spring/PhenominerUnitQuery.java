package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.PhenominerUnit;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kthorat
 * @since Oct 23 2020
 * Returns a row from PHENOMINER UNIT query
 */

public class PhenominerUnitQuery extends MappingSqlQuery{
    public PhenominerUnitQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PhenominerUnit pUnit = new PhenominerUnit();
        pUnit.setCmoId(rs.getString("clinical_measurement_ont_id"));
        pUnit.setpTerm(rs.getString("term"));
        pUnit.setMeasurementUnit(rs.getString("measurement_units"));
        pUnit.setExperimentId(rs.getInt("experiment_id"));
        pUnit.setExperimentRecordId(rs.getInt("experiment_record_id"));
        pUnit.setStudyId(rs.getInt("study_id"));
        pUnit.setpRefRgdId(rs.getInt("ref_rgd_id"));
        pUnit.setStdUnit(rs.getString("standard_unit"));
        return pUnit;
    }
}
