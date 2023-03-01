package edu.mcw.rgd.datamodel;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Feb 19, 2010
 * Time: 3:55:28 PM
 *
 * pipeline logs consists of a number of entries, called log props (log properties);
 * there is a finite number of pipelines;
 * there is a finite number of log props assigned to pipelines;
 * this class provides a set of utility functions to retrieve and insert log props
 */
public class PipelineLog {

    // pipeline log props
    //
    // this log prop value is a date string;
    // RGD database with dates of up to 'DATASYNCDATE' (including) should be in sync
    // from data from external data source, like NCBI Gene;
    // example of this prop value: '2010/01/01'
    public static final String LOGPROP_DATASYNCDATE = "SYNCDATE";
    // date range specific to the pipeline when it is run
    public static final String LOGPROP_DATERANGE = "DATERANGE";
    // pipeline progress message - severity level DEBUG (used for debugging purposes)
    public static final String LOGPROP_DEBUGMESSAGE = "DEBUG";
    // regular pipeline progress message - severity level INFORMATIONAL
    public static final String LOGPROP_INFOMESSAGE = "INFO";
    // pipeline progress message - severity level WARNING (unexpected event found, but pipeline will continue running)
    public static final String LOGPROP_WARNMESSAGE = "WARN";
    // pipeline progress message - severity level ERROR (unexpected event found -- pipeline will be aborted)
    public static final String LOGPROP_ERRORMESSAGE = "ERROR";
    // pipeline total counter: EVENT_INFO - total counter name, EVENT_VALUE: total counter value
    public static final String LOGPROP_TOTAL = "TOTAL";
    // pipeline record property - external id (XDB_KEY-XDB_NAME-ACC_ID) as read from source record
    public static final String LOGPROP_REC_XDBID = "XDBID";
    // pipeline record property - rgd id as read from source record
    public static final String LOGPROP_REC_RGDID = "RGDID";
    // pipeline record property - rgd id as matched to rgd
    public static final String LOGPROP_REC_MATCHINGRGDID = "MATCHINGRGDID";
    // pipeline record property - post-processing flag
    public static final String LOGPROP_REC_FLAG = "FLAG";
    // pipeline property - xml representation of the record
    public static final String LOGPROP_REC_XML = "RECXML";
    // pipeline property - db log
    public static final String LOGPROP_REC_DBLOG = "RECLOG";
    // pipeline property - db action
    public static final String LOGPROP_REC_DBACTION = "DBACTION";
    // pipeline property - rec count in data source
    public static final String LOGPROP_RECCOUNT = "RECCOUNT";

    // unique pipeline key
    private Pipeline pipeline;

    // unique pipeline log key: created when pipeline starts
    private int pipelineLogKey;

    // run_mode: additional mode in which the pipeline is run, specific to the pipeline
    private String runMode;

    private java.util.Date runStartTime;
    private java.util.Date runStopTime;
    private String success;

    // list of logprops for the current record
    List<LogProp> logProps = new ArrayList<LogProp>();

    // remove all log props -- return nr of removed log props
    public int removeAllLogProps() {
        int removeCount = logProps.size();
        logProps.clear();
        return removeCount;
    }

    // remove all log props -- return nr of removed log props
    public int removeAllLogProps(int recno) {
        Iterator<LogProp> it = logProps.listIterator();
        int removeCount = 0;
        while( it.hasNext() ) {
            LogProp logProp = it.next();
            if( logProp.getRecNo()==recno ) {
                removeCount++;
                it.remove();
            }
        }
        return removeCount;
    }

    // add new log prop to the collection
    public void addLogProp(LogProp logProp) {

        this.logProps.add(logProp);
    }

    // add new log prop collection to the collection
    public void addLogProps(List<LogProp> logProps) {

        this.logProps.addAll(logProps);
    }

    // get first occurence of a given log prop of given type
    public LogProp getLogProp(String logPropKey) {

        for( LogProp logProp: this.logProps ) {
            if( logProp.getKey().equals(logPropKey) )
                return logProp;
        }
        return null;
    }

    // get all log props of given type
    public List<LogProp> getLogProps(String logPropKey) {

        ArrayList<LogProp> logProps = new ArrayList<LogProp>();
        for( LogProp logProp: this.logProps ) {
            if( logProp.getKey().equals(logPropKey) )
                logProps.add(logProp);
        }
        return logProps;
    }

    // sort all log properties by value
    public void sortLogPropsByValue() {
        Collections.sort(this.logProps, new Comparator<LogProp>(){
            public int compare(LogProp o1, LogProp o2) {
                String val1 = o1.getValue()!=null ? o1.getValue() : "";
                String val2 = o2.getValue()!=null ? o2.getValue() : "";
                return val1.compareToIgnoreCase(val2);
            }
        });
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public int getPipelineLogKey() {
        return pipelineLogKey;
    }

    public void setPipelineLogKey(int pipelineLogKey) {
        this.pipelineLogKey = pipelineLogKey;
    }

    public List<LogProp> getLogProps() {
        return logProps;
    }

    public void setLogProps(List<LogProp> logProps) {
        this.logProps = logProps;
    }


    // internal class
    public class LogProp {
        private int id; // EVENT_KEY
        private int pipelineLogKey; // PIPELINE_LOG_KEY
        private String key; // PIPELINE_LOG_PROP_KEY
        private String info; // EVENT_INFO
        private String value; // EVENT_VALUE
        private int recNo; // EVENT_RECNO
        private java.util.Date date; // EVENT_TIME
        private String xml; // EVENT_XML - CLOB

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getRecNo() {
            return recNo;
        }

        public void setRecNo(int recNo) {
            this.recNo = recNo;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getXml() {
            return xml;
        }

        public void setXml(String xml) {
            this.xml = xml;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPipelineLogKey() {
            return pipelineLogKey;
        }

        public void setPipelineLogKey(int pipelineLogKey) {
            this.pipelineLogKey = pipelineLogKey;
        }
    }

    public LogProp createLogProp() {
        return new LogProp();
    }

    // singleton code for handling a pool of singleton PipelineLog objects
    private static HashMap<String, PipelineLog> _ourInstances = new HashMap<String, PipelineLog>();
    public static PipelineLog getInstance(Pipeline pipeline) {

        // create map key: concatenation of pipeline-name and species-name
        String key = pipeline.getName()+pipeline.getSpecies();

        // access the pipeline log
        PipelineLog log = _ourInstances.get(key);
        if( log==null ) {
            _ourInstances.put(key, new PipelineLog(pipeline));
            log = _ourInstances.get(key);
        }
        return log;
    }
    // get temporary instance of pipeline log -- not a singleton!
    public static PipelineLog getTempInstance(Pipeline pipeline) {
        return new PipelineLog(pipeline);
    }
    private PipelineLog() {
        setPipeline(null);
    }
    // initialize with given pipeline nr
    private PipelineLog(Pipeline pipeline) {
        setPipeline(pipeline);
    }

    public String getRunMode() {
        return runMode;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public Date getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(Date runStartTime) {
        this.runStartTime = runStartTime;
    }

    public Date getRunStopTime() {
        return runStopTime;
    }

    public void setRunStopTime(Date runStopTime) {
        this.runStopTime = runStopTime;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
