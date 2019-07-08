package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.ConditionQuery;
import edu.mcw.rgd.dao.spring.GeneExpressionRecordQuery;
import edu.mcw.rgd.dao.spring.GeneExpressionRecordValueQuery;
import edu.mcw.rgd.dao.spring.MeasurementMethodQuery;
import edu.mcw.rgd.datamodel.pheno.Condition;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecord;
import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecordValue;
import edu.mcw.rgd.datamodel.pheno.MeasurementMethod;

import java.util.Collection;
import java.util.List;

/**
 * Created by mtutaj on 10/31/2018.
 */
public class GeneExpressionDAO extends PhenominerDAO {

    /**
     * insert a gene expression record into db
     * @param r GeneExpressionRecord object
     * @return generated id of the inserted record
     */
    public int insertGeneExpressionRecord(GeneExpressionRecord r) throws Exception {

        int id = getNextKeyFromSequence("gene_expression_exp_record_seq");
        r.setId(id);

        String sql = "INSERT INTO gene_expression_exp_record (gene_expression_exp_record_id, experiment_id, sample_id"
            +",last_modified_by, last_modified_date, curation_status, species_type_key) VALUES(?,?,?,?,?,?,?)";

        update(sql, id, r.getExperimentId(), r.getSampleId(), r.getLastModifiedBy(), r.getLastModifiedDate(),
                r.getCurationStatus(), r.getSpeciesTypeKey());
        return id;
    }

    /**
     * insert a gene expression record value into db
     * @param v GeneExpressionRecordValue object
     * @return generated id of the inserted value
     */
    public int insertGeneExpressionRecordValue(GeneExpressionRecordValue v) throws Exception {

        int id = getNextKeyFromSequence("gene_expression_values_seq");
        v.setId(id);

        String sql = "INSERT INTO gene_expression_values (gene_expression_value_id, expressed_object_rgd_id"
            +",expression_measurement_ont_id, expression_value_notes, gene_expression_exp_record_id"
            +",expression_value, expression_unit, map_key) VALUES(?,?,?,?,?,?,?,?)";

        update(sql, id, v.getExpressedObjectRgdId(), v.getExpressionMeasurementAccId(), v.getNotes(),
                v.getGeneExpressionRecordId(), v.getExpressionValue(), v.getExpressionUnit(), v.getMapKey());
        return id;
    }

    /**
     * delete a gene expression record value from db
     * @param geneExpressionValueId
     * @return count of rows affected
     */
    public int deleteGeneExpressionRecordValue(int geneExpressionValueId) throws Exception {

        String sql = "DELETE FROM gene_expression_values WHERE gene_expression_value_id=?";
        return update(sql, geneExpressionValueId);
    }

    /**
     * Returns a list of values for given gene expression experiment record
     * @param geneExpressionRecordId gene expression experiment record id
     * @return list of GeneExpressionRecordValue objects; could be empty
     * @throws Exception
     */
    public List<GeneExpressionRecordValue> getGeneExpressionRecordValues(int geneExpressionRecordId) throws Exception {
        String query = "SELECT * FROM gene_expression_values WHERE gene_expression_exp_record_id=?";

        GeneExpressionRecordValueQuery q = new GeneExpressionRecordValueQuery(getDataSource(), query);
        return execute(q, geneExpressionRecordId);
    }

    /**
     * Returns a list of conditions based on a gene expression record id
     * @param geneExpressionRecordId gene expression experiment record id
     * @return list of Condition objects; could be empty
     * @throws Exception
     */
    public List<Condition> getConditions(int geneExpressionRecordId) throws Exception {
        String query = "SELECT * FROM experiment_condition ec WHERE gene_expression_exp_record_id=? "+
                "ORDER BY ec.exp_cond_ordinality, ec.experiment_condition_id";

        ConditionQuery q = new ConditionQuery(this.getDataSource(), query);
        return execute(q, geneExpressionRecordId);
    }

    /**
     * Returns a list of measurement methods based on a gene expression record id
     * @param geneExpressionRecordId gene expression experiment record id
     * @return list of MeasurementMethod objects; should not be empty
     * @throws Exception
     */
    public List<MeasurementMethod> getMeasurementMethods(int geneExpressionRecordId) throws Exception {
        String query = "SELECT * FROM measurement_method mm WHERE gene_expression_exp_record_id=?";

        MeasurementMethodQuery q = new MeasurementMethodQuery(this.getDataSource(), query);
        return execute(q, geneExpressionRecordId);
    }

    /**
     * For given experiment id, get all gene expression records
     * @param experimentId
     * @return
     * @throws Exception
     */
    public List<GeneExpressionRecord> getGeneExpressionRecords(int experimentId) throws Exception {

        // load records
        String sql = "SELECT * FROM gene_expression_exp_record WHERE experiment_id=?";
        GeneExpressionRecordQuery q = new GeneExpressionRecordQuery(getDataSource(), sql);
        List<GeneExpressionRecord> records = execute(q, experimentId);

        // load record values and conditions
        for( GeneExpressionRecord record: records ) {
            record.setValues( getGeneExpressionRecordValues(record.getId()) );
            record.setConditions( getConditions(record.getId()) );
            record.setMeasurementMethods( getMeasurementMethods(record.getId()) );
        }

        return records;
    }

    /**
     * save gene expression records into database
     * @param records
     * @throws Exception
     */
    public void saveGeneExpressionRecords(Collection<GeneExpressionRecord> records) throws Exception {

        for( GeneExpressionRecord r: records ) {
            // if the record does not have an id, insert it
            if( r.getId()==0 ) {
                insertGeneExpressionRecord(r);
                for( GeneExpressionRecordValue v: r.getValues() ) {
                    v.setGeneExpressionRecordId(r.getId());
                }
                for( Condition c: r.getConditions() ) {
                    c.setGeneExpressionRecordId(r.getId());
                }
                for( MeasurementMethod m: r.getMeasurementMethods() ) {
                    m.setGeneExpressionRecordId(r.getId());
                }
            }

            for( GeneExpressionRecordValue v: r.getValues() ) {
                // if the value does not have an id, insert it
                if( v.getId()==0 ) {
                    insertGeneExpressionRecordValue(v);
                } else if( v.deleteFlag ){
                    deleteGeneExpressionRecordValue(v.getId());
                }
            }

            for( Condition c: r.getConditions() ) {
                // if the condition does not have an id, insert it
                if( c.getId()==0 ) {
                    insertCondition(c);
                } else {
                    updateCondition(c);
                }
            }

            for( MeasurementMethod m: r.getMeasurementMethods() ) {
                // if the measurement method does not have an id, insert it
                if( m.getId()==0 ) {
                    insertMeasurementMethod(m);
                } else {
                    updateMeasurementMethod(m);
                }
            }
        }
    }
}