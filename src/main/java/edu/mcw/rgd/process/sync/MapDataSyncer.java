package edu.mcw.rgd.process.sync;

import edu.mcw.rgd.datamodel.MapData;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/19/12
 * Time: 2:33 PM
 * <p>
 *    Synchronizes positions between incoming data and data in RGD.
 *    New positions are added, updated or deleted if needed.
 *    One RGD object can have positions on multiple maps, each map identified by different MAP_KEY.
 *    Incoming positions are compared against existing positions in RGD and then updated until they are in sync.
 *    To avoid deletion of good data, the following rule is observed: only maps that appear in incoming data are synchronized.
 *    Optionally all database operations regarding gene positions could be logged into log4j file.
 * </p>
 */
public class MapDataSyncer extends RgdObjectSyncer {

    @Override
    public boolean objectsAreEqualByUniqueKey(Object obj1, Object obj2) {
        MapData md1 = (MapData) obj1;
        MapData md2 = (MapData) obj2;

        return md1.equalsByGenomicCoords(md2);
    }

    @Override
    public boolean objectsAreEqualByContent(Object obj1, Object obj2) {
        return objectsAreEqualByUniqueKey(obj1, obj2);
    }

    @Override
    public void updateContentForObjectsEqualByUniqueKey(Object objDest, Object objSource) {
    }

    @Override
    public String getGroupId(Object obj) {
        MapData md = (MapData) obj;
        return md.getMapKey().toString();
    }

    @Override
    public boolean cloneObjectForIndelOptimization(Object to, Object from) {
        MapData mdTo = (MapData) to;
        MapData mdFrom = (MapData) from;

        mdTo.setRgdId(mdFrom.getRgdId());
        mdTo.setKey(mdFrom.getKey());
        mdTo.setNotes(mdFrom.getNotes()+", updated at "+new java.util.Date());

        return true;
    }
}
