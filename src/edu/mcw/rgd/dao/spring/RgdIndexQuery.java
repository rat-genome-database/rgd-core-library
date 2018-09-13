package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.search.IndexRow;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the rgd_index table
 */
public class RgdIndexQuery extends MappingSqlQuery {

    public RgdIndexQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        IndexRow row = new IndexRow();
        row.setDataType(rs.getString("data_type"));
        row.setKeyword(rs.getString("keyword_lc"));
        row.setObjectType(rs.getString("object_type"));
        row.setRank(rs.getInt("rank"));
        row.setRgdId(rs.getInt("rgd_id"));
        row.setSpeciesTypeKey(rs.getInt("species_type_key"));
        return row;
    }

}