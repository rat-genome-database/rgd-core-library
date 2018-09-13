package edu.mcw.rgd.datamodel.search;

import edu.mcw.rgd.process.Utils;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 3, 2008
 * Time: 9:47:46 AM
 */
public class RankedIndexItem {

    private int id;
    private int rank = 0;
    private int speciesTypeKey;
    private HashMap dataType = new HashMap();
    private String objectType;


    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public Set getDataTypeIterator() {
        return dataType.keySet();
    }

    public String buildSourceString() {
        return Utils.concatenate(dataType.keySet(), " , ");
    }

    public void addDataType(String dataType) {
        if (this.dataType.get(dataType) == null) {
            this.dataType.put(dataType, null);
        }
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void addToRank(int rank) {
        this.rank = this.rank + rank;
    }

}
