package edu.mcw.rgd.util;


/**
 * Created by IntelliJ IDEA.
 * User: gkowalski
 * Date: Jun 11, 2009
 * Time: 9:29:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static String ORDER_BY_SUBJECT_LAST_NAME = "1";
    public static String ORDER_BY_PHYSICIAN_AND_SUBJECT_LAST_NAME = "2";


    /**
     * Given the fullRoleDN of 'cn=testsystem,dc=hmgc,dc=mcw,dc=edu' returns just the base role  "testsystem"
     * If we cannot determine the base role name we return the string passed in.
     *
     * @param fullRoleDN
     * @return
     */
    public static String getRoleName(String fullRoleDN) {
        if (fullRoleDN == null) return null;
        String[] splitDNArray = fullRoleDN.split(",");
        if (splitDNArray.length > 2) {
            String subDN = splitDNArray[0];
            String[] subSplitDNArray = subDN.split("=");
            return subSplitDNArray[1];
        } else {
            return fullRoleDN;
        }

    }

    /**
     * TODO: Use the CHART_DICTIONARY TABLE INSTEAD OF THIS METHOD.
     *
     * @param statusId
     * @return
     */
    public static String getChartStatusById(int statusId) {
        switch (statusId) {
            case 1:
                return "Awaiting CC Facilitation";

            case 2:
                return "Awaiting Re-Review";

            case 3:
                return "Awaiting Review";

            case 4:
                return "Call Back Expected";

            case 5:
                return "Completed";

            case 6:
                return "In Treatment with Specialist";

            case 7:
                return "Non-Compliant";

            case 8:
                return "Ongoing Facilitation";

            case 9:
                return "Priority Consult Status";

            case 10:
                return "To Be Scheduled";

            case 11:
                return "Intake Pending";

            case 12:
                return "Waiting for Records";
            
            case 13:
                return "Insurance Verification";

            default:
                return "Unknown";
        }
    }

    public final static String xmlEncodeTextAsPCDATA(String text) {
        if (text == null)
            return null;
        char c;
        StringBuffer n = new StringBuffer(text.length() * 2);
        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);
            switch (c) {
                case '&':
                    n.append("&amp;");
                    break;
                case '<':
                    n.append("&lt;");
                    break;
                case '>': // FIX for sourceforge bug #802520 ("]]>" needs encoding)
                    n.append("&gt;");
                    break;
                case '"':

                    n.append("&quot;");

                    break;
                case '\'':

                    n.append("&apos;");

                    break;
                default: {
                    n.append(c);
                    break;
                }
            }
        }


        return n.toString();
    }

}
