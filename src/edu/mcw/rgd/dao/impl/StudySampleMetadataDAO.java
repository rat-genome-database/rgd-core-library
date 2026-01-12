package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StudySampleMetadataQuery;
import edu.mcw.rgd.datamodel.StudySampleMetadata;

import java.util.List;

public class StudySampleMetadataDAO extends AbstractDAO {

    public List<StudySampleMetadata> getStudySampleMetadata(int studyId) throws Exception{
        String sql= """
                SELECT\s
                    s.GEO_SAMPLE_ACC,
                    e.EXPERIMENT_ID,
                    ec.EXP_COND_ORDINALITY,
                    tissue_terms.TERM as Tissue,
                    strain_terms.TERM as Strain,
                    s.SEX,
                    s.COMPUTED_SEX,
                    s.AGE_DAYS_FROM_DOB_LOW_BOUND,
                    s.AGE_DAYS_FROM_DOB_HIGH_BOUND,
                    s.LIFE_STAGE,
                    exp_cond_terms.TERM as Experimental_Conditions,
                    cell_type_terms.TERM as Cell_Type,
                    ec.EXP_COND_ASSOC_UNITS,
                    ec.EXP_COND_ASSOC_VALUE_MIN,
                    ec.EXP_COND_ASSOC_VALUE_MAX,
                    ec.EXP_COND_DUR_SEC_LOW_BOUND,
                    ec.EXP_COND_DUR_SEC_HIGH_BOUND,
                    ec.EXP_COND_APPLICATION_METHOD,
                    ec.EXP_COND_NOTES
                FROM STUDY st
                JOIN EXPERIMENT e ON st.STUDY_ID = e.STUDY_ID
                JOIN GENE_EXPRESSION_EXP_RECORD ger ON e.EXPERIMENT_ID = ger.EXPERIMENT_ID
                JOIN SAMPLE s ON ger.SAMPLE_ID = s.SAMPLE_ID
                LEFT JOIN EXPERIMENT_CONDITION ec ON ger.GENE_EXPRESSION_EXP_RECORD_ID = ec.GENE_EXPRESSION_EXP_RECORD_ID
                LEFT JOIN RNA_SEQ rs ON rs.SAMPLE_ACCESSION_ID = s.GEO_SAMPLE_ACC
                JOIN ONT_TERMS tissue_terms ON s.TISSUE_ONT_ID = tissue_terms.TERM_ACC
                JOIN ONT_TERMS strain_terms ON s.STRAIN_ONT_ID = strain_terms.TERM_ACC
                LEFT JOIN ONT_TERMS exp_cond_terms ON ec.EXP_COND_ONT_ID = exp_cond_terms.TERM_ACC
                LEFT JOIN ONT_TERMS cell_type_terms ON s.CELL_TYPE_ONT_ID = cell_type_terms.TERM_ACC
                WHERE st.STUDY_ID = ?
                  AND (rs.CURATION_STATUS IS NULL OR rs.CURATION_STATUS != 'futureCuration')
                  AND ger.CURATION_STATUS=35
                  AND st.geo_series_acc=rs.geo_accession_id
                ORDER BY s.GEO_SAMPLE_ACC, ec.EXP_COND_ORDINALITY ASC
                """;
        return StudySampleMetadataQuery.execute(this,sql,studyId);
    }
}
