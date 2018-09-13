package edu.mcw.rgd.datamodel;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 12/28/11
 * Time: 1:18 PM
 * represents a row from PORTAL_CAT1 table
 */
public class PortalCat {

    private int portalCatId;
    private int portalVerId;
    private String categoryName;
    private String categoryTermAcc;
    private Integer parentCatId;
    private String summaryTableHtml;
    private String gViewerXmlRat;
    private String gViewerXmlMouse;
    private String gViewerXmlHuman;
    private String geneInfoHtml;
    private String qtlInfoHtml;
    private String strainInfoHtml;
    private int annotObjCntRat;
    private int annotObjCntWithChildrenRat;
    private int annotObjCntWithChildrenMouse;
    private int annotObjCntWithChildrenHuman;
    private String chartXmlCc; // GOSLIM CC
    private String chartXmlBp; // GOSLIM BP
    private String chartXmlMp; // GOSLIM MP

    public int getPortalCatId() {
        return portalCatId;
    }

    public void setPortalCatId(int portalCatId) {
        this.portalCatId = portalCatId;
    }

    public int getPortalVerId() {
        return portalVerId;
    }

    public void setPortalVerId(int portalVerId) {
        this.portalVerId = portalVerId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTermAcc() {
        return categoryTermAcc;
    }

    public void setCategoryTermAcc(String categoryTermAcc) {
        this.categoryTermAcc = categoryTermAcc;
    }

    public Integer getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(Integer parentCatId) {
        this.parentCatId = parentCatId;
    }

    public String getSummaryTableHtml() {
        return summaryTableHtml;
    }

    public void setSummaryTableHtml(String summaryTableHtml) {
        this.summaryTableHtml = summaryTableHtml;
    }

    public String getgViewerXmlRat() {
        return gViewerXmlRat;
    }

    public void setgViewerXmlRat(String gViewerXmlRat) {
        this.gViewerXmlRat = gViewerXmlRat;
    }

    public String getgViewerXmlMouse() {
        return gViewerXmlMouse;
    }

    public void setgViewerXmlMouse(String gViewerXmlMouse) {
        this.gViewerXmlMouse = gViewerXmlMouse;
    }

    public String getgViewerXmlHuman() {
        return gViewerXmlHuman;
    }

    public void setgViewerXmlHuman(String gViewerXmlHuman) {
        this.gViewerXmlHuman = gViewerXmlHuman;
    }

    public String getGeneInfoHtml() {
        return geneInfoHtml;
    }

    public void setGeneInfoHtml(String geneInfoHtml) {
        this.geneInfoHtml = geneInfoHtml;
    }

    public String getQtlInfoHtml() {
        return qtlInfoHtml;
    }

    public void setQtlInfoHtml(String qtlInfoHtml) {
        this.qtlInfoHtml = qtlInfoHtml;
    }

    public String getStrainInfoHtml() {
        return strainInfoHtml;
    }

    public void setStrainInfoHtml(String strainInfoHtml) {
        this.strainInfoHtml = strainInfoHtml;
    }

    public int getAnnotObjCntRat() {
        return annotObjCntRat;
    }

    public void setAnnotObjCntRat(int annotObjCntRat) {
        this.annotObjCntRat = annotObjCntRat;
    }

    public int getAnnotObjCntWithChildrenRat() {
        return annotObjCntWithChildrenRat;
    }

    public void setAnnotObjCntWithChildrenRat(int annotObjCntWithChildrenRat) {
        this.annotObjCntWithChildrenRat = annotObjCntWithChildrenRat;
    }

    public int getAnnotObjCntWithChildrenMouse() {
        return annotObjCntWithChildrenMouse;
    }

    public void setAnnotObjCntWithChildrenMouse(int annotObjCntWithChildrenMouse) {
        this.annotObjCntWithChildrenMouse = annotObjCntWithChildrenMouse;
    }

    public int getAnnotObjCntWithChildrenHuman() {
        return annotObjCntWithChildrenHuman;
    }

    public void setAnnotObjCntWithChildrenHuman(int annotObjCntWithChildrenHuman) {
        this.annotObjCntWithChildrenHuman = annotObjCntWithChildrenHuman;
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
