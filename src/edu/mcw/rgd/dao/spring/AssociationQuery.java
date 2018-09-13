package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Association;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 10/27/11 <br>
 * Time: 1:37 PM <br>
 * <p/>
 * wrapper to conveniently query RGD_ASSOCIATION table
 */
public class AssociationQuery extends MappingSqlQuery {

    public AssociationQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Association assoc = new Association();
        mapRow(rs, assoc);
        return assoc;
    }

    protected void mapRow(ResultSet rs, Association assoc) throws SQLException {

        assoc.setAssocKey(rs.getInt("assoc_key"));
        assoc.setAssocSubType(rs.getString("assoc_subtype"));
        assoc.setAssocType(rs.getString("assoc_type"));
        assoc.setCreationDate(rs.getTimestamp("creation_date"));
        assoc.setMasterRgdId(rs.getInt("master_rgd_id"));
        assoc.setDetailRgdId(rs.getInt("detail_rgd_id"));
        assoc.setSrcPipeline(rs.getString("src_pipeline"));
    }
}
