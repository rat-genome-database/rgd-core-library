package edu.mcw.rgd.process;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.describe.DescriptionGenerator;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * groups utility functions frequently used by many applications
 */
public class Utils {

    /**
     * return true if string is null, or has zero length
     * @param str string
     * @return true if string is null, or has zero length
     */
    static public boolean isStringEmpty(String str) {
        return str==null || str.length()==0;
    }

    /**
     * return true if both strings are equal; null strings are treated as ""
     * @param s1 first string; if null, "" is used in comparison
     * @param s2 second string; if null, "" is used in comparison
     * @return true if both strings are equal
     */
    public static boolean stringsAreEqual(String s1, String s2) {
        if( s1==null )
            s1 = "";
        if( s2==null )
            s2 = "";
        return s1.equals(s2);
    }

    /**
     * return true if both strings are equal; null strings are treated as ""
     * @param s1 first string; if null, "" is used in comparison
     * @param s2 second string; if null, "" is used in comparison
     * @return true if both strings are equal
     */
    public static boolean stringsAreEqualIgnoreCase(String s1, String s2) {
        if( s1==null )
            s1 = "";
        if( s2==null )
            s2 = "";
        return s1.equalsIgnoreCase(s2);
    }

    /**
     * compare to strings; null strings are treated as ""
     * @param s1 first string; if null, "" is used in comparison
     * @param s2 second string; if null, "" is used in comparison
     * @return 0 if both strings are equal; positive or negative int otherwise
     */
    public static int stringsCompareTo(String s1, String s2) {
        if( s1==null )
            s1 = "";
        if( s2==null )
            s2 = "";
        return s1.compareTo(s2);
    }

    /**
     * compare to strings with case ignore; null strings are treated as ""
     * @param s1 first string; if null, "" is used in comparison
     * @param s2 second string; if null, "" is used in comparison
     * @return 0 if both strings are equal; positive or negative int otherwise
     */
    public static int stringsCompareToIgnoreCase(String s1, String s2) {
        if( s1==null )
            s1 = "";
        if( s2==null )
            s2 = "";
        return s1.compareToIgnoreCase(s2);
    }

    /**
     * like in apache commons StringUtils: returns either the passed in String, or if the String is null, an empty String ("")
     * @param s the String to check, may be null
     * @return the passed in String, or the empty String if it was null
     */
    public static String defaultString(String s) {
        return s==null ? "" : s;
    }

    /**
     * based on Oracle SQL NVL function
     * @param str the String to check, may be null
     * @return the passed in String, or the defaultStr if it was null
     */
    public static String NVL(String str, String defaultStr) {
        return str==null ? defaultStr : str;
    }

    /**
     * @param val integer to check, may be null
     * @return the passed integer, or the defaultInt, if val was null
     */
    public static int NVL(Integer val, int defaultVal) {
        return val==null ? defaultVal : val;
    }

    /**
     * return true if both dates are equal; null dates are treated as new java.util.Date(0)
     * @param d1 first date; if null, 0 is used in comparison
     * @param d2 second date; if null, 0 is used in comparison
     * @return true if both dates are equal
     */
    public static boolean datesAreEqual(Date d1, Date d2) {
        long t1 = d1==null ? 0 : d1.getTime();
        long t2 = d2==null ? 0 : d2.getTime();
        return t1==t2;
    }

    /**
     * given date string in format yyyy/mm/dd, add some days to the date
     * note: you can pass 'null' for todays date!
     * @param date date string in format yyyy/mm/dd
     * @param daysToAdd number of days to add; could be negative
     * @return return date string incremented by given amount of days
     * @throws ParseException
     */
    static public String addDaysToDate( String date, int daysToAdd ) throws ParseException {

        java.util.Date syncDate = date==null ? new Date() : dateFormat.parse(date);
        java.util.Date adjustedDate = addDaysToDate(syncDate, daysToAdd);
        return dateFormat.format(adjustedDate);
    }
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * given a date, add some days to the date
     * note: you can pass 'null' for todays date!
     * @param date date object, or null for today's date
     * @param daysToAdd number of days to add; could be negative
     * @return return date object incremented by given amount of days
     */
    static public Date addDaysToDate( Date date, int daysToAdd ) {
        int hoursToAdd = 24*daysToAdd;
        return addHoursToDate(date, hoursToAdd);
    }

