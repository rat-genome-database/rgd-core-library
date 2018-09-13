package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 2, 2008
 * Time: 9:00:51 AM
 */

/**
 * Interface implemented by all objects with an RGD ID
 */
public interface Identifiable {

    /**
     * Return this objects rgd id.  This method must be implemented by
     * subclasses.
     * @return
     */
    public int getRgdId();

    public void setRgdId(int rgdId);
}
