package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.Author;
import edu.mcw.rgd.datamodel.Reference;
import edu.mcw.rgd.datamodel.XdbId;
import edu.mcw.rgd.process.Utils;

import java.util.List;

/**
 * @author jdepons
 * @since May 19, 2008
 */
public class ReferenceDAO extends AbstractDAO {

    public List<Reference> getActiveReferences() throws Exception {
        String query = "SELECT ref.*, r.species_type_key FROM references ref, rgd_ids r " +
                "where r.OBJECT_STATUS='ACTIVE' and ref.RGD_ID=r.RGD_ID";
        return executeRefQuery(query);
    }

    public List<Author> getAuthors(int refKey) throws Exception {
        String query = "SELECT a.* FROM rgd_ref_author rra, authors a "+
                "WHERE ref_key=? AND rra.author_key=a.author_key ORDER BY rra.author_order";
        AuthorQuery q = new AuthorQuery(this.getDataSource(), query);
        return execute(q, refKey);
    }

    public List<Reference> getActiveReferences(String keywords, String author, int year) throws Exception {

        String query = "SELECT r.*, i.species_type_key FROM references r, rgd_ids i " +
                "WHERE r.rgd_id=i.rgd_id AND r.ref_key IN ( " +
                "SELECT distinct(r.ref_key) FROM references r, rgd_ref_author k, rgd_ids rgd, authors a " +
                "where r.ref_key=k.ref_key and k.author_key=a.author_key and r.rgd_id=rgd.rgd_id ";

        try {
            Integer.parseInt(keywords);
            query = query + "and r.rgd_id=" + keywords;

        }catch (Exception e){

            if (keywords != null && !keywords.equals("")) {
                query = query + " and (lower(r.title)  like '%" + keywords + "%'  OR lower(r.citation)  like '%" + keywords + "%')";
            }

            if (author !=null && !author.equals("")) {
                query = query + " and lower(a.author_lname) like '%" + author + "%'";
            }

            if (year > 1900) {
                query = query + " and r.pub_date between TO_DATE('01-JAN-" + year + "', 'DD-MON-YYYY') and TO_DATE('01-JAN-" + (year + 1) + "','DD-MON-YYYY') ";
            }


        }

        query = query + ")";

        return executeRefQuery(query);
    }


    /**
     * get reference object given rgd id for this reference
     * @param rgdId reference rgd id
     * @return Reference object
     * @throws Exception thrown if there is no reference with such rgd id
     * @deprecated Exceptions should not be used as replacement for normal logic!
     */
    public Reference getReference(int rgdId) throws Exception {

        Reference ref = getReferenceByRgdId(rgdId);
        if( ref==null ) {
            // marekt: bad code practice: exceptions should not be used as a substitute of normal logic
            throw new Exception("Reference " + rgdId + " not found");
        }
        return ref;
    }

    /**
     * get reference object given rgd id for this reference
     * @param rgdId reference rgd id
     * @return Reference object or null if reference rgd id is invalid
     * @throws Exception if something unexpected happens in the framework
     */
    public Reference getReferenceByRgdId(int rgdId) throws Exception {

        String query = "SELECT ref.*, r.species_type_key FROM references ref, rgd_ids r "+
                "WHERE r.rgd_id=ref.rgd_id AND r.rgd_id=?";

        List<Reference> refs = executeRefQuery(query, rgdId);
        return refs.isEmpty() ? null : refs.get(0);
    }

    /**
     * Returns a Reference Object if the given doi for that reference object exists in the database.
     * @param doi DOI
     * @return Reference Object if present else returns null
     * @throws Exception if something unexpected happens in the framework
     */
    public Reference getReferenceByDOI(String doi) throws Exception {

        String query = "SELECT ref.*, r.species_type_key FROM references ref, rgd_ids r WHERE r.rgd_id=ref.rgd_id AND ref.DOI=?";

        List<Reference> refs = executeRefQuery(query, doi);
        return refs.isEmpty() ? null : refs.get(0);
    }

    /**
     * Returns a Reference Object Rgd Id if the given doi for that reference object exists in the database.
     * @param doi DOI
     * @return Rgd Id of Reference Object if present else returns 0
     * @throws Exception if something unexpected happens in the framework
     */
    public int getReferenceRgdIdByDOI(String doi) throws Exception {

        String query = "SELECT r.rgd_id FROM references r WHERE r.DOI=?";
        List<Integer> refRgdIds = IntListQuery.execute(this, query, doi);
        return refRgdIds.isEmpty() ? 0 : refRgdIds.get(0);
    }


