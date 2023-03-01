package edu.mcw.rgd.process.sync;

import edu.mcw.rgd.datamodel.XdbId;
import edu.mcw.rgd.process.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/19/12
 * Time: 2:33 PM
 * <p>
 * Synchronizes xdb ids between incoming data and data in RGD.
 * New xdb ids are added, updated or deleted if needed.
 * One RGD object can have xdb ids for multiple xdb keys.
 * Incoming positions are compared against existing positions in RGD and then updated until they are in sync.
 * To avoid deletion of good data, the following rule is observed: only xdb keys that appear in incoming data are synchronized.
 * Optionally all database operations regarding xdb ids could be logged into log4j file.
 * </p>
 */
public class XdbIdSyncer extends RgdObjectSyncer {

    public boolean objectsAreEqual(Object obj1, Object obj2) {

        XdbId x1 = (XdbId) obj1;
        XdbId x2 = (XdbId) obj2;

        return Utils.intsAreEqual(x1.getXdbKey(), x2.getXdbKey())
            && Utils.stringsAreEqual(x1.getAccId(), x2.getAccId());
    }

    @Override
    public boolean objectsAreEqualByUniqueKey(Object obj1, Object obj2) {
        return objectsAreEqual(obj1, obj2);
    }

    @Override
    public boolean objectsAreEqualByContent(Object obj1, Object obj2) {
        return objectsAreEqual(obj1, obj2);
    }

    @Override
    public void updateContentForObjectsEqualByUniqueKey(Object objDest, Object objSource) {
    }

    public String getGroupId(Object obj) {
        XdbId id = (XdbId) obj;
        return Integer.toString(id.getXdbKey());
    }

    @Override
    public boolean cloneObjectForIndelOptimization(Object to, Object from) {
        return cloneObject(to, from);
    }

    public boolean cloneObject(Object to, Object from) {

        XdbId mdTo = (XdbId) to;
        XdbId mdFrom = (XdbId) from;

        mdTo.setRgdId(mdFrom.getRgdId());
        mdTo.setKey(mdFrom.getKey());
        mdTo.setNotes(mdFrom.getNotes()+", updated at "+new java.util.Date());

        return true;
    }
}
