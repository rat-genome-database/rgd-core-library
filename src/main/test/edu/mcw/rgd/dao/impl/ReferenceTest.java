package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.ReferenceDAO;
import edu.mcw.rgd.datamodel.Reference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 13, 2008
 * Time: 1:45:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceTest extends TestCase {

    Reference ref = new Reference();
    ReferenceDAO rdao = new ReferenceDAO();

    public ReferenceTest(String testName) {
            super(testName);
    }

    public void testAll() throws Exception {

        testReferenceUpdate();
        testReferenceSearch();
        testReferencebyDOI();
    }

    private void testReferencebyDOI() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
        String doi = "10.1172/JCI118244";
        ref = rdao.getReferenceByDOI(doi);
        System.out.println("get ref rgdid : " + ref.getRgdId());

        int refRgdId = rdao.getReferenceRgdIdByDOI(doi);
        System.out.println("get ref rgdid : " + refRgdId);
        assertEquals(refRgdId, ref.getRgdId());

        refRgdId = rdao.getReferenceRgdIdByDOI("not_existing_doi");
        System.out.println("get ref rgdid : " + refRgdId);
        assertEquals(refRgdId, 0);
    }

    private void testReferenceSearch() throws Exception{


        int rgdid = 5720097;
        //ref = rdao.getReference(rgdid);
        //System.out.println("this is the doi:" + ref.getDoi());

        rgdid = 2004;
        List<Reference> refs = rdao.getReferencesForObject(rgdid);
        int refCount = rdao.getReferenceCountForObject(rgdid);
        List<Integer> refRgdIds = rdao.getReferenceRgdIdsForObject(rgdid);

        assertEquals(refs.size(), refCount);
        assertEquals(refRgdIds.size(), refCount);
    }

    private void testReferenceUpdate() throws Exception{
        int rgdid = 61696;
        ref = rdao.getReference(rgdid);
        ref.setDoi("NULL2");
        rdao.updateReference(ref);
        ref.setDoi("NULXL");
        ref.setRgdId(2004); // unique rgd id
        rdao.insertReference(ref);
    }


}
