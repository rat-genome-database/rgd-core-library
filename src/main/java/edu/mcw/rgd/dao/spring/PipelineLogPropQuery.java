package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Pipeline;
import edu.mcw.rgd.datamodel.PipelineLog;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 18, 2010
 * Time: 12:12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineLogPropQuery extends MappingSqlQuery {

    PipelineLog plog;

    public PipelineLogPropQuery(DataSource ds, String query, PipelineLog plog) {
        super(ds, query);
        this.plog = plog;
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PipelineLog.LogProp logprop = this.plog.createLogProp();
        logprop.setId(rs.getInt("PIPELINE_LOG_PROPERTY_KEY"));
        logprop.setPipelineLogKey(rs.getInt("PIPELINE_LOG_KEY"));
        logprop.setDate(rs.getTimestamp("EVENT_TIMESTAMP"));
        logprop.setInfo(rs.getString("EVENT_NOTES"));
        logprop.setKey(rs.getString("PIPELINE_LOG_PROPERTYTYPES_KEY"));
        logprop.setRecNo(rs.getInt("EVENT_RECORD_NO"));
        logprop.setValue(rs.getString("EVENT_VALUE"));
        logprop.setXml(rs.getString("EVENT_XML"));
        return logprop;
    }
}