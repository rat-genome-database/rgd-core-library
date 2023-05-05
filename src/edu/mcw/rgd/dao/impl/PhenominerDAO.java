package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.GeoRecord;
import edu.mcw.rgd.datamodel.HistogramRecord;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.ontologyx.Ontology;
import edu.mcw.rgd.datamodel.pheno.*;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.pheno.SearchBean;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

/**
 * @author mtutaj
 * @since Oct 15, 2010
 */
public class PhenominerDAO extends AbstractDAO {

    final int CURATION_STATUS = 40; // experiment records visible in phenominer


    /**
     * return a study given a study id
     * @param id study id
     * @return Study object or null if no study with such an id
     * @throws Exception on error in spring framework
     */
    public Study getStudy(int  id) throws Exception {
        String query = "SELECT * FROM study WHERE study_id=?";

        StudyQuery q = new StudyQuery(this.getDataSource(), query);
        List<Study> studies = execute(q, id);
        if( studies.isEmpty() )
            return null;
        return studies.get(0);
    }

    public Study getStudyByGeoId(String  id) throws Exception {
        String query = "SELECT * FROM study WHERE geo_series_acc=?";

        StudyQuery q = new StudyQuery(this.getDataSource(), query);
        List<Study> studies = execute(q, id);
        if( studies.isEmpty() )
            return null;
        return studies.get(0);
    }

    /**
     *  Return all studies
     *  @return list of all studies
     */
    public List<Study> getStudies() throws Exception {
        String query = "SELECT * FROM study ORDER BY study_id desc";

        StudyQuery q = new StudyQuery(this.getDataSource(), query);
        return execute(q);
    }
    /**
     *  Return all Phenominer Units
     *  @return list of all phenominerUnits
     */
    public List<PhenominerUnit> getPhenominerUnits(String query) throws Exception {
        //String query = "";
        PhenominerUnitQuery pquery = new PhenominerUnitQuery(this.getDataSource(), query);
        return execute(pquery);
    }

    /**
     *  Return all Phenominer Units
     *  @return list of all phenominer Unit Tables
     */
    public List<PhenominerUnitTable> getPhenominerUnitTables() throws Exception {
        String query = "select distinct su.ONT_ID, su.STANDARD_UNIT, tus.UNIT_FROM, tus.UNIT_TO, tus.TERM_SPECIFIC_SCALE, tus.ZERO_OFFSET from PHENOMINER_STANDARD_UNITS su\n" +
                "join PHENOMINER_TERM_UNIT_SCALES tus on su.ONT_ID=tus.ONT_ID order by su.ONT_ID";
        PhenominerUnitTablesQuery pquery = new PhenominerUnitTablesQuery(this.getDataSource(), query);
        return execute(pquery);
    }

    /**
     *  Return all Phenominer Enums
     *  @return list of all phenominer Enum Tables
     */
    public List<phenominerEnumTable> getPhenominerEnumTables() throws Exception {
        String query = "select distinct en.TYPE, en.LABEL, en.VALUE, entp.DESCRIPTION from PHENOMINER_ENUMERABLES en \n" +
                "join PHENOMINER_ENUM_TYPES entp on en.TYPE = entp.ID ORDER BY en.TYPE";
        PhenominerEnumTablesQuery pquery = new PhenominerEnumTablesQuery(this.getDataSource(), query);
        return execute(pquery);
    }

    /**
     *  Return all records having No std Units (CMO IDs that are being used for measurements but do not appear in the standard units table)
     *  @return list of all phenominer Enum Tables
     */
    public List<phenominerNoStdUnitTable> getPhenominerNoStdUnitsTables() throws Exception {
        String query = "select distinct cm.CLINICAL_MEASUREMENT_ONT_ID,ot.TERM, er.MEASUREMENT_UNITS from EXPERIMENT_RECORD er join CLINICAL_MEASUREMENT cm on cm.CLINICAL_MEASUREMENT_ID=er.CLINICAL_MEASUREMENT_ID\n" +
                "JOIN ONT_TERMS ot on cm.CLINICAL_MEASUREMENT_ONT_ID=ot.TERM_ACC\n" +
                "where er.CURATION_STATUS<>50 and cm.CLINICAL_MEASUREMENT_ONT_ID not in(select su.ONT_ID from PHENOMINER_STANDARD_UNITS su) order by cm.CLINICAL_MEASUREMENT_ONT_ID";
        PhenominerNoStdUnitsTablesQuery pquery = new PhenominerNoStdUnitsTablesQuery(this.getDataSource(), query);
        return execute(pquery);
    }

    /**
     *  Return all Phenominer Unit search results
     *  @return list of all phenominer Unit Tables
     */
    public List<PhenominerUnitTable> getPhenominerUnitSearchResultsTables(PhenominerUnitTable pUnitTable) throws Exception{
        String cmoIdField = pUnitTable.getOntId();
        String stdUnitField = pUnitTable.getStdUnit();
        String unitFromField = pUnitTable.getUnitFrom();
        String query = "select distinct su.ONT_ID, su.STANDARD_UNIT, tus.UNIT_FROM, tus.UNIT_TO, tus.TERM_SPECIFIC_SCALE, tus.ZERO_OFFSET from PHENOMINER_STANDARD_UNITS su\n" +
                "join PHENOMINER_TERM_UNIT_SCALES tus on su.ONT_ID=tus.ONT_ID where 1=1";

        ArrayList<String> paramList = new ArrayList<String>();
        if(!(cmoIdField == null || cmoIdField.length() == 0)) {
            if (cmoIdField.length() == 11) {
                query = query + " and su.ONT_ID = ?";
                paramList.add(cmoIdField);
            }
        }
        if(!(stdUnitField == null || stdUnitField.length() == 0) ){
            query = query + " and su.STANDARD_UNIT = ?";
            paramList.add(stdUnitField);
        }
        if(!(unitFromField == null || unitFromField.length() == 0)){
            query = query + " and tus.UNIT_FROM = ?";
            paramList.add(unitFromField);
        }
        query = query + " order by su.ONT_ID";
        Object params[] = new Object[paramList.size()];
        params = paramList.toArray();
        PhenominerUnitTablesQuery pquery = new PhenominerUnitTablesQuery(this.getDataSource(), query);
        return execute(pquery,params);
    }
    /**
     *  Return all Phenominer Enums search results
     *  @return list of all phenominer Enum Tables
     */
    public List<phenominerEnumTable> getPhenominerEnumSearchResultsTables(phenominerEnumTable pEnumTable) throws Exception{
        int typeField = pEnumTable.getType();
        String labelField = pEnumTable.getLabel();
        String query = "select distinct en.TYPE, en.LABEL, en.VALUE, entp.DESCRIPTION from PHENOMINER_ENUMERABLES en \n" +
                "join PHENOMINER_ENUM_TYPES entp on en.TYPE = entp.ID where 1=1";

        ArrayList<String> paramList = new ArrayList<String>();
        if(typeField!=0){
            query = query + " and en.TYPE = ?";
            paramList.add(String.valueOf(typeField));
        }
        if(!(labelField == null || labelField.length() == 0)){
            query = query + " and UPPER(en.LABEL) = UPPER(?)";
            paramList.add(labelField);
        }
        query = query + "ORDER BY en.TYPE" ;
        Object params[] = new Object[paramList.size()];
        params = paramList.toArray();
        PhenominerEnumTablesQuery pquery = new PhenominerEnumTablesQuery(this.getDataSource(), query);
        return execute(pquery,params);
    }
    /**
     *  Return all GEO studies
     *  @return list of all studies
     */
    public HashMap<String,GeoRecord> getGeoStudies(String species,String status) throws Exception {

        String query = "SELECT * FROM rna_seq where sample_organism like ? and platform_technology= 'high-throughput sequencing' and curation_status = ? ORDER BY geo_accession_id desc";

        GeoRecordQuery q = new GeoRecordQuery(this.getDataSource(), query);
        List<GeoRecord> result = execute(q,species+"%",status);
        HashMap r = new HashMap();
        List<String> studyList = new ArrayList<>();
        if(result != null) {
            for(GeoRecord s:result){
                r.put(s.getGeoAccessionId(),s);
            }
             return r;
        }

        return null;
    }

    public void updateGeoStudyStatus(String gse,String status,String species) throws Exception{

        String query = "UPDATE rna_seq SET curation_status = ? WHERE geo_accession_id =? and sample_organism like ? ";
        update(query, status,gse,species+"%");

    }

    public void updateGeoSampleStatus(String gse,String gsm,String status,String species) throws Exception{

        String query = "UPDATE rna_seq SET curation_status = ? WHERE geo_accession_id =? and sample_accession_id =? and sample_organism like ? ";
        update(query, status,gse,gsm,species+"%");

    }
    /**
     * get list of studies given list of study ids
     * <p>
     * Note: only first 1000 studies is returned, due to Oracle limitations
     * @param studyIds list of study ids
     * @return list of Study objects
     * @throws Exception
     */
    public List<Study> getStudies(List studyIds) throws Exception {
        if (studyIds.size() == 0) {
            return Collections.emptyList();
        }
        if( studyIds.size()>1000 ) {
            studyIds = studyIds.subList(0, 999);
        }

        String query = "SELECT * FROM study WHERE study_id IN ("
            + Utils.concatenate(studyIds,",")
            + ")  ORDER BY study_id DESC";

        return execute(new StudyQuery(this.getDataSource(), query));
    }

    /**
     * Update study in the data store
     * @param study Study object
     * @throws Exception
     */
    public void updateStudy(Study study) throws Exception{

        String query = "UPDATE study SET study_name=?, study_source=?, study_type=?, ref_rgd_id=?, "+
            "data_type=?, geo_series_acc=?, last_modified_by = ?, last_modified_date = SYSTIMESTAMP WHERE study_id=?";
        update(query, study.getName(), study.getSource(), study.getType(), study.getRefRgdId(),
                study.getDataType(), study.getGeoSeriesAcc(),study.getLastModifiedBy(), study.getId());

        // Update curation status for each experiment record that belongs to this study
        if (study.getCurationStatus() != -1) {
            query = "update experiment_record er set er.curation_status = ? " +
                    "where exists " +
                    "(select experiment_record_id from experiment ex " +
                    "where ex.study_id = ? and ex.experiment_id = er.experiment_id)";

            update(query, study.getCurationStatus(), study.getId());
        }
    }

    /**
     * Insert a study in the data store; return study id assigned automatically during db insert
     * @param study Study object
     * @return study id assigned automatically during db insert
     * @throws Exception
     */
    public int insertStudy(Study study) throws Exception{

        int studyId = this.getNextKey("study_seq");
        study.setId(studyId);

        String sql = "INSERT INTO study (study_name, study_source, study_type, ref_rgd_id, " +
                "data_type, geo_series_acc, study_id,last_modified_by,created_by,created_date, last_modified_date) VALUES(?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,SYSTIMESTAMP)";

        update(sql, study.getName(), study.getSource(), study.getType(), study.getRefRgdId(),
                study.getDataType(), study.getGeoSeriesAcc(), study.getId(),study.getLastModifiedBy(),study.getCreatedBy());

        return studyId;
    }

    /**
     * Delete a study from the data store
     * @param studyId study id
     * @throws Exception
     */
    public void deleteStudy(int studyId) throws Exception{

        List<Experiment> experiments = this.getExperiments(studyId);

        for (Experiment e: experiments) {
            this.deleteExperiment(e.getId());
        }
        
        String sql = "delete from study where study_id=?";
        update(sql, studyId);
    }

    /**
     * Get the total count of studies
     * @return total count of studies
     * @throws Exception
     */
    public int getStudyCount() throws Exception {
        String query = "SELECT count(*) from study";
        return getCount(query);
    }

    /**
     * Return an experiment based on an experiement ID
     * @param id
     * @return
     * @throws Exception
     */
    public Experiment getExperiment(int  id) throws Exception {
        String query = "SELECT * from experiment where experiment_id=?";

        ExperimentQuery sq = new ExperimentQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();

        List<Experiment> studies = sq.execute(id);
        return studies.get(0);
    }


    /**
     * Return the experiment count based on a study ID
     * @param studyId
     * @return
     * @throws Exception
     */
    public int getExperimentCount(int  studyId) throws Exception {
        String query = "SELECT count(*) from experiment where study_id=?";
        return getCount(query, studyId);
    }

    /**
     * Return a list of experiments tied to a study ID
     * @param studyId
     * @return
     * @throws Exception
     */
    public List<Experiment> getExperiments(int  studyId) throws Exception {
        String query = "SELECT * from experiment where study_id=? order by experiment_id desc";

        ExperimentQuery sq = new ExperimentQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();

        return sq.execute(new Object[]{studyId});
    }

    /**
     * Return a list of experiments from a list of experiment ID's passed in
     * <p>
     *     Note: only first 1000 experiments is returned, due to Oracle limitations
     * </p>
     * @param exIds
     * @return
     * @throws Exception
     */
    public List<Experiment> getExperiments(List exIds) throws Exception {
        if (exIds.size() == 0) {
            return new ArrayList<Experiment>();
        }
        if( exIds.size()>1000 ) {
            exIds = exIds.subList(0, 999);
        }
        String query = "SELECT * from experiment where experiment_id in (";

        for (int i=0; i< exIds.size(); i++) {
            if (i > 0) {
                query = query + " ,";
            }

            query = query + " ?";
        }

        query = query + ")  order by experiment_id desc";

        ExperimentQuery sq = new ExperimentQuery(this.getDataSource(), query);

        Object[] objArray = new Object[exIds.size()];

        for (int i=0; i< exIds.size(); i++) {
            sq.declareParameter(new SqlParameter(Types.INTEGER));
            objArray[i] = exIds.get(i);
        }

        sq.compile();

        return sq.execute(objArray);
    }

    /**
     * Updates the experiment in the data store
     * @param ex
     * @throws Exception
     */
    public void updateExperiment(Experiment ex) throws Exception{
        
        String query = "update experiment set study_id=?, experiment_name=?, experiment_notes=?, trait_ont_id=? ,last_modified_by=?, last_modified_date = SYSTIMESTAMP where experiment_id=? ";
        update(query, ex.getStudyId(),ex.getName(),ex.getNotes(), ex.getTraitOntId(),ex.getLastModifiedBy(), ex.getId());

        /* Update curation status for each experiment record that belongs to this experiment */
        if (ex.getCurationStatus() != -1) {
            query = "update experiment_record er set er.curation_status = ? " +
                    "where er.experiment_id = ?";
            update(query, ex.getCurationStatus(), ex.getId());
        }
    }

    /**
     * Inserts an experiment into the data store; returns experiment_id assigned automatically during insert
     * @param ex Experiment object
     * @return experiment_id assigned automatically during insert
     * @throws Exception
     */
    public int insertExperiment(Experiment ex) throws Exception{

        int experimentId = this.getNextKey("experiment_seq");
        ex.setId(experimentId);

        String query = "insert into experiment (study_id, experiment_name, experiment_notes, experiment_id, trait_ont_id,last_modified_by,created_by,created_date,last_modified_date) " +
                "values (?,?,?,?,?,?,?,SYSTIMESTAMP,SYSTIMESTAMP) ";
        update(query, ex.getStudyId(),ex.getName(),ex.getNotes(),ex.getId(),ex.getTraitOntId(),ex.getLastModifiedBy(),ex.getCreatedBy());

        return experimentId;
    }

    /**
     * Deletes an experiment from the data store
     * @param exId experiment id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void deleteExperiment(int exId) throws Exception{

        List<Record> records = this.getRecords(exId);
        for (Record r: records) {
            this.deleteRecord(r.getId());
        }

        String sql = "DELETE FROM experiment WHERE experiment_id=?";
        update(sql, exId);
    }

    /**
     * Returns the record count for a given experiment
     * @param experimentId experiment id
     * @return the record count for a given experiment
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCount(int experimentId) throws Exception {

        String query = "SELECT COUNT(*) FROM experiment_record WHERE experiment_id=?";
        return getCount(query, experimentId);
    }

    /**
     * Returns the record count for total and all curation statuses
     * @param experimentId experiment id
     * @return the record count for a given experiment
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordStatusCount(int experimentId) throws Exception {
        String query = "SELECT COUNT(*) FROM experiment_record er \n" +
                "WHERE er.experiment_id = ?\n" +
                "union all select * from (\n" +
                "select count(curation_status) from (select * from PHENOMINER_ENUMERABLES where type=6) pe left join EXPERIMENT_RECORD er\n" +
                "on pe.VALUE_INT = er.CURATION_STATUS and \n" +
                "er.EXPERIMENT_ID = ?\n" +
                "group by value_int order by value_int) a";

        return IntListQuery.execute(this, query, experimentId, experimentId);
    }

    /**
     * Returns the record count for a given term
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the record count for a given term; -1 if ontology is not one of 'RS,'CS','MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForTerm(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT COUNT(1) FROM sample s,experiment_record r "+
                    "WHERE strain_ont_id=? AND r.sample_id=s.sample_id AND r.curation_status=?";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT COUNT(1) FROM measurement_method m,experiment_record r " +
                    "WHERE measurement_method_ont_id=? AND r.measurement_method_id=m.measurement_method_id AND r.curation_status=?";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT COUNT(1) FROM clinical_measurement c,experiment_record r " +
                    "WHERE clinical_measurement_ont_id=? AND r.clinical_measurement_id=c.clinical_measurement_id AND curation_status=?";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT COUNT(1) FROM experiment_condition x,experiment_record r "+
                    "WHERE exp_cond_ont_id=? AND x.experiment_record_id=r.experiment_record_id AND r.curation_status=?";
        }
        else {
            // bad ontology
            return -1;
        }

        return getCount(query, accId, CURATION_STATUS);
    }

    /**
     * Returns the record count for a given term
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the record count for a given term; -1 if ontology is not one of 'RS,'CS','MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForTerm(String accId, int speciesTypeKey) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT COUNT(1) FROM sample s,experiment_record r "+
                    "WHERE strain_ont_id=? AND r.sample_id=s.sample_id";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT COUNT(1) FROM measurement_method m,experiment_record r " +
                    "WHERE measurement_method_ont_id=? AND r.measurement_method_id=m.measurement_method_id";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT COUNT(1) FROM clinical_measurement c,experiment_record r " +
                    "WHERE clinical_measurement_ont_id=? AND r.clinical_measurement_id=c.clinical_measurement_id";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT COUNT(1) FROM experiment_condition x,experiment_record r "+
                    "WHERE exp_cond_ont_id=? AND x.experiment_record_id=r.experiment_record_id";
        }
        else {
            // bad ontology
            return -1;
        }
        query += " AND r.curation_status=? AND r.species_type_key=?";

        return getCount(query, accId, CURATION_STATUS, speciesTypeKey);
    }

    /**
     * Returns the record count for a given term strain ontology term
     * @param rsId strain ontology term accession id
     * @param sex 'male','female' or 'both'; if null, it defaults to 'both'
     * @return the record count for a given term; -1 if ontology is not 'RS' or 'CS'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForStrainTerm(String rsId, String sex, int speciesTypeKey) throws Exception {

        if( !(rsId.startsWith("RS:") || rsId.startsWith("CS:")) ) {
            // bad ontology
            return -1;
        }

        // validate sex
        sex = Utils.NVL(sex, "both");
        if( !sex.equals("male") && !sex.equals("female") ) {
            return getRecordCountForTerm(rsId, speciesTypeKey);
        }

        // determine kind of term
        String sql = "SELECT COUNT(1) FROM sample s,experiment_record r "+
                    "WHERE strain_ont_id=? AND r.sample_id=s.sample_id AND r.curation_status=? AND sex=? AND species_type_key=?";

        return getCount(sql, rsId, CURATION_STATUS, sex, speciesTypeKey);
    }

    /**
     * Returns the record count for a given term and its descendant terms
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the record count for a given term and its descedants terms; -1 if ontology is not one of 'RS,'CS','MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForTermAndDescendants(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT COUNT(1) FROM sample s,experiment_record r\n"+
                    "WHERE r.curation_status=? AND r.sample_id=s.sample_id AND strain_ont_id IN(\n";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT COUNT(1) FROM measurement_method m,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT COUNT(1) FROM clinical_measurement c,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT COUNT(1) FROM experiment_condition x,experiment_record r\n"+
                    "WHERE r.curation_status=? AND x.experiment_record_id=g.experiment_record_id AND exp_cond_ont_id IN(\n";
        }
        else {
            // bad ontology
            return -1;
        }

        query +=
        "SELECT ? FROM dual UNION \n"+
        "SELECT child_term_acc FROM ont_dag\n" +
        "START WITH parent_term_acc=?\n" +
        "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return getCount(query, CURATION_STATUS, accId, accId);
    }

    /**
     * Returns the record count for a given term and its descendant terms
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the record count for a given term and its descedants terms; -1 if ontology is not one of 'RS,'CS','MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForTermAndDescendants(String accId, int speciesTypeKey) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT COUNT(1) FROM sample s,experiment_record r\n"+
                    "WHERE r.curation_status=? AND r.species_type_key=? AND r.sample_id=s.sample_id AND strain_ont_id IN(\n";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT COUNT(1) FROM measurement_method m,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.species_type_key=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT COUNT(1) FROM clinical_measurement c,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.species_type_key=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT COUNT(1) FROM experiment_condition x,experiment_record r\n"+
                    "WHERE r.curation_status=? AND r.species_type_key=? AND x.experiment_record_id=r.experiment_record_id AND exp_cond_ont_id IN(\n";
        }
        else {
            // bad ontology
            return -1;
        }

        query +=
                "SELECT ? FROM dual UNION \n"+
                        "SELECT child_term_acc FROM ont_dag\n" +
                        "START WITH parent_term_acc=?\n" +
                        "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return getCount(query, CURATION_STATUS, speciesTypeKey, accId, accId);
    }

    /**
     * Returns the record count for a given strain term and its descendant terms
     * @param rsId 'RS' or 'CS' ontology term accession id
     * @return the record count for a given term and its descendants terms; -1 if ontology is not 'RS' or 'CS'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForStrainTermAndDescendants(String rsId, String sex, int speciesTypeKey) throws Exception {

        if( !(rsId.startsWith("RS:") || rsId.startsWith("CS:")) ) {
            // bad ontology
            return -1;
        }

        // validate sex
        sex = Utils.NVL(sex, "both");
        if( !sex.equals("male") && !sex.equals("female") ) {
            return getRecordCountForTermAndDescendants(rsId, speciesTypeKey);
        }

        // determine kind of term
        String sql;
        sql = "SELECT COUNT(1) FROM sample s,experiment_record r\n"+
            "WHERE r.curation_status=? AND r.species_type_key=? AND r.sample_id=s.sample_id AND sex=? AND strain_ont_id IN(\n"+
                "SELECT ? FROM dual UNION \n"+
                "SELECT child_term_acc FROM ont_dag\n" +
                "START WITH parent_term_acc=?\n" +
                "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return getCount(sql, CURATION_STATUS, speciesTypeKey, sex, rsId, rsId);
    }

    /**
     * Returns the record ids for a given term and its descendant terms
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the experiment record ids for a given term and its descedants terms
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordIdsForTermAndDescendants(String accId, int speciesTypeKey) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM sample s,experiment_record r\n"+
                    "WHERE r.curation_status=? AND species_type_key=? AND r.sample_id=s.sample_id AND strain_ont_id IN(\n";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM measurement_method m,experiment_record r\n" +
                    "WHERE r.curation_status=? AND species_type_key=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM clinical_measurement c,experiment_record r\n" +
                    "WHERE r.curation_status=? AND species_type_key=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM experiment_condition x,experiment_record r\n"+
                    "WHERE r.curation_status=? AND species_type_key=? AND x.experiment_record_id=r.experiment_record_id\n"+
                    "AND exp_cond_ont_id IN(\n";
        }
        else {
            // bad ontology
            return null;
        }

        query +=
            "SELECT ? FROM dual UNION \n"+
            "SELECT child_term_acc FROM ont_dag\n" +
            "START WITH parent_term_acc=?\n" +
            "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return IntListQuery.execute(this, query, CURATION_STATUS, speciesTypeKey, accId, accId);
    }

    /**
     * Returns the record ids for a given strain term and its descendant terms
     * @param termId ontology term acc id
     * @return the record ids for a given term and its descendants terms
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordIdsForTermAndDescendants(String termId, String sex, int speciesTypeKey) throws Exception {

        if( !(termId.startsWith("RS:") || termId.startsWith("CS:")) ) {
            // ignore sex param for ontology other than 'RS'
            return getRecordIdsForTermAndDescendants(termId, speciesTypeKey);
        }

        // validate sex
        sex = Utils.NVL(sex, "both");
        if( !sex.equals("male") && !sex.equals("female") ) {
            return getRecordIdsForTermAndDescendants(termId, speciesTypeKey);
        }

        // determine kind of term
        String sql =
            "SELECT DISTINCT r.experiment_record_id FROM sample s,experiment_record r\n"+
            "WHERE r.curation_status=? AND r.species_type_key=? AND r.sample_id=s.sample_id AND sex=? AND strain_ont_id IN(\n"+
            "SELECT ? FROM dual UNION \n"+
            "SELECT child_term_acc FROM ont_dag\n" +
            "START WITH parent_term_acc=?\n" +
            "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return IntListQuery.execute(this, sql, CURATION_STATUS, speciesTypeKey, sex, termId, termId);
    }

    /**
     * Returns the record ids for a given term and its descendant terms
     * @param termId ontology term acc id
     * @param sex sex (null, 'male', or 'female')
     * @return the record ids for a given term and its descendants terms or null if none
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordIdsForTermAndDescendantsCached(String termId, String sex, int speciesTypeKey) throws Exception {

        // validate sex parameter
        String sexParam = null;
        if( termId.startsWith("RS") && sex!=null ) {
            if( sex.equals("male") ) {
                sexParam = "M";
            } else if( sex.equals("female") ) {
                sexParam = "F";
            }
        }

        String recordIdsStr;
        String sql = "SELECT experiment_record_ids FROM phenominer_record_ids WHERE term_acc=? AND species_type_key=?";
        if( sexParam==null ) {
            sql += " AND sex IS NULL";
            recordIdsStr = getStringResult(sql, termId, speciesTypeKey);
        } else {
            sql += " AND sex=?";
            recordIdsStr = getStringResult(sql, termId, speciesTypeKey, sexParam);
        }
        if( recordIdsStr==null ) {
            return null;
        }

        String[] arr = recordIdsStr.split(",");
        List<Integer> recordIds = new ArrayList<>(arr.length);
        for( String recordId: arr ) {
            recordIds.add(Integer.parseInt(recordId));
        }
        return recordIds;
    }


    /**
     * Returns the record ids for a given term only
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the experiment record ids for a given term only
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordIdsForTermOnly(String accId, int speciesTypeKey) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM sample s,experiment_record r\n"+
                    "WHERE r.curation_status=? AND species_type_key=? AND r.sample_id=s.sample_id AND strain_ont_id=?";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM measurement_method m,experiment_record r\n" +
                    "WHERE r.curation_status=? AND species_type_key=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id=?";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM clinical_measurement c,experiment_record r\n" +
                    "WHERE r.curation_status=? AND species_type_key=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id=?";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM experiment_condition x,experiment_record r\n"+
                    "WHERE r.curation_status=? AND species_type_key=? AND x.experiment_record_id=r.experiment_record_id\n"+
                    "AND exp_cond_ont_id=?";
        }
        else {
            // bad ontology
            return null;
        }

        return IntListQuery.execute(this, query, CURATION_STATUS, speciesTypeKey, accId);
    }

    /**
     * Returns the record ids for a given strain term only
     * @param termId ontology term acc id
     * @return the record ids for a given term only
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordIdsForTermOnly(String termId, String sex, int speciesTypeKey) throws Exception {

        if( !(termId.startsWith("RS:") || termId.startsWith("CS:")) ) {
            // ignore sex param for ontology other than 'RS'
            return getRecordIdsForTermOnly(termId, speciesTypeKey);
        }

        // validate sex
        sex = Utils.NVL(sex, "both");
        if( !sex.equals("male") && !sex.equals("female") ) {
            return getRecordIdsForTermOnly(termId, speciesTypeKey);
        }

        // determine kind of term
        String sql = "SELECT DISTINCT r.experiment_record_id FROM sample s,experiment_record r\n"+
            "WHERE r.curation_status=? AND r.species_type_key=? AND r.sample_id=s.sample_id AND sex=? AND strain_ont_id=?";

        return IntListQuery.execute(this, sql, CURATION_STATUS, speciesTypeKey, sex, termId);
    }



    public Map<String, Integer> getRecordCountForTermAndDescendantsByListOfAccdIds(List<String> accIds) throws Exception {
        Map<String, Integer> recordCountMap= new HashMap<>();

        // determine kind of term
        for(String accId: accIds) {
            String query;
            if (accId.startsWith("RS:") || accId.startsWith("CS:")) {
                query = "SELECT COUNT(1) FROM sample s,experiment_record r\n" +
                        "WHERE r.curation_status=? AND r.sample_id=s.sample_id AND strain_ont_id IN(\n";
            } else if (accId.startsWith("MMO:")) {
                query = "SELECT COUNT(1) FROM measurement_method m,experiment_record r\n" +
                        "WHERE r.curation_status=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
            } else if (accId.startsWith("CMO:")) {
                query = "SELECT COUNT(1) FROM clinical_measurement c,experiment_record r\n" +
                        "WHERE r.curation_status=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
            } else if (accId.startsWith("XCO:")) {
                query = "SELECT COUNT(1) FROM experiment_condition x,experiment_record r\n" +
                        "WHERE r.curation_status=? AND x.experiment_record_id=r.experiment_record_id\n" +
                        "AND exp_cond_ont_id IN(\n";
            } else {
                // bad ontology
                return null;
            }

            query += "SELECT ? FROM dual UNION \n" +
                     "SELECT child_term_acc FROM ont_dag\n" +
                     "START WITH parent_term_acc =?\n" +
                     "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

            int count = getCount(query, CURATION_STATUS, accId, accId);
            recordCountMap.put(accId, count);
        }
        return recordCountMap;
    }

    /**
     * Returns ids of all records (annotations) associated with given term
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return list of record ids: ids of annotations (records) for a given term; empty if there are no annotations; null for bad ontology id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getAnnotationsForTerm(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM sample s,experiment_record r "+
                    "WHERE r.curation_status=? AND s.strain_ont_id=? AND r.sample_id=s.sample_id";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM measurement_method m,experiment_record r " +
                    "WHERE r.curation_status=? AND m.measurement_method_ont_id=? AND r.measurement_method_id=m.measurement_method_id";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM clinical_measurement c,experiment_record r " +
                    "WHERE r.curation_status=? AND c.clinical_measurement_ont_id=? AND r.clinical_measurement_id=c.clinical_measurement_id";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT r.experiment_record_id FROM experiment_condition x,experiment_record r "+
                    "WHERE r.curation_status=? AND x.exp_cond_ont_id=? AND x.experiment_record_id=r.experiment_record_id";
        }
        else {
            // bad ontology
            return null;
        }

        return IntListQuery.execute(this, query, CURATION_STATUS, accId);
    }

    /**
     * Returns strain acc ids for all experiments associated with given term
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return list of strain acc ids
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<String> getStrainAnnotationsForTerm(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM sample s,experiment_record r "+
                    "  WHERE strain_ont_id=? AND r.sample_id=s.sample_id)";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM measurement_method m,experiment_record r " +
                    "  WHERE measurement_method_ont_id=? AND r.measurement_method_id=m.measurement_method_id)";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM clinical_measurement c,experiment_record r " +
                    "  WHERE clinical_measurement_ont_id=? AND r.clinical_measurement_id=c.clinical_measurement_id)";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM experiment_condition x,experiment_record r "+
                    "  WHERE exp_cond_ont_id=? AND x.experiment_record_id=r.experiment_record_id)";
        }
        else {
            // bad ontology
            return null;
        }

        return StringListQuery.execute(this, query, CURATION_STATUS, accId);
    }

    /**
     * Returns the ids of records for a given term and its descendant terms
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the ids of records for a given term and its descedants terms; null if ontology is not one of 'RS,'MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getAnnotationsForTermAndDescendants(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT experiment_record_id FROM sample s,experiment_record r\n"+
                    "WHERE r.curation_status=? AND r.sample_id=s.sample_id AND strain_ont_id IN(\n";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT experiment_record_id FROM measurement_method m,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT experiment_record_id FROM clinical_measurement c,experiment_record r\n" +
                    "WHERE r.curation_status=? AND r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT experiment_record_id FROM experiment_condition x,experiment_record r\n"+
                    "WHERE r.curation_status=? AND x.experiment_record_id=r.experiment_record_id\n"+
                        "AND exp_cond_ont_id IN(\n";
        }
        else {
            // bad ontology
            return null;
        }

        query +=
        "SELECT ? FROM dual UNION \n"+
        "SELECT child_term_acc FROM ont_dag\n" +
        "START WITH parent_term_acc=?\n" +
        "CONNECT BY PRIOR child_term_acc=parent_term_acc)";

        return IntListQuery.execute(this, query, CURATION_STATUS, accId, accId);
    }

    /**
     * Returns the strain acc ids for annotations to given term and term descendants
     * @param accId term accession id; must belong to one of the following ontologies: 'RS,'CS','MMO','CMO','XCO'
     * @return the strain ont acc ids of records for a given term and its descendants terms; null if ontology is not one of 'RS,'MMO','CMO','XCO'
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<String> getStrainAnnotationsForTermAndDescendants(String accId) throws Exception {

        // determine kind of term
        String query;
        if( accId.startsWith("RS:") || accId.startsWith("CS:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM sample s,experiment_record r\n"+
                    "  WHERE r.sample_id=s.sample_id AND strain_ont_id IN(\n";
        }
        else if( accId.startsWith("MMO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM measurement_method m,experiment_record r\n" +
                    "  WHERE r.measurement_method_id=m.measurement_method_id AND measurement_method_ont_id IN(\n";
        }
        else if( accId.startsWith("CMO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM clinical_measurement c,experiment_record r\n" +
                    "  WHERE r.clinical_measurement_id=c.clinical_measurement_id AND clinical_measurement_ont_id IN(\n";
        }
        else if( accId.startsWith("XCO:") ) {
            query = "SELECT DISTINCT strain_ont_id FROM experiment_record r,sample s " +
                    "WHERE r.curation_status=? AND s.sample_id=r.sample_id AND experiment_record_id IN(" +
                    "  SELECT experiment_record_id FROM experiment_condition x,experiment_record r\n"+
                    "  WHERE x.experiment_record_id=r.experiment_record_id AND exp_cond_ont_id IN(\n";
        }
        else {
            // bad ontology
            return null;
        }

        query +=
        "SELECT ? FROM dual UNION \n"+
        "SELECT child_term_acc FROM ont_dag\n" +
        "START WITH parent_term_acc=?\n" +
        "CONNECT BY PRIOR child_term_acc=parent_term_acc))";

        return StringListQuery.execute(this, query, CURATION_STATUS, accId, accId);
    }

    /**
     * Returns the record count for a study
     * @param studyId study id
     * @return record count for study
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRecordCountForStudy(int studyId) throws Exception {
        String query = "SELECT COUNT(*) FROM study s, experiment e, experiment_record er \n" +
                "WHERE s.study_id=e.study_id AND e.experiment_id=er.experiment_id AND s.study_id=?";
        return getCount(query, studyId);
    }

    /**
     * Returns list of record count for study, initial load, in progress, final
     * @param studyId study id
     * @return List of record count for study, initial load, in progress, final
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Integer> getRecordStatusCountForStudy(int studyId) throws Exception {
        String query = "SELECT COUNT(*) FROM experiment e, experiment_record er \n" +
                "WHERE e.experiment_id=er.experiment_id AND e.study_id=?\n" +
                "UNION ALL SELECT * FROM (\n" +
                "select count(curation_status) from (select * from PHENOMINER_ENUMERABLES where type=6) pe cross join experiment e left join EXPERIMENT_RECORD er\n" +
                "on pe.VALUE_INT = er.CURATION_STATUS and \n" +
                "er.EXPERIMENT_ID = e.experiment_id and e.study_id=?\n" +
                "group by value_int order by value_int) a";

        return IntListQuery.execute(this, query, studyId, studyId);
    }


    /**
     * Returns a record for a record id
     * @param id record id
     * @return Record object
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Record getRecord(int id) throws Exception {
        String query = "SELECT st.study_id, er.*, s.*, cm.*, mm.* from study st, experiment e, experiment_record er, sample s, clinical_measurement cm, measurement_method mm " +
                "where er.experiment_record_id=? and er.sample_id=s.sample_id and er.clinical_measurement_id=cm.clinical_measurement_id " +
                "and er.measurement_method_id = mm.measurement_method_id and e.experiment_id = er.experiment_id " +
                "and st.study_id=e.study_id ";
                
        RecordQuery sq = new RecordQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();

        List<Record> studies = sq.execute(id);

        if (studies.size() == 0) {
            throw new Exception("Record " + id + " not found");
        }

        Record rec = studies.get(0);
        rec.setConditions(this.getConditions(rec.getId()));
        return rec;
    }

    /**
     * Returns a list of records for an experiment
     *
     * @param experimentId experiment id
     * @return list of Record objects
     * @throws Exception
     */
    public List<Record> getRecords(int experimentId) throws Exception {
        String query = "SELECT st.study_id, st.study_name, er.*, s.*, cm.*, mm.*, e.experiment_name, e.experiment_notes "+
                "FROM study st, experiment e, experiment_record er, sample s, clinical_measurement cm, measurement_method mm " +
                "WHERE er.experiment_id=? AND er.sample_id=s.sample_id AND er.clinical_measurement_id=cm.clinical_measurement_id " +
                " AND er.measurement_method_id = mm.measurement_method_id AND e.experiment_id = er.experiment_id " +
                " AND st.study_id=e.study_id "+
                "ORDER BY er.experiment_record_id DESC";

        RecordQuery q = new RecordQuery(this.getDataSource(), query);
        List<Record> records = execute(q, experimentId);

        for (Record rec: records) {
            rec.setConditions(this.getConditions(rec.getId()));
        }

        return records;
    }

