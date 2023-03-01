package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.PipelineFlagQuery;
import edu.mcw.rgd.datamodel.pipeline.PipelineFlag;
import edu.mcw.rgd.datamodel.pipeline.PipelineLogFlag;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 9/12/11 <br>
 * Time: 3:50 PM <br>
 * <p/>
 * general API to handle data in PIPELINE_FLAGS and PIPELINE_LOG_FLAGS tables
 */
public class PipelineLogFlagDAO extends AbstractDAO {


    /**
     * get pipeline flag given flag symbol and the pipeline name
     * @param pipeline pipeline name
     * @param flagSymbol flag symbol
     * @return PipelineFlag object or null if no match
     * @throws Exception
     */
    public PipelineFlag getFlag(String pipeline, String flagSymbol) throws Exception {

        String query = "SELECT * FROM pipeline_flags WHERE pipeline_name=? AND pipeline_flag_symbol=?";
        PipelineFlagQuery q = new PipelineFlagQuery(this.getDataSource(), query);

        List<PipelineFlag> flags = execute(q, pipeline, flagSymbol);
        if( flags.isEmpty() )
            return null;
        if( flags.size()>1 )
            throw new Exception("Unexpected: multiple flags for pipeline="+pipeline+", flag_symbol="+flagSymbol);
        return flags.get(0);

    }

    /**
     * insert a new pipeline flag -- pipeline_flag_id is assigned from sequence PIPELINE_FLAGS_SEQ
     * @param pipeline pipeline name
     * @param flagSymbol flag symbol
     * @param flagDescription flag description
     * @return PipelineFlag object
     * @throws Exception
     */
    public PipelineFlag insertFlag(String pipeline, String flagSymbol, String flagDescription) throws Exception {

        PipelineFlag pflag = new PipelineFlag();
        pflag.setId(this.getNextKeyFromSequence("PIPELINE_FLAGS_SEQ"));
        pflag.setDescription(flagDescription);
        pflag.setSymbol(flagSymbol);
        pflag.setPipeline(pipeline);

        String sql = "INSERT INTO pipeline_flags "
            +"(pipeline_flag_id, pipeline_flag_symbol, pipeline_flag_description, pipeline_name) VALUES(?,?,?,?)";

        update(sql, pflag.getId(), pflag.getSymbol(), pflag.getDescription(), pflag.getPipeline());

        return pflag;
    }

    /**
     * update description for pipeline flag
     * @param pflag PipelineFlag object with set pipeline_flag_Id
     * @return count of rows affected
     * @throws Exception
     */
    public int updateFlag(PipelineFlag pflag) throws Exception {

        String sql = "UPDATE pipeline_flags SET pipeline_flag_description=? WHERE pipeline_flag_id=?";
        return update(sql, pflag.getDescription(), pflag.getId());
    }

    /**
     * write a bunch of pipeline log flags into a database table PIPELINE_LOG_FLAGS in one transaction
     *
     * @param flags a collection of pipeline log flags
     * @return count of rows affected
     * @throws Exception
     */
    public int writeFlags(Collection<PipelineLogFlag> flags) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO pipeline_log_flags (pipeline_log_key, pipeline_flag_id, pipeline_log_record_no) VALUES(?,?,?)",
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});
        su.compile();

        for( PipelineLogFlag flag: flags ) {
            su.update(flag.getPipelineLogKey(), flag.getPipelineFlagId(), flag.getPipelineLogRecNo());
        }

        return executeBatch(su);
    }

    /**
     * get frequencies of how often given pipeline log flag occurs in the pipeline log
     * @param pipelineLogKey pipeline log key
     * @return get map of frequencies given pipeline log flag occurs in the pipeline log
     * @throws Exception
     */
    public HashMap<String, Integer> getFlagSummary(int pipelineLogKey) throws Exception {

        final HashMap<String,Integer> freqMap = new HashMap<String,Integer>();
        String query =
            "SELECT pipeline_flag_symbol,cnt FROM pipeline_flags f,(\n"+
            "  SELECT pipeline_flag_id,COUNT(*) cnt\n"+
            "  FROM pipeline_log_flags\n"+
            "  WHERE pipeline_log_key=?\n"+
            "  GROUP BY pipeline_flag_id\n"+
            ")x\n"+
            "WHERE x.pipeline_flag_id=f.pipeline_flag_id\n";

        MappingSqlQuery msq = new MappingSqlQuery(this.getDataSource(), query){
            @Override
            protected Object mapRow(ResultSet rs, int i) throws SQLException {
                freqMap.put(rs.getString(1), rs.getInt(2));
                return null;
            }
        };
        execute(msq, pipelineLogKey);

        return freqMap;
    }

    /**
     * load from given pipeline log a list of flags for given record
     * @param pipelineLogKey - pipeline log key
     * @param recNo - record number (as found in the pipeline log)
     * @param flagSymbol - flag symbol; if empty, all flags are returned
     * @return list of PipelineFlag objects (could be empty)
     */
    public List<PipelineFlag> getFlagsForRecord(int pipelineLogKey, int recNo, String flagSymbol) throws Exception {

        // sanity check
        if( pipelineLogKey==0 )
            return null;

        // if flagSymbol is not given, any flag will match
        if( flagSymbol==null ) {
            String query = "SELECT * FROM pipeline_log_flags l,pipeline_flags f "+
                    "WHERE pipeline_log_key=? AND pipeline_log_record_no=? AND l.pipeline_flag_id=f.pipeline_flag_id";
            PipelineFlagQuery pq = new PipelineFlagQuery(this.getDataSource(), query);

            return execute(pq, pipelineLogKey, recNo);
        }
        else {
            // flag symbol is given
            String query = "SELECT * FROM pipeline_log_flags l,pipeline_flags f "+
                    "WHERE pipeline_log_key=? AND pipeline_log_record_no=? AND pipeline_flag_symbol=? AND l.pipeline_flag_id=f.pipeline_flag_id";
            PipelineFlagQuery pq = new PipelineFlagQuery(this.getDataSource(), query);

            return execute(pq, pipelineLogKey, recNo, flagSymbol);
        }
    }

}




/*
CREATE
    TABLE PIPELINE_FLAGS
    (
        PIPELINE_FLAG_ID NUMBER(10),
        PIPELINE_FLAG_SYMBOL VARCHAR2(40),
        PIPELINE_FLAG_DESCRIPTION VARCHAR2(1000),
        PIPELINE_NAME VARCHAR2(80)
    );

CREATE
    TABLE PIPELINE_LOG_FLAGS
    (
        PIPELINE_LOG_KEY NUMBER NOT NULL,
        PIPELINE_FLAG_ID NUMBER(10) NOT NULL,
        PIPELINE_LOG_RECORD_NO NUMBER NOT NULL
    );

create sequence pipeline_flags_seq nocache;
*/