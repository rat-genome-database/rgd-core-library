package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;
import oracle.jdbc.OracleBlob;
import java.sql.Blob;
import java.sql.Date;

public class StrainFiles {
    private int strainId;
    private String fileType;
    private Blob fileData;
    private String contentType;
    private String fileName;
    private Date lastModifiedDate;
    private String modifiedBy;

    public int getStrainId() { return strainId; }
    public String getFileType() { return fileType; }
    public Blob getFileData() { return fileData; }
    public String getContentType() { return contentType; }
    public String getFileName() { return fileName; }
    public Date getLastModifiedDate() { return lastModifiedDate; }
    public String getModifiedBy() { return modifiedBy; }

    public void setStrainId(int strainId) { this.strainId = strainId; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFileData(Blob fileData) { this.fileData = fileData; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setLastModifiedDate(Date lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

}