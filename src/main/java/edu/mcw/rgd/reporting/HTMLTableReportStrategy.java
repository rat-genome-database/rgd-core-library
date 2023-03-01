package edu.mcw.rgd.reporting;
import java.util.Iterator;

/**
 * ReportStrategy implementation for creating delimited reports.
 *
 * The table generated will use css class headerRow, evenRow, and oddRow for formatting.
 *
 * Currently all the style information is contained in the separate css file,
 * as pointed to by getStyles() method.
 *
 * Example usage.
 * <code>
 * //first lets create a record.
 * Record record = new Record();
 * record.append("hello");
 * record.append("world");
 *
 * //next lets create a report
 * Report report = new Report();
 * report.append(record);
 *
 * //We will use the delimited format for this report
 * HTMLTableReportStrategy format = new HTMLTableReportStrategy();
 *
 * //Print out the record in csv format
 * System.out.println(format.format(report));
 *
 * //The output would be
 * <link rel="stylesheet" type="text/css" href="/rgdweb/css/treport.css">
 *   <table border='0' cellpadding='2' cellspacing='2' />
 *      <tr class='headerRow' >
 *          <td  >hello</td>
 *          <td  >world</td>
 *      </tr>
 *   </table>
 *
 * </code>
 *
 * Example to use custom column styling, like right align contents of first column,
 * or vertical align 2nd column
 * <code>
 * Report report = new Report();
 * report.append(new Record().append("Number").append("Text"));
 * report.append(new Record().append("10").append("hello"));
 * report.append(new Record().append("100").append("world"));
 *
 * HTMLTableReportStrategy format = new HTMLTableReportStrategy();
 * format.setTdProperties(new String[]{"align='right'", "valign='top'"};
 *
 * System.out.println(format.format(report));
 *
 * <table border='0' cellpadding='2' cellspacing='2' />
 *   <tr class='headerRow' >
 *     <td >Number</td>
 *     <td >Text</td>
 *   </tr>
 *   <tr class='evenRow' >
 *     <td align='right' >10</td>
 *     <td valign='top' >hello</td>
 *   </tr>
 *   <tr class='oddRow' >
 *     <td align='right' >100</td>
 *     <td valign='top' >world</td>
 *   </tr>
 * </table>
 * </code>
 *
 * Sample css styling:
 * <code>
 *      .evenRow {
 *          background-color: #ffffff;
 *      }
 *      .oddRow {
 *          background-color: #e2e2e2;
 *      }
 *      .headerRow {
 *          background-color: #838383;
 *          font-weight: 700;
 *          font-size: 11px;
 *          color: white;
 *          font-family: arial;
 *      }
 *      tr {
 *          color: black;
 *          font-size: 10px;
 *          font-family: arial;
 *      }
 *      h1 {
 *          color: black;
 *          font-size: 12px;
 *          font-family: arial;
 *          font-weight: 700;
 *          text-decoration: underline;
 *      }
 *      h2 {
 *          color: black;
 *          font-size: 10px;
 *          font-family: arial;
 *          font-weight: 500;
 *      }
 * </code>
 */
public class HTMLTableReportStrategy implements ReportStrategy {

    private String tableProperties = "border='0' cellpadding='2' cellspacing='2'";
    private String[] tdProperties = null; // optional user supplied td properties : one for a column

    public static void main(String[] args) throws Exception {
         //first lets create a record.
         Record record = new Record();
         record.append("hello");
         record.append("world");

         //next lets create a report
         Report report = new Report();
         report.append(record);

         //We will use the delimited format for this report
         HTMLTableReportStrategy format = new HTMLTableReportStrategy();

         //Print out the record in csv format
         System.out.println(format.format(report));
    }


    /**
     * Returns the report formatted in an html table
     * @param report Report object
     * @return HTML string for this report
     * @throws Exception
     */
    public String format(Report report) throws Exception{
        StringBuilder buf = new StringBuilder();

        buf.append(getStyles());

        buf.append("<table ").append(tableProperties).append(" />");
        Iterator reportIt = report.iterator();
        int count=1;
        while (reportIt.hasNext()) {
            String trProperties = "";
            if (count == 1) {
                trProperties = "class='headerRow'";
            }else if (count % 2 == 0) {
                trProperties = "class='evenRow'";
            }else {
                trProperties = "class='oddRow'";
            }

            buf.append("<tr ").append(trProperties).append(" >");
            Record rec = (Record) reportIt.next();
            int colCount = 0;
            Iterator recordIt = rec.iterator();
            while (recordIt.hasNext()) {
                String nxt = (String) recordIt.next();

                if (nxt == null || nxt.toUpperCase().equals("NULL")) {
                    nxt = "";
                }

                // handle user supplied td properties for this column (skip header column)
                String tdAttrs = "";
                if( count>1 && tdProperties!=null && colCount<tdProperties.length ) {
                    tdAttrs = tdProperties[colCount];
                }

                buf.append("<td ").append(tdAttrs).append(" >");
                buf.append(nxt);
                buf.append("</td>");

                colCount++;
            }
            buf.append("</tr>");
            count++;
        }
        buf.append("</table>");
        return buf.toString();
    }

    public void setTableProperties(String tableProperties) {
        this.tableProperties = tableProperties;
    }

    /**
     * Returns the styles for the table.
     * @return html code with link to css file with styles
     */
    protected String getStyles() {
        return "<link rel='stylesheet' type='text/css' href='/rgdweb/css/treport.css'>";
    }

    public String[] getTdProperties() {
        return tdProperties;
    }

    /**
     * set custom user-supplied properties for every column to be emitted in attributes of TD field;
     * note: these properties are NOT emitted for first row, which is supposed to be a table header row
     * @param tdProperties array of TD properties, one for each column
     */
    public void setTdProperties(String[] tdProperties) {
        this.tdProperties = tdProperties;
    }
}
