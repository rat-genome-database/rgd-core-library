package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.GeneDAO;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 7/9/12
 */

public class NomenDaoTest extends TestCase{

    GeneDAO geneDAO = new GeneDAO();

    public static final Date TODAY = new Date();
    public static final Date UNTOUCHABLE = new GregorianCalendar(2200,9,1).getTime(); // Oct 1, 2200
    public static final Date REVIEWABLE = new GregorianCalendar(2100,9,1).getTime();  // Oct 1, 2100
    public static final Date ONEYEAR = addOneYear(TODAY).getTime();
    public static final Date START = new GregorianCalendar(1974,9,1).getTime();  // Oct 1, 1974
    public static final Date TOMORROW = getTomorrow(TODAY).getTime();

    private static GregorianCalendar addOneYear(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR,1);
        return gc;
    }

    private static GregorianCalendar getTomorrow(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_YEAR,1);
        return gc;
    }


    public void testAll() throws Exception{
        testNomenInterface();
    }

    private void testNomenInterface() throws Exception{

        // show genes with new nomenclature
        Date from = START;
        Date to = TODAY;
        int count1 = geneDAO.getActiveGeneCount(3, from, to);
        int count2 = geneDAO.getActiveGenes(3, from, to).size();
        assertEquals(count1, count2);
        System.out.println("Genes with new nomenclature: "+count1);

        // show genes with no good ortholog or no change
        from = REVIEWABLE;
        to = REVIEWABLE;
        count1 = geneDAO.getActiveGeneCount(3, from, to);
        count2 = geneDAO.getActiveGenes(3, from, to).size();
        assertEquals(count1, count2);
        System.out.println("Genes with no good ortholog or no change: "+count1);

        // show genes with untouchable nomenclature
        from = UNTOUCHABLE;
        to = UNTOUCHABLE;
        count1 = geneDAO.getActiveGeneCount(3, from, to);
        count2 = geneDAO.getActiveGenes(3, from, to).size();
        assertEquals(count1, count2);
        System.out.println("Genes with untouchable nomenclature: "+count1);

        // show genes set for review in next year
        from = TOMORROW;
        to = ONEYEAR;
        count1 = geneDAO.getActiveGeneCount(3, from, to);
        count2 = geneDAO.getActiveGenes(3, from, to).size();
        assertEquals(count1, count2);
        System.out.println("Genes set for review in next year: "+count1);
    }
}