    /**
     * given a date, add some hours to the date
     * note: you can pass 'null' for todays date!
     * @param date date object, or null for today's date
     * @param hoursToAdd number of hours to add; if negative hours are subtracted from the date
     * @return return date object incremented by given amount of hours
     */
    static public Date addHoursToDate( Date date, int hoursToAdd ) {

        java.util.Date syncDate = date==null ? new Date() : date;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(syncDate);
        calendar.add(Calendar.HOUR, hoursToAdd);
        return calendar.getTime();
    }

    /**
     * given a date, add some minutes to the date
     * note: you can pass 'null' for todays date!
     * @param date date object, or null for today's date
     * @param minutesToAdd number of minutes to add; if negative minutes are subtracted from the date
     * @return return date object incremented by given amount of minutes
     */
    static public Date addMinutesToDate( Date date, int minutesToAdd ) {

        java.util.Date syncDate = date==null ? new Date() : date;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(syncDate);
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar.getTime();
    }

    /**
     * get human readable string showing elapsed time between two time points
     * @param t1 first time point (as returned by call to System.currentTimeMillis())
     * @param t2 second time point
     * @return return formatted string showing elapsed time between two time points
     */
    static public String formatElapsedTime(long t1, long t2) {
        long diff = t2 - t1;
        if( diff < 0 )
            diff = -diff;

        long secondInMillis = 1000;
        long minuteInMillis = secondInMillis * 60;
        long hourInMillis = minuteInMillis * 60;
        long dayInMillis = hourInMillis * 24;
        long yearInMillis = dayInMillis * 365;

        long elapsedYears = diff / yearInMillis;
        diff = diff % yearInMillis;
        long elapsedDays = diff / dayInMillis;
        diff = diff % dayInMillis;
        long elapsedHours = diff / hourInMillis;
        diff = diff % hourInMillis;
        long elapsedMinutes = diff / minuteInMillis;
        diff = diff % minuteInMillis;
        long elapsedSeconds = diff / secondInMillis;

        StringBuilder buf = new StringBuilder(100);
        if( elapsedYears>0 ) {
            buf.append(elapsedYears).append(" years ");
        }
        if( elapsedDays>0 ) {
            buf.append(elapsedDays).append(" days ");
        }
        if( elapsedHours>0 ) {
            buf.append(elapsedHours).append(" hours ");
        }
        if( elapsedMinutes>0 ) {
            buf.append(elapsedMinutes).append(" minutes ");
        }
        if( elapsedSeconds>0 ) {
            buf.append(elapsedSeconds).append(" seconds ");
        }
        String elapsedTime = buf.toString();
        return elapsedTime.length()>0 ? elapsedTime : "0";
    }

