package edu.mcw.rgd.datamodel;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Aug 31, 2009
 * Time: 2:20:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult implements Serializable {


    String error = "";

    GeneAssociation geneAssociation;

    List<VariantResult> variantResults = new ArrayList();

    public List<VariantResult> getVariantResults() {
        return variantResults;
    }

    public void setVariantResults(List<VariantResult> variantResults) {
        this.variantResults = variantResults;
    }

    public int getVariantCount() {
        return variantResults.size();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public GeneAssociation getGeneAssociation() {
        return geneAssociation;
    }

    public void setGeneAssociation(GeneAssociation geneAssociation) {
        this.geneAssociation = geneAssociation;
    }
}