    /**
     * Returns a Reference Object if the given pmid for that reference object exists in the database.
     * @param pmid PubmedId
     * @return Reference Object if present else returns null
     * @throws Exception if something unexpected happens in the framework
     */
    public Reference getReferenceByPubmedId(String pmid) throws Exception {

        String query = "SELECT ref.*, r.species_type_key \n" +
                "FROM references ref, rgd_acc_xdb x, rgd_ids r \n" +
                "WHERE x.xdb_key=" + XdbId.XDB_KEY_PUBMED +
                " AND x.rgd_id=ref.rgd_id AND x.acc_id=?\n" +
                " AND ref.rgd_id=r.rgd_id AND r.object_status='ACTIVE'";

        List<Reference> refs = executeRefQuery(query, pmid);
        return refs.isEmpty() ? null : refs.get(0);
    }

    /**
     * Returns a Reference Object Rgd Id if the given pmid for that reference object exists in the database.
     * @param pmid PubmedId
     * @return Rgd Id of Reference Object if present else returns 0
     * @throws Exception if something unexpected happens in the framework
     */
    public int getReferenceRgdIdByPubmedId(String pmid) throws Exception {

        String query = "SELECT MAX(ref.rgd_id) \n" +
            "FROM references ref, rgd_acc_xdb x, rgd_ids r \n" +
            "WHERE x.xdb_key=" + XdbId.XDB_KEY_PUBMED +
            " AND x.rgd_id=ref.rgd_id AND x.acc_id=?\n" +
            " AND ref.rgd_id=r.rgd_id AND r.object_status='ACTIVE'";

        return getCount(query, pmid);
    }

    /**
     * get PubMed IDs for all active references
     * @return list of external ids
     */
    public List<String> getPubmedIdsForActiveReferences() throws Exception {

        String sql = "SELECT acc_id FROM rgd_acc_xdb x, rgd_ids r \n" +
                "WHERE x.rgd_id=r.rgd_id AND x.xdb_key=2 \n" +
                "AND r.object_status='ACTIVE' AND r.object_key=12";

        return StringListQuery.execute(this, sql);
    }

    /**
     * get {ref-rgd-id, PMID} for all active references
     * @return list of IntStringMapQuery.MapPair objects
     */
    public List<IntStringMapQuery.MapPair> getPubmedIdsAndRefRgdIds() throws Exception {

        String sql = "SELECT x.rgd_id,acc_id FROM rgd_acc_xdb x, rgd_ids r \n" +
                "WHERE x.rgd_id=r.rgd_id AND x.xdb_key=2 \n" +
                "AND r.object_status='ACTIVE' AND r.object_key=12";

        return IntStringMapQuery.execute(this, sql);
    }

    /**
     * get references for given rgd object
     * @param rgdId object rgd id
     * @return List of Reference objects
     * @throws Exception if something unexpected happens in the framework
     */
    public List<Reference> getReferencesForObject(int rgdId) throws Exception {

        String query = "SELECT ref.*, r.species_type_key "+
                "FROM references ref, rgd_ids r, rgd_ref_rgd_id rid "+
                "WHERE r.rgd_id=ref.rgd_id AND rid.ref_key=ref.ref_key AND rid.RGD_ID=?";

        return executeRefQuery(query, rgdId);
    }

    /**
     * get reference count for given rgd object
     * @param rgdId object rgd id
     * @return count of Reference objects for given object
     * @throws Exception if something unexpected happens in the framework
     */
    public int getReferenceCountForObject(int rgdId) throws Exception {

        String query = "SELECT COUNT(ref.rgd_id) "+
                "FROM references ref, rgd_ids r, rgd_ref_rgd_id rid "+
                "WHERE r.rgd_id=ref.rgd_id AND rid.ref_key=ref.ref_key AND rid.rgd_id=?";
        return getCount(query, rgdId);
    }

    /**
     * get rgd ids of references for given rgd object
     * @param rgdId object rgd id
     * @return List of Reference rgd ids
     * @throws Exception if something unexpected happens in the framework
     */
    public List<Integer> getReferenceRgdIdsForObject(int rgdId) throws Exception {

        String query = "SELECT ref.rgd_id "+
                "FROM references ref, rgd_ids r, rgd_ref_rgd_id rid "+
                "WHERE r.rgd_id=ref.rgd_id AND rid.ref_key=ref.ref_key AND rid.rgd_id=?";

        return IntListQuery.execute(this, query, rgdId);
    }

