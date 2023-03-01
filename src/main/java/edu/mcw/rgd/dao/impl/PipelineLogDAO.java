package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.*;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Feb 22, 2010
 * Time: 9:45:36 AM
 */
public class PipelineLogDAO extends AbstractDAO {

    // get list of all pipelines
    public List<Pipeline> getPipelines() throws Exception {

        String query = "SELECT * FROM pipelines";
        PipelineQuery q = new PipelineQuery(this.getDataSource(), query);
        return execute(q);
    }

    // get pipeline object given pipeline name and species type key
    public Pipeline getPipelineKey(String pipelineName, int speciesTypeKey ) throws Exception {
        String species = SpeciesType.getCommonName(speciesTypeKey);
        String query = "SELECT * FROM pipelines WHERE pipeline_name=? AND species=?";
        PipelineQuery q = new PipelineQuery(this.getDataSource(), query);
        List<Pipeline> pipelines = execute(q, pipelineName, species);
        if( pipelines.size()>0 )
            return pipelines.get(0);
        return null;
    }

    public int insertPipeline(String pipelineName, int speciesTypeKey) throws Exception {
        String species = SpeciesType.getCommonName(speciesTypeKey);
        int key = getNextKeyFromSequence("PIPELINES_SEQ");
        String sql = "INSERT INTO pipelines (pipeline_key,pipeline_name,species) VALUES(?,?,?)";
        update(sql, key, pipelineName, species);
        return key;
    }

    // get pipeline object given pipeline key
    public Pipeline getPipeline( int pipelineKey ) throws Exception {
        String query = "SELECT * FROM pipelines WHERE pipeline_key=?";
        PipelineQuery pq = new PipelineQuery(this.getDataSource(), query);
        List<Pipeline> pipelines = execute(pq, pipelineKey);
        if( pipelines.size()>0 )
            return pipelines.get(0);
        return null;
    }

    /**
     * get list of all pipeline logs for given pipeline; most recent logs are returned first;
     * retMax parameter is used to restrict the number of rows returned
     * @param pipelineKey pipeline key identifying a pipeline; if 0, all pipeline logs are returned!
     * @param retMax maximum number of rows to be returned; if 0 or negative, then it is set to 100
     * @return list of pipeline logs associated with given pipeline
     * @throws Exception
     */
    public List<PipelineLog> getPipelineLogs(int pipelineKey, int retMax) throws Exception {

        if( retMax <= 0 )
            retMax = 100;

        if( pipelineKey!=0 ) {
            // return pipeline logs for given pipeline
            String query = "select * from ("+
                    "select PIPELINE_KEY,PIPELINE_LOG_KEY,RUN_START_TIME,RUN_END_TIME,SUCCESS_IND,RUN_MODE "+
                    "from PIPELINE_LOGS where PIPELINE_KEY=? order by PIPELINE_LOG_KEY desc"+
                    ") where rownum<=?";

            PipelineLogQuery pq = new PipelineLogQuery(this.getDataSource(), query, null);
            return execute(pq, pipelineKey, retMax);
        }
        else {
            // return all pipeline logs
            String query = "select * from ("+
                "select PIPELINE_KEY,PIPELINE_LOG_KEY,RUN_START_TIME,RUN_END_TIME,SUCCESS_IND,RUN_MODE "+
                "from PIPELINE_LOGS order by PIPELINE_LOG_KEY desc"+
                ") where rownum<=?";

            PipelineLogQuery pq = new PipelineLogQuery(this.getDataSource(), query, null);
            return execute(pq, retMax);
        }
    }

    // given pipeline log key, return pipeline log object
    public PipelineLog getPipelineLog(int pipelineLogKey) throws Exception {

        String query = "select PIPELINE_KEY,PIPELINE_LOG_KEY,RUN_START_TIME,RUN_END_TIME,SUCCESS_IND,RUN_MODE "+
                "from PIPELINE_LOGS where PIPELINE_LOG_KEY=?";

        PipelineLogQuery pq = new PipelineLogQuery(this.getDataSource(), query, null);

        List<PipelineLog> logs = execute(pq, pipelineLogKey);
        if (logs==null || logs.size()==0 )
            return null;
        PipelineLog plog = logs.get(0);

        // load pipeline properties as well
        plog.setPipeline(this.getPipeline(plog.getPipeline().getPipelineKey()));
        
        return plog;
    }

