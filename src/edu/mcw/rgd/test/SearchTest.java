package edu.mcw.rgd.test;

import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.search.GeneralSearchResult;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.search.ReportFactory;
import edu.mcw.rgd.reporting.Report;
import edu.mcw.rgd.reporting.SearchReportStrategy;
import junit.framework.TestCase;
import edu.mcw.rgd.dao.impl.SearchDAO;
import edu.mcw.rgd.dao.impl.GeneDAO;
import edu.mcw.rgd.process.search.SearchBean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 13, 2008
 * Time: 1:45:17 PM
 */
public class SearchTest extends TestCase {

    public SearchTest(String testName) {
            super(testName);
    }

    public void testAll() throws Exception {
        testGeneralSearch();
        testOntologySearch();
        testGeneralSearchForGenomicElements();
        testQtlSearch();
        testGeneralSearchForVariants();
        testDeleteFromIndex();
    }

    public void testQtlSearch() throws Exception {

        SearchBean search = new SearchBean();
        String term = "blood pressure";
        search.setTerm(term);
        search.setMap(60);
        search.setSpeciesType(3);
        //search.setObj("qtl");
        Report r = ReportFactory.getInstance().getQTLReport(search);
        int header = r.getIndex("1");
    }

    public void testGeneralSearchForVariants() throws Exception {

        SearchBean search = new SearchBean();
        String term = "duchenne";
        search.setTerm(term);
        search.setSpeciesType(1);
        Report result = ReportFactory.getInstance().getVariantReport(search);
        System.out.println("search term: "+term);
    }

    public void testGeneralSearchForGenomicElements() throws Exception {

        SearchBean search = new SearchBean();
        String term = "\"Dac2\""; // cell line
        search.setTerm(term);
        GeneralSearchResult result = ReportFactory.getInstance().execute(search);
        System.out.println("search term: "+term);
        for( int i=1; i<4; i++ ) {
            System.out.println("genomic_elements for species "+i+" "+result.getHitCount(RgdId.OBJECT_KEY_CELL_LINES, i));
        }

        term = "\"HG_KWN:425\"";
        search.setTerm(term);
        result = ReportFactory.getInstance().execute(search);
        System.out.println("search term: "+term);
        for( int i=1; i<4; i++ ) {
            System.out.println("genomic_elements for species "+i+" "+result.getHitCount(RgdId.OBJECT_KEY_PROMOTERS, i));
        }

        term = "HG_KWN:425";
        search = new SearchBean();
        search.setTerm(term);
        result = ReportFactory.getInstance().execute(search);
        System.out.println("search term: "+term);
        for( int i=1; i<4; i++ ) {
            System.out.println("genomic_elements for species "+i+" "+result.getHitCount(RgdId.OBJECT_KEY_PROMOTERS, i));
        }

        Report report = ReportFactory.getInstance().getGenomicElementReport(search);
        SearchReportStrategy strat = new SearchReportStrategy();
        //strat.hideColumn(0);
        strat.setTableProperties("border='0' cellpadding='2' cellspacing='2' width=100%");
        strat.setHighlightedTerms(search.getRequired());
        System.out.println(report.format(strat));
    }

    public void testOntologySearch() throws Exception {

        String term = "islet";

        SearchBean search = new SearchBean();
        search.setTerm("Woolly Hair, Autosomal Recessive");
        GeneralSearchResult result = ReportFactory.getInstance().searchOntologies(search);
        System.out.println(result.ontTermHits);
        System.out.println(result.ontologyTerms);

        term = "NM_001107539 NM_001130577 NM_001172305 NintendoÃ¢Â\u0080Â\u0099s Wii U console has officially sold more than 10 million units worldwide and it likely has its new ink-based shooter Splatoon to thank for the milestone.";
        // break term into words
        search = new SearchBean();
        search.setTerm(term);

        // setTerm is filtering out any common words so you can end having no terms to search;
        // but, if term, being one of common words, is quoted, it won't be checked against common word list :-)
        if( search.getRequired().isEmpty()  &&  search.getNegated().isEmpty() ) {
            search.setTerm("\""+term+"\"");
        }

        // get search result for the words
        search.setTerm("deafness");
        result = ReportFactory.getInstance().searchOntologies(search);
        System.out.println(result.ontTermHits);
        System.out.println(result.ontologyTerms);
    }

    public void testDeleteFromIndex() throws Exception {

        // we are deleting aliases for mgc_clone_id
        SearchDAO sdao = new SearchDAO();
        System.out.println("deleting GENES mgc_clone_id: " + sdao.deleteFromIndex("GENES", "mgc_clone_id"));
    }

    public void testGeneralSearch() throws Exception {

        SearchBean search = new SearchBean();
        //String term = "\"tyrosine kinase 2\"";
        //String term = "a2m";
        //String term = "gUwm54-14";
        //String term = "Ear's";
        //String term = "(-)-noscapine";
        //String term = "%ephedrine";
        String term = "smith 2007";
        search.setTerm(term);

        GeneralSearchResult result = ReportFactory.getInstance().execute(search);
        System.out.println(result.getHitCount(RgdId.OBJECT_KEY_GENES, 3));

        Report report = ReportFactory.getInstance().getGeneReport(search);


        SearchReportStrategy strat = new SearchReportStrategy();
        strat.hideColumn(0);
        strat.setTableProperties("border='0' cellpadding='2' cellspacing='2' width=100%");
        strat.setHighlightedTerms(search.getRequired());

        System.out.println(report.format(strat));
    }
}
