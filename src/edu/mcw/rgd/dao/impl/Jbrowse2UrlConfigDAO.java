package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.Jbrowse2UrlConfigQuery;
import edu.mcw.rgd.datamodel.Jbrowse2UrlConfig;

import java.util.*;

public class Jbrowse2UrlConfigDAO extends AbstractDAO {

    // the table is small (under 100 rows) and changes rarely, while report pages look it up once per row
    // (thousands of times for a large ontology term); it is read whole and kept for CACHE_MS
    private static final long CACHE_MS = 10 * 60 * 1000;
    private static volatile Map<String, List<Jbrowse2UrlConfig>> cache;
    private static volatile long cacheLoadedAt;

    public List<Jbrowse2UrlConfig> getAllConfigs() throws Exception{
        String sql = "Select * from jbrowse2_url_config";
        return Jbrowse2UrlConfigQuery.execute(this,sql);
    }

    /**
     * @return configs for the given map and object type, from a cache of the whole table refreshed every 10 minutes
     */
    public List<Jbrowse2UrlConfig> getJbrowse2UrlConfigsByMapAndObjectKey(int mapKey, int objectKey) throws Exception{
        Map<String, List<Jbrowse2UrlConfig>> configs = cache;
        if( configs==null || System.currentTimeMillis()-cacheLoadedAt > CACHE_MS ) {
            configs = new HashMap<>();
            for( Jbrowse2UrlConfig c: getAllConfigs() ) {
                configs.computeIfAbsent(c.getMapKey()+":"+c.getObjectKey(), k -> new ArrayList<>()).add(c);
            }
            for( Map.Entry<String, List<Jbrowse2UrlConfig>> e: configs.entrySet() ) {
                e.setValue(Collections.unmodifiableList(e.getValue()));
            }
            cache = configs;
            cacheLoadedAt = System.currentTimeMillis();
        }
        List<Jbrowse2UrlConfig> list = configs.get(mapKey+":"+objectKey);
        return list==null ? Collections.emptyList() : list;
    }
}
