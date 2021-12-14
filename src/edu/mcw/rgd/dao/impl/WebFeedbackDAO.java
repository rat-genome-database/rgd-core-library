package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.Date;

public class WebFeedbackDAO extends AbstractDAO {
    public WebFeedbackDAO() {}
    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getRgdFbDataSource();
    }
    public void insertLike(String page) throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into WEBSITE_FEEDBACK (THUMBS_UP, DATE_LIKED, WEB_PAGE) values (1, to_date(?, 'yyyy-MM-dd'), ?)";
        String curDate = date.toString();

        update(sql,curDate, page);

        return;
    }
    public void insertDislike(String page) throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into WEBSITE_FEEDBACK (THUMBS_DOWN, DATE_LIKED, WEB_PAGE) values (1, to_date(?, 'yyyy-MM-dd'), ?)";
        String curDate = date.toString();

        update(sql,curDate, page);
        return;
    }
}
