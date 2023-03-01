package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Pipeline;
import edu.mcw.rgd.datamodel.PipelineLog;
import edu.mcw.rgd.datamodel.pipeline.PipelineFlag;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 9/12/11 <br>
 * Time: 3:51 PM <br>
 * <p>
 * helper class to browse PIPELINE_FLAGS table
 */
public class PipelineFlagQuery extends MappingSqlQuery {

    public PipelineFlagQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PipelineFlag row = new PipelineFlag();

        row.setId(rs.getInt("PIPELINE_FLAG_ID"));
        row.setSymbol(rs.getString("PIPELINE_FLAG_SYMBOL"));
        row.setDescription(rs.getString("PIPELINE_FLAG_DESCRIPTION"));
        row.setPipeline(rs.getString("PIPELINE_NAME"));

        return row;
    }
}
