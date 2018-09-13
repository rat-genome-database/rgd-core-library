package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Sep 22, 2009
 * Time: 4:30:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OMIMMapping {

    private int OMIMId;
    private String type;
    private String disorder;
    private String location;

    public int getOMIMId() {
        return OMIMId;
    }

    public void setOMIMId(int OMIMId) {
        this.OMIMId = OMIMId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisorder() {
        return disorder;
    }

    public void setDisorder(String disorder) {
        this.disorder = disorder;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
