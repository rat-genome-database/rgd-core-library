package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecord;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 10/31/2018
 * QurRepresents a row from GENE_EXPRESSION_EXP_RECORD table.
 */
public class GeneExpressionRecordQuery extends MappingSqlQuery {

    public GeneExpressionRecordQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeneExpressionRecord rec = new GeneExpressionRecord();
        rec.setId(rs.getInt("gene_expression_exp_record_id"));
        rec.setExperimentId(rs.getInt("experiment_id"));
        rec.setSampleId(rs.getInt("sample_id"));
        rec.setLastModifiedBy(rs.getString("last_modified_by"));
        rec.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        rec.setCurationStatus(rs.getInt("curation_status"));
        rec.setSpeciesTypeKey(rs.getInt("species_type_key"));
        rec.setClinicalMeasurementId(rs.getInt("CLINICAL_MEASUREMENT_ID"));
        return rec;
    }
}
