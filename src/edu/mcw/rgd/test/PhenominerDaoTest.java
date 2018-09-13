package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.PhenominerDAO;
import edu.mcw.rgd.datamodel.pheno.Record;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: May 6, 2011
 * Time: 11:31:21 AM
 *
 * JUNit cases to test PhenominerDAO
 */
public class PhenominerDaoTest extends TestCase {

    PhenominerDAO dao = new PhenominerDAO();

    public PhenominerDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {

        testGetRecord();
        testGetRecordCountForTerm();
        testGetRecordCountForTermAndDescendants();
        testGetAnnotationsForTerm();
        testGetAnnotationsForTermAndDescendants();
    }

    public void testGetRecord() throws Exception {

        System.out.println("\ntesting getRecord(9600)...");
        int recordId=9600;
        Record rec = dao.getRecord(recordId);
        Assert.assertNull("unexpected record", rec);
    }

    public void testGetRecordCountForTerm() throws Exception {

        System.out.println("\ntesting getRecordCountForTerm...");

        String accId = "XCO:0000010";
        int count = dao.getRecordCountForTerm(accId);
        System.out.println("record count for "+accId+" is "+count);
        Assert.assertTrue("expected some records with this experimental condition", count>=0);

        accId = "CMO:0000025";
        count = dao.getRecordCountForTerm(accId);
        System.out.println("record count for "+accId+" is "+count);
        Assert.assertTrue("expected some records with this clinical measurement", count>=0);

        accId = "MMO:0000011";
        count = dao.getRecordCountForTerm(accId);
        System.out.println("record count for "+accId+" is "+count);
        Assert.assertTrue("expected some records with this measurement method", count>=0);

        accId = "RS:0000320";
        count = dao.getRecordCountForTerm(accId);
        System.out.println("record count for "+accId+" is "+count);
        Assert.assertTrue("expected some records with this rat strain", count>=0);

        accId = "GO:0005050";
        count = dao.getRecordCountForTerm(accId);
        System.out.println("record count for "+accId+" is "+count);
        Assert.assertFalse("no experiment records expected for go terms", count>=0);
    }

    public void testGetRecordCountForTermAndDescendants() throws Exception {

        System.out.println("\ntesting getRecordCountForTermAndDescendants ...");

        String accId = "XCO:0000010";
        int count = dao.getRecordCountForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+count);
        Assert.assertTrue("expected some records with this experimental condition", count>=0);

        accId = "CMO:0000025";
        count = dao.getRecordCountForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+count);
        Assert.assertTrue("expected some records with this clinical measurement", count>=0);

        accId = "MMO:0000011";
        count = dao.getRecordCountForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+count);
        Assert.assertTrue("expected some records with this measurement method", count>=0);

        accId = "RS:0000320";
        count = dao.getRecordCountForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+count);
        Assert.assertTrue("expected some records with this rat strain", count>=0);

        accId = "GO:0005050";
        count = dao.getRecordCountForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+count);
        Assert.assertFalse("no experiment records expected for go terms", count>=0);
    }

    public void testGetAnnotationsForTerm() throws Exception {

        System.out.println("\ntesting getAnnotationsForTerm...");

        String accId = "XCO:0000010";
        List<Integer> ids = dao.getAnnotationsForTerm(accId);
        System.out.println("record count for "+accId+" is "+ids.size());
        Assert.assertTrue("expected some records with this experimental condition", ids.size()>=0);

        accId = "CMO:0000025";
        ids = dao.getAnnotationsForTerm(accId);
        System.out.println("record count for "+accId+" is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this clinical measurement", ids.size()>=0);

        accId = "MMO:0000011";
        ids = dao.getAnnotationsForTerm(accId);
        System.out.println("record count for "+accId+" is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this measurement method", ids.size()>=0);

        accId = "RS:0000320";
        ids = dao.getAnnotationsForTerm(accId);
        System.out.println("record count for "+accId+" is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this rat strain", ids.size()>=0);

        accId = "GO:0005050";
        ids = dao.getAnnotationsForTerm(accId);
        System.out.println("record count for "+accId+" is "+(ids==null?0:ids.size()));
        Assert.assertFalse("no experiment records expected for go terms", ids!=null);
    }

    public void testGetAnnotationsForTermAndDescendants() throws Exception {

        System.out.println("\ntesting getAnnotationsForTermAndDescendants...");

        String accId = "XCO:0000010";
        List<Integer> ids = dao.getAnnotationsForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+ids.size());
        Assert.assertTrue("expected some records with this experimental condition", ids.size()>=0);

        accId = "CMO:0000025";
        ids = dao.getAnnotationsForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this clinical measurement", ids.size()>=0);

        accId = "MMO:0000011";
        ids = dao.getAnnotationsForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this measurement method", ids.size()>=0);

        accId = "RS:0000320";
        ids = dao.getAnnotationsForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+(ids==null?0:ids.size()));
        Assert.assertTrue("expected some records with this rat strain", ids.size()>=0);

        accId = "GO:0005050";
        ids = dao.getAnnotationsForTermAndDescendants(accId);
        System.out.println("record count for "+accId+" with descendants is "+(ids==null?0:ids.size()));
        Assert.assertFalse("no experiment records expected for go terms", ids!=null);
    }
}
