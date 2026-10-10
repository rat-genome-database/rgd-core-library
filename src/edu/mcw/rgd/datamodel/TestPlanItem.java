package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * One page or tool in the site test plan, with its assignment, result and sign-off (table TEST_PLAN_ITEMS).
 */
public class TestPlanItem {

    public static final String[] STATUSES = {"Not started", "In progress", "Passed", "Failed", "Blocked"};

    private String itemId;
    private String area;
    private String name;
    private String path;
    private String checkText;
    private String covers;
    private boolean curation;
    private boolean noCompare;
    private int sortOrder;
    private String assignee;
    private String status;
    private String notes;
    private String signedBy;
    private Date signedDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCheckText() {
        return checkText;
    }

    public void setCheckText(String checkText) {
        this.checkText = checkText;
    }

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public boolean isCuration() {
        return curation;
    }

    public void setCuration(boolean curation) {
        this.curation = curation;
    }

    public boolean isNoCompare() {
        return noCompare;
    }

    public void setNoCompare(boolean noCompare) {
        this.noCompare = noCompare;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
