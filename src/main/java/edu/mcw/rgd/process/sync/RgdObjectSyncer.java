package edu.mcw.rgd.process.sync;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.datamodel.Identifiable;
import edu.mcw.rgd.process.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/3/12
 * Time: 3:26 PM
 * Synces two sets of data: incoming and in rgd. Synced data can be grouped.
 * For example, SSLPS can have multiple positions on multiple assemblies. When grouping by MAP_KEY
 * will be implemented, then positions on every map will be synced independently.
 */
abstract public class RgdObjectSyncer {

    Log log;

    private String logMessagePrefix;

    private List objsIncoming = new ArrayList();
    private List objsInRgd;

    // list with results after running QC:
    List objsMatching = new ArrayList();
    List objsForUpdate = new ArrayList();
    List objsForInsert = new ArrayList();
    List objsForDelete = new ArrayList();

    public List getIncomingObjects() {
        return objsIncoming;
    }

    public List getObjectsInRgd() {
        return objsInRgd;
    }

    public List getMatchingObjects() {
        return objsMatching;
    }

    public List getObjectsForUpdate() {
        return objsForUpdate;
    }

    public List getObjectsForInsert() {
        return objsForInsert;
    }

    public List getObjectsForDelete() {
        return objsForDelete;
    }

    abstract public boolean objectsAreEqualByUniqueKey(Object obj1, Object obj2);

    abstract public boolean objectsAreEqualByContent(Object obj1, Object obj2);

    abstract public void updateContentForObjectsEqualByUniqueKey(Object objDest, Object objSource);

    abstract public String getGroupId(Object obj);

    abstract public boolean cloneObjectForIndelOptimization(Object to, Object from);


    public int addIncomingObjects(Collection objs) {

        int addCount = 0;
        if( objs!=null ) {
            for( Object obj: objs ) {
                if( addIncomingObject(obj) )
                    addCount++;
            }
        }
        return addCount;
    }

    /**
     * add a new incoming object;
     * duplicate objects won't be added (object comparator is called to determine of objects are equal)
     * @param obj new object to be added
     * @return true if an object has been added; false if it was a duplicate
     */
    public boolean addIncomingObject(Object obj) {

        if( obj==null ) {
            return false;
        }

        for( Object obj1: objsIncoming ) {
            // look for duplicates
            if( objectsAreEqualByUniqueKey(obj, obj1) ) {
                // duplicate found
                return false;
            }
        }

        // no duplicates -- add new incoming object
        objsIncoming.add(obj);
        return true;
    }

    /**
     * set rgd id for all incoming objects
     * @param rgdId rgd id to set
     */
    public void setRgdId(int rgdId) {
        for( Object obj: objsIncoming ) {
            Identifiable ido = (Identifiable) obj;
            ido.setRgdId(rgdId);
        }
    }

    public void setObjectsInRgd(List objsInRgd) throws Exception  {

        this.objsInRgd = objsInRgd;
    }

    /**
     * qc map data between rgd and incoming data; only maps that are present in incoming data are qc-ed
     * unless parameter 'deleteStaleObjects' is set to true
     */
    public void qc(String logName, boolean deleteStaleObjects) throws Exception {

        if( logName!=null && !logName.isEmpty() ) {
            log = LogFactory.getLog(logName);
        }

        // make a copy of both data in RGD and incoming data
        List objsIncomingCopy = new ArrayList(objsIncoming);
        List objsInRgdCopy = objsInRgd!=null ? new ArrayList(objsInRgd) : new ArrayList();

        // initialize result collections
        objsMatching.clear();
        objsForDelete.clear();
        objsForInsert.clear();
        objsForUpdate.clear();

        // synchronize every map present in incoming data
        while( !objsIncomingCopy.isEmpty() ) {
            // get map positions for one map
            String groupId = getGroupId(objsIncomingCopy.get(0));

            List objsIncoming = detachGroup(groupId, objsIncomingCopy);
            List objsInRgd = detachGroup(groupId, objsInRgdCopy);
            List objsForInsert = new ArrayList();

            // process all positions for incoming data
            for( Object obj: objsIncoming ) {
                // check if position matches exactly by coordinates a position in RGD
                Object objMatch = detachMatchingObject(obj, objsInRgd);
                if( objMatch!=null ) {
                    // objects do match by unique key -- do they match by content (more restrictive)
                    if( !objectsAreEqualByContent(obj, objMatch) ) {
                        updateContentForObjectsEqualByUniqueKey(objMatch, obj);
                        objsForUpdate.add(objMatch);
                    }
                    else
                        objsMatching.add(objMatch);
                }
                else {
                    objsForInsert.add(obj);
                }
            }

            // whatever is left, goes into insert or delete arrays
            if( !objsForInsert.isEmpty() )
                this.objsForInsert.addAll(objsForInsert);
            if( !objsInRgd.isEmpty() )
                this.objsForDelete.addAll(objsInRgd);
        }

        // optionally delete all objects that had not been processed by qc so far
        if( deleteStaleObjects ) {
            this.objsForDelete.addAll(objsInRgdCopy);
        }

        // so far our logic determined:
        // 1. matching positions, that landed in mdMatching array
        // 2. to be inserted positions, that landed in mdsForInsert array
        // 3. to be deleted positions, that are found in mdsRgd array
        //
        optimizeInDels();

        qcLog();
    }

