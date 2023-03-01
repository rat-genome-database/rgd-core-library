package edu.mcw.rgd.datamodel;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Sep 23, 2009
 * Time: 2:04:33 PM
 */
public class GeneAssociation {

    private Gene gene;
    private int ncbiGeneId;

    private List<String> proteinId;
    private String protienLength = "";

    private List<MapData> mappings = new ArrayList<MapData>();

    private List<OMIMMapping> OMIMMappings = new ArrayList();


    //String chromosome = "";
    //long startPos;
    //long stopPos;

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public int getEntrezGeneId() {
        return ncbiGeneId;
    }

    public void setEntrezGeneId(int ncbiGeneId) {
        this.ncbiGeneId = ncbiGeneId;
    }

    public List<String> getProteinId() {
        return proteinId;
    }

    public void setProteinId(List<String> proteinId) {
        this.proteinId = proteinId;
    }

    public String getProtienLength() {
        return protienLength;
    }

    public void setProtienLength(String protienLength) {
        this.protienLength = protienLength;
    }

    public List<MapData> getMappings() {
        return mappings;
    }

    public void setMappings(List<MapData> mappings) {
        this.mappings = mappings;
    }

    public List getOMIMMappings() {
        return OMIMMappings;
    }

    public void setOMIMMappings(List OMIMMappings) {
        this.OMIMMappings = OMIMMappings;
    }
}
