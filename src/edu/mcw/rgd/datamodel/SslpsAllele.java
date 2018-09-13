package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 21, 2007
 * Time: 9:28:50 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Bean class for an RGD SSLPS_Allele.
 *
 * Data is currently contained in the sslps_allele table
 */
public class SslpsAllele {

    private int Key;
    private int size1;
    private int size2;
    private int sslpKey;
    private int strainKey;
    private String notes;
    private String strainSymbol;
    private int strainRgdId;

    public String getStrainSymbol() {
        return strainSymbol;
    }

    public void setStrainSymbol(String strainSymbol) {
        this.strainSymbol = strainSymbol;
    }

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }

    public int getSize1() {
        return size1;
    }

    public void setSize1(int size1) {
        this.size1 = size1;
    }

    public int getSize2() {
        return size2;
    }

    public void setSize2(int size2) {
        this.size2 = size2;
    }

    public int getSslpKey() {
        return sslpKey;
    }

    public void setSslpKey(int sslpKey) {
        this.sslpKey = sslpKey;
    }

    public int getStrainKey() {
        return strainKey;
    }

    public void setStrainKey(int strainKey) {
        this.strainKey = strainKey;
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int Key) {
        this.Key = Key;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
