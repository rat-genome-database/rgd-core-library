package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by jthota on 3/14/2016.
 */
public class InteractionAttribute {
    private int attributeKey;
    private int interactionKey;
    private String attributeName;
    private String attributeValue;
    private Date createdDate;
    private Date lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InteractionAttribute that = (InteractionAttribute) o;

        if (interactionKey != that.interactionKey) return false;
        if (!attributeName.equals(that.attributeName)) return false;
        return attributeValue.equals(that.attributeValue);

    }

    @Override
    public int hashCode() {
        int result = interactionKey;
        result = 31 * result + attributeName.hashCode();
        result = 31 * result + attributeValue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InteractionAttribute{ " +
                "attributeKey=" + attributeKey +
                ", interactionKey=" + interactionKey +
                ", attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }

    public int getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(int attributeKey) {
        this.attributeKey = attributeKey;
    }

    public int getInteractionKey() {
        return interactionKey;
    }

    public void setInteractionKey(int interactionKey) {
        this.interactionKey = interactionKey;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
