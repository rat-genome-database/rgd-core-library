package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/27/11
 * Time: 12:41 PM
 * represents a row in PORTAL_VER1 table
 */
public class PortalVersion {

    private int portalVerId; // unique key
    private int portalKey;
    private Date dateLastUpdated;
    private String portalVerStatus;
    private long portalBuildNum;
    private String chartXmlCc;
    private String chartXmlBp;
    private String chartXmlMp;

    public int getPortalVerId() {
        return portalVerId;
    }

    public void setPortalVerId(int portalVerId) {
        this.portalVerId = portalVerId;
    }

    public int getPortalKey() {
        return portalKey;
    }

    public void setPortalKey(int portalKey) {
        this.portalKey = portalKey;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getPortalVerStatus() {
        return portalVerStatus;
    }

    public void setPortalVerStatus(String portalVerStatus) {
        this.portalVerStatus = portalVerStatus;
    }

    public long getPortalBuildNum() {
        return portalBuildNum;
    }

    public void setPortalBuildNum(long portalBuildNum) {
        this.portalBuildNum = portalBuildNum;
    }

    public String getChartXmlCc() {
        return chartXmlCc;
    }

    public void setChartXmlCc(String chartXmlCc) {
        this.chartXmlCc = chartXmlCc;
    }

    public String getChartXmlBp() {
        return chartXmlBp;
    }

    public void setChartXmlBp(String chartXmlBp) {
        this.chartXmlBp = chartXmlBp;
    }

    public String getChartXmlMp() {
        return chartXmlMp;
    }

    public void setChartXmlMp(String chartXmlMp) {
        this.chartXmlMp = chartXmlMp;
    }
}
