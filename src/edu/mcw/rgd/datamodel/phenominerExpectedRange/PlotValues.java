package edu.mcw.rgd.datamodel.phenominerExpectedRange;

import java.util.List;

/**
 * Created by jthota on 5/22/2018.
 */
public class PlotValues {


    private List<Double> y;
    private String name;
    private String type;


    public List<Double> getY() {
        return y;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{ y:").append(y.toString()).append(",").append("name:").append("'").append(name).append("',").append("type:'").append(type).append("'}");
        return sb.toString();
    }
}