package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.Pathway;
import edu.mcw.rgd.datamodel.PathwayObject;
import edu.mcw.rgd.datamodel.Reference;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.process.Utils;
import org.apache.commons.collections.ListUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 28, 2011
 * Time: 4:09:52 PM
 * <p>
 * curated pathway: a pathway that has an entry in PATHWAY table, thus having a custom pathway diagram
 */
public class PathwayDAO extends AbstractDAO {

    private static Map<String,String> pathwayCategories;

    static {
        pathwayCategories = new HashMap<>();
        pathwayCategories.put("classic", "PW:0000002");
        pathwayCategories.put("disease", "PW:0000013");
        pathwayCategories.put("drug", "PW:0000754");
        pathwayCategories.put("regulatory", "PW:0000004");
        pathwayCategories.put("signaling", "PW:0000003");
    }

    private OntologyXDAO odao = new OntologyXDAO();
    private ReferenceDAO rdao = new ReferenceDAO();

    /**
     * get pathway object given pathway accession id
     * @param accID pathway accession id (PW:xxxxxxx)
     * @return Pathway object or null if accession id is invalid
     * @throws Exception
     */
    public Pathway getPathway(String accID) throws Exception {
        String query = "SELECT p.* FROM pathway p WHERE p.term_acc=?";
        PathwayQuery q = new PathwayQuery(this.getDataSource(), query);
        List<Pathway> pathwayList = execute(q, accID);
        if(pathwayList.isEmpty())
            return null;
        return pathwayList.get(0);
    }

    /**
     * get pathway object with categories given pathway accession id
     * @param accID pathway accession id (PW:xxxxxxx)
     * @return Pathway object or null if accession id is invalid
     * @throws Exception
     */
    public Pathway getPathway2(String accID) throws Exception {
        // get pathway object given acc id
        Pathway pw = getPathway(accID);
        if( pw==null ) {
            return null;
        }

        // get pathway categories
        List<String> pathwayCategoryList = new ArrayList<>();
        for( StringMapQuery.MapPair pair: odao.getTopLevelTerms(accID) ) {
            // map term-acc to pathway category
            for( Map.Entry<String,String> entry: pathwayCategories.entrySet() ) {
                if( entry.getValue().equals(pair.keyValue) ) {
                    pathwayCategoryList.add(entry.getKey());
                }
            }
        }
        pw.setPathwayCategories(pathwayCategoryList);
        return pw;
    }

    public Pathway getPathwayInfo(String accID) throws Exception {

        // get pathway object given acc id
        Pathway pw = getPathway2(accID);
        if( pw==null )
            return null;

        // get pathway objects
        String query = "SELECT po.*, pt.object_type_name FROM pathway_object_association po, pathway_object_types pt "+
                "WHERE pt.object_type_id=po.object_type_id AND po.term_acc=?";
        PathwayObjectsQuery po = new PathwayObjectsQuery(this.getDataSource(), query);
        List<PathwayObject> pwObjects = execute(po, accID);
        if(pwObjects.size()>0){
            pw.setObjectList(pwObjects);
        }else{
            pw.setObjectList(null);
        }

        // get references for pathway
        String queryForRef = "SELECT pr.ref_rgd_key FROM pathway_reference pr WHERE pr.term_acc=?";
        List<Integer> pwRefKeys = IntListQuery.execute(this, queryForRef, accID);

        pw.setReferenceList(rdao.getReferencesByKeyList(pwRefKeys));
        return pw;
    }

    /**
     * returns list of pwIds from pathway table
     * @return list of pathway accession ids
     * @throws Exception
     */
    public List<String> getPathwayList() throws Exception{

        String sql = "SELECT term_acc FROM pathway";
        return StringListQuery.execute(this, sql);
    }

    /**
     * given a list of pathways, return subset of curated pathways (pathway acc ids present in PATHWAY table)
     * @param accIds list of pathway accession ids
     * @return list of pathway accession ids for curated pathways
     * @throws Exception
     */
    public List<String> getCuratedPathways(List<String> accIds) throws Exception{

        String sql = "SELECT term_acc FROM pathway WHERE term_acc IN("+Utils.buildInPhraseQuoted(accIds)+")";
        return StringListQuery.execute(this, sql);
    }

    /** get available pathway categories
     *
     * @return list of available pathway categories
     */
    public Collection<String> getPathwayCategories() {
        return pathwayCategories.keySet();
    }

