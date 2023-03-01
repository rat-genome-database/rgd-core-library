package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Pipeline;
import edu.mcw.rgd.datamodel.PipelineLog;
import edu.mcw.rgd.datamodel.SpeciesType;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 18, 2010
 * Time: 11:57:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineLogQuery extends MappingSqlQuery {

    Pipeline pipeline;

    public PipelineLogQuery(DataSource ds, String query, Pipeline pipeline) {
        super(ds, query);
        this.pipeline = pipeline;
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PipelineLog log = PipelineLog.getTempInstance(this.pipeline);

        int pipelineKey = rs.getInt("PIPELINE_KEY");
        if( this.pipeline==null || this.pipeline.getPipelineKey()!=pipelineKey ) {
            this.pipeline = new Pipeline();
            this.pipeline.setPipelineKey(pipelineKey);
            log.setPipeline(this.pipeline);
        }

        log.setPipelineLogKey(rs.getInt("PIPELINE_LOG_KEY"));
        log.setRunStartTime(rs.getTimestamp("RUN_START_TIME"));
        log.setRunStopTime(rs.getTimestamp("RUN_END_TIME"));
        log.setSuccess(rs.getString("SUCCESS_IND"));
        log.setRunMode(rs.getString("RUN_MODE"));
        return log;
    }
}