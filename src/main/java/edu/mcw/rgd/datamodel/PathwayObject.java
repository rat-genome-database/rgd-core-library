package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 28, 2011
 * Time: 5:53:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayObject {
    String id;
    int typeId;
    String accId;
    Integer xdb_key;
    String url;
    String typeName;
    String objName;
    String objDesc;

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjDesc() {
        return objDesc;
    }

    public void setObjDesc(String objDesc) {
        this.objDesc = objDesc;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getXdb_key() {
        return xdb_key;
    }

    public void setXdb_key(Integer xdb_key) {
        this.xdb_key = xdb_key;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }





}
