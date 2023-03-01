package edu.mcw.rgd.reporting;

import java.util.Iterator;
import java.beans.XMLEncoder;
import java.net.URLEncoder;

/**
 * ReportStrategy implementation for creating delimited reports
 */
public class GViewerReportStrategy implements ReportStrategy {

    /**
     * Formats this report and returns the result as a string
     * @param report
     * @return
     * @throws Exception
     */
    public String format(Report report) throws Exception{
        StringBuilder buf = new StringBuilder();
        Iterator reportIt = report.iterator();

        buf.append("<?xml version=\"1.0\" standalone=\"yes\"?><genome>");

        //skip the header
        if (report.records.size() > 0) {
            reportIt.next();
        }

        while (reportIt.hasNext()) {
            Record rec = (Record) reportIt.next();
            buf.append("<feature>");
            buf.append("<chromosome>" + rec.get(report.getIndex("Chr")) + "</chromosome>");
            buf.append("<start>" + rec.get(report.getIndex("Start")) + "</start>");
            buf.append("<end>" + rec.get(report.getIndex("Stop")) + "</end>");
            buf.append("<type>" + rec.get(report.getIndex("Type")) + "</type>");
            buf.append("<label>" + URLEncoder.encode(rec.get(report.getIndex("Symbol"))) + "</label>");
            buf.append("<color>blue</color>");
            buf.append("<link>" + Link.it(rec.get(report.getIndex("RGD ID"))) + "</link>");
            buf.append("</feature>");
        }
        buf.append("</genome>");
        return buf.toString();
    }
}
