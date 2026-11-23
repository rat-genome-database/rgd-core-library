package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.FBPersonQuery;
import edu.mcw.rgd.dao.spring.FBQuestionQuery;
import edu.mcw.rgd.datamodel.FBPerson;
import edu.mcw.rgd.datamodel.FBQuestion;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class RgdFbDAO extends AbstractDAO {
    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getRgdFbDataSource();
    }
    public List<FBPerson> getPersonByEmail(String email) throws Exception {
        String query = "select * from FB_PERSON where EMAIL_ADDRESS like ?";
        return FBPersonQuery.execute(this,getDataSource(),query, email);
    }

    public FBPerson getPersonById(int id) throws Exception {
        String query = "select * from FB_PERSON where PERSON_ID=?";
        List<FBPerson> person = FBPersonQuery.execute(this,getDataSource(),query, id);
        return person.get(0);
    }

    public void insertPerson(FBPerson person) throws Exception {
        String query = "insert into FB_PERSON (PERSON_ID,FIRSTNAME,LASTNAME,EMAIL_ADDRESS,PHONE_NUMBER,INSTITUTE, ADDRESS,CITY,STATE,ZIP_CODE,COUNTRY,IS_CURATOR) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int personId = this.getNextKeyFromSequence("FB_PERSON_SEQ");
        update(query,personId, person.getFirstName(),person.getLastName(),person.getEmail(),person.getPhoneNumber(),
                person.getInstitute(),person.getAddress(),person.getCity(),person.getState(),person.getZipCode(),person.getCountry(),person.getIsCurator());
    }

    public List<FBPerson> getAllPeople() throws Exception {
        String query = "select * from FB_PERSON";
        return FBPersonQuery.execute(this,getDataSource(),query);
    }

    /**
     * Get the people who are RGD curators (IS_CURATOR is 'Y'), sorted by last name and first name.
     */
    public List<FBPerson> getCurators() throws Exception {
        String query = "select * from FB_PERSON where IS_CURATOR='Y' order by LASTNAME, FIRSTNAME, PERSON_ID";
        return FBPersonQuery.execute(this,getDataSource(),query);
    }

    public int insertMessage(String subject, String message, int type, int personId) throws Exception {
        String query = "insert into FB_QUESTION (MESSAGE_ID, SUBJECT, MESSAGE, MESSAGE_DATE, TYPE_ID, PERSON_ID) " +
                "values(?, ?, ?, LOCALTIMESTAMP(0), ?, ?)";
        int messageId = this.getNextKeyFromSequence("FB_QUESTION_SEQ");
        update(query, messageId, subject, message, type, personId);
        return messageId;
    }

    /**
     * Used for the Send Message Form, not Contact Us Form
     * */
    public int insertMessageForm(String subject, String message, int type) throws Exception {
        String query = "insert into FB_QUESTION (MESSAGE_ID, SUBJECT, MESSAGE, MESSAGE_DATE, TYPE_ID) " +
                "values(?, ?, ?, LOCALTIMESTAMP(0), ?)";
        int messageId = this.getNextKeyFromSequence("FB_QUESTION_SEQ");
        update(query, messageId, subject, message, type);
        return messageId;
    }

    // a message with its type name, sender and number of answers: the columns FBQuestionQuery reads
    private static final String QUESTION_SELECT =
            "select q.MESSAGE_ID, q.SUBJECT, q.MESSAGE, q.MESSAGE_DATE, q.TYPE_ID, t.TYPE_NAME, q.PERSON_ID, " +
            "p.FIRSTNAME, p.LASTNAME, p.EMAIL_ADDRESS, p.INSTITUTE, " +
            "(select count(*) from FB_ANSWER a where a.MESSAGE_ID=q.MESSAGE_ID) ANSWER_COUNT " +
            "from FB_QUESTION q " +
            "left join FB_TYPE t on t.TYPE_ID=q.TYPE_ID " +
            "left join FB_PERSON p on p.PERSON_ID=q.PERSON_ID ";

    /**
     * Get messages submitted in a date range, newest first, each with its type name, sender and number of answers.
     * @param fromDate messages submitted at or after this moment
     * @param toDate messages submitted before this moment
     */
    public List<FBQuestion> getQuestions(Date fromDate, Date toDate) throws Exception {
        String query = QUESTION_SELECT +
                "where q.MESSAGE_DATE>=? and q.MESSAGE_DATE<? " +
                "order by q.MESSAGE_DATE desc, q.MESSAGE_ID desc";
        return FBQuestionQuery.execute(this, getDataSource(), query, fromDate, toDate);
    }

    /**
     * Get one message with its type name, sender and number of answers.
     * @return the message, or null if there is no message with this id
     */
    public FBQuestion getQuestion(int messageId) throws Exception {
        String query = QUESTION_SELECT + "where q.MESSAGE_ID=?";
        List<FBQuestion> questions = FBQuestionQuery.execute(this, getDataSource(), query, messageId);
        return questions.isEmpty() ? null : questions.get(0);
    }

    /**
     * Get the day the oldest message was submitted.
     * @return date as yyyy-MM-dd, or null if there are no messages
     */
    public String getOldestQuestionDate() throws Exception {
        return getStringResult("select to_char(min(MESSAGE_DATE),'YYYY-MM-DD') from FB_QUESTION");
    }

    public void updatePerson(FBPerson person) throws Exception {
        String query = "update FB_PERSON set FIRSTNAME=?,LASTNAME=?,EMAIL_ADDRESS=?,PHONE_NUMBER=?,INSTITUTE=?, ADDRESS=?," +
                "CITY=?,STATE=?,ZIP_CODE=?,COUNTRY=?,IS_CURATOR=? where PERSON_ID=?";
        update(query,person.getFirstName(),person.getLastName(),person.getEmail(),person.getPhoneNumber(),person.getInstitute(),
                person.getAddress(),person.getCity(),person.getState(),person.getZipCode(),person.getCountry(),person.getIsCurator(),person.getPersonId());
    }

}
