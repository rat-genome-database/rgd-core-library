package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

public class FBPerson {

    private int personId;
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
    private String institute;
    private String address;
    private String city;
    private String state;
    private int zipCode;
    private String country;

    public FBPerson() {}

    public int getPersonId() {
        return personId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getInstitute() {
        return institute;
    }

    public String getLastName() {
        return lastName;
    }

    public String getState() {
        return state;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object obj) {
        FBPerson fb = (FBPerson) obj;
        return Utils.stringsAreEqual(fb.getFirstName(),firstName) && Utils.stringsAreEqual(fb.getLastName(),lastName) && Utils.stringsAreEqual(fb.getEmail(), email)
                && fb.getPhoneNumber()==phoneNumber && Utils.stringsAreEqual(fb.getInstitute(),institute) && Utils.stringsAreEqual(fb.getAddress(),address)
                && Utils.stringsAreEqual(fb.getCity(),city) && Utils.stringsAreEqual(fb.getCountry(),country) && fb.getZipCode()==zipCode;
    }
    @Override
    public int hashCode() {
        return Utils.defaultString(firstName).hashCode() ^ Utils.defaultString(lastName).hashCode() ^ Utils.defaultString(email).hashCode()
                ^ getPhoneNumber() ^ Utils.defaultString(institute).hashCode() ^ Utils.defaultString(address).hashCode() ^ Utils.defaultString(city).hashCode()
                ^ Utils.defaultString(country).hashCode() ^ getZipCode();
    }
}
