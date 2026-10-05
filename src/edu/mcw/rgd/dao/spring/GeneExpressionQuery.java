package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GeneExpression;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecord;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecordValue;
import edu.mcw.rgd.datamodel.pheno.Sample;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneExpressionQuery extends MappingQuery<edu.mcw.rgd.datamodel.GeneExpression> {

    public GeneExpressionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected GeneExpression mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeneExpression ge = new GeneExpression();

/* ======================
   GeneExpressionRecord
   ====================== */
        GeneExpressionRecord rec = new GeneExpressionRecord();
        rec.setId(getIntOrZero(rs, "gene_expression_exp_record_id"));
        rec.setExperimentId(getIntOrZero(rs, "experiment_id"));
        rec.setSampleId(getIntOrZero(rs, "sample_id"));
        rec.setLastModifiedBy(getString(rs, "last_modified_by"));
       rec.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        rec.setCurationStatus(getIntOrZero(rs, "curation_status"));
        rec.setSpeciesTypeKey(getIntOrZero(rs, "species_type_key"));
        rec.setClinicalMeasurementId(getIntOrZero(rs, "CLINICAL_MEASUREMENT_ID"));
        rec.setTraitTerm(getString(rs, "trait_term"));
        rec.setMeasurementTerm(getString(rs, "measurement"));
        rec.setExperimentCondition(getString(rs, "condition"));
        rec.setConditionAccId(getString(rs, "condition_acc"));

        ge.setGeneExpressionRecord(rec);

/* ======================
   GeneExpressionRecordValue
   ====================== */
        GeneExpressionRecordValue recV = new GeneExpressionRecordValue();
        recV.setId(getIntOrZero(rs, "gene_expression_value_id"));
        recV.setExpressedGeneSymbol(getString(rs, "gene_symbol"));
        recV.setExpressedObjectRgdId(getIntOrZero(rs, "expressed_object_rgd_id"));
        recV.setExpressionMeasurementAccId(getString(rs, "expression_measurement_ont_id"));
        recV.setNotes(getString(rs, "expression_value_notes"));
        recV.setGeneExpressionRecordId(getIntOrZero(rs, "gene_expression_exp_record_id"));
        recV.setExpressionValue(getDoubleOrZero(rs, "expression_value"));
        recV.setExpressionUnit(getString(rs, "expression_unit"));
        recV.setMapKey(getIntOrZero(rs, "map_key"));
        recV.setExpressionLevel(getString(rs, "expression_level"));
        recV.setTpmValue(getDoubleOrZero(rs, "tpm_value"));

        ge.setGeneExpressionRecordValue(recV);

/* ======================
   Sample
   ====================== */
        Sample s = new Sample();
        s.setId(getIntOrZero(rs, "sample_id"));
        s.setAgeDaysFromHighBound(getDoubleOrZero(rs, "age_days_from_dob_high_bound"));
        s.setAgeDaysFromLowBound(getDoubleOrZero(rs, "age_days_from_dob_low_bound"));
        s.setNumberOfAnimals(getIntOrZero(rs, "number_of_animals"));
        s.setNotes(getString(rs, "sample_notes"));
        s.setSex(getString(rs, "sex"));
        s.setStrainAccId(getString(rs, "strain_ont_id"));
        s.setTissueAccId(getString(rs, "tissue_ont_id"));
        s.setCellTypeAccId(getString(rs, "cell_type_ont_id"));
        s.setCellLineId(getString(rs, "cell_line_id"));
        s.setGeoSampleAcc(getString(rs, "geo_sample_acc"));
        s.setBioSampleId(getString(rs, "biosample_id"));
        s.setLifeStage(getString(rs, "life_stage"));
        s.setLastModifiedBy(getString(rs, "last_modified_by"));
        s.setCreatedBy(getString(rs, "created_by"));
        s.setCuratorNotes(getString(rs, "CURATOR_NOTES"));
        s.setCultureDur(getIntOrZero(rs, "CULTURE_DUR_VALUE"));
        s.setCultureDurUnit(getString(rs, "CULTURE_DUR_UNIT"));
        s.setStrainTerm(getString(rs, "strain_term"));
        s.setTissueTerm(getString(rs, "tissue_term"));
        s.setComputedSex(getString(rs, "COMPUTED_SEX"));

        ge.setSample(s);

/* ======================
   Study / Reference
   ====================== */
        ge.setRefRgdId(getIntOrZero(rs, "REF_RGD_ID"));
        ge.setStudyId(getIntOrZero(rs, "STUDY_ID"));
        ge.setStudySource(getString(rs, "STUDY_SOURCE"));
        ge.setGeoSeriesAcc(getString(rs, "GEO_SERIES_ACC"));

        return ge;


    }
}

