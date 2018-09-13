package edu.mcw.rgd.datamodel.models;

import edu.mcw.rgd.datamodel.Reference;



import java.util.Comparator;
import java.util.List;

/**
 * Created by jthota on 9/2/2016.
 */
public class GeneticModel   {
    private  String gene;
    private  String geneSymbol;
    private String alleleSymbol;
    private String strainSymbol;
    private String availability;
    private String source;
    private int geneRgdId;
    private int strainRgdId;
    private int alleleRgdId;
    private List<String> aliases;

    private String parentTermAcc;
    private int experimentRecordCount;
    private String phenominerUrl;
    private String backgroundStrain;
    private int backgroundStrainRgdId;
    private String method;
   private List<Reference> references;
    private String lastStatus;

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public int getBackgroundStrainRgdId() {
        return backgroundStrainRgdId;
    }

    public void setBackgroundStrainRgdId(int backgroundStrainRgdId) {
        this.backgroundStrainRgdId = backgroundStrainRgdId;
    }

    public String getBackgroundStrain() {
          return backgroundStrain;
    }

    public void setBackgroundStrain(String backgroundStrain) {
        this.backgroundStrain = backgroundStrain;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParentTermAcc() {
        return parentTermAcc;
    }

    public void setParentTermAcc(String parentTermAcc) {
        this.parentTermAcc = parentTermAcc;
    }

    public int getExperimentRecordCount() {
        return experimentRecordCount;
    }

    public void setExperimentRecordCount(int experimentRecordCount) {
        this.experimentRecordCount = experimentRecordCount;
    }

    public String getPhenominerUrl() {
        return phenominerUrl;
    }

    public void setPhenominerUrl(String phenominerUrl) {
        this.phenominerUrl = phenominerUrl;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getAlleleSymbol() {
        return alleleSymbol;
    }

    public void setAlleleSymbol(String alleleSymbol) {
        this.alleleSymbol = alleleSymbol;
    }

    public String getStrainSymbol() {
        return strainSymbol;
    }

    public void setStrainSymbol(String strainSymbol) {
        this.strainSymbol = strainSymbol;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }

    public int getAlleleRgdId() {
        return alleleRgdId;
    }

    public void setAlleleRgdId(int alleleRgdId) {
        this.alleleRgdId = alleleRgdId;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public static Comparator<GeneticModel> GeneSymbolComparator
            = new Comparator<GeneticModel>() {

        @Override
        public int compare(GeneticModel o1, GeneticModel o2) {
            String geneSymbol1=o1.getGeneSymbol().toLowerCase();
            String geneSymbol2=o2.getGeneSymbol().toLowerCase();
            return geneSymbol1.compareTo(geneSymbol2);
        }



    };

}
