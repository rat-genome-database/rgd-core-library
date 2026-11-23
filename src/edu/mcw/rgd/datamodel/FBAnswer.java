package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * An answer to a message submitted through the Contact Us / Send Message forms (table FB_ANSWER in RGD_FB),
 * together with the person who gave the answer.
 */
public class FBAnswer {

    private int answerId;
    private Date answerDate;
    private String answer;
    private String answerOrig;
    private int messageId;
    private int personId; // the person who gave the answer
    private String firstName;
    private String lastName;
    private String email;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerOrig() {
        return answerOrig;
    }

    public void setAnswerOrig(String answerOrig) {
        this.answerOrig = answerOrig;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
