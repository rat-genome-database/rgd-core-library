package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.Jbrowse2UrlConfigQuery;
import edu.mcw.rgd.datamodel.Jbrowse2UrlConfig;

import java.util.List;

public class Jbrowse2UrlConfigDAO extends AbstractDAO {
    public List<Jbrowse2UrlConfig> getAllConfigs() throws Exception{
        String sql = "Select * from jbrowse2_url_config";
        return Jbrowse2UrlConfigQuery.execute(this,sql);
    }

    public List<Jbrowse2UrlConfig> getJbrowse2UrlConfigsByMapAndObjectKey(int mapKey, int objectKey) throws Exception{
        String sql = "Select * from jbrowse2_url_config where map_key=? and object_key=?";
        return Jbrowse2UrlConfigQuery.execute(this,sql,mapKey,objectKey);
    }
}
