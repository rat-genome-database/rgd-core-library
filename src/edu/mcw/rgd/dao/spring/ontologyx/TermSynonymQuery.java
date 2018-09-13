package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.TermSynonym;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Apr 13, 2011
 * Time: 9:57:23 AM
 * represents a term synonym object, as found in ONT_TERM_SYNONYMS table
 */
public class TermSynonymQuery extends MappingSqlQuery {

    public TermSynonymQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermSynonym syn = new TermSynonym();
        syn.setKey(rs.getInt("syn_key"));
        syn.setTermAcc(rs.getString("term_acc"));
        syn.setName(rs.getString("synonym_name"));
        syn.setType(rs.getString("synonym_type"));
        syn.setDbXrefs(rs.getString("dbxrefs"));
        syn.setSource(rs.getString("source"));
        syn.setCreatedDate(rs.getTimestamp("created_date"));
        syn.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return syn;
    }
}