    /** return list of pathways with diagrams for given category
     *
     * @param category pathway category; if null, pathways for all categories are returned
     * @return list of Pathway objects
     * @throws Exception
     */
    public List<Pathway> getPathwaysForCategory(String category) throws Exception {

        // get anchor term for pathway category
        String rootAccId;
        if( category==null ) {
            rootAccId = odao.getRootTerm("PW");
        } else {
            rootAccId = pathwayCategories.get(category);
        }
        if( rootAccId==null ) {
            return null;
        }

        String sql =
        "SELECT * FROM pathway w \n"+
        "WHERE term_acc IN(\n"+
        " SELECT s.term_acc FROM (\n"+
                "SELECT d.child_term_acc FROM ont_dag d\n"+
                "START WITH parent_term_acc=?\n"+
                "CONNECT BY PRIOR child_term_acc=parent_term_acc\n"+
        " )d, ont_term_stats2 s\n"+
        " WHERE d.child_term_acc=s.term_acc AND stat_name='diagram_count' AND with_children=0\n"+
        ")";
        PathwayQuery q = new PathwayQuery(this.getDataSource(), sql);
        return execute(q, rootAccId);
    }

    /** get pathway with diagrams that match a given searchString and, optionally,
     * that do belong to the supplied list of pathway categories
     * @param pathwayCategories 0 or more pathway categories; if no pathway categories are supplied,
     *                          only searchString matters
     * @param searchString search string
     * @return
     * @throws Exception
     */
    public List<Pathway> searchPathways(List<String> pathwayCategories, String searchString) throws Exception {

        if( pathwayCategories==null || pathwayCategories.isEmpty() ) {
            return searchPathways(searchString);
        }

        List<Term> matches = odao.findTerms(searchString, "PW");
        if( matches.isEmpty() ) {
            return Collections.emptyList();
        }

        List<Pathway> results = new ArrayList<>(matches.size());
        for( Term term: matches ) {
            Pathway pathway = getPathway2(term.getAccId());
            if( !ListUtils.intersection(pathway.getPathwayCategories(), pathwayCategories).isEmpty() ) {
                results.add(pathway);
            }
        }
        return results;
    }

    public List<Pathway> searchPathways(String searchString) throws Exception {

        List<Term> matches = odao.findTerms(searchString, "PW");
        if( matches.isEmpty() ) {
            return Collections.emptyList();
        }

        List<Pathway> results = new ArrayList<>(matches.size());
        for( Term term: matches ) {
            results.add(getPathway2(term.getAccId()));
        }
        return results;
    }

    /**
     * get count of descendants for given term (including the most remote descendants)
     * @param termAcc term accession id
     * @return count of descendant terms
     * @throws Exception if something wrong happens in spring framework
     */
    public int getCountOfCuratedPathwaysForTermDescendants(String termAcc) throws Exception {

        String sql = "SELECT COUNT(*) FROM pathway "+
                "WHERE term_acc IN("+
                "  SELECT child_term_acc FROM ont_dag "+
                "  START WITH parent_term_acc=? "+
                "  CONNECT BY PRIOR child_term_acc=parent_term_acc "+
                ")";
        return getCount(sql, termAcc);
    }

    public List<String> getAltPwIDs(String accId) throws Exception{

        String sql = "SELECT alt_pw_term_acc FROM pathway_altpathways WHERE pw_term_acc=?";
        return StringListQuery.execute(this, sql, accId);
    }

    public int getObjectTypeId(String objTypeName) throws Exception{
        String sql = "select ot.OBJECT_TYPE_ID from PATHWAY_OBJECT_TYPES ot, " +
                "PATHWAY_OBJECT_ASSOCIATION poa where poa.OBJECT_TYPE_ID = ot.OBJECT_TYPE_ID and ot.OBJECT_TYPE_NAME=?";

        List<Integer> ids = IntListQuery.execute(this, sql, objTypeName);
        return ids.isEmpty() ? 0 : ids.get(0);
    }

    /**
     *
     * @param obj_type_id
     * @return
     * @throws Exception
     */
    public List<String> getObjectTypeName(int obj_type_id) throws Exception{
        String sql = "select ot.OBJECT_TYPE_NAME from PATHWAY_OBJECT_TYPES ot where ot.OBJECT_TYPE_ID=?";
        return StringListQuery.execute(this, sql, obj_type_id);
    }

    /**
     * returns list of all pathway object types; only fields object_type_id and object_type_name are filled
     * @return list of PathwayObject objects
     * @throws Exception
     */
    public List<PathwayObject> getObjectTypes() throws Exception{
        String query = "select null object_acc_id,null term acc,object_type_id,null xdb_key,object_type_name,"+
                "null pathway_objects_url, null object_name,null object_description from PATHWAY_OBJECT_TYPES ot";

        PathwayObjectsQuery sq = new PathwayObjectsQuery(this.getDataSource(), query);
        sq.compile();
        return sq.execute();
    }

     /**
     * Insert new annotation into PATHWAY tables; PathwayID will be sent back
     *
     * @param pwObject Pathway object representing column values
     * @throws Exception
     * @return value of pathwayID
     */
    public String insertPathwayData(Pathway pwObject) throws Exception{

        String sql = "insert into PATHWAY (term_acc, pathway_name, pathway_desc, has_altered_pathway) "+
                "VALUES(?,?,?,?)";
        update(sql, pwObject.getId(), pwObject.getName(), pwObject.getDescription(), pwObject.getHasAlteredPath());

        List<Reference> pwRefList = pwObject.getReferenceList();
        insertPathwayRefs(pwObject.getId(), pwRefList);

        if(pwObject.getObjectList()!=null){
            insertPathwayObjects(pwObject.getObjectList());
        }
        
        return pwObject.getId();
    }

