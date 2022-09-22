package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.FBPersonQuery;
import edu.mcw.rgd.datamodel.FBPerson;

import javax.sql.DataSource;
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
        String query = "insert into FB_PERSON (PERSON_ID,FIRSTNAME,LASTNAME,EMAIL_ADDRESS,PHONE_NUMBER,INSTITUTE, ADDRESS,CITY,STATE,ZIP_CODE,COUNTRY) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int personId = this.getNextKeyFromSequence("FB_PERSON_SEQ");
        update(query,personId, person.getFirstName(),person.getLastName(),person.getEmail(),person.getPhoneNumber(),
                person.getInstitute(),person.getAddress(),person.getCity(),person.getState(),person.getZipCode(),person.getCountry());
    }

    public List<FBPerson> getAllPeople() throws Exception {
        String query = "select * from FB_PERSON";
        return FBPersonQuery.execute(this,getDataSource(),query);
    }

    public int insertMessage(String subject, String message, int type, int personId) throws Exception {
        String query = "insert into FB_QUESTION (MESSAGE_ID, SUBJECT, MESSAGE, MESSAGE_DATE, TYPE_ID, PERSON_ID) " +
                "values(?, ?, ?, CURRENT_DATE, ?, ?)";
        int messageId = this.getNextKeyFromSequence("FB_QUESTION_SEQ");
        update(query, messageId, subject, message, type, personId);
        return messageId;
    }

    /**
     * Used for the Send Message Form, not Contact Us Form
     * */
    public int insertMessageForm(String subject, String message, int type) throws Exception {
        String query = "insert into FB_QUESTION (MESSAGE_ID, SUBJECT, MESSAGE, MESSAGE_DATE, TYPE_ID) " +
                "values(?, ?, ?, CURRENT_DATE, ?)";
        int messageId = this.getNextKeyFromSequence("FB_QUESTION_SEQ");
        update(query, messageId, subject, message, type);
        return messageId;
    }

    public void updatePerson(FBPerson person) throws Exception {
        String query = "update FB_PERSON set FIRSTNAME=?,LASTNAME=?,EMAIL_ADDRESS=?,PHONE_NUMBER=?,INSTITUTE=?, ADDRESS=?," +
                "CITY=?,STATE=?,ZIP_CODE=?,COUNTRY=? where PERSON_ID=?";
        update(query,person.getFirstName(),person.getLastName(),person.getEmail(),person.getPhoneNumber(),person.getInstitute(),
                person.getAddress(),person.getCity(),person.getState(),person.getZipCode(),person.getCountry(),person.getPersonId());
    }

}
