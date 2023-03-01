package edu.mcw.rgd.reporting;

import java.util.Iterator;

/**
 * ReportStrategy implementation for creating delimited reports
 */
public class DelimitedReportStrategy implements ReportStrategy {

    public String delimiter = ",";
    public String endOfLine = "\n";

    /**
     * Formats this report and returns the result as a string
     * @param report
     * @return
     * @throws Exception
     */
    public String format(Report report) throws Exception{
        StringBuilder buf = new StringBuilder();
        Iterator reportIt = report.iterator();
        while (reportIt.hasNext()) {
            Record rec = (Record) reportIt.next();
            Iterator recordIt = rec.iterator();
            while (recordIt.hasNext()) {
                String nxt = (String) recordIt.next();

                if (nxt == null || nxt.toUpperCase().equals("NULL")) {
                    nxt = "";
                }

                if (nxt.contains(delimiter)) {
                    nxt = "\"" + nxt + "\"";
                }                                
                buf.append(nxt);
                if (recordIt.hasNext()) {
                    buf.append(delimiter);
                }else {
                    buf.append(endOfLine);
                }
            }
        }
        return buf.toString();
    }

    /**
     * Sets the delimiter to use for this format.  By default the delimiter is a ,
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Sets the end of line character to use for this format.  By default the EOL char is a "\n"
     * @param endOfLine
     */
    public void setEndOfLineCharacter(String endOfLine) {
        this.endOfLine = endOfLine;
    }
}
