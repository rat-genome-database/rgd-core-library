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
