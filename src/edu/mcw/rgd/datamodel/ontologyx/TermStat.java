package edu.mcw.rgd.datamodel.ontologyx;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 3/20/15
 * Time: 2:20 PM
 * <p>
 * represents a rows from ONT_TERM_STATS2 table
 */
public class TermStat {
    private String termAcc;
    private Date lastModifiedDate;
    private int speciesTypeKey;
    private int objectKey;
    private int withChildren; // 0 or 1
    private String statName;
    private int statValue;
    private String filter;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }




    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public int getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(int objectKey) {
        this.objectKey = objectKey;
    }

    public int getWithChildren() {
        return withChildren;
    }

    public void setWithChildren(int withChildren) {
        this.withChildren = withChildren;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public int getStatValue() {
        return statValue;
    }

    public void setStatValue(int statValue) {
        this.statValue = statValue;
    }
}
