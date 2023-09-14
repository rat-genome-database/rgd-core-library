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

    public String getProtocol_name() {
        return protocol_name;
    }

    public void setProtocol_name(String protocol_name) {
        this.protocol_name = protocol_name;
    }

    private String protocol_name;

    public int getFile_key() {
        return file_key;
    }

    public void setFile_key(int file_key) {
        this.file_key = file_key;
    }

    private int file_key;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private String protocol;

}
