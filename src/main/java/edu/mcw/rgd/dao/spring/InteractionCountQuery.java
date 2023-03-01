package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.InteractionCount;
import org.springframework.jdbc.object.MappingSqlQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 6/9/2016.
 */
public class InteractionCountQuery extends MappingSqlQuery{
    public InteractionCountQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        InteractionCount obj = new InteractionCount();
        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setCount(rs.getInt("interactions_count"));
        obj.setCreateDate(rs.getDate("created_date"));
        obj.setLastModifiedDate(rs.getDate("last_modified_date"));

        return obj;
    }
}
