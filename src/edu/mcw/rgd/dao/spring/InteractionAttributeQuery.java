package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.InteractionAttribute;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 3/23/2016.
 */
public class InteractionAttributeQuery extends MappingSqlQuery {
    public InteractionAttributeQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    public InteractionAttribute mapRow(ResultSet rs, int i) throws SQLException {
        InteractionAttribute a = new InteractionAttribute();
        a.setAttributeKey(rs.getInt("attribute_key"));
        a.setInteractionKey(rs.getInt("interaction_key"));
        a.setAttributeName(rs.getString("attribute_name"));
        a.setAttributeValue(rs.getString("attribute_value"));
        a.setCreatedDate(rs.getDate("created_date"));
        a.setLastModifiedDate(rs.getDate("last_modified_date"));
        return a;
    }
}