    /**
     * get most recent pipeline log given pipeline-name and species; optionally you can specify runMode
     * if runOutcome
     * @param pipelineName - pipeline name
     * @param speciesTypeKey - species type key: 1, 2 or 3
     * @param runMode - filter by run-mode specific to given pipeline -- can be null
     * @param runOutcome - filter by run-outcome: 'Y'-pipeline successful, 'N'-pipeline failed, null-any
     * @return pipeline log in question or null if not found
     */
    public PipelineLog getLastPipelineLog(String pipelineName, int speciesTypeKey, String runMode, String runOutcome) throws Exception {

        // first initialize the pipeline key
        Pipeline pipeline = this.getPipelineKey(pipelineName, speciesTypeKey);

        String query = "select PIPELINE_KEY,PIPELINE_LOG_KEY,RUN_START_TIME,RUN_END_TIME,SUCCESS_IND,RUN_MODE "+
                "from PIPELINE_LOGS where PIPELINE_KEY=? and RUN_MODE=? ";
        if( runOutcome!=null ) {
            if( runOutcome.equals("Y") || runOutcome.equals("N") )
                query += "and SUCCESS_IND='"+runOutcome+"' ";
        }
        query += "order by PIPELINE_LOG_KEY desc"; // most recent log first
        
        PipelineLogQuery pq = new PipelineLogQuery(this.getDataSource(), query, pipeline);
        List<PipelineLog> logs = execute(pq, pipeline.getPipelineKey(),runMode);

        if( logs==null || logs.size()==0 )
            return null; // empty result set

        return logs.get(0);
    }

    /**
     * load from given pipeline log list of log-props
     * @param plog - pipeline-log object to be searched for properties
     * @param propType - optional filter by prop-type; if null, any prop-type will be considered during search
     * @param recNo - optional filter by recNo (if less than zero, any recno will match, including null)
     * @return list of LogProp objects
     */
    public List<PipelineLog.LogProp> getLogProps(PipelineLog plog, String propType, int recNo) throws Exception {

        // sanity check
        if( plog==null )
            return null;
        
        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML from PIPELINE_LOG_PROPERTIES ";
        query += "where PIPELINE_LOG_KEY=? ";
        if( propType!= null) {
            query += "and PIPELINE_LOG_PROPERTYTYPES_KEY='"+propType+"' ";
        }
        if( recNo >= 0 ) {
            query += "and EVENT_RECORD_NO="+Integer.toString(recNo)+" ";
        }
        query += "order by PIPELINE_LOG_PROPERTY_KEY desc";

        PipelineLogPropQuery pq = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        List<PipelineLog.LogProp> logProps = execute(pq, plog.getPipelineLogKey());
        return logProps;
    }

    /**
     * get for given pipeline a list of log-props
     * @param pipeline - pipeline object to be searched for log properties
     * @param propType - filter by prop-type
     * @param recNo - optional filter by recNo (if less than zero, any recno will match, including null)
     * @return list of LogProp objects
     */
    public List<PipelineLog.LogProp> getLogProps(Pipeline pipeline, String propType, int recNo) throws Exception {

        String query = "select p.PIPELINE_LOG_PROPERTY_KEY,p.PIPELINE_LOG_KEY,p.PIPELINE_LOG_PROPERTYTYPES_KEY,p.EVENT_NOTES,p.EVENT_VALUE,p.EVENT_RECORD_NO,p.EVENT_TIMESTAMP,p.EVENT_XML "+
                "from PIPELINE_LOG_PROPERTIES p,PIPELINE_LOGS l where l.PIPELINE_LOG_KEY=p.PIPELINE_LOG_KEY "+
                "and l.PIPELINE_KEY=? and PIPELINE_LOG_PROPERTYTYPES_KEY=?";
        if( recNo >= 0 ) {
            query += " and EVENT_RECORD_NO="+Integer.toString(recNo);
        }

        PipelineLog plog = PipelineLog.getTempInstance(pipeline);
        PipelineLogPropQuery pq = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        List<PipelineLog.LogProp> logProps = execute(pq, pipeline.getPipelineKey(), propType);
        return logProps;
    }

    public PipelineLog.LogProp getLogProp(int eventKey) throws Exception {

        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML "+
                "from PIPELINE_LOG_PROPERTIES where PIPELINE_LOG_PROPERTY_KEY=?";

        Pipeline pipeline = new Pipeline();
        PipelineLog plog = PipelineLog.getTempInstance(pipeline);
        PipelineLogPropQuery pq = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        List<PipelineLog.LogProp> logProps = execute(pq, eventKey);
        if( logProps!=null && logProps.size()>0 )
            return logProps.get(0);
        return null;
    }

