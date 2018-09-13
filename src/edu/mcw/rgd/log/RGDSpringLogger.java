package edu.mcw.rgd.log;

import edu.mcw.rgd.dao.AbstractDAO;

import java.text.*;

/**
 * RGDSpringLogger.java
 * $Revision: 1.1 $
 * $Date: 2007/04/09 16:23:17 $
 * Created by George Kowalski
 * Modified by Marek Tutaj to reuse AbstractDAO class
 * <p>
 * RGDLogging class that only works within the SPRING Framework
 */
public class RGDSpringLogger extends AbstractDAO {

    // Default date format for Dates that are written to the log file . Can be overwritten by
    // Interjecting dateFormat with setDateFormat();

    String dateFormat = "yyyy.MM.dd.HH.mm.ss";

    /**
     * @param subSystemName - Name of this subsystem 'EntrezGeneLoad' , etc.
     * @param dataExtract   - Type of data we're extracting 'timeToExecute', etc.
     * @param dataValue     - Value of the data to log
     */
    public void log(String subSystemName, String dataExtract, String dataValue) throws Exception {

        // First get get the rpt_process_type_id given the subSystem and dataExtract names.
        String sql = "SELECT rpt_process_type_id FROM report_process_types WHERE subsystem_name=? AND data_extract=?";
        int rptProcessType;
        try {
            rptProcessType = getCount(sql, subSystemName, dataExtract);
        } catch( IndexOutOfBoundsException e ) {
            // trick: if rpt_process_type_id, getCount() will throw IndexOutOfBoundsException
            // we intercept the exception to create a new rpt_process_type_id
            String sql2 = "INSERT INTO report_process_types (rpt_process_type_id,subsystem_name,data_extract) VALUES(report_process_types_seq.NEXTVAL,?,?)";
            update(sql2, subSystemName, dataExtract);

            // retry to get rpt_process_type_id
            rptProcessType = getCount(sql, subSystemName, dataExtract);
        }

        // Finally do the insert
        sql = "INSERT INTO report_extracts VALUES(report_extracts_seq.nextval, ?, ?, SYSDATE)";
        update(sql, rptProcessType, dataValue);
    }

    /**
     * Helper Method to take integer data and convert to string to store in database
     *
     * @param subSystemName
     * @param dataExtract
     * @param dataValue
     */
    public void log(String subSystemName, String dataExtract, int dataValue) throws Exception {
        this.log(subSystemName, dataExtract, Integer.toString(dataValue));
    }

    /**
     * Helper Method to take float data and convert to string to store in database
     *
     * @param subSystemName
     * @param dataExtract
     * @param dataValue
     */
    public void log(String subSystemName, String dataExtract, float dataValue) throws Exception {
        this.log(subSystemName, dataExtract, Float.toString(dataValue));
    }

    /**
     * @param subSystemName
     * @param dataExtract
     * @param dataValue
     */
    public void log(String subSystemName, String dataExtract, Double dataValue) throws Exception {
        this.log(subSystemName, dataExtract, Double.toString(dataValue));
    }

    /**
     * Helper Method to take java.util.Date data and convert to string to store in database
     *
     * @param subSystemName
     * @param dataExtract
     * @param dataValue
     */
    public void log(String subSystemName, String dataExtract, java.util.Date dataValue) throws Exception {
        Format formatter;
        formatter = new SimpleDateFormat(dateFormat);
        String dateFormatted = formatter.format(dataValue);
        this.log(subSystemName, dataExtract, dateFormatted);
    }


    /**
     * Returns the date format to be used when date being sent to database needs to be converted to text.
     *
     * @return String
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the date format to be used when date being sent to database needs to be converted to text.
     *
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
