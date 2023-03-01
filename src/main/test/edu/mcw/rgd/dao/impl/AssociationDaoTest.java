package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.AssociationDAO;
import edu.mcw.rgd.datamodel.Association;
import edu.mcw.rgd.datamodel.GenomicElement;
import edu.mcw.rgd.datamodel.Identifiable;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.List;


/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 10/27/11 <br>
 * Time: 2:27 PM <br>
 * <p/>
 * to test association dao methods
 */
public class AssociationDaoTest extends TestCase {

    AssociationDAO dao = new AssociationDAO();

    public AssociationDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {

        testReference();
        testStrainAssoc();
        testInsUpdDel(dao);
    }

    public void testReference() throws Exception {

        int refRgdId = 1303940;
        List<Integer> list1 = dao.getObjectsAssociatedWithReference(refRgdId);
        System.out.println("objects associated with reference: "+list1.size());
        List<GenomicElement> list2 = dao.getElementsAssociatedWithReference(refRgdId);
        System.out.println("elements associated with reference: "+list2.size());
        Assert.assertEquals(list1.size(), list2.size());
    }

    public void testStrainAssoc() throws Exception {
        List<Identifiable> list = dao.getStrainAssociations(1579681);
        for( Identifiable i: list ) {
            System.out.println(i.getRgdId());
        }
    }

    public void testInsUpdDel(AssociationDAO dao) throws Exception {

        Association assoc = new Association();
        assoc.setAssocType("tEst");
        assoc.setSrcPipeline("test");
        assoc.setMasterRgdId(3009);
        assoc.setDetailRgdId(3016);
        int assocKey = dao.insertAssociation(assoc);
        assertTrue(assocKey>0);

        assoc.setDetailRgdId(3013);
        assoc.setAssocSubType("SUB-TYPE");
        int rows = dao.updateAssociation(assoc);
        assertTrue(rows==1);

        List<Association> assocList = dao.getAssociationsForMasterRgdId(3008);
        for( Association a: assocList ) {
            if( a.getAssocType().equals("test") ) {
                if( a.getAssocKey()==assocKey )
                    assertTrue(assoc.equals(a));
                else
                    assertFalse(assoc.equals(a));
            }
        }

        assocList = dao.getAssociationsByType("TEST");
        for( Association a: assocList ) {
            assertTrue(a.getAssocType().equals("test"));

            if( a.getAssocKey()==assocKey )
                assertTrue(assoc.equals(a));
            else
                assertFalse(assoc.equals(a));

            rows = dao.deleteAssociationByKey(a.getAssocKey());
            assertTrue(rows==1);
        }
    }

}
