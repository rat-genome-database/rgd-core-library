package edu.mcw.rgd.datamodel.skygen;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 5/9/14
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Patient {
    private String mrn;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
