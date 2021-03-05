package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.WebFeedback;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WebFeedbackQuery extends MappingSqlQuery {
    public WebFeedbackQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        WebFeedback wl = new WebFeedback();
        wl.setThumbsUp(rs.getInt("THUMBS_UP"));
        wl.setThumbsDown(rs.getInt("THUMBS_DOWN"));
        wl.setDate(rs.getDate("DATE_LIKED"));
        return wl;
    }

    public static List<WebFeedback> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        WebFeedbackQuery q = new WebFeedbackQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