    /**
     * Returns a list of individual records for an experiment record
     *
     * @param expRecordId experiment record id
     * @return list of IndividualRecord objects, possibly empty
     * @throws Exception
     */
    public List<IndividualRecord> getIndividualRecords(int expRecordId) throws Exception {
        String query = "SELECT * FROM experiment_record_ind WHERE experiment_record_id=?";

        IndividualRecordQuery q = new IndividualRecordQuery(this.getDataSource(), query);
        return execute(q, expRecordId);
    }

    /**
     * Returns a list of records for a list of record ids;
     * <p>
     * Note: only first 1000 records are returned, due to Oracle limitations
     * @param ids list of record ids
     * @return list of records
     * @throws Exception
     */
    public List<Record> getRecords(List ids) throws Exception {

        if (ids.size() == 0) {
            return new ArrayList<Record>();
        }
        // oracle internal limitation is 1000 expressions in IN list
        if( ids.size()>1000 ) {
            ids = ids.subList(0, 999);
        }

        String query = "SELECT st.study_id, er.*, s.*, cm.*, mm.* from study st, experiment e, experiment_record er, sample s, clinical_measurement cm, measurement_method mm " +
                "where er.sample_id=s.sample_id and er.clinical_measurement_id=cm.clinical_measurement_id " +
                "and er.measurement_method_id = mm.measurement_method_id and e.experiment_id = er.experiment_id " +
                "and st.study_id=e.study_id and er.experiment_record_id in (";

        for (int i=0; i< ids.size(); i++) {
            if (i > 0) {
                query = query + ",";
            }
            query = query + "?";
        }

        query = query + ")  order by er.experiment_record_id desc";
        RecordQuery sq = new RecordQuery(this.getDataSource(), query);
        Object[] objArray = new Object[ids.size()];

        for (int i=0; i< ids.size(); i++) {
            sq.declareParameter(new SqlParameter(Types.INTEGER));
            objArray[i] = ids.get(i);
        }

        sq.compile();

        return sq.execute(objArray);
    }

