package edu.mcw.rgd.datamodel;

public class ProjectFile {

    private int rgdid;
    private String protocolName;
    private int fileKey;
    private String protocol;
    private String projectFileType;
    private String downloadUrl;

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProjectFileType() {
        return projectFileType;
    }

    public void setProjectFileType(String projectFileType) {
        this.projectFileType = projectFileType;
    }

    public int getFileKey() {
        return fileKey;
    }

    public void setFileKey(int fileKey) {
        this.fileKey = fileKey;
    }

    public int getRgdid() {
        return rgdid;
    }

    public void setRgdid(int rgdid) {
        this.rgdid = rgdid;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}
