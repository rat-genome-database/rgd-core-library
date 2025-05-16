package edu.mcw.rgd.datamodel;

public class Jbrowse2UrlConfig {
    private int configKey;
    private int mapKey;
    private int objectKey;
    private String assembly;
    private String tracks;
    private String chrPrefix;

    public int getConfigKey() {
        return configKey;
    }

    public void setConfigKey(int configKey) {
        this.configKey = configKey;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public int getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(int objectKey) {
        this.objectKey = objectKey;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }

    public String getChrPrefix() {
        return chrPrefix;
    }

    public void setChrPrefix(String chrPrefix) {
        this.chrPrefix = chrPrefix;
    }
}