    /**
     * get reference given reference key
     * @param key reference key
     * @return Reference object
     * @throws Exception when reference key is invalid
     */
    public Reference getReferenceByKey(int key) throws Exception {

        String query = "SELECT ref.*, r.species_type_key FROM references ref, rgd_ids r "+
                "WHERE r.rgd_id=ref.rgd_id AND ref.ref_key=?";

        List<Reference> refs = executeRefQuery(query, key);

        if (refs.size() == 0) {
            throw new Exception("Reference key " + key + " not found");
        }
        return refs.get(0);
    }

    /**
     * get reference rgd id given reference key
     * @param refKey reference key
     * @return reference rgd id or 0 if reference key is invalid
     * @throws Exception if something unexpected happens in the framework
     */
    public int getReferenceRgdIdByKey(int refKey) throws Exception {

        String query = "SELECT rgd_id FROM references WHERE ref_key=?";
        List<Integer> refRgdIds = IntListQuery.execute(this, query, refKey);
        return refRgdIds.isEmpty() ? 0 : refRgdIds.get(0);
    }

    /**
     * get references given list of reference keys
     * @param keys reference keys
     * @return List of Reference objects
     * @throws Exception
     */
    public List<Reference> getReferencesByKeyList(List<Integer> keys) throws Exception {

        String query = "SELECT ref.*, r.species_type_key FROM references ref, rgd_ids r "+
                "WHERE r.rgd_id=ref.rgd_id AND ref.ref_key IN("+ Utils.concatenate(keys,",")+")";
        return executeRefQuery(query);
    }

    /**
     * get references for given rgd object
     * @param rgdId object rgd id
     * @return List of Reference objects
     * @throws Exception if something unexpected happens in the framework
     * @deprecated see #getReferencesForObject(int)
     */
    public List<Reference> getObjectReferences(int rgdId) throws Exception {

        return getReferencesForObject(rgdId);
    }

    /**
     * Update reference in the data store based on rgd id
     *
     * @param ref Reference object
     * @return count of rows affected
     * @throws Exception if something unexpected happens in the framework
     */
    public int updateReference(Reference ref) throws Exception{

        String sql = "update References set REF_KEY=?, TITLE=?, EDITORS=?, " +
                "PUBLICATION=?, VOLUME=?, ISSUE=?, PAGES=?, PUB_STATUS=?, PUB_DATE=?, NOTES=?, REFERENCE_TYPE=?, CITATION=?, " +
                "ABSTRACT=?, PUBLISHER=?, PUBLISHER_CITY=?, URL_WEB_REFERENCE=?, DOI=? where RGD_ID=?";

        return upsertReference(ref, sql);
    }

    /**
     * insert a new reference into database; new reference key is retrieved from REFERENCES_SEQ sequence
     * @param ref Reference object
     * @return count of rows affected
     * @throws Exception if something unexpected happens in the framework
     */
    public int insertReference(Reference ref) throws Exception{

        String sql = "INSERT INTO references (REF_KEY, TITLE, EDITORS, " +
                "PUBLICATION, VOLUME, ISSUE, PAGES, PUB_STATUS, PUB_DATE, NOTES, REFERENCE_TYPE, CITATION, " +
                "ABSTRACT, PUBLISHER, PUBLISHER_CITY, URL_WEB_REFERENCE, DOI, RGD_ID) values " +
                "(?,?,?, ?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?)";

        ref.setKey(this.getNextKeyFromSequence("REFERENCES_SEQ"));

        return upsertReference(ref, sql);
    }

    private int upsertReference(Reference ref, String sql) throws Exception{

        return update(sql, ref.getKey(), ref.getTitle(), ref.getEditors(), ref.getPublication(), ref.getVolume(), ref.getIssue(),
                ref.getPages(), ref.getPubStatus(), ref.getPubDate(), ref.getNotes(), ref.getReferenceType(),
                ref.getCitation(), ref.getRefAbstract(), ref.getPublisher(), ref.getPublisherCity(),
                ref.getUrlWebReference(), ref.getDoi(), ref.getRgdId());
    }

    /**
     * get a list of all reference types
     * @return List of reference types
     * @throws Exception if something unexpected happens in the framework
     */
    public List<String> getReferenceTypes() throws Exception {

        String sql = "SELECT reference_type FROM reference_types";
        return StringListQuery.execute(this, sql);
    }

    /**
     * get a list of all the RGD IDs report pages that are pipelines
     * @return List of the pipeline RGD IDs
     * @throws Exception
     */
    public List<Reference> getAllReferencesByReferenceType(String referenceType) throws Exception {
        String query = "select r.*, s.species_type_key from REFERENCES r, rgd_ids s where r.REFERENCE_TYPE=? and r.rgd_id=s.rgd_id";
        return executeRefQuery(query, referenceType);
    }


