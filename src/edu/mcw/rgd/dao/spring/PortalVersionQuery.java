package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PortalVersion;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/27/11
 * Time: 12:44 PM
 * helper class to simplify querying of PORTAL_VER1 table
 */
public class PortalVersionQuery extends MappingSqlQuery {

    public PortalVersionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PortalVersion p = new PortalVersion();
        p.setPortalVerId(rs.getInt("portal_ver_id"));
        p.setPortalKey(rs.getInt("portal_key"));
        p.setDateLastUpdated(rs.getTimestamp("date_last_updated"));
        p.setPortalVerStatus(rs.getString("portal_ver_status"));
        p.setPortalBuildNum(rs.getLong("portal_build_num"));
        p.setChartXmlBp(rs.getString("chart_xml_bp"));
        p.setChartXmlCc(rs.getString("chart_xml_cc"));
        p.setChartXmlMp(rs.getString("chart_xml_mp"));
        return p;
    }
}
