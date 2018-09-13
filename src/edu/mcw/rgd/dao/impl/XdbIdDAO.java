package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * dao for handling external database ids, like NCBI Gene Ids, stored in RGD_ACC_XDB table
 */
public class XdbIdDAO extends AbstractDAO {

    /**
     * get all genes with given external id
     * @param xdbKey - external db key
     * @param accId - external id to be looked for
     * @return list of Gene objects
     */
    public List<Gene> getGenesByXdbId(int xdbKey, String accId) throws Exception {

        String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, rgd_acc_xdb x " +
                    "WHERE r.rgd_id=g.rgd_id AND x.rgd_id=g.rgd_id AND x.xdb_key=? AND x.acc_id=?";

        return GeneQuery.execute(this, sql, xdbKey, accId);
    }

    /**
     * get genes by acc id
     * @param accId accession id
     * @return all possible list of genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGenesByXdbId(String accId) throws Exception {

        String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, rgd_acc_xdb x " +
                    "WHERE r.rgd_id=g.rgd_id AND x.rgd_id=g.rgd_id AND x.acc_id=?";

        return GeneQuery.execute(this, sql, accId);
    }

    /**
     * get all genes with given external id for given species
     * @param xdbKey - external db key
     * @param accId - external id to be looked for
     * @param speciesTypeKey species type key
     * @return list of Gene objects
     */
    public List<Gene> getGenesByXdbId(int xdbKey, String accId, int speciesTypeKey) throws Exception {

        // handle case where species is not specified
        if( speciesTypeKey==SpeciesType.ALL ) {
            return getGenesByXdbId(xdbKey, accId);
        }

        String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, rgd_acc_xdb x " +
            "WHERE r.rgd_id=g.rgd_id AND x.RGD_ID=g.RGD_ID AND x.XDB_KEY=? AND x.ACC_ID=? " +
            " AND r.species_type_key=?";

        return GeneQuery.execute(this, sql, xdbKey, accId, speciesTypeKey);
    }

    /**
     * get all genes with given external id for given species irrespective of xdb Key
     * @param accId - external id to be looked for
     * @param speciesTypeKey species type key
     * @return list of Gene objects
     */
    public List<Gene> getGenesByXdbId(String accId, int speciesTypeKey) throws Exception {

        // handle case where species is not specified
        if( speciesTypeKey==SpeciesType.ALL ) {
            return getGenesByXdbId(accId);
        }

        String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, rgd_acc_xdb x " +
            "where r.RGD_ID=g.RGD_ID AND x.RGD_ID=g.RGD_ID AND x.ACC_ID=?" +
            " AND r.species_type_key=?";

        return GeneQuery.execute(this, sql, accId, speciesTypeKey);
    }

