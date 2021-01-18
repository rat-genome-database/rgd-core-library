package edu.mcw.rgd.datamodel;

public class RGDNewsConf {
    private int newsId;
    private String displayText;
    private String redirectLink;
    private String contentType;
    private String strongText;

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
}