package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.*;
import edu.mcw.rgd.datamodel.pheno.Record;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 */

/**
 * Returns a row from EXPERIMENT_RECORD table
 */
public class RecordQuery extends MappingSqlQuery {

    public RecordQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Record rec = new Record();
        rec.setId(rs.getInt("experiment_record_id"));
        rec.setClinicalMeasurementId(rs.getInt("clinical_measurement_id"));
        rec.setExperimentId(rs.getInt("experiment_id"));
        rec.setMeasurementMethodId(rs.getInt("measurement_method_id"));
        rec.setSampleId(rs.getInt("sample_id"));
        rec.setMeasurementSD(rs.getString("measurement_sd"));
        rec.setMeasurementSem(rs.getString("measurement_sem"));
        rec.setMeasurementUnits(rs.getString("measurement_units"));
        rec.setMeasurementValue(rs.getString("measurement_value"));
        rec.setMeasurementError(rs.getString("measurement_error"));
        rec.setStudyId(rs.getInt("study_id"));
        rec.setHasIndividualRecord(rs.getBoolean("has_individual_record"));
        rec.setCurationStatus(rs.getInt("curation_status"));
        try {
            rec.setRefRgdId(rs.getInt("ref_rgd_id"));
        }catch (Exception e){
            
        }
        rec.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        try {
            rec.setStudyName(rs.getString("study_name"));
            rec.setExperimentName(rs.getString("experiment_name"));
            rec.setExperimentNotes(rs.getString("experiment_notes"));
            rec.setCreatedBy(rs.getString("created_by"));
            rec.setLastModifiedBy(rs.getString("last_modified_by"));
        }catch (Exception e) {
            //e.printStackTrace();
        }

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

        MeasurementMethod mm =  new MeasurementMethod();
        mm.setId(rs.getInt("measurement_method_id"));
        mm.setDuration(rs.getString("measurement_duration_in_secs"));
        mm.setNotes(rs.getString("measurement_method_notes"));
        mm.setAccId(rs.getString("measurement_method_ont_id"));
        mm.setSite(rs.getString("measurement_site"));
        mm.setPiType(rs.getString("measurement_method_pi_type"));
        if (rs.getString("meas_method_pi_time_value") != null) {
            mm.setPiTimeValue(rs.getInt("meas_method_pi_time_value"));
        }

        mm.setSiteOntIds(rs.getString("measurement_site_ont_ids"));
        mm.setPiTypeUnit(rs.getString("meas_method_pi_time_unit"));
        rec.setMeasurementMethod(mm);

        ClinicalMeasurement cm =  new ClinicalMeasurement();
        cm.setId(rs.getInt("clinical_measurement_id"));
        cm.setNotes(rs.getString("clinical_measurement_notes"));
        cm.setAccId(rs.getString("clinical_measurement_ont_id"));
        cm.setSiteOntIds(rs.getString("CLINICAL_MEAS_SITE_ONT_ID"));
        cm.setSite(rs.getString("CLINICAL_MEASUREMENT_SITE"));
        cm.setFormula(rs.getString("formula"));
        cm.setAverageType(rs.getString("clinical_meas_average_type"));
        cm.setSiteOntIds(rs.getString("CLINICAL_MEAS_SITE_ONT_ID"));
        cm.setSite(rs.getString("CLINICAL_MEASUREMENT_SITE"));

        rec.setClinicalMeasurement(cm);

        rec.setTraitId(rs.getString("trait_ont_id"));
        return rec;
    }

    public double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public String round(String value, int numberOfDigitsAfterDecimalPoint) {
        try {
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                    BigDecimal.ROUND_HALF_UP);
            return bigDecimal.doubleValue() + "";
        }catch (Exception e) {
            return "";
        }
    }
}
