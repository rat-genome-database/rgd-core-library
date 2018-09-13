package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.NomenclatureEvent;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.object.MappingSqlQuery;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 16, 2008
 * Time: 8:37:24 AM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Created by IntelliJ IDEA.
 * User: GKowalski
 * Date: Nov 9, 2007
 * Time: 4:35:31 PM
 *
 */
public class NomenclatureEventsQuery extends MappingSqlQuery {

    public NomenclatureEventsQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        NomenclatureEvent ne = new NomenclatureEvent();

        ne.setRgdId(rs.getInt("rgd_id"));
        ne.setEventDate(rs.getDate("event_date"));
        ne.setName(rs.getString("name"));
        ne.setNomenEventKey(rs.getInt("nomen_event_key"));
        ne.setSymbol(rs.getString("symbol"));
        ne.setRefKey(rs.getString("ref_key"));
        ne.setNomenStatusType(rs.getString("nomen_status_type"));
        ne.setDesc(rs.getString("description"));
        ne.setNotes(rs.getString("notes"));
        ne.setOriginalRGDId(rs.getInt("original_rgd_id"));
        ne.setPreviousSymbol(rs.getString("previous_symbol"));
        ne.setPreviousName(rs.getString("previous_name"));
        return ne;
    }
}
