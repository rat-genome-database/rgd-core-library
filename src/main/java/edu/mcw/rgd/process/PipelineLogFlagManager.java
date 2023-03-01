package edu.mcw.rgd.process;

import edu.mcw.rgd.dao.impl.PipelineLogFlagDAO;
import edu.mcw.rgd.datamodel.pipeline.PipelineFlag;
import edu.mcw.rgd.datamodel.pipeline.PipelineLogFlag;

import java.util.*;

/**
 * @author mtutaj
 * @since 9/12/11
 * Manages reading and writing of pipeline flags. This implementation is suitable to work with one pipeline log
 * setup in the constructor.
 * <p>
 * To start working with pipeline log flags, the application first to have register the all flags
 * being used by the pipeline. The flags will be identified by the symbol.
 * <p>
 * To minimize the count of database calls, all pipeline flags are accumulated in the memory for one pipeline record,
 * and they are flushed into the database once the record processing is complete.
 */
public class PipelineLogFlagManager {

    private PipelineLogFlagDAO dao = new PipelineLogFlagDAO();
    private PipelineLogger pipelineLogger;

    // list of all flags defined for the pipeline
    private HashMap<String, PipelineFlag> flagCache = new HashMap<String, PipelineFlag>();
    // map of flag ids to flag objects
    private HashMap<Integer, PipelineFlag> flagMap = new HashMap<Integer, PipelineFlag>();

    // set of flags defined for the current pipeline record
    private HashSet<PipelineLogFlag> recordFlags = new HashSet<PipelineLogFlag>();

    public PipelineLogFlagManager(PipelineLogger logger) {
        this.pipelineLogger = logger;
    }

    /**
     * register a flag for this pipeline;
     * if there is no flag with such a symbol for this pipeline, it will be created in PIPELINE_FLAGS table;
     * if there is a flag with such a symbol for this pipeline, its definition will be updated if necessary
     * @param symbol pipeline flag symbol
     * @param description pipeline flag description
     * @return PipelineFlag object
     */
    synchronized public PipelineFlag registerFlag(String symbol, String description) throws Exception {

        // read pipeline flag from database
        PipelineFlag pflag = dao.getFlag(getPipelineName(), symbol);
        if( pflag==null ) {
            // there is no such flag in database -- create a new one
            pflag = dao.insertFlag(getPipelineName(), symbol, description);
        }
        else {
            // there is a flag with such a symbol for this pipeline
            // check if flag description is the same -- if it is different, update it
            if( !Utils.stringsAreEqual(description, pflag.getDescription()) ) {
                pflag.setDescription(description);
                dao.updateFlag(pflag);
            }
        }

        //  add the registered flag to the flag cache
        flagCache.put(pflag.getSymbol(), pflag);
        flagMap.put(pflag.getId(), pflag);

        return pflag;
    }

    /**
     * set a pipeline flag for given pipeline record
     * @param symbol flag symbol
     * @param recNo pipeline record no
     * @return true if the flag was added to record flags; false if it was already set
     */
    synchronized public boolean setFlag(String symbol, int recNo) throws Exception {

        // find the flag in the flag cache
        PipelineFlag pflag = flagCache.get(symbol);
        if( pflag==null )
            throw new Exception("Attempt to add unregistered flag "+symbol+" to record flags");

        // create an object to be added to record flag pool
        PipelineLogFlag plFlag = new PipelineLogFlag();
        plFlag.setPipelineFlagId(pflag.getId());
        plFlag.setPipelineLogRecNo(recNo);
        plFlag.setPipelineLogKey(pipelineLogger.getPipelineLog().getPipelineLogKey());

        return recordFlags.add(plFlag);
    }

    /**
     * check if a given pipeline flag is set for given pipeline record
     * @param symbol flag symbol
     * @param recNo pipeline record no
     * @return true if the flag was added to record flags; false otherwise
     */
    synchronized public boolean isFlagSet(String symbol, int recNo) throws Exception {

        // find the flag in the flag cache
        PipelineFlag pflag = flagCache.get(symbol);
        if( pflag==null )
            throw new Exception("Attempt to check unregistered flag "+symbol);

        // lookup for our flag in the flag pool for current record
        for( PipelineLogFlag plFlag: recordFlags ) {
            if( plFlag.getPipelineFlagId()==pflag.getId() && plFlag.getPipelineLogRecNo()==recNo )
                return true;
        }
        return false;
    }

    /**
     * write pipeline log flags for given pipeline record;
     * internal record flag cache is cleared after writing;
     * list of flags written is returned
     * @param recNo record nr for which the pipeline flags are written and then removed from cache
     * @return Collection<String> set of all flags written and removed from flag cache
     * @throws Exception
     */
    synchronized public Collection<String> writeFlags(int recNo) throws Exception {

        // create a copy of flags for given record
        List<PipelineLogFlag> flags = new ArrayList<PipelineLogFlag>();
        Iterator<PipelineLogFlag> it = recordFlags.iterator();
        while( it.hasNext() ) {
            PipelineLogFlag pflag = it.next();
            if( pflag.getPipelineLogRecNo()==recNo ) {
                flags.add(pflag);
                it.remove();
            }
        }

        // write all flags for given record to database
        dao.writeFlags(flags);

        // crate a list of flags
        List<String> flagNames = new ArrayList<String>(flags.size());
        for( PipelineLogFlag pflag: flags ) {
            flagNames.add(flagMap.get(pflag.getPipelineFlagId()).getSymbol());
        }
        return flagNames;
    }

    private String getPipelineName() {
        return pipelineLogger.getPipelineLog().getPipeline().getName();
    }
}
