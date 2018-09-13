package edu.mcw.rgd.datamodel.ontologyx;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.process.Dumper;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 8/21/13
 * Time: 12:30 PM
 * represent a dbxref for term definition
 */
public class TermXRef implements Dumpable {

    private int key;
    private String termAcc;
    private String xrefType;
    private String xrefValue;
    private String xrefDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermXRef termXRef = (TermXRef) o;

        if (termAcc != null ? !termAcc.equals(termXRef.termAcc) : termXRef.termAcc != null) return false;
        if (xrefType != null ? !xrefType.equals(termXRef.xrefType) : termXRef.xrefType != null) return false;
        if (xrefValue != null ? !xrefValue.equals(termXRef.xrefValue) : termXRef.xrefValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = termAcc != null ? termAcc.hashCode() : 0;
        result = 31 * result + (xrefType != null ? xrefType.hashCode() : 0);
        result = 31 * result + (xrefValue != null ? xrefValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if( termAcc!=null ) {
            buf.append(termAcc).append(" ");
        }
        if( xrefType!=null ) {
            buf.append(xrefType);
        }
        buf.append(":");
        if( xrefValue!=null ) {
            buf.append(xrefValue);
        }
        if( xrefDescription!=null ) {
            buf.append(" \"").append(xrefDescription).append("\"");
        }
        return buf.toString();
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("XREF_KEY", key)
            .put("TERM_ACC", termAcc)
            .put("XREF_TYPE", xrefType)
            .put("XREF_VALUE", xrefValue)
            .put("XREF_DESC", xrefDescription)
            .dump();
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public String getXrefType() {
        return xrefType;
    }

    public void setXrefType(String xrefType) {
        this.xrefType = xrefType;
    }

    public String getXrefValue() {
        return xrefValue;
    }

    public void setXrefValue(String xrefValue) {
        this.xrefValue = xrefValue;
    }

    public String getXrefDescription() {
        return xrefDescription;
    }

    public void setXrefDescription(String xrefDescription) {
        this.xrefDescription = xrefDescription;
    }
}