    /**
     * qc map data between rgd and incoming data; only maps that are present in incoming data are qc-ed
     */
    public void qc(String logName) throws Exception {

        qc(logName, false);
    }

    // to minimize inserts/delete operations, we can replace one insert/delete pair with an update operation
    private int optimizeInDels() {

        int count = 0; // count of optimized indels
        while( !objsForInsert.isEmpty() && !objsForDelete.isEmpty() ) {
            Object objInsert = objsForInsert.get(0);
            Object objDelete = objsForDelete.get(0);
            writeLogMessage("OPTIMIZE_INDELS_INSERT|",(Dumpable)objInsert);
            writeLogMessage("OPTIMIZE_INDELS_DELETE|",(Dumpable)objDelete);

            // if object cannot be cloned, stop indel optimizations
            if( !cloneObjectForIndelOptimization(objInsert, objDelete) )
                return count;

            objsForUpdate.add(objInsert);
            writeLogMessage("OPTIMIZE_INDELS_UPDATE|", (Dumpable) objInsert);

            objsForInsert.remove(0);
            objsForDelete.remove(0);
        }
        return count;
    }

    private void qcLog() {

        if( log==null )
            return;

        for( Object obj: objsForUpdate ) {
            writeLogMessage("UPDATE_NEW ", (Dumpable)obj);
        }

        for( Object obj: objsForInsert ) {
            writeLogMessage("INSERT ", (Dumpable)obj);
        }

        for( Object obj: objsForDelete ) {
            writeLogMessage("DELETE ", (Dumpable)obj);
        }
    }

    /**
     * detach from list of MapData object a subset matching given map key
     * @param groupId id group id
     * @param objs List of objects
     * @return List<MapData>
     */
    private List detachGroup(String groupId, List objs) {

        List results = new ArrayList(objs.size());

        Iterator it = objs.iterator();
        while( it.hasNext() ) {
            Object obj = it.next();
            if(Utils.stringsAreEqual(getGroupId(obj), groupId) ) {
                it.remove();
                results.add(obj);
            }
        }
        return results;
    }

    /**
     * detach from list of objects a single matching object
     * @param obj Object to match to
     * @param list of objects to match from
     * @return Object
     */
    private Object detachMatchingObject(Object obj, List list) {

        Iterator it = list.iterator();
        while( it.hasNext() ) {
            Object obj2 = it.next();
            if( objectsAreEqualByUniqueKey(obj, obj2) ) {
                it.remove();
                return obj2;
            }
        }
        return null;
    }

    /**
     * get log message prefix prepended to text of every message in the log
     * @return log message prefix; could be null
     */
    public String getLogMessagePrefix() {
        return logMessagePrefix;
    }

    /**
     * set log message prefix prepended to text of every message in the log
     * @param logMessagePrefix new value of log message prefix
     */
    public void setLogMessagePrefix(String logMessagePrefix) {
        this.logMessagePrefix = logMessagePrefix;
    }

    /**
     * writes a message into a log
     * <p>
     * Note: if logMessagePrefix is set, it will be written first, then the label followed by
     *  representation of dumpable object
     * @param label text prepending the message
     * @param obj Dumpable object which will be serialized into a log message
     */
    public void writeLogMessage(String label, Dumpable obj) {
        if( log!=null ) {
            log.info((getLogMessagePrefix()!=null ? getLogMessagePrefix() : "")
                    +label
                    +obj.dump("|"));
        }
    }
}
