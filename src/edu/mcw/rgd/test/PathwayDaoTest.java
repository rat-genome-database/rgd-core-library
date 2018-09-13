package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.PathwayDAO;
import edu.mcw.rgd.dao.impl.ReferenceDAO;
import edu.mcw.rgd.datamodel.Pathway;
import edu.mcw.rgd.datamodel.Reference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Jun 22, 2011
 * Time: 12:12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayDaoTest extends TestCase {
    PathwayDAO pwDao = new PathwayDAO();
    ReferenceDAO refdao = new ReferenceDAO();

    public PathwayDaoTest(String testName){
        super(testName);
    }

    public void testAll() throws Exception{
        testInsertPathwayInfo();
        //testGetPathwayInfo();
        //testGetObjectTypeName();
        //testgetPwIds();

    }

    private void testInsertPathwayInfo() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
        Pathway pwObject = new Pathway();
        List<Reference> refList = refdao.getReferencesForObject(2004);

        pwObject.setId("PW:0000835");
        pwObject.setName("newPathway");
        pwObject.setDescription("XXXXXXXXXXXXXXXX");
        pwObject.setHasAlteredPath("N");
        pwObject.setObjectList(null);
        pwObject.setReferenceList(refList);

        pwDao.insertPathwayData(pwObject);

        System.out.println("done!!");
    }

    public void testGetPathwayInfo() throws Exception {
        String acc_id = "PW:0000564";
        Pathway pw = pwDao.getPathwayInfo(acc_id);
        System.out.println(pw.getName());
    }

    public void testgetPwIds() throws Exception {
        //To change body of created methods use File | Settings | File Templates.

        List<String> pwids = pwDao.getPathwayList();
    }

    public void testGetObjectTypeName() throws Exception{

        int obj_type_id = 11;

        List name = pwDao.getObjectTypeName(obj_type_id);
        assertEquals("Gene", name.get(0));
    }
}
