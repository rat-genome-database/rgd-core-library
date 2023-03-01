package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 2, 2008
 * Time: 9:00:51 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interface implemented by all objects with an RGD ID
 */
public interface Speciated {

    /**
     * Return this objects rgd id.  This method must be implemented by
     * subclasses.
     * @return
     */
    public int getSpeciesTypeKey();

}
