package edu.mcw.rgd.reporting;


import java.util.Iterator;
import java.util.HashMap;



/**
 * ReportStrategy implementation for creating delimited reports.
 *
  * Example usage.
 *
 * //first lets create a record.
 * Record record = new Record()
 * record.append("hello");
 * record.append("world");
 *
 * //next lets create a report
 * Report report = new Report();
 * report.append(record);
 *
 * //We will use the delimited format for this report
 * FixedWidthReportStrategy format = new FixedWidthReportStrategy();
 *
 * format.setColumnWidth(1,8);
 * format.setColumnWidth(2,15,FixedWidthReportStrategy.RIGHT_JUSTIFY)
 *
 * //Print out the record in csv format
 * System.out.println(format.format(report));
 *
 * --The output would be
 * hello             world
 */
public class FixedWidthReportStrategy implements ReportStrategy {

    public int defaultColumnWidth = 10;
    public String endOfLine = "\n";

    HashMap metadata = new HashMap();

    public static final int LEFT_JUSTIFY = 1;
    public static final int RIGHT_JUSTIFY = 2;

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
            int count = 0;
            while (recordIt.hasNext()) {

                Integer width = (Integer) metadata.get(count + ".width");
                if (width == null) {
                    width = defaultColumnWidth;
                }

                String field = (String) recordIt.next();
                if (field.length() > width) {
                    field = field.substring(0, width -1);
                }

                Integer justification = (Integer) metadata.get(count + ".justification");
                if (justification == null || justification == LEFT_JUSTIFY) {
                    buf.append(rightPad(field," ", width));
                }else {
                    buf.append(leftPad(field," ", width));
                }

                count++;
            }
            buf.append(endOfLine);
        }
        return buf.toString();
    }

    /**
     * Sets the column width at a specified column index.  The default column width is 10 characters
     * @param columnIndex
     * @param value
     */
    public void setColumnWidth(int columnIndex, int value) {
        metadata.put(columnIndex + ".width", value);
    }

    /**
     * Sets the column width and justification at a specified column index.
     * The default column width is 10 characters, and justification is left
     * @param columnIndex
     * @param value
     */
    public void setColumnWidth(int columnIndex, int value, int justification) {
        metadata.put(columnIndex + ".width", value);
        metadata.put(columnIndex + ".justification", justification);
    }

    /**
     * Private utility method to pad the right end of a string
     * @param str
     * @param pad
     * @param minLength
     * @return
     */
    private String rightPad(String str, String pad, int minLength) {
        while (str.length() < minLength) {
            str = str + pad;
        }
        return str;
    }

    /**
     * Private utility method to pad the left end of a string
     * @param str
     * @param pad
     * @param minLength
     * @return
     */
    private String leftPad(String str, String pad, int minLength) {
        while (str.length() < minLength) {
            str = pad + str;
        }
        return str;
    }
}
