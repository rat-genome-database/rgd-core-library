package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.datamodel.pheno.*;
import edu.mcw.rgd.process.mapping.MapManager;
import edu.mcw.rgd.reporting.Link;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.reporting.Report;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
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


    public HashMap<String, Report> createExpressionReport(List<String> terms, Gene obj, List<String> exclude, HashMap<String,Integer> recordValuesMap)throws Exception {
        PhenominerDAO phenominerDAO = new PhenominerDAO();
        OntologyXDAO xdao = new OntologyXDAO();
        HashMap<String, Report> reportMap = new HashMap<>();
        for (String term : terms) {
            List<GeneExpressionRecordValue> gerv = getGeneExprRecordValuesForGeneByTermRgdIdUnit(obj.getRgdId(), "TPM", term);
            if (gerv.isEmpty())
                exclude.add(term);
            else {
                recordValuesMap.put(term, gerv.size());
                HashMap<Integer, GeneExpressionRecord> geneExprRecMap = new HashMap<>();
                HashMap<Integer, Experiment> experimentMap = new HashMap<>();
                HashMap<Integer, edu.mcw.rgd.datamodel.pheno.Sample> sampleMap = new HashMap<>();
                HashMap<Integer, Study> studyMap = new HashMap<>();

                Report report = new Report();
                Record recHeader = new Record();
                if (obj.getSpeciesTypeKey() == SpeciesType.HUMAN) {
                    recHeader.append("Cell line");
                } else recHeader.append("Strain");
                recHeader.append("Sex");
                recHeader.append("Age");
                recHeader.append("Tissue");
                recHeader.append("Value");
                recHeader.append("Unit");
                recHeader.append("Assembly");
                recHeader.append("Reference");
                report.append(recHeader);


                for (GeneExpressionRecordValue rec : gerv) {
                    GeneExpressionRecord geneExpRec;
                    Experiment e;
                    edu.mcw.rgd.datamodel.pheno.Sample s;
                    Study study;
                    if (geneExprRecMap.isEmpty() || !geneExprRecMap.keySet().contains(rec.getGeneExpressionRecordId())) {
                        geneExpRec = getGeneExpressionRecordById(rec.getGeneExpressionRecordId());
                        geneExprRecMap.put(rec.getGeneExpressionRecordId(), geneExpRec);
                    } else geneExpRec = geneExprRecMap.get(rec.getGeneExpressionRecordId());

                    if (experimentMap.isEmpty() || !experimentMap.keySet().contains(geneExpRec.getExperimentId())) {
                        e = phenominerDAO.getExperiment(geneExpRec.getExperimentId());
                        study = phenominerDAO.getStudy(e.getStudyId());
                        experimentMap.put(e.getId(), e);
                        studyMap.put(e.getStudyId(), study);
                    } else {
                        e = experimentMap.get(geneExpRec.getExperimentId());
                        study = studyMap.get(e.getStudyId());
                    }

                    if (sampleMap.isEmpty() || !sampleMap.keySet().contains(geneExpRec.getSampleId())) {
                        s = phenominerDAO.getSample(geneExpRec.getSampleId());
                        sampleMap.put(s.getId(), s);

                    } else s = sampleMap.get(geneExpRec.getSampleId());


                    Record record = new Record();

                    String age;
                    if (s.getAgeDaysFromLowBound() == 0 && s.getAgeDaysFromHighBound() == 0)
                        age = "not available";
                    else {
                        if (s.getAgeDaysFromHighBound() < 0 || s.getAgeDaysFromLowBound() < 0) {
                            if (obj.getSpeciesTypeKey() == SpeciesType.HUMAN) {
                                String ageLow = String.valueOf(s.getAgeDaysFromLowBound() + 280);
                                String ageHigh = String.valueOf(s.getAgeDaysFromHighBound() + 280);
                                if (ageLow.equalsIgnoreCase(ageLow))
                                    age = ageLow + " days post conception";
                                else {
                                    age = ageLow + " - " + ageHigh;
                                    age += " days post conception";
                                }
                            } else {
                                String ageLow = String.valueOf(s.getAgeDaysFromLowBound() + 21);
                                String ageHigh = String.valueOf(s.getAgeDaysFromHighBound() + 23);
                                if (ageLow.equalsIgnoreCase(ageLow))
                                    age = ageLow + " embryonic days";
                                else {
                                    age = ageLow + " - " + ageHigh;
                                    age += " embryonic days";
                                }
                            }
                        } else {
                            if (s.getAgeDaysFromLowBound().compareTo(s.getAgeDaysFromHighBound()) == 0) {
                                if (s.getAgeDaysFromLowBound() > 365)
                                    age = (s.getAgeDaysFromLowBound() / 365) + " years";
                                else if (s.getAgeDaysFromLowBound() < 365 && s.getAgeDaysFromLowBound() > 30)
                                    age = (s.getAgeDaysFromLowBound() / 30) + " months";
                                else age = s.getAgeDaysFromLowBound() + " days";
                            } else {
                                if (s.getAgeDaysFromLowBound() > 365 || s.getAgeDaysFromHighBound() > 365)
                                    age = String.valueOf(s.getAgeDaysFromLowBound() / 365) + " - " + String.valueOf(s.getAgeDaysFromHighBound() / 365) + " years";
                                else if ((s.getAgeDaysFromLowBound() < 365 || s.getAgeDaysFromHighBound() < 365) && (s.getAgeDaysFromHighBound() > 30 || s.getAgeDaysFromLowBound() > 30))
                                    age = String.valueOf(s.getAgeDaysFromLowBound() / 30) + " - " + String.valueOf(s.getAgeDaysFromHighBound() / 30) + " months";
                                else
                                    age = String.valueOf(s.getAgeDaysFromLowBound()) + " - " + String.valueOf(s.getAgeDaysFromHighBound()) + " days";
                            }
                        }
                    }
                    if (s.getStrainAccId() != null && !s.getStrainAccId().isEmpty()) {
                        Term t = xdao.getTermByAccId(s.getStrainAccId());
                        if (term != null)
                            record.append(t.getTerm());
                        else record.append(s.getStrainAccId());
                    } else record.append("");
                    record.append(s.getSex());
                    record.append(age);
                    if (s.getTissueAccId() != null && !s.getTissueAccId().isEmpty()) {
                        Term t = xdao.getTermByAccId(s.getTissueAccId());
                        if (term != null)
                            record.append(t.getTerm());
                        else record.append(s.getTissueAccId());
                    } else record.append("");
                    record.append(rec.getExpressionValue().toString());
                    record.append("TPM");
                    try {
                        record.append(MapManager.getInstance().getMap(rec.getMapKey()).getName());
                    } catch (Exception ex) {
//                    throw new RuntimeException(ex);
                    }
                    record.append("<a href=" + Link.ref(study.getRefRgdId()) + ">" + study.getRefRgdId() + "</a>");
                    report.append(record);

                }
                reportMap.put(term, report);
            }
        }
        return reportMap;
    }
}
