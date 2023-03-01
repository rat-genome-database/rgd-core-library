package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 21, 2007
 * Time: 10:52:05 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Records changes to an RGD objects RGD ID.  Each record contains the original
 * RGD ID along with the new RGD ID and last modified/created dates.
 *
 * This information is currently contained in the RGD_ID_History table
 */
public class RGDIdHistoryEntry {
    private int key;
    private int oldRgdId;
    private int newRgdId;
    private Date lastModifiedDate;
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getOldRgdId() {
        return oldRgdId;
    }

    public void setOldRgdId(int oldRgdId) {
        this.oldRgdId = oldRgdId;
    }

    public int getNewRgdId() {
        return newRgdId;
    }

    public void setNewRgdId(int newRgdId) {
        this.newRgdId = newRgdId;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