    public static GregorianCalendar addOneYear(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR,1);
        return gc;
    }

    public static GregorianCalendar getTomorrow(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_YEAR,1);
        return gc;
    }

    /**
     * return true if both integers are equal; null integers are treated as 0
     * @param i1 first int; if null, 0 is used in comparison
     * @param i2 second int; if null, 0 is used in comparison
     * @return true if both ints are equal
     */
    public static boolean intsAreEqual(Integer i1, Integer i2) {
        return intsCompareTo(i1, i2)==0;
    }

    /**
     * compare two integers; substitute 0 if any parameter is null
     * @param i1 first int; if null, 0 is used in comparison
     * @param i2 second int; if null, 0 is used in comparison
     * @return 0 if equal; positive or negative int otherwise
     */
    public static int intsCompareTo(Integer i1, Integer i2) {
        if( i1==null )
            i1 = 0;
        if( i2==null )
            i2 = 0;
        return i1.compareTo(i2);
    }

    /**
     * return true if both long integers are equal; null integers are treated as 0
     * @param i1 first long int; if null, 0 is used in comparison
     * @param i2 second long int; if null, 0 is used in comparison
     * @return true if both long ints are equal
     */
    public static boolean longsAreEqual(Long i1, Long i2) {
        return longsCompareTo(i1, i2)==0;
    }

    /**
     * compare two long integers; substitute 0 if any parameter is null
     * @param i1 first int; if null, 0 is used in comparison
     * @param i2 second int; if null, 0 is used in comparison
     * @return 0 if equal; positive or negative int otherwise
     */
    public static int longsCompareTo(Long i1, Long i2) {
        if( i1==null )
            i1 = 0L;
        if( i2==null )
            i2 = 0L;
        return i1.compareTo(i2);
    }

    /**
     * return true if both double precision numbers are equal up to specific precision;
     * null numbers are treated as 0.0
     * @param d1 first double; if null, 0.0 is used in comparison
     * @param d2 second double; if null, 0.0 is used in comparison
     * @param prec precision: how many digits after the decimal point
     * @return true if both double precision numbers are equal up to given precision
     */
    public static boolean doublesAreEqual(Double d1, Double d2, int prec) {
        if( d1==null )
            d1 = 0.0;
        if( d2==null )
            d2 = 0.0;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(prec);
        df.setMaximumFractionDigits(prec);
        String s1 = df.format(d1);
        String s2 = df.format(d2);
        return s1.equals(s2);
    }

    /**
     * build a string from a collection of objects; the result could be used as IN phrase in oracle sql statement;
     * Note: this will work great for collection of Integer objects
     * @param objs collection of objects
     * @return String
     */
    public static String buildInPhrase(Collection objs) {
        return concatenate(objs, ",");
    }

    /**
     * build a string from a collection of objects; the result could be used as IN phrase in oracle sql statement;
     * Note: this will work great for collection of String objects that have to be quoted
     * @param objs collection of objects
     * @return String
     */
    public static String buildInPhraseQuoted(Collection objs) {
        return concatenate(objs, ",", "'");
    }

    /**
     * build a string from a collection of objects to be used as a IN phrase in a SQL query;
     * a string representation of every object is used during concatenation, so it will work great for
     * any collection of Integers and Strings as well
     * @param objs collection of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @return string: concatenation of all objects
     */
    public static String concatenate(Collection objs, String separator) {
        if( objs==null || objs.isEmpty() )
            return "";

        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 )
                buf.append(separator);
            buf.append(obj.toString());
        }
        return buf.toString();
    }

    /**
     * concatenate an array of objects
     * @param objs array of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @return string: concatenation of all objects
     */
    public static String concatenate(Object[] objs, String separator) {
        if( objs==null || objs.length==0 )
            return "";

        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 )
                buf.append(separator);
            buf.append(obj.toString());
        }
        return buf.toString();
    }

    /**
     * build a string from a collection of objects to be used as a IN phrase in a SQL query;
     * a string representation of every object is used during concatenation, so it will work great for
     * any collection of Integers and Strings as well
     * @param objs collection of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @param quoteChar quote character preceding and following each value
     * @return string: concatenation of all objects
     */
    public static String concatenate(Collection objs, String separator, String quoteChar) {

        if( quoteChar==null || quoteChar.isEmpty() ) {
            return concatenate(objs, separator);
        }

        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 )
                buf.append(separator);
            buf.append(quoteChar).append(obj.toString()).append(quoteChar);
        }
        return buf.toString();
    }

    /**
     * build a string from an array of objects to be used as a IN phrase in a SQL query;
     * a string representation of every object is used during concatenation, so it will work great for
     * any array of Integers and Strings as well
     * @param objs array of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @param quoteChar quote character preceding and following each value
     * @return string: concatenation of all objects
     */
    public static String concatenate(Object[] objs, String separator, String quoteChar) {

        if( quoteChar==null || quoteChar.isEmpty() || objs==null ) {
            return concatenate(objs, separator);
        }

        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 )
                buf.append(separator);
            buf.append(quoteChar).append(obj.toString()).append(quoteChar);
        }
        return buf.toString();
    }

    /**
     * build a string from a collection of objects to be used as a IN phrase in a SQL query;
     * a string representation of every object is used during concatenation
     * a method is called on every object to access the value to be used in concatenation
     * <p>
     * for example, if we have a collection of MapData objects and we want to concatenate all chromosomes by '|',
     * call: Utils.concatenate("|",objs,"getChromosome")
     * @param objs collection of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @param methodName name of the method to be invoked for every object in a collection
     * @return string: concatenation of all objects
     */
    public static String concatenate(String separator, Collection objs, String methodName) throws Exception {
        Method method = null;
        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 ) {
                buf.append(separator);
            }
            if( method==null ) {
                method = obj.getClass().getMethod(methodName, new Class[]{});
            }
            Object v = method.invoke(obj);
            if( v!=null )
                buf.append(v.toString());
        }
        return buf.toString();
    }

    /**
     * build a string from a collection of objects to be used as a IN phrase in a SQL query;
     * a string representation of every object is used during concatenation
     * a method is called on every object to access the value to be used in concatenation
     * <p>
     * for example, if we have a collection of MapData objects and we want to concatenate all chromosomes by '|',
     * call: Utils.concatenate("|",objs,"getChromosome","'")
     * @param objs collection of objects to be concatenated
     * @param separator separator inserted between two concatenated objects
     * @param methodName name of the method to be invoked for every object in a collection
     * @param quoteChar quote character preceding and following each value
     * @return string: concatenation of all objects
     */
    public static String concatenate(String separator, Collection objs, String methodName, String quoteChar) throws Exception {
        if( quoteChar==null ) {
            return concatenate(separator, objs, methodName);
        }

        Method method = null;
        StringBuilder buf = new StringBuilder();
        for( Object obj: objs ) {
            if( buf.length()>0 ) {
                buf.append(separator);
            }
            if( method==null ) {
                method = obj.getClass().getMethod(methodName, new Class[]{});
            }
            Object v = method.invoke(obj);
            if( v!=null )
                buf.append(quoteChar).append(v.toString()).append(quoteChar);
        }
        return buf.toString();
    }

    /** format genomic positions with a thousands (grouping) separators
     * f.e. 12345678 is shown as 12,345,678
     */
    public static String formatThousands(Object i) {
        if( i==null )
            return "0";
        return _thFmt.format(i);
    }
    private static DecimalFormat _thFmt = new DecimalFormat("###,###,###,###");

    /**
     * get a single row from a database given query; if the query returns more rows, they are ignored and only one is returned
     * @param query sql query
     * @param params optional query parameters
     * @return list of columns with the query's first row; if empty, no results
     * @throws Exception
     */
    public static List<String> getSingleRow(String query, String[] params) throws Exception {

        Connection conn = DataSourceFactory.getInstance().getDataSource().getConnection();
        List<String> columns = new ArrayList<>();
        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            if( params!=null ) {
                for( int i=0; i<params.length; i++ ) {
                    stmt.setString(i+1, params[i]);
                }
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int columnCount = rs.getMetaData().getColumnCount();
                for( int i=1; i<=columnCount; i++ )
                    columns.add(rs.getString(i));
            }

        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return columns;
    }

    /**
     * get a multiple rows from a database until two rows are equal; equality
     * @param query sql query
     * @param params optional query parameters
     * @param comparator comparator that will compare two rows of data: compareTo() should return 0 as long as we want
     * the rows to be included in the results; row processing is stopped when the row read is different from the previous row;
     * if null, all rows from the query are returned
     * @return list of columns with the query's first row; if empty, no results
     * @throws Exception
     */
    public static List<List<String>> getRows(String query, String[] params, Comparator<List<String>> comparator) throws Exception {

        Connection conn = DataSourceFactory.getInstance().getDataSource().getConnection();
        List<List<String>> rows = new ArrayList<>();
        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            if( params!=null ) {
                for( int i=0; i<params.length; i++ ) {
                    stmt.setString(i+1, params[i]);
                }
            }

            ResultSet rs = stmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            List<String> prevRow = null;
            while (rs.next()) {
                // read row: all columns
                List<String> currRow = new ArrayList<>();
                for( int i=1; i<=columnCount; i++ ) {
                    currRow.add(rs.getString(i));
                }

                // if this a first row, add it to 'rows'
                if( prevRow==null ) {
                    rows.add(currRow);
                }
                else {
                    // if this is not first row, compare it with the previous
                    // if they are the same, add it to 'rows', if they are different, stop processing
                    if( comparator==null || comparator.compare(currRow, prevRow)==0 ) {
                        // rows the same
                        rows.add(currRow);
                    }
                    else // rows different -- stop processing
                        break;
                }
                prevRow = currRow;
            }

        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return rows;
    }

    public static List<String> symbolSplit(String symbolString) {

        List<String> symbolList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[\\w-:\\.\\/\\<\\>]+");
        Matcher matcher = pattern.matcher(symbolString);
        while (matcher.find()) {
            String match = matcher.group();
            symbolList.add(match);
         //   System.out.println(match);
        }
        return symbolList;

    }

    public static String removeParameter(String name, String queryString) {
        Pattern p = Pattern.compile("(.*)(&" + name + "=[^&]*)(.*)");
        Matcher m = p.matcher(queryString);

        m.matches();

        if (m.matches()) {
            queryString=m.group(1) + m.group(3);
        }else {
            p = Pattern.compile("(.*)(\\?" + name + "=[^&]*)(.*)");
            m = p.matcher(queryString);

            m.matches();

            if (m.matches()) {
                queryString=m.group(1) + m.group(3);
            }
        }
        return queryString;

    }

    /**
     * get gene description:
     * <ul>
     * <li>regular rat genes have always automatically generated description</li>
     * <li>rat gene alleles and splices: have curated description merged with autogenerated description</li>
     * <li>human and mouse genes only if there is no description from RefSeq</li>
     * </ul>
     * @param obj Gene object
     * @return description string
     */
    public static String getGeneDescription(Gene obj) throws Exception {

        String description = null;

        DescriptionGenerator dg = new DescriptionGenerator();
        String autoDescription = dg.buildDescription(obj.getRgdId());
        String geneDescription = autoDescription;
        if (geneDescription.trim().equals("")) {
            geneDescription = obj.getDescription();
        }

        if( obj.getSpeciesTypeKey() == SpeciesType.RAT  && !obj.isVariant() ) {
            // regular rat gene: use auto-generated description
            description = geneDescription;
        } else if( obj.getSpeciesTypeKey() == SpeciesType.RAT  && obj.isVariant() ) {
            // rat gene allele/splice: merge gene description with auto-generated description
            description = obj.getDescription();
            if( !autoDescription.trim().isEmpty() ) {
                if( description!=null ) {
                    description += "<br>"+autoDescription;
                } else {
                    description = autoDescription;
                }
            }
        } else { // non-rat
            // non-rat gene: use auto generated desc
            description = obj.getDescription();
            if( description==null || description.trim().isEmpty() ) {
                description = geneDescription;
            }
        }

        return description!=null ? description : "";
    }

    static public void sendMail(String[] recipients, String subject, String message) {
        sendMail(null, null, recipients, subject, message);
    }

    /**
     * connect to a smtp server, a send a simple message through it
     * @param mailServer mail server name; if null or empty, 'localhost' will be used
     * @param mailFrom email address of the sender f.e 'rgddata@kirwan.mcw.edu';
     * if null or empty, 'rgdpub' user and the local host name will be used
     * @param recipients array of recipient email addresses
     * @param subject message subject
     * @param message message text
     */
    static public void sendMail(String mailServer, String mailFrom, String[] recipients, String subject, String message) {

        try {
            if( Utils.isStringEmpty(mailServer) )
                mailServer = "localhost";
            if( Utils.isStringEmpty(mailFrom) )
                mailFrom = "rgdpub@"+java.net.InetAddress.getLocalHost().getHostName();

            Socket s = new Socket(mailServer, 25);
            BufferedReader in = new BufferedReader (new InputStreamReader(s.getInputStream(), "8859_1"));
            BufferedWriter out = new BufferedWriter (new OutputStreamWriter(s.getOutputStream(), "8859_1"));

            send(in); // eat the prompt from SMTP server
            send(in, out, "HELO theWorld");
            send(in, out, "MAIL FROM: "+mailFrom);
            for( String recipient: recipients ) {
                send(in, out, "RCPT TO: " + recipient);
            }
            send(in, out, "DATA");
            send(out, "Subject: "+subject);
            send(out, "From: "+mailFrom);
            send(out, "");

            // message body
            send(out, message);
            send(out, ".");
            send(in, out, "QUIT");
            s.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void send(BufferedReader in, BufferedWriter out, String s) throws IOException {
        send(out, s);
        send(in);
    }

    static private void send(BufferedWriter out, String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            //System.out.println("  OUT> "+s);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void send(BufferedReader in) throws IOException {
        String s = in.readLine();
        //System.out.println("IN> "+s);
    }

    /**
     * read text file contents into a string variable
     * @param fileName file name on disk
     * @return string with contents of a text file
     * @throws IOException
     */
    static public String readFileAsString(String fileName) throws IOException {

        File file = new File(fileName);
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(buffer);
        f.close();
        return new String(buffer);
    }

    /**
     * save a string into a disk file
     * @param txt string to be written
     * @param fileName file name on disk
     * @throws IOException
     */
    static public void writeStringToFile(String txt, String fileName) throws IOException {

        FileOutputStream f = new FileOutputStream(fileName);
        f.write(txt.getBytes());
        f.close();
    }

    /**
     * print stack trace to the supplied log, as an error
     */
    static public void printStackTrace(Exception e, org.apache.commons.logging.Log log) {
        // print stack trace to error stream
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bs));
        log.error(bs.toString());
    }

    /**
     * print stack trace to the supplied log, as an error
     */
    static public void printStackTrace(Exception e, org.apache.logging.log4j.Logger log) {
        // print stack trace to error stream
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bs));
        log.error(bs.toString());
    }

    /** generate MD5 digest for given string of data
     *
     * @param str data string
     * @return MD5 digest (hex-encoded string)
     */
    static public String generateMD5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5 = md.digest(str.getBytes());
