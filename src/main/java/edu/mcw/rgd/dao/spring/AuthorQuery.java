package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Author;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * <p>
 * Returns a row from AUTHORS table
 */
public class AuthorQuery extends MappingSqlQuery {

    public AuthorQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Author a = new Author();
        a.setKey(rs.getInt("author_key"));
        a.setEmailAddress(rs.getString("email_address"));
        a.setFirstName(rs.getString("author_fname"));
        a.setLastName(rs.getString("author_lname"));
        a.setInstitution(rs.getString("institution"));
        a.setNotes(rs.getString("notes"));
        a.setInitials(rs.getString("author_iname"));
        a.setSuffix(rs.getString("author_suffix"));

        return a;
    }
}