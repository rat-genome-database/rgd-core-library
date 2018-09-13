package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 7, 2011
 * Time: 4:13:57 PM
 */
public class Author {
    private int key;
    private String lastName;
    private String firstName;
    private String initials;
    private String suffix;
    private String emailAddress;
    private String institution;
    private String notes;

    // author last name optionally followed by initials if available
    public String getAuthorForCitation() {
        String lastName = Utils.defaultString(getLastName());
        if( Utils.isStringEmpty(getInitials()) ) {
            return lastName; // no initials
        } else {
            return lastName+" "+getInitials();
        }
    }

    /**
     * check if this author is the same as the other author
     * by having the same (last name, first name, initials and suffix)
     * @return true if this author is the same as the other author
     */
    public boolean equalsByContent(Author a) {
        return Utils.stringsAreEqualIgnoreCase(this.getLastName(), a.getLastName()) &&
                Utils.stringsAreEqualIgnoreCase(this.getFirstName(), a.getFirstName()) &&
                Utils.stringsAreEqualIgnoreCase(this.getInitials(), a.getInitials()) &&
                Utils.stringsAreEqualIgnoreCase(this.getSuffix(), a.getSuffix());
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
