package edu.mcw.rgd.test;

import junit.framework.TestCase;

import java.util.List;

import edu.mcw.rgd.dao.impl.NomenclatureDAO;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 31, 2007
 * Time: 10:05:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpringTest extends TestCase {

    public SpringTest(String testName) {
        super(testName);
    }

    public void testGetNomenclatureEvents() throws Exception{
       NomenclatureDAO dao = new NomenclatureDAO();
        List list = dao.getNomenclatureEvents(3172);        
        System.out.println(list.size());
    }



}
