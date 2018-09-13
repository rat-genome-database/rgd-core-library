package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

import java.util.Date;

/**
 * Created by mtutaj on 8/15/2016.
 * <p>
 * represents a row in MIRNA_TARGET_STATS table
 */
public class MiRnaTargetStat implements Identifiable {

    int key;
    int rgdId;
    String name;
    String value;
    Date createdDate;
    Date lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        MiRnaTargetStat s = (MiRnaTargetStat)o;

        return this.rgdId == s.rgdId
            && Utils.stringsAreEqual(this.name, s.name)
            && Utils.stringsAreEqual(this.value, s.value);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + rgdId;
        return result;
    }

    @Override
    public int getRgdId() {
        return rgdId;
    }

    @Override
    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
