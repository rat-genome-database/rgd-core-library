package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.models.SubmittedStrainAvailabiltiy;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 8/12/2016.
 */
public class SubmittedStrainAvailabilityQuery extends MappingSqlQuery {

    public SubmittedStrainAvailabilityQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected SubmittedStrainAvailabiltiy mapRow(ResultSet rs, int i) throws SQLException {

        SubmittedStrainAvailabiltiy sa= new SubmittedStrainAvailabiltiy();
        sa.setAvailabilityType(rs.getString("availability_type"));
        sa.setSubmittedStrainKey(rs.getInt("submitted_strain_key"));
        return sa;
    }
}
