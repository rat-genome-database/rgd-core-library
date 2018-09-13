package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.Relation;
import edu.mcw.rgd.datamodel.ontologyx.TermWithStats;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Loads additional statistics information for the term, as found in ONT_TERM_STATS2 table;
 * Note: gviewer related data is *NOT* loaded, for efficiency reasons
 */
public class TermWithStatsQuery extends TermQuery {

    final String STATS_SQL_WITH_FILTER = "SELECT * FROM ont_term_stats2 WHERE term_acc=? AND filter=?";
    final String STATS_SQL_NO_FILTER = "SELECT * FROM ont_term_stats2 WHERE term_acc=? AND filter IS NULL";

    private DataSource ds;
    private String filter;

    public TermWithStatsQuery(DataSource ds, String query, String filter) {
        super(ds, query);
        this.ds = ds;
        this.filter = filter;
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermWithStats term = new TermWithStats(filter);
        populateData(term, rs); // populate term specific data
        term.setRel(Relation.getRelFromRelId(rs.getString("ONT_REL_ID")));
        return term;
    }

    // overridden to additionally query ONT_TERM_STATS2 table
    public List<TermWithStats> execute(Object[] params, Map context) throws DataAccessException {
        // run the original query
        List<TermWithStats> results = (List<TermWithStats>) super.execute(params, context);

        // post-processing of the results
        String sql = filter==null ? STATS_SQL_NO_FILTER : STATS_SQL_WITH_FILTER;
        TermStatQuery q = new TermStatQuery(ds, sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        if (filter != null) {
            q.declareParameter(new SqlParameter(Types.VARCHAR));
        }

        for( TermWithStats term: results ) {
            if (filter == null) {
                term.setStats(q.execute(term.getAccId()));
            }else {
                term.setStats(q.execute(term.getAccId(), filter));
            }
        }
        return results;
    }
}

