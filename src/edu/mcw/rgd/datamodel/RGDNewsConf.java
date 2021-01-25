package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

import java.util.Date;


public class RGDNewsConf {
    private int newsId;
    private String displayText;
    private String redirectLink;
    private String contentType;
    private String strongText;
    private Date date;

    public RGDNewsConf() {}

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public void setStrongText(String strongText) {
        this.strongText = strongText;
    }

    public String getStrongText() {
        return strongText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        RGDNewsConf nc = (RGDNewsConf)  obj;
        return Utils.stringsAreEqual(displayText,nc.getDisplayText()) && Utils.stringsAreEqual(redirectLink, nc.getRedirectLink())
                && Utils.stringsAreEqual(contentType, nc.getContentType()) && Utils.stringsAreEqual(strongText, nc.getStrongText());
    }

    @Override
    public int hashCode() {
        return Utils.defaultString(displayText).hashCode() ^ Utils.defaultString(redirectLink).hashCode()
                ^ Utils.defaultString(contentType).hashCode() ^ Utils.defaultString(strongText).hashCode();
    }
}