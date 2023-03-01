package edu.mcw.rgd.dao;

import edu.mcw.rgd.dao.spring.CountLongQuery;
import edu.mcw.rgd.dao.spring.CountQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 28, 2007
 * Time: 9:38:02 AM
 * <p>
 * Abstract class to be implemented by data access objects in rgd.
 */
public class AbstractDAO implements DAO {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Returns an initialized DataSource
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getDataSource();
    }

    /**
     * Returns an initialized DataSource
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getEnsemblDataSource() throws Exception{
        return DataSourceFactory.getInstance().getEnsemblDataSource();
    }

    /**
     * Returns an initialized DataSource
     * @return DataSource object
     * @throws Exception
     */
    public DataSource getSkygenDataSource() throws Exception{
        return DataSourceFactory.getInstance().getDataSource("skygen");
    }


    /**
     * Return a data base connection object.
     * @return Connection object
     * @throws Exception
     */
    public Connection getConnection() throws Exception{
        return DataSourceFactory.getInstance().getDataSource().getConnection();
    }

    /**
     * selects max value from column, and returns the next integer.  Many tables in RGD do not
     * increment key fields automatically.  This method provide a programmatic way to do it.
     *
     * @param table table name
     * @param column column name
     * @return new, possibly unique, value for the column
     * @throws Exception
     * @deprecated you should use getNextKeyFromSequence() whenever possible
     */
    public int getNextKey(String table, String column) throws Exception {

        String sql = "select max(" + column + " ) as nextVal from " + table;
        return 1+getCount(sql);
    }

    /**
     * select next value from a sequence
     *
     * @param seqName sequence name
     * @return next key value from sequence
     * @throws Exception
     */
    public int getNextKeyFromSequence(String seqName) throws Exception {

        String query = "SELECT " + seqName + ".nextval from dual";
        return getCount(query);
    }

    /**
     * select next value from a sequence
     *
     * @param seqName sequence name
     * @return next key value from sequence
     * @throws Exception
     */
    public int getNextKey(String seqName) throws Exception {

        return getNextKeyFromSequence(seqName);
    }

    public SqlParameter getType(int type, Object value) {
        if (value == null) {
            return new SqlParameter(Types.NULL);
        }else {
            return new SqlParameter(type);
        }
    }

    /**
     * execute batch statement and return number of rows affected
     * @param bsu BatchSqlUpdate object
     * @return total number of rows affected
     */
    public int executeBatch(BatchSqlUpdate bsu) {
        bsu.flush();

        // compute nr of rows affected
        int totalRowsAffected = 0;
        for( int rowsAffected: bsu.getRowsAffected() ) {
            totalRowsAffected += rowsAffected;
        }
        return totalRowsAffected;
    }

    /**
     * return db user name and url
     * @return db user name and url
     */
    public String getConnectionInfo() {
        try( Connection conn = getConnection() ) {
            DatabaseMetaData meta = conn.getMetaData();
            return "DB user="+meta.getUserName()+", url="+meta.getURL();
        }
        catch( Exception e ) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * execute sql query and return a single integer
     * f.e. as in a query SELECT COUNT(*) FROM GENES
     * @param sqlQuery sql query returning a single row: an integer
     * @return integer value, result of the query
     * @throws Exception
     */
    public int getCount(String sqlQuery, Object... params) throws Exception {
        CountQuery q = new CountQuery(this.getDataSource(), sqlQuery);
        List<Integer> results = execute(q, params);
        return results.get(0);
    }

    /**
     * execute sql query and return a single long integer
     * f.e. as in a query SELECT COUNT(LENGTH(gene_symbol)) FROM GENES
     * @param sqlQuery sql query returning a single row: a long integer
     * @return long integer value, result of the query
     * @throws Exception
     */
    public long getLongCount(String sqlQuery, Object... params) throws Exception {
        CountLongQuery q = new CountLongQuery(this.getDataSource(), sqlQuery);
        List<Long> results = execute(q, params);
        return results.get(0);
    }

    /**
     * execute sql query and return a single string from the first row of results
     * f.e. as in a query SELECT object_status FROM rgd_ids WHERE rgd_id=2004
     * @param sqlQuery sql query returning a single row: an integer
     * @return string value, result of the query; return null if there are no results, or the value is null
     * @throws Exception
     */
    public String getStringResult(String sqlQuery, Object... params) throws Exception {
        List<String> results = StringListQuery.execute(this, sqlQuery, params);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * return count of rows in given table
     * @param tableName table name
     * @return count of rows in the table
     * @throws Exception
     */
    public long getRowCount(String tableName) throws Exception {

        String query = "SELECT COUNT(0) FROM "+tableName;
        return getLongCount(query);
    }

    /**
     * helper method to simplify execution of queries with parameters
     * <p>
     * this method will automatically make all the required q.declareParameter() calls,
     * then it will call q.compile() and finally it will call q.execute() passing all of the parameters
     * @param q instance of MappingSqlQuery object
     * @param params variable list of params
     * @return List
     */
    public List execute(MappingSqlQuery q, Object ... params) {

        // declare parameters
        for( Object param: params ) {
            q.declareParameter(new SqlParameter(getParamType(param)));
        }

        // compile sql statement
        q.compile();

        // execute the statement
        return q.execute(params);
    }

    /**
     * helper method to call SqlUpdate wrapper with variable number of paramaters
     * @param sql sql query
     * @param params variable list of parameters
     * @return count of rows affected
     */
    public int update(String sql, Object ... params) throws Exception {

        SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

        // declare parameters
        for( Object param: params ) {
            su.declareParameter(new SqlParameter(getParamType(param)));
        }
        su.compile();

        return su.update(params);
    }

    private int getParamType(Object param) {
        int paramType;
        if( param == null ) {
            paramType = Types.NULL;
        }
        else if( param instanceof String ) {
            paramType = Types.VARCHAR;
        }
        else if( param instanceof Integer ) {
            paramType = Types.INTEGER;
        }
        else if( param instanceof Long ) {
            paramType = Types.BIGINT;
        }
        else if( param instanceof java.util.Date ) {
            paramType = Types.TIMESTAMP;
        }
        else if( param instanceof Double ) {
            paramType = Types.DOUBLE;
        }
        else {
            paramType = Types.OTHER;
        }
        return paramType;
    }

    /**
     * Returns a list of distinct values in a single column from a table
     *
     * @param table
     * @param column
     * @param addEmpty
     * @return list of distinct values
     * @throws Exception
     */
    public List<String> getDistinct(String table, String column, boolean addEmpty) throws Exception{
        String query = "SELECT distinct(" + column + ") from " + table + " order by " + column;

        List<String> l = StringListQuery.execute(this, query);

        if (addEmpty) {
            l.add(0,"");
        }
        return l;
    }

    public void setInt(CallableStatement cs, int fieldNr, Integer val) throws SQLException {
        if( val==null )
            cs.setNull(fieldNr, Types.INTEGER);
        else
            cs.setInt(fieldNr, val);
    }

    public void setTimestamp(CallableStatement cs, int fieldNr, java.util.Date val) throws SQLException {
        if( val==null )
            cs.setNull(fieldNr, Types.TIMESTAMP);
        else
            cs.setTimestamp(fieldNr, new Timestamp(val.getTime()));
    }


    ///// DDL operations: enable/disable indexes/constraints

    /**
     * get all indexes for table with given status
     * @param tableName table name
     * @param indexStatus index status, f.e. VALID or UNUSABLE
     * @return names of indexes
     * @throws Exception
     */
    public List<String> getIndexesForTable(String tableName, String indexStatus) throws Exception {

        String sql = "SELECT index_name FROM user_indexes WHERE table_name=? AND status=?";
        //String sql = "SELECT index_name FROM user_indexes WHERE table_name=? AND status=? AND UNIQUENESS<>'UNIQUE'";
        StringListQuery q = new StringListQuery(this.getDataSource(), sql);
        return execute(q, tableName, indexStatus);
    }

    public int disableIndexesForTable(String tableName) throws Exception {

        int count = 0;
        for( String indexName: getIndexesForTable(tableName, "VALID"))  {
            String sql = "ALTER INDEX "+indexName+" UNUSABLE";
            System.out.println(sql);

            update(sql);
            count++;
        }
        return count;
    }

    public int enableIndexesForTable(String tableName) throws Exception {

        int count = 0;
        for( String indexName: getIndexesForTable(tableName, "UNUSABLE"))  {
            String sql = "ALTER INDEX "+indexName+" REBUILD PARALLEL NOCOMPRESS NOLOGGING";
            System.out.println(sql);

            update(sql);
            count++;
        }
        return count;
    }


    public List<String> getConstraintsForTable(String tableName, String constraintType) throws Exception {

        String sql = "SELECT constraint_name FROM user_constraints WHERE table_name=? AND constraint_type=?";
        StringListQuery q = new StringListQuery(this.getDataSource(), sql);
        return execute(q, tableName, constraintType);
    }

    public int disableConstraintsForTable(String tableName) throws Exception {

        int count = 0;
        for( String constraintName: getConstraintsForTable(tableName, "R"))  {
            String sql = "ALTER TABLE "+tableName+" DISABLE CONSTRAINT "+constraintName;
            //System.out.println(sql);

            update(sql);
            count++;
        }
        return count;
    }

    public int enableConstraintsForTable(String tableName) throws Exception {

        int count = 0;
        for( String constraintName: getConstraintsForTable(tableName, "R"))  {
            String sql = "ALTER TABLE "+tableName+" ENABLE CONSTRAINT "+constraintName;
            //System.out.println(sql);

            update(sql);
            count++;
        }
        return count;
    }

}
