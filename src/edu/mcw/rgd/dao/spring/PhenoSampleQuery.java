package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Sample;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Returns a row from SAMPLE table (DEV schema)
 */
public class PhenoSampleQuery extends MappingSqlQuery {

    public PhenoSampleQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Sample s = new Sample();

        s.setId(rs.getInt("sample_id"));
        s.setAgeDaysFromHighBound(rs.getInt("age_days_from_dob_high_bound"));
        s.setAgeDaysFromLowBound(rs.getInt("age_days_from_dob_low_bound"));
        s.setNumberOfAnimals(rs.getInt("number_of_animals"));
        s.setNotes(rs.getString("sample_notes"));
        s.setSex(rs.getString("sex"));
        s.setStrainAccId(rs.getString("strain_ont_id"));
        s.setTissueAccId(rs.getString("tissue_ont_id"));
        s.setCellTypeAccId(rs.getString("cell_type_ont_id"));
        s.setSubcellComponentAccId(rs.getString("subcell_component_ont_id"));
        s.setGeoSampleAcc(rs.getString("geo_sample_acc"));
        s.setBioSampleId(rs.getString("biosample_id"));

        return s;
    }

}