    /**
     * Insert list of pathway Objects into PATHWAY_OBJECT_ASSOCIATION tables
     *
     * @param pathAssObjects Pathway Objects representing column values
     * @throws Exception
     */
    public void insertPathwayObjects(List<PathwayObject> pathAssObjects) throws Exception{

        for(PathwayObject pwObj: pathAssObjects){
            String sql = "insert into PATHWAY_OBJECT_ASSOCIATION (term_acc, object_type_id, "+
                "object_acc_id, xdb_key, pathway_objects_url, object_name, object_description) "+
                "VALUES(?,?,?,?,?,?,?)";

            update(sql, pwObj.getId(), pwObj.getTypeId(), pwObj.getAccId(), pwObj.getXdb_key(),
                    pwObj.getUrl(), pwObj.getObjName(), pwObj.getObjDesc());
        }
    }

    /**
     * Insert list of pathway References into PATHWAY_REFERENCES tables
     *
     * @param acc_id  pathway acc term ID
     * @param pathRefs Pathway Objects representing column values
     * @throws Exception
     */
    public void insertPathwayRefs(String acc_id, List<Reference> pathRefs) throws Exception{

        for(Reference pwObj: pathRefs){
            String sql = "INSERT INTO pathway_reference (term_acc, ref_rgd_key) VALUES(?,?)";
            update(sql, acc_id, pwObj.getKey());
        }
    }

    /**
     * Insert list of pathway References into PATHWAY_REFERENCES tables

     * @param altPathIds Pathway ID representing alt pathways assoc with this pathway
     * @param acc_id pathway acc id
     * @throws Exception
     */
    public void insertAltPathwayIds(String acc_id, List<String> altPathIds) throws Exception{

        for(String pwObj: altPathIds){
            String sql = "insert into PATHWAY_ALTPATHWAYS (pw_term_acc, alt_pw_term_acc) VALUES(?,?)";
            update(sql, acc_id, pwObj);
        }
    }

    /**
     * delete all of the Pathway Information for a given pathway ID
     *
     * @param pwObject Pathway object representing column values
     * @throws Exception
     * @return void
     */
    public void deletePathwayData(Pathway pwObject) throws Exception{

        deletePathwayRefs(pwObject.getId());
        deletePathwayObjects(pwObject.getId());
        deleteAltPathwayIds(pwObject.getId());

        String sql = "delete from PATHWAY where term_acc=?";
        update(sql, pwObject.getId());
    }

    /**
     * Update new annotation into PATHWAY tables; PathwayID will be sent back
     *
     * @param pwObject Pathway object representing column values
     * @throws Exception
     * @return value of pathwayID
     */
    public String updatePathwayData(Pathway pwObject) throws Exception{

        deletePathwayData(pwObject);
        insertPathwayData(pwObject);
        return pwObject.getId();
    }

    /**
     * Updates PathwayObjects table by drop-and-reload method. drop all rows for that given PathwayID. this method only drops
     * the rows for a given acc_id. Inserting the new data happens in the updatePathwayData where it inserts new data
     * @param pwId
     * @throws Exception
     */
    public void deletePathwayObjects(String pwId) throws Exception{

        String sql = "delete from PATHWAY_OBJECT_ASSOCIATION where term_acc=?";
        update(sql, pwId);
    }

    /**
     * Update Pathway Reference table using drop-and-reload this method only drops the rows for a given acc_id.
     * Inserting the new data happens in the updatePathwayData where it inserts new data
     * @param acc_id
     * @throws Exception
     */
    public void deletePathwayRefs(String acc_id) throws Exception{

        String sql = "delete from PATHWAY_REFERENCE where term_acc=?";
        update(sql, acc_id);
    }

    /**
     * deletes all rows that contain Alt Path IDS for a given acc_id. this method only drops the rows for a given acc_id.
     * Inserting the new data happens in the updateAltPathwayData where it inserts new data
     * @param acc_id
     * @throws Exception
     */
    public void deleteAltPathwayIds(String acc_id) throws Exception{

        String sql = "delete from PATHWAY_ALTPATHWAYS where pw_term_acc=?";
        update(sql, acc_id);
    }

    /**
     * Insert list of pathway References into PATHWAY_REFERENCES tables

     * @param altPathIds Pathway ID representing alt pathways assoc with this pathway
     * @param acc_id pathway acc id
     * @throws Exception
     */
    public void updateAltPathwayIds(String acc_id, List<String> altPathIds) throws Exception{

        deleteAltPathwayIds(acc_id);
        insertAltPathwayIds(acc_id, altPathIds);
    }
}
