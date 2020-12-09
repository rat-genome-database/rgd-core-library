package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.RGDNewsConf;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RGDNewsConfQuery extends MappingSqlQuery {

    public RGDNewsConfQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        RGDNewsConf newsConf = new RGDNewsConf();
        newsConf.setNewsId(rs.getInt("NEWS_ID"));
        newsConf.setContentType(rs.getString("CONTENT_TYPE"));
        newsConf.setDisplayText(rs.getString("DISPLAY_TEXT"));
        newsConf.setRedirectLink(rs.getString("REDIRECT_LINK"));


        return newsConf;
    }

    public static List<RGDNewsConf> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        RGDNewsConfQuery q = new RGDNewsConfQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}