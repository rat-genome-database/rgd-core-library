package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.StrainDAO;
import edu.mcw.rgd.datamodel.Strain;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Jun 22, 2011
 * Time: 12:12:44 PM
 */
public class StrainDaoTest extends TestCase {
    StrainDAO stDao = new StrainDAO();
    

    public StrainDaoTest(String testName){
        super(testName);
    }

    public void testAll() throws Exception{
        testGetStrains();
        

    }

    private List<Strain> testGetStrains() throws Exception {
        List<Strain> strainList = stDao.getMappedStrains(60);

        return strainList;
    }


}