    /**
     * find author given unique set of author properties: (last_name, first_name, suffix, initials)
     * @param auth Author object to search
     * @return Author object in RGD
     * @throws Exception
     */
    public Author findAuthor(Author auth) throws Exception{

        String sql = "SELECT * FROM authors "+
                "WHERE NVL(author_lname,'*')=NVL(?,'*')"+
                " AND NVL(author_fname,'*')=NVL(?,'*')"+
                " AND NVL(author_suffix,'*')=NVL(?,'*')"+
                " AND NVL(author_iname,'*')=NVL(?,'*')";

        AuthorQuery q = new AuthorQuery(this.getDataSource(), sql);
        List<Author> authors = execute(q, auth.getLastName(), auth.getFirstName(), auth.getSuffix(), auth.getInitials());
        return authors.isEmpty() ? null : authors.get(0);
    }

    /**
     * insert author data
     * @param auth
     * @return
     * @throws Exception
     */
    public int insertAuthor(Author auth) throws Exception{

        String sql = "INSERT INTO authors (AUTHOR_KEY, AUTHOR_LNAME, AUTHOR_FNAME, " +
                "EMAIL_ADDRESS, INSTITUTION, NOTES, AUTHOR_INAME, AUTHOR_SUFFIX) values " +
                "(?,?,?,?,?,?,?,?)";

        auth.setKey(getNextKeyFromSequence("AUTHORS_SEQ"));

        return update(sql, auth.getKey(), auth.getLastName(), auth.getFirstName(), auth.getEmailAddress(),
            auth.getInstitution(), auth.getNotes(), auth.getInitials(), auth.getSuffix());
    }

    public int updateAuthor(Author auth) throws Exception{

        String sql = "UPDATE authors SET author_lname=?, author_fname=?, email_address=?, institution=?, " +
                "NOTES=?, AUTHOR_INAME=?, AUTHOR_SUFFIX=? where AUTHOR_KEY=?";

        return update(sql, auth.getLastName(), auth.getFirstName(), auth.getEmailAddress(),
            auth.getInstitution(), auth.getNotes(), auth.getInitials(), auth.getSuffix(), auth.getKey());
    }

    /**
     * return author associations for given reference
     * @param refKey key of reference
     * @return List of author keys in order
     * @throws Exception
     */
    public List<Integer> getRefAuthorAssociations(int refKey) throws Exception{
        String sql = "SELECT author_key,author_order FROM rgd_ref_author WHERE ref_key=? ORDER BY author_order";
        return IntListQuery.execute(this, sql, refKey);
    }

    /**
     * insert reference author association for given author key and ref key
     * @param refKey
     * @param authorKey
     * @param authorOrder
     * @return number of rows inserted; return 0 if association already exists
     * @throws Exception
     */
    public int insertRefAuthorAssociation(int refKey, int authorKey, int authorOrder) throws Exception{
        String sql =
                "INSERT INTO rgd_ref_author (ref_key, author_key, author_order) SELECT ?,?,? FROM dual "+
                        "WHERE NOT EXISTS (SELECT 1 FROM rgd_ref_author WHERE ref_key=? AND author_key=? AND author_order=?)";
        return update(sql, refKey, authorKey, authorOrder, refKey, authorKey, authorOrder);
    }

    /**
     * update author in reference author list at given position
     * @param refKey
     * @param authorKeyOld
     * @param authorKeyNew
     * @param authorOrder
     * @return number of rows updated
     * @throws Exception
     */
    public int updateRefAuthorAssociation(int refKey, int authorKeyOld, int authorKeyNew, int authorOrder) throws Exception{
        String sql = "UPDATE rgd_ref_author SET author_key=? WHERE ref_key=? AND author_key=? AND author_order=?";
        return update(sql, authorKeyNew, refKey, authorKeyOld, authorOrder);
    }

    /**
     * delete reference author association for given author key and ref key
     * @param refKey
     * @param authorKey
     * @param authorOrder
     * @return number of rows affected
     * @throws Exception
     */
    public int deleteRefAuthorAssociation(int refKey, int authorKey, int authorOrder) throws Exception{
        String sql = "DELETE FROM rgd_ref_author WHERE ref_key=? AND author_key=? AND author_order=?";
        return update(sql, refKey, authorKey, authorOrder);
    }

    /// Reference query implementation helper
    public List<Reference> executeRefQuery(String query, Object ... params) throws Exception {
        ReferenceQuery q = new ReferenceQuery(this.getDataSource(), query);
        return execute(q, params);
    }
}
