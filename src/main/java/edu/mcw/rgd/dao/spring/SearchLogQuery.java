package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.SearchLog;
import org.springframework.jdbc.object.MappingSqlQuery;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 6/30/2017.
 */
public class SearchLogQuery extends MappingSqlQuery{
    public SearchLogQuery(DataSource ds, String query){
        super(ds, query);

    }

    @Override
    protected SearchLog mapRow(ResultSet rs, int i) throws SQLException {
        SearchLog log= new SearchLog();
        log.setSearchTerm(rs.getString("search_term"));
        log.setCategory(rs.getString("category"));
        log.setResults(rs.getLong("results"));
        log.setDate(rs.getDate("search_date"));
        return log;
    }
}