    /**
     * delete a sample from the datastore given sample id
     * @param sampleId sample id
     * @return count of rows affected
     * @throws Exception
     */
    public int deleteSample(int sampleId) throws Exception{

        String sql = "DELETE FROM sample WHERE sample_id=?";
        return update(sql, sampleId);
    }

    /**
     * delete a measurement method from the datastore
     * @param mmId
     * @return count of rows affected
     * @throws Exception
     */
    public int deleteMeasurementMethod(int mmId) throws Exception{

        String sql = "DELETE FROM measurement_method WHERE measurement_method_id=?";
        return update(sql, mmId);
    }

    /**
     * delete and experiment condition from the datastore
     * @param ecId
     * @throws Exception
     */
    public void deleteExperimentCondition(int ecId) throws Exception{

        String sql = "delete from experiment_condition where experiment_condition_id=?";
        update(sql, ecId);
    }

    /**
     * delete a list of conditions from the datastore
     * @param conditions
     * @throws Exception
     */
    public void deleteExperimentConditions(List<Condition> conditions) throws Exception{

        for (Condition cond: conditions) {
            this.deleteExperimentCondition(cond.getId());
        }
    }

    /**
     * delete a clinical measurement from the datastore
     * @param cmId
     * @throws Exception
     */
    public void deleteClinicalMeasurement(int cmId) throws Exception{

        String sql = "delete from clinical_measurement where clinical_measurement_id=?";
        update(sql, cmId);
    }

    /**
     * delete individual records
     * @param recordId
     * @throws Exception
     */
    public void deleteIndividualRecords(int recordId) throws Exception{

        String sql = "delete from experiment_record_ind where experiment_record_id=?";
        update(sql, recordId);
    }

    /**
     * delete a record from the datastore
     * @param recordId
     * @throws Exception
     */
    public void deleteRecord(int recordId) throws Exception{

        try {
            this.deleteIndividualRecords(recordId);
        }catch (Exception e) {
            e.printStackTrace();
        }

        Record rec = this.getRecord(recordId);

        String sql = "delete from experiment_record where experiment_record_id=?";
        update(sql, recordId);


        try {
            this.deleteSample(rec.getSampleId());
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.deleteMeasurementMethod(rec.getMeasurementMethodId());
        }catch (Exception e) {
            e.printStackTrace();                
        }
        try {
            this.deleteClinicalMeasurement(rec.getClinicalMeasurementId());
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.deleteExperimentConditions(rec.getConditions());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a clinical measurement in the datastore
     * @param cm
     * @throws Exception
     */
    public void updateClinicalMeasurement(ClinicalMeasurement cm) throws Exception{

        String query = "UPDATE clinical_measurement set clinical_measurement_notes=?, clinical_measurement_ont_id=?, " +
                " formula=?, clinical_meas_average_type=?, CLINICAL_MEAS_SITE_ONT_ID=?, " +
                "CLINICAL_MEASUREMENT_SITE=? where clinical_measurement_id=?";

        update(query, cm.getNotes(), cm.getAccId(), cm.getFormula(), cm.getAverageType(),
                cm.getSiteOntIds(), cm.getSite(), cm.getId());
    }

    /**
     * Update a sample in the datastore
     * @param s
     * @throws Exception
     */
    public void updateSample(Sample s) throws Exception{

        String query = "UPDATE sample SET age_days_from_dob_high_bound=?, age_days_from_dob_low_bound=?, number_of_animals=?, " +
                "sample_notes=?, sex=?, strain_ont_id=?, tissue_ont_id=?, cell_type_ont_id=?, cell_line_id=?, "+
                "geo_sample_acc=?, biosample_id=?, life_stage=?, curator_notes=?, last_modified_by = ?, last_modified_date = SYSTIMESTAMP, " +
                "CULTURE_DUR_VALUE=?, CULTURE_DUR_UNIT=? WHERE sample_id=?";

        update(query, s.getAgeDaysFromHighBound(), s.getAgeDaysFromLowBound(), s.getNumberOfAnimals(), s.getNotes(), s.getSex(),
                s.getStrainAccId(), s.getTissueAccId(), s.getCellTypeAccId(), s.getCellLineId(), s.getGeoSampleAcc(),
                s.getBioSampleId(), s.getLifeStage(), s.getCuratorNotes(),s.getLastModifiedBy(), s.getCultureDur(), s.getCultureDurUnit(), s.getId());
    }

    /**
     * Update a condition in the datastore
     * @param c Condition object
     * @throws Exception
     */
    public void updateCondition(Condition c) throws Exception{

        String query = "UPDATE experiment_condition SET exp_cond_ordinality=?, exp_cond_assoc_units=?, exp_cond_assoc_value_min=?, "+
            "exp_cond_assoc_value_max=?, exp_cond_dur_sec_low_bound=?, exp_cond_dur_sec_high_bound=?, exp_cond_notes=?, "+
            "exp_cond_ont_id=?, exp_cond_application_method=?, experiment_record_id=?, gene_expression_exp_record_id=? " +
            "WHERE experiment_condition_id=?";

        Double valueMin = null;
        if( c.getValueMin() == null ) {
            if (c.getValueMax() != null) {
                valueMin = Double.parseDouble(c.getValueMax());
            }
        }else {
            valueMin = Double.parseDouble(c.getValueMin());
        }

        Double valueMax = null;
        if (c.getValueMax() == null) {
            if (valueMin != null) {
                valueMax = valueMin;
            }
        }else {
            valueMax = Double.parseDouble(c.getValueMax());
        }

        Integer recId = c.getExperimentRecordId()==0 ? null : c.getExperimentRecordId();
        Integer expRecId = c.getGeneExpressionRecordId()==0 ? null : c.getGeneExpressionRecordId();

        update(query, c.getOrdinality(), c.getUnits(),valueMin, valueMax, c.getDurationLowerBound(), c.getDurationUpperBound(),
                c.getNotes(), c.getOntologyId(), c.getApplicationMethod(), recId, expRecId, c.getId());
    }

    /**
     * Update a measurement method in the datastore
     * @param mm MeasurementMethod object
     * @throws Exception
     */
    public void updateMeasurementMethod(MeasurementMethod mm) throws Exception{

        Integer recId = mm.getExperimentRecordId()==0 ? null : mm.getExperimentRecordId();
        Integer expRecId = mm.getGeneExpressionRecordId()==0 ? null : mm.getGeneExpressionRecordId();

        String query = "UPDATE measurement_method SET measurement_duration_in_secs=?, measurement_method_notes=?, " +
                "measurement_method_ont_id=?, measurement_site=?, measurement_site_ont_ids=?, measurement_method_pi_type=?, " +
                "meas_method_pi_time_value=?, meas_method_pi_time_unit=?, measurement_method_type=?, "+
                "experiment_record_id=?, gene_expression_exp_record_id=? WHERE measurement_method_id=?";

        update(query, mm.getDuration(), mm.getNotes(), mm.getAccId(), mm.getSite(), mm.getSiteOntIds(), mm.getPiType(),
                mm.getPiTimeValue(), mm.getPiTypeUnit(), mm.getType(), recId, expRecId, mm.getId());
    }

    /**
     * Insert a clinical measurement in the datastore
     * @param cm ClinicalMeasurement object
     * @return clinical_measurement_id, as retrieved from clinical_measurement_seq
     * @throws Exception
     */
    public int insertClinicalMeasurement(ClinicalMeasurement cm) throws Exception{
        int next = this.getNextKey("clinical_measurement_seq");
		cm.setId(next);

        String query = "INSERT INTO clinical_measurement (clinical_measurement_notes, " +
                " clinical_measurement_ont_id, formula, clinical_meas_average_type, " +
                "clinical_measurement_id, clinical_meas_site_ont_id, clinical_measurement_site, class) "+
				"VALUES(?,?,?,?,?,?,?,'edu.mcw.rgd.phenodb.ClinicalMeasurement')";

        update(query, cm.getNotes(), cm.getAccId(), cm.getFormula(), cm.getAverageType(),
                next, cm.getSiteOntIds(), cm.getSite());

        return next;
    }

    /**
     * Insert a measurement method in the datastore
     * @param mm MeasurementMethod object
     * @return key generated from sequence MEASUREMENT_METHOD_SEQ
     * @throws Exception
     */
    public int insertMeasurementMethod(MeasurementMethod mm) throws Exception{
        int next = this.getNextKey("measurement_method_seq");
        mm.setId(next);

        Integer recId = mm.getExperimentRecordId()==0 ? null : mm.getExperimentRecordId();
        Integer expRecId = mm.getGeneExpressionRecordId()==0 ? null : mm.getGeneExpressionRecordId();

        String query = "INSERT INTO measurement_method (measurement_duration_in_secs, measurement_method_notes, " +
                "measurement_method_ont_id, measurement_site, measurement_site_ont_ids, measurement_method_pi_type, " +
                "meas_method_pi_time_value, meas_method_pi_time_unit, measurement_method_type, experiment_record_id, "+
                "gene_expression_exp_record_id, measurement_method_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        update(query, mm.getDuration(), mm.getNotes(), mm.getAccId(), mm.getSite(), mm.getSiteOntIds(), mm.getPiType(),
                mm.getPiTimeValue(), mm.getPiTypeUnit(), mm.getType(), recId, expRecId, next);
        return next;
    }

    /**
     * Return a sample based on an sample ID
     * @param id
     * @return
     * @throws Exception
     */
    public Sample getSample(int  id) throws Exception {
        String query = "SELECT * from sample where sample_id=?";

        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();

        List<Sample> samples = sq.execute(id);
        return samples.get(0);
    }

    public List<Sample> getSamplesByIds(List<String> ids) throws Exception {
        String query = "select * from sample where sample_id in ("+Utils.buildInPhrase(ids)+")";
        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(), query);
        sq.compile();
        return sq.execute();
    }

    public List<Sample> getSamplesByStudyId(int studyId) throws Exception {
        String query = "select s.* from sample s where s.sample_id in (" +
                        "select r.sample_id from experiment_record r where r.experiment_id in (" +
                        "select experiment_id from experiment where study_id=?) )";
        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(),query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();
        return sq.execute(studyId);
    }

