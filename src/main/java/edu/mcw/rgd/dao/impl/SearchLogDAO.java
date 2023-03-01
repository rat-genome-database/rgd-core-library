package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SearchLogQuery;
import edu.mcw.rgd.datamodel.SearchLog;

/**
 * Created by jthota on 6/30/2017.
 */
public class SearchLogDAO extends AbstractDAO {

    public int insert(SearchLog log) throws Exception {
        String query= "INSERT INTO SEARCH_LOG(SEARCH_TERM, CATEGORY, RESULTS, SEARCH_DATE) VALUES(?,?,?,SYSDATE)";
        return update(query, log.getSearchTerm(),log.getCategory(), log.getResults());
    }
}
