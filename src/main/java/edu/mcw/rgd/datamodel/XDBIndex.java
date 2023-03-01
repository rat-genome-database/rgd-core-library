package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.impl.XdbIdDAO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 11/9/11
 * Time: 10:34 AM
 */
public class XDBIndex {

    private static XDBIndex index = new XDBIndex();
    private HashMap xdbs;


    private XDBIndex() {

    }

    synchronized public static XDBIndex getInstance() throws Exception {

        if (index.xdbs == null) {
            index.xdbs=new HashMap();
            XdbIdDAO dao = new XdbIdDAO();
            List<Xdb> list = dao.getXdbs();
            for (Xdb x: list) {
                index.xdbs.put(x.getKey(), x);
            }
        }

        return index;
    }


    public Xdb getXDB(int key) {
        return (Xdb) this.xdbs.get(key);

    }


}
