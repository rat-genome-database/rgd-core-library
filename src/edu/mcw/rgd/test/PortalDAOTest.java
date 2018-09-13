package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.PortalDAO;
import edu.mcw.rgd.datamodel.Portal;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 11/29/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalDAOTest extends TestCase{
    PortalDAO portdao = new PortalDAO();

    public void testAll() throws Exception{
        testGetPortalsList();
    }

    private List<Portal> testGetPortalsList() throws Exception{
        List<Portal> portalsList = portdao.getPortals(2306893);
        System.out.println("portals size is: " + portalsList);
        return portalsList;
    }
}
