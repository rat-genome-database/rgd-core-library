package edu.mcw.rgd.process.sync;

import edu.mcw.rgd.datamodel.Association;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.sync.RgdObjectSyncer;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 5/21/13
 * Time: 3:25 PM
 * <p>
 * Synchronizes associations between incoming data and data in RGD.
 * New associations are added, updated or deleted if needed.
 * One RGD object can have associations for multiple types.
 * To avoid deletion of good data, the following rule is observed: only types that appear in incoming data are synchronized.
 * Optionally all database operations regarding associations could be logged into log4j file.
 * </p>
 */
public class AssociationSyncer extends RgdObjectSyncer {

    public boolean objectsAreEqualByUniqueKey(Object obj1, Object obj2) {

        Association x1 = (Association) obj1;
        Association x2 = (Association) obj2;

        return x1.equals(x2);
    }

    public boolean objectsAreEqualByContent(Object obj1, Object obj2) {

        Association x1 = (Association) obj1;
        Association x2 = (Association) obj2;

        return objectsAreEqualByUniqueKey(obj1, obj2) &&
            Utils.stringsAreEqualIgnoreCase(x1.getAssocSubType(), x2.getAssocSubType());
    }

    public void updateContentForObjectsEqualByUniqueKey(Object objDest, Object objSource) {

        Association x1 = (Association) objDest;
        Association x2 = (Association) objSource;

        x1.setAssocSubType(x2.getAssocSubType());
    }

    public String getGroupId(Object obj) {
        Association x = (Association) obj;
        return Integer.toString(x.getMasterRgdId());
    }

    public boolean cloneObjectForIndelOptimization(Object objInsert, Object objDelete) {

        Association ins = (Association) objInsert;
        Association del = (Association) objDelete;

        ins.setAssocKey(del.getAssocKey());

        return true;
    }
}
