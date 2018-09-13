package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PortalCat;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 05/11/12
 * Time: 12:44 PM
 * helper class to simplify querying of PORTAL_CAT1 table
 */
public class PortalCatQuery extends MappingSqlQuery {

    public PortalCatQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PortalCat p = new PortalCat();
        p.setPortalCatId(rs.getInt("portal_cat_id"));
        p.setPortalVerId(rs.getInt("portal_ver_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setCategoryTermAcc(rs.getString("category_term_acc"));
        p.setParentCatId(rs.getInt("parent_cat_id"));
        if( rs.wasNull() )
            p.setParentCatId(null);
        p.setSummaryTableHtml(rs.getString("summary_table_html"));
        p.setgViewerXmlRat(rs.getString("gviewer_xml_rat"));
        p.setgViewerXmlMouse(rs.getString("gviewer_xml_mouse"));
        p.setgViewerXmlHuman(rs.getString("gviewer_xml_human"));
        p.setGeneInfoHtml(rs.getString("gene_info_html"));
        p.setQtlInfoHtml(rs.getString("qtl_info_html"));
        p.setStrainInfoHtml(rs.getString("strain_info_html"));
        p.setAnnotObjCntRat(rs.getInt("annot_obj_cnt_rat"));
        p.setAnnotObjCntWithChildrenRat(rs.getInt("annot_obj_cnt_w_children_rat"));
        p.setAnnotObjCntWithChildrenMouse(rs.getInt("annot_obj_cnt_w_children_mouse"));
        p.setAnnotObjCntWithChildrenHuman(rs.getInt("annot_obj_cnt_w_children_human"));
        p.setChartXmlBp(rs.getString("chart_xml_bp"));
        p.setChartXmlCc(rs.getString("chart_xml_cc"));
        p.setChartXmlMp(rs.getString("chart_xml_mp"));
        return p;
    }
}
