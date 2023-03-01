package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.XdbIdDAO;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.Xdb;
import edu.mcw.rgd.datamodel.XdbId;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 7/1/11 <br>
 * Time: 12:09 PM <br>
 * <p/>
 * Test cases for XdbIdDAO
 */
public class XdbIdDaoTest extends TestCase {

    XdbIdDAO dao = new XdbIdDAO();

    public XdbIdDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {

        testXdb();
        testGetPubmedIds();
    }

    public void testXdb() throws Exception {

        Date dt = new GregorianCalendar(2013, 4, 5, 16, 30).getTime();
        String srcPipeline = "UniProtKB/TrEMBL";
        int speciesTypeKey = SpeciesType.ALL;
        List<XdbId> xdbIds = dao.getXdbIdsModifiedBefore(dt, srcPipeline, speciesTypeKey);
        System.out.println("XdbIds for ALL species modified before Apr 07, 2013:"+ xdbIds.size());

        speciesTypeKey = SpeciesType.RAT;
        xdbIds = dao.getXdbIdsModifiedBefore(dt, srcPipeline, speciesTypeKey);
        System.out.println("XdbIds for RAT species modified before Apr 07, 2013:"+ xdbIds.size());

        String s = dao.getXdbUrl(XdbId.XDB_KEY_HGNC, 1);
        Assert.assertNotNull(s);
        s = dao.getXdbUrl(-33, -33);
        Assert.assertNull(s);

        List<Xdb> xdbs = dao.getXdbs();
        System.out.println("Count of all Xdb Names: "+xdbs.size());
        List<Xdb> activeXdbs = dao.getActiveXdbs();
        System.out.println("Count of active Xdb Names: "+activeXdbs.size());
        Assert.assertTrue(activeXdbs.size() <= xdbs.size());
        List<Xdb> inactiveXdbs = dao.getInactiveXdbs();
        System.out.println("Count of inactive Xdb Names: "+inactiveXdbs.size());
        Assert.assertTrue(inactiveXdbs.size() <= xdbs.size());
        Assert.assertTrue(activeXdbs.size()+inactiveXdbs.size() == xdbs.size());
    }

    /**
     * test for getCuratedPubmedIds(int rgdId) and getUncuratedPubmedIds(int rgdId)
     * @throws Exception
     */
    public void testGetPubmedIds() throws Exception {
        int rgdId = 69417;
        List<XdbId> xdbIds = dao.getUncuratedPubmedIds(rgdId);
        System.out.println("uncurated pubmed ids for rgd_id="+rgdId);
        for( XdbId xdbId :  xdbIds ) {
            System.out.print(" "+xdbId.getAccId());
        }
        System.out.println();

        xdbIds = dao.getCuratedPubmedIds(rgdId);
        System.out.println("curated pubmed ids for rgd_id="+rgdId);
        for( XdbId xdbId :  xdbIds ) {
            System.out.print(" "+xdbId.getAccId());
        }
        System.out.println();
    }
}
