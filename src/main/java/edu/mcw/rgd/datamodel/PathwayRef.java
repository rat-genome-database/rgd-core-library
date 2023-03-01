package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 31, 2011
 * Time: 3:39:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayRef extends Reference{

    private String pwId;
    private String refKey;

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }
    
    public String getPwId() {
        return pwId;
    }

    public void setPwId(String pwId) {
        this.pwId = pwId;
    }

}
