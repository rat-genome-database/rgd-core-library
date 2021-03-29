package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;

import java.sql.Date;

public class WebFeedbackDAO extends AbstractDAO {
    public WebFeedbackDAO() {}
    public void insertLike(String page) throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into website_feedback (thumbs_up, date_liked, web_page) values (1, to_date(?, 'yyyy-MM-dd'), ?)";
        String curDate = date.toString();

        update(sql,curDate, page);

        return;
    }
    public void insertDislike(String page) throws Exception {
        java.util.Date today = new java.util.Date();
        Date date = new Date(today.getTime());

        String sql = "insert into website_feedback (thumbs_down, date_liked, web_page) values (1, to_date(?, 'yyyy-MM-dd'), ?)";
        String curDate = date.toString();

        update(sql,curDate, page);
        return;
    }
}
