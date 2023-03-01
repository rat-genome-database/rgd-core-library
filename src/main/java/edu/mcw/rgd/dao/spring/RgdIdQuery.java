package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.RgdId;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the Alias table
 */
public class RgdIdQuery extends MappingSqlQuery {

    public RgdIdQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        RgdId rgdId = new RgdId(rs.getInt("rgd_id"));
        rgdId.setCreatedDate(rs.getDate("created_date"));
        rgdId.setLastModifiedDate(rs.getDate("last_modified_date"));
        rgdId.setNotes(rs.getString("notes"));
        rgdId.setObjectKey(rs.getInt("object_key"));
        rgdId.setObjectStatus(rs.getString("object_status"));
        rgdId.setReleasedDate(rs.getDate("released_date"));
        rgdId.setRgdFlag(rs.getString("rgd_flag"));
        rgdId.setSpeciesTypeKey(rs.getInt("species_type_key"));

        return rgdId;
    }
}