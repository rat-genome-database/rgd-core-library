package edu.mcw.rgd.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: GKowalski
 * Date: Jul 28, 2009
 * Time: 11:12:35 AM
 */
public class OracleUtils {

    protected final Log logger = LogFactory.getLog(getClass());
    public static String NOTNULL = "NULL";
    public static String ISNULL = "ISNULL";

    /**
     * Returns true if string passed in is capable of  being formatted for Oracle Date , else returns false
     * Date must be of format DD/MM/YYYY with zero padding in front or days and months
     * "(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d"
     * FYI: I Test regex at http://www.regexplanet.com/simple/index.jsp
     *
     * @param searchString
     * @return
     */
    public static boolean dateFound(String searchString) {
        if (searchString == null) return false;

        String patternStr = "(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d";
        Pattern pattern = Pattern.compile(patternStr);

        // Determine if there is an exact match
        Matcher matcher = pattern.matcher(searchString);
        return (matcher.matches());
    }
}
