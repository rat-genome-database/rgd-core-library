package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.FBQuestion;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Maps a row of FB_QUESTION joined with FB_TYPE and FB_PERSON, plus an ANSWER_COUNT column.
 */
public class FBQuestionQuery extends MappingSqlQuery {
    public FBQuestionQuery(DataSource ds, String query) {
        super(ds, query);
    }
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        FBQuestion q = new FBQuestion();
        q.setMessageId(rs.getInt("MESSAGE_ID"));
        q.setSubject(rs.getString("SUBJECT"));
        q.setMessage(rs.getString("MESSAGE"));
        q.setMessageDate(rs.getTimestamp("MESSAGE_DATE"));
        q.setTypeId(rs.getInt("TYPE_ID"));
        q.setTypeName(rs.getString("TYPE_NAME"));
        q.setPersonId(rs.getInt("PERSON_ID"));
        q.setFirstName(rs.getString("FIRSTNAME"));
        q.setLastName(rs.getString("LASTNAME"));
        q.setEmail(rs.getString("EMAIL_ADDRESS"));
        q.setInstitute(rs.getString("INSTITUTE"));
        q.setAnswerCount(rs.getInt("ANSWER_COUNT"));
        return q;
    }
    public static List<FBQuestion> execute(AbstractDAO dao, DataSource src, String sql, Object... params) throws Exception {
        FBQuestionQuery q = new FBQuestionQuery(src, sql);
        return dao.execute(q, params);
    }
}
