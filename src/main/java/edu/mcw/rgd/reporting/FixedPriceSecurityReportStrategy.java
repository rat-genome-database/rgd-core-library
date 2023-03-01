package edu.mcw.rgd.reporting;


import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * ReportStrategy implementation for creating fixed price reports.
 *
 */
public class FixedPriceSecurityReportStrategy extends HTMLTableReportStrategy {

    String tableProperties = "border='0' cellpadding='2' cellspacing='2'";
    String trProperties = "";
    String tdProperties = "";

    public static void main(String[] args) throws Exception {
         //first lets create a record.
         Record record = new Record();
         record.append("hello");
         record.append("world");

         //next lets create a report
         Report report = new Report();
         report.append(record);

         //We will use the delimited format for this report
         FixedPriceSecurityReportStrategy format = new FixedPriceSecurityReportStrategy();

         //Print out the record in csv format
         System.out.println(format.format(report));
    }


    /**
     * Returns the report formatted in an html table
     * @param report
     * @return
     * @throws Exception
     */
    public String format(Report report) throws Exception{
        StringBuilder buf = new StringBuilder();

        buf.append(getStyles());

        buf.append("<table ").append(tableProperties).append(" />");
        Iterator reportIt = report.iterator();
        int count=1;

        buf.append("<tr><td align='center' class='xtitle' colspan='8'>FIXED PRICE SECURITIES REPORT<br>SEPARATELY MANAGED<br><br></td</tr>");
        buf.append("<tr><td align='center' class='xsubtitle' colspan='8'>").append(new SimpleDateFormat("MMM d, yyyy").format(new Date())).append("<br><br></td</tr>");


        String prevCategory = "";
        while (reportIt.hasNext()) {
            if (count == 1) {
                trProperties = "class='headerRow'";
            }else if (count % 2 == 0) {
                trProperties = "class='evenRow'";
            }else {
                trProperties = "class='oddRow'";
            }


            Record rec = (Record) reportIt.next();

            String nextCategory = rec.get(7);

            if (!nextCategory.equals(prevCategory) && count != 1) {
                buf.append("<tr >");
                buf.append("<td class='xsubtitleRow'><br>CASA CLASS: ").append(nextCategory).append("</td>");
                buf.append("</tr >");
                prevCategory = nextCategory;
            }

            buf.append("<tr ").append(trProperties).append(" >");

            Iterator recordIt = rec.iterator();
            int recNum = 1;
            while (recordIt.hasNext()) {
                String field = (String) recordIt.next();

                if (recNum > 7 && count > 1) {
                    field = "<b><u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></b>";
                }

                buf.append("<td ").append(tdProperties).append(" ");

                try {
                    if (recNum != 1) {
                        Double.parseDouble(field);
                    buf.append(" align='right' ");
                    }
                }catch (Exception e) {
                }

                buf.append(" >");
                buf.append(field);
                buf.append("</td>");

                recNum++;
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
}
