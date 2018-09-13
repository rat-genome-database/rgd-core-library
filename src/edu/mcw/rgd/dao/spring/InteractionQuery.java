package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Interaction;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 3/23/2016.
 */
public class InteractionQuery extends MappingSqlQuery {
    public InteractionQuery(DataSource ds, String query){
       super(ds, query);
    }
    @Override
    public Interaction mapRow(ResultSet rs, int i) throws SQLException {
        Interaction p=new Interaction();
        p.setInteractionKey(rs.getInt("interaction_key"));
        p.setRgdId1(rs.getInt("rgd_id_1"));
        p.setRgdId2(rs.getInt("rgd_id_2"));
        p.setInteractionType(rs.getString("interaction_type"));
        p.setCreatedDate(rs.getDate("created_date"));
        p.setLastModifiedDate(rs.getDate("last_modified_date"));
        return p;
    }
}
