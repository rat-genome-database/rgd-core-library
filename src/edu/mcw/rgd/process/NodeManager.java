package edu.mcw.rgd.process;

import edu.mcw.rgd.dao.impl.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.reporting.Link;
import org.json.JSONObject;
import java.util.*;
import java.util.Map;

/**
 * Created by jthota on 6/17/2016.
 */
public class NodeManager {
    private Map<String, String> typeMap= new HashMap<>();
    private JSONObject typeMapJson= new JSONObject();
    private List<Interaction> interactions = new ArrayList<>();
    private Set<Edge> edgeList= new HashSet<>();
    private Set log= new HashSet<>();
    private Set<String> matched= new HashSet<>();
    private List<Node> nodeList= new ArrayList<>();
    private  Map<Integer, Protein> proteinMap= new HashMap<>();
    private Map<String, String> accTypeMap= new HashMap<>();
    private Map<Integer, Set<Gene>> assocGeneMap= new HashMap<>();
    InteractionsDAO idao= new InteractionsDAO();
    ProteinDAO pdao = new ProteinDAO();
    AssociationDAO associationDAO= new AssociationDAO();
    InteractionAttributesDAO adao= new InteractionAttributesDAO();
    XdbIdDAO xdao= new XdbIdDAO();
    OntologyXDAO ontdao= new OntologyXDAO();

    public Map<String, String> getAccTypeMap() {
        return accTypeMap;
    }

    public void setAccTypeMap(Map<String, String> accTypeMap) {
        this.accTypeMap = accTypeMap;
    }

    public Map<Integer, Protein> getProteinMap() {
        return proteinMap;
    }

    public void setProteinMap(Map<Integer, Protein> proteinMap) {
        this.proteinMap = proteinMap;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public Map<String, String> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<String, String> typeMap) {
        this.typeMap = typeMap;
    }

    public JSONObject getTypeMapJson() {
        return typeMapJson;
    }

    public void setTypeMapJson(JSONObject typeMapJson) {
        this.typeMapJson = typeMapJson;
    }

    public Set<String> getMatched() {
        return matched;
    }

