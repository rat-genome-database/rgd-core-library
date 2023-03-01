package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.XdbId;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 11/29/12
 * Time: 11:38 AM
 * <p>
 * used for querying RGD_ACC_XDB table
 */
public class XdbQuery extends MappingSqlQuery {

    public XdbQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        XdbId xdb = new XdbId();
        xdb.setKey(rs.getInt("ACC_XDB_KEY"));
        xdb.setXdbKey(rs.getInt("XDB_KEY"));
        xdb.setAccId(rs.getString("ACC_ID"));
        xdb.setRgdId(rs.getInt("RGD_ID"));
        xdb.setLinkText(rs.getString("LINK_TEXT"));
        xdb.setSrcPipeline(rs.getString("SRC_PIPELINE"));
        xdb.setNotes(rs.getString("NOTES"));
        xdb.setCreationDate(rs.getTimestamp("CREATION_DATE"));
        xdb.setModificationDate(rs.getTimestamp("MODIFICATION_DATE"));
        return xdb;
    }
}
