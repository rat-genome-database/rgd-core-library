package edu.mcw.rgd.datamodel;

public class ProjectFile {
    private int fileKey;
    private int rgdId;
    private String projectFileType;
    private String fileTypeName;
    private String downloadUrl;

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

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

}
