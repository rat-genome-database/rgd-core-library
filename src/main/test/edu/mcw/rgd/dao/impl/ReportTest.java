package edu.mcw.rgd.test;

import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.search.ReportFactory;
import edu.mcw.rgd.process.search.SearchBean;
import edu.mcw.rgd.reporting.Report;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 11/30/11
 * Time: 10:37 AM
 */
public class ReportTest extends TestCase {

    public ReportTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception{
        testGetQtlReport();
        testGetReferenceReport();
    }

    public void testGetQtlReport() throws Exception{

        SearchBean bean = new SearchBean();
        ReportFactory factory;
        Report r;

        bean = new SearchBean();
        bean.setMap(60);
        bean.setSpeciesType(SpeciesType.RAT);
        //bean.setTerm("");
        //bean.setChr("12");
        //bean.setStart(14365649);
        //bean.setStop (14511170);
        factory = ReportFactory.getInstance();
        r = factory.getGeneReport(bean);
        System.out.println("OK");


        bean = new SearchBean();
        bean.setMap(60);
        bean.setSpeciesType(SpeciesType.RAT);
        bean.setTerm("jacob");
        //bean.setChr("18");
        factory = ReportFactory.getInstance();
        r = factory.getQTLReport(bean);
        System.out.println("OK");

        bean = new SearchBean();
        bean.setMap(60);
        bean.setSpeciesType(SpeciesType.RAT);
        bean.setTerm("lepr");
        factory = ReportFactory.getInstance();
        r = factory.getGeneReport(bean);
        System.out.println("OK");

        bean = new SearchBean();
        bean.setMap(60);
        bean.setSpeciesType(SpeciesType.RAT);
        //bean.setTerm("cbp");
        bean.setTerm("shr");
        factory = ReportFactory.getInstance();
        r = factory.getStrainReport(bean);
        r.sort(0, 0, Report.DECENDING_SORT, true, 2);

        bean = new SearchBean();
        bean.setMap(60);
        bean.setSpeciesType(SpeciesType.RAT);
        bean.setTermAccId2("VT:0001758");
        bean.setTerm("");
        factory = ReportFactory.getInstance();
        r = factory.getQTLReport(bean);
        System.out.println("OK");

        bean = new SearchBean();
        bean.setTerm("blood");
        bean.setMap(18);
        bean.setSpeciesType(SpeciesType.MOUSE);

        factory = ReportFactory.getInstance();
        r = factory.getQTLReport(bean);
        System.out.println("OK");
    }

    public void testGetReferenceReport() throws Exception{

        SearchBean bean = new SearchBean();
        //bean.setTerm("electronic transfer of est data"); // title of one of the references
        bean.setTerm("smith 2007");

        ReportFactory factory = ReportFactory.getInstance();
        Report r = factory.getReferenceReport(bean);
        System.out.println(r.toString());
        System.out.println("OK");
    }
}