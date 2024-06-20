package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Record;
import edu.mcw.rgd.datamodel.pheno.Sample;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 10/31/2018
 * QurRepresents a row from GENE_EXPRESSION_EXP_RECORD table.
 */
public class GeneExpressionFullRecordQuery extends MappingSqlQuery {

    public GeneExpressionFullRecordQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Record rec = new Record();
        rec.setId(rs.getInt("gene_expression_exp_record_id"));
        rec.setExperimentId(rs.getInt("experiment_id"));
        rec.setSampleId(rs.getInt("sample_id"));
        rec.setLastModifiedBy(rs.getString("last_modified_by"));
        rec.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        rec.setCurationStatus(rs.getInt("curation_status"));
        rec.setClinicalMeasurementId(rs.getInt("CLINICAL_MEASUREMENT_ID"));
        rec.setStudyId(rs.getInt("study_id"));
        rec.setRefRgdId(rs.getInt("ref_rgd_id"));
        Sample s = new Sample();
        s.setId(rs.getInt("sample_id"));
        s.setAgeDaysFromHighBound(rs.getInt("age_days_from_dob_high_bound"));
        if (rs.wasNull()) {
            s.setAgeDaysFromHighBound(null);
        }
        s.setAgeDaysFromLowBound(rs.getInt("age_days_from_dob_low_bound"));
        if (rs.wasNull()) {
            s.setAgeDaysFromLowBound(null);
        }
        s.setLifeStage(rs.getString("LIFE_STAGE"));
        if (rs.wasNull()) {
            s.setLifeStage(null);
        }
        s.setNumberOfAnimals(rs.getInt("number_of_animals"));
        if (rs.wasNull()) {
            s.setNumberOfAnimals(null);
        }
        s.setNotes(rs.getString("sample_notes"));
        s.setSex(rs.getString("sex"));
        s.setBioSampleId(rs.getString("biosample_id"));

        String strainOntId = rs.getString("strain_ont_id");
        s.setStrainAccId(strainOntId);
        s.setCultureDur(rs.getInt("CULTURE_DUR_VALUE"));
        s.setCultureDurUnit(rs.getString("CULTURE_DUR_UNIT"));

        rec.setSample(s);
        return rec;
    }
}
