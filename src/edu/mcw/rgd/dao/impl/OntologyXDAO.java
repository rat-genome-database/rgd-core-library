package edu.mcw.rgd.dao.impl;


import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.dao.spring.ontologyx.*;
import edu.mcw.rgd.datamodel.ontologyx.*;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author jdepons
 * @since Jul 14, 2008
 * <p>API to access ontology objects;</p>
 * <b>vocabulary:</b>
 * <dl>
 *     <dt>descendant</dt><dd>a term below given term in the hierarchy; descendant terms are 1 or more hops below the given term</dd>
 *     <dt>ancestor</dt><dd>a term above given term in the hierarchy; ancestor terms are 1 or more hops above the given term</dd>
 *     <dt>child</dt><dd>closest descendant of given term; child terms are always 1 hop below the given term</dd>
 *     <dt>parent</dt><dd>closest ascendant of given term; parent terms are always 1 hop above the given term</dd>
 *     <dt>root</dt><dd>term having no ancestors</dd>
 *     <dt>leaf</dt><dd>term having no descendants</dd>
 *     <dt>orphan</dt><dd>term having no descendants and no ancestors</dd>
 * </dl>
 */
public class OntologyXDAO extends AbstractDAO {

    /**
     * get a term given accession id;
     * accession id is normalized if needed ('RS:1' is changed to 'RS:0000001')
     * throws Exception if there is no term with such accession id
     * @param accId accession id
     * @return Term if accession id is valid; null is never returned
     * @throws Exception thrown when accession id is invalid
     * @see #getTermByAccId
     */
    public Term getTerm(String accId) throws Exception {

        accId = Ontology.formatId(accId);

        Term term = getTermByAccId(accId);

        if (term==null) {
            throw new OntologyXDAOException("Term " + accId + " not found");
        }

        return term;
    }

    /**
     * get an ontology term given term accession id;
     * return null if accession id is invalid
     * @param termAcc term accession id
     * @return Term object if given term found in database or null otherwise
     * @throws Exception if something wrong happens in spring framework
     * @see #getTerm
     */
    public Term getTermByAccId(String termAcc) throws Exception {

        logger.debug("getting term !" + termAcc + "!");

        String sql = "SELECT * FROM ont_terms WHERE term_acc=?";
        List<Term> terms = executeTermQuery(sql, termAcc);
        if( terms.isEmpty() )
            return null;
        return terms.get(0);
    }

    /**
     * get ontology terms given an array of term accession ids;
     * return null if accession id is invalid
     * @param rdoAccId RDO ontology accession id
     * @return An array of term objects if given terms found in database or null otherwise
     * @throws Exception if something wrong happens in spring framework
     * @see #getTerm
     */
    public List<Term> getEquivilentPhenotypeTermsForDisease(String rdoAccId) throws Exception {

        String sql = "SELECT * FROM ont_terms t, ont_ext_relationship oer WHERE oer.term_acc1=? AND oer.term_acc2=t.term_acc";
        return executeTermQuery(sql, rdoAccId);
    }

    /**
     * get equivalent phenotype terms for disease term;
     * return null if accession id is invalid
     * @param termAccs an array of term accession ids
     * @return An array of term objects if given terms found in database or null otherwise
     * @throws Exception if something wrong happens in spring framework
     * @see #getTerm
     */
    public List<Term> getTermByAccId(String[] termAccs) throws Exception {

        String termCondition = constructAccCondition(termAccs, "term_acc");
        String sql = "SELECT * FROM ont_terms WHERE " + termCondition;
        List<Term> terms = executeTermQuery(sql);
        if( terms.isEmpty() )
            return null;
        return terms;
    }


     /**
     * get an ontology term given term accession id;
     * return null if accession id is invalid
     * @param term term name
     * @param ont_id ontology ID
     * @return Term object if given term found in database or null otherwise
     * @throws Exception if something wrong happens in spring framework
     * @see #getTerm
     */
    public Term getTermByTermName(String term, String ont_id) throws Exception {

        String sql = "SELECT * FROM ont_terms WHERE term=? and ont_id=?";
        List<Term> terms = executeTermQuery(sql, term, ont_id);
        if( terms.isEmpty() )
            return null;
        return terms.get(0);
    }

