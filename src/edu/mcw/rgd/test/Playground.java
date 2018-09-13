package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.SampleDAO;
import edu.mcw.rgd.datamodel.Sample;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.reporting.Report;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 9, 2008
 * Time: 9:15:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class Playground {

    public static void main (String[] args) throws Exception{

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