    /**
     * for logprop identified by id (EVENT_KEY) update key (PIPELINE_LOG_PROP_KEY), info (EVENT_INFO) and value (EVENT_VALUE)
     * @param prop
     * @throws Exception
     */
    public void updateLogProp(PipelineLog.LogProp prop) throws Exception {

        String query = "update PIPELINE_LOG_PROPERTIES set PIPELINE_LOG_PROPERTYTYPES_KEY=?,EVENT_NOTES=?,EVENT_VALUE=? WHERE PIPELINE_LOG_PROPERTY_KEY=?";
        update(query, prop.getKey(), prop.getInfo(), prop.getValue(), prop.getId());
    }

    public List<PipelineLog.LogProp> getLogProps(int pipelineLogKey, int recNo) throws Exception {

        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML "+
                "from PIPELINE_LOG_PROPERTIES where PIPELINE_LOG_KEY=? and EVENT_RECORD_NO=?";

        Pipeline pipeline = new Pipeline();
        PipelineLog plog = PipelineLog.getTempInstance(pipeline);
        PipelineLogPropQuery q = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        return execute(q, pipelineLogKey, recNo);
    }

    /**
     * load from given pipeline log a list of log-props for records with given log-prop-key
     * @param plog - pipeline-log object to be searched for properties
     * @param propType - filter by prop-type
     * @param pageNr - 0-based page nr to return paged results
     * @param pageSize - number of records per page (if 0, default value of 25 will be used)
     * @return list of LogProp objects
     */
    public List<PipelineLog.LogProp> getRecords(PipelineLog plog, String propType, int pageNr, int pageSize) throws Exception {

        // sanity check
        if( plog==null )
            return null;
        if( pageNr < 0 )
            pageNr = 0;
        if( pageSize <= 0 )
            pageSize = 25;

        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML from PIPELINE_LOG_PROPERTIES ";
        query += "where (PIPELINE_LOG_KEY,EVENT_RECORD_NO) in(";
        query += "select PIPELINE_LOG_KEY,EVENT_RECORD_NO from(";
        query += "select PIPELINE_LOG_KEY,EVENT_RECORD_NO,ROWNUM rn from PIPELINE_LOG_PROPERTIES where PIPELINE_LOG_KEY=? and EVENT_RECORD_NO>0 and PIPELINE_LOG_PROPERTYTYPES_KEY=?)";
        query += "where rn between ? AND ?)";

        PipelineLogPropQuery pq = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        return execute(pq, plog.getPipelineLogKey(),propType,1+pageNr*pageSize,(1+pageNr)*pageSize);
    }

    /**
     * load from given pipeline log list of log-props for records with log-prop-key having specified value
     * @param plog - pipeline-log object to be searched for properties
     * @param propType - filter by prop-type
     * @param propValue - filter by prop-value
     * @param pageNr - 0-based page nr to return paged results
     * @param pageSize - number of records per page (if 0, default value of 25 will be used)
     * @return list of LogProp objects
     */
    public List<PipelineLog.LogProp> getRecords(PipelineLog plog, String propType, String propValue, int pageNr, int pageSize) throws Exception {

        // sanity check
        if( plog==null )
            return null;
        if( pageNr < 0 )
            pageNr = 0;
        if( pageSize <= 0 )
            pageSize = 25;

        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML from PIPELINE_LOG_PROPERTIES ";
        query += "where (PIPELINE_LOG_KEY,EVENT_RECORD_NO) in(";
        query += "select PIPELINE_LOG_KEY,EVENT_RECORD_NO from(";
        query += "select PIPELINE_LOG_KEY,EVENT_RECORD_NO,ROWNUM rn from PIPELINE_LOG_PROPERTIES where PIPELINE_LOG_KEY=? and EVENT_RECORD_NO>0 and PIPELINE_LOG_PROPERTYTYPES_KEY=? AND EVENT_VALUE=?)";
        query += "where rn between ? AND ?)";

        PipelineLogPropQuery pq = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        return execute(pq, plog.getPipelineLogKey(),propType,propValue,1+pageNr*pageSize,(1+pageNr)*pageSize);
    }

