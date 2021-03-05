package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;

import java.sql.Date;

public class WebFeedbackDAO extends AbstractDAO {
    public WebFeedbackDAO() {}
    public void insertLike() throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into website_feedback (thumbs_up, date_liked) values (1, to_date(?, 'yyyy-MM-dd))";
        String curDate = date.toString();

        update(sql,curDate);

        return;
    }
    public void insertDislike() throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into website_feedback (thumbs_down, date_liked) values (1, to_date(?, 'yyyy-MM-dd))";
        String curDate = date.toString();

        update(sql,curDate);
        return;
    }
}