//        return DatatypeConverter.printHexBinary(md5); // old Java 8 method removed in Java 11
        return bytesToHex(md5);
    }

    /**
     * given file name, return BufferedReader object;
     * if file name ends with '.gz', assumes that the file is 'gzip'-compressed
     * @param fileName file name to open
     * @return BufferedReader object
     * @throws IOException
     */
    static public BufferedReader openReader(String fileName) throws IOException {
        BufferedReader reader;
        if( fileName.endsWith(".gz") || fileName.contains(".gz_") ) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName))));
        } else
            reader = new BufferedReader(new FileReader(fileName));
        return reader;
    }

    /**
     * given file name, return BufferedReader object;
     * if file name ends with '.gz', assumes that the file is 'gzip'-compressed;
     * assume the file content is UTF-8 encoded
     * @param fileName file name to open
     * @return BufferedReader object
     * @throws IOException
     */
    static public BufferedReader openReaderUtf8(String fileName) throws IOException {
        final String encoding = "UTF-8";
        InputStream is;
        if( fileName.endsWith(".gz") || fileName.contains(".gz_") ) {
            is = new GZIPInputStream(new FileInputStream(fileName));
        } else {
            is = new FileInputStream(fileName);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
        return reader;
    }

    /**
     * given file name, return BufferedWriter object;
     * if file name ends with '.gz', the file will be 'gzip'-compressed
     * @param fileName file name to create/overwrite
     * @return BufferedWriter object
     * @throws IOException
     */
    static public BufferedWriter openWriter(String fileName) throws IOException {
        BufferedWriter writer;
        if( fileName.endsWith(".gz") || fileName.contains(".gz_") ) {
            writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(fileName))));
        } else
            writer = new BufferedWriter(new FileWriter(fileName));
        return writer;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
