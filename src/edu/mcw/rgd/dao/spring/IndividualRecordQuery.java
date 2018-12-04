package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.IndividualRecord;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author WLiu
 * @since 10/14/11
 */
public class IndividualRecordQuery extends MappingSqlQuery  {

    public IndividualRecordQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        IndividualRecord irec = new IndividualRecord();

        irec.setId(rs.getInt("experiment_record_ind_id"));
        irec.setRecordId(rs.getInt("experiment_record_id"));
        irec.setAnimalId(rs.getString("animal_id"));
        irec.setMeasurementValue(rs.getString("measurement_value"));

        return irec;
    }
}
