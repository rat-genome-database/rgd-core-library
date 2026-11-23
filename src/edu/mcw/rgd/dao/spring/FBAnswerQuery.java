package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.FBAnswer;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Maps a row of FB_ANSWER joined with FB_PERSON (the person who gave the answer).
 */
public class FBAnswerQuery extends MappingSqlQuery {
    public FBAnswerQuery(DataSource ds, String query) {
        super(ds, query);
    }
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        FBAnswer a = new FBAnswer();
        a.setAnswerId(rs.getInt("ANSWER_ID"));
        a.setAnswerDate(rs.getTimestamp("ANSWER_DATE"));
        a.setAnswer(rs.getString("ANSWER"));
        a.setAnswerOrig(rs.getString("ANSWER_ORIG"));
        a.setMessageId(rs.getInt("MESSAGE_ID"));
        a.setPersonId(rs.getInt("PERSON_ID"));
        a.setFirstName(rs.getString("FIRSTNAME"));
        a.setLastName(rs.getString("LASTNAME"));
        a.setEmail(rs.getString("EMAIL_ADDRESS"));
        return a;
    }
    public static List<FBAnswer> execute(AbstractDAO dao, DataSource src, String sql, Object... params) throws Exception {
        FBAnswerQuery q = new FBAnswerQuery(src, sql);
        return dao.execute(q, params);
    }
}