    /**
     * load from given pipeline log list of log-props for records having given flag assigned in table PIPELINE_LOG_FLAGS
     * @param plog - pipeline-log object to be searched for properties
     * @param flag - filter by flag symbol -- must not be null
     * @param pageNr - 0-based page nr to return paged results
     * @param pageSize - number of records per page (if 0, default value of 25 will be used)
     * @return list of LogProp objects
     */
    public List<PipelineLog.LogProp> getRecordsForFlag(PipelineLog plog, String flag, int pageNr, int pageSize) throws Exception {

        // sanity check
        if( plog==null )
            return null;
        if( pageNr < 0 )
            pageNr = 0;
        if( pageSize <= 0 )
            pageSize = 25;

        String query = "select PIPELINE_LOG_PROPERTY_KEY,PIPELINE_LOG_KEY,PIPELINE_LOG_PROPERTYTYPES_KEY,EVENT_NOTES,EVENT_VALUE,EVENT_RECORD_NO,EVENT_TIMESTAMP,EVENT_XML from PIPELINE_LOG_PROPERTIES "+
            "where (PIPELINE_LOG_KEY,EVENT_RECORD_NO) in("+
            "select PIPELINE_LOG_KEY,EVENT_RECORD_NO from("+
            "SELECT pipeline_log_key,pipeline_log_record_no event_record_no,ROWNUM rn FROM pipeline_log_flags l,pipeline_flags f WHERE pipeline_log_key=? and pipeline_flag_symbol=? and f.pipeline_flag_id=l.pipeline_flag_id)"+
            "where rn between ? AND ?)";

        PipelineLogPropQuery q = new PipelineLogPropQuery(this.getDataSource(), query, plog);
        return execute(q, plog.getPipelineLogKey(), flag, 1+pageNr*pageSize, (1+pageNr)*pageSize);
    }

    // return map of how often given EVENT_VALUE of given prop_key occurs in the log
    // (only rows with EVENT_RECNO > 0 are counted)
    public HashMap<String, Integer> getRecordSummary(PipelineLog plog, String logPropKey) throws Exception {

        String query = "select EVENT_VALUE,COUNT(*) CNT from PIPELINE_LOG_PROPERTIES where PIPELINE_LOG_KEY=? and PIPELINE_LOG_PROPERTYTYPES_KEY=? and EVENT_RECORD_NO>0 group by EVENT_VALUE";

        LogPropStatQuery pq = new LogPropStatQuery(this.getDataSource(), query);
        List<ArrayList> logProps = execute(pq, plog.getPipelineLogKey(), logPropKey);
        if( logProps==null )
            return null;

        HashMap<String,Integer> freqMap = new HashMap<>();
        for( ArrayList list: logProps ) {
            freqMap.put((String)list.get(0), (Integer)list.get(1));
        }
        return freqMap;
    }

    // start a given pipeline running; pipeline-log associated with the pipeline is returned
    public PipelineLog startPipeline(Pipeline pipeline, String runMode) throws Exception  {

        // first initialize the pipeline key
        if( pipeline.getPipelineKey()==0 ) {
            Pipeline pipeline2 = getPipelineKey(pipeline.getName(), pipeline.getSpeciesTypeKey());
            if( pipeline2==null ) {
                pipeline.setPipelineKey(insertPipeline(pipeline.getName(), pipeline.getSpeciesTypeKey()));
            } else {
                pipeline.setPipelineKey(pipeline2.getPipelineKey());
            }
        }

        // get new pipeline_log_key from sequence
        int pipelineLogKey = getNextKeyFromSequence("PIPELINE_LOGS_SEQ");

        // start new pipeline log
        String query = "INSERT INTO pipeline_logs (PIPELINE_KEY, PIPELINE_LOG_KEY, RUN_START_TIME, RUN_END_TIME, SUCCESS_IND, RUN_MODE) VALUES(?,?,SYSDATE,null,'N',?)";
        update(query, pipeline.getPipelineKey(), pipelineLogKey, runMode);
        
        // create pipeline log object
        PipelineLog pipelineLog = PipelineLog.getInstance(pipeline);
        pipelineLog.setPipelineLogKey(pipelineLogKey);
        pipelineLog.setRunMode(runMode);
        return pipelineLog;
    }

    // stops a given pipeline
    public void stopPipeline(PipelineLog log, boolean success) throws Exception  {

        // update pipeline log setting RUN_END_TIME and STATUS
        String query = "update PIPELINE_LOGS set RUN_END_TIME=SYSDATE, SUCCESS_IND=? where PIPELINE_LOG_KEY=?";
        update(query, success?"Y":"N", log.getPipelineLogKey());

        // since pipeline log is stopped, clear the log key
        log.setPipelineLogKey(0);
    }

