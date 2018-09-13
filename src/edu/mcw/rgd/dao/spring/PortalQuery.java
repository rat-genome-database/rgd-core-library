package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Portal;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 26, 2011
 * Time: 12:44:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalQuery extends MappingSqlQuery {

    public PortalQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Portal p = new Portal();
        p.setKey(rs.getInt("portal_key"));
        p.setUrlName(rs.getString("url_name"));
        p.setFullName(rs.getString("full_name"));
        p.setPageName(rs.getString("page_name"));
        p.setPageTitle(rs.getString("page_title"));
        p.setImageUrl(rs.getString("page_img_url"));
        p.setCategoryPageDescription(rs.getString("page_category_page_desc"));
        p.setSubCategoryDescription(rs.getString("page_sub_category_desc"));
        p.setSummaryDescription(rs.getString("page_summary_desc"));
        p.setLastUpdated(rs.getDate("date_last_updated"));
        p.setPortalType(rs.getString("portal_type"));
        p.setPortalType(rs.getString("portal_type"));
        p.setMasterPortalKey(rs.getInt("master_portal_key"));

        return p;
    }
}