    /**
     * get all genes with given external id
     * @param xdbKey - external db key
     * @param accId - external id to be looked for
     * @return list of Gene objects
     */
    public List<RgdId> getRGDIdsByXdbId(int xdbKey, String accId) throws Exception {

        String sql = "select r.* from  RGD_IDS r, RGD_ACC_XDB x " +
                    "where x.RGD_ID=r.RGD_ID AND x.XDB_KEY=? AND x.ACC_ID=?";

        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), sql);
        return execute(q, xdbKey, accId);
    }

    /**
     * get rgd ids for genes, excluding allele and splices, with matching accession ids and species
     * @param xdbKey - external db key
     * @param accId - external id to be looked for
     * @param speciesTypeKey - species type key for matching genes
     * @param excludedSources - list of sources of xdb ids, that should not be used when matching
     * @return list of gene rgd ids
     */
    public List<Integer> getGeneRgdIdsByXdbId(int xdbKey, String accId, int speciesTypeKey, List<String> excludedSources) throws Exception {

        String sql = "SELECT g.rgd_id FROM rgd_ids r, rgd_acc_xdb x, genes g " +
            "WHERE x.rgd_id=r.rgd_id AND r.rgd_id=g.rgd_id AND x.xdb_key=? AND x.acc_id=? AND r.species_type_key=? " +
            " AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
            " AND NVL(src_pipeline,'*') NOT IN ("+Utils.buildInPhraseQuoted(excludedSources)+")";

        return IntListQuery.execute(this, sql, xdbKey, accId, speciesTypeKey);
    }

    /**
     * get active genes with given external id
     * @param xdbKey - external db key
     * @param accId - external id to be looked for
     * @return list of Gene objects
     */
    public List<Gene> getActiveGenesByXdbId(int xdbKey, String accId) throws Exception {

        String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, rgd_acc_xdb x " +
                    "where r.RGD_ID=g.RGD_ID AND x.RGD_ID=g.RGD_ID AND x.XDB_KEY=? AND x.ACC_ID=? AND r.OBJECT_STATUS='ACTIVE'";

        return GeneQuery.execute(this, sql, xdbKey, accId);
    }

    /**
     * get active xdb ids for given xdb key and object key
     * @param xdbKey - external db key
     * @param objectKey - object key
     * @return list of Gene objects
     */
    public List<XdbId> getActiveXdbIds(int xdbKey, int objectKey) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x,rgd_id r "+
                "WHERE x.xdb_key=? AND object_key=? AND object_status='ACTIVE' AND x.rgd_id=r.rgd_id";

        return executeXdbIdQuery(sql, xdbKey, objectKey);
    }

    /**
     * return external ids for given xdb key and rgd-id
     * @param rgdId - rgd-id
     * @return list of external ids
     */
    public List<XdbId> getAllXdbIdsByRgdId(int rgdId) throws Exception {

        String sql = "SELECT a.* FROM rgd_acc_xdb a WHERE a.rgd_id=?";
        return executeXdbIdQuery(sql, rgdId);
    }


    /**
     * return external ids for given xdb key and rgd-id
     * @param xdbKey - external database key (like 2 for PubMed)
     * @param rgdId - rgd-id
     * @return list of external ids
     */
    public List<XdbId> getXdbIdsByRgdId(int xdbKey, int rgdId) throws Exception {

        String sql = "SELECT a.* FROM rgd_acc_xdb a WHERE a.rgd_id=? AND a.xdb_key=?";
        return executeXdbIdQuery(sql, rgdId, xdbKey);
    }

    /**
     * return external ids for objects of given type given xdb key and rgd-id
     * @param xdbKey - external database key (like 2 for PubMed)
     * @param rgdId - rgd-id
     * @param objectKey object key (f.e. 1 - genes; see RgdId)
     * @return list of external ids
     */
    public List<XdbId> getXdbIdsByRgdId(int xdbKey, int rgdId, int objectKey) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x,rgd_ids r "+
                "WHERE x.rgd_id=? AND x.xdb_key=? AND x.rgd_id=r.rgd_id AND r.object_key=?";

        return executeXdbIdQuery(sql, rgdId, xdbKey, objectKey);
    }

    /**
     * return external ids for given xdb key and rgd-id
     * @param xdbKeys - list of external database keys (like 2 for PubMed)
     * @param rgdId - rgd-id
     * @return list of external ids
     */
    public List<XdbId> getXdbIdsByRgdId(List xdbKeys, int rgdId) throws Exception {

        String sql = "SELECT * FROM rgd_acc_xdb a, rgd_xdb x WHERE a.xdb_key=x.xdb_key AND a.RGD_ID=? AND a.XDB_KEY IN ( "+
            Utils.buildInPhrase(xdbKeys) + ") order by x.xdb_name";

        return executeXdbIdQuery(sql, rgdId);
    }

    /**
     * get PubMed IDs for given reference rgd id
     * @param rgdId - reference rgd-id
     * @return list of external ids
     */
    public List<XdbId> getPubmedIdsByRefRgdId(int rgdId) throws Exception {

        String sql = "SELECT x.*, 0 species_type_key " +
                "FROM rgd_acc_xdb x, references f, rgd_ids r \n" +
                "WHERE x.RGD_ID = ? \n" +
                "AND x.RGD_ID=f.RGD_ID\n" +
                "AND x.RGD_ID=r.RGD_ID\n" +
                "AND r.OBJECT_STATUS='ACTIVE'\n" +
                "AND x.XDB_KEY = 2";

        return executeXdbIdQuery(sql, rgdId);
    }

    /**
     * get PubMed IDs without reference that were created after given cut-off date
     * @param cutoffDate cut-off date
     * @return list of PubMed Id accession ids, possibly empty
     */
    public List<String> getPubmedIdsWithoutReference(Date cutoffDate) throws Exception {

        String sql = "SELECT acc_id FROM rgd_acc_xdb x\n" +
                "  WHERE xdb_key=2 AND creation_date>?\n" +
                "  AND NOT EXISTS \n" +
                "  (SELECT 1 FROM rgd_acc_xdb u,references r\n" +
                "   WHERE r.rgd_id=u.rgd_id AND u.xdb_key=2 AND u.acc_id=x.acc_id)";

        return StringListQuery.execute(this, sql, cutoffDate);
    }

    /**
     * given pubmed id, return a reference rgd id
     * <p> note: some pubmed id could be assigned to multiple references, but only to one active reference, therefore
     * inactive references are skipped from the searches
     * @param pubmedId PubMed Accession Id
     * @return highest reference rgd id matching the pubmed id, or 0 if no match
     * @throws Exception
     */
    public int getRefIdByPubMedId(String pubmedId) throws Exception {

        String sql = "SELECT r.rgd_id FROM rgd_acc_xdb x,rgd_ids r "+
                "WHERE x.rgd_id=r.rgd_id AND x.xdb_key=2 AND r.object_key=12 AND x.acc_id=? AND r.object_status='ACTIVE'";
        List<Integer> results = IntListQuery.execute(this, sql, pubmedId);
        if( results.isEmpty() )
            return 0;
        if( results.size()>1 )
            throw new Exception("ERROR: PMID:"+pubmedId+" is assigned to multiple active references!");
        return results.get(0);
    }


    /**
     * get all xdb ids for given pipeline and species modified before given date and time
     *
     * @param modDate cut-off modification date
     * @param srcPipeline source pipeline
     * @param speciesTypeKey species type key (optional = if 0, rows for all species are processed)
     * @return list of matching xdb ids
     * @throws Exception on spring framework dao failure
     */
    public List<XdbId> getXdbIdsModifiedBefore(java.util.Date modDate, String srcPipeline, int speciesTypeKey) throws Exception{

        String query = "SELECT * FROM rgd_acc_xdb x WHERE modification_date<? AND src_pipeline=? ";
        if( speciesTypeKey!=0 )
            query += "AND EXISTS(SELECT 1 FROM rgd_ids r WHERE r.rgd_id=x.rgd_id AND r.species_type_key=?)";

        XdbQuery q = new XdbQuery(this.getDataSource(), query);
        if( speciesTypeKey!=0 )
            return execute(q, modDate, srcPipeline, speciesTypeKey);
        else
            return execute(q, modDate, srcPipeline);
    }

    /**
     * return XdbIDs for given xdb and pipeline modified before given date
     *
     * @param xdbKey xdb key
     * @param srcPipeline source
     * @param modDate modification date
     * @return XdbIds for given xdb and pipeline modified before given date
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<XdbId> getXdbIdsModifiedBefore(int xdbKey, String srcPipeline, java.util.Date modDate) throws Exception {

        String query = "SELECT * FROM rgd_acc_xdb WHERE xdb_key=? AND src_pipeline=? AND modification_date<?";
        return executeXdbIdQuery(query, xdbKey, srcPipeline, modDate);
    }

    /**
     * return count of rows for given xdb and pipeline modified before given date
     *
     * @param xdbKey xdb key
     * @param srcPipeline source
     * @param modDate modification date
     * @return count of rows for given xdb and pipeline modified before given date
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getCountOfXdbIdsModifiedBefore(int xdbKey, String srcPipeline, java.util.Date modDate) throws Exception {

        String query = "SELECT count(*) FROM rgd_acc_xdb WHERE xdb_key=? AND src_pipeline=? AND MODIFICATION_DATE<?";
        return getCount(query, xdbKey, srcPipeline, modDate);
    }

    /**
     * delete entries for given xdb and pipeline modified before given date
     *
     * @param xdbKey xdb key
     * @param srcPipeline source
     * @param modDate modification date
     * @return count of rows deleted
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteXdbIdsModifiedBefore(int xdbKey, String srcPipeline, java.util.Date modDate) throws Exception {

        String query = "DELETE FROM rgd_acc_xdb WHERE xdb_key=? AND src_pipeline=? AND MODIFICATION_DATE<?";
        return update(query, xdbKey, srcPipeline, modDate);
    }

    /**
     * return external ids for all rgd ids of given object group, like genes, and species
     * @param objectKey object key (1-genes, etc)
     * @param speciesTypeKey species type key
     * @return list of external ids
     */
    public List<XdbId> getXdbIdsByObjectKey(int objectKey, int speciesTypeKey) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x,rgd_ids r WHERE x.rgd_id=r.rgd_id AND r.object_key=? AND r.species_type_key=?";
        return executeXdbIdQuery(sql, objectKey, speciesTypeKey);
    }

    /**
     * return external ids for any combination of parameters;
     * if given parameter is null or 0, it means, that any value of this parameter could be accepted
     *
     * @param filter: acc_id,xdb_id,rgd_id and src_pipeline are checked
     * @return list of external ids
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<XdbId> getXdbIds(XdbId filter) throws Exception {

        String sql = "SELECT a.* FROM rgd_acc_xdb a WHERE ";

        // code below is using sql parameter binding feature to improve performance
        List<Object> oa = new ArrayList<Object>(4); // query parameter values
        List<SqlParameter> sqlParams = new ArrayList<SqlParameter>(4);
        StringBuilder params = new StringBuilder();

        return finishXdbIdQuery(filter, oa, sqlParams, sql, params);
    }

    /**
     * return external ids for any combination of parameters given in filter;
     * if given parameter is null or 0, it means, that any value of this parameter could be accepted
     *
     * @param filter - any combination of acc_id,xdb_id,rgd_id and src_pipeline is honored
     * @param speciesType - species type key
     * @return list of external ids matching the filter; empty list is returned if no matching entries are found
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<XdbId> getXdbIds(XdbId filter, int speciesType) throws Exception {

        String sql = "SELECT a.*,r.species_type_key FROM rgd_acc_xdb a,rgd_ids r WHERE r.rgd_id=a.rgd_id ";

        // code below is using sql parameter binding feature to improve performance
        List<Object> oa = new ArrayList<Object>(5); // query parameter values
        List<SqlParameter> sqlParams = new ArrayList<SqlParameter>(5);
        StringBuilder params = new StringBuilder();

        // add species_type_key parameter
        params.append("AND r.species_type_key=? ");
        sqlParams.add(new SqlParameter(Types.INTEGER));
        oa.add(speciesType);

        return finishXdbIdQuery(filter, oa, sqlParams, sql, params);
    }

    /**
     * return external ids for any combination of parameters given in filter;
     * if given parameter is null or 0, it means, that any value of this parameter could be accepted
     *
     * @param filter - any combination of acc_id,xdb_id,rgd_id and src_pipeline is honored
     * @param speciesType - species type key; if 0, any species
     * @param objectKey - object key; if 0, any kind of object
     * @return list of external ids matching the filter; empty list is returned if no matching entries are found
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<XdbId> getXdbIds(XdbId filter, int speciesType, int objectKey) throws Exception {

        if( objectKey <= 0 ) {
            return getXdbIds(filter, speciesType);
        }

        String sql = "SELECT a.*,r.species_type_key FROM rgd_acc_xdb a,rgd_ids r WHERE r.rgd_id=a.rgd_id ";

        // code below is using sql parameter binding feature to improve performance
        List<Object> oa = new ArrayList<Object>(6); // query parameter values
        List<SqlParameter> sqlParams = new ArrayList<SqlParameter>(6);
        StringBuilder params = new StringBuilder();

        if( speciesType>0 ) {
            // add species_type_key parameter
            params.append("AND r.species_type_key=? ");
            sqlParams.add(new SqlParameter(Types.INTEGER));
            oa.add(speciesType);
        }

        // add object_key parameter
        params.append("AND r.object_key=? ");
        sqlParams.add(new SqlParameter(Types.INTEGER));
        oa.add(objectKey);

        return finishXdbIdQuery(filter, oa, sqlParams, sql, params);
    }

    private List<XdbId> finishXdbIdQuery(XdbId xdbId, List<Object> oa, List<SqlParameter> sqlParams, String sql, StringBuilder params) throws Exception {
        
        // handle optional ACC_ID
        if( xdbId.getAccId()!=null && xdbId.getAccId().length()> 0 ) {
            if( params.length()>0 )
                params.append("and ");
            params.append("a.ACC_ID=? ");
            sqlParams.add(new SqlParameter(Types.VARCHAR));
            oa.add(xdbId.getAccId());
        }

        // handle optional XDB_ID
        if( xdbId.getXdbKey()> 0 ) {
            if( params.length()>0 )
                params.append("and ");
            params.append("a.XDB_KEY=? ");
            sqlParams.add(new SqlParameter(Types.INTEGER));
            oa.add(xdbId.getXdbKey());
        }

        // handle optional RGD_ID
        if( xdbId.getRgdId()> 0 ) {
            if( params.length()>0 )
                params.append("and ");
            params.append("a.RGD_ID=? ");
            sqlParams.add(new SqlParameter(Types.INTEGER));
            oa.add(xdbId.getRgdId());
        }

        // handle optional SRC_PIPELINE
        if( xdbId.getSrcPipeline()!=null && xdbId.getSrcPipeline().length()> 0 ) {
            if( params.length()>0 )
                params.append("and ");
            params.append("a.SRC_PIPELINE=? ");
            sqlParams.add(new SqlParameter(Types.VARCHAR));
            oa.add(xdbId.getSrcPipeline());
        }

        // run the query
        XdbQuery query = new XdbQuery(this.getDataSource(), sql+params);
        for( SqlParameter sqlParam: sqlParams )
            query.declareParameter(sqlParam);
        query.compile();
        return query.execute(oa.toArray());
    }


    /**
     * remove all external ids (RGD_ACC_XDB rows) for given RGD_ID and pipeline name
     * @param rgdId  RGD_ID in question
     * @param pipeline name of the pipeline
     * @return count of deleted rows
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteXdbIds( int rgdId, String pipeline ) throws Exception {

        String sql = "DELETE FROM rgd_acc_xdb WHERE rgd_id=? AND src_pipeline=?";
        return update(sql, rgdId, pipeline);
    }

    /**
     * delete all external ids (RGD_ACC_XDB rows) for given pipeline and species
     * @param pipeline  name of the pipeline
     * @param speciesTypeKey - if given, the deleted XDB_IDS must be related to RGD_IDs of proper species
     *                         if 0, any species will do
     * @return count of deleted rows
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteAllXdbIds( String pipeline, int speciesTypeKey ) throws Exception {

        // 1. delete all external ids for given pipeline -- no checking for species
        if( speciesTypeKey==0 ) {
            String sql = "delete from RGD_ACC_XDB where SRC_PIPELINE=?";
            return update(sql, pipeline);
        }

        // 2. delete all external ids for given pipeline and species
        String sql = "delete from RGD_ACC_XDB x where EXISTS(select RGD_ID from RGD_IDS r where x.RGD_ID=r.RGD_ID and x.SRC_PIPELINE=? and r.SPECIES_TYPE_KEY=?)";
        return update(sql, pipeline, speciesTypeKey);
    }

    /**
     * delete a list external ids (RGD_ACC_XDB rows);
     * if ACC_XDB_KEY is provided, it is used to delete the row;
     * else ACC_ID, RGD_ID, XDB_KEY and SRC_PIPELINE are used to locate and delete every row
     *
     * @param xdbIds list of external ids to be deleted
     * @return nr of rows deleted
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteXdbIds( List<XdbId> xdbIds ) throws Exception {

        // sanity check
        if (xdbIds==null || xdbIds.size()==0) {
            return 0;
        }

        String sql1 = "delete from RGD_ACC_XDB where RGD_ID=? and XDB_KEY=? and ACC_ID=? and SRC_PIPELINE=?";
        String sql2 = "delete from RGD_ACC_XDB where ACC_XDB_KEY=?";

        BatchSqlUpdate su1 = new BatchSqlUpdate(this.getDataSource(), sql1,
                new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
        su1.compile();

        BatchSqlUpdate su2 = new BatchSqlUpdate(this.getDataSource(), sql2,
                new int[]{Types.INTEGER});
        su2.compile();

        for (XdbId xdb : xdbIds ) {
            // determine which prepared statement is to be used
            if( xdb.getKey()>0 ) {
                su2.update(xdb.getKey());
            } else {
                su1.update(xdb.getRgdId(), xdb.getXdbKey(), xdb.getAccId(), xdb.getSrcPipeline());
            }
        }
        return Math.abs(executeBatch(su1) + executeBatch(su2));
    }

    /**
     * insert an XdbId; duplicate entries are not inserted (with same RGD_ID,XDB_KEY,ACC_ID,SRC_PIPELINE)
     * @param xdb XdbId object to be inserted
     * @return number of actually inserted rows (0 or 1)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertXdb(XdbId xdb) throws Exception {
        List<XdbId> xdbs = new ArrayList<XdbId>(1);
        xdbs.add(xdb);
        return insertXdbs(xdbs);
    }

    /**
     * insert a bunch of XdbIds; duplicate entries are not inserted (with same RGD_ID,XDB_KEY,ACC_ID,SRC_PIPELINE)
     * @param xdbs list of XdbIds objects to be inserted
     * @return number of actually inserted rows
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertXdbs(List<XdbId> xdbs) throws Exception {

        // sanity check
        if (xdbs==null || xdbs.size()==0) {
            return 0;
        }

        // this sql query ensures we will not insert duplicate entries into RGD_ACC_XDB
        String sql = "insert into RGD_ACC_XDB "
            +"(ACC_XDB_KEY, RGD_ID, XDB_KEY, ACC_ID, CREATION_DATE, LINK_TEXT, SRC_PIPELINE, NOTES, MODIFICATION_DATE) "
            +"(select ?,?,?,?,nvl(?,sysdate),?,?,?,? from DUAL where not exists "
            +" (select 1 from RGD_ACC_XDB where RGD_ID=? and XDB_KEY=? and ACC_ID=? and SRC_PIPELINE=?)"
            +")";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql, new int[]{
                Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,
                Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
        su.compile();

        for( XdbId xdbId: xdbs ) {
            xdbId.setKey(this.getNextKey());
            su.update(xdbId.getKey(), xdbId.getRgdId(), xdbId.getXdbKey(), xdbId.getAccId(), xdbId.getCreationDate(),
                    xdbId.getLinkText(), xdbId.getSrcPipeline(), xdbId.getNotes(), xdbId.getModificationDate(),
                    xdbId.getRgdId(), xdbId.getXdbKey(), xdbId.getAccId(), xdbId.getSrcPipeline());
        }

        return executeBatch(su);
    }

    /**
     * for a bunch of rows identified by acc_xdb_key, set MODIFICATION_DATE to SYSDATE
     * @param accXdbKeys list of ACC_XDB_KEYs
     * @return number of actually updated rows
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateModificationDate(List<Integer> accXdbKeys) throws Exception {

        // sanity check
        if (accXdbKeys==null || accXdbKeys.size()==0) {
            return 0;
        }

        // Oracle internal limit of in-phrase is 1000 -- handle cases when list is longer than 1000
        int rowsAffected = 0;
        int toIndex;
        for( int i=0; i<accXdbKeys.size(); i+= 1000 ) {
            toIndex = i + 1000;
            if( toIndex > accXdbKeys.size() )
                toIndex = accXdbKeys.size();

            String sql = "UPDATE rgd_acc_xdb "
                +"SET modification_date=SYSDATE "
                +"WHERE acc_xdb_key IN("+Utils.buildInPhrase(accXdbKeys.subList(i, toIndex))+")";
            rowsAffected += update(sql);
        }
        return rowsAffected;
    }

    /**
     * update properties of this row by ACC_XDB_KEY
     * @param xdbId object with data to be updated
     * @return 1 if row have been changed, or 0 if left unchanged
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateByKey(XdbId xdbId) throws Exception {

        String sql = "update RGD_ACC_XDB set RGD_ID=?, XDB_KEY=?, ACC_ID=?, LINK_TEXT=?, SRC_PIPELINE=?, NOTES=? "+
            ", CREATION_DATE=?, MODIFICATION_DATE=? "+
            "where ACC_XDB_KEY=?";
        return update(sql, xdbId.getRgdId(), xdbId.getXdbKey(), xdbId.getAccId(), xdbId.getLinkText(), xdbId.getSrcPipeline(),
                xdbId.getNotes(), xdbId.getCreationDate(), xdbId.getModificationDate(), xdbId.getKey());
    }

    // given XDB_KEY, return XDB_NAME
    // @return "" if key not found in the database
    public String getXdbName(int xdbKey) throws Exception {

        String sql = "select XDB_NAME from RGD_XDB where XDB_KEY=?";
        return Utils.defaultString(getStringResult(sql, xdbKey));
    }

    /**
     * for external database identified by XDB_KEY and species identified by type key, return url to access data in external database
     * or null if there is no match in database
     * @param xdbKey XDB_KEY of external database
     * @param speciesTypeKey species type key
     * @return url to external database or null on error
     * @throws Exception when something went wrong in spring framework
     */
    public String getXdbUrl(int xdbKey, int speciesTypeKey) throws Exception {

        String sql = "select XDB_URL from RGD_XDB_SPECIES_URL where XDB_KEY=? and SPECIES_TYPE_KEY=?";
        return getStringResult(sql, xdbKey, speciesTypeKey);
    }

    /**
     * for external database identified by XDB_KEY, return species and urls to access data in external database
     * @param xdbKey XDB_KEY of external database
     * @return species to url to external database
     * @throws Exception when something went wrong in spring framework
     */
    public List<IntStringMapQuery.MapPair> getXdbUrls(int xdbKey) throws Exception {

        String sql = "SELECT species_type_key,xdb_url FROM rgd_xdb_species_url WHERE XDB_KEY=?";
        return IntStringMapQuery.execute(this, sql, xdbKey);
    }

    /**
     * for external database identified by XDB_KEY and species identified by type key, return url to access data in external database
     * @param xdbKey XDB_KEY of external database
     * @return url to external database or null on error
     * @throws Exception when something went wrong in spring framework
     */
    public String getXdbUrlnoSpecies(int xdbKey) throws Exception {

        String sql = "select XDB_URL from RGD_XDB where XDB_KEY=?";
        return getStringResult(sql, xdbKey);
    }

    /**
     * return list of curated pubmed ids for given rgd object; that means the rgd object is searched for rgd references
     * and pubmed ids associated with these references will be returned
     * @param rgdId object rgd id
     * @return list of XdbIds
     * @throws Exception when something went wrong in spring framework
     */
    public List<XdbId> getCuratedPubmedIds ( int rgdId ) throws Exception {
        String sql = "select x.*, 0 species_type_key " +
                "from rgd_ref_rgd_id r, rgd_acc_xdb x, references rf " +
                "where r.rgd_id=? and r.ref_key = rf.ref_key and rf.rgd_id = x.rgd_id and x.xdb_key = 2";

        return executeXdbIdQuery(sql, rgdId);
  	}

    /**
     * return list of uncurated pubmed ids for given rgd object; that means the rgd object is searched for rgd references
     * and pubmed ids associated with given rgd object but not associated with these references will be returned
     * @param rgdId object rgd id
     * @return list of XdbIds
     * @throws Exception when something went wrong in spring framework
     */
    public List<XdbId> getUncuratedPubmedIds ( int rgdId ) throws Exception {
        // PUBMED ids that do not have curated references in RGD
        String sql =
            "SELECT x.*, 0 species_type_key "+
            "FROM rgd_acc_xdb x "+
            "WHERE x.rgd_id=? AND x.xdb_key = 2 "+
            "AND NOT EXISTS( SELECT 1 "+
            "  FROM rgd_ref_rgd_id r, rgd_acc_xdb x2, references rf "+
            "  WHERE r.rgd_id=x.rgd_id AND r.ref_key=rf.ref_key AND rf.rgd_id=x2.rgd_id "+
                "AND x2.xdb_key=x.xdb_key AND x2.acc_id=x.acc_id)";

        return executeXdbIdQuery(sql, rgdId);
  	}

    /**
     * return curated pubmed ids
     * @param objectKey object key for which we return the curated pubmed ids
     * @param speciesTypeKey species type key
     * @return map of objects: rgd id mapped to set of accession ids
     * @throws Exception
     */
    public HashMap<Integer, Set<String>> getCuratedPubmedIds ( int objectKey, int speciesTypeKey ) throws Exception {

        final HashMap<Integer, Set<String>> results = new HashMap<>();

        String sql = "SELECT r.rgd_id,x.acc_id " +
                "FROM rgd_ids r,rgd_ref_rgd_id rr,references rf,rgd_acc_xdb x " +
                "WHERE r.object_key=? AND r.species_type_key=? AND object_status='ACTIVE' "+
                "AND rr.rgd_id=r.rgd_id AND rf.ref_key=rr.ref_key " +
                "AND rf.rgd_id=x.rgd_id AND x.xdb_key=2 ";

        MappingSqlQuery query = new MappingSqlQuery(getDataSource(), sql) {
            protected Object mapRow(ResultSet rs, int i) throws SQLException {
                int rgdId = rs.getInt(1);
                String accId = rs.getString(2);
                Set<String> pubmedIds = results.get(rgdId);
                if( pubmedIds==null ) {
                    pubmedIds = new HashSet<String>();
                    results.put(rgdId, pubmedIds);
                }
                pubmedIds.add(accId);
                return null;
            }
        };
        execute(query, objectKey, speciesTypeKey);
        return results;
  	}

    /**
     * Return a list of all xdb sources in database
     * @return list of Xdb objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Xdb> getXdbs() throws Exception {
        String sql = "SELECT * FROM rgd_xdb ORDER BY UPPER(xdb_name)";
        return getXdbsInternal(sql);
  	}

    /**
     * Return a list of active xdb sources in database
     * <p>
     * Xdb source is active if it has entries in RGD_ACC_XDB table
     * @return list of active Xdb objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Xdb> getActiveXdbs() throws Exception {
        String sql = "SELECT * FROM rgd_xdb x "+
                "WHERE EXISTS(SELECT 1 FROM rgd_acc_xdb a WHERE a.xdb_key=x.xdb_key) "+
                "ORDER BY UPPER(xdb_name)";
        return getXdbsInternal(sql);
  	}

    /**
     * Return a list of inactive xdb sources in database
     * <p>
     * Xdb source is inactive if it does NOT have entries in RGD_ACC_XDB table
     * @return list of inactive Xdb objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Xdb> getInactiveXdbs() throws Exception {
        String sql = "SELECT * FROM rgd_xdb x "+
                "WHERE NOT EXISTS(SELECT 1 FROM rgd_acc_xdb a WHERE a.xdb_key=x.xdb_key) "+
                "ORDER BY UPPER(xdb_name)";
        return getXdbsInternal(sql);
  	}

    private List<Xdb> getXdbsInternal(String sql) throws Exception {

        RgdXdbQuery query = new RgdXdbQuery(this.getDataSource(), sql);
        query.compile();
        List<Xdb> xdbs = query.execute();
        String url;
        int speciesTypeKey;

        for (Xdb xdb: xdbs) {
            for( IntStringMapQuery.MapPair pair: getXdbUrls(xdb.getKey()) ) {
                speciesTypeKey = pair.keyValue;
                url = pair.stringValue;
                if( url==null )
                    url = xdb.getUrl();
                xdb.addURL(speciesTypeKey, url);
            }
        }

        return xdbs;
  	}


    /**
     * get result set for list of external identifiers -- to be used by Spider, for compatibility;
     * NOTE: exclude xdb ids for zebrafish
     * @return ResultSet
     * @throws Exception when something went wrong in spring framework
     */
    public ResultSet getExternalIdsResultSet() throws Exception {

        String sql = "SELECT x.rgd_id, x.acc_id, i.species_type_key, o.object_name, d.xdb_name " +
                "FROM rgd_acc_xdb x, rgd_ids i, rgd_objects o, rgd_xdb d " +
                "WHERE x.rgd_id = i.rgd_id AND i.object_key = o.object_key AND x.xdb_key = d.xdb_key "+
                " AND i.object_status='ACTIVE' AND i.species_type_key<>8";
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet getExternalIdsResultSet(int rgdId) throws Exception {

        String sql = "SELECT x.rgd_id, x.acc_id, i.species_type_key, o.object_name, d.xdb_name " +
                "FROM rgd_acc_xdb x, rgd_ids i, rgd_objects o, rgd_xdb d " +
                "WHERE x.rgd_id = i.rgd_id AND i.object_key = o.object_key AND x.xdb_key = d.xdb_key AND i.object_status='ACTIVE' AND x.rgd_id=?";
        PreparedStatement pstmt=getConnection().prepareStatement(sql);
        pstmt.setInt(1, rgdId);
        return pstmt.executeQuery();
    }

    /**
     * get next external identifier from result set;
     * if there are no external identifiers available, result set (and underlying connection) is closed and null is returned
     * @param rs ResultSet opend by a call to getExternalIdsResultSet
     * @return ExternalIdentifierXRef object or null
     * @throws Exception when something went wrong in spring framework
     */
    public ExternalIdentifierXRef getNextExternalIdentifierXRef(ResultSet rs) throws Exception {

        if( !rs.next() ) {
            // no more results available
            Statement stmt = rs.getStatement();
            Connection conn = stmt.getConnection();

            rs.close();
            stmt.close();
            conn.close();
            return null;
        }

        ExternalIdentifierXRef xref = new ExternalIdentifierXRef();
        xref.setExId(rs.getString("acc_id" ));
        xref.setObjectType(rs.getString("object_name" ));
        xref.setRgdId(rs.getInt("rgd_id" ));
        xref.setSpeciesTypeKey(rs.getInt("species_type_key" ));
        xref.setIdType(rs.getString("xdb_name" ));
        return xref;
    }

    // returns next key for RGD_ACC_XDB table
    public int getNextKey() throws Exception {
        return getNextKeyFromSequence("RGD_ACC_XDB_SEQ");
    }

    // used for querying RGD_XDB table
    public class RgdXdbQuery extends MappingSqlQuery {

        public RgdXdbQuery(DataSource ds, String query) {
            super(ds, query);
        }

        protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Xdb xdb = new Xdb();
            xdb.setKey(rs.getInt("xdb_key"));
            xdb.setName(rs.getString("xdb_name"));
            xdb.setUrl(rs.getString("xdb_url"));
            xdb.setNotes(rs.getString("notes"));
            return xdb;
        }
    }

    /// XdbId query implementation helper
    public List<XdbId> executeXdbIdQuery(String query, Object ... params) throws Exception {
        XdbQuery q = new XdbQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    /**
     * return external ids for objects of given type given xdb key and rgd-id and between dates
     * @return list of external ids
     */
    public List<XdbId> getXdbIds(int rgdId, Date from, Date to) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x "+
                "WHERE x.rgd_id=? AND x.creation_date BETWEEN ? AND ?";

        return executeXdbIdQuery(sql, rgdId, from, to);
    }

    /**
     * return external ids for objects of given type given xdb key and rgd-id and between dates
     * @return list of external ids
     */
    public List<XdbId> getXdbIds(int rgdId, Date from, Date to, int xdbKey) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x "+
                "WHERE x.rgd_id=? and x.xdb_key=? and x.creation_date between ? and ?";

        return executeXdbIdQuery(sql, rgdId, xdbKey, from, to);
    }

    /**
     * return external ids for objects of given type given xdb key and rgd-id and between dates
     * @return list of external ids
     */
    public List<XdbId> getXdbIds(int rgdId, Date from, Date to, List<Integer> xdbKeys) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x "+
                "WHERE x.rgd_id=? AND x.creation_date between ? and ? and x.xdb_key in("
                + Utils.buildInPhrase(xdbKeys) + ")";

        return executeXdbIdQuery(sql, rgdId, from, to);
    }

    /**
     * return external ids for objects of given type given xdb key and rgd-id and between dates
     * @return list of external ids
     */
    public List<XdbId> getXdbIdsWithExclusion(int rgdId, Date from, Date to, List<Integer> xdbKeys) throws Exception {

        String sql = "SELECT x.* FROM rgd_acc_xdb x "+
                "WHERE x.rgd_id=? AND x.creation_date between ? and ? and x.xdb_key not in("
                + Utils.buildInPhrase(xdbKeys) + ")";

        return executeXdbIdQuery(sql, rgdId, from, to);
    }
}
