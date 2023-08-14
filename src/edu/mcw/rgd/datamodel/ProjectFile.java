package edu.mcw.rgd.datamodel;

public class ProjectFile {

    public int getRgdid() {
        return rgdid;
    }

    public void setRgdid(int rgdid) {
        this.rgdid = rgdid;
    }

    public String getProject_file_type() {
        return project_file_type;
    }

    public void setProject_file_type(String project_file_type) {
        this.project_file_type = project_file_type;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    private int rgdid;
    private String project_file_type;
    private String download_url;

}
