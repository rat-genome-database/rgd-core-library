package edu.mcw.rgd.process;

import edu.mcw.rgd.dao.impl.PipelineLogDAO;
import edu.mcw.rgd.datamodel.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 9, 2010
 * Time: 10:26:32 AM
 * singleton wrapper to handle all database logging to EntrezGene pipeline
 */
public class PipelineLogger {

    // predefined pipeline names, as found in database
    public static final String PIPELINE_ENTREZGENE = "EntrezGene";
    public static final String PIPELINE_IPI = "IPI"; // retired in 2010
    public static final String PIPELINE_UNIPROT = "UniProtKB"; // started in 2010


    // pipeline progress message - severity level DEBUG (used for debugging purposes)
    public static final String DEBUG = PipelineLog.LOGPROP_DEBUGMESSAGE;
    // regular pipeline progress message - severity level INFORMATIONAL
    public static final String INFO = PipelineLog.LOGPROP_INFOMESSAGE;
    // pipeline progress message - severity level WARNING (unexpected event found, but pipeline will continue running)
    public static final String WARN = PipelineLog.LOGPROP_WARNMESSAGE;
    // pipeline progress message - severity level ERROR (unexpected event found -- pipeline will be aborted)
    public static final String ERROR = PipelineLog.LOGPROP_ERRORMESSAGE;
    // pipeline total counter: EVENT_INFO - total counter name, EVENT_VALUE: total counter value
    public static final String TOTAL = PipelineLog.LOGPROP_TOTAL;

    // pipeline record property - external id (XDB_KEY-XDB_NAME-ACC_ID) as read from source record
    public static final String REC_XDBID = PipelineLog.LOGPROP_REC_XDBID;
    // pipeline record property - rgd id as read from source record
    public static final String REC_RGDID = PipelineLog.LOGPROP_REC_RGDID;
    // pipeline record property - rgd id as matched with rgd
    public static final String REC_MATCHINGRGDID = PipelineLog.LOGPROP_REC_MATCHINGRGDID;
    // pipeline record property - post-processing flag
    public static final String REC_FLAG = PipelineLog.LOGPROP_REC_FLAG;
    // pipeline record property - post-processing flag
    public static final String REC_XML = PipelineLog.LOGPROP_REC_XML;
    // pipeline record property - db action
    public static final String REC_DBACTION = PipelineLog.LOGPROP_REC_DBACTION;
    // pipeline record property - db log
    public static final String REC_DBLOG = PipelineLog.LOGPROP_REC_DBLOG;


    PipelineLog pipelineLog = null;
    PipelineLogDAO pipelineLogDAO = new PipelineLogDAO();
    boolean isStarted = false; // true if pipeline is started
    boolean isStopped = false; // true if pipeline is stopped

    public static PipelineLogger getInstance() {
        return _theLogger;
    }
    static PipelineLogger _theLogger = new PipelineLogger();
    private PipelineLogger() {
    }

    public boolean init(int speciesTypeKey, String runMode, String pipelineName) throws Exception {
        if( isStopped )
            return false; // out-of-context: pipeline is already stopped
        if( isStarted )
            return true; // pipeline is already running -- multiple init calls allowed

        Pipeline pipeline = new Pipeline();
        pipeline.setName(pipelineName);
        pipeline.setSpeciesTypeKey(speciesTypeKey);

        pipelineLog = pipelineLogDAO.startPipeline(pipeline, runMode);

        // log the pipeline name and species into db log
        this.log("pipeline "+pipelineLog.getPipeline().getFullName()+" started!", PipelineLogger.INFO);
        isStarted = true;
        return true;
    }

    // close the pipeline
    public boolean close(boolean success) throws Exception {
        if( isStopped )
            return false; // out-of-context: pipeline is already stopped
        if( !isStarted )
            return false; // cannot stop a pipeline that has not been started

        pipelineLogDAO.stopPipeline(pipelineLog, success);
        isStarted = false;
        isStopped = true;
        return true;
    }

