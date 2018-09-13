package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 26, 2011
 * Time: 12:45:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Portal {

    private int key;
    private String urlName;
    private String fullName;
    private String pageName;
    private String pageTitle;
    private String imageUrl;
    private String categoryPageDescription;
    private String subCategoryDescription;
    private String summaryDescription;
    private Date lastUpdated;
    private String portalType;
    private int masterPortalKey;

    public int getMasterPortalKey() {
        return masterPortalKey;
    }

    public void setMasterPortalKey(int masterPortalKey) {
        this.masterPortalKey = masterPortalKey;
    }

    public String getPortalType() {
        return portalType;
    }

    public void setPortalType(String portalType) {
        this.portalType = portalType;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryPageDescription() {
        return categoryPageDescription;
    }

    public void setCategoryPageDescription(String categoryPageDescription) {
        this.categoryPageDescription = categoryPageDescription;
    }

    public String getSubCategoryDescription() {
        return subCategoryDescription;
    }

    public void setSubCategoryDescription(String subCategoryDescription) {
        this.subCategoryDescription = subCategoryDescription;
    }

    public String getSummaryDescription() {
        return summaryDescription;
    }

    public void setSummaryDescription(String summaryDescription) {
        this.summaryDescription = summaryDescription;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
