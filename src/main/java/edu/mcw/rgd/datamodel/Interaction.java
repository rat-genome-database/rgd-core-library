package edu.mcw.rgd.datamodel;

import java.util.Date;
import java.util.List;

/**
 * Created by jthota on 2/25/2016.
 */
public class Interaction {
    private int interactionKey;
    private int RgdId1;
    private int RgdId2;
    private List<InteractionAttribute> InteractionAttributes;
    private String interactionType ;
    private Date createdDate ;
    private Date lastModifiedDate;

    public int getInteractionKey() {
        return interactionKey;
    }

    public void setInteractionKey(int interactionKey) {
        this.interactionKey = interactionKey;
    }

    public int getRgdId1() {
        return RgdId1;
    }

    public void setRgdId1(int rgdId1) {
        RgdId1 = rgdId1;
    }

    public int getRgdId2() {
        return RgdId2;
    }

    public void setRgdId2(int rgdId2) {
        RgdId2 = rgdId2;
    }

    public List<InteractionAttribute> getInteractionAttributes() {
        return InteractionAttributes;
    }

    public void setInteractionAttributes(List<InteractionAttribute> interactionAttributes) {
        InteractionAttributes = interactionAttributes;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
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
