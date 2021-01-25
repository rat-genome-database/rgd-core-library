package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RGDNewsConfQuery;
import edu.mcw.rgd.datamodel.RGDNewsConf;

import java.util.List;


public class RGDNewsConfDAO extends AbstractDAO {
    public RGDNewsConfDAO() {}

    public List<RGDNewsConf> getAllNews() throws Exception {
        String query = "select * from RGD_NEWS_CONFERENCES where CONTENT_TYPE='NEWS'";
        return RGDNewsConfQuery.execute(this,query);
    }

    public List<RGDNewsConf> getAllConferences() throws Exception {
        String query = "select * from RGD_NEWS_CONFERENCES where CONTENT_TYPE='CONFERENCE'";
        return RGDNewsConfQuery.execute(this,query);
    }

    public List<RGDNewsConf> getAllVideos() throws Exception {
        String query = "select * from RGD_NEWS_CONFERENCES where CONTENT_TYPE='VIDEO'";
        return RGDNewsConfQuery.execute(this,query);
    }

    public int deleteRGDNewsConf(int newsId) throws Exception{
        String sql = "DELETE FROM RGD_NEWS_CONFERENCES WHERE NEWS_ID=?";
        return update(sql, newsId);
    }

    public void insertIntoRGDNewsConf(RGDNewsConf newsConf) throws Exception {
        int key = this.getNextKeyFromSequence("RGD_NEWS_CONFERENCES_SEQ");
        newsConf.setNewsId(key);
        String query = "insert into RGD_NEWS_CONFERENCES (news_id, display_text, redirect_link, CONTENT_TYPE, strong_text, release_date) " +
                "values (?, ?, ?, upper(?), ?, to_date(?,'yyyy-MM-dd'))";
        update(query, newsConf.getNewsId(), newsConf.getDisplayText(), newsConf.getRedirectLink(), newsConf.getContentType(),
                newsConf.getStrongText(), newsConf.getDate().toString());

        return;
    }

}