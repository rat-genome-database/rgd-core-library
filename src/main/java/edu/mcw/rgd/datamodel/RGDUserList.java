package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 2/26/15
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class RGDUserList {

    private int userId;
    private int listId;
    private int objectType;
    private int mapKey;
    private String listName;
    private Date createdDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}



