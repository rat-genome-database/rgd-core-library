package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.Term;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 */
public class TermQuery extends MappingSqlQuery {

    public TermQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Term term = new Term();
        populateData(term, rs);
        return term;
    }

    protected void populateData(Term term, ResultSet rs) throws SQLException {
        term.setOntologyId(rs.getString("ont_id"));
        term.setAccId(rs.getString("term_acc"));
        term.setTerm(rs.getString("term"));
        term.setObsolete(rs.getInt("is_obsolete"));
        term.setCreatedBy(rs.getString("created_by"));
        term.setCreationDate(rs.getTimestamp("creation_date"));
        term.setModificationDate(rs.getTimestamp("modification_date"));
        term.setDefinition(rs.getString("term_definition"));
        term.setComment(rs.getString("term_comment"));
    }
}