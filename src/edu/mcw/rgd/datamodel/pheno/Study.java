package edu.mcw.rgd.datamodel.pheno;

/**
 * @author jdepons
 * @since 2/7/11
 */
public class Study {

    private int id;
    private String name;
    private String source;
    private String type;
    private Integer refRgdId;
    private String dataType;
    private String geoSeriesAcc;
    private int curationStatus = -1;
    private String lastModifiedBy;
    private String createdBy;



    public int getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(int curationStatus) {
        this.curationStatus = curationStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRefRgdId() {
        return refRgdId;
    }

    public void setRefRgdId(Integer refRgdId) {
        this.refRgdId = refRgdId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getGeoSeriesAcc() {
        return geoSeriesAcc;
    }

    public void setGeoSeriesAcc(String geoSeriesAcc) {
        this.geoSeriesAcc = geoSeriesAcc;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
