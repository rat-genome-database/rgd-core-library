package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.annotation.Enrichment;
import edu.mcw.rgd.datamodel.annotation.OntologyEnrichment;
import edu.mcw.rgd.datamodel.ontology.Annotation;
import edu.mcw.rgd.datamodel.ontologyx.Relation;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author jdepons
 * @since May 19, 2008
 * Allows manipulation of annotations found in FULL_ANNOT table
 */
public class AnnotationDAO extends AbstractDAO {

    /// method should be renamed to 'getGeneAnnotations'
    public List<Annotation> getAnnotations(String accId, List<String> ids, List<Integer> speciesTypeKeys, List<String> evidenceCodes) throws Exception {

        String speciesInClause = "(" + Utils.buildInPhrase(speciesTypeKeys) + ")";
        String idInClause2 = "(" + Utils.buildInPhrase(ids) + ")";

        String query = "SELECT a.*, r.species_type_key FROM full_annot a, rgd_ids r, genes g "+
            "WHERE term_acc=? AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key in " + speciesInClause +
            " AND object_status='ACTIVE' AND rgd_object_key=1  AND a.annotated_object_rgd_id in " + idInClause2 +
            " AND evidence IN (" + Utils.buildInPhraseQuoted(evidenceCodes) + ") AND r.rgd_id=g.rgd_id ORDER BY g.gene_symbol_lc";
        return executeAnnotationQuery(query, accId);
    }

    /**
     * get annotation by unique key
     * @param key annotation key
     * @return Annotation object
     * @throws Exception if annotation key is invalid, or on spring framework dao failure
     */
    public Annotation getAnnotation(int key) throws Exception {
        String query = "SELECT * FROM full_annot WHERE full_annot_key=?";
        List<Annotation> annots = executeAnnotationQuery(query, key);
        if (annots.isEmpty() ) {
            throw new Exception("Annotation " + key + " not found");
        }
        return annots.get(0);
    }

    /**
     * get annotation by a list of values that comprise unique key:
     * TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @param annot Annotation object with the following fields set: TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @return Annotation object or null if invalid key
     * @throws Exception on spring framework dao failure
     */
    public Annotation getAnnotation(Annotation annot) throws Exception {

        String query = "SELECT * FROM full_annot WHERE "
                // fields that are never null
                +"term_acc=? AND annotated_object_rgd_id=? AND evidence=? AND "
                // fields that could be null
                +"NVL(ref_rgd_id,0) = NVL(?,0) AND "
                +"NVL(with_info,'*') = NVL(?,'*') AND "
                +"NVL(qualifier,'*') = NVL(?,'*') AND "
                +"NVL(xref_source,'*') = NVL(?,'*')";

        List<Annotation> list = executeAnnotationQuery(query, annot.getTermAcc(), annot.getAnnotatedObjectRgdId(),
                annot.getEvidence(), annot.getRefRgdId(), annot.getWithInfo(), annot.getQualifier(), annot.getXrefSource());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * get annotation notes given a list of values that comprise unique key:
     * TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @param annot Annotation object with the following fields set: TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @return Annotation object with annot_key and notes field set, or null if invalid key
     * @throws Exception on spring framework dao failure
     */
    public IntStringMapQuery.MapPair getAnnotationNotes(Annotation annot) throws Exception {

        String query = "SELECT a.full_annot_key,notes FROM full_annot a WHERE "
                // fields that are never null
                +"term_acc=? AND annotated_object_rgd_id=? AND evidence=? AND "
                // fields that could be null
                +"NVL(ref_rgd_id,0) = NVL(?,0) AND "
                +"NVL(with_info,'*') = NVL(?,'*') AND "
                +"NVL(qualifier,'*') = NVL(?,'*') AND "
                +"NVL(xref_source,'*') = NVL(?,'*')";

        List<IntStringMapQuery.MapPair> results = IntStringMapQuery.execute(this, query, annot.getTermAcc(),
                annot.getAnnotatedObjectRgdId(), annot.getEvidence(), annot.getRefRgdId(), annot.getWithInfo(),
                annot.getQualifier(), annot.getXrefSource());
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * get annotation key by a list of values that comprise unique key:
     * TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @param annot Annotation object with the following fields set: TERM_ACC+ANNOTATED_OBJECT_RGD_ID+REF_RGD_ID+EVIDENCE+WITH_INFO+QUALIFIER+XREF_SOURCE
     * @return value of annotation key or 0 if there is no such annotation
     * @throws Exception on spring framework dao failure
     */
    public int getAnnotationKey(Annotation annot) throws Exception {

        String query = "SELECT full_annot_key FROM full_annot WHERE "
                // fields that are never null
                +"term_acc=? AND annotated_object_rgd_id=? AND evidence=? AND "
                // fields that could be null
                +"NVL(ref_rgd_id,0) = NVL(?,0) AND "
                +"NVL(with_info,'*') = NVL(?,'*') AND "
                +"NVL(qualifier,'*') = NVL(?,'*') AND "
                +"NVL(xref_source,'*') = NVL(?,'*')";

        IntListQuery q = new IntListQuery(this.getDataSource(), query);

        List<Integer> list = execute(q, annot.getTermAcc(), annot.getAnnotatedObjectRgdId(), annot.getEvidence(),
            annot.getRefRgdId(), annot.getWithInfo(), annot.getQualifier(), annot.getXrefSource());
        return list.isEmpty() ? 0 : list.get(0);
    }

    /**
     * get list of annotations for given term acc and created by
     * @param termAcc term accession id
     * @param createdBy created by (usually denotes a pipeline)
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
    */
    public List<Annotation> getAnnotations(String termAcc, int createdBy) throws Exception {
        String query = "SELECT * FROM full_annot WHERE term_acc=? AND created_by=?";
        return executeAnnotationQuery(query, termAcc, createdBy);
    }

    public List<Annotation> getAnnotations(String termAcc) throws Exception {
        String query = "SELECT * FROM full_annot WHERE term_acc=?";
        return executeAnnotationQuery(query, termAcc);
    }
    /**
     * get list of annotations for given term acc, created by, object key and species
     * @param termAcc term accession id
     * @param createdBy created by (usually denotes a pipeline)
     * @param objectKey object key
     * @param speciesTypeKey species type key
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
    */
    public List<Annotation> getAnnotations(String termAcc, int createdBy, int objectKey, int speciesTypeKey) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE term_acc=? AND created_by=? AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND object_key=? AND species_type_key=?";
        return executeAnnotationQuery(query, termAcc, createdBy, objectKey, speciesTypeKey);
    }

    /**
     * get list of annotations for given rgd_id, term acc and created by
     * @param annotatedObjectRGDId annotated object rgd id
     * @param termAcc term accession id
     * @param createdBy created by
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
    */
    public List<Annotation> getAnnotations(int annotatedObjectRGDId, String termAcc, int createdBy) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? AND term_acc=? AND created_by=?";
        return executeAnnotationQuery(query, annotatedObjectRGDId, termAcc, createdBy);
    }

