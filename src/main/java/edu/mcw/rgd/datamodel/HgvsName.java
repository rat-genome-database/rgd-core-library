package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 3/12/14
 * Time: 2:41 PM
 * <p>
 * represents a row in HGVS_NAMES table
 */
public class HgvsName implements Dumpable {

    private int rgdId;
    private String type;
    private String name;

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * hgvs name and hgvs name type must match
     * @param o HgvsName object to evaluate equality
     * @return true or false
     */
    public boolean equalsByValue(HgvsName o) {
        return Utils.stringsAreEqualIgnoreCase(o.getType(), this.getType()) &&
                Utils.stringsAreEqualIgnoreCase(o.getName(), this.getName());
    }

    @Override
    public String toString() {
        return "RGD_ID="+rgdId + ", TYPE="+type+", NAME="+name;
    }

    /**
     * dumps object attributes as pipe delimited string
     * @return pipe-delimited String of object attributes
     */
    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("RGD_ID", getRgdId())
            .put("TYPE", getType())
            .put("NAME", getName())
            .dump();
    }
}
