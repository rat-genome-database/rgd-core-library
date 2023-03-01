package edu.mcw.rgd.reporting;




/**
 * Interface used to implement Report Formats.  Report Formats format data stored in report objects.
 *
 * For examples see FixedWidthReportStrategy or Delimited Report Format
 */

public interface ReportStrategy {

    /**
     * Formats the Report object and returns the formatted output as a String
      * @param report
     * @return
     * @throws Exception
     */
    public String format(Report report) throws Exception;

}
