package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.OrthologDAO;
import edu.mcw.rgd.datamodel.Ortholog;
import edu.mcw.rgd.datamodel.SpeciesType;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Jun 1, 2011
 * Time: 12:53:40 PM
 */
public class OrthologDaoTest extends TestCase {

    OrthologDAO dao = new OrthologDAO();

    public OrthologDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {

        test1();
        testGetAllOrthologs();
    }

    public void test1() throws Exception {

        int srcRgdId = 1316123;
        int destRgdId = 1316122;
        System.out.println("areGenesOrthologous("+srcRgdId+","+destRgdId+"):"+dao.areGenesOrthologous(srcRgdId, destRgdId));

        destRgdId = 0;
        System.out.println("areGenesOrthologous("+srcRgdId+","+destRgdId+"):"+dao.areGenesOrthologous(srcRgdId, destRgdId));
    }

    public void testGetAllOrthologs() throws Exception {

        List<Ortholog> orthologs = dao.getAllOrthologs(SpeciesType.RAT);
        System.out.println("ortholog entries for rat: "+orthologs.size());
        orthologs = dao.getAllOrthologs(SpeciesType.MOUSE);
        System.out.println("ortholog entries for mouse: "+orthologs.size());
        orthologs = dao.getAllOrthologs(SpeciesType.HUMAN);
        System.out.println("ortholog entries for human: "+orthologs.size());
    }
}
