package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GeneExpression;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecord;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecordValue;
import edu.mcw.rgd.datamodel.pheno.Sample;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneExpressionQuery extends MappingSqlQuery {

    public GeneExpressionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeneExpression ge = new GeneExpression();
        GeneExpressionRecord rec = new GeneExpressionRecord();
        rec.setId(rs.getInt("gene_expression_exp_record_id"));
        rec.setExperimentId(rs.getInt("experiment_id"));
        rec.setSampleId(rs.getInt("sample_id"));
        rec.setLastModifiedBy(rs.getString("last_modified_by"));
        rec.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        rec.setCurationStatus(rs.getInt("curation_status"));
        rec.setSpeciesTypeKey(rs.getInt("species_type_key"));
        rec.setClinicalMeasurementId(rs.getInt("CLINICAL_MEASUREMENT_ID"));
        ge.setGeneExpressionRecord(rec);

        GeneExpressionRecordValue recV = new GeneExpressionRecordValue();
        recV.setId(rs.getInt("gene_expression_value_id"));
        recV.setExpressedObjectRgdId(rs.getInt("expressed_object_rgd_id"));
        recV.setExpressionMeasurementAccId(rs.getString("expression_measurement_ont_id"));
        recV.setNotes(rs.getString("expression_value_notes"));
        recV.setGeneExpressionRecordId(rs.getInt("gene_expression_exp_record_id"));
        recV.setExpressionValue(rs.getDouble("expression_value"));
        recV.setExpressionUnit(rs.getString("expression_unit"));
        recV.setMapKey(rs.getInt("map_key"));
        recV.setExpressionLevel(rs.getString("expression_level"));

        recV.setTpmValue(rs.getDouble("tpm_value"));
        if( rs.wasNull() ) {
            recV.setTpmValue(null);
        }
        ge.setGeneExpressionRecordValue(recV);

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
        s.setCellLineId(rs.getString("cell_line_id"));
        s.setGeoSampleAcc(rs.getString("geo_sample_acc"));
        s.setBioSampleId(rs.getString("biosample_id"));
        s.setLifeStage(rs.getString("life_stage"));
        s.setLastModifiedBy(rs.getString("last_modified_by"));
        s.setCreatedBy(rs.getString("created_by"));
        s.setCuratorNotes(rs.getString("CURATOR_NOTES"));
        s.setCultureDur(rs.getInt("CULTURE_DUR_VALUE"));
        s.setCultureDurUnit(rs.getString("CULTURE_DUR_UNIT"));
        ge.setSample(s);

        ge.setRefRgdId(rs.getInt("REF_RGD_ID"));
        try {
            ge.setGeoSeriesAcc(rs.getString("GEO_SERIES_ACC"));
        }catch (Exception ignored){}
        return ge;
    }
}

