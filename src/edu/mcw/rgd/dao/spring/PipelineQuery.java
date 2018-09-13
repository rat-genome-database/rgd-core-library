package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Pipeline;
import edu.mcw.rgd.datamodel.SpeciesType;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.lang.Object;import java.lang.String;import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 15, 2010
 * Time: 1:03:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PipelineQuery extends MappingSqlQuery {

    public PipelineQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Pipeline p = new Pipeline();
        p.setPipelineKey(rs.getInt("PIPELINE_KEY"));
        p.setName(rs.getString("PIPELINE_NAME"));
        p.setInfo(rs.getString("NOTES"));
        p.setSpeciesTypeKey(SpeciesType.parse(rs.getString("SPECIES")));

        return p;
    }
}
