package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.GeneExpression;
import edu.mcw.rgd.datamodel.pheno.*;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
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
            +",last_modified_by, curation_status, species_type_key, CLINICAL_MEASUREMENT_ID, last_modified_date) VALUES(?,?,?,?,?,?,?,SYSTIMESTAMP)";

        update(sql, id, r.getExperimentId(), r.getSampleId(), r.getLastModifiedBy(),
                r.getCurationStatus(), r.getSpeciesTypeKey(), r.getClinicalMeasurementId());
        return id;
    }

    public void updateGeneExpressionRecord(GeneExpressionRecord r) throws Exception {
        String sql = "update gene_expression_exp_record set experiment_id=?, sample_id=?, last_modified_by=?, curation_status=?," +
                "species_type_key=?, CLINICAL_MEASUREMENT_ID=?, last_modified_date = SYSTIMESTAMP where gene_expression_exp_record_id=?";
        update(sql, r.getExperimentId(), r.getSampleId(), r.getLastModifiedBy(),
                r.getCurationStatus(), r.getSpeciesTypeKey(), r.getClinicalMeasurementId(), r.getId());
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

    public GeneExpressionRecord getGeneExpressionRecordBySampleId(int sampleId) throws Exception {
        String sql = "SELECT * FROM gene_expression_exp_record WHERE sample_id=? and curation_status=35";
        GeneExpressionRecordQuery q = new GeneExpressionRecordQuery(getDataSource(), sql);
        List<GeneExpressionRecord> records = execute(q, sampleId);
        if (records.isEmpty())
            return null;
        else {
            records.get(0).setValues(getGeneExpressionRecordValues(records.get(0).getId()));
            records.get(0).setConditions(getConditions(records.get(0).getId()));
            records.get(0).setMeasurementMethods(getMeasurementMethods(records.get(0).getId()));

            return records.get(0);
        }
    }

    public GeneExpressionRecord getGeneExpressionRecordByExperimentIdAndSampleId(int experimentId, int sampleId) throws Exception {

        // load records
        String sql = "SELECT * FROM gene_expression_exp_record WHERE experiment_id=? and sample_id=?";
        GeneExpressionRecordQuery q = new GeneExpressionRecordQuery(getDataSource(), sql);
        List<GeneExpressionRecord> records = execute(q, experimentId,sampleId);
        if (records.isEmpty())
            return null;
        else {
            // load record values and conditions
    //        for( GeneExpressionRecord record: records ) {
                records.get(0).setValues( getGeneExpressionRecordValues(records.get(0).getId()) );
                records.get(0).setConditions( getConditions(records.get(0).getId()) );
                records.get(0).setMeasurementMethods( getMeasurementMethods(records.get(0).getId()) );
    //        }

            return records.get(0);
        }
    }

    public Experiment getExperimentBySampleId(int sampleId) throws Exception {
        String sql = "select * from experiment where experiment_id in (select experiment_id FROM gene_expression_exp_record WHERE sample_id=?)";
        ExperimentQuery sq = new ExperimentQuery(this.getDataSource(), sql);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();

        List<Experiment> experiments = sq.execute(sampleId);
        return experiments.get(0);
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

    /**
     * Returns a list of values for given gene expression experiment record
     * @param rgdId expressed Object Rgd id
     * @return list of GeneExpressionRecordValue objects; could be empty
     * @throws Exception
     */
    public List<GeneExpressionRecordValue> getGeneExprRecordValuesForGene(int rgdId,String unit) throws Exception {
        String query = "SELECT * FROM gene_expression_values WHERE expressed_object_rgd_id=? and expression_unit =? order by gene_expression_exp_record_id";

        GeneExpressionRecordValueQuery q = new GeneExpressionRecordValueQuery(getDataSource(), query);
        return execute(q, rgdId,unit);
    }
    /**
     * Returns a list of values for given gene expression experiment record
     * @param rgdId expressed Object Rgd id
     * @return list of GeneExpressionRecordValue objects; could be empty
     * @throws Exception
     */
    public List<GeneExpressionRecordValue> getGeneExprRecordValuesForGeneByTissue(int rgdId,String unit,String level,String tissueOntId) throws Exception {
        String query = "select ge.* FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id\n" +
                "        join sample s on s.sample_id = gr.sample_id\n" +
                "        join experiment e on gr.experiment_id = e.experiment_id\n" +
                "        join study st on st.study_id = e.study_id " +
                "WHERE ge.expressed_object_rgd_id=? and ge.expression_unit =? and ge.expression_level =? and s.tissue_ont_id =? order by ge.gene_expression_exp_record_id";
        GeneExpressionRecordValueQuery q = new GeneExpressionRecordValueQuery(getDataSource(), query);
        return execute(q, rgdId,unit,level,tissueOntId);
    }
    /**
     * Returns a list of values for given gene expression experiment record
     * @return list of GeneExpressionRecordValue objects; could be empty
     * @throws Exception
     */
    public List<GeneExpressionRecordValue> getGeneExprRecordValuesForGeneBySlim(int rgdId,String unit,String level,String termAcc) throws Exception {
        String query = "select ge.* FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id" +
                " join sample s on s.sample_id = gr.sample_id join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?" +
                " CONNECT BY PRIOR child_term_acc=parent_term_acc ) AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit =?" +
                " and ge.expression_level=? order by ge.gene_expression_exp_record_id";

        GeneExpressionRecordValueQuery q = new GeneExpressionRecordValueQuery(getDataSource(), query);
        return execute(q, termAcc,rgdId,unit,level);
    }
    /**
     * Returns a list of values for given gene expression experiment record
     * @return list of GeneExpressionRecordValue objects; could be empty
     * @throws Exception
     */
    public List<GeneExpressionRecordValue> getGeneExprRecordValuesForGeneByTermRgdIdUnit(int rgdId,String unit,String termAcc) throws Exception {
        String query = "select ge.* FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id" +
                " join sample s on s.sample_id = gr.sample_id join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?" +
                " CONNECT BY PRIOR child_term_acc=parent_term_acc ) AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit =?" +
                " order by ge.gene_expression_exp_record_id";

        GeneExpressionRecordValueQuery q = new GeneExpressionRecordValueQuery(getDataSource(), query);
        return execute(q, termAcc,rgdId,unit);
    }

    public List<GeneExpressionRecord> getGeneExpressionRecordsByRecordValues(int rgdId, String unit, String termAcc) throws Exception{
        String sql ="select * from gene_expression_exp_record where gene_expression_exp_record_id in (" +
                " select ge.gene_expression_exp_record_id FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id" +
                " join sample s on s.sample_id = gr.sample_id" +
                " join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?" +
                " CONNECT BY PRIOR child_term_acc=parent_term_acc )" +
                " AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit =?)";
        GeneExpressionRecordQuery q = new GeneExpressionRecordQuery(getDataSource(),sql);
        return execute(q,termAcc, rgdId, unit);
    }

    public int getGeneExpressionSamplesCountByTermRgdIdUnit(String termAcc, int rgdId, String unit) throws Exception{
        String sql = """
                SELECT count(*)
                        FROM study st, experiment e, gene_expression_exp_record er, sample s
                        WHERE er.experiment_id in (
                                select distinct(experiment_id) from gene_expression_exp_record where gene_expression_exp_record_id in (
                                        select ge.gene_expression_exp_record_id FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id
                                        join sample s on s.sample_id = gr.sample_id
                                        join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc= ?
                                        CONNECT BY PRIOR child_term_acc=parent_term_acc )
                                        AND t.is_obsolete=0 and ge.expressed_object_rgd_id = ? and ge.expression_unit = ?
                                )
                        ) AND er.sample_id=s.sample_id
                        AND e.experiment_id = er.experiment_id
                        AND st.study_id=e.study_id
                        ORDER BY er.gene_expression_exp_record_id DESC""";
        return getCount(sql,termAcc,rgdId,unit);
    }

    /**
     * Returns count values for given gene
     * @return count of GeneExpressionRecordValue objects
     * @throws Exception
     */
    public String getGeneExprReValCountForGeneBySlim(int rgdId,String unit,String level,String termAcc) throws Exception {
        String query = "select value_count FROM gene_expression_value_counts ge where ge.expressed_object_rgd_id=? and ge.expression_unit =?" +
                " and ge.expression_level=? and ge.term_acc = ?";

       return getStringResultAdditive(query,rgdId,unit,level,termAcc);
    }
    /**
     * For given  id, get all gene expression record
     * @param id
     * @return
     * @throws Exception
     */
    public GeneExpressionRecord getGeneExpressionRecordById(int id) throws Exception {

        String sql = "SELECT * FROM gene_expression_exp_record WHERE gene_expression_exp_record_id=?";
        GeneExpressionRecordQuery q = new GeneExpressionRecordQuery(getDataSource(), sql);
        List<GeneExpressionRecord> record = execute(q, id);
        if( record.isEmpty() )
            return null;
        return record.get(0);
    }

    public List<GeneExpression> getGeneExpressionObjectsByTermRgdIdUnit(String termAcc, int rgdId, String unit) throws Exception{
        String query = """
                select ge.*,gr.*,s.*, st.ref_rgd_id from gene_expression_values ge, gene_expression_exp_record gr, sample s, experiment e, study st, ont_terms t\s
                        where ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id and s.sample_id = gr.sample_id and t.term_acc = s.tissue_ont_id and
                        t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?\s
                        CONNECT BY PRIOR child_term_acc=parent_term_acc )
                        AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit = ? and gr.experiment_id=e.experiment_id and e.study_id=st.study_id""";
        GeneExpressionQuery q = new GeneExpressionQuery(getDataSource(),query);
        return execute(q,termAcc,rgdId,unit);
    }

    public int getGeneExpressionCountByTermRgdIdUnit(String termAcc, int rgdId, String unit) throws Exception{
        String query = """
                select count(*) FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id
                        join sample s on s.sample_id = gr.sample_id
                        join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?\s
                        CONNECT BY PRIOR child_term_acc=parent_term_acc )
                        AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit=?""";
        return getCount(query, termAcc, rgdId, unit);
    }

    public int getGeneExprRecordValuesCountForGeneBySlim(String termAcc, int rgdId, String unit, String level) throws Exception{
        String query = """
                select count(*) FROM gene_expression_values ge join gene_expression_exp_record gr on ge.gene_expression_exp_record_id = gr.gene_expression_exp_record_id
                        join sample s on s.sample_id = gr.sample_id
                        join ont_terms t on t.term_acc = s.tissue_ont_id where  t.term_acc IN(SELECT child_term_acc FROM ont_dag START WITH parent_term_acc=?\s
                        CONNECT BY PRIOR child_term_acc=parent_term_acc )
                        AND t.is_obsolete=0 and ge.expressed_object_rgd_id=? and ge.expression_unit=? and and ge.expression_level=?""";
        return getCount(query, termAcc, rgdId, unit, level);
    }

    public List<GeneExpressionValueCount> getValueCountsByGeneRgdIdAndTerm(int rgdId, String termAcc) throws Exception{
        String sql = "SELECT * FROM GENE_EXPRESSION_VALUE_COUNTS where EXPRESSED_OBJECT_RGD_ID=? and TERM_ACC=?";
        GeneExpressionValueCountsQuery q = new GeneExpressionValueCountsQuery(DataSourceFactory.getInstance().getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rgdId,termAcc);
    }

    public GeneExpressionValueCount getValueCountsByGeneRgdIdTermUnitAndLevel(int rgdId, String termAcc, String unit, String level) throws Exception{
        String sql = "SELECT * FROM GENE_EXPRESSION_VALUE_COUNTS where EXPRESSED_OBJECT_RGD_ID=? and TERM_ACC=? and EXPRESSION_UNIT=? and EXPRESSION_LEVEL=?";
        GeneExpressionValueCountsQuery q = new GeneExpressionValueCountsQuery(DataSourceFactory.getInstance().getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        List<GeneExpressionValueCount> list = q.execute(rgdId,termAcc,unit,level);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public List<GeneExpressionValueCount> getValueCountsByGeneRgdIdTermAndUnit(int rgdId, String termAcc, String unit) throws Exception{
        String sql = "SELECT * FROM GENE_EXPRESSION_VALUE_COUNTS where EXPRESSED_OBJECT_RGD_ID=? and TERM_ACC=? and EXPRESSION_UNIT=?";
        GeneExpressionValueCountsQuery q = new GeneExpressionValueCountsQuery(DataSourceFactory.getInstance().getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rgdId,termAcc,unit);
    }

    public int insertGeneExpressionValueCountBatch(List<GeneExpressionValueCount> valueCounts) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getDataSource(),
                "insert into gene_expression_value_counts (VALUE_COUNT, EXPRESSED_OBJECT_RGD_ID, TERM_ACC, EXPRESSION_UNIT, EXPRESSION_LEVEL, LAST_MODIFIED_DATE) values (?,?,?,?,?,SYSDATE)",
                new int[]{Types.INTEGER,Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
        for (GeneExpressionValueCount vc : valueCounts){
            su.update(vc.getValueCnt(),vc.getExpressedRgdId(),vc.getTermAcc(),vc.getUnit(),vc.getLevel());
        }
        return executeBatch(su);
    }

    public int UpdateGeneExpressionValueCountBatch(List<GeneExpressionValueCount> valueCounts) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getDataSource(),
                "UPDATE gene_expression_value_counts set VALUE_COUNT=?, LAST_MODIFIED_DATE=SYSDATE "+
                        "where EXPRESSED_OBJECT_RGD_ID=? and TERM_ACC=? and EXPRESSION_UNIT=? and EXPRESSION_LEVEL=? ",
                new int[]{Types.INTEGER,Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
        for (GeneExpressionValueCount vc : valueCounts){
            su.update(vc.getValueCnt(),vc.getExpressedRgdId(),vc.getTermAcc(),vc.getUnit(),vc.getLevel());
        }
        return executeBatch(su);
    }
    public int UpdateGeneExpressionValueLastModifiedBatch(List<GeneExpressionValueCount> valueCounts) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getDataSource(),
                "UPDATE gene_expression_value_counts set LAST_MODIFIED_DATE=SYSDATE "+
                        "where EXPRESSED_OBJECT_RGD_ID=? and TERM_ACC=? and EXPRESSION_UNIT=? and EXPRESSION_LEVEL=? ",
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
        for (GeneExpressionValueCount vc : valueCounts){
            su.update(vc.getExpressedRgdId(),vc.getTermAcc(),vc.getUnit(),vc.getLevel());
        }
        return executeBatch(su);
    }
}
