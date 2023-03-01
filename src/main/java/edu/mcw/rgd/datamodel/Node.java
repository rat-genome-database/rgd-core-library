package edu.mcw.rgd.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jthota on 6/17/2016.
 */
public class Node {
    private String id;
    private String name;
    private String description;
    private String href;
    private String species;
    private String uniprotUrl;
    private String gene;
    private String nodeColor;
    private String parent;
    private int speciesTypeKey;
/*----------------------------------TEMP CODE----------------------------------------------------------------------*/
    private List<String> hrefs= new ArrayList<>();
    private List<String> geneSymbols= new ArrayList<>();
    private List<Integer> geneRgdIds= new ArrayList<>();
    private int geneSymbolCount;
    private int hrefCount;

    public List<String> getHrefs() {
        return hrefs;
    }

    public void setHrefs(List<String> hrefs) {
        this.hrefs = hrefs;
    }

    public List<String> getGeneSymbols() {
        return geneSymbols;
    }

    public void setGeneSymbols(List<String> geneSymbols) {
        this.geneSymbols = geneSymbols;
    }

    public List<Integer> getGeneRgdIds() {
        return geneRgdIds;
    }

    public void setGeneRgdIds(List<Integer> geneRgdIds) {
        this.geneRgdIds = geneRgdIds;
    }

    public int getGeneSymbolCount() {
        return geneSymbolCount;
    }

    public void setGeneSymbolCount(int geneSymbolCount) {
        this.geneSymbolCount = geneSymbolCount;
    }

    public int getHrefCount() {
        return hrefCount;
    }

    public void setHrefCount(int hrefCount) {
        this.hrefCount = hrefCount;
    }

    /*---------------------------------------------------END TEMP OODE----------------------------------------------*/


    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getNodeColor() {
        return nodeColor;
    }

    public void setNodeColor(String nodeColor) {
        this.nodeColor = nodeColor;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getUniprotUrl() {
        return uniprotUrl;
    }

    public void setUniprotUrl(String uniprotUrl) {
        this.uniprotUrl = uniprotUrl;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{data: {");
        buffer.append("id: "+ "\"" + id + "\"" + ", ");
        buffer.append("name: \"" + name + "\", ");
        buffer.append("href: \"" + href + "\", ");
        buffer.append("species: \"" + species + "\", ");
        buffer.append("speciesTypeKey: \"" + speciesTypeKey + "\", ");
        buffer.append("uniprotUrl: \"" + uniprotUrl + "\", ");
        buffer.append("gene: \"" + gene + "\", ");
        int i=0;
        for(String gene:geneSymbols){
            buffer.append("gene" +i + ": \"" + gene + "\", ");
            i++;
        }
        int j=0;
        for(String href:hrefs){
            buffer.append("href" + j + ": \"" + href + "\", ");
            j++;
        }
        buffer.append("hrefCount: \"" + hrefCount + "\", ");
        buffer.append("geneSymbolCount: \"" + geneSymbolCount + "\", ");
        buffer.append("nodeColor:\"" + nodeColor +"\", ");
        buffer.append("description: \"" + description + "\" },");
        if(species.toLowerCase().equals("rat")){
            buffer.append("classes: \"rat\" }");
        }else {
            if(species.toLowerCase().equals("mouse")){
                buffer.append("classes: \"mouse\" }");
            }else {
                buffer.append("classes: \"human\" }");
            }
        }
        return buffer.toString();
    }
}