    // msgtype parameter must be one of PipelineLog.LOGPROP_xxx
    public void logDbMessage(String message, String msgtype, PipelineLog log) throws Exception{

        logDbMessage(message, null, msgtype, null, log);
    }

    // msgtype parameter must be one of PipelineLog.LOGPROP_xxx
    // both meaaseg and value cannot be longer than 4000
    public void logDbMessage(String message, String value, String msgtype, PipelineLog log) throws Exception{

        logDbMessage(message, value, msgtype, null, log);
    }

    // msgtype parameter must be one of PipelineLog.LOGPROP_xxx
    // both meaaseg and value cannot be longer than 4000
    public void logDbMessage(String message, String value, String msgtype, String clob, PipelineLog log) throws Exception{

        // insert a message of given type into pipeline_log
        String query = "insert into PIPELINE_LOG_PROPERTIES "+
                "(PIPELINE_LOG_KEY, PIPELINE_LOG_PROPERTYTYPES_KEY, EVENT_TIMESTAMP, EVENT_NOTES, EVENT_VALUE, EVENT_XML, PIPELINE_LOG_PROPERTY_KEY) "+
                "VALUES(?,?,SYSDATE,?,?,?,PIPELINE_LOG_PROPERTIES_SEQ.NEXTVAL)";
        update(query, log.getPipelineLogKey(), msgtype, message, value, clob);
    }

    public void writeLogProps(int pipelineLogKey, List<PipelineLog.LogProp> logProps) throws Exception {

        writeLogProps(pipelineLogKey, logProps, 0);
    }

    public void writeLogProps(int pipelineLogKey, List<PipelineLog.LogProp> logProps, int recno) throws Exception {

        String query = "insert into PIPELINE_LOG_PROPERTIES "+
                "(PIPELINE_LOG_KEY, PIPELINE_LOG_PROPERTYTYPES_KEY, EVENT_TIMESTAMP, EVENT_NOTES, EVENT_VALUE,"+
                " EVENT_RECORD_NO, EVENT_XML, PIPELINE_LOG_PROPERTY_KEY) "+
                "VALUES(?,?,?,?,?,?,?,PIPELINE_LOG_PROPERTIES_SEQ.NEXTVAL)";

        BatchSqlUpdate su = new BatchSqlUpdate(getDataSource(), query,
                new int[]{Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR,
                        Types.INTEGER, Types.VARCHAR});
        su.compile();

        for( PipelineLog.LogProp logProp: logProps ) {
            // skip log props belonging to different records
            if( recno>0 && logProp.getRecNo()!=recno )
                continue;

            // info field cannot be longer than 4000 characters!
            String info = logProp.getInfo();
            if( info!=null && info.length()>4000 )
                info = info.substring(0, 4000);

            // value field cannot be longer than 4000 characters!
            String value = logProp.getValue();
            if( value!=null && value.length()>4000 )
                value = value.substring(0, 4000);

            su.update(pipelineLogKey, logProp.getKey(), logProp.getDate(), info, value, logProp.getRecNo(), logProp.getXml());
        }

        executeBatch(su);
    }

    /** write a single logprop to database */
    public void writeLogProp(int pipelineLogKey, PipelineLog.LogProp logProp) throws Exception {

        String query = "insert into PIPELINE_LOG_PROPERTIES "+
                "(PIPELINE_LOG_KEY, PIPELINE_LOG_PROPERTYTYPES_KEY, EVENT_TIMESTAMP, EVENT_NOTES, EVENT_VALUE,"+
                " EVENT_RECORD_NO, EVENT_XML, PIPELINE_LOG_PROPERTY_KEY) "+
                "VALUES(?,?,?,?,?,?,?,PIPELINE_LOG_PROPERTIES_SEQ.NEXTVAL)";

        // info field cannot be longer than 4000 characters!
        String info = logProp.getInfo();
        if( info!=null && info.length()>4000 )
            info = info.substring(0, 4000);

        // value field cannot be longer than 4000 characters!
        String value = logProp.getValue();
        if( value!=null && value.length()>4000 )
            value = value.substring(0, 4000);

        update(query, pipelineLogKey, logProp.getKey(), logProp.getDate(), info, value, logProp.getRecNo(), logProp.getXml());
    }

    class LogPropStatQuery extends MappingSqlQuery {

        public LogPropStatQuery(DataSource ds, String query) {
            super(ds, query);
        }

        protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArrayList list = new ArrayList();
            list.add(rs.getString("EVENT_VALUE"));
            list.add(new Integer(rs.getInt("CNT")));
            return list;
        }
    }

}
