package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.ProteinDAO;
import edu.mcw.rgd.dao.impl.SampleDAO;
import edu.mcw.rgd.dao.impl.TranscriptDAO;
import edu.mcw.rgd.datamodel.Protein;
import edu.mcw.rgd.datamodel.Sample;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.reporting.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 9, 2008
 */
public class Playground {

    public static void main (String[] args) throws Exception{

        ProteinDAO pdao = new ProteinDAO();
        List<String> accIds = new ArrayList<>();
        accIds.add("a0A0g2k562");
        accIds.add("ADA10_rat");
        List<Protein> p = pdao.getProteinListByUniProtIdOrSymbol(accIds, 3);
        System.out.println(p.size());

        TranscriptDAO tdao = new TranscriptDAO();
        tdao.deleteTranscript(12269125, 13284682);

        SampleDAO sampleDAO = new SampleDAO();
        sampleDAO.setDataSource(DataSourceFactory.getInstance().getCarpeNovoDataSource());
        List<Sample> samples = sampleDAO.getSamples(180);

        Report report = new Report();
        Record rec = new Record();
        rec.append("hello to all");
        rec.append("goodbye all");
        rec.append("I am here");

        report.append(rec);
        /*
        SearchBean search = new SearchBean();
        search.parse("hello all");
        SearchReportStrategy strat = new SearchReportStrategy();
        strat.setHighlightedTerms(search.getRequired());

        System.out.println(report.format(strat));
        */
        }


        

    }