    /**
     * get terms matching given synonym; match could be exact or partial;
     * exact match is when synonym-to-match matches the whole term synonym name;
     * partial match is when synonym-to-match matches part of the term synonym name;
     * <p>
     * Note: match is case insensitive
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymToMatch synonym to match
     * @param matchType either 'exact', 'partial' or 'starts_with'
     * @return List of matching Term objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getTermsBySynonym(String ontologyId, String synonymToMatch, String matchType) throws Exception {

        String query = "SELECT t.* FROM ont_terms t \n"+
                "WHERE ont_id=? AND t.term_acc IN(SELECT term_acc FROM ont_synonyms WHERE LOWER(synonym_name) LIKE ?)";

        if( matchType.equals("partial") ) {
            synonymToMatch = "%"+synonymToMatch+"%";
        }
        else if( matchType.equals("starts_with") ) {
            synonymToMatch = synonymToMatch+"%";
        }

        return executeTermQuery(query, ontologyId, synonymToMatch.toLowerCase());
    }

    /**
     * get terms matching given synonym and synonym type
     * <p>
     * Note: match is case sensitive
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymToMatch synonym to match
     * @param synonymType synonym type
     * @return List of matching Term objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getTermsBySynonymAndType(String ontologyId, String synonymToMatch, String synonymType) throws Exception {

        String query = "SELECT t.* FROM ont_terms t,ont_synonyms s \n"+
                "WHERE ont_id=? AND t.term_acc=s.term_acc AND s.synonym_name=? AND synonym_type=?";

        return executeTermQuery(query, ontologyId, synonymToMatch, synonymType);
    }

    /**
     * get terms matching given synonym and synonym source
     * <p>
     * Note: match is case sensitive
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymToMatch synonym to match
     * @param synonymSource synonym source
     * @return List of matching Term objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getTermsBySynonymAndSource(String ontologyId, String synonymToMatch, String synonymSource) throws Exception {

        String query = "SELECT t.* FROM ont_terms t,ont_synonyms s \n"+
                "WHERE ont_id=? AND t.term_acc=s.term_acc AND s.synonym_name=? AND source=?";

        return executeTermQuery(query, ontologyId, synonymToMatch, synonymSource);
    }

    /**
     * get all active terms in given ontology
     * @param ontologyId ontology id
     * @return List of Term objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getActiveTerms(String ontologyId) throws Exception {

        String query = "SELECT * FROM ont_terms WHERE is_obsolete=0 AND ont_id=?";
        return executeTermQuery(query, ontologyId);
    }

    /**
     * get all terms for given ontology, including obsolete terms
     * @param ontologyId ontology id
     * @return List of Term objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getAllTerms(String ontologyId) throws Exception {

        String query = "SELECT * FROM ont_terms WHERE ont_id=?";
        return executeTermQuery(query, ontologyId);
    }

    /**
     * Check if a term can be used for curation.
     * @param termAcc term ACC id
     * @return true if the term doesn't have a "Not4Curation" synonym
     * @throws Exception if something wrong happens in spring framework
     */
    public boolean isForCuration(String termAcc) throws Exception {
        String sql = "SELECT s.* FROM ont_synonyms s WHERE term_acc=? AND SYNONYM_NAME = 'Not4Curation'";
        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), sql);
        List<TermSynonym> synonyms = execute(q, termAcc);
        return synonyms.size() == 0;
    }

    /**
     * get synonyms for all active terms in given ontology
     * @param ontologyId ontology id
     * @return List of TermSynonym objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getActiveSynonyms(String ontologyId) throws Exception {

        String sql = "SELECT s.* FROM ont_synonyms s,ont_terms t WHERE t.ont_id=? AND t.is_obsolete=0 AND t.term_acc=s.term_acc";
        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), sql);
        return execute(q, ontologyId);
    }

    /**
     * get terms synonyms of given type within a specified ontology
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymType synonym type
     * @return List of matching TermSynonym objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getActiveSynonymsByType(String ontologyId, String synonymType) throws Exception {

        String query;
        query = "SELECT s.* FROM ont_terms t,ont_synonyms s \n"+
                "WHERE ont_id=? AND t.is_obsolete=0 AND t.term_acc=s.term_acc AND synonym_type=?";

        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), query);
        return execute(q, ontologyId, synonymType);
    }

    /**
     * get terms synonyms of given name within a specified ontology
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymName synonym name
     * @return List of matching TermSynonym objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getActiveSynonymsByName(String ontologyId, String synonymName) throws Exception {

        String query;
        query = "SELECT s.* FROM ont_terms t,ont_synonyms s \n"+
                "WHERE ont_id=? AND t.is_obsolete=0 AND t.term_acc=s.term_acc AND synonym_name=?";

        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), query);
        return execute(q, ontologyId, synonymName);
    }

    /**
     * get terms synonyms with name matching given pattern within a specified ontology
     * @param ontologyId id of ontology to be searched for; must not be null
     * @param synonymNamePattern synonym name pattern, f.e. 'KEGG:%'
     * @return List of matching TermSynonym objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getActiveSynonymsByNamePattern(String ontologyId, String synonymNamePattern) throws Exception {

        String query;
        query = "SELECT s.* FROM ont_terms t,ont_synonyms s \n"+
                "WHERE ont_id=? AND t.is_obsolete=0 AND t.term_acc=s.term_acc AND synonym_name LIKE ?";

        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), query);
        return execute(q, ontologyId, synonymNamePattern);
    }

    /**
     * get terms synonyms of given type for term and ascendant terms (parent terms)<br>
     * NOTE: top level terms are NOT searched!
     * @param termAcc term accession
     * @param synonymTypes list of synonym types
     * @return List of matching TermSynonym objects; could be empty
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getSynonymsForTermAndAscendants(String termAcc, List<String> synonymTypes) throws Exception {

        String query;
        query = "SELECT DISTINCT s.* FROM (\n"+
            "  SELECT * FROM ont_dag \n"+
            "  START WITH child_term_acc=? \n"+
            "  CONNECT BY PRIOR parent_term_acc=child_term_acc \n"+
            ")d, ont_synonyms s \n"+
            " WHERE child_term_acc=term_acc AND synonym_type IN("+Utils.buildInPhraseQuoted(synonymTypes)+")";

        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), query);
        return execute(q, termAcc);
    }

    /**
     * find all active terms where 'termPart' is either an accession id, or is part of a term name;
     * search is limited to the particular ontology only
     * @param termPart part of term
     * @param ontologyId ontology id
     * @return list of Term objects; empty list possible
     * @throws Exception if something wrong happens in spring framework
     * @see #searchForTerms
     */
    public List<Term> findTerms(String termPart, String ontologyId) throws Exception {

        String query = "SELECT * FROM ont_terms WHERE (UPPER(term) LIKE ? OR term_acc=?) AND ont_id=? AND is_obsolete=0 ORDER BY term";
        return executeTermQuery(query, "%"+termPart.toUpperCase()+"%", termPart.toUpperCase(), ontologyId);
    }

    /**
     * search for ontology terms by providing a searchString and by specifying
     * how the matching should be performed
     * @param matchType one of: 'contains', 'equals', 'begins', 'ends'
     * @param searchString search string; cannot be null or empty
     * @param searchSynonyms if true search also synonyms
     * @return List of OnTerm objects; null if parameters are invalid
     * @throws Exception if something wrong happens in spring framework
     * @see #findTerms
     */
    public List<Term> searchForTerms(String matchType, String searchString, boolean searchSynonyms) throws Exception {

        // parameter validation and processing

        if( searchString==null || searchString.trim().length()==0 || matchType==null )
            return null;
        String lcSearchString = searchString.toLowerCase();
        String likePhrase =
            matchType.equals("contains") ? "%"+lcSearchString+"%" :
            matchType.equals("equals") ? lcSearchString :
            matchType.equals("ends") ? "%"+lcSearchString :
            matchType.equals("begins") ? lcSearchString+"%" :
            null;
        if( likePhrase==null )
            return null;

        String sql = "SELECT * FROM ont_terms WHERE lower(term) like ? AND is_obsolete=0";
        return executeTermQuery(sql, likePhrase);
    }

    /**
     *
     * @param matchType
     * @param searchString
     * @return
     */
    public String processLikePhrase(String matchType, String searchString){

        // parameter validation and processing
        if( searchString==null || searchString.trim().length()==0 || matchType==null )
            return null;
        //String lcSearchString = searchString.toLowerCase();
        String likePhrase =
            matchType.equals("contains") ? "%"+searchString+"%" :
            matchType.equals("equals") ? searchString :
            matchType.equals("ends") ? "%"+searchString :
            matchType.equals("begins") ? searchString+"%" :
            null;
        if( likePhrase==null )
            return null;

        return likePhrase;
    }

    /**
     * insert ontology term into database if it does not exist;
     * @param term OntTerm object to be inserted
     * @return count of rows affected; 0 if ontology term already existed in the databse
     * @throws Exception if something wrong happens in spring framework
     */
    public int insertTerm(Term term) throws Exception {

        String sql =
                "insert into ONT_TERMS (ONT_ID,TERM_ACC,TERM,IS_OBSOLETE,TERM_DEFINITION,CREATED_BY,CREATION_DATE,TERM_COMMENT) "+
                "select ?,?,?,?,?,?,?,? from DUAL where not exists(select 1 from ONT_TERMS where TERM_ACC=?)";

        return update(sql, term.getOntologyId(), term.getAccId(), term.getTerm(), term.getObsolete(),
                term.getDefinition(), term.getCreatedBy(), term.getCreationDate(), term.getComment(), term.getAccId());
    }

    /**
     * update ontology term in the database
     * @param term Term object to be inserted
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int updateTerm(Term term) throws Exception {

        String sql =
                "update ONT_TERMS set TERM=?,IS_OBSOLETE=?,TERM_DEFINITION=?,CREATED_BY=?,CREATION_DATE=?" +
                ",MODIFICATION_DATE=SYSDATE,TERM_COMMENT=? where TERM_ACC=?";

        return update(sql, term.getTerm(), term.getObsolete(), term.getDefinition(),
                term.getCreatedBy(), term.getCreationDate(), term.getComment(), term.getAccId());
    }

    /**
     * get all direct ascendant (parent) terms of given term
     * @param termAcc child term accession id
     * @return map of ascendant terms: acc ids are the keys, term relations are the values
     * @throws Exception if something wrong happens in spring framework
     */
    public Map<String, Relation> getTermAncestors(String termAcc) throws Exception {
        String sql = "select PARENT_TERM_ACC,ONT_REL_ID from ONT_DAG where CHILD_TERM_ACC=?";
        final Map<String, Relation> results = new HashMap<>();
        MappingSqlQuery q = new MappingSqlQuery(getDataSource(), sql) {
            @Override
            protected Object mapRow(ResultSet rs, int i) throws SQLException {
                results.put(rs.getString(1), Relation.getRelFromRelId(rs.getString(2)));
                return null;
            }
        };
        execute(q, termAcc);
        return results;
    }

    /**
     * get all direct ascendant (parent) terms of given term
     * @param termAcc child term accession id
     * @return list of TermDagEdge objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermDagEdge> getAncestorEdges(String termAcc) throws Exception {
        String sql = "SELECT d.*,t.term parent_term_name FROM ont_dag d,ont_terms t "+
                "WHERE child_term_acc=? AND parent_term_acc=term_acc";
        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        return execute(q, termAcc);
    }

    /**
     * for given term, return all edges between given term and the root term
     * @param termAcc accession id of the term to start with
     * @return list of all parent edges
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermDagEdge> getAllParentEdges(String termAcc) throws Exception {

        String sql = "select distinct d.child_term_acc,d.parent_term_acc,d.ont_rel_id,d.created_date,t.term parent_term_name from (\n" +
                "select d.* from ont_dag d\n" +
                "start with child_term_acc=?\n" +
                "connect by prior parent_term_acc=child_term_acc\n" +
                ")d, ont_terms t where  parent_term_acc=term_acc";

        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        return execute(q, termAcc);
    }

    /**
     * for given a set of terms, return all edges between given term and the root term
     * @param termAccs an array of accession ids of terms to start with
     * @return list of all parent edges
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermDagEdge> getAllParentEdges(String[] termAccs) throws Exception {

        String startCondition = constructAccCondition(termAccs, "child_term_acc");

        String sql = "select distinct d.child_term_acc,d.parent_term_acc,d.ont_rel_id,t.term parent_term_name from (\n" +
                "select d.* from ont_dag d\n" +
                "start with " + startCondition +
                "connect by prior parent_term_acc=child_term_acc\n" +
                ")d, ont_terms t where  parent_term_acc=term_acc";

        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        q.compile();
        return q.execute();
    }

	 /**
      * for given term, return all edges below given term	 
      * @param termAcc accession id of the term to start with	 
      * @return list of all child edges	 
      * @throws Exception if something wrong happens in spring framework	 
      */	 
     public List<TermDagEdge> getAllChildEdges(String termAcc) throws Exception {	 
 	 
         String sql = "select distinct d.child_term_acc,d.parent_term_acc,d.ont_rel_id,t.term parent_term_name from (\n" +	 
                 "select d.* from ont_dag d\n" +	 
                 "start with parent_term_acc=?\n" +	 
                 "connect by prior child_term_acc=parent_term_acc\n" +	 
                 ")d, ont_terms t where child_term_acc=term_acc";	 
				 
         TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
         return execute(q, termAcc);
     }

    public List<TermDagEdge> getAllChildEdges(List<String> termAccList) throws Exception {

        String sql = "select distinct d.child_term_acc,d.parent_term_acc,d.ont_rel_id,t.term parent_term_name from (\n" +
                "select d.* from ont_dag d\n" +
                "start with parent_term_acc in ("+Utils.buildInPhraseQuoted(termAccList)+
                ") connect by prior child_term_acc=parent_term_acc\n" +
                ")d, ont_terms t where child_term_acc=term_acc";
        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        return execute(q);
    }
    /**
     * get custom term relationships (that were defined solely by RGD curators) for active terms
     * @param ontId ontology id, f.e. 'RDO'
     * @return list of TermDagEdgeCustom objects
     * @throws Exception if something wrong happens in spring framework
     */
     public List<TermDagEdgeCustom> getCustomTermRelationships(String ontId) throws Exception {

         String sql = "SELECT d.*,t.term parent_term_name FROM ont_dag_custom d,ont_terms t,ont_terms t2 "+
                "WHERE t.term_acc=d.parent_term_acc AND t.ont_id=? AND t.is_obsolete=0 "+
                " AND t2.term_acc=d.child_term_acc AND t2.is_obsolete=0";

         TermDagEdgeCustomQuery q = new TermDagEdgeCustomQuery(this.getDataSource(), sql);
         return execute(q, ontId);
     }

    /**
     * check if a given term is a descendant, either direct or indirect descendant of a given term;<br>
     * f.e. 'inbred strain' --&gt; 'SS' --&gt; 'SS/Jr' <br>
     *      term 'SS/Jr' is a (direct) descendant of ancestor term 'SS' <br>
     *      term 'SS/Jr' is a (indirect) descendant of ancestor term 'inbred strain' <br>
     * @param termAcc accession id of the term in question
     * @param ancestorTermAcc accession id of the ancestor term
     * @return true if the term is a descendant of the ancestor term
     * @throws Exception if something wrong happens in spring framework
     */
    public boolean isDescendantOf(String termAcc, String ancestorTermAcc) throws Exception {

        String sql = "SELECT COUNT(parent_term_acc) FROM ont_dag \n"+
                "WHERE parent_term_acc=? \n"+
                "START WITH child_term_acc=? \n"+
                "CONNECT BY PRIOR parent_term_acc=child_term_acc";

        return getCount(sql, ancestorTermAcc, termAcc)!=0;
    }

    /**
     * get active (non-obsolete) direct descendant (child) terms of given term;
     * auxiliary term stats are also returned: parent/child term counts and relationship type between given term and the child term;
     * @param termAcc child term accession id
     * @param speciesTypeKey species used to load term stats, 0 to load cumulative stats for all species
     * @return list of child terms with stats info; ordered by term name
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermWithStats> getActiveChildTerms(String termAcc, int speciesTypeKey) throws Exception {

        String sql = "SELECT t.*,ont_rel_id,? species_type_key "+
                "FROM ont_dag d,ont_terms t "+
                "where PARENT_TERM_ACC=? and CHILD_TERM_ACC=t.TERM_ACC and t.IS_OBSOLETE=0 "+
                "ORDER BY t.term";

        TermWithStatsQuery q = new TermWithStatsQuery(this.getDataSource(), sql, null);
        return execute(q, speciesTypeKey,termAcc);
    }

    /**
     * get active (non-obsolete) descendant (child) terms of given term, recursively
     * @param termAcc term accession id
     * @return list of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getAllActiveTermAncestorAccIds(String termAcc) throws Exception {
        String sql = "SELECT t.term_acc FROM ont_terms t "+
                "WHERE term_acc IN( "+
                "  SELECT parent_term_acc FROM ont_dag "+
                "  START WITH child_term_acc=? "+
                "  CONNECT BY PRIOR parent_term_acc=child_term_acc"+
                ") AND is_obsolete=0";
        return StringListQuery.execute(this, sql, termAcc);
    }

    /**
     * get active (non-obsolete) descendant (child) terms of given term, recursively
     * @param termAcc term accession id
     * @return list of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getAllActiveTermAncestors(String termAcc) throws Exception {
        String sql = "SELECT t.* FROM ont_terms t "+
                "WHERE term_acc IN( "+
                "  SELECT parent_term_acc FROM ont_dag "+
                "  START WITH child_term_acc=? "+
                "  CONNECT BY PRIOR parent_term_acc=child_term_acc"+
                ") AND is_obsolete=0";
        return executeTermQuery(sql, termAcc);
    }

    /**
     * get active (non-obsolete) descendant (child) terms of given term, recursively
     * @param termAcc term accession id
     * @return list of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getAllActiveTermDescendants(String termAcc) throws Exception {
        String sql = "SELECT t.* FROM ont_terms t "+
                "WHERE term_acc IN( "+
                "  SELECT child_term_acc FROM ont_dag "+
                "  START WITH parent_term_acc=? "+
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc "+
                ") AND is_obsolete=0";
        return executeTermQuery(sql, termAcc);
    }

    /**
     * get active (non-obsolete) descendant (child) terms of given term, recursively
     * @param termAcc term accession id
     * @return list of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getAllActiveTermDescendantAccIds(String termAcc) throws Exception {
        String sql = "SELECT t.term_acc FROM ont_terms t "+
                "WHERE term_acc IN( "+
                "  SELECT child_term_acc FROM ont_dag "+
                "  START WITH parent_term_acc=? "+
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc "+
                ") AND is_obsolete=0";
        return StringListQuery.execute(this, sql, termAcc);
    }

    /**
     * get count of descendants for given term (including the most remote descendants)
     * @param termAcc term accession id
     * @return count of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public int getCountOfDescendants(String termAcc) throws Exception {

        String sql = "SELECT COUNT(DISTINCT child_term_acc) FROM ont_dag "+
                "  START WITH parent_term_acc=? "+
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc";
        return getCount(sql, termAcc);
    }

    /**
     * get count of ancestors for given term (including most remote ancestors)
     * @param termAcc term accession id
     * @return count of ancestor terms
     * @throws Exception if something wrong happens in spring framework
     */
    public int getCountOfAncestors(String termAcc) throws Exception {

        String sql = "SELECT COUNT(DISTINCT parent_term_acc) FROM ont_dag "+
                "  START WITH child_term_acc=? "+
                "  CONNECT BY PRIOR parent_term_acc=child_term_acc";
        return getCount(sql, termAcc);
    }

    /**
     * get all direct descendant (child) terms of given term
     * @param termAcc child term accession id
     * @return map of descendant terms: acc ids are the keys, term relations are the values
     * @throws Exception if something wrong happens in spring framework
     */
    public Map<String, Relation> getTermDescendants(String termAcc) throws Exception {
        String sql = "select CHILD_TERM_ACC,ONT_REL_ID from ONT_DAG where PARENT_TERM_ACC=?";
        final Map<String, Relation> results = new HashMap<>();
        MappingSqlQuery q = new MappingSqlQuery(getDataSource(), sql) {
            @Override
            protected Object mapRow(ResultSet rs, int i) throws SQLException {
                results.put(rs.getString(1), Relation.getRelFromRelId(rs.getString(2)));
                return null;
            }
        };
        execute(q, termAcc);
        return results;
    }

    public TermWithStats getTermWithStats(String termAcc, String childTermAcc) throws Exception {
        return this.getTermWithStats(termAcc, childTermAcc, null);
    }


    /**
     * get terms with stats given term accession id;
     * @param termAcc term accession id
     * @param childTermAcc optional child term accession id: if given, ont rel id is extracted
     * @return TermWithStats object or null if term acc is not valid
     * @throws Exception if something wrong happens in spring framework
     */
    public TermWithStats getTermWithStats(String termAcc, String childTermAcc, String filter) throws Exception {

        TermWithStats term;
        String sql;
        List<TermWithStats> list;
        if (childTermAcc == null) {
            // no child term given -- relation not possible to detect in general
            sql = "SELECT t.*,'NS' ont_rel_id " +
                    "FROM ont_terms t " +
                    "WHERE t.term_acc=?";

            TermWithStatsQuery q = new TermWithStatsQuery(this.getDataSource(), sql, filter);
            list = execute(q, termAcc);
        } else {
            // parent-child term pair given
            sql = "SELECT t.*,ont_rel_id " +
                    "from ONT_DAG d,ONT_TERMS t " +
                    "where PARENT_TERM_ACC=? and CHILD_TERM_ACC=? and PARENT_TERM_ACC=t.TERM_ACC";

            TermWithStatsQuery q = new TermWithStatsQuery(this.getDataSource(), sql, filter);
            list = execute(q, termAcc, childTermAcc);
        }
        term = list.isEmpty() ? null : list.get(0);
        return term;
    }

    public TermWithStats getTermWithStatsCached(String termAcc, String childTermAcc) throws Exception {
        return this.getTermWithStatsCached(termAcc, childTermAcc, null);
    }

    /**
     * get terms with stats given term accession id; object is returned from cache first, if not in cache, from database
     * @param termAcc term accession id
     * @param childTermAcc optional child term accession id: if given, ont rel id is extracted
     * @return TermWithStats object or null if term acc is not valid
     * @throws Exception if something wrong happens in spring framework
     */
    public TermWithStats getTermWithStatsCached(String termAcc, String childTermAcc, String filter) throws Exception {

        if (filter != null) {
            // create cache key
            String cacheKey = childTermAcc == null ? termAcc + "|" + filter: termAcc + "|" + childTermAcc + "|" + filter;

            TermWithStats result = _termWithStatsCache.get(cacheKey);
            if (result != null) {
                logger.debug("found a result for " + termAcc + " and filter " + filter + " and cache key " + cacheKey);
                return result;
            }
            logger.debug("setting new result for  " + termAcc + " and filter " + filter  + " and cache key " + cacheKey);

            // not in cache
            result = getTermWithStats(termAcc, childTermAcc, filter);

            logger.debug(result.getStats().keySet());

            // ConcurrentHashMap throws an exception when you try to insert a null key
            // we need to protect against it
            if (result != null) {
                _termWithStatsCache.put(cacheKey, result);
            }

            return result;

        }else {


            // create cache key
            String cacheKey = childTermAcc == null ? termAcc : termAcc + "|" + childTermAcc;
            TermWithStats result = _termWithStatsCache.get(cacheKey);
            if (result != null) {
                logger.debug("found a result 2 for " + termAcc + " and filter " + filter  + " and cache key " + cacheKey);
                return result;
            }
            logger.debug("setting new result 2 for  " + termAcc + " and filter " + filter  + " and cache key " + cacheKey);

            // not in cache
            result = getTermWithStats(termAcc, childTermAcc);

            // ConcurrentHashMap throws an exception when you try to insert a null key
            // we need to protect against it
            if (result != null) {
                _termWithStatsCache.put(cacheKey, result);
            }
            return result;
        }

    }
    static Map<String, TermWithStats> _termWithStatsCache = new ConcurrentHashMap<>();

    /**
     * get terms with stats given term accession id; object is returned from cache first, if not in cache, from database
     * @param termAcc term accession id
     * @return TermWithStats object or null if term acc is not valid
     * @throws Exception if something wrong happens in spring framework
     */
    public TermWithStats getTermWithStatsCached(String termAcc) throws Exception {
        return getTermWithStatsCached(termAcc, null);
    }

    public static final int PATH_OPTION_DEFAULT = 0; /// equivalent to PATH_OPTION_ONE_SHORTEST_AND_LONGEST
    public static final int PATH_OPTION_ONE_SHORTEST = 1;
    public static final int PATH_OPTION_ALL_SHORTEST = 2;
    public static final int PATH_OPTION_ONE_LONGEST = 3;
    public static final int PATH_OPTION_ALL_LONGEST = 4;
    public static final int PATH_OPTION_ONE_SHORTEST_AND_LONGEST = 5;
    public static final int PATH_OPTION_ALL = 6; /// up to 50 paths shown

    /**
     * return list of paths from given term to the root
     * @param termAcc accession id of term from which the paths starts
     * @param option which paths should be returned (please use one of OntologyXDAO.PATH_OPTION_ constants):
     * @return null if accession id is invalid; empty list if the term is a root term; at least one path for other terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<List<TermWithStats>> getPathsToRoot(String termAcc, int option) throws Exception {

        List<List<TermWithStats>> pathList = new ArrayList<>();
        if( option==PATH_OPTION_ONE_SHORTEST ) {

            String sql = "select tpath\n" +
                    " from (\n" +
                    "select level tlevel,sys_connect_by_path(parent_term_acc,'/') tpath ,connect_by_isleaf isleaf\n" +
                    "from ont_dag \n" +
                    "    start with child_term_acc=?\n" +
                    "    connect by prior parent_term_acc=child_term_acc\n" +
                    ") x where isleaf>0\n" +
                    "order by tlevel";
            List<String> columns = Utils.getSingleRow(sql, new String[]{termAcc});
            if( columns.size()>0 )
                pathList.add(buildPath(termAcc+columns.get(0)));
        }
        else if( option==PATH_OPTION_ONE_LONGEST ) {

            String sql = "select tpath\n" +
                    " from (\n" +
                    "select level tlevel,sys_connect_by_path(parent_term_acc,'/') tpath ,connect_by_isleaf isleaf\n" +
                    "from ont_dag \n" +
                    "    start with child_term_acc=?\n" +
                    "    connect by prior parent_term_acc=child_term_acc\n" +
                    ") x where isleaf>0\n" +
                    "order by tlevel desc";
            List<String> columns = Utils.getSingleRow(sql, new String[]{termAcc});
            if( columns.size()>0 )
                pathList.add(buildPath(termAcc+columns.get(0)));
        }
        else if( option==PATH_OPTION_ALL_SHORTEST ) {

            String sql = "select tpath\n" +
                    " from (\n" +
                    "select level tlevel,sys_connect_by_path(parent_term_acc,'/') tpath ,connect_by_isleaf isleaf\n" +
                    "from ont_dag \n" +
                    "    start with child_term_acc=?\n" +
                    "    connect by prior parent_term_acc=child_term_acc\n" +
                    ") x where isleaf>0\n" +
                    "order by tlevel";
            List<List<String>> rows = Utils.getRows(sql, new String[]{termAcc}, new RowComparator());
            for( List<String> row: rows ) {
                pathList.add(buildPath(termAcc+row.get(0)));
            }
        }
        else if( option==PATH_OPTION_ALL_LONGEST ) {

            String sql = "select tpath\n" +
                    " from (\n" +
                    "select level tlevel,sys_connect_by_path(parent_term_acc,'/') tpath ,connect_by_isleaf isleaf\n" +
                    "from ont_dag \n" +
                    "    start with child_term_acc=?\n" +
                    "    connect by prior parent_term_acc=child_term_acc\n" +
                    ") x where isleaf>0\n" +
                    "order by tlevel desc";
            List<List<String>> rows = Utils.getRows(sql, new String[]{termAcc}, new RowComparator());
            for( List<String> row: rows ) {
                pathList.add(buildPath(termAcc+row.get(0)));
            }
        }
        else if( option==PATH_OPTION_ALL ) {

            String sql = "select tpath\n" +
                    " from (\n" +
                    "select level tlevel,sys_connect_by_path(parent_term_acc,'/') tpath ,connect_by_isleaf isleaf\n" +
                    "from ont_dag \n" +
                    "    start with child_term_acc=?\n" +
                    "    connect by prior parent_term_acc=child_term_acc\n" +
                    ") x where isleaf>0\n" +
                    "";
            List<List<String>> rows = Utils.getRows(sql, new String[]{termAcc}, null);
            for( List<String> row: rows ) {
                pathList.add(buildPath(termAcc+row.get(0)));
                // no more than 50 paths will be shown in PATH_OPTION_ALL option
                if( pathList.size()>=50 )
                    break;
            }
        }
        else if( option==PATH_OPTION_ONE_SHORTEST_AND_LONGEST || option==PATH_OPTION_DEFAULT ) {

            List<List<TermWithStats>> pathList1 = getPathsToRoot(termAcc, PATH_OPTION_ONE_SHORTEST);
            pathList.addAll(pathList1);
            List<List<TermWithStats>> pathList2 = getPathsToRoot(termAcc, PATH_OPTION_ONE_LONGEST);

            // add the longest path if and only if it is longer than the shortest path
            int shortestPathLen = pathList1.isEmpty() ? 0 : pathList1.get(0).size();
            int longestPathLen = pathList2.isEmpty() ? 0 : pathList2.get(0).size();
            if( longestPathLen > shortestPathLen )
                pathList.addAll(pathList2);
        }

        return pathList;
    }

    private List<TermWithStats> buildPath(String path) throws Exception {

        // split paths into terms; the term in question is first, the root term is last
        String[] terms = path.split("/");

        // build the path
        List<TermWithStats> pathTerms = new ArrayList<>(terms.length);
        for( int i=terms.length-1; i>=0; i-- ) {
            pathTerms.add(getTermWithStatsCached(terms[i], i-1>=0?terms[i-1]:null));
        }
        return pathTerms;
    }


    private class RowComparator implements Comparator<List<String>> {
        public int compare(List<String> o1, List<String> o2) {
            // the first column in a row is a path, the second column is a depth
            // two rows are equal id f they have same depth
            return o1.get(0).length() - o2.get(0).length();
        }
    }

    /**
     * insert a new dag edge into ONT_DAG table; if the dag edge already exist, update last_modified_date
     * @param parentTermAcc parent term accession id; must not be null
     * @param childTermAcc child term accession id; must not be null
     * @param relId relation id, as found in database
     * @return 1 if rows was inserted, 0 if it was updated
     * @throws Exception if something wrong happens in spring framework
     */
    public int upsertDag(String parentTermAcc, String childTermAcc, String relId) throws Exception {

        // parent-child terms must be not null and different from each other
        if( parentTermAcc==null || childTermAcc==null ) {
            throw new OntologyXDAOException("Both parent and child term accession id must not be null");
        }
        if( parentTermAcc.equalsIgnoreCase(childTermAcc) ) {
            throw new OntologyXDAOException("You cannot insert a dag edge where both parent and child term accession id are the same: "+childTermAcc);
        }

        String sql = "UPDATE ont_dag SET last_modified_date=SYSDATE,ont_rel_id=? "+
                "WHERE parent_term_acc=? AND child_term_acc=?";
        int rowsAffected = update(sql, relId, parentTermAcc, childTermAcc);
        if( rowsAffected!=0 ) {
            // DAG was updated
            return 0;
        }

        sql = "INSERT INTO ont_dag (parent_term_acc,child_term_acc,ont_rel_id,created_date,last_modified_date) " +
            "VALUES (?,?,?,SYSDATE,SYSDATE)";

        return update(sql, parentTermAcc, childTermAcc, relId);
    }

    /**
     * delete a dag edge from ONT_DAG table
     * @param dag TermDagEdge object to delete
     * @return 1 if rows was deleted, 0 otherwise
     * @throws Exception if something wrong happens in spring framework
     */
    public int deleteDag(TermDagEdge dag) throws Exception {

        String sql = "DELETE FROM ont_dag WHERE parent_term_acc=? AND child_term_acc=? AND ont_rel_id=?";
        return update(sql, dag.getParentTermAcc(), dag.getChildTermAcc(), dag.getRelId());
    }

    /**
     * delete dag edges from ONT_DAG table for a given term that is obsolete
     * @param termAcc term acc of an obsolete term
     * @return nr of dags deleted or -1 if term acc is invalid or active
     * @throws Exception if something wrong happens in spring framework
     */
    public int deleteDagsForObsoleteTerm(String termAcc) throws Exception {

        Term t = getTermByAccId(termAcc);
        if( t==null || !t.isObsolete() ) {
            return -1;
        }
        String sql = "DELETE FROM ont_dag WHERE parent_term_acc=? OR child_term_acc=?";
        return update(sql, termAcc, termAcc);
    }

    public int mergeDags(String termAccFrom, String termAccTo) throws Exception {
        String sqlp = "UPDATE ont_dag SET child_term_acc=?,last_modified_date=SYSDATE\n" +
                "WHERE child_term_acc=? AND parent_term_acc IN(\n" +
                "  SELECT parent_term_acc FROM ont_dag WHERE child_term_acc=? \n" +
                "  MINUS\n" +
                "  SELECT parent_term_acc FROM ont_dag WHERE child_term_acc=?\n" +
                ")";
        int parentsMerged = update(sqlp, termAccTo, termAccFrom, termAccFrom, termAccTo);

        String sqlc = "UPDATE ont_dag SET parent_term_acc=?,last_modified_date=SYSDATE\n" +
                "WHERE parent_term_acc=? AND child_term_acc IN(\n" +
                "  SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=? \n" +
                "  MINUS\n" +
                "  SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=?\n" +
                ")";
        int childrenMerged = update(sqlc, termAccTo, termAccFrom, termAccFrom, termAccTo);
        return parentsMerged + childrenMerged;
    }

    /**
     * get all dags for given ontology that have been created after the given cut off date
     * @param ontId ontology id
     * @return list of TermDagEdge objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermDagEdge> getNewDags(String ontId, Date cutoffDate) throws Exception {

        String sql =
                "SELECT * FROM ont_dag d WHERE d.created_date>? AND EXISTS "+
                "(SELECT 1 FROM ONT_TERMS t WHERE t.term_acc=d.child_term_acc AND t.ont_id=?)";

        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        return execute(q, cutoffDate, ontId);
    }

    /**
     * get all dags for given ontology that had not been updated before the given cut off date
     * @param ontId ontology id
     * @return list of TermDagEdge objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermDagEdge> getStaleDags(String ontId, Date cutoffDate) throws Exception {

        String sql =
                "SELECT * FROM ont_dag d WHERE d.last_modified_date<? AND EXISTS "+
                "(SELECT 1 FROM ONT_TERMS t WHERE t.term_acc=d.child_term_acc AND t.ont_id=?)";

        TermDagEdgeQuery q = new TermDagEdgeQuery(this.getDataSource(), sql);
        return execute(q, cutoffDate, ontId);
    }

    /**
     * delete all dags for given ontology that had not been updated before the given cut off date
     * @param ontId ontology id
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int deleteStaleDags(String ontId, Date cutoffDate) throws Exception {

        String sql =
            "DELETE from ONT_DAG d WHERE d.last_modified_date<? AND EXISTS "+
            "(SELECT 1 FROM ONT_TERMS t "+
            " WHERE t.term_acc=d.child_term_acc AND t.ont_id=?)";
        return update(sql, cutoffDate, ontId);
    }

    /**
     * get list of all synonyms for given term
     * @param termAcc term accession id
     * @return list of all synonyms; could be empty list
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getTermSynonyms(String termAcc) throws Exception {
        String sql = "SELECT s.* FROM ont_synonyms s WHERE term_acc=?";
        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), sql);
        return execute(q, termAcc);
    }

    /**
     * get list of all synonyms for given term and source
     * @param termAcc term accession id
     * @param source source
     * @return list of all synonyms; could be empty list
     * @throws Exception if something wrong happens in spring framework
     */
    public List<TermSynonym> getTermSynonyms(String termAcc, String source) throws Exception {
        String sql = "SELECT s.* FROM ont_synonyms s WHERE term_acc=? AND source=?";
        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), sql);
        return execute(q, termAcc, source);
    }

    /**
     * insert new synonym for given term
     * @param synonym OntTermSynonym object to be inserted
     * @return synonym key of newly inserted synonym
     * @throws Exception if something wrong happens in spring framework
     */
    public int insertTermSynonym(TermSynonym synonym) throws Exception {

        String sql = "BEGIN INSERT INTO ont_synonyms (term_acc, synonym_name, synonym_type, dbxrefs, " +
                " source, created_date, last_modified_date, syn_key)" +
                "VALUES(?,?,?,?,?,?,?,ont_synonyms_seq.NEXTVAL) "+
                "RETURNING syn_key INTO ?; END;";

        try (Connection conn = this.getConnection() ){
            CallableStatement cs = conn.prepareCall(sql);
            cs.setString(1, synonym.getTermAcc());
            cs.setString(2, synonym.getName());
            cs.setString(3, synonym.getType());
            cs.setString(4, synonym.getDbXrefs());
            cs.setString(5, synonym.getSource());
            setTimestamp(cs, 6, synonym.getCreatedDate());
            setTimestamp(cs, 7, synonym.getLastModifiedDate());

            cs.registerOutParameter(8, Types.INTEGER); // syn_key

            cs.execute();

            synonym.setKey(cs.getInt(8));
        }

        return synonym.getKey();
    }

    /**
     * update existing synonym for given term
     * @param syn OntTermSynonym object with modified data
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int updateTermSynonym(TermSynonym syn) throws Exception {

        String sql = "UPDATE ont_synonyms SET term_acc=?,synonym_name=?,synonym_type=?,dbxrefs=?,"+
                "source=?,last_modified_date=SYSDATE "+
                "WHERE syn_key=?";
        return update(sql, syn.getTermAcc(), syn.getName(), syn.getType(), syn.getDbXrefs(),
                syn.getSource(), syn.getKey());
    }

    /**
     * update existing synonym for given term
     * @param synOriginal original OntTermSynonym object to be updated
     * @param synUpdated OntTermSynonym object with modified data
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int updateTermSynonym(TermSynonym synOriginal, TermSynonym synUpdated) throws Exception {

        String sql = "UPDATE ont_synonyms SET term_acc=?,synonym_name=?,synonym_type=?,dbxrefs=? "+
                "WHERE term_acc=? AND synonym_name=? AND synonym_type=? AND NVL(dbxrefs,'*')=NVL(?,'*')";

        return update(sql, synUpdated.getTermAcc(), synUpdated.getName(), synUpdated.getType(), synUpdated.getDbXrefs(),
                synOriginal.getTermAcc(), synOriginal.getName(), synOriginal.getType(), synOriginal.getDbXrefs());
    }

    /**
     * update last modification date for a list of synonyms
     * @param synonyms collection of TermSynonym objects
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int updateTermSynonymLastModifiedDate(Collection<TermSynonym> synonyms) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "UPDATE ont_synonyms SET last_modified_date=SYSDATE WHERE syn_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( TermSynonym syn: synonyms ) {
            su.update(syn.getKey());
        }

        return executeBatch(su);
    }

    /**
     * delete all synonyms for given ontology
     * @param ontId id ontology id
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int dropTermSynonymsForOntology(String ontId) throws Exception {

        String sql = "DELETE FROM ont_synonyms s WHERE EXISTS (SELECT 1 FROM ont_terms t WHERE t.term_acc=s.term_acc AND t.ont_id=?)";
        return update(sql, ontId);
    }

    /**
     * delete given term synonym
     * Note: if multiple synonyms are matched, only one will be deleted
     * @param synonym TermSynonym object: term acc id, synonym name and synonym type must be set
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int dropTermSynonym(TermSynonym synonym) throws Exception {

        if( synonym.getKey()!=0 ) {
            return dropTermSynonym(synonym.getKey());
        }

        String sql = "DELETE FROM ont_synonyms s WHERE s.term_acc=? AND s.synonym_name=? AND s.synonym_type=? AND ROWNUM<2";
        return update(sql, synonym.getTermAcc(), synonym.getName(), synonym.getType());
    }

    /**
     * delete given term synonym by id
     * @param synonymKey unique synonym key
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int dropTermSynonym(int synonymKey) throws Exception {

        String sql = "DELETE FROM ont_synonyms s WHERE s.syn_key=?";
        return update(sql, synonymKey);
    }

    /**
     * delete a collection of term synonyms
     * @param synonyms collection of term synonyms
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int deleteTermSynonyms(Collection<TermSynonym> synonyms) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "DELETE FROM ont_synonyms WHERE syn_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( TermSynonym syn: synonyms ) {
            su.update(syn.getKey());
        }

        return executeBatch(su);
    }

    /**
     * get all ontology synonyms for given source modified before given date and time
     *
     * @param ontId id of ontology to be processed
     * @param source source of term synonyms
     * @param dt cut-off date of last modification
     * @return list of TermSynonym objects
     * @throws Exception on spring framework dao failure
     */
    public List<TermSynonym> getTermSynonymsModifiedBefore(String ontId, String source, Date dt) throws Exception{

        String query = "SELECT * FROM ont_synonyms s WHERE source=? AND last_modified_date<? "+
                "AND EXISTS(SELECT 1 FROM ont_terms t WHERE ont_id=? AND s.term_acc=t.term_acc)";
        TermSynonymQuery q = new TermSynonymQuery(this.getDataSource(), query);
        return execute(q, source, dt, ontId);
    }

    /**
     * get ontology object given ont_id
     * @param ontId ontology id
     * @return Ontology object or null if ont_id is invalid
     * @throws Exception if something wrong happens in spring framework
     */
    public Ontology getOntology(String ontId) throws Exception {

        String query = "select * from ONTOLOGIES where ont_id=?";
        return getOntologyImp(query, ontId);
    }

    /**
     * get ontology object given aspect
     * @param aspect ontology aspect
     * @return Ontology object or null if aspect is invalid
     * @throws Exception if something wrong happens in spring framework
     */
    public Ontology getOntologyFromAspect(String aspect) throws Exception {

        String query = "select * from ONTOLOGIES where aspect=?";
        return getOntologyImp(query, aspect);
    }

    public Ontology getOntologyFromAccId(String accId) throws Exception {

        String query = "select o.* from ONTOLOGIES o, ont_terms ot where o.ont_id = ot.ont_id and ot.term_acc=?";
        return getOntologyImp(query, accId);
    }

    private Ontology getOntologyImp(String query, String param) throws Exception {

        OntologyQuery q = new OntologyQuery(this.getDataSource(), query);
        List<Ontology> ontologies = execute(q, param);
        return ontologies.isEmpty() ? null : ontologies.get(0);
    }

    /**
     * get list of all ontologies (including non-public ontologies)
     * @return List of Ontology objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Ontology> getOntologies() throws Exception {

        return getOntologiesImpl("SELECT * FROM ontologies ORDER BY ont_name");
    }

    /**
     * get list of all public ontologies (non-public ontologies are skipped)
     * @return List of Ontology objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Ontology> getPublicOntologies() throws Exception {

        return getOntologiesImpl("SELECT * FROM ontologies WHERE is_public<>0 ORDER BY ont_name");
    }

    private List<Ontology> getOntologiesImpl(String query) throws Exception {

        OntologyQuery q = new OntologyQuery(this.getDataSource(), query);
        q.compile();
        return q.execute();
    }

    /**
     * get accession id for a root term of given ontology
     * @param ontId ontology id
     * @return accession id of a root term, or null if not found
     * @throws Exception if something wrong happens in spring framework
     */
    public String getRootTerm(String ontId) throws Exception {
                
        String query = "SELECT root_term_acc FROM ontologies WHERE ont_id=?";
        StringListQuery q = new StringListQuery(this.getDataSource(), query);

        List<String> rootTerms = execute(q, ontId);
        return rootTerms.isEmpty() ? null : rootTerms.get(0);
    }

    /**
     * get those top level terms that are ascendants of a given term;
     * note: top-level terms are the direct child terms of the root term of given ontology
     * f.e. for   Wiskott-Aldrich Syndrome (RDO:0004595)
     *   we will return 3 terms:
     *     1) Immune System Diseases (RDO:0004950)
     *     2) Hemic and Lymphatic Diseases (RDO:0005741)
     *     3) Congenital, Hereditary, and Neonatal Diseases and Abnormalities (RDO:0003053)
     * @param termAcc term accession
     * @return top level terms that are ascendants of a given term
     * @throws Exception if something wrong happens in spring framework
     */
    public List<StringMapQuery.MapPair> getTopLevelTerms(String termAcc) throws Exception {
        String sql =
            "SELECT term_acc,term FROM ont_terms "+
            "WHERE term_acc IN("+
            "  SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=(SELECT root_term_acc FROM ontologies WHERE ont_id=(SELECT ont_id FROM ont_terms WHERE term_acc=?))"+
            ") AND EXISTS ("+
            "  SELECT 1 FROM( SELECT parent_term_acc FROM ont_dag"+
            " START WITH child_term_acc=?"+
            " CONNECT BY PRIOR parent_term_acc=child_term_acc"+
            ") WHERE parent_term_acc=term_acc"+
            ")";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), sql);
        return execute(q, termAcc, termAcc);
    }

    /**
     * get those top level disease terms that are ascendants of a given disease term;
     * note: top-level disease terms are the direct child terms of the root term of disease ontology
     * f.e. for   Wiskott-Aldrich Syndrome (RDO:0004595)
     *   we will return 3 terms:
     *     1) Immune System Diseases (RDO:0004950)
     *     2) Hemic and Lymphatic Diseases (RDO:0005741)
     *     3) Congenital, Hereditary, and Neonatal Diseases and Abnormalities (RDO:0003053)
     * @param rdoTermAcc term accession of a disease term
     * @return top level disease terms that are ascendants of a given disease term
     * @throws Exception if something wrong happens in spring framework
     */
    public List<StringMapQuery.MapPair> getTopLevelDiseaseTerms(String rdoTermAcc) throws Exception {
        return getTopLevelTerms(rdoTermAcc);
    }

    /**
     * @param rdoTermAcc term accession
     * @throws Exception if something wrong happens in spring framework
     */
    public List<StringMapQuery.MapPair> getAnchorTerms(String rdoTermAcc, String anchorTerm) throws Exception {
        String sql =
                "SELECT term_acc,term FROM ont_terms "+
                        "WHERE term_acc IN("+
                        "  SELECT child_term_acc FROM ont_dag WHERE parent_term_acc=?"+
                        ") AND EXISTS ("+
                        "  SELECT 1 FROM( SELECT parent_term_acc FROM ont_dag"+
                        " START WITH child_term_acc=?"+
                        " CONNECT BY PRIOR parent_term_acc=child_term_acc"+
                        ") WHERE parent_term_acc=term_acc"+
                        ")";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), sql);
        return execute(q, anchorTerm, rdoTermAcc);
    }

    /** get all orphaned terms for given ontology: orphaned terms are active terms
     * without a parent term and without a child term
     * @param ontId ontology id
     * @return list of orphaned terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getOrphanedTerms(String ontId) throws Exception {

        String sql = "SELECT * FROM ont_terms \n" +
                "WHERE is_obsolete=0 AND ont_id=? \n" +
                "AND NOT EXISTS (SELECT 1 FROM ont_dag WHERE term_acc=child_term_acc)\n" +
                "AND NOT EXISTS (SELECT 1 FROM ont_dag WHERE term_acc=parent_term_acc)";

        return executeTermQuery(sql, ontId);
    }

    /**
     * examines all terms and orphaned terms (all active terms that do not appear in ontology dag trees)
     * receive 'obsolete' status = 2
     * @param ontId ontology id
     * @return count of terms made obsolete
     * @throws Exception if something wrong happens in spring framework
     */
    public int obsoleteOrphanedTerms(String ontId) throws Exception {
        String sql = "UPDATE ont_terms SET is_obsolete=2 \n" +
                "WHERE is_obsolete=0 AND ont_id=? \n" +
                "AND NOT EXISTS (SELECT 1 FROM ont_dag WHERE term_acc=child_term_acc)\n" +
                "AND NOT EXISTS (SELECT 1 FROM ont_dag WHERE term_acc=parent_term_acc)";

        return update(sql, ontId);
    }

    /**
     * given strain ontology id, return associated strain rgd id;
     * note: as of May 2011, rat strain ontology contained at most one RGD ID for every term;
     * <p>
     * term synonyms are examined; if there is a synonym having 'RGD ID : [rgd_id]' in the name,
     * [rgd_id] is assumed to be a strain rgd id
     * @param accId strain ontology accession id
     * @return strain rgd id or 0 if there is no mapping between ontology and rgd strain
     * @throws Exception if something wrong happens in spring framework
     */
    public int getRgdIdForStrainOntId(String accId) throws Exception {

        // shortcut: accession id for strain ontology is 'RS:'
        if( !accId.startsWith("RS:") )
            return 0; // only strain ontology is processed

        for( TermSynonym synonym: this.getTermSynonyms(accId) ) {
            // synonym name must begin with 'RGD ID: '
            if( synonym.getName().startsWith("RGD ID: ") ) {
                // try to parse the rgd id
                try {
                    return Integer.parseInt(synonym.getName().substring(8));
                } catch(NumberFormatException e) {}
            }
        }
        return 0; // rgd-id not found
    }


    /**
     * Return a strain ontology term acc for a given rgd Id
     * Note: some rgd ids are mapped to multiple strain ontology ids; in that case a randomly chosen
     * strain acc id is returned; if given rgd id is not mapped to any strain ontology terms, null is returned
     * @param rgdId rgd id
     * @return strain ontology term acc or null
     * @throws Exception if something wrong happens in spring framework
     */
    public String getStrainOntIdForRgdId(int rgdId) throws Exception {

        String sql = "SELECT ot.term_acc FROM ont_synonyms os, ont_terms ot WHERE synonym_name = ? and ot.term_acc=os.term_acc order by is_obsolete asc";

        List<String> strainOntIds = StringListQuery.execute(this, sql, "RGD ID: "+rgdId);
        if( strainOntIds.isEmpty() ) {
            return null;
        }
        return strainOntIds.get(0);
    }

    public Term getSoTermForGeneType(String geneType) throws Exception {
        String sql = "SELECT t.* FROM ont_terms t WHERE term_acc="+
                "(SELECT so_acc_id FROM gene_types WHERE gene_type_lc=?)";
        List<Term> terms = executeTermQuery(sql, geneType);
        return terms.isEmpty() ? null : terms.get(0);
    }

    /**
     * get list of GOSLIM terms for given rgd id; note: duplicate rows in the result are expected
     * @param rgdId id
     * @return list of all GO SLIM terms for given rgd id; could be empty list
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Term> getGoSlimTerms(int rgdId) throws Exception {
        String sql = "SELECT t.* FROM rgd_to_slim_map m,ont_terms t WHERE rgd_id=? AND m.term_acc=t.term_acc";
        return executeTermQuery(sql, rgdId);
    }

    /**
     *
     * @param termAcc term acc
     * @param patternToMatch
     * @return
     * @throws Exception if something wrong happens in spring framework
     */
    public int getCountOfSynonymsByTermAcc(String termAcc, String patternToMatch) throws Exception {

        String likePhrase = this.processLikePhrase("contains", patternToMatch);

        String query = "SELECT COUNT(s.synonym_name) FROM ont_synonyms s " +
                "WHERE s.synonym_name LIKE ? AND s.term_acc=?";

        return getCount(query, likePhrase, termAcc);
    }

    public int getCountOfDistinctTermsByMatchingPattern(String synonymPattern, String ontId) throws Exception{

        String likePhrase = this.processLikePhrase("begins", synonymPattern);
        String ontIdPattern = this.processLikePhrase("begins", ontId);

        String query = "SELECT COUNT(DISTINCT(s.term_acc)) FROM ont_synonyms s " +
                "WHERE s.synonym_name LIKE ? AND s.term_acc LIKE ?";

        return getCount(query, likePhrase, ontIdPattern);
    }

    /**
     * get all pairs of xrefs {term-acc, CasRN}
     * for terms having a 'CAS Registry Number' as description or 'CAS' as type
     * and synonyms of type 'xref_cas'
     * @return pairs of {term-acc, CasRN}
     * @throws Exception if something wrong happens in spring framework
     */
    public List<StringMapQuery.MapPair> getTermsWithCasRN() throws Exception {

        String sql = "SELECT DISTINCT term_acc,xref_value FROM ont_xrefs x "+
                "WHERE xref_description='CAS Registry Number' OR xref_type='CAS'";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), sql);
        return execute(q);
    }

    /**
     * get all pairs of {term-acc, MESH-ID}
     * for terms of given ontology and synonym_name starting with 'MESH:'
     * @return pairs of {term-acc, MESH-ID}
     * @throws Exception if something wrong happens in spring framework
     */
    public List<StringMapQuery.MapPair> getTermsWithMesh(String ontId) throws Exception {

        String sql = "SELECT DISTINCT term_acc,synonym_name mesh_id FROM ont_synonyms s "+
                "WHERE synonym_name LIKE 'MESH:%' "+
                "AND EXISTS(SELECT 1 FROM ont_terms t WHERE t.term_acc=s.term_acc AND is_obsolete=0 AND ont_id=?)";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), sql);
        return execute(q, ontId);
    }

    public List<TermXRef> getTermXRefs(String termAcc) throws Exception {

        String sql = "SELECT x.* FROM ont_xrefs x WHERE term_acc=?";
        TermXRefQuery q = new TermXRefQuery(this.getDataSource(), sql);
        return execute(q, termAcc);
    }

    public int insertTermXRef(TermXRef xref) throws Exception {

        xref.setKey(this.getNextKey("ont_xrefs_seq"));

        String sql = "INSERT INTO ont_xrefs(xref_key,term_acc,xref_type,xref_value,xref_description) VALUES(?,?,?,?,?)";
        return update(sql, xref.getKey(), xref.getTermAcc(), xref.getXrefType(), xref.getXrefValue(), xref.getXrefDescription());
    }

    public int deleteTermXRef(TermXRef xref) throws Exception {

        String sql = "DELETE FROM ont_xrefs WHERE xref_key=?";
        return update(sql, xref.getKey());
    }

    public int updateTermXRef(TermXRef xref) throws Exception {

        String sql = "UPDATE ont_xrefs SET term_acc=?,xref_type=?,xref_value=?,xref_description=? WHERE xref_key=?";
        return update(sql, xref.getTermAcc(), xref.getXrefType(), xref.getXrefValue(), xref.getXrefDescription(), xref.getKey());
    }

    public int updateTermXRefDescription(TermXRef xref) throws Exception {

        String sql = "UPDATE ont_xrefs SET xref_description=? WHERE xref_key=?";
        return update(sql, xref.getXrefDescription(), xref.getKey());
    }

    /**
     * get list of allowed ontology qualifiers, as found in table ONTOLOGY_QUALIFIERS
     * @return list of allowed ontology qualifiers
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getOntologyQualifiers() throws Exception {

        String query = "SELECT ont_qualifier_name FROM ontology_qualifier";
        return StringListQuery.execute(this, query);
    }

    /**
     * get list of synonym types for given ontology
     * @return list of synonym types for given ontology
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getSynonymTypes(String ontId) throws Exception {

        String query = "SELECT synonym_type FROM ont_synonyms s,ont_terms t\n" +
                "WHERE s.term_acc=t.term_acc AND ont_id=?\n" +
                "GROUP BY synonym_type\n" +
                "ORDER BY COUNT(*) DESC\n";
        return StringListQuery.execute(this, query, ontId);
    }

    /**
     * get list of Obsolete Terms for an OntId
     * @param ontId
     * @return list of obsolete terms
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getObsoleteTerms(String ontId) throws Exception {

        String query = "SELECT term_acc FROM ont_terms where ont_id = ? and is_obsolete = 1";
        return StringListQuery.execute(this, query, ontId);
    }
    /**
     * to differentiate between ours and the framework's exceptions
     */
    public class OntologyXDAOException extends Exception {

        public OntologyXDAOException(String msg) {
            super(msg);
        }
    }

    private String constructAccCondition(String[] termAccs, String columnName) {
        StringBuilder condition = new StringBuilder();
        StringBuilder curBracket = new StringBuilder();
        int bracketSize = 999;
        int curCount = 0;

        for (int i=0; i < termAccs.length; i++) {
            if (curCount <= bracketSize) {
                curBracket.append((curCount == 0 ? "" : ", ")).append("'").append(termAccs[i]).append("'");
                curCount ++;
            } else {
                condition.append(condition.length()==0 ? "" : " OR ").append(
                columnName).append(" in (").append(curBracket).append(")");
                curCount = 1;
                curBracket = new StringBuilder("'" + termAccs[i] + "'");
            }
        }
        condition.append(condition.length()==0 ? "" : " OR ").append(
        columnName).append(" in (").append(curBracket).append(")");
        return condition.toString();
    }

    /// Term query implementation helper
    public List<Term> executeTermQuery(String query, Object ... params) throws Exception {
        TermQuery q = new TermQuery(this.getDataSource(), query);
        return execute(q, params);
    }
}
