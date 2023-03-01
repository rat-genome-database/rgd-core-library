package edu.mcw.rgd.datamodel;

import java.util.*;

/**
 * Created by jthota on 6/17/2016.
 */
public class Edge {
    private int id;
    private String name;
    private String source;
    private String target;
    private String srcSymbol;
    private String tSymbol;
    private String description;
    private String sourceDb;
    private String pubmedId;
    private String imexId;
    private List<InteractionAttribute> attributes;
    private String pudmedUrl;
    private String IMEX_URL="http://www.ebi.ac.uk/intact/imex/main.xhtml?query=";
    private String ontUrl;
    private String typeClass;
    private String srcGene;
    private String targetGene;
    private String srcSpecies;
    private String targetSpecies;
    private String srcGeneUrl;
    private String targetGeneUrl;
    private String uniprotUrl;
    /**************************************************************TEMP CODE*********************************************/
    private List<String> srcGeneList;
    private  List<String> targetGeneList;
    private List<Integer> srcGeneRgdIdList;
    private List<Integer> targetGeneRgdIdList;
    private List<String> srcGeneURLs;
    private List<String> targetGeneURLs;

    public List<String> getSrcGeneList() {
        return srcGeneList;
    }

    public void setSrcGeneList(List<String> srcGeneList) {
        this.srcGeneList = srcGeneList;
    }

    public List<String> getTargetGeneList() {
        return targetGeneList;
    }

    public void setTargetGeneList(List<String> targetGeneList) {
        this.targetGeneList = targetGeneList;
    }

    public List<Integer> getSrcGeneRgdIdList() {
        return srcGeneRgdIdList;
    }

    public void setSrcGeneRgdIdList(List<Integer> srcGeneRgdIdList) {
        this.srcGeneRgdIdList = srcGeneRgdIdList;
    }

    public List<Integer> getTargetGeneRgdIdList() {
        return targetGeneRgdIdList;
    }

    public void setTargetGeneRgdIdList(List<Integer> targetGeneRgdIdList) {
        this.targetGeneRgdIdList = targetGeneRgdIdList;
    }

    /************************************************************END TEMP CODE**********************************************/

    public String getUniprotUrl() {
        return uniprotUrl;
    }

    public void setUniprotUrl(String uniprotUrl) {
        this.uniprotUrl = uniprotUrl;
    }

    public String getSrcGeneUrl() {
        return srcGeneUrl;
    }

    public void setSrcGeneUrl(String srcGeneUrl) {
        this.srcGeneUrl = srcGeneUrl;
    }

    public String getTargetGeneUrl() {
        return targetGeneUrl;
    }

    public void setTargetGeneUrl(String targetGeneUrl) {
        this.targetGeneUrl = targetGeneUrl;
    }

    public String getSrcSpecies() {
        return srcSpecies;
    }

    public void setSrcSpecies(String srcSpecies) {
        this.srcSpecies = srcSpecies;
    }

    public String getTargetSpecies() {
        return targetSpecies;
    }

    public void setTargetSpecies(String targetSpecies) {
        this.targetSpecies = targetSpecies;
    }

    public String getSrcGene() {
        return srcGene;
    }

    public void setSrcGene(String srcGene) {
        this.srcGene = srcGene;
    }

    public String getTargetGene() {
        return targetGene;
    }

    public void setTargetGene(String trgetGene) {
        this.targetGene = trgetGene;
    }

    public String getSrcSymbol() {
        return srcSymbol;
    }

    public void setSrcSymbol(String srcSymbol) {
        this.srcSymbol = srcSymbol;
    }

    public String gettSymbol() {
        return tSymbol;
    }

    public void settSymbol(String tSymbol) {
        this.tSymbol = tSymbol;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getOntUrl() {
        return ontUrl;
    }

    public void setOntUrl(String ontUrl) {
        this.ontUrl = ontUrl;
    }

    public String getPudmedUrl() {
        return pudmedUrl;
    }

    public void setPudmedUrl(String pudmedUrl) {
        this.pudmedUrl = pudmedUrl;
    }

    public List<InteractionAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<InteractionAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getSourceDb() {
        return sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getImexId() {
        return imexId;
    }

    public void setImexId(String imexId) {
        this.imexId = imexId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{data: {");
        buffer.append("id: \"" + id + "\", ");
        buffer.append("name: \"" + name + "\", ");
        buffer.append("source: \"" + source +"\", ");
        buffer.append("target: \"" + target +"\", ");
        buffer.append("srcSymbol: \"" + srcSymbol +"\", ");
        buffer.append("tSymbol: \"" + tSymbol +"\", ");
        java.util.Map<String, List<String>> attributesMap= new HashMap<>();
        List<String> imex=new ArrayList<>();
        List<String> pubmed=new ArrayList<>();
        List<String> other= new ArrayList<>();
        List<String> source=new ArrayList<>();
        List<String> intAc= new ArrayList<>();
        for(InteractionAttribute a: attributes){
            String name= a.getAttributeName();
            String value= a.getAttributeValue().trim();

            if(name.toLowerCase().equals("imex")){
                imex.add(value);
                continue;
            }
            if(name.toLowerCase().equals("pubmed")){
                pubmed.add(value);
                continue;
            }
            if(name.toLowerCase().equals("sourcedb")){
                source.add(value);
                continue;
            }
            if(name.toLowerCase().equals("interaction_ac")){
                intAc.add(value);
                continue;
            }
            other.add(value);

        }
        attributesMap.put("imex", imex);
        attributesMap.put("pubmed", pubmed);
        attributesMap.put("sourcedb", source);
        attributesMap.put("intAc", intAc);
        attributesMap.put("other", other);

        for(java.util.Map.Entry entry:attributesMap.entrySet()){
            String str=entry.getValue().toString().replaceAll("\\["  , "");
            String strValue=str.replaceAll("\\]"  , "");
            //     buffer.append(entry.getKey()+":" + "\"" +strValue+ "\", ");
            List list= (List) entry.getValue();
            Iterator i$= list.iterator();
            int j=0;
            String countName=new String();
            while(i$.hasNext()){
                String attName= (String) entry.getKey();
                countName=attName;
                String attValue= (String) i$.next();

                buffer.append( attName + j +":"+ "\"" + attValue+ "\", ");
                j++;
            }
            buffer.append( countName + "count:" +j + ",");
        }

        buffer.append("pubmedUrl: \"" + pudmedUrl +"\", ");
        buffer.append("imexUrl: \"" + IMEX_URL +"\", ");
        buffer.append(("ontUrl: \""+ ontUrl + "\", "));
        buffer.append(("typeColor: \""+ typeClass + "\", "));
        buffer.append(("srcGene: \""+ srcGene + "\", "));
        buffer.append(("tagetGene: \""+ targetGene + "\", "));
        buffer.append("description: \"" + description +"\" }}");


        return buffer.toString();
    }

}

