package edu.mcw.rgd.datamodel.ontology;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 6/30/14
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SNVAnnotation extends Annotation {

    private int geneRgdId;
    private String concreteTermAccId;

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public String getConcreteTermAccId() {
        return concreteTermAccId;
    }

    public void setConcreteTermAccId(String concreteTermAccId) {
        this.concreteTermAccId = concreteTermAccId;
    }
}
