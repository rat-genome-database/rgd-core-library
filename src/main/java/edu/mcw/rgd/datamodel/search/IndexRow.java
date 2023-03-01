package edu.mcw.rgd.datamodel.search;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 14, 2009
 * Time: 10:55:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexRow {

    private int rgdId;
    private String keyword;
    private String objectType;
    private String dataType;
    private int speciesTypeKey;
    private int rank;

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
