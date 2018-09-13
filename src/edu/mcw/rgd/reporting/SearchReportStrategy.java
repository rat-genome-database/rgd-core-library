package edu.mcw.rgd.reporting;

import edu.mcw.rgd.datamodel.XDBIndex;
import edu.mcw.rgd.datamodel.XdbId;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ReportStrategy implementation for creating delimited reports.
 * <p/>
 * The table generated will use css class headerRow, evenRow, and oddRow for formatting.
 * <p/>
 * Currently all the style information is hard coded in the class. Eventually this should be moved
 * out.
 * <p/>
 * Example usage.
 * <p/>
 * //first lets create a record.
 * Record record = new Record();
 * record.append("hello");
 * record.append("world");
 * <p/>
 * //next lets create a report
 * Report report = new Report();
 * report.append(record);
 * <p/>
 * //We will use the delimited format for this report
 * HTMLTableReportStrategy format = new HTMLTableReportStrategy();
 * <p/>
 * //Print out the record in csv format
 * System.out.println(format.format(report));
 * <p/>
 * --The output would be
 * <style>
 * .evenRow {
 * background-color: #ffffff;
 * }
 * .oddRow {
 * background-color: #e2e2e2;
 * }
 * .headerRow {
 * background-color: #838383;
 * font-weight: 700;
 * font-size: 11px;
 * color: white;
 * font-family: arial;
 * }
 * tr {
 * color: black;
 * font-size: 10px;
 * font-family: arial;
 * }
 * h1 {
 * color: black;
 * font-size: 12px;
 * font-family: arial;
 * font-weight: 700;
 * text-decoration: underline;
 * }
 * h2 {
 * color: black;
 * font-size: 10px;
 * font-family: arial;
 * font-weight: 500;
 * }
 * </style>
 * <table border='0' cellpadding='2' cellspacing='2' />
 * <tr class='headerRow' >
 * <td  >hello</td>
 * <td  >world</td>
 * </tr>
 * </table>
 */
public class SearchReportStrategy implements ReportStrategy {

    String tableProperties = "border='0' cellpadding='2' cellspacing='2'";
    String trProperties = "";
    String tdProperties = "";

    List highlightedTerms = new ArrayList();
    List colTitles = new ArrayList();

    private HashMap hiddenColumns = new HashMap();

    public void hideColumn(int columnIndex) {
        hiddenColumns.put(columnIndex, null);
    }

    public void setHighlightedTerms(List terms) {
        this.highlightedTerms = terms;
    }

    public static void main(String[] args) throws Exception {
        //first lets create a record.
        Record record = new Record();
        record.append("hello");
        record.append("world");

        //next lets create a report
        Report report = new Report();
        report.append(record);

        //We will use the delimited format for this report
        SearchReportStrategy format = new SearchReportStrategy();

        //Print out the record in csv format
        System.out.println(format.format(report));
    }