    public void setMatched(Set<String> matched) {
        this.matched = matched;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    /**
     * To get associated proteins of list of genes and returns list of rgdids of those proteins
     * @param geneList
     * @return list of rgdIds of associated proteins of genes.
     * @throws Exception
     */
    public List<Integer> getProteins(List<Gene> geneList) throws Exception{
        List<Integer> pRgdIds= new ArrayList<>();
        List<Integer> geneRgdIDs= new ArrayList<>();
        List<Protein> proteins = new ArrayList<>();
        List<Association> associations= new ArrayList<>();
        for(Gene g :geneList){
            int geneRgdId= g.getRgdId();
            geneRgdIDs.add(geneRgdId);
        }
        Collection[] collections= this.split(geneRgdIDs, 1000);
        for(int i=0;i<collections.length;i++){
            List<Integer> geneRgdIdsSublist= (List<Integer>) collections[i];
            associations.addAll(associationDAO.getAssociationsForDetailRgdIdList(geneRgdIdsSublist, "protein_to_gene"));
        }
        for(Association a:associations){
            int rgdId= a.getMasterRgdId();
            pRgdIds.add(rgdId);
        }
        return pRgdIds;
    }

    /**
     * To get Edges of the interactions set in getNodes() method
     * @return List of edges in STRING format
     * @throws Exception
     */
    public List<String> getEdges() throws Exception {
        List<String> edges = new ArrayList<>();
        List<Edge> elist=new ArrayList<>();

        Map<String, String> typeMap= new HashMap<>();
        Map<Integer, Protein> pMap= this.getProteinMap();//Map of Protein RGDID and Protein
        String pubmedurl = xdao.getXdbUrlnoSpecies(2);
        String uniprotUrl = xdao.getXdbUrlnoSpecies(14);
        List<Interaction> interactions=this.getInteractions();

        Set<String> typeSet= this.getTypeSet(interactions); //NonRedundant Set of InteractionTypes
        Random random= new Random();
        for(String type:typeSet){
            int r= random.nextInt(255);
            int g=random.nextInt(255);
            int b= random.nextInt(255);
            typeMap.put(type,"rgb("+ r +"," + g+ "," +b + ")"); //Map of type and distinct Color
        }
        this.setTypeMap(typeMap);
        JSONObject typeMapJson= new JSONObject(typeMap); //typeMap in JSON format
        this.setTypeMapJson(typeMapJson);

        Map<Integer, List<InteractionAttribute>> attributeMap= this.getAttributeMap(this.getInteractions()); //Map of interactionKey and List of Attributes of that interactionKey

        for (Interaction i: interactions) {    // Iterating on list of interactions
            Edge e = new Edge();
            int key = i.getInteractionKey();
            List<InteractionAttribute> attributeList =new ArrayList<>();

            /************Finding Attibutes list of this interaction key from the attributeMap***************/

            for(Map.Entry entry: attributeMap.entrySet()){
                int k= (int) entry.getKey();
                List<InteractionAttribute> aList= (List<InteractionAttribute>) entry.getValue();
                Iterator iterator= aList.iterator();
                if(k==key){
                    while(iterator.hasNext()){
                        InteractionAttribute a= (InteractionAttribute) iterator.next();
                        attributeList.add(a);
                    }
                }
            }
            /****************Finding proteins of this interaction interactors from pMap*******************/

            Protein p1=new Protein();
            Protein p2=new Protein();
            for(Map.Entry entry:pMap.entrySet()){
                int k= (int) entry.getKey(); //protein rgd_id
                if(k==i.getRgdId1()){        //compare protein rgd_id with rgd_id_1 of interaction
                    p1= (Protein) entry.getValue();
                }
                if(k==i.getRgdId2()){     //compare protein rgd_id with rgd_id_2 of interaction
                    p2=(Protein) entry.getValue();
                }

            }
            /*****************Finding Genes of proteins(interactors) from assocGeneMap*********************/
            String srcGene= new String();
            String targetGene= new String();
            int srcGeneRgdId= 0;
            int targetGeneRgdId=0;

            /**********************************TEMP CODE *************************************************/
            List<String> srcGenes= new ArrayList<>();
            List<String> targetGenes= new ArrayList<>();
            List<Integer> srcGeneRgdIds= new ArrayList<>();
            List<Integer> targetGeneRgdIds= new ArrayList<>();
            /***************************************END TEMP CODE**************************************/

            for(java.util.Map.Entry entry:this.getAssocGeneMap().entrySet()){
                int k= (int) entry.getKey();

                Set<Gene> genes= (Set<Gene>) entry.getValue();
                // if(g!=null) {
                if(genes.size()>0){
                    for(Gene g:genes){
                        if(k==p1.getRgdId()){
                            /******************************TEMP CODE***************************************/

                            srcGene=g.getSymbol();
                            srcGeneRgdId=g.getRgdId();
                            srcGenes.add(srcGene);
                            srcGeneRgdIds.add(srcGeneRgdId);


                            /****************************END TEMP CODE************************************/
                        }
                        if(k==p2.getRgdId()){

                            targetGene=g.getSymbol();
                            targetGeneRgdId= g.getRgdId();
                            /*******************************TEMP CODE************************************/
                            targetGenes.add(targetGene);
                            targetGeneRgdIds.add(targetGeneRgdId);
                            /*************************************END TEMP CODE*************************/
                        }}}
            }




            /**************Finding Interaction Type of this interaction from this.getAccTypeMap()********************/
            String typeAccId = i.getInteractionType();
            String description=new String();
            String ontUrl="/rgdweb/ontology/view.html?acc_id="+typeAccId;
            for(Map.Entry entry: this.getAccTypeMap().entrySet()){
                String accId= (String) entry.getKey();
                if(typeAccId.equals(accId)){
                    description= (String) entry.getValue();
                }
            }
            /***********Finding the Type Class of this interaction from typeMap******************/
            for(Map.Entry entry:typeMap.entrySet()){
                if(description!=null) {
                    if (description.equals(entry.getKey())) {
                        e.setTypeClass(String.valueOf(entry.getValue()));
                        break;
                    }
                }
            }


            String source = p1.getUniprotId().trim();
            String srcSymbol= p1.getSymbol();
            String target = p2.getUniprotId();
            String tSymbol=p2.getSymbol();
            String srcSpeices= SpeciesType.getCommonName(p1.getSpeciesTypeKey());
            String targetSpecies= SpeciesType.getCommonName(p2.getSpeciesTypeKey());

            e.setId(key);
            e.setName(source + "-->" + target);
            e.setSource(source);
            e.setTarget(target);
            e.setSrcSymbol(srcSymbol);
            e.settSymbol(tSymbol);
            e.setDescription(description);
            e.setAttributes(attributeList);
            e.setPudmedUrl(pubmedurl);
            e.setOntUrl(ontUrl);
            e.setSrcGene(srcGene);
            e.setTargetGene(targetGene);
            e.setSrcSpecies(srcSpeices);
            e.setTargetSpecies(targetSpecies);
            e.setSrcGeneUrl(Link.gene(srcGeneRgdId));
            e.setTargetGeneUrl(Link.gene(targetGeneRgdId));
            e.setUniprotUrl(uniprotUrl);

            /***********************************************TEMP CODE******************************************/
            List<String> srcUrls= new ArrayList<>();
            List<String> targetUrls= new ArrayList<>();
            for(int srcGRgdId: srcGeneRgdIds){
                String srcUrl= Link.gene(srcGRgdId);
                srcUrls.add(srcUrl);
            }
            for(int targetGRgdId: targetGeneRgdIds){
                String targetUrl=Link.gene(targetGRgdId);
                targetUrls.add(targetUrl);
            }
            e.setSrcGeneList(srcGenes);
            e.setSrcGeneRgdIdList(srcGeneRgdIds);
            e.setTargetGeneList(targetGenes);
            e.setTargetGeneRgdIdList(targetGeneRgdIds);

            /********************************************END TEMP CODE*******************************************/

            elist.add(e);
            edges.add(e.toString());

        }
        Set<Edge> edgeList= new HashSet<>(elist);

        this.setEdgeList(edgeList);
        return edges;
    }

    /**
     * To get the distinct interaction types of the given list of interactions
     * @param interactions
     * @return Set of distinct interaction types
     * @throws Exception
     */
    public Set<String> getTypeSet(List<Interaction> interactions) throws Exception{
        Set<String> typeSet= new HashSet<>();
        int size= interactions.size();
        Iterator i$= interactions.iterator();
        String[] typeArray= new String[size];
        Map<String, String> acctypeMap= new HashMap<>();
        int j=0;
        while(i$.hasNext() && j<=size) {
            Interaction i = (Interaction) i$.next();
            String type = i.getInteractionType();
            typeArray[j] = type;
            j++;
        }
        List<Term> terms= ontdao.getTermByAccId(typeArray);
        for(Term term:terms){
            String description= term.getTerm();
            String acc= term.getAccId();
            acctypeMap.put(acc, description);
            typeSet.add(description);
        }
        this.setAccTypeMap(acctypeMap);
        return typeSet;
    }

    /**
     * To get the list of nodes of given rgdIdList. This method is called in getNodes(List<Object> resultSet)
     * @param rgdIdList
     * @return List of Nodes in String format.
     * @throws Exception
     */
    public List<String> getNodes(List<Integer> rgdIdList) throws Exception{

        List<String> nodes= new ArrayList<>();
        List<Node> nList= new ArrayList<>();

        Set<Integer> rgdIds = new HashSet<>(rgdIdList);
        List<Integer> rgdList= new ArrayList<>(rgdIds);
        String uniprotUrl = xdao.getXdbUrlnoSpecies(14);
        List<Protein> proteinList= new ArrayList<>();
        Collection[] collections=  this.split(rgdList, 1000);
        for(int i=0;i<collections.length;i++){
            List<Integer> rgdIdSublist= (List<Integer>) collections[i];
            proteinList.addAll(pdao.getProteinsByRgdIdList(rgdIdSublist));
        }

        Map<Integer, Set<Gene>> assocGeneMap= this.getAssociatedGenes(rgdList);
        this.setAssocGeneMap(assocGeneMap);

        Map<Integer, Protein> proteinMap= new HashMap<>();
        Iterator iterator = proteinList.iterator();
        while (iterator.hasNext()) {
            Node n = new Node();
            Protein p = (Protein) iterator.next();
            String name = p.getSymbol();
            int rId= p.getRgdId();
            proteinMap.put(rId, p);
            // Gene g= new Gene();
            List<Gene> genes = new ArrayList<>();
            // int geneRgdId = 0;
            for (java.util.Map.Entry entry : assocGeneMap.entrySet()) {
                int mRgdId = (int) entry.getKey();
                Set<Gene> g = (Set<Gene>) entry.getValue();
                if (rId == mRgdId) {
                    // g= (Gene) entry.getValue();
                    genes.addAll(g);
                }
            }
            //  String geneSymbol= new String();
            //  geneSymbol=g.getSymbol();
            // geneRgdId=g.getRgdId();
            //**************************************Testing***********************************//
            List<String> geneSymbols= new ArrayList<>();
            List<Integer> geneRgdIds= new ArrayList<>();
            for (Gene g : genes) {
                String geneSymbol = g.getSymbol();
                int geneRgdId = g.getRgdId();
                geneSymbols.add(geneSymbol);
                geneRgdIds.add(geneRgdId);
            }
//**************************************************End Testing************************************//
            String id = p.getUniprotId();
            String description = p.getName();
            int speciesTypeKey = p.getSpeciesTypeKey();
            String speciesEach = SpeciesType.getCommonName(speciesTypeKey);
            n.setId(id);
            if(speciesEach.toLowerCase().equals("rat")){
                n.setNodeColor("red");
            }
            if(speciesEach.toLowerCase().equals("mouse")){
                n.setNodeColor("springgreen");
            }
            if(speciesEach.toLowerCase().equals("human")){
                n.setNodeColor("dodgerblue");
            }
            if(speciesEach.toLowerCase().equals("dog")){
                n.setNodeColor("magenta");
            }
       /*     if (geneSymbol != null) {
                if (!(name.contains(geneSymbol)))
                    n.setName(name + " (" + geneSymbol + ")");
                else
                    n.setName(name);
            }else{
                n.setName(name);
            }
            n.setSpeciesTypeKey(speciesTypeKey);
            n.setGene(geneSymbol);
            n.setDescription(description);
            n.setSpecies(speciesEach);
            n.setHref("http://rgd.mcw.edu/rgdweb/report/gene/main.html?id=" + geneRgdId);
            n.setUniprotUrl(uniprotUrl + id);
            nList.add(n);
            String nodeString= n.toString();
            nodes.add(nodeString);*/
            /*****************************************************************Test Code begins****************************/
        if(geneSymbols.isEmpty()){
            n.setName(name);
        }
        if(geneSymbols.size()>2){
            n.setName(name);
        }
            if(geneSymbols.size()>0 && geneSymbols.size()<=2){
                String geneSymbol= Utils.concatenate(geneSymbols, ",");
                n.setName(name + " (" + geneSymbol + ")");
            }
            if(geneSymbols.size()>0){
                n.setGeneSymbols(geneSymbols);
            }

            List<String> hrefs= new ArrayList<>();
            if(geneRgdIds.size()>0){
                n.setGeneRgdIds(geneRgdIds);
                for(int rgdId:geneRgdIds){
                    hrefs.add(Link.gene(rgdId));
                }
            }
            n.setSpeciesTypeKey(speciesTypeKey);
            n.setGeneSymbols(geneSymbols);
            n.setGeneSymbolCount(geneSymbols.size());
            n.setHrefs(hrefs);
            n.setHrefCount(hrefs.size());
            n.setDescription(description);
            n.setSpecies(speciesEach);
            n.setUniprotUrl(uniprotUrl + id);
            nList.add(n);
            String nodeString = n.toString();
            nodes.add(nodeString);
            /**************************************************test Code Ends****************************************************/
        }
        this.setProteinMap(proteinMap);
        this.setNodeList(nList);
        return  nodes;
    }

    /**
     * Finds RgdIds of set of Objects and gets List of Nodes by calling getNodes(rgdIdsList)
     * @param resultSet
     * @return List of Nodes in String format.
     * @throws Exception
     */
    public List<String> getNodes(Set<Object> resultSet) throws Exception {

        List<String> nodes = new ArrayList<>();
        List<Interaction> iList = new ArrayList<>();
        List<Integer> rgdIdsList = new ArrayList<>();

        Iterator i$ = resultSet.iterator();
        System.out.println("Iterate OM Result Start time: " + new Date());
        List<Integer> proteinRgdIds= new ArrayList<>();
        List<Gene> geneList= new ArrayList<>();
        List<String> matched = new ArrayList<>();

        if(resultSet.size()>0){
            while (i$.hasNext()) {
                Object o = i$.next();
                if (o instanceof Gene) {
                    Gene g = (Gene) o;
                    String symbol=g.getSymbol().toLowerCase();
                    matched.add(symbol);
                    geneList.add(g);
                } else if (o instanceof Protein) {

                    Protein p = (Protein) o;
                    int rgdId= p.getRgdId();
                    String symbol=p.getUniprotId();
                    matched.add(symbol.toLowerCase());
                    proteinRgdIds.add(rgdId);

                }

            }}

        List<Integer> geneProteinRgdIds=new ArrayList<>();
        if(geneList.size()>0){
            geneProteinRgdIds=  this.getProteins(geneList);
        }
        if(geneProteinRgdIds.size()>0){
            proteinRgdIds.addAll(geneProteinRgdIds);
        }
        if(proteinRgdIds.size()>0) {
            Collection[] collections = this.split(proteinRgdIds, 1000);
            for (int i = 0; i < collections.length; i++) {
                List<Integer> list = (List<Integer>) collections[i];
                List<Interaction> interactions = idao.getInteractionsByRgdIdsList(list);
                iList.addAll(interactions);
            }
            Iterator iterator = iList.iterator();
            while (iterator.hasNext()) {
                Interaction i = (Interaction) iterator.next();
                int rgdId1 = i.getRgdId1();
                int rgdId2 = i.getRgdId2();
                rgdIdsList.add(rgdId1);
                rgdIdsList.add(rgdId2);
            }
            System.out.println("Iterate OM Resultset END time: " + new Date());

            if (rgdIdsList.size() > 0) {
                System.out.println("getNodes(rgdidsList) : start time: " + new Date());
                nodes = this.getNodes(rgdIdsList);
                System.out.println("getNodes(rgdIdsList): END time: " + new Date());
                Set<String> matchedSet = new HashSet<>(matched);
                this.setMatched(matchedSet);
                this.setInteractions(iList);
            }
        }  else{
            return null;
        }

        System.out.println("getNodes(q,s) : End time: " + new Date());
        return nodes;
    }

    /**
     * To get Associated genes of list of protein rgdIds
     * @param rgdList
     * @return Map of protein rgdId and associated gene.
     * @throws Exception
     */
    public  Map<Integer, Set<Gene>> getAssociatedGenes(List<Integer> rgdList) throws Exception{
        Map<Integer, Set<Gene>> assocGeneMap= new HashMap<>();
        List<Association> associations= new ArrayList<>();
        List<Gene> assocGenes= new ArrayList<>();
        Collection[] collections= this.split(rgdList,1000);
        for(int i=0;i<collections.length;i++){
            List<Integer> sublist= (List<Integer>) collections[i];
            associations.addAll(associationDAO.getAssociationsForMRgdIdList(sublist, "protein_to_gene"));
            assocGenes.addAll(associationDAO.getAssociatedGenesForMasterRgdIdList(sublist, "protein_to_gene"));
        }
        for(Association a: associations){
            Set<Gene> genes= new HashSet<>();
            int mRgdId= a.getMasterRgdId();
            Set<Integer> geneRgdIdSet= new HashSet<>();
            for(Association association:associations){
                if(mRgdId==association.getMasterRgdId()){
                    geneRgdIdSet.add(association.getDetailRgdId());
                }

            }
           for(int rgdId:geneRgdIdSet){
               for(Gene g: assocGenes){
                   if(rgdId==g.getRgdId()){
                       genes.add(g);
                   }
               }
           }

            assocGeneMap.put(mRgdId, genes);
        }
        return assocGeneMap;
    }

    /**
     * Maps list of attributes of an interaction to interactionkey of given list of interactions
     * @param interactions
     * @return Map of InteractionKey and Attributes List
     * @throws Exception
     */
    public Map<Integer, List<InteractionAttribute>> getAttributeMap(List<Interaction> interactions) throws  Exception{
        Map<Integer, List<InteractionAttribute>> attributeMap= new HashMap<>();
        List<InteractionAttribute> attributeList= new ArrayList<>();
        List<Integer> keyList= new ArrayList<>();
        for(Interaction i: interactions){
            int key= i.getInteractionKey();
            keyList.add(key);
        }
        Collection[] collection= this.split(keyList, 1000);
        for(int i=0; i<collection.length;i++){
            List<Integer> keys= (List<Integer>) collection[i];
            attributeList.addAll(adao.getAttributes(keys));
        }

        for(int key:keyList){
            List<InteractionAttribute> list= new ArrayList<>();
            for(InteractionAttribute a: attributeList){
                int intKey= a.getInteractionKey();
                if(key==intKey){
                    list.add(a);
                }

            }
            attributeMap.put(key, list);
        }

        return attributeMap;
    }

    /**
     * To get Interactions Count of given list of rgdIds
     * @param symbolRgdIds
     * @return
     * @throws Exception
     */

    public int getInteractionsCount(List<Integer> symbolRgdIds) throws Exception{
        int interactionsCount=0;
        InteractionCountsDAO countsDAO= new InteractionCountsDAO();
        List<InteractionCount> counts= new ArrayList<>();
        if(symbolRgdIds.size()>0){
            Collection[] collections= this.split(symbolRgdIds,1000);
            for(int i=0;i<collections.length;i++) {
                List<Integer> sublist = (List<Integer>) collections[i];
                counts.addAll(countsDAO.getInteractionCountsOfRgdIds(sublist));
            }
            //  System.out.println("RGDID || Count");
            for(InteractionCount c: counts){
                //   int rgdId= c.getRgdId();
                int count= c.getCount();
                //   System.out.println(rgdId  + " || " + count);
                interactionsCount= interactionsCount+ count;
            }}
        return interactionsCount;
    }

    public Collection[] split(List<Integer> rgdids, int size) throws Exception{
        int numOfBatches=(rgdids.size()/size)+1;
        Collection[] batches= new Collection[numOfBatches];
        for(int index=0; index<numOfBatches; index++){
            int count=index+1;
            int fromIndex=Math.max(((count-1)*size),0);
            int toIndex=Math.min((count*size), rgdids.size());
            batches[index]= rgdids.subList(fromIndex, toIndex);
        }
        return batches;
    }

    public Map<Integer, Set<Gene>> getAssocGeneMap() {
        return assocGeneMap;
    }

    public void setAssocGeneMap(Map<Integer, Set<Gene>> assocGeneMap) {
        this.assocGeneMap = assocGeneMap;
    }

    public Set<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(Set<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public Set getLog() {
        return log;
    }

    public void setLog(Set log) {
        this.log = log;
    }
}
