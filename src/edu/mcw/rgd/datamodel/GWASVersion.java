package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Utils;

public class GWASVersion {
    private int gwasId;
    private String version;

    public int getGwasId() {
        return gwasId;
    }

    public void setGwasId(int gwasId) {
        this.gwasId = gwasId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        GWASVersion v = (GWASVersion) obj;
        return gwasId == v.getGwasId() && Utils.stringsAreEqualIgnoreCase(version, v.getVersion());
    }

    @Override
    public int hashCode() {
        return gwasId ^ Utils.defaultString(version).hashCode();
    }
}