    /**
     * Returns the report formatted in an html table
     *
     * @param report Report object
     * @return report formatted in an html table
     * @throws Exception
     */
    public String format(Report report) throws Exception {

        StringBuilder buf = new StringBuilder();

        buf.append(getStyles());

        buf.append("<table ").append(tableProperties).append(" />");
        Iterator reportIt = report.iterator();
        int count = 1;
        while (reportIt.hasNext()) {
            if (count == 1) {
                trProperties = "class='headerRow'";
            } else if (count % 2 == 0) {
                trProperties = "class='evenRow'";
            } else {
                trProperties = "class='oddRow'";
            }

            buf.append("<tr ").append(trProperties).append(" >");
            Record rec = (Record) reportIt.next();
            Iterator recordIt = rec.iterator();
            int colCount = 0;
            while (recordIt.hasNext()) {
                String nxt = (String) recordIt.next();

                if (nxt == null || nxt.toUpperCase().equals("NULL")) {
                    nxt = "";
                }

                if (count == 1) {
                    colTitles.add(nxt);
                }

                if (hiddenColumns.containsKey(colCount)) {
                    colCount++;
                    continue;
                }

                buf.append("<td ").append(tdProperties).append(" >");

                String title_lc = colTitles.get(colCount).toString().toLowerCase();

                Iterator it = highlightedTerms.iterator();
                while (it.hasNext()) {
                    String term = (String) it.next();
                    if (term.equals("weight") || term.equals("span") || term.equals("style") || term.equals("font")) {
                        term = " " + term;
                    }

                    if (!nxt.contains("</a>") && !term.contains("*") && !(title_lc.equals("symbol") || title_lc.equals("title")  || title_lc.equals("pubmed"))) {
                        nxt = makeBold(nxt, term);
                    }
                }

                if (nxt.length() > 300) {

                    String divId = count + "-" + colCount;
                    String start = nxt.substring(0, 300);

                    if (start.substring(300, 300).equals("<")) {
                        start = nxt.substring(0, 301);
                    }

                    if (start.contains("<span")) {
                        while (start.lastIndexOf("<span") > start.lastIndexOf("</span>")) {
                            start = nxt.substring(0, start.length() + 1);
                        }
                    }

                    start = start + "<div id='" + divId + "a' style=\"display:inline;\">...<a href='javascript:document.getElementById(\"" + divId + "\").style.display=\"inline\";document.getElementById(\"" + divId + "a\").style.display=\"none\";void(0);' > (more)</a></div>";
                    String last = nxt.substring(300);
                    String div = "<div id='" + divId + "' style='display:none;'>" + last + "</div>";
                    nxt = start + div;
                }


                if (title_lc.equals("symbol") && count > 1) {
                    buf.append("<a class='geneList' href='").append(Link.it(rec.get(colCount - 1), rec.getObjectKey())).append("'>").append(nxt).append("</a>");


                } else if (title_lc.equals("title") && count > 1) {
                    buf.append("<a class='hotLink' href='").append(Link.it(rec.get(colCount - 1), rec.getObjectKey())).append("'>").append(nxt).append("</a>");
                } else if (title_lc.equals("pubmed") && count > 1) {

                    buf.append("<a class='hotLink' href='").append(XDBIndex.getInstance().getXDB(XdbId.XDB_KEY_PUBMED).getUrl()).append(nxt).append("'>").append(nxt).append("</a>");
                } else {
                    buf.append(nxt);
                }

                buf.append("</td>");
                colCount++;
            }
            buf.append("</tr>");
            count++;
        }
        buf.append("</table>");
        return buf.toString();
    }

    private String makeBold(String nxt, String term) {

        try {
            String term2 = term.toLowerCase();
            if( nxt.contains(term2) ) {
                nxt = nxt.replaceAll(term2, "<span style='font-weight:700;'>" + term + "</span>");
            }
            term2 = term.toUpperCase();
            if( nxt.contains(term2) ) {
                nxt = nxt.replaceAll(term2, "<span style='font-weight:700;'>" + term2 + "</span>");
            }
            term2 = term.substring(0, 1).toUpperCase() + term.substring(1);
            if( nxt.contains(term2) ) {
                nxt = nxt.replaceAll(term2, "<span style='font-weight:700;'>" + term2 + "</span>");
            }
        } catch (Exception ignored) {
            //if this fails, we don't care.  regex chars in input could cause failure.
        }
        return nxt;
    }

    public void setTableProperties(String tableProperties) {
        this.tableProperties = tableProperties;
    }

    /**
     * Get the styles for the table.  Since styles are hard coded in the method, to change the style,
     * the class will need to be extended.  In the future this information could be moved into a separate file.
     *
     * @return HTML with styles for the table
     */
    protected String getStyles() {

        return "<style> " +

                ".evenRow { " +
                "    background-color: #ffffff; " +
                "} " +

                ".oddRow { " +
                "    background-color: #e2e2e2; " +
                "} " +

                ".headerRow { " +
                "    background-color: #838383; " +
                "    font-weight: 700; " +
                "    font-size: 11px; " +
                "    color: white; " +
                "    font-family: arial; " +
                "} " +

                "tr { " +
                "  color: black; " +
                "  font-size: 11px; " +
                "  font-family: arial; " +
                "} " +
                "h1 { " +
                "  color: black; " +
                "  font-size: 12px; " +
                "  font-family: arial; " +
                "  font-weight: 700; " +
                "  text-decoration: underline; " +
                "} " +
                "h2 { " +
                "  color: black; " +
                "  font-size: 10px; " +
                "  font-family: arial; " +
                "  font-weight: 500; " +
                "} " +
                "</style>";
    }
}