    // restart the stopped pipeline in different mode
    public boolean restart(String runMode) throws Exception {
        // pipeline must be already stopped
        if( !isStopped || isStarted )
            return false; // cannot restart a running pipeline
        isStopped = false;

        Pipeline pipeline = pipelineLog.getPipeline();
        pipelineLog = pipelineLogDAO.startPipeline(pipeline, runMode);

        // log the pipeline name and species into db log
        this.log("pipeline "+pipelineLog.getPipeline().getFullName()+" started!", PipelineLogger.INFO);
        isStarted = true;
        return true;
    }

    public void log(String message, String msgtype) throws Exception {
        log(message, null, msgtype);
    }

    public void log(String message, String value, String msgtype) throws Exception {
        pipelineLogDAO.logDbMessage(message, value, msgtype, this.pipelineLog);
    }

    public String getPipelineFullName() {
        return pipelineLog.getPipeline().getFullName();
    }
    public String getPipelineRunMode() {
        return pipelineLog.getRunMode();
    }

    // return the date string formatted as yyyy/mm/dd, or null if not found in database
    public String getLastDataSyncDate() throws Exception {

        // is the variable already loaded?
        PipelineLog.LogProp logProp = pipelineLog.getLogProp(PipelineLog.LOGPROP_DATASYNCDATE);
        if( logProp!=null )
            return logProp.getValue(); // already loaded -- use that value
        // variable not loaded

        // get most recent successful pipleline log
        PipelineLog plog = pipelineLogDAO.getLastPipelineLog(
                pipelineLog.getPipeline().getName(),
                pipelineLog.getPipeline().getSpeciesTypeKey(),
                pipelineLog.getRunMode(), "Y"); // pipeline_outcome.success

        // and load SYNCDATE property from it
        List<PipelineLog.LogProp> logProps = pipelineLogDAO.getLogProps(plog, PipelineLog.LOGPROP_DATASYNCDATE, -1);
        if( logProps==null || logProps.size()==0 ) {
            return "2000/01/01"; // no SYNCDATE in the table -- default to '2000/01/01'
        }
        logProp = logProps.get(0);
        return logProp.getValue();
    }

    public void setLastDataSyncDate(String date) throws Exception {

        // is the variable already loaded?
        PipelineLog.LogProp logProp = pipelineLog.getLogProp(PipelineLog.LOGPROP_DATASYNCDATE);
        if( logProp!=null ) {
            logProp.setValue(date);
            return;
        }

        // update the property in property collection
        logProp = pipelineLog.createLogProp();
        logProp.setKey(PipelineLog.LOGPROP_DATASYNCDATE);
        logProp.setValue(date);
        logProp.setInfo("last-data-sync-date");
        logProp.setDate(new Date());
        pipelineLog.addLogProp(logProp);
    }

    public synchronized void removeAllLogProps() {
        pipelineLog.removeAllLogProps();
    }

    public synchronized void removeAllLogProps(int recno) {
        pipelineLog.removeAllLogProps(recno);
    }

    public synchronized void addLogProp(String info, String value, int recNo, String logPropKey) {

        PipelineLog.LogProp logProp = pipelineLog.createLogProp();
        logProp.setKey(logPropKey);
        logProp.setValue(value);
        logProp.setInfo(info);
        logProp.setDate(new Date());
        logProp.setRecNo(recNo);

        pipelineLog.addLogProp(logProp);
    }

    public synchronized void addLogProp(String info, String value, int recNo, String logPropKey, String xml) {

        PipelineLog.LogProp logProp = pipelineLog.createLogProp();
        logProp.setKey(logPropKey);
        logProp.setValue(value);
        logProp.setInfo(info);
        logProp.setDate(new Date());
        logProp.setRecNo(recNo);
        logProp.setXml(xml);

        pipelineLog.addLogProp(logProp);
    }

    public synchronized void writeLogProps() throws Exception {
        pipelineLogDAO.writeLogProps(pipelineLog.getPipelineLogKey(), pipelineLog.getLogProps());
    }

    public synchronized void writeLogProps(int recno) throws Exception {
        pipelineLogDAO.writeLogProps(pipelineLog.getPipelineLogKey(), pipelineLog.getLogProps(), recno);
    }

    public PipelineLog getPipelineLog() {
        return this.pipelineLog;
    }

    public PipelineLogDAO getPipelineLogDAO() {
        return this.pipelineLogDAO;
    }
}