    public List<Annotation> getAnnotationsForOntology(int annotatedObjectRGDId, String ontologyPrefix) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? AND term_acc LIKE ?";
        return executeAnnotationQuery(query, annotatedObjectRGDId, ontologyPrefix+"%");
    }

    /**
     * get annotations for given rgd_id, and for all child terms of given term
     * Note: all child terms, direct and indirect are examined
     * Note: this method does not work well for strains!!!
     * @param rgdId annotated object rgd id
     * @param termAcc term accession id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
    */
    public List<Annotation> getChildAnnotations(int rgdId, String termAcc) throws Exception {
        String query = "SELECT a.* FROM full_annot a\n" +
                "WHERE a.annotated_object_rgd_id=? AND term_acc IN(\n" +
                "  SELECT child_term_acc FROM ont_dag\n" +
                "  START WITH parent_term_acc=?\n" +
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc\n" +
                ")\n" +
                "ORDER BY term,qualifier";
        return executeAnnotationQuery(query, rgdId, termAcc);
    }

    /**
     * get annotations for all child terms of given term
     * Note: all child terms, direct and indirect are examined
     * @param termAcc term accession id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getChildAnnotations(String termAcc) throws Exception {
        String query = "SELECT a.* FROM full_annot a\n" +
                "WHERE term_acc IN(\n" +
                "  SELECT child_term_acc FROM ont_dag\n" +
                "  START WITH parent_term_acc=?\n" +
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc\n" +
                ")\n" +
                "ORDER BY term,qualifier";
        return executeAnnotationQuery(query, termAcc);
    }

    /* unused -- to be removed?
    public List<Annotation> getAnnotationList(List<String> termAccs, List<Integer> objectRgdIds, int objectKey) throws Exception {
        String query = "select distinct fa.full_annot_key, fa.term, fa.annotated_object_rgd_id, fa.rgd_object_key, fa.data_src, fa.object_symbol, fa.ref_rgd_id, fa.evidence, fa.with_info, fa.aspect, fa.object_name \n" +
                 ",fa.qualifier, fa.relative_to, fa.created_date, fa.last_modified_date, fa.term_acc, fa.created_by, fa.last_modified_by, fa.xref_source " +
                " from full_annot_index fai, full_annot fa " +
                 " where fai.full_annot_key = fa.full_annot_key ";

        if (objectRgdIds.size()==0 && termAccs.size()==0) {
            throw new Exception("Gene and term lists can not be 0 length");
        }

        if (objectRgdIds.size() > 0) {
            query += " and annotated_object_rgd_id in ( ";
            query += Utils.concatenate(objectRgdIds, ",");
            query += ") ";
        }

        if (termAccs.size() > 0) {
            query += " and fai.term_acc in (";
            query += Utils.buildInPhraseQuoted(termAccs);
            query += ")";
        }


        query += " and rgd_object_key=" + objectKey;
        query += "  order by term, object_symbol ";

        AnnotationQuery q = new AnnotationQuery(this.getDataSource(), query);
        q.compile();

        return q.execute();

    }
*/

    public List<Annotation> getAnnotationByEvidence(int annotatedObjectRGDId, String termAcc, int createdBy, String evidence) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? and term_acc=? and created_by=? and evidence=?";
        return executeAnnotationQuery(query, annotatedObjectRGDId, termAcc, createdBy, evidence);
    }


    /**
     * get list of annotations by created date
     * @return list of annotations
     * @throws Exception
     */
    public List<Annotation> getAnnotations(int rgdId, Date start, Date end, String aspect) throws Exception {

        String query = "SELECT * FROM full_annot WHERE " +
                "annotated_object_rgd_id =? AND created_date between ? and ? " +
                "AND aspect=?";

        return executeAnnotationQuery(query, rgdId, start, end, aspect);
    }

    /**
     * get list of annotations by created date
     * @return list of annotations
     * @throws Exception
     */
    public List<Annotation> getAnnotations(String termAcc, Date start, Date end) throws Exception {

        String query = "select * from full_annot fa, full_annot_index fai " +
                "where fa.full_annot_key=fai.full_annot_key and fai.term_acc=? " +
                "and fa.created_date between ? and ?";

        return executeAnnotationQuery(query, termAcc, start, end);
    }

    /**
     * @return list of annotations
     * @throws Exception
     */
    public List<Annotation> getAnnotations(String termAcc, int objectKey, Date start, Date end, int speciesTypeKey) throws Exception {

        String query = "select * from full_annot fa, full_annot_index fai, rgd_ids ri " +
                "where fa.full_annot_key=fai.full_annot_key and fa.annotated_object_rgd_id=ri.rgd_id and fa.rgd_object_key=? and fai.term_acc=? and ri.species_type_key=? " +
                "and fa.created_date between ? and ? ";

        return executeAnnotationQuery(query, objectKey, termAcc, speciesTypeKey, start, end);
    }

    /**
     * get list of annotations matching the parameters
     * @param annotatedObjectRGDId
     * @param evidence
     * @param termAcc
     * @param createdBy
     * @param xrefSource
     * @return list of annotations
     * @throws Exception
     */
    public List<Annotation> getAnnotations(int annotatedObjectRGDId, String evidence, String termAcc, int createdBy, String xrefSource) throws Exception {

        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? AND evidence=? AND term_acc=? AND created_by=? ";

        if (xrefSource == null) {
            query += " AND xref_source is null";
        } else {
           query += " AND xref_source='" + xrefSource + "'";
        }

        return executeAnnotationQuery(query, annotatedObjectRGDId, evidence, termAcc, createdBy);
    }

    /**
     * get list of annotations matching the parameters
     * @param annotatedObjectRGDId annotated object rgd id
     * @param evidence
     * @param termAcc
     * @param xrefSource
     * @return list of annotations
     * @throws Exception
     */
    public List<Annotation> getAnnotations(int annotatedObjectRGDId, String evidence, String termAcc, String xrefSource) throws Exception {

        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? and evidence=? and term_acc=? ";

        if (xrefSource == null) {
            query += " and xref_source is null";
        } else {
           query += " and xref_source='" + xrefSource + "'";
        }

        return executeAnnotationQuery(query, annotatedObjectRGDId, evidence, termAcc);
    }

    public List<Annotation> getAnnotationsWithDiseaseCategory(int annotatedObjectRGDId) throws Exception {

        String query = "SELECT DISTINCT fa.full_annot_key, fa.term, fa.ANNOTATED_OBJECT_RGD_ID, fa.term_acc, fa.data_src, fai.*, ont.term  \n" +
                "FROM full_annot fa \n" +
                "INNER JOIN full_annot_index fai ON fai.full_annot_key=fa.full_annot_key\n" +
                "INNER JOIN  ont_terms ont ON ont.term_acc = fai.term_acc\n" +
                "WHERE fa.annotated_object_rgd_id=? AND fai.term_acc \n" +
                "IN('DOID:9004985', 'DOID:9008261','DOID:0050117', 'DOID:7', 'DOID:14566', 'DOID:9007801', 'DOID:9001878', \n" +
                "        'DOID:9008231', 'DOID:9005463', 'DOID:9000298', 'DOID:0080015', 'DOID:0050155', 'DOID:9001349', \n" +
                "        'DOID:225', 'DOID:9001600', 'DOID:9008231')";


        return executeAnnotationQuery(query, annotatedObjectRGDId);
    }

    public int updateLastModified(int annotatedObjectRGDID, String evidence, String termAcc, int createdBy, String xrefSource) throws Exception{
        String sql = "UPDATE full_annot SET last_modified_date=SYSDATE " +
                "WHERE annotated_object_rgd_id=? AND evidence=? AND term_acc=? AND created_by=? ";

        if (xrefSource == null) {
            sql += " AND xref_source IS NULL";
        } else {
           sql += " AND xref_source='" + xrefSource + "'";
        }

        return update(sql, annotatedObjectRGDID, evidence, termAcc, createdBy);
    }

    /**
     * update last modified date to SYSDATE for annotation given full annot key
     * @param fullAnnotKey FULL_ANNOT_KEY
     * @return count of rows affected
     * @throws Exception on spring framework dao failure
     */
    public int updateLastModified(int fullAnnotKey) throws Exception{

        String sql = "UPDATE full_annot SET last_modified_date=SYSDATE WHERE full_annot_key=?";
        return update(sql, fullAnnotKey);
    }

    /**
     * update last modified date to SYSDATE for annotation given full annot key
     * @param fullAnnotKey FULL_ANNOT_KEY
     * @param lastModifiedBy LAST_MODIFIED_BY
     * @return count of rows affected
     * @throws Exception on spring framework dao failure
     */
    public int updateLastModified(int fullAnnotKey, int lastModifiedBy) throws Exception{

        String sql = "UPDATE full_annot SET last_modified_date=SYSDATE, last_modified_by=? WHERE full_annot_key=?";
        return update(sql, lastModifiedBy, fullAnnotKey);
    }

    /**
      * update last modified date for annotation list given their full annot keys
      * @param fullAnnotKeys list of FULL_ANNOT_KEYs
      * @return count of rows affected
      * @throws Exception on spring framework dao failure
      */
    public int updateLastModified(List<Integer> fullAnnotKeys) throws Exception{

        if( fullAnnotKeys==null || fullAnnotKeys.isEmpty() ) {
            return 0;
        }

        String sql = "UPDATE full_annot SET last_modified_date=SYSDATE " +
                "WHERE full_annot_key IN (" + Utils.buildInPhrase(fullAnnotKeys)+ " )";
        return update(sql);
    }

    /**
     * update created-date and last-modified-date for annotation list given their full annot keys
     * @param fullAnnotKeys list of FULL_ANNOT_KEYs
     * @return count of rows affected
     * @throws Exception on spring framework dao failure
     */
    public int updateDates(List<Integer> fullAnnotKeys) throws Exception{

        if( fullAnnotKeys==null || fullAnnotKeys.isEmpty() ) {
            return 0;
        }

        String sql = "UPDATE full_annot SET created_date=SYSDATE,last_modified_date=SYSDATE " +
                "WHERE full_annot_key IN (" + Utils.buildInPhrase(fullAnnotKeys)+ " )";
        return update(sql);
    }

    /**
     * delete annotation object given full_annot_key
     *
     * @param key full_annot_key
     * @return number of rows affected by the delete: 1 - successful delete, 0 - invalid key
     * @throws Exception on spring framework dao failure
     */
    public int deleteAnnotation(int key) throws Exception{

        String sql = "DELETE FROM full_annot WHERE full_annot_key=?";
        return update(sql, key);
    }

    public int deleteAnnotations(List<Integer> keys) throws Exception{

        int deleted = 0;
        for( int i=0; i<keys.size(); i+=1000 ) {
            int j = i+1000;
            if( j > keys.size() ) {
                j = keys.size();
            }
            String sql = "DELETE FROM full_annot WHERE full_annot_key IN(" + Utils.buildInPhrase(keys.subList(i,j)) + ")";
            deleted += update(sql);
        }
        return deleted;
    }

    /**
     * for given source, get all annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(int createdBy, GregorianCalendar gc) throws Exception{

        return getAnnotationsModifiedBeforeTimestamp(createdBy, gc.getTime());
    }

    /**
     * for given source, get all annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(int createdBy, Date dt) throws Exception{

        String query = "SELECT * FROM full_annot WHERE created_by=? AND last_modified_date<?";
        return executeAnnotationQuery(query, createdBy, dt);
    }

    /**
     * for given pipeline and ontology, get all annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(int createdBy, Date dt, String aspect) throws Exception{

        String query = "SELECT * FROM full_annot WHERE created_by=? AND last_modified_date<? AND aspect=?";
        return executeAnnotationQuery(query, createdBy, dt, aspect);
    }

    /**
     * for given pipeline and reference, get all annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(int createdBy, Date dt, int refRgdId) throws Exception{

        String query = "SELECT * FROM full_annot WHERE created_by=? AND last_modified_date<? AND ref_rgd_id=?";
        return executeAnnotationQuery(query, createdBy, dt, refRgdId);
    }

    /**
     * for given pipeline, reference and species, get all annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(int createdBy, Date dt, int refRgdId, int speciesTypeKey) throws Exception{

        String query = "SELECT a.*,i.species_type_key FROM full_annot a,rgd_ids i WHERE a.annotated_object_rgd_id=i.rgd_id "+
                "AND created_by=? AND a.last_modified_date<? AND ref_rgd_id=? AND species_type_key=?";
        return executeAnnotationQuery(query, createdBy, dt, refRgdId, speciesTypeKey);
    }

    /**
     * for given ontology, get annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(String aspect, Date dt) throws Exception{

        String query = "SELECT * FROM full_annot WHERE aspect=? AND last_modified_date<?";
        return executeAnnotationQuery(query, aspect, dt);
    }

    /**
     * for given ontology and species, get annotations modified before given date and time
     *
     * @return list of annotations
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsModifiedBeforeTimestamp(String aspect, Date dt, int speciesTypeKey) throws Exception{

        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id=rgd_id AND aspect=? AND a.last_modified_date<? AND species_type_key=?";
        return executeAnnotationQuery(query, aspect, dt, speciesTypeKey);
    }

    /**
     * delete annotations older than passed in date
     *
     * @return number of rows affected
     * @throws Exception on spring framework dao failure
     */
    public int deleteAnnotations(int createdBy, GregorianCalendar gc) throws Exception{

        return deleteAnnotations(createdBy, gc.getTime());
    }

    /**
     * delete annotations older than passed in date
     *
     * @return number of rows affected
     * @throws Exception on spring framework dao failure
     */
    public int deleteAnnotations(int createdBy, Date dt) throws Exception{

        String sql = "DELETE FROM full_annot WHERE created_by=? AND last_modified_date<? AND ROWNUM<10000";
        int totalRowsAffected = 0, rowsAffected;
        do {
            rowsAffected = update(sql, createdBy, dt);
            totalRowsAffected += rowsAffected;
        }
        while( rowsAffected>0 );
        return totalRowsAffected;
    }


    /**
     * get annotations given reference rgd id
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsByReference(int refRgdId) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n"+
                "FROM full_annot a,rgd_ids r, references ref \n"+
                "WHERE ref_rgd_id=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n" +
                "ORDER BY a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId);
    }
    public List<Annotation> getAnnotationsByReferenceForProject(int projectRgdId) throws Exception {
        List<Integer> refRgdIds = new ProjectDAO().getReferenceRgdIdsForProject(projectRgdId);
        String refRgdIdsStr = Utils.buildInPhrase(refRgdIds);
        String query = "SELECT a.*, r.species_type_key \n" +
                "FROM full_annot a, rgd_ids r, references ref \n" +
                "WHERE a.ref_rgd_id IN ("+ refRgdIdsStr + ") \n" +
                "AND a.annotated_object_rgd_id = r.rgd_id \n" +
                "AND a.ref_rgd_id = ref.rgd_id \n" +
                "AND r.object_status = 'ACTIVE' \n" +
                "ORDER BY a.object_symbol ASC";
        return executeAnnotationQuery(query);
    }


    /**
     * get annotations given reference rgd id
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @param src source of annotations
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsByReferenceSource(int refRgdId, String src) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n"+
                "FROM full_annot a,rgd_ids r, references ref \n"+
                "WHERE ref_rgd_id=? \n" +
                "AND a.data_src=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n" +
                "ORDER BY a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, src);
    }

    /**
     * get count of annotations in phenominer given reference rgd id
     * @param refRgdId reference rgd id
     * @return number of annotations
     * @throws Exception on spring framework dao failure
     */
    public int getPhenoAnnotationsCountByReference(int refRgdId) throws Exception {
        String query = "SELECT COUNT(*) FROM study s, experiment ex, experiment_record_view er\n" +
                "WHERE s.ref_rgd_id=? \n" +
                "AND s.study_id=ex.study_id AND ex.experiment_id=er.experiment_id";
        return getCount(query, refRgdId);
    }
    public int getPhenoAnnotationsCountByReferenceForProject(int projectRgdId) throws Exception {
        List<Integer> refRgdIds = new ProjectDAO().getReferenceRgdIdsForProject(projectRgdId );
        if(refRgdIds.isEmpty()) {
            return 0;
        }
        String refRgdIdsStr = Utils.buildInPhrase(refRgdIds);
        String query = "SELECT COUNT(*) FROM study s, experiment ex, experiment_record_view er\n" +
                "WHERE s.ref_rgd_id IN("+refRgdIdsStr+") \n" +
                "AND s.study_id=ex.study_id AND ex.experiment_id=er.experiment_id";
        return getCount(query);
    }

    /**
     * get annotations by Reference and limit by evidence code
     * @param refRgdId reference rgd id
     * @param evidence evidence code
     * @return list of Annotation objects
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByReferenceAndEvidence(int refRgdId, String evidence) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r \n" +
                "WHERE ref_rgd_id=? \n" +
                " AND a.evidence=? \n" +
                " AND a.annotated_object_rgd_id=r.rgd_id \n" +
                " AND r.object_status='ACTIVE'";
        return executeAnnotationQuery(query, refRgdId, evidence);
    }
    /**
     * get annotations by Reference and limit by evidence code
     * @param refRgdId reference rgd id
     * @param termAcc termAcc
     * @return list of Annotation objects
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByReferenceAndTermAcc(int refRgdId, String termAcc) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r \n" +
                "WHERE ref_rgd_id=? \n" +
                " AND a.term_acc=? \n" +
                " AND a.annotated_object_rgd_id=r.rgd_id \n" +
                " AND r.object_status='ACTIVE' \n" +
                "order by a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, termAcc);
    }
    /**
     * get annotations by Reference and limit by aspect
     * @param refRgdId
     * @param aspect
     * @return list of annottaion objects.
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByReference(int refRgdId, String aspect) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r, ONTOLOGIES o, references ref \n" +
                "WHERE ref_rgd_id=? \n" +
                "and a.ASPECT=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "and a.ASPECT=o.ASPECT \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n"+
                "order by a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, aspect);
    }

    /**
     * get annotations by Reference and limit by aspect
     * @param refRgdId
     * @param aspect
     * @param src source of annotations
     * @return list of annotation objects
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByReference(int refRgdId, String aspect, String src) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r, ONTOLOGIES o, references ref \n" +
                "WHERE ref_rgd_id=? \n" +
                "and a.ASPECT=? \n" +
                "AND a.data_src=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "and a.ASPECT=o.ASPECT \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n"+
                "order by a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, aspect, src);
    }

    /**
     * get annotations by ref rgdid limiting it by rgd object key
     * @param refRgdId
     * @param aspect
     * @param objKey
     * @return
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByReference(int refRgdId, String aspect, int objKey) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r, ONTOLOGIES o, references ref \n" +
                "WHERE ref_rgd_id=? \n" +
                "and a.ASPECT=? \n" +
                "and a.RGD_OBJECT_KEY=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "and a.ASPECT=o.ASPECT \n" +
                "AND a.data_src=? \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n"+
                "order by a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, aspect, objKey);
    }

    /**
     * get annotations by ref rgdid limiting it by rgd object key
     * @param refRgdId
     * @param aspect
     * @param objKey
     * @src source of annotations
     * @return
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsByReference(int refRgdId, String aspect, int objKey, String src) throws Exception {
        String query = "SELECT a.*,r.species_type_key \n" +
                "FROM full_annot a,rgd_ids r, ontologies o, references ref \n" +
                "WHERE ref_rgd_id=? \n" +
                "AND a.aspect=? \n" +
                "and a.RGD_OBJECT_KEY=? \n" +
                "AND a.data_src=? \n" +
                "AND a.annotated_object_rgd_id=r.rgd_id \n" +
                "and a.ASPECT=o.ASPECT \n" +
                "AND a.ref_rgd_id=ref.rgd_id \n" +
                "AND r.object_status='ACTIVE' \n"+
                "order by a.object_symbol asc";
        return executeAnnotationQuery(query, refRgdId, aspect, objKey, src);
    }

    /**
     * get annotations in RGD without reference hard links;
     * reference hard links allow reference report pages to show the annotated data objects;
     * reference hard links should be automatically created by curation software;
     * an annotation has a reference hard-link if it has an entry in RGD_REF_RGD_ID table;
     * @param refType result is limited to annotations with given reference type, f.e. 'JOURNAL ARTICLE'
     * @return list of annotations with given reference type that have missing reference hard links
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsWithMissingReferenceHardLinks(String refType) throws Exception {

        String query = "SELECT a.* FROM full_annot a,references f\n" +
                "WHERE a.ref_rgd_id=f.rgd_id AND (f.ref_key,a.annotated_object_rgd_id) IN(\n" +
                "SELECT f.ref_key,a.annotated_object_rgd_id rgd_id FROM full_annot a,references f,rgd_ids r\n" +
                "WHERE a.ref_rgd_id=f.rgd_id AND r.rgd_id=f.rgd_id AND r.object_status='ACTIVE'\n" +
                " AND f.reference_type=?\n" +
                "MINUS\n" +
                "SELECT rr.ref_key,rr.rgd_id FROM rgd_ref_rgd_id rr\n" +
                ")\n";
        return executeAnnotationQuery(query, refType);
    }

    public int getCountOfAnnotationsForCreatedBy(int createdBy) throws Exception {
        String sql = "SELECT COUNT(*) FROM full_annot WHERE created_by=?";
        return getCount(sql, createdBy);
    }

    /**
     * get count of annotations given reference rgd id
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @return count of annotations
     * @throws Exception on spring framework dao failure
     */
    public int getCountOfAnnotationsByReference(int refRgdId) throws Exception {

        String query = "SELECT COUNT(*) "+
                "FROM full_annot a,rgd_ids r "+
                "WHERE ref_rgd_id=? AND annotated_object_rgd_id=rgd_id AND r.object_status='ACTIVE'";
        return getCount(query, refRgdId);
    }

    /**
     * get count of annotations given reference rgd id and the data source
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @param src source of annotations
     * @return count of annotations
     * @throws Exception on spring framework dao failure
     */
    public int getCountOfAnnotationsByReference(int refRgdId, String src) throws Exception {

        String query = "SELECT COUNT(*) "+
                "FROM full_annot a,rgd_ids r "+
                "WHERE ref_rgd_id=? AND data_src=? AND annotated_object_rgd_id=rgd_id AND r.object_status='ACTIVE'";
        return getCount(query, refRgdId, src);
    }

    /**
     * get count of annotations given reference rgd id, data source and ontology identified by aspect
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @param src source of annotations
     * @param aspect ontology aspect
     * @return count of annotations
     * @throws Exception on spring framework dao failure
     */
    public int getCountOfAnnotationsByReference(int refRgdId, String src, String aspect) throws Exception {

        String query = "SELECT COUNT(*) "+
                "FROM full_annot a,rgd_ids r "+
                "WHERE ref_rgd_id=? AND data_src=? AND aspect=? AND annotated_object_rgd_id=rgd_id AND r.object_status='ACTIVE'";
        return getCount(query, refRgdId, src, aspect);
    }

    /**
     * get count of annotations given reference rgd id and species type key
     * <br> Note: annotations to non-active rgd objects are skipped!
     * @param refRgdId reference rgd id
     * @param speciesTypeKey species type key
     * @return count of annotations
     * @throws Exception on spring framework dao failure
     */
    public int getCountOfAnnotationsByReference(int refRgdId, int speciesTypeKey) throws Exception {

        String query = "SELECT COUNT(*) "+
                "FROM full_annot a,rgd_ids r "+
                "WHERE ref_rgd_id=? AND annotated_object_rgd_id=rgd_id AND r.object_status='ACTIVE' AND r.species_type_key=?";
        return getCount(query, refRgdId, speciesTypeKey);
    }

    /**
     * get annotations by annotated object rgd id
     * @param rgdId annotated object rgd id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotations(int rgdId, String termAcc) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? AND term_acc=? ORDER BY term";
        return executeAnnotationQuery(query, rgdId, termAcc);
    }

    /**
     * get annotations by annotated object rgd id
     * @param rgdId annotated object rgd id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotations(int rgdId) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id=? AND rgd_id=annotated_object_rgd_id "+
                "ORDER BY term";
        return executeAnnotationQuery(query, rgdId);
    }

    public List<Annotation> getAnnotationsForProject(int projectRgdId) throws Exception {
        List<Integer> refRgdIds = new ProjectDAO().getReferenceRgdIdsForProject(projectRgdId );
        if(refRgdIds.isEmpty()) {
            return Collections.emptyList();
        }
        String refRgdIdsStr = Utils.buildInPhrase(refRgdIds);
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id IN ("+refRgdIdsStr+") AND rgd_id=annotated_object_rgd_id "+
                "ORDER BY term";
        return executeAnnotationQuery(query);
    }

    /**
     * get list of (term accession id,term name) pairs for annotations by annotated object rgd id
     *
     * @param rgdId annotated object rgd id
     * @return list of MapPair objects
     * @throws Exception on spring framework dao failure
     */
    public List<StringMapQuery.MapPair> getAnnotationTermAccIds(int rgdId) throws Exception {
        String query = "SELECT DISTINCT term_acc,term FROM full_annot WHERE annotated_object_rgd_id=?";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), query);
        return execute(q, rgdId);
    }



    /**
     * get list of (term accession id,term name) pairs for annotations by annotated object rgd id
     * and ontology
     * @param rgdId annotated object rgd id
     * @param aspect ontology aspect
     * @return list of MapPair objects
     * @throws Exception on spring framework dao failure
     */
    public List<StringMapQuery.MapPair> getAnnotationTermAccIds(int rgdId, String aspect) throws Exception {
        String query = "SELECT DISTINCT term_acc,term FROM full_annot WHERE annotated_object_rgd_id=? AND aspect=?";
        return StringMapQuery.execute(this, query, rgdId, aspect);
    }

    /**
     * get active annotations by species
     * @param speciesTypeKey species type key
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsBySpecies(int speciesTypeKey) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND species_type_key=?";
        return executeAnnotationQuery(query, speciesTypeKey);
    }

    /**
     * get active annotations for given object type by species
     * @param speciesTypeKey species type key
     * @param objectKey object key
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsBySpecies(int speciesTypeKey, int objectKey) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND species_type_key=? AND object_key=?";
        return executeAnnotationQuery(query, speciesTypeKey, objectKey);
    }
    public List<Annotation> getDiseaseAndStrainAnnotationsBySpecies(int speciesTypeKey, int objectKey) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r "+
                "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND species_type_key=? AND object_key=?" +
                "   and aspect in ('D', 'N')";
        return executeAnnotationQuery(query, speciesTypeKey, objectKey);
    }

    public List<Annotation> getAnnotationsBySpecies(int speciesTypeKey, String aspect) throws Exception {
        String sql = "SELECT a.* FROM full_annot a,rgd_ids r\n" +
                "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND aspect=? AND species_type_key=?";
        return executeAnnotationQuery(sql, aspect, speciesTypeKey);
    }

    public List<Annotation> getAnnotationsBySpeciesAspectAndSource(int speciesTypeKey, String aspect, String dataSrc) throws Exception {
        String sql = "SELECT a.* FROM full_annot a,rgd_ids r\n" +
                "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND aspect=? AND species_type_key=? AND data_src=?";
        return executeAnnotationQuery(sql, aspect, speciesTypeKey, dataSrc);
    }

    /**
     * get annotations by annotated object rgd id , accId and speciesTypeKey
     * @param rgdId rgd id for annotated object
     * @param accId term accession id
     * @param speciesTypeKey species type key
     * @return list of Annotation objects; could be empty if there are no annotations for given object
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getObjectAnnotationbyTermAndSpecies(int rgdId, String accId, int speciesTypeKey) throws Exception {
        if(speciesTypeKey==0){
            String query = "SELECT f.*,r.species_type_key FROM FULL_ANNOT f, RGD_IDS r WHERE " +
                    "r.RGD_ID=f.ANNOTATED_OBJECT_RGD_ID AND "+
                    "f.ANNOTATED_OBJECT_RGD_ID=? AND " +
                    "f.TERM_ACC=?";

            return executeAnnotationQuery(query, rgdId, accId);
        }else{
            String query = "SELECT f.*,r.species_type_key FROM FULL_ANNOT f, RGD_IDS r WHERE " +
                    "r.RGD_ID=f.ANNOTATED_OBJECT_RGD_ID AND "+
                    "f.ANNOTATED_OBJECT_RGD_ID=? AND " +
                    "f.TERM_ACC=? "+
                    "AND r.SPECIES_TYPE_KEY=?";

            return executeAnnotationQuery(query, rgdId, accId, speciesTypeKey);
        }
    }

    /**
     * get annotations with stale term names;<p>
     * term names for these annotations are different than term names in ONT_TERMS table;
     * in other words, an annotation has a stale term name if its term name is not up-to-date with ONT_TERMS table
     * @return list of Annotation objects; could be empty
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsWithStaleTermNames() throws Exception {
        String query = "SELECT a.* FROM full_annot a,ont_terms t " +
                "WHERE a.term_acc=t.term_acc AND a.term<>t.term";
        return executeAnnotationQuery(query);
    }

    /**
     * get annotations with stale aspect;<p>
     * aspect for these annotations is different than ontology aspect in ONTOLOGIES table;
     * in other words, an annotation has a stale aspect if its aspect does not match the ontology aspect of the annotated term
     * @return list of Annotation objects; could be empty
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsWithStaleAspect() throws Exception {
        String query = "SELECT a.* FROM full_annot a,ont_terms t,ontologies o " +
                "WHERE a.term_acc=t.term_acc AND t.ont_id=o.ont_id AND a.aspect<>o.aspect";
        return executeAnnotationQuery(query);
    }

    /**
     * get active annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, annotations for child terms are also returned;
     * you can also limit the results to given species
     * @param accId ontology term accession id
     * @param withChildren if true then gene annotations for all child terms are also returned;
     *        if false, only gene annotations for given term are returned
     * @param speciesTypeKey species type key -- if not zero, only annotations for given species are returned
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotations(String accId, boolean withChildren, int speciesTypeKey) throws Exception {

        if( !withChildren ) {
            if( speciesTypeKey==0 ) {
                // annotations for any species
                String query = "SELECT * FROM full_annot,rgd_ids "+
                        "WHERE term_acc=? AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE'";
                return executeAnnotationQuery(query, accId);
            }
            else {
                // annotations for specific species
                String query = "SELECT a.* FROM full_annot a,rgd_ids r WHERE term_acc=? "+
                        "AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key=? AND object_status='ACTIVE'";
                return executeAnnotationQuery(query, accId, speciesTypeKey);
            }
        }
        else {
           if( speciesTypeKey==0 ) {
                // annotations for any species
                String query = "SELECT a.*\n" +
                        " FROM full_annot a,rgd_ids r\n" +
                        "WHERE annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' AND term_acc IN(\n" +
                        "  select ? from dual\n" +
                        "  union all\n" +
                        "  select distinct child_term_acc from ont_dag\n" +
                        "  start with parent_term_acc=?\n" +
                        "  connect by prior child_term_acc=parent_term_acc\n" +
                        ")";

                return executeAnnotationQuery(query, accId, accId);
            }
            else {
                String query = "SELECT a.*\n" +
                        " FROM full_annot a,rgd_ids r\n" +
                        "where term_acc in(\n" +
                        "  select ? from dual\n" +
                        "  union all\n" +
                        "  select distinct child_term_acc from ont_dag\n" +
                        "  start with parent_term_acc=?\n" +
                        "  connect by prior child_term_acc=parent_term_acc\n" +
                        ") AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key=? AND object_status='ACTIVE'";

                return executeAnnotationQuery(query, accId, accId, speciesTypeKey);
            }
        }
    }

    /**
     * get annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, annotations for child terms are also returned;
     * you can also limit the results to given species
     * @param accId ontology term accession id
     * @param withChildren if true then gene annotations for all child terms are also returned;
     *        if false, only gene annotations for given term are returned
     * @param speciesTypeKey species type key -- if not zero, only annotations for given species are returned
     * @param maxRows maximum nr of rows returned; it there are more rows available than this limit, they will be ignored
     * @param objectKey object key (to limit the annotations to specific object group, like genes)
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotations(String accId, boolean withChildren, int speciesTypeKey, int maxRows, int objectKey) throws Exception {

        if( !withChildren ) {
            if( speciesTypeKey==0 ) {
                // annotations for any species
                String query = "SELECT * FROM full_annot,rgd_ids "+
                        "WHERE term_acc=? AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE' and object_key=?"+
                        " AND ROWNUM<=?";
                return executeAnnotationQuery(query, accId, objectKey, maxRows);
            }
            else {
                // annotations for specific species
                String query = "select a.* from full_annot a,rgd_ids r where term_acc=? "+
                        "and annotated_object_rgd_id=r.rgd_id and r.species_type_key=? AND object_status='ACTIVE' and object_key=?"+
                        " AND ROWNUM<=?";
                return executeAnnotationQuery(query, accId, speciesTypeKey, objectKey, maxRows);
            }
        }
        else {
            return getAnnotationsWithLimit(accId, speciesTypeKey, objectKey, false);
        }
    }

    /**
     * get annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, annotations for child terms are also returned;
     * you can also limit the results to given species;
     * Note: results are grouped per gene; qualifiers, evidences, references, data_sources and notes are merged
     * ('|'-separator, ref_rgd_ids are merged into relatedTo field)
     * @param accId ontology term accession id
     * @param withChildren if true then gene annotations for all child terms are also returned;
     *        if false, only gene annotations for given term are returned
     * @param speciesTypeKey species type key -- if not zero, only annotations for given species are returned
     * @param maxRows maximum nr of rows returned; it there are more rows available than this limit, they will be ignored
     * @param objectKey object key (to limit the annotations to specific object group, like genes)
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsGroupedByGene(String accId, boolean withChildren, int speciesTypeKey, int maxRows, int objectKey) throws Exception {

        /*
        if( !withChildren ) {
            String query = "SELECT a.*,r.species_type_key FROM full_annot a,rgd_ids r WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=r.rgd_id AND object_status='ACTIVE'";

            List args = new ArrayList();
            args.add(accId);

            if( speciesTypeKey!=0 ) {
                query += "AND r.species_type_key=? ";
                args.add(speciesTypeKey);
            }

            if( objectKey>0 ) {
                query += "AND object_key=? ";
                args.add(objectKey);
            }
            query += "ORDER BY rgd_id, qualifier";

            List<Annotation> annots = executeAnnotationQuery(query, args.toArray());

            return mergeAnnotationsByRgdId(annots, maxRows);
        }
        else {
        */
            return getAnnotationsWithLimit(accId, speciesTypeKey, objectKey, withChildren);
        /* } */
    }

    /*
    List<Annotation> mergeAnnotationsByRgdId(List<Annotation> annots, int maxRows) {

        Annotation ca = new Annotation(); // combined annot to be inserted into results
        ca.setAnnotatedObjectRgdId(0);

        List<Annotation> results = new ArrayList<Annotation>();
        for( Annotation a: annots ) {
            // combine annotation by rgd_id; the above queries are supposed to return rows sorted by rgd_id
            //
            // new rgd_id?
            if(!ca.getAnnotatedObjectRgdId().equals(a.getAnnotatedObjectRgdId())) {
                // move combined annot into results
                if( ca.getAnnotatedObjectRgdId()!=0 ) {
                    results.add(ca);
                    if( results.size()>=maxRows )
                        break;
                }
                // prepare new combined annot
                ca = a;
                if( ca.getRefRgdId()!=null )
                    ca.setRelativeTo(ca.getRefRgdId().toString());
            } else {
                // merge annots

                // merge qualifiers
                if( a.getQualifier()!=null ) {
                    if( ca.getQualifier()==null )
                        ca.setQualifier(a.getQualifier());
                    else if( !ca.getQualifier().contains(a.getQualifier()))
                        ca.setQualifier(ca.getQualifier()+", "+a.getQualifier());
                }

                // merge evidences (they are never null
                if( !ca.getEvidence().contains(a.getEvidence()))
                    ca.setEvidence(ca.getEvidence()+", "+a.getEvidence());

                // merge ref_rgd_ids
                if( a.getRefRgdId()!=null ) {
                    String refRgdId = a.getRefRgdId().toString();
                    if( ca.getRelativeTo()==null )
                        ca.setRelativeTo(refRgdId);
                    else if( !ca.getRelativeTo().contains(refRgdId))
                        ca.setRelativeTo(ca.getRelativeTo()+", "+refRgdId);
                }

                // merge data sources
                if( a.getDataSrc()!=null ) {
                    if( ca.getDataSrc()==null )
                        ca.setDataSrc(a.getDataSrc());
                    else if( !ca.getDataSrc().contains(a.getDataSrc()))
                        ca.setDataSrc(ca.getDataSrc()+", "+a.getDataSrc());
                }

                // merge xref sources
                if( a.getXrefSource()!=null ) {
                    for( String xref: a.getXrefSource().split("[\\|]") ) {
                        if (ca.getXrefSource() == null)
                            ca.setXrefSource(xref);
                        else if (!ca.getXrefSource().contains(xref))
                            ca.setXrefSource(ca.getXrefSource() + "|" + xref);
                    }
                }

                // merge notes
                if( a.getNotes()!=null ) {
                    if( ca.getNotes()==null )
                        ca.setNotes(a.getNotes());
                    else if( !ca.getNotes().contains(a.getNotes()))
                        ca.setNotes(ca.getNotes()+"; "+a.getNotes());
                }
            }
        }
        // move combined annot into results
        if( ca.getAnnotatedObjectRgdId()!=0 && results.size()<maxRows )
            results.add(ca);
        return results;
    }
    */
    /**
     * get annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, annotations for child terms are also returned;
     * you can also limit the results to given species
     * @param accId ontology term accession id
     * @param withChildren if true then gene annotations for all child terms are also returned;
     *        if false, only gene annotations for given term are returned
     * @param speciesTypeKey species type key -- if not zero, only annotations for given species are returned
     * @param maxRows maximum nr of rows returned; it there are more rows available than this limit, they will be ignored
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotations(String accId, boolean withChildren, int speciesTypeKey, int maxRows) throws Exception {
        return getAnnotations(accId, withChildren, speciesTypeKey, maxRows, -1);
    }


    List<Annotation> getAnnotationsWithLimit(String accId, int speciesTypeKey, int objectKey, boolean withChildren) throws Exception {

        String query = "SELECT fa.*, ri.species_type_key FROM full_annot_index fai, full_annot fa, rgd_ids ri " +
            " WHERE fa.annotated_object_rgd_id=ri.rgd_id AND ri.object_key=? AND ri.object_status='ACTIVE' ";

        if (!withChildren) {
            query += " AND fa.term_acc=fai.term_acc";
        }

        if (speciesTypeKey !=0) {
            query += " AND ri.species_type_key=" + speciesTypeKey;
        }

        query += " AND fai.term_acc=? AND fa.full_annot_key=fai.full_annot_key ORDER BY fa.term_acc, annotated_object_rgd_id";

        List<Annotation> annotList = executeAnnotationQuery(query, objectKey, accId);
        return annotList;
    }

    /**
     * get count of active gene annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, count of annotations for child terms are also returned;
     * the results are limited to given species
     * @param accId ontology term accession id
     * @param withChildren if true then count of gene annotations for all child terms are also returned;
     *        if false, only count of gene annotations for given term are returned
     * @param speciesTypeKey species type key filter -- mandatory
     * @return count of gene annotations found
     * @throws Exception on spring framework dao failure
     */
    public int getAnnotationCount(String accId, boolean withChildren, int speciesTypeKey) throws Exception {

        if( !withChildren ) {
            String query = "SELECT COUNT(1) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE'";

            return getCount(query, accId, speciesTypeKey);
        }
        else {
            String query = "select count(1)\n" +
                    " from full_annot a,rgd_ids r\n" +
                    "where term_acc in(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE'";

            return getCount(query, accId, accId, speciesTypeKey);
        }
    }

    /**
     * get count of active gene annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, count of annotations for child terms are also returned;
     * the results are limited to given species
     * @param accId ontology term accession id
     * @param withChildren if true then count of gene annotations for all child terms are also returned;
     *        if false, only count of gene annotations for given term are returned
     * @return count of gene annotations found
     * @throws Exception on spring framework dao failure
     */
    public int getAnnotationCount(String accId, boolean withChildren) throws Exception {

        if( !withChildren ) {
            String query = "SELECT COUNT(1) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE'";

            return getCount(query, accId);
        }
        else {
            String query = "select count(1)\n" +
                    " from full_annot a,rgd_ids r\n" +
                    "where term_acc in(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE'";

            return getCount(query, accId, accId);
        }
    }

    /**
     * get count of active gene annotations for term identified by term accession id;
     * if 'withChildren' parameter is true, count of annotations for child terms are also returned;
     * the results are limited to given species
     * @param accId ontology term accession id
     * @param withChildren if true then count of gene annotations for all child terms are also returned;
     *        if false, only count of gene annotations for given term are returned
     * @param speciesTypeKey species type key filter -- mandatory
     * @return count of gene annotations found
     * @throws Exception on spring framework dao failure
     */
    public int getAnnotationCount(String accId, boolean withChildren, int speciesTypeKey, int objectTypeKey) throws Exception {

        if( !withChildren ) {
            String query = "SELECT COUNT(1) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";

            return getCount(query, accId, speciesTypeKey);
        }
        else {
            String query = "select count(1)\n" +
                    " from full_annot a,rgd_ids r\n" +
                    "where term_acc in(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";

            return getCount(query, accId, accId, speciesTypeKey, objectTypeKey);
        }
    }

    /**
     *
     * @param accId term accession id
     * @param withChildren
     * @param speciesTypeKey species type key
     * @param objectTypeKey if 0, any kind of object will match
     * @return
     * @throws Exception
     */
    public int getAnnotatedObjectCount(String accId, boolean withChildren, int speciesTypeKey, int objectTypeKey) throws Exception {

        String query;
        if( !withChildren ) {
            query = "SELECT COUNT(distinct(rgd_id)) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE'";
        }
        else {
            query = "SELECT COUNT(DISTINCT(rgd_id)) \n" +
                    "FROM full_annot_index i,full_annot a,rgd_ids r \n" +
                    "WHERE i.term_acc=? AND i.full_annot_key=a.full_annot_key AND annotated_object_rgd_id=rgd_id\n" +
                    "  AND species_type_key=? AND object_status='ACTIVE'";
        }
        if( objectTypeKey!=0 ) {
            query += " AND object_key=?";
            return getCount(query, accId, speciesTypeKey, objectTypeKey);
        }
        return getCount(query, accId, speciesTypeKey);
    }

    public List<String> getAnnotatedSymbols(List accIds, int speciesTypeKey, int objectKey) throws Exception {

        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol from full_annot fa, full_annot_index fai, rgd_ids ri where ri.rgd_id = fa.annotated_object_rgd_id \n" +
                "AND ri.object_status='ACTIVE' AND ri.species_type_key=? AND fa.rgd_object_key=? AND fai.term_acc IN(";

        query += Utils.buildInPhraseQuoted(accIds);

        query += ") and fa.full_annot_key=fai.full_annot_key order by object_symbol";

        logger.debug("---\n" + query + "\n-----");

        System.out.println(query);

        return StringListQuery.execute(this, query, speciesTypeKey, objectKey);
    }

    public List getAnnotatedGenes(List accIds, int speciesTypeKey) throws Exception {

        int objectKey=1;

        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol, g.*, ri.species_type_key  from full_annot fa, full_annot_index fai, rgd_ids ri, genes g where ri.rgd_id = fa.annotated_object_rgd_id \n" +
                "and ri.rgd_id=g.rgd_id and ri.object_status='ACTIVE' and ri.species_type_key=" + speciesTypeKey + " and fa.rgd_object_key=" + objectKey + " AND fai.term_acc IN(";

        query += Utils.buildInPhraseQuoted(accIds);

        query += ") and fa.full_annot_key=fai.full_annot_key order by object_symbol";

        logger.debug("---\n" + query + "\n-----");

        System.out.println(query);

        GeneQuery slq = new GeneQuery(this.getDataSource(), query);
        return slq.execute();
    }


    public List getAnnotatedGenesLazyLoad(List accIds, int speciesTypeKey) throws Exception {

        int objectKey=1;

        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol as gene_symbol, ri.species_type_key, ri.rgd_id  from full_annot fa, full_annot_index fai, rgd_ids ri where ri.rgd_id = fa.annotated_object_rgd_id \n" +
                "and ri.object_status='ACTIVE' and ri.species_type_key=" + speciesTypeKey + " and fa.rgd_object_key=" + objectKey + " AND fai.term_acc IN(";

        query += Utils.buildInPhraseQuoted(accIds);

        query += ") and fa.full_annot_key=fai.full_annot_key order by object_symbol";

        logger.debug("---\n" + query + "\n-----");

        System.out.println(query);

        GeneQueryLazyLoad slq = new GeneQueryLazyLoad(this.getDataSource(), query);
        return slq.execute();
    }



    public List getAnnotatedQTLS(List accIds, int speciesTypeKey) throws Exception {

        int objectKey=6;

        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol, q.*, ri.species_type_key  from full_annot fa, full_annot_index fai, rgd_ids ri, qtls q where ri.rgd_id = fa.annotated_object_rgd_id \n" +
                "and ri.rgd_id=q.rgd_id and ri.object_status='ACTIVE' and ri.species_type_key=" + speciesTypeKey + " and fa.rgd_object_key=" + objectKey + " AND fai.term_acc IN(";

        query += Utils.buildInPhraseQuoted(accIds);

        query += ") and fa.full_annot_key=fai.full_annot_key order by object_symbol";

        logger.debug("---\n" + query + "\n-----");

        QTLQuery slq = new QTLQuery(this.getDataSource(), query);
        return slq.execute();
    }

    public List getAnnotatedStrains(List accIds, int speciesTypeKey) throws Exception {

        logger.debug("in top of annotated strains");


        int objectKey=5;
        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol, s.*, ri.species_type_key  from full_annot fa, full_annot_index fai, rgd_ids ri, strains s where ri.rgd_id = fa.annotated_object_rgd_id \n" +
                "and ri.rgd_id = s.rgd_id and ri.object_status='ACTIVE' and ri.species_type_key=" + speciesTypeKey + " and fa.rgd_object_key=" + objectKey + " AND fai.term_acc IN(";

        query += Utils.buildInPhraseQuoted(accIds);

        query += ") and fa.full_annot_key=fai.full_annot_key order by object_symbol";

        logger.debug("---\n" + query + "\n-----");


        StrainQuery slq = new StrainQuery(this.getDataSource(), query);
        return slq.execute();
    }

    public List<String> getAnnotatedGeneSymbols(List accIds, int speciesTypeKey) throws Exception {

        if (accIds.size()==0) throw new Exception("Must pass a term");

        String query = "select distinct object_symbol from full_annot fa, full_annot_index fai, rgd_ids ri where ri.rgd_id = fa.annotated_object_rgd_id \n" +
            "AND ri.species_type_key=? AND fa.rgd_object_key=1 AND fai.term_acc IN(" + Utils.buildInPhraseQuoted(accIds) +
            ") AND fa.full_annot_key=fai.full_annot_key  ORDER BY object_symbol";

        logger.debug("---\n" + query + "\n-----");

        return StringListQuery.execute(this, query, speciesTypeKey);
    }

 	public List<String> getAnnotatedGeneSymbols(List accIds, int mapKey, String chromosome) throws Exception {

        String query = "select distinct object_symbol from full_annot fa, full_annot_index fai, maps_data md " +
            "where fa.annotated_object_rgd_id = md.rgd_id and md.chromosome=? and fa.rgd_object_key=1 and md.map_key=? and fai.term_acc in ("+
            Utils.buildInPhraseQuoted(accIds) + ") and fa.full_annot_key=fai.full_annot_key";

        logger.debug(query);

        return StringListQuery.execute(this, query, chromosome, mapKey);
 	}


    public List<String> getAnnotatedGeneSymbols(List accIds, int mapKey, String chromosome, Integer start, Integer stop) throws Exception {

        if (start == null || stop == null) {
            return getAnnotatedGeneSymbols(accIds, mapKey, chromosome);
        }

        String query = "SELECT DISTINCT object_symbol FROM full_annot fa, full_annot_index fai, maps_data md  " +
            "WHERE fa.annotated_object_rgd_id = md.rgd_id and md.chromosome=? AND md.start_pos<? AND md.stop_pos>? \n" +
            " AND fa.rgd_object_key=1 AND md.map_key=? AND fai.term_acc IN(" + Utils.buildInPhraseQuoted(accIds) +
            ") AND fa.full_annot_key=fai.full_annot_key";

        logger.debug(query);

        return StringListQuery.execute(this, query, chromosome, stop, start, mapKey);
    }

    public List<Integer> getAnnotatedObjectIds(String accId, boolean withChildren, int speciesTypeKey, int objectTypeKey) throws Exception {

        if( !withChildren ) {
            String query = "SELECT distinct(rgd_id) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";

            return IntListQuery.execute(this, query, accId, speciesTypeKey, objectTypeKey);
        }
        else {
            String query = "select distinct(rgd_id)\n" +
                    " from full_annot a,rgd_ids r\n" +
                    "where term_acc in(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";
            return IntListQuery.execute(this, query, accId, accId, speciesTypeKey, objectTypeKey);
        }
    }

    public List<IntStringMapQuery.MapPair> getAnnotatedObjectIdsAndTerms(String accId, int speciesTypeKey) throws Exception {

        String query = "SELECT rgd_id,term_acc\n" +
                " FROM full_annot a,rgd_ids r\n" +
                "WHERE term_acc IN(\n" +
                "  select ? from dual\n" +
                "  union all\n" +
                "  select distinct child_term_acc from ont_dag\n" +
                "  start with parent_term_acc=?\n" +
                "  connect by prior child_term_acc=parent_term_acc\n" +
                ") AND annotated_object_rgd_id=r.rgd_id AND r.species_type_key=? AND object_status='ACTIVE'";

        return IntStringMapQuery.execute(this, query, accId, accId, speciesTypeKey);
    }

    public List<StringMapQuery.MapPair> getAnnotatedObjectIdsAndSymbols(String accId, boolean withChildren, int speciesTypeKey, int objectTypeKey) throws Exception {

        if( !withChildren ) {
            String query = "SELECT DISTINCT rgd_id, object_symbol FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";
            return StringMapQuery.execute(this, query, accId, speciesTypeKey, objectTypeKey);
        }
        else {
            String query = "SELECT DISTINCT rgd_id, object_symbol\n" +
                    " FROM full_annot a,rgd_ids r\n" +
                    "WHERE term_acc IN(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND species_type_key=? AND object_status='ACTIVE' and object_key=?";
            return StringMapQuery.execute(this, query, accId, accId, speciesTypeKey, objectTypeKey);
        }
    }

    /**
     * get top-level terms for terms, that are annotated to given object
     * @param rgdId rgd id of object
     * @param aspect aspect of ontology
     * @return
     * @throws Exception
     */
    public List<StringMapQuery.MapPair> getAnnotatedTopLevelTerms(int rgdId, String aspect) throws Exception {

        String query = "SELECT term_acc,term FROM ont_terms \n" +
                "WHERE term_acc IN(\n" +
                "        SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=(SELECT root_term_acc FROM ontologies WHERE aspect=?)\n" +
                ") AND EXISTS (\n" +
                "SELECT 1 FROM( SELECT parent_term_acc FROM ont_dag\n" +
                "            START WITH child_term_acc IN\n" +
                "            (SELECT term_acc FROM full_annot WHERE aspect=? AND annotated_object_rgd_id=?)\n" +
                "            CONNECT BY PRIOR parent_term_acc=child_term_acc\n" +
                ") WHERE parent_term_acc=term_acc\n" +
                ")\n";
        return StringMapQuery.execute(this, query, aspect, aspect, rgdId);
    }

    /**
     * get second-level terms for terms, that are annotated to given object
     * @param rgdId
     * @param aspect
     * @return
     * @throws Exception
     */
    public List<StringMapQuery.MapPair> getAnnotatedTopLevel2Terms(int rgdId, String aspect) throws Exception {

        String query = "SELECT term_acc,term FROM ont_terms \n" +
                "WHERE term_acc IN(\n" +
                "  SELECT child_term_acc FROM ont_dag WHERE parent_term_acc IN(\n"+
                "        SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=(SELECT root_term_acc FROM ontologies WHERE aspect=?)\n" +
                "  )\n"+
                ") AND EXISTS (\n" +
                "SELECT 1 FROM( SELECT parent_term_acc FROM ont_dag\n" +
                "            START WITH child_term_acc IN\n" +
                "            (SELECT term_acc FROM full_annot WHERE aspect=? AND annotated_object_rgd_id=?)\n" +
                "            CONNECT BY PRIOR parent_term_acc=child_term_acc\n" +
                ") WHERE parent_term_acc=term_acc\n" +
                ")\n";
        return StringMapQuery.execute(this, query, aspect, aspect, rgdId);
    }

    public int getAnnotatedObjectCount(String accId, boolean withChildren) throws Exception {

        if( !withChildren ) {
            String query = "SELECT COUNT(distinct(rgd_id)) FROM full_annot,rgd_ids WHERE term_acc=? "+
                    "AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE'";

            return getCount(query, accId);

        }
        else {
            String query = "select count(distinct(rgd_id))\n" +
                    " from full_annot a,rgd_ids r\n" +
                    "where term_acc in(\n" +
                    "  select ? from dual\n" +
                    "  union all\n" +
                    "  select distinct child_term_acc from ont_dag\n" +
                    "  start with parent_term_acc=?\n" +
                    "  connect by prior child_term_acc=parent_term_acc\n" +
                    ") AND annotated_object_rgd_id=rgd_id AND object_status='ACTIVE'";

            return getCount(query, accId, accId);
        }
    }

    /**
     * get annotations by annotated object rgd id and aspect
     * @param rgdId annotated object rgd id
     * @param aspect aspect
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<Annotation> getAnnotationsByAspect(int rgdId, String aspect) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id=? AND aspect=? ORDER BY term";
        return executeAnnotationQuery(query, rgdId, aspect.toUpperCase());
    }

    /**
     * Get Annotations by list of annotated object rgd ids and aspect;
     * @param rgdIdsList
     * @param aspect
     * @return
     * @throws Exception
     */
    public List<Annotation> getAnnotationsByRgdIdsListAndAspect(List<Integer> rgdIdsList, String aspect) throws Exception {
        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id in (" ;
        query += Utils.buildInPhrase(rgdIdsList);
        query += ")  AND aspect=? ORDER BY term";

        return executeAnnotationQuery(query,aspect);
    }

    public HashMap getCommonAnnotationTermCounts(List<Integer> rgdIds, String aspect) throws Exception {
        HashMap<String, Integer> counts = new HashMap<>();
        if (rgdIds.isEmpty()) {
            return counts;
        }

        String query = "SELECT * FROM full_annot WHERE annotated_object_rgd_id IN ("
            + Utils.buildInPhrase(rgdIds)
            + ") AND aspect=?";

        List<Annotation> annots = executeAnnotationQuery(query, aspect.toUpperCase());

        OntologyXDAO oDao = new OntologyXDAO();

        HashMap processed = new HashMap();

        for (Annotation annot : annots) {

            String key = annot.getTermAcc() + annot.getAnnotatedObjectRgdId();
            if (processed.get(key) != null) {
                continue;
            }

            // increment count
            Integer count = counts.get(annot.getTerm());
            if (count==null) {
                count = 0;
            }
            counts.put(annot.getTerm(), count+1);

            processed.put(key, true);

            Map<String, Relation> map = oDao.getTermAncestors(annot.getTermAcc());
            for (String termAcc: map.keySet()) {
                Term term = oDao.getTerm(termAcc);

                String parent = term.getTerm();

                if (processed.get(term.getAccId() + annot.getAnnotatedObjectRgdId()) == null) {
                    if (counts.get(parent) == null) {
                        counts.put(parent, 1);
                    } else {
                        counts.put(parent, counts.get(parent) + 1);
                    }
                    processed.put(term.getAccId() + annot.getAnnotatedObjectRgdId(), true);
                }
            }

        }

        return counts;
    }

    /**
     * Returns ontology enrichment object used by the ga tool
     * @param rgdIds list of rgd ids
     * @return
     * @throws Exception
     */
    public OntologyEnrichment getOntologyEnrichment(List<Integer> rgdIds) throws Exception {
        return this.getOntologyEnrichment(rgdIds, null,null);
    }

    /**
     * build sql IN () phrase for list of rgd_ids; the list could be up to 3,000 long;
     * overcomes internal Oracle limit of 1,000 rgd_ids
     * @param rgdIds list of up-to 3,000 rgd ids
     * @param tableField table field, like 'gi.annotated_object_rgd_id'
     * @param prefix, f.e. " AND "
     * @return partial sql query
     */
    private String buildInPhrase1000(List<Integer> rgdIds, String tableField, String prefix) {

        String inPhrase = "";
        if (rgdIds != null && rgdIds.size() > 0) {
            StringBuilder buf = new StringBuilder();
            if (rgdIds.size() <= 1000) {
                buf.append(prefix).append(" ").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds)).append(") ");
            }else if (rgdIds.size() <= 2000) {
                buf.append(prefix).append(" (").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds.subList(0, 1000))).append(") OR ");
                buf.append(" ").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds.subList(1000, rgdIds.size()))).append(")) ");
            }else {
                buf.append(prefix).append(" (").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds.subList(0, 1000))).append(") OR ");
                buf.append(" ").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds.subList(1000, 2000))).append(") OR ");
                buf.append(" ").append(tableField).append(" IN (").append(Utils.buildInPhrase(rgdIds.subList(2000, rgdIds.size()))).append(")) ");
            }
            inPhrase = buf.toString();
        }
        return inPhrase;
    }

    /**
     * Returns ontology enrichment object used by the ga tool
     * @param rgdIds
     * @param termAccs
     * @param aspects
     * @return
     * @throws Exception
     */
    public LinkedHashMap<String,Integer> getGeneCounts(List<Integer> rgdIds, List<String> termAccs, List<String> aspects) throws Exception {
        LinkedHashMap hm = new LinkedHashMap();
        int i = 0, j = 0;
        int size = rgdIds.size();
        for( i=0; i < size; i++ ) {

            if ((i % 999 == 0 && i != 0) || i == size-1) {
                if( i == size -1) {
                    if( i > 999)
                    j += 999;
                    i += 1;
                } else j = i - 999;
                List<Integer> idList = rgdIds.subList(j, i);
                String query = "SELECT COUNT(*) as tcount, term_acc FROM ga_index WHERE (";

                query += buildInPhrase1000(idList, "annotated_object_rgd_id", "");

                if (aspects != null && aspects.size() > 0) {
                    query += " AND aspect IN(" + Utils.buildInPhraseQuoted(aspects) + ")) ";
                }

                query += "GROUP BY term_acc ORDER BY tcount desc, term_acc ";

                GeneCountQuery gcq = new GeneCountQuery(this.getDataSource(), query);
                gcq.compile();
                List enrichmentList = gcq.execute();

                for (Object list : enrichmentList) {
                    HashMap hm1 = (HashMap) list;
                    for(Object key: hm1.keySet())
                    {
                        String term = (String)key;
                        if(hm.keySet().contains(term)){
                            int count = (int)hm1.get(term);
                            count += (int)hm.get(key);
                            hm.put(key,count);
                        }else
                            hm.put(key,hm1.get(key));
                    }

                }
            }
        }

        return hm;

    }


    /**
     * Returns ontology enrichment object used by the ga tool
     * @param rgdIds list of rgd ids (up to 3,000)
     * @param term1AccId
     * @param term2AccId
     * @return
     * @throws Exception
     */
    public List<String> getOverlap(List<Integer> rgdIds, String term1AccId, String term2AccId) throws Exception {

        String query = "SELECT g.gene_symbol " +
                "FROM ga_index gi, ga_index gi2, genes g " +
                "WHERE gi.annotated_object_rgd_id = gi2.annotated_object_rgd_id " +
                "AND gi.term_acc=? " +
                "AND gi2.term_acc=? " +
                "AND gi.annotated_object_rgd_id = g.rgd_id ";

        query += buildInPhrase1000(rgdIds, "gi.annotated_object_rgd_id", " AND ");

        GeneOverlapQuery q = new GeneOverlapQuery(this.getDataSource(), query);
        return execute(q, term1AccId, term2AccId);
    }



    /**
     * Returns ontology enrichment object used by the ga tool
     * @param rgdIds list of rgd ids (up to 3,000)
     * @param term1AccIds
     * @param term2AccIds
     * @return
     * @throws Exception
     */
    public HashMap<String,Integer> getGeneCountOverlap(List<Integer> rgdIds, List<String> term1AccIds, List<String> term2AccIds) throws Exception {

        String query = "SELECT fai.term_acc as term1, fai2.term_acc as term2, fai.annotated_object_rgd_id from ga_index fai, ga_index fai2 " +
                "WHERE fai.annotated_object_rgd_id = fai2.annotated_object_rgd_id ";

        query +=" AND fai.term_acc IN (" + Utils.buildInPhraseQuoted(term1AccIds) + ") ";

        query +=" AND fai2.term_acc IN (" + Utils.buildInPhraseQuoted(term2AccIds) + ") ";

        query += buildInPhrase1000(rgdIds, "fai.annotated_object_rgd_id", " AND ");

        GeneCountOverlapQuery gcq = new GeneCountOverlapQuery(this.getDataSource(), query);
        gcq.compile();

        List results =  gcq.execute();

        HashMap hm = new HashMap();

        Iterator it = results.iterator();
        while (it.hasNext()) {

            List nxt = (List)it.next();
            String t1 = (String) nxt.get(0);
            String t2 = (String) nxt.get(1);
            //Integer cnt = (Integer) nxt.get(2);

            String combinedKey = t1 + "_" + t2;

            if (hm.containsKey(combinedKey)) {
                hm.put(combinedKey, ((Integer)hm.get(combinedKey)) + 1);
            }else {
                hm.put(combinedKey, 1);
            }
        }


        return hm;
    }


    /**
     * Returns ontology enrichment object used by the ga tool
     * @param rgdIds list of rgd ids (up to 3,000)
     * @param termAccs
     * @param aspects
     * @return
     * @throws Exception
     */
    public OntologyEnrichment getOntologyEnrichment(List<Integer> rgdIds, List<String> termAccs, List<String> aspects) throws Exception {
        int i = 0, j = 0;
        int size = rgdIds.size();
        OntologyEnrichment oe = new OntologyEnrichment();
        List<Enrichment> enrichmentList = new ArrayList<>();
        for( i=0; i < size; i++ ) {

            if ((i % 999 == 0 && i != 0) || i == size-1) {
                if( i == size -1) {
                    if(i > 999)
                    j += 999;
                    i += 1;
                } else j = i - 999;
                List<Integer> idList = rgdIds.subList(j, i);
                String query = "select fa.aspect, ot.term, ot.term_acc, fa.term as root, fa.term_acc as root_acc, fa.object_symbol, fa.annotated_object_rgd_id, fa.evidence " +
                        "from full_annot_index fae, full_annot fa, ont_terms ot ";

                query += "where fa.full_annot_key=fae.full_annot_key and ot.term_acc = fae.term_acc ";

                if (termAccs != null && termAccs.size() > 0) {
                    query += " and ot.term_acc in (" + Utils.buildInPhraseQuoted(termAccs) + ") ";
                }

                query += buildInPhrase1000(idList, "fa.annotated_object_rgd_id", " AND ");


                if (aspects != null && aspects.size() > 0) {
                    query += " and fa.aspect in (" + Utils.buildInPhraseQuoted(aspects) + ") ";
                }

                query += " order by fa.aspect, ot.term, fa.object_symbol ";

                EnrichmentQuery gq = new EnrichmentQuery(this.getDataSource(), query);
                gq.compile();
                enrichmentList.addAll(gq.execute());
            }
        }
        for (Enrichment e : enrichmentList) {

            Term t = new Term();
            t.setAccId(e.getTerm_acc());
            t.setTerm(e.getTerm());

            Gene g = new Gene();
            g.setRgdId(e.getObjectId());
            g.setSymbol(e.getObjectSymbol());

            if (e.getTerm_acc().equals(e.getRoot_acc())) {
                oe.addAssociation(t, g);
            } else {
                Term root = new Term();
                root.setAccId(e.getRoot_acc());
                root.setTerm(e.getRoot());

                oe.addAssociation(t, g, root);
            }
        }
        return oe;
    }

    /**
     * Update annotation object in FULL_ANNOT table
     * <p>Note: annot.getKey() must be a valid full_annot_key
     * <p>Note: annot.createdDate() is ignored and CREATION_DATE won't be updated
     * @param annot Annotation object representing properties to be updated
     * @throws Exception
     * @return number of rows affected by the update
     */
    public int updateAnnotation(Annotation annot) throws Exception{

        // NOTE: LOB fields (like NOTES) must come at the end of bind list, to avoid exception:
        // ORA-24816: Expanded non LONG bind data supplied after actual LONG or LOB column

        String sql = "UPDATE full_annot SET term=?, annotated_object_rgd_id=?, rgd_object_key=?, data_src=?, " +
                "object_symbol=?, ref_rgd_id=?, evidence=?, with_info=?, aspect=?, object_name=?, qualifier=?, " +
                "relative_to=?, last_modified_date=?, term_acc=?, created_by=?, last_modified_by=?, xref_source=?, " +
                "annotation_extension=?, gene_product_form_id=?, notes=?, original_created_date=? " +
                "WHERE full_annot_key=?";

        return update(sql, annot.getTerm(), annot.getAnnotatedObjectRgdId(), annot.getRgdObjectKey(),
                annot.getDataSrc(), annot.getObjectSymbol(), annot.getRefRgdId(), annot.getEvidence(),
                annot.getWithInfo(), annot.getAspect(), annot.getObjectName(), annot.getQualifier(),
                annot.getRelativeTo(), annot.getLastModifiedDate(), annot.getTermAcc(), annot.getCreatedBy(),
                annot.getLastModifiedBy(), annot.getXrefSource(), annot.getAnnotationExtension(),
                annot.getGeneProductFormId(), annot.getNotes(), annot.getOriginalCreatedDate(), annot.getKey());
    }

    /**
     * Update annotation object in FULL_ANNOT table
     * <p>Note: annot.getKey() must be a valid full_annot_key
     * <p>Note: annot.createdDate() is ignored and CREATION_DATE won't be updated
     * @param annots Annotation objects representing properties to be updated
     * @throws Exception
     * @return number of rows affected by the update
     */
    public int updateAnnotationBatch(List<Annotation> annots) throws Exception{

        // NOTE: LOB fields (like NOTES) must come at the end of bind list, to avoid exception:
        // ORA-24816: Expanded non LONG bind data supplied after actual LONG or LOB column

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "UPDATE full_annot SET term=?, annotated_object_rgd_id=?, rgd_object_key=?, data_src=?, " +
                "object_symbol=?, ref_rgd_id=?, evidence=?, with_info=?, aspect=?, object_name=?, qualifier=?, " +
                "relative_to=?, last_modified_date=SYSDATE, term_acc=?, created_by=?, last_modified_by=?, xref_source=?, " +
                "annotation_extension=?, gene_product_form_id=?, notes=?, original_created_date=? " +
                "WHERE full_annot_key=?",
                new int[] {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
                        Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.INTEGER});

        for (Annotation a : annots){
            su.update(a.getTerm(), a.getAnnotatedObjectRgdId(), a.getRgdObjectKey(), a.getDataSrc(), a.getObjectSymbol(),
                    a.getRefRgdId(), a.getEvidence(), a.getWithInfo(), a.getAspect(), a.getObjectName(), a.getQualifier(),
                    a.getRelativeTo(), a.getTermAcc(), a.getCreatedBy(), a.getLastModifiedBy(), a.getXrefSource(),
                    a.getAnnotationExtension(), a.getGeneProductFormId(), a.getNotes(), a.getOriginalCreatedDate(), a.getKey());
        }

       return executeBatch(su);
    }

    /**
     * Insert new annotation into FULL_ANNOT table; full_annot_key will be auto generated from sequence and returned
     * <p>
     * Note: this implementation uses only one roundtrip to database vs traditional approach resulting in double throughput
     * <p>
     * Note: CREATION_DATE and LAST_MODIFIED_DATE are always set to SYSDATE, and incoming CREATION_DATE and LAST_MODIFIED_DATE
     * are ignored; this is done in order annotation watcher in RGD will get useful notifications for new annotations
     * @param annot Annotation object representing column values
     * @throws Exception
     * @return value of new full annot key
     */
    public int insertAnnotation(Annotation annot) throws Exception{

        String sql = "BEGIN INSERT INTO full_annot (term, annotated_object_rgd_id, rgd_object_key, data_src, " +
                " object_symbol, ref_rgd_id, evidence, with_info, aspect, object_name, notes, qualifier, " +
                " relative_to, created_date, last_modified_date, term_acc, created_by, last_modified_by, " +
                " xref_source, annotation_extension, gene_product_form_id, original_created_date, full_annot_key) "+
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?,?,?,?,?,?,?,full_annot_seq.NEXTVAL) "+
                "RETURNING full_annot_key,created_date,last_modified_date INTO ?,?,?; END;";

        try( Connection conn = this.getConnection() ) {
            CallableStatement cs = conn.prepareCall(sql);
            cs.setString(1, annot.getTerm());
            setInt(cs, 2, annot.getAnnotatedObjectRgdId());
            setInt(cs, 3, annot.getRgdObjectKey());
            cs.setString(4, annot.getDataSrc());
            cs.setString(5, annot.getObjectSymbol());
            setInt(cs, 6, annot.getRefRgdId());
            cs.setString(7, annot.getEvidence());
            cs.setString(8, annot.getWithInfo());
            cs.setString(9, annot.getAspect());
            cs.setString(10, annot.getObjectName());
            cs.setString(11, annot.getNotes());
            cs.setString(12, annot.getQualifier());
            cs.setString(13, annot.getRelativeTo());
            cs.setString(14, annot.getTermAcc());
            setInt(cs, 15, annot.getCreatedBy());
            setInt(cs, 16, annot.getLastModifiedBy());
            cs.setString(17, annot.getXrefSource());
            cs.setString(18, annot.getAnnotationExtension());
            cs.setString(19, annot.getGeneProductFormId());
            setTimestamp(cs, 20, annot.getOriginalCreatedDate());

            cs.registerOutParameter(21, Types.INTEGER); // full_annot_key
            cs.registerOutParameter(22, Types.TIMESTAMP); // created_date
            cs.registerOutParameter(23, Types.TIMESTAMP); // last_modified_date

            cs.execute();

            annot.setKey(cs.getInt(21));
            annot.setCreatedDate(cs.getTimestamp(22));
            annot.setLastModifiedDate(cs.getTimestamp(23));
        }

        return annot.getKey();
    }

    public int insertAnnotationsBatch(Collection<Annotation> annots) throws Exception {
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "INSERT INTO full_annot (term, annotated_object_rgd_id, rgd_object_key, data_src, " +
                " object_symbol, ref_rgd_id, evidence, with_info, aspect, object_name, notes, qualifier, " +
                        " relative_to, created_date, last_modified_date, term_acc, created_by, last_modified_by, " +
                        " xref_source, annotation_extension, gene_product_form_id, original_created_date, full_annot_key) "+
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?,?,?,?,?,?,?,?)",
                new int[] {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
                        Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.INTEGER});
        su.compile();
        for (Annotation annot : annots){
            int fullAnnotKey = this.getNextKeyFromSequence("full_annot_seq");
            annot.setKey(fullAnnotKey);

            su.update(annot.getTerm(), annot.getAnnotatedObjectRgdId(), annot.getRgdObjectKey(), annot.getDataSrc(), annot.getObjectSymbol(),
                    annot.getRefRgdId(), annot.getEvidence(), annot.getWithInfo(), annot.getAspect(), annot.getObjectName(), annot.getNotes(),
                    annot.getQualifier(), annot.getRelativeTo(), annot.getTermAcc(), annot.getCreatedBy(), annot.getLastModifiedBy(),
                    annot.getXrefSource(), annot.getAnnotationExtension(), annot.getGeneProductFormId(), annot.getOriginalCreatedDate(), annot.getKey());
        }
        return executeBatch(su);
    }

    /**
     * Update notes for given annotation object; also last_modified_date is set to SYSDATE
     * <p>Much faster than updateAnnotation because notes field is not a part of any index so update is much faster
     * @param fullAnnotKey key uniquely identifying the annotation
     * @param notes new contents of notes field
     * @throws Exception
     * @return number of rows affected by the update
     */
    public int updateAnnotationNotes(int fullAnnotKey, String notes) throws Exception{

        String sql = "UPDATE full_annot SET notes=?,last_modified_date=SYSDATE WHERE full_annot_key=?";
        return update(sql, notes, fullAnnotKey);
    }

    public int reassignAnnotations(String termAccFrom, String termAccTo) throws Exception {
        String sql = "UPDATE full_annot SET term_acc=? WHERE term_acc=?";
        int annotCount = update(sql, termAccTo, termAccFrom);

        sql = "UPDATE full_annot_index SET term_acc=? WHERE term_acc=?";
        update(sql, termAccTo, termAccFrom);

        return annotCount;
    }

    /// Annotation query implementation helper
    public List<Annotation> executeAnnotationQuery(String query, Object ... params) throws Exception {
        return AnnotationQuery.execute(this, query, params);
    }

    /**
     * @deprecated query does not use any passed parameters -- sloppy debug code
     */
    public List<Annotation> getAnnotations(String termAcc, List<Integer> speciesTypeKeys, List<String> evidenceCodes, Integer someInt) throws Exception {

        String sql = "select * from full_annot fa, rgd_ids ri " +
                "where fa.term_acc=?" +
                "and fa.annotated_object_rgd_id=ri.rgd_id " +
                "and ri.species_type_key in (1,2,3) " +
                "and ri.object_key=1 and evidence in ('EXP','IAGP','IDA','IED','IEP','IGI','IMP','IPI','IPM','QTM')";

        return executeAnnotationQuery(sql, termAcc );
    }
    public List<Annotation> getModels(String aspect) throws Exception {
        String sql="select * from full_annot where (qualifier like '%MODEL%' or qualifier like '%odel') and aspect=?";
        return  executeAnnotationQuery(sql, aspect);

    }

    /**
     * get RatStrain models by annotated disease/phenotype search term, strain type
     * @param Term for disease/phenotype search term
     * @param StrainType strain type such as inbred,congenic etc
     * @return list of RatStrain models; could be empty if there is no strain type or related search term
     * @throws Exception on spring framework dao failure
     */

    public List<RatModelWebServiceQuery.test> getAnnotationsByTermAndStrainType(String Term, String StrainType) throws Exception {
        String sql = "SELECT a.object_symbol as strain,a.annotated_object_rgd_id as strain_rgd_id,a.qualifier, a.term as Disease_OR_Phenotype_Term, o.term as With_Conditions, a.evidence, a.ref_rgd_id, s.strain_type_name_lc as strain_type " +
                "FROM full_annot a " +
                "LEFT JOIN ont_terms o ON a.with_info = o.term_acc " +
                "JOIN rgd_ids r ON a.annotated_object_rgd_id = r.rgd_id " +
                "JOIN strains s ON a.annotated_object_rgd_id = s.rgd_id " +
                "WHERE r.object_status = 'ACTIVE' " +
                "AND r.species_type_key = 3 " +
                "AND r.object_key = 5 " +
                "AND LOWER(a.term) LIKE ?";

        if (StrainType != null && !StrainType.isEmpty()) {
            sql += " AND LOWER(s.strain_type_name_lc) = ?";
        }

        String lowerCaseTerm = "%" + Term.toLowerCase() + "%";
        if (StrainType != null && !StrainType.isEmpty()) {
            String lowerCaseStrainType = StrainType.toLowerCase();
            return RatModelWebServiceQuery.execute(this, sql, lowerCaseTerm, lowerCaseStrainType);
        } else {
            return RatModelWebServiceQuery.execute(this, sql, lowerCaseTerm);
        }
    }

}