    public List<Sample> getSamplesByExperimentId(int experimentId) throws Exception {
        String query = "select s.* from sample s where s.sample_id in (" +
                "select r.sample_id from experiment_record r where r.experiment_id=? )";
        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(),query);
        sq.declareParameter(new SqlParameter(Types.INTEGER));
        sq.compile();
        return sq.execute(experimentId);
    }

    /**
     * Return a sample based on an Geo Sample ID
     * @param id
     * @return
     * @throws Exception
     */
    public Sample getSampleByGeoId(String  id) throws Exception {
        String query = "SELECT * from sample where geo_sample_acc=?";

        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.VARCHAR));
        sq.compile();

        List<Sample> samples = sq.execute(id);
        if(samples.size() != 0)
            return samples.get(0);
        else return null;
    }
    public List<Sample> getSampleByGeoStudyId(String id) throws Exception{
        String query = "select * from sample where geo_sample_acc in (select SAMPLE_ACCESSION_ID from rna_seq where geo_accession_id=?)";
        PhenoSampleQuery sq = new PhenoSampleQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.VARCHAR));
        sq.compile();
        return sq.execute(id);
    }
    /**
     * Return geo records based on an Geo  ID
     * @param geoId
     * @return
     * @throws Exception
     */
    public List<GeoRecord> getGeoRecords(String  geoId,String species) throws Exception {
        String query = "SELECT * from rna_seq where geo_accession_id=? and sample_organism like ?";

        GeoRecordQuery sq = new GeoRecordQuery(this.getDataSource(), query);
        sq.declareParameter(new SqlParameter(Types.VARCHAR));
        sq.declareParameter(new SqlParameter(Types.VARCHAR));
        sq.compile();

        List<GeoRecord> records = sq.execute(geoId,species+"%");
        return records;
    }



    /**
     * Insert a sample in the datastore
     * @param s Sample object
     * @return just generated sample key
     * @throws Exception
     */
    public int insertSample(Sample s) throws Exception{
        int next = this.getNextKey("sample_seq");
        s.setId(next);

        String query = "INSERT INTO sample (age_days_from_dob_high_bound, age_days_from_dob_low_bound, " +
                "number_of_animals, sample_notes, sex, strain_ont_id, tissue_ont_id, cell_type_ont_id, "+
                "cell_line_id, geo_sample_acc, biosample_id, sample_id,life_stage,CURATOR_NOTES,last_modified_by,created_by, " +
                "CULTURE_DUR_VALUE, CULTURE_DUR_UNIT,created_date, last_modified_date) "+
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,SYSTIMESTAMP)";

        update(query, s.getAgeDaysFromHighBound(), s.getAgeDaysFromLowBound(), s.getNumberOfAnimals(), s.getNotes(),
                s.getSex(), s.getStrainAccId(), s.getTissueAccId(), s.getCellTypeAccId(), s.getCellLineId(),
                s.getGeoSampleAcc(), s.getBioSampleId(), next,s.getLifeStage(),s.getCuratorNotes(),s.getLastModifiedBy(),s.getCreatedBy(),
                s.getCultureDur(), s.getCultureDurUnit());
        return next;
    }

    /**
     * Update samples life stage column
     * @param samples list of samples
     * @throws Exception
     */
    public void updateSampleLifeStageBatch(List<Sample> samples) throws Exception{
        String query = "update sample set life_stage=? where sample_id=?";
        BatchSqlUpdate sql =new BatchSqlUpdate(this.getDataSource(), query, new int[]{Types.VARCHAR,Types.INTEGER}, 1000);
        sql.compile();
        for (Sample s : samples){
            sql.update(s.getLifeStage(), s.getId());
        }
        sql.flush();
    }

    /**
     * Insert a condition in the datastore
     * @param c Condition object
     * @throws Exception
     */
    public int insertCondition(Condition c) throws Exception{

        int next = this.getNextKey("experiment_condition_seq");
        c.setId(next);

        String query = "INSERT INTO experiment_condition (exp_cond_ordinality, exp_cond_assoc_units, exp_cond_assoc_value_min, "+
            "exp_cond_assoc_value_max, exp_cond_dur_sec_low_bound, exp_cond_dur_sec_high_bound, exp_cond_notes, exp_cond_ont_id, "+
            "exp_cond_application_method, experiment_condition_id, experiment_record_id, gene_expression_exp_record_id) "+
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        Double valueMin = null;
        if( c.getValueMin() == null ) {
            if (c.getValueMax() != null) {
                valueMin = Double.parseDouble(c.getValueMax());
            }
        }else {
            valueMin = Double.parseDouble(c.getValueMin());
        }

        Double valueMax = null;
        if (c.getValueMax() == null) {
            if (valueMin != null) {
                valueMax = valueMin;
            }
        }else {
            valueMax = Double.parseDouble(c.getValueMax());
        }

        Integer recId = c.getExperimentRecordId()==0 ? null : c.getExperimentRecordId();
        Integer expRecId = c.getGeneExpressionRecordId()==0 ? null : c.getGeneExpressionRecordId();

        update( query, c.getOrdinality(), c.getUnits(), valueMin, valueMax, c.getDurationLowerBound(), c.getDurationUpperBound(),
            c.getNotes(), c.getOntologyId(), c.getApplicationMethod(), next, recId, expRecId);

        return next;
    }

    /**
     * Inserts a record in the datastore; return experiment_record_id assigned automatically during insert
     * @param r Record object
     * @return experiment_record_id assigned automatically during insert
     * @throws Exception
     */
    public int insertRecord(Record r) throws Exception{

        int expRecId = this.getNextKey("experiment_record_seq");
        r.setId(expRecId);

        r.setClinicalMeasurementId(this.insertClinicalMeasurement(r.getClinicalMeasurement()));
        r.setMeasurementMethodId(this.insertMeasurementMethod(r.getMeasurementMethod()));
        r.setSampleId(this.insertSample(r.getSample()));

        String query = "INSERT INTO experiment_record (clinical_measurement_id, experiment_id, " +
                "curation_status, last_modified_date, measurement_method_id, sample_id, measurement_sd, measurement_sem, " +
                "measurement_units, measurement_value, measurement_error,experiment_record_id, class, has_individual_record, "+
                "species_type_key,last_modified_by,created_by,created_date) VALUES( " +
                "?,?,?,SYSTIMESTAMP,?,?,?,?,?,?,?,?, 'edu.mcw.rgd.phenodb.QuantExperimentRecord', ?,?,?,?,SYSTIMESTAMP)";

        int hasIndividualRecord = r.getHasIndividualRecord() ? 1 : 0;

        // currently we recognize only CS and RS ontologies for species; 'RS' is default
        int speciesTypeKey = SpeciesType.RAT;
        String strainAccId = Utils.NVL(r.getSample().getStrainAccId(), "RS");
        if( strainAccId.startsWith("CS") ) {
            speciesTypeKey = SpeciesType.CHINCHILLA;
        }

        update(query, r.getClinicalMeasurementId(), r.getExperimentId(), r.getCurationStatus(),
                r.getMeasurementMethodId(), r.getSampleId(), r.getMeasurementSD(), r.getMeasurementSem(),
                r.getMeasurementUnits(), r.getMeasurementValue(), r.getMeasurementError(), expRecId, hasIndividualRecord,
                speciesTypeKey,r.getLastModifiedBy(),r.getCreatedBy());

        // conditions must be added after the experiment record was inserted, due to referential integrity constraints
        for (Condition cond: r.getConditions()) {
            cond.setExperimentRecordId(expRecId);
            this.insertCondition(cond);
        }

        return expRecId;
    }

    /**
     * Inserts an individual record in the datastore;
     * @param irec an IndividualRecord object
     * @return
     * @throws Exception
     */
    public void insertIndividualRecord (IndividualRecord irec) throws Exception {
        int next = this.getNextKey("EXPERIMENT_RECORD_IND_SEQ");

        String query = "insert into experiment_record_ind (experiment_record_ind_id, experiment_record_id, animal_id, measurement_value) values ( " +
                "?,?,?,?) ";

        update(query, next, irec.getRecordId(), irec.getAnimalId(), Double.parseDouble(irec.getMeasurementValue()));
    }

    /**
     * Updates a record in the datastore
     * @param r
     * @throws Exception
     */
    public void updateRecord(Record r) throws Exception{

        this.updateClinicalMeasurement(r.getClinicalMeasurement());
        this.updateMeasurementMethod(r.getMeasurementMethod());
        this.updateSample(r.getSample());

        for (Condition cond: r.getConditions()) {

            if (cond.getId() > 0) {
                this.updateCondition(cond);
            }else {
                cond.setExperimentRecordId(r.getId());
                this.insertCondition(cond);
            }
        }

        String query = "UPDATE experiment_record SET clinical_measurement_id=?, experiment_id=?, curation_status=?, " +
                "last_modified_date=SYSTIMESTAMP, measurement_method_id=?, sample_id=?, measurement_sd=?, measurement_sem=?, " +
                "measurement_units=?, measurement_value=?, measurement_error=?,last_modified_by=? WHERE experiment_record_id=?";

        update(query, r.getClinicalMeasurementId(), r.getExperimentId(), r.getCurationStatus(),
                r.getMeasurementMethodId(), r.getSampleId(), r.getMeasurementSD(), r.getMeasurementSem(),
                r.getMeasurementUnits(), r.getMeasurementValue(),r.getMeasurementError(),r.getLastModifiedBy(), r.getId());
    }

    /**
     * Returns a list of conditions based on a experiment record id
     * @param experimentRecordId experiment record id
     * @return list of Condition objects; could be empty
     * @throws Exception
     */
    public List<Condition> getConditions(int experimentRecordId) throws Exception {
        String query = "SELECT * FROM experiment_condition ec WHERE experiment_record_id=? "+
                "ORDER BY ec.exp_cond_ordinality, ec.experiment_condition_id";

        ConditionQuery q = new ConditionQuery(this.getDataSource(), query);
        return execute(q, experimentRecordId);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIds(SearchBean sb, int curationStatus) throws Exception {
        return this.getRecordIds(sb,-1,-1, curationStatus);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIdsForReport(SearchBean sb) throws Exception {
        return this.getRecordIdsForReport(sb, CURATION_STATUS);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @param curationStatus
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIdsForReport(SearchBean sb, int curationStatus) throws Exception {
        return this.getRecordIdsForReport(sb,-1,-1, curationStatus);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @param studyId
     * @param experimentId
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIds(SearchBean sb, int studyId, int experimentId, int curationStatus) throws Exception {
        return this.getRecordIds(sb, studyId, experimentId, -1, curationStatus, false);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @param studyId
     * @param experimentId
     * @param curationStatus
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIdsForReport(SearchBean sb, int studyId, int experimentId, int curationStatus) throws Exception {
        return this.getRecordIdsForReport(sb, studyId, experimentId, -1, curationStatus);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @param studyId
     * @param experimentId
     * @param recordId
     * @param curationStatus
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIds(SearchBean sb, int studyId, int experimentId, int recordId, int curationStatus) throws Exception {
        return getRecordIds(sb, studyId, experimentId, recordId, curationStatus, false);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb
     * @param studyId
     * @param experimentId
     * @param recordId
     * @param curationStatus
     * @return
     * @throws Exception
     */
    public List<Integer> getRecordIdsForReport(SearchBean sb, int studyId, int experimentId, int recordId, int curationStatus) throws Exception {
        return this.getRecordIds(sb, studyId, experimentId, recordId, curationStatus, true);
    }

    /**
     * Returns a list of record ids for a search bean
     * @param sb SearchBean object
     * @param studyId study id
     * @param experimentId experiment id
     * @param recordId record id
     * @param curationStatus curation status
     * @param isForReport is for report
     * @return list of record ids
     * @throws Exception
     */
    public List<Integer> getRecordIds(SearchBean sb, int studyId, int experimentId, int recordId, int curationStatus, boolean isForReport) throws Exception {
        StringBuilder query = new StringBuilder("SELECT DISTINCT(er1.experiment_record_id) FROM study st \n" +
            "LEFT JOIN experiment e ON st.study_id = e.study_id \n" +
            "LEFT JOIN \n" +
            "(\n" +
            "SELECT er.experiment_id, er.measurement_error,  er.measurement_sd, er.measurement_sem, \n" +
            "er.measurement_units, er.measurement_value, er.curation_status, er.has_individual_record, \n" +
            "mm.measurement_method_id, mm.measurement_method_ont_id, mm.measurement_duration_in_secs, mm.measurement_site, " +
            "s1.*, cm.*, ec.*\n" +
            "FROM " + (isForReport ? "experiment_record_view" : "experiment_record") + " er, sample s1, clinical_measurement cm, measurement_method mm, " +
            " experiment_condition ec\n" +
            "WHERE er.sample_id = s1.sample_id AND er.clinical_measurement_id = cm.clinical_measurement_id\n" +
            " AND er.measurement_method_id = mm.measurement_method_id AND er.experiment_record_id=ec.experiment_record_id\n" +
            ")er1 \n" +
            "ON e.experiment_id = er1.experiment_id\n" +
            "WHERE NOT er1.experiment_record_id IS NULL ");

        if (recordId != -1) {
            query.append(" and er1.experiment_record_id=").append(recordId).append(" ");
        }

        if (experimentId != -1) {
            query.append(" and e.experiment_id=").append(experimentId).append(" ");
        }

        if (studyId != -1) {
            query.append(" and st.study_id=").append(studyId).append(" ");
        }

        if (curationStatus != -1) {
            query.append(" and er1.curation_status=").append(curationStatus).append(" ");
        }

        query.append(this.buildLike(sb.getStudyName(), "study_name"));
        query.append(this.buildStrEqual(sb.getStudySource(), "study_source"));
        query.append(this.buildStrEqual(sb.getStudyType(), "study_type"));

        query.append(this.buildNumberEqual(sb.getReference(), "ref_rgd_id"));
        query.append(this.buildLike(sb.getExperimentName(), "experiment_name"));
        query.append(this.buildStrEqual(sb.getCmAccId(), "clinical_measurement_ont_id"));
        query.append(this.buildNumberEqual(sb.getCmValue(), "measurement_value"));
        query.append(this.buildLike(sb.getCmUnits(), "measurement_units"));
        query.append(this.buildNumberEqual(sb.getCmSD(), "measurement_sd"));
        query.append(this.buildNumberEqual(sb.getCmSEM(), "measurement_sem"));
        query.append(this.buildStrEqual(sb.getCmError(), "measurement_error"));
        query.append(this.buildLike(sb.getCmFormula(), "formula"));
        query.append(this.buildLike(sb.getCmAveType(), "clinical_meas_average_type"));

        query.append(this.buildStrEqual(sb.getMmAccId(), "measurement_method_ont_id"));
        query.append(this.buildNumberEqual(sb.getMmDuration(), "measurement_duration_in_secs"));
        query.append(this.buildLike(sb.getMmSite(), "measurement_site"));
        query.append(this.buildLike(sb.getMmPIType(), "measurement_method_pi_type"));
        query.append(this.buildNumberEqual(sb.getMmPITime(), "meas_method_pi_time_value"));
        query.append(this.buildLike(sb.getMmPIUnit(), "meas_method_pi_time_unit"));

        query.append(this.buildStrEqual(sb.getSAccId(), "strain_ont_id"));
        query.append(this.buildNumberEqual(sb.getSAnimalCount(), "number_of_animals"));
        query.append(this.buildNumberEqual(sb.getSMinAge(), "age_days_from_dob_low_bound"));
        query.append(this.buildNumberEqual(sb.getSMaxAge(), "age_days_from_dob_high_bound"));
        query.append(this.buildStrEqual(sb.getSSex(), "sex"));

        if (sb.conditionSet()) {

            if (sb.getCAccId() != null) {
                query.append(this.buildStrEqual(sb.getCAccId(), "exp_cond_ont_id"));
            }
            if (sb.getCValueMin() != null) {
                query.append(this.buildNumberEqual(sb.getCValueMin(), "exp_cond_assoc_value_min"));
            }

            if (sb.getCValueMax() != null) {
                query.append(this.buildNumberEqual(sb.getCValueMax(), "exp_cond_assoc_value_max"));
            }

            if (sb.getCUnits() != null) {
                query.append(this.buildLike(sb.getCUnits(), "exp_cond_assoc_units"));
            }
            if (sb.getCapplicationMethod() != null) {
                query.append(this.buildLike(sb.getCapplicationMethod(), "exp_cond_application_method"));
            }
            if (sb.getCMaxDuration() != null) {
                query.append(this.buildStrEqual(sb.getCMaxDuration(), "exp_cond_dur_sec_low_bound"));
            }
            if (sb.getCMinDuration() != null ) {
                query.append(this.buildStrEqual(sb.getCMinDuration(), "exp_cond_dur_sec_high_bound"));
            }
            if (sb.getCordinality() != null ) {
                query.append(this.buildNumberEqual(sb.getCordinality(), "exp_cond_ordinality"));
            }
        }

        return IntListQuery.execute(this, query.toString());
    }

    public List getStudyIds(SearchBean sb) throws Exception {
        return getStudyIds(sb, -1, -1, -1);
    }

    /**
     * Returns a list of study ids for a search bean
     * @param sb
     * @return
     * @throws Exception
     */
    public List getStudyIds(SearchBean sb, int studyId, int experimentId, int recordId) throws Exception {
        //get record ids for condition

        String query = "select distinct(st.study_id) from study st \n" +
                "left join experiment e on \n" +
                "st.study_id = e.study_id \n" +
                "left join \n" +
                "(\n" +
                "SELECT er.experiment_id, er.measurement_error, er.last_modified_date,\n" +
                "er.measurement_sd, er.measurement_sem, er.measurement_units, er.measurement_value,\n" +
                "er.curation_status, er.has_individual_record, er.last_modified_by,\n" +
                "s1.sample_id, cm.clinical_measurement_id, mm.measurement_method_id, ec.experiment_record_id\n" +
                "from experiment_record er, sample s1, clinical_measurement cm, measurement_method mm, \n" +
                " experiment_condition ec\n" +
                "WHERE er.sample_id = s1.sample_id\n" +
                "and er.clinical_measurement_id = cm.clinical_measurement_id\n" +
                "and er.measurement_method_id = mm.measurement_method_id\n" +
                " AND er.experiment_record_id=ec.experiment_record_id) er1 \n" +
                "on e.experiment_id = er1.experiment_id\n" +
                "where \n" +
                "1=1 ";

        if (recordId != -1) {
            query += " and er1.experiment_record_id=" + recordId + " ";
        }

        if (experimentId != -1) {
            query = query + " and e.experiment_id=" + experimentId + " ";
        }

        if (studyId != -1) {
            query += " and st.study_id=" + studyId + " ";
        }

        query = query + this.buildLike(sb.getStudyName(),"study_name");
        query = query + this.buildStrEqual(sb.getStudySource(),"study_source");
        query = query + this.buildStrEqual(sb.getStudyType(),"study_type");

        query = query + this.buildNumberEqual(sb.getReference(),"ref_rgd_id");
        query = query + this.buildLike(sb.getExperimentName(),"experiment_name");
        query = query + this.buildStrEqual(sb.getCmAccId(),"clinical_measurement_ont_id");
        query = query + this.buildNumberEqual(sb.getCmValue(),"measurement_value");
        query = query + this.buildLike(sb.getCmUnits(),"measurement_units");
        query = query + this.buildNumberEqual(sb.getCmSD(),"measurement_sd");
        query = query + this.buildNumberEqual(sb.getCmSEM(),"measurement_sem");
        query = query + this.buildStrEqual(sb.getCmError(),"measurement_error");
        query = query + this.buildLike(sb.getCmFormula(),"formula");
        query = query + this.buildLike(sb.getCmAveType(),"clinical_meas_average_type");

        query = query + this.buildStrEqual(sb.getMmAccId(),"measurement_method_ont_id");
        query = query + this.buildNumberEqual(sb.getMmDuration(),"measurement_duration_in_secs");
        query = query + this.buildLike(sb.getMmSite(),"measurement_site");
        query = query + this.buildLike(sb.getMmPIType(),"measurement_method_pi_type");
        query = query + this.buildNumberEqual(sb.getMmPITime(),"meas_method_pi_time_value");
        query = query + this.buildLike(sb.getMmPIUnit(),"meas_method_pi_time_unit");

        query = query + this.buildStrEqual(sb.getSAccId(),"strain_ont_id");
        query = query + this.buildNumberEqual(sb.getSAnimalCount(),"number_of_animals");
        query = query + this.buildNumberEqual(sb.getSMinAge(),"age_days_from_dob_low_bound");
        query = query + this.buildNumberEqual(sb.getSMaxAge(),"age_days_from_dob_high_bound");
        query = query + this.buildStrEqual(sb.getSSex(),"sex");

        if (sb.conditionSet()) {

            if (sb.getCAccId() != null) {
                query = query + this.buildStrEqual(sb.getCAccId(), "exp_cond_ont_id");
            }
            if (sb.getCValueMin() != null) {
                query = query + this.buildNumberEqual(sb.getCValueMin(), "exp_cond_assoc_value_min");
            }

            if (sb.getCValueMax() != null) {
                query = query + this.buildNumberEqual(sb.getCValueMax(), "exp_cond_assoc_value_max");
            }

            if (sb.getCUnits() != null) {
                query = query + this.buildLike(sb.getCUnits(), "exp_cond_assoc_units");
            }
            if (sb.getCapplicationMethod() != null) {
                query = query + this.buildLike(sb.getCapplicationMethod(), "exp_cond_application_method");
            }
            if (sb.getCMaxDuration() != null) {
                query = query + this.buildStrEqual(sb.getCMaxDuration(), "exp_cond_dur_sec_low_bound");
            }
            if (sb.getCMinDuration() != null ) {
                query = query + this.buildStrEqual(sb.getCMinDuration(), "exp_cond_dur_sec_high_bound");
            }
            if (sb.getCordinality() != null ) {
                query = query + this.buildNumberEqual(sb.getCordinality(), "exp_cond_ordinality");
            }
        }
        
        return IntListQuery.execute(this, query);
    }

    /**
     * Returns a list of experiment ids based on a search bean.
     * @param sb
     * @return
     * @throws Exception
     */
    public List getExperimentIds(SearchBean sb) throws Exception {
        return this.getExperimentIds(sb,-1);
    }

    /**
     * Retunrs a list fo experiment ids based on parameterized search bean
     * @param sb
     * @param experimentId
     * @return
     * @throws Exception
     */
    public List getExperimentIds(SearchBean sb, int experimentId) throws Exception {
        return this.getExperimentIds(sb, -1, experimentId, -1);
    }

    /**
     * Retunrs a list fo experiment ids based on parameterized search bean
     * @param sb
     * @param studyId
     * @param experimentId
     * @return
     * @throws Exception
     */
    public List getExperimentIds(SearchBean sb, int studyId, int experimentId, int recordId) throws Exception {

        String query = "select distinct(e.experiment_id) from study st \n" +
                "left join experiment e on \n" +
                "st.study_id = e.study_id \n" +
                "left join \n" +
                "(\n" +
                "SELECT er.experiment_id, er.measurement_error,\n" +
                "er.measurement_sd, er.measurement_sem, er.measurement_units, er.measurement_value,\n" +
                "er.curation_status, er.has_individual_record,\n" +
                "s1.*, cm.*, mm.*, ec.*\n" +
                "from experiment_record er, sample s1, clinical_measurement cm, measurement_method mm, \n" +
                "experiment_condition ec\n" +
                "WHERE er.sample_id = s1.sample_id\n" +
                "and er.clinical_measurement_id = cm.clinical_measurement_id\n" +
                "and er.measurement_method_id = mm.measurement_method_id\n" +
                "and er.experiment_record_id=ec.experiment_record_id) er1 \n" +
                "on e.experiment_id = er1.experiment_id\n" +
                "where \n" +
                "not e.experiment_id is null ";

        if (recordId != -1) {
            query += " and er1.experiment_record_id=" + recordId + " ";
        }

        if (experimentId != -1) {
            query = query + " and e.experiment_id=" + experimentId + " ";
        }

        if (studyId != -1) {
            query += " and st.study_id=" + studyId + " ";
        }

        query = query + this.buildLike(sb.getStudyName(),"study_name");
        query = query + this.buildStrEqual(sb.getStudySource(),"study_source");
        query = query + this.buildStrEqual(sb.getStudyType(),"study_type");

        query = query + this.buildNumberEqual(sb.getReference(),"ref_rgd_id");
        query = query + this.buildLike(sb.getExperimentName(),"experiment_name");
        query = query + this.buildStrEqual(sb.getCmAccId(),"clinical_measurement_ont_id");
        query = query + this.buildNumberEqual(sb.getCmValue(),"measurement_value");
        query = query + this.buildLike(sb.getCmUnits(),"measurement_units");
        query = query + this.buildNumberEqual(sb.getCmSD(),"measurement_sd");
        query = query + this.buildNumberEqual(sb.getCmSEM(),"measurement_sem");
        query = query + this.buildStrEqual(sb.getCmError(),"measurement_error");
        query = query + this.buildLike(sb.getCmFormula(),"formula");
        query = query + this.buildLike(sb.getCmAveType(),"clinical_meas_average_type");

        query = query + this.buildStrEqual(sb.getMmAccId(),"measurement_method_ont_id");
        query = query + this.buildNumberEqual(sb.getMmDuration(),"measurement_duration_in_secs");
        query = query + this.buildLike(sb.getMmSite(),"measurement_site");
        query = query + this.buildLike(sb.getMmPIType(),"measurement_method_pi_type");
        query = query + this.buildNumberEqual(sb.getMmPITime(),"meas_method_pi_time_value");
        query = query + this.buildLike(sb.getMmPIUnit(),"meas_method_pi_time_unit");

        query = query + this.buildStrEqual(sb.getSAccId(),"strain_ont_id");
        query = query + this.buildNumberEqual(sb.getSAnimalCount(),"number_of_animals");
        query = query + this.buildNumberEqual(sb.getSMinAge(),"age_days_from_dob_low_bound");
        query = query + this.buildNumberEqual(sb.getSMaxAge(),"age_days_from_dob_high_bound");
        query = query + this.buildStrEqual(sb.getSSex(),"sex");

        if (sb.conditionSet()) {

            if (sb.getCAccId() != null) {
                query = query + this.buildStrEqual(sb.getCAccId(), "exp_cond_ont_id");
            }
            if (sb.getCValueMin() != null) {
                query = query + this.buildNumberEqual(sb.getCValueMin(), "exp_cond_assoc_value_min");
            }

            if (sb.getCValueMax() != null) {
                query = query + this.buildNumberEqual(sb.getCValueMax(), "exp_cond_assoc_value_max");
            }

            if (sb.getCUnits() != null) {
                query = query + this.buildLike(sb.getCUnits(), "exp_cond_assoc_units");
            }
            if (sb.getCapplicationMethod() != null) {
                query = query + this.buildLike(sb.getCapplicationMethod(), "exp_cond_application_method");
            }
            if (sb.getCMaxDuration() != null) {
                query = query + this.buildStrEqual(sb.getCMaxDuration(), "exp_cond_dur_sec_low_bound");
            }
            if (sb.getCMinDuration() != null ) {
                query = query + this.buildStrEqual(sb.getCMinDuration(), "exp_cond_dur_sec_high_bound");
            }
            if (sb.getCordinality() != null ) {
                query = query + this.buildNumberEqual(sb.getCordinality(), "exp_cond_ordinality");
            }
        }

        return IntListQuery.execute(this, query);
    }

    /**
     * Get ordinality counts of records
     * @param recordIDs
     * @return
     * @throws Exception
     */
    public List<HistogramRecord> getOrdCounts(List<String> recordIDs) throws Exception {

        if (recordIDs != null && recordIDs.size() > 0) {
            String IDs = Utils.concatenate(recordIDs, ",");

            String query = "SELECT ord_counts, COUNT(*) FROM ( " +
                    "SELECT COUNT(*) AS ord_counts " +
                    "FROM experiment_condition ec, experiment_record er " +
                    "WHERE " +
                    "er.experiment_record_id in (" + IDs + ") " +
                    "AND ec.experiment_record_id = er.experiment_record_id " +
                    "group by er.experiment_record_id) a " +
                    "group by a.ord_counts " +
                    "order by a.ord_counts desc";

            HistogramQuery sq = new HistogramQuery(this.getDataSource(), query);
            sq.compile();

            return sq.execute();
        } else {
            return null;
        }
    }
    /**
     * Insert a unit in the data store;
     * @throws Exception
     */
    public int insertEnumerable(Enumerable e) throws Exception{

        String sql = "INSERT INTO  phenominer_enumerables (type,label,value,description,onto_id,value_int) VALUES(?,?,?,?,?,?)";

        return update(sql,e.getType(),e.getLabel(),e.getValue(),e.getDescription(),e.getOntId(),e.getValueInt());
    }
    /**
     * Returns the label of the enumerable given its type and int value.
     * @param type
     * @param value
     * @return String of the lable
     */
    public String getEnumerableLabel(int type, int value) {
        String sql = "SELECT label FROM phenominer_enumerables WHERE type=? AND value_int=?";

        try {
            List<String> enumerableLabels = StringListQuery.execute(this, sql, type, value);
            if( enumerableLabels.isEmpty() ) {
                return "";
            }
            return enumerableLabels.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the enumerable values of a given type
     * @param type
     * @param type of the value. Default: Int value; 1: String value;
     * @return Map<String, String> of the label and
     */
    public Map<String, String> getEnumerableMap(int type, int valueType, boolean addEmpty) {
        String sql;
        switch (valueType)
        {
            case 1: // Sting value
                sql = "select value, label from phenominer_enumerables where type = ? order by value";
                break;
            default: // Int value
                sql = "select value_int, label from phenominer_enumerables where type = ? order by value_int";
                break;
        }

        try {
            StringMapQuery sq = new StringMapQuery(this.getDataSource(), sql);
            sq.declareParameter(new SqlParameter(Types.NUMERIC));
            sq.compile();

            List<StringMapQuery.MapPair> enumerables = sq.execute(new Object[]{type});
            if( enumerables.isEmpty() ) {
                return null;
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            if (addEmpty) map.put("", "");
            for (StringMapQuery.MapPair pair : enumerables) {
                 map.put(pair.keyValue, pair.stringValue);
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    private String buildLike(String value, String column) {
        if (value != null) {
            return " and lower(" + column + ") like '%" + value.toLowerCase() + "%' ";
        }
        return "";
    }
    private String buildStrEqual(String value, String column) {
        if (value != null) {
            return " and lower(" + column + ") = '" + value.toLowerCase() + "' ";
        }
        return "";
    }

    private String buildNumberEqual(String value, String column) {
        if (value != null) {
            return " and " + column + "=" + value.toLowerCase() + " ";
        }
        return "";
    }

    public List<HeatMapRecord> getHeatMap(String colIDs, String rowIDs, char sex) throws Exception {
        String sexCondition = " AND pai3.ONT_ID IN (";
        switch (sex) {
            case 'f':
                sexCondition += "'SEX:0000001') ";
                break;
            case 'm':
                sexCondition += "'SEX:0000002') ";
                break;
            case 'b':
            case 'u':
                sexCondition = " ";
                break;
        }

        String query = "select ot2.term as row_Term, ot1.term as col_TERM, v.heat_level  from \n" +
                "                (\n" +
                "                select * from \n" +
                "                ( \n" +
                "                select bb.column_value as col_term, dd.column_value as row_term from \n" +
                "                table(sys.ODCIVarchar2List("+ rowIDs +")) dd, \n" +
                "                table(sys.ODCIVarchar2List("+ colIDs +")) bb \n" +
                "                )  aa \n" +
                "                left join\n" +
                "        (select pcrs.term1, \n" +
                "                pcrs.term2, \n" +
                "                decode(pcs.std, 0, 0,(pcrs.value_avg- pcs.AVG)/ pcs.STD) as heat_level \n" +
                "                from (\n" +
                "                         SELECT a.term1                AS term1,\n" +
                "                          a.term2                      AS term2,\n" +
                "                          AVG(er.MEASUREMENT_VALUE)    AS value_avg,\n" +
                "                          stddev(er.MEASUREMENT_VALUE) AS std,\n" +
                "                          MIN(er.MEASUREMENT_VALUE)    AS MIN,\n" +
                "                          MAX(er.MEASUREMENT_VALUE)    AS MAX,\n" +
                "                          COUNT(*)                     AS COUNT,\n" +
                "                          0\n" +
                "                        FROM experiment_record_view er,\n" +
                "                          (SELECT UNIQUE pai1.ONT_ID AS term1,\n" +
                "                            pai2.ONT_ID              AS term2,\n" +
                "                            pai1.EXPERIMENT_RECORD_ID\n" +
                "                          FROM PHENOMINER_ANNOTATION_INDEX pai1,\n" +
                "                            PHENOMINER_ANNOTATION_INDEX pai2,\n" +
                "                            PHENOMINER_ANNOTATION_INDEX pai3\n" +
                "                          WHERE  pai1.ONT_ID                IN ("+ rowIDs +")\n" +
                "                          AND pai1.EXPERIMENT_RECORD_ID = pai2.EXPERIMENT_RECORD_ID\n" +
                "                          AND pai2.ONT_ID              IN ("+ colIDs +")\n" +
                "                          AND pai1.EXPERIMENT_RECORD_ID = pai3.EXPERIMENT_RECORD_ID " + sexCondition +
                "                          ) a\n" +
                "                        WHERE er.EXPERIMENT_RECORD_ID = a.EXPERIMENT_RECORD_ID\n" +
                "                        GROUP BY a.term1,\n" +
                "                          a.term2 ) pcrs, PHENOMINER_CMO_STATS PCS\n" +
                "                        where pcrs.term1=pcs.ONT_ID ) pcrs on\n" +
                "                pcrs.term2 = aa.col_term and \n" +
                "                pcrs.term1 = aa.row_term \n" +
                "                ) v,\n" +
                "                ont_terms ot1, \n" +
                "                ont_terms ot2\n" +
                "                where  \n" +
                "                v.col_term = ot1.term_acc and \n" +
                "                v.row_term = ot2.term_acc";

        HeatMapQuery sq = new HeatMapQuery(this.getDataSource(), query);
        sq.compile();

        List<HeatMapRecord> ords = sq.execute();
        return ords;
    }

    public List<HeatMapRecord> getHeatMap(List<String> colTerms, List<String> rowTerms, char sex) throws Exception {

        if (colTerms != null && colTerms.size() > 0 &&
                rowTerms != null && rowTerms.size() > 0) {
            String allColIDs = Utils.concatenate(colTerms, ",", "'");
            String allRowIDs = Utils.concatenate(rowTerms, ",", "'");
            return getHeatMap(allColIDs, allRowIDs, sex);
        } else {
            return null;
        }
    }

    public List<HeatMapRecord> getHeatMapGivenTerms(String term1, String ont_id1,
                                                    String term2, String ont_id2,
                                                    String ont3, String ont4,
                                                    String cmo_term, char sex) {
        String sexCondition =                     "        INTERSECT\n" +
                    "        SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( ";
        switch (sex) {
            case 'f':
                sexCondition += "'SEX:0000001') ";
                break;
            case 'm':
                sexCondition += "'SEX:0000002') ";
                break;
            case 'b':
            case 'u':
                sexCondition = " ";
                break;
        }
        try {
            OntologyXDAO ont_dao = new OntologyXDAO();
            String ont_term1 = ont_dao.getTermByTermName(term1, ont_id1).getAccId();
            String ont_term2 = ont_dao.getTermByTermName(term2, ont_id2).getAccId();
            String query = "SELECT ot2.term AS ROW_Term,\n" +
                    "  ot1.term      AS COL_TERM,\n" +
                    "  v.heat_level\n" +
                    "FROM\n" +
                    "  (SELECT aa.term1,\n" +
                    "    aa.term2,\n" +
                    "    DECODE(pcs.std, 0, 0,(pcrs.value_avg - pcs.AVG)/ pcs.STD) AS heat_level\n" +
                    "  FROM\n" +
                    "    (SELECT bb.ONT_ID AS term1,\n" +
                    "      dd.ONT_ID       AS term2\n" +
                    "    FROM\n" +
                    "      (SELECT UNIQUE ONT_ID\n" +
                    "      FROM PHENOMINER_ANNOTATION_INDEX pai\n" +
                    "      WHERE pai.EXPERIMENT_RECORD_ID IN\n" +
                    "        (SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term1 + "' )\n" +
                    "        INTERSECT\n" +
                    "        SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term2 + "' )\n" +
                    sexCondition +
                    "        )\n" +
                    "      AND pai.SOURCE_ONT='" + ont3 + "'\n" +
                    "      ) bb,\n" +
                    "      (SELECT UNIQUE ONT_ID\n" +
                    "      FROM PHENOMINER_ANNOTATION_INDEX pai\n" +
                    "      WHERE pai.EXPERIMENT_RECORD_ID IN\n" +
                    "        (SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term1 + "' )\n" +
                    "        INTERSECT\n" +
                    "        SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term2 + "' )\n" +
                    sexCondition +
                    "        )\n" +
                    "      AND pai.SOURCE_ONT='" + ont4 + "'\n" +
                    "      ) dd\n" +
                    "    ) aa\n" +
                    "  LEFT JOIN\n" +
                    "    (SELECT a.term1                AS term1,\n" +
                    "      a.term2                      AS term2,\n" +
                    "      AVG(er.MEASUREMENT_VALUE)    AS value_avg,\n" +
                    "      stddev(er.MEASUREMENT_VALUE) AS std,\n" +
                    "      MIN(er.MEASUREMENT_VALUE)    AS MIN,\n" +
                    "      MAX(er.MEASUREMENT_VALUE)    AS MAX,\n" +
                    "      COUNT(*)                     AS COUNT,\n" +
                    "      0\n" +
                    "    FROM experiment_record_view er,\n" +
                    "      (SELECT UNIQUE pai1.ONT_ID AS term1,\n" +
                    "        pai2.ONT_ID              AS term2,\n" +
                    "        pai1.EXPERIMENT_RECORD_ID\n" +
                    "      FROM PHENOMINER_ANNOTATION_INDEX pai1,\n" +
                    "        PHENOMINER_ANNOTATION_INDEX pai2\n" +
                    "      WHERE (pai1.EXPERIMENT_RECORD_ID IN\n" +
                    "        (SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term1 + "' )\n" +
                    "        INTERSECT\n" +
                    "        SELECT UNIQUE pai0.EXPERIMENT_RECORD_ID\n" +
                    "        FROM PHENOMINER_ANNOTATION_INDEX pai0\n" +
                    "        WHERE pai0.ONT_ID IN ( '" + ont_term2 + "' )\n" +
                    sexCondition +
                    "        ))\n" +
                    "      AND pai1.SOURCE_ONT               = '" + ont3 + "'\n" +
                    "      AND pai1.EXPERIMENT_RECORD_ID = pai2.EXPERIMENT_RECORD_ID\n" +
                    "      AND pai2.SOURCE_ONT               ='" + ont4 + "'\n" +
                    "      ) a\n" +
                    "    WHERE er.EXPERIMENT_RECORD_ID = a.EXPERIMENT_RECORD_ID\n" +
                    "    GROUP BY a.term1,\n" +
                    "      a.term2\n" +
                    "    ) pcrs\n" +
                    "  ON pcrs.term1  = aa.term1\n" +
                    "  AND pcrs.term2 = aa.term2\n" +
                    "  LEFT JOIN PHENOMINER_CMO_STATS PCS\n" +
                    "  ON pcs.ONT_ID = '" + cmo_term + "'\n" +
                    "  ) v,\n" +
                    "  ont_terms ot1,\n" +
                    "  ont_terms ot2\n" +
                    "WHERE v.term1 = ot1.term_acc\n" +
                    "AND v.term2  = ot2.term_acc\n" +
                    "order by ROW_term, COL_term";

            HeatMapQuery sq = new HeatMapQuery(this.getDataSource(), query);
            sq.compile();

            List<HeatMapRecord> ords = sq.execute();
            return ords;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String checkUnitConversion(String termAcc, String unitFrom) {
        try {
            String sqlStr = "select standard_unit from PHENOMINER_STANDARD_UNITS where ont_id=?";
            List<String> result = StringListQuery.execute(this, sqlStr, termAcc);

            if (result.size() == 0) {
                sqlStr = "insert into PHENOMINER_STANDARD_UNITS (ont_id,standard_unit)" +
                        " values (?, ?)";
                update(sqlStr, termAcc, unitFrom);


                sqlStr = "insert into PHENOMINER_TERM_UNIT_SCALES (ont_id, unit_from, unit_to, TERM_SPECIFIC_SCALE, ZERO_OFFSET) values (?, ?, ?,1,0)";
                update(sqlStr, termAcc, unitFrom, unitFrom);

                OntologyXDAO odao = new OntologyXDAO();
                String emailBody = "Standard unit of " + termAcc + " (" + odao.getTerm(termAcc).getTerm() + ") has been assigned: " + unitFrom;
                Utils.sendMail("localhost", "PhenoMinerCurationSoftware", new String[]{"jrsmith@mcw.edu", "slaulede@mcw.edu"}, "New standard unit assigned", emailBody);
                return "";
            }

            String unitTo = result.get(0);

            sqlStr = "SELECT count(*) from PHENOMINER_TERM_UNIT_SCALES " +
                                "WHERE ont_id=? AND unit_from=? AND unit_to=?";
            int cnt = getCount(sqlStr, termAcc, unitFrom, unitTo);
            if (cnt > 0) return "";
            sqlStr = "select count(*) from PHENOMINER_UNIT_SCALES " +
                    "where unit_from='" + unitFrom + "' and unit_to='" + unitTo + "'";
            cnt = getCount(sqlStr);
            if (cnt == 0) {
                if (!unitFrom.equals(unitTo)) return "Clinical Measurement Unit \"" +
                        unitFrom + "\" can not be converted to " + termAcc + "'s standard unit: \"" + unitTo + "\"";

                sqlStr = "insert into PHENOMINER_UNIT_SCALES (unit_from, unit_to, SCALE, ZERO_OFFSET) values (?,?,1,0)";
                update(sqlStr, unitFrom, unitTo);
            }

            sqlStr = "insert into PHENOMINER_TERM_UNIT_SCALES select '" + termAcc + "'," +
                    "unit_from,unit_to,scale,zero_offset from PHENOMINER_UNIT_SCALES where unit_from=? and " +
                    "unit_to=?";

            update(sqlStr, unitFrom, unitTo);
            return "";
        } catch (Exception e) {
            return "Cannot convert the units!";
        }
    }

    public String insertUnitConversion(String termAcc, String unitFrom, String termScale) {
        try {
            String msg = checkUnitConversion(termAcc,unitFrom);
            if(msg != "") {
                String sqlStr = "select standard_unit from PHENOMINER_STANDARD_UNITS where ont_id=?";
                List<String> result = StringListQuery.execute(this, sqlStr, termAcc);
                String unitTo = result.get(0);
                sqlStr = "insert into PHENOMINER_UNIT_SCALES (unit_from, unit_to, SCALE, ZERO_OFFSET) values (?,?,?,0)";
                update(sqlStr, unitFrom, unitTo, termScale);

                sqlStr = "insert into PHENOMINER_TERM_UNIT_SCALES (ont_id,unit_from,unit_to,term_specific_scale,zero_offset) values(?,?,?,?,0)";

                update(sqlStr, termAcc,unitFrom, unitTo,termScale);
            }
            return "";
        } catch (Exception e) {
            return "Conversion Failed!";
        }
    }
    /**
     * return standard Unit for the input term acc in phenominer
     * @param accId termAcc
     * @return standard Unit for term . Returns null if no unit exists
     * @throws Exception on error in spring framework
     */
    public String getStandardUnit(String accId) throws Exception{
        String query = "select standard_unit from PHENOMINER_STANDARD_UNITS where ont_id=?";
        return getStringResult(query, accId);
    }
    /**
     * return number of data entries for the input RGD ID in phenominer
     * @param rgdId rgd id
     * @return Number of data entries. Returns 0 if no record found
     * @throws Exception on error in spring framework
     */
    public int getNumOfRecords(int rgdId) throws Exception{
        String query = "SELECT COUNT(*) FROM study s, experiment e, experiment_record er" +
                " WHERE ref_rgd_id=? " +
                "AND s.study_id = e.study_id " +
                "AND e.experiment_id = er.experiment_id " +
                "AND er.curation_status=40";
        return getCount(query, rgdId);
    }


    /**
     * Insert a study in the data store; return study id assigned automatically during db insert
     * @param recordId record id
     * @throws Exception
     */
    public void insertToIndex(int recordId, String accId, String primaryAccId, int studyId, String studyName, int experimentId, String experimentName) throws Exception{


        String[] parts = accId.split(":");

        String ont = parts[0].toUpperCase();

        String aspect = "";

        //done to avoid a query for common phenomoner ontologies
        if (ont.equals("RS")) {
            aspect = "S";

        }else if (ont.equals("XCO")) {
            aspect = "X";

        }else if (ont.equals("MMO")) {
            aspect = "M";

        }else if (ont.equals("CMO")) {
            aspect = "L";

        }else  {
            try {
                OntologyXDAO odao = new OntologyXDAO();
                Ontology o = odao.getOntology(ont);
                aspect = o.getAspect();
            }catch (Exception ignored) {

            }
        }


        String sql = "INSERT INTO full_record_index (experiment_record_id, term_acc, primary_term_acc, aspect, study_id, "+
                "study_name, experiment_id, experiment_name)  VALUES (?,?,?,?,?,?,?,?)";

        update(sql, recordId,accId,primaryAccId, aspect,studyId,studyName,experimentId,experimentName);
    }

    public OverlapReport getRecordCountOverlap(List<String> term1AccIds, List<String> term2AccIds, String column, int studyId) throws Exception {

        String query = "SELECT distinct fai.term_acc as term1, fai2.term_acc as term2, fai." + column + " as rid, fai.study_id, fai.study_name ";

        if (!column.equals("study_id")) {
            query += ", fai.experiment_id, fai.experiment_name ";
        }

        query += " from full_record_index fai, full_record_index fai2 WHERE fai.experiment_record_id = fai2.experiment_record_id ";
        query +=" AND fai.term_acc IN (" + Utils.buildInPhraseQuoted(term1AccIds) + ") ";
        query +=" AND fai2.term_acc IN (" + Utils.buildInPhraseQuoted(term2AccIds) + ") ";

        if (studyId != 0) {
            query += " and fai.study_id = " + studyId;
        }


        RecordCountOverlapQuery gcq = new RecordCountOverlapQuery(this.getDataSource(), query);
        gcq.compile();

        List results =  gcq.execute();

        OverlapReport or = new OverlapReport();

        HashMap hm = new HashMap();
        HashMap studies = new HashMap();
        HashMap experiments = new HashMap();

        Iterator it = results.iterator();
        while (it.hasNext()) {

            List nxt = (List)it.next();
            String t1 = (String) nxt.get(0);
            String t2 = (String) nxt.get(1);

            String sid = ((Integer) nxt.get(3)) + "";
            String studyName = (String) nxt.get(4);

            //String expId = ((Integer) nxt.get(5)) + "";
            //String expName = (String) nxt.get(6);

            studies.put(sid,studyName);
            //experiments.put(expId,expName);

            //Integer cnt = (Integer) nxt.get(2);

            String combinedKey = t1 + "_" + t2;

            if (hm.containsKey(combinedKey)) {
                hm.put(combinedKey, ((Integer)hm.get(combinedKey)) + 1);
            }else {
                hm.put(combinedKey, 1);
            }
        }


        or.setRecordCountMap(hm);
        or.setStudyMap(studies);
        or.setExperimentMap(experiments);


        return or;

    }

    public List getTermIdsAndChildrenWithRecords(String term1AccId, String term2AccId, int studyId) throws Exception {

        String query = "SELECT distinct fai.primary_term_acc as term1, fai2.primary_term_acc as term2";

        query += " from full_record_index fai, full_record_index fai2 WHERE fai.experiment_record_id = fai2.experiment_record_id ";
        query +=" AND fai.term_acc = '" + term1AccId + "'";
        query +=" AND fai2.term_acc = '" + term2AccId + "'";

        if (studyId != 0) {
            query += " and fai.study_id = " + studyId;
        }

        HashMap hm = new HashMap();

        Connection conn = this.getConnection();
        Statement st = conn.createStatement();

        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            hm.put(rs.getString(1), null);
            hm.put(rs.getString(2), null);
        }

        ArrayList al = new ArrayList();
        Iterator it = hm.keySet().iterator();
        while (it.hasNext()) {
            String termAccId=(String) it.next();
            String[] termParts = termAccId.split(":");
            al.add(termParts[0] + ":" + Integer.parseInt(termParts[1]));
        }

        return al;

    }


    public int deleteAllFromFullRecordIndex() throws Exception{

        String sql = "DELETE FROM full_record_index";
        return update(sql);
    }



    private String buildInclauseForgetFullRecords(String colRef, List<String> ids) {

        if( ids.isEmpty() ) {
            return "";
        }

        return "AND " + colRef + " IN ("+Utils.buildInPhraseQuoted(ids)+") ";
    }

    public String getCondtionGroupIdsSQL(List<String> experimentalConditionIds)  throws Exception{

        String sql = "SELECT DISTINCT experiment_record_id FROM experiment_condition ec " +
                "WHERE 1=1 ";

        sql +=  this.buildInclauseForgetFullRecords("exp_cond_ont_id", experimentalConditionIds);

        return sql;
    }



    public List<Record> getFullRecords(List<String> sampleIds, List<String> measurementMethodIds, List<String> clinicalMeasurementIds,
                       List<String> experimentalConditionIds, int speciesTypeKey) throws Exception {

        String query = "SELECT * FROM experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "WHERE er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id  " +
                "and er.measurement_method_id=mm.measurement_method_id  " +
                "and er.experiment_id=e.experiment_id and e.study_id=st.study_id " +
                "and er.curation_status=40 and er.species_type_key=" + speciesTypeKey + " ";

        query += this.buildInclauseForgetFullRecords("s.strain_ont_id", sampleIds);
        query += this.buildInclauseForgetFullRecords("mm.measurement_method_ont_id", measurementMethodIds);
        query += this.buildInclauseForgetFullRecords("cm.clinical_measurement_ont_id", clinicalMeasurementIds);

        String conditionGroupSQL = this.getCondtionGroupIdsSQL(experimentalConditionIds);

        query += " and er.experiment_record_id in (" + conditionGroupSQL + ")";

        return runFullRecordsQuery(query);
    }

    public List<Record> getFullRecords(List<String> sampleIds, List<String> measurementMethodIds, List<String> clinicalMeasurementIds,
                                       List<String> experimentalConditionIds, int speciesTypeKey, int refRgdId) throws Exception {

        String query = "SELECT * FROM experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "WHERE er.clinical_measurement_id=cm.clinical_measurement_id  AND er.sample_id=s.sample_id " +
                " AND er.measurement_method_id=mm.measurement_method_id " +
                " AND er.experiment_id=e.experiment_id  AND e.study_id=st.study_id  AND st.ref_rgd_id=" + refRgdId + " " +
                " AND er.curation_status=40  AND er.species_type_key=" + speciesTypeKey + " ";

        query += this.buildInclauseForgetFullRecords("s.strain_ont_id", sampleIds);
        query += this.buildInclauseForgetFullRecords("mm.measurement_method_ont_id", measurementMethodIds);
        query += this.buildInclauseForgetFullRecords("cm.clinical_measurement_ont_id", clinicalMeasurementIds);

        String conditionGroupSQL = this.getCondtionGroupIdsSQL(experimentalConditionIds);

        query += " and er.experiment_record_id in (" + conditionGroupSQL + ")";

        return runFullRecordsQuery(query);
    }

    public List<Record> getFullRecords(List<Integer> experimentRecordIds) throws Exception {
        String query = "select * from experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "where er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id  " +
                "and er.measurement_method_id=mm.measurement_method_id  " +
                "and er.experiment_id=e.experiment_id and e.study_id=st.study_id " +

                "and er.curation_status=40 and er.species_type_key=3 "+
                "and er.experiment_record_id in (";
        query += Utils.concatenate(experimentRecordIds, ", ");
        query += ")";

        return runFullRecordsQuery(query);
    }
    public List<Record> getFullRecords() throws Exception {
        String query = "select * from experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "where er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id  " +
                "and er.measurement_method_id=mm.measurement_method_id  " +
                "and er.experiment_id=e.experiment_id and e.study_id=st.study_id " +

                "and er.curation_status=40 and er.species_type_key=3 ";

        return runFullRecordsQuery(query);
    }
    public List<Record> getFullRecordsByCMO(String cmoTermAcc) throws Exception {
        String query = "select * from experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "where er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id  " +
                "and er.measurement_method_id=mm.measurement_method_id  " +
                "and er.experiment_id=e.experiment_id and e.study_id=st.study_id " +

                "and er.curation_status=40 and er.species_type_key=3 " +
                "and cm.clinical_measurement_ont_id='"+cmoTermAcc+"'";

        return runFullRecordsQuery(query);
    }

    /**
     * get full phenominer records based on reference rgd id of a study
     * @param refRgdId reference rgd id of a study
     * @return list of Record objects
     * @throws Exception
     */
    public List<Record> getFullRecords(int refRgdId) throws Exception {
        String query = "SELECT * FROM experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "WHERE er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id " +
                " AND er.measurement_method_id=mm.measurement_method_id" +
                " AND er.experiment_id=e.experiment_id AND e.study_id=st.study_id" +
                " AND er.curation_status=40 AND ref_rgd_id="+refRgdId;

        return runFullRecordsQuery(query);
    }
    /** created by JTHOTA for indexing chinchilla records
     *
     * @return
     * @throws Exception
     */
    public List<Record> getChincillaFullRecords() throws Exception {
        String query = "select * from experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "where er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id  " +
                "and er.measurement_method_id=mm.measurement_method_id  " +
                "and er.experiment_id=e.experiment_id and e.study_id=st.study_id " +

                "and er.curation_status=40 and er.species_type_key=4 ";

        return runFullRecordsQuery(query);
    }
    /**
     * get full phenominer records based on reference rgd id of a study
     * @param refRgdId reference rgd id of a study, ontId RS, CMO, VT, or MMO id to fine tune amount of records
     * @return list of Record objects
     * @throws Exception
     */
    public List<Record> getFullRecords(int refRgdId, String ontId) throws Exception {

        String prefix=ontId.split(":")[0];
        String query = "SELECT * FROM experiment_record_view er, clinical_measurement cm, sample s, experiment e, study st, measurement_method mm " +
                "WHERE er.clinical_measurement_id=cm.clinical_measurement_id and er.sample_id=s.sample_id " +
                " AND er.measurement_method_id=mm.measurement_method_id" +
                " AND er.experiment_id=e.experiment_id AND e.study_id=st.study_id" +
                " AND er.curation_status=40 AND ref_rgd_id="+refRgdId;
        switch (prefix){
            case "RS":
                query += " and STRAIN_ONT_ID='"+ontId+"'";
                break;
            case "CMO":
                query += " and CLINICAL_MEASUREMENT_ONT_ID='"+ontId+"'";
                break;
            case "VT":
                query += " and TRAIT_ONT_ID='"+ontId+"'";
                break;
            case "MMO":
                query += " and MEASUREMENT_METHOD_ONT_ID='"+ontId+"'";
                break;
        }

        return runFullRecordsQuery(query);
    }

    List<Record> runFullRecordsQuery(String query ) throws Exception {

        RecordQuery rq = new RecordQuery(this.getDataSource(), query);
        rq.compile();

        List<Record> recList = rq.execute();

        for (Record r: recList) {
            r.setConditions(this.getConditions(r.getId()));

            String conditionSetItem = "";
            int lastOrdinality = 1;

            for (Condition c : r.getConditions()) {

                if (!conditionSetItem.equals("")) {
                    if (c.getOrdinality() == lastOrdinality) {
                        conditionSetItem += " and ";
                    } else {
                        conditionSetItem += " then ";
                        lastOrdinality = c.getOrdinality();
                    }
                }

                String desc = c.getConditionDescription2();

                conditionSetItem += desc;
            }

            r.setConditionDescription(conditionSetItem);
        }
        return recList;
    }
    public List<PhenominerUnitTable> getConversionFactorToStandardUnits(int recordId) throws Exception {
        String sql="select * from phenominer_term_unit_scales tus where unit_from in (\n" +
                "select measurement_units from experiment_record er where experiment_record_id=?)";
        PhenominerUnitTablesQuery query=new PhenominerUnitTablesQuery(this.getDataSource(), sql);
        return execute(query, recordId);
    }
}
