package edu.mcw.rgd.util;


/**
 * User: GKowalski
 * Date: 1/11/12
 */
public final class EqualsUtil {

    static public boolean areEqual(String aThis, String aThat) {
        //System.out.println("boolean");
        if (aThis == null) {
            if (aThat == null) {
                return true;
            } else {
                return false;
            }
        }
        return aThis.equals(aThat);
    }

    static public boolean areEqual(boolean aThis, boolean aThat) {
        //System.out.println("boolean");
        return aThis == aThat;
    }

    static public boolean areEqual(char aThis, char aThat) {
        //System.out.println("char");
        return aThis == aThat;
    }

    static public boolean areEqual(long aThis, long aThat) {
        /*
        * Implementation Note
        * Note that byte, short, and int are handled by this method, through
        * implicit conversion.
        */
        return aThis == aThat;
    }

    static public boolean areEqual(float aThis, float aThat) {
        //System.out.println("float");
        return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
    }

    static public boolean areEqual(double aThis, double aThat) {
        //System.out.println("double");
        return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
    }
}