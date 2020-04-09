package edu.mcw.rgd.process.mapping;

import edu.mcw.rgd.dao.impl.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.datamodel.search.IndexRow;
import edu.mcw.rgd.process.Stamp;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 12/7/11
 * Time: 10:12 AM
 */
public class ObjectMapper {

    private List logUnformatted = new ArrayList();
    private List log = new ArrayList();

    //private HashMap log = new HashMap();

    private List mapped = new ArrayList();
    Set seenIds = new HashSet();

    int maxGenes = 4000;


    public void addToMap(Gene g) throws Exception {

        if (this.mapped.size() > maxGenes) {
            throw new Exception("Maximum gene set size is " + maxGenes + " Please reduce the gene set size.");
        }

        if (g != null && !seenIds.contains(g.getRgdId())) {
            this.mapped.add(g);
            this.seenIds.add(g.getRgdId());
        }
    }

    public List getMappedRgdIds() {
        List mappedRGDIds = new ArrayList();
        Iterator mappedIt = this.getMapped().iterator();
        while (mappedIt.hasNext()) {
            Object next = mappedIt.next();

            if (next instanceof Identifiable) {
                Identifiable ided = (Identifiable) next;
                mappedRGDIds.add(ided.getRgdId());
            }
        }
        return mappedRGDIds;
    }

    public List getMapped() {
        return mapped;
    }

    public List getLog() {
        return log;
    }

    public List getLogUnformatted() {
        return logUnformatted;
    }

    public String getMappedAsString() {
        String str ="";
        Iterator it = this.mapped.iterator();

        boolean first=true;
        while (it.hasNext()) {
            Gene g = null;
            try {
                g = (Gene) it.next();
            }catch (ClassCastException cce) {
                continue;
            }

            if (first) {
                str=g.getSymbol();
                first=false;
            }else {
                str+="," + g.getSymbol();
            }
        }
        return str;
    }

    public void mapPosition(String chr, int start, int stop, int mapKey) throws Exception {
        GeneDAO gdao = new GeneDAO();

        List mappedGenes = gdao.getGenesByPosition(chr, start, stop, mapKey);

        Iterator it = mappedGenes.iterator();
        while (it.hasNext()) {
            Gene g = (Gene) it.next();
            this.addToMap(g);
        }
    }

    public void mapSymbols(List<String> symbols, int speciesTypeKey)  throws Exception{
        this.mapSymbols(symbols,speciesTypeKey,null);

    }

    public void mapSymbols(List<String> symbols, int speciesTypeKey, String numericIdType)  throws Exception{

        if (symbols.size() == 0) {
            return;
        }

        GeneDAO gdao = new GeneDAO();
        RGDManagementDAO rdao = new RGDManagementDAO();
        SearchDAO sdao = new SearchDAO();


        if (numericIdType == null) {
            numericIdType="";
        }

        for (String symbol: symbols) {

            //strip off trailing dot values
            Pattern p = Pattern.compile("(.*)(\\.\\d+)");
            Matcher m = p.matcher(symbol);
            if (m.matches()) {
                symbol=(m.group(1));
            }

            //check for numeric values
            try {
                if (numericIdType.equals("rgd")) {
                    RgdId rid = rdao.getRgdId(Integer.parseInt(symbol));

                    if (rid.getSpeciesTypeKey() != speciesTypeKey) {
                        throw new Exception();
                    }

                    Gene g  = gdao.getGene(rid.getRgdId());

                    if (g != null) {
                        this.addToMap(g);
                        log.add("<span style='color:blue;'>Converted RGD ID <b>" + symbol + "</b> to Gene Symbol <b>" + g.getSymbol() + "</b>");
                        logUnformatted.add("Converted RGD ID " + symbol + " to Gene Symbol " + g.getSymbol());
                        continue;
                    }else {
                        throw new Exception();
                    }

                }else if (numericIdType.equals("affy")) {
                    List<Gene> genes = gdao.getGenesForAffyId(symbol, speciesTypeKey);

                    if (genes.size() == 1) {
                        Gene gene = genes.get(0);
                        this.addToMap(gene);
                        log.add("<span style='color:blue;'>Converted Affymetrix ID <b>" + symbol + "</b> to Gene Symbol <b>" + gene.getSymbol() + "</b>");
                        logUnformatted.add("Converted Affymetrix ID " + symbol + " to Gene Symbol " + gene.getSymbol() );
                        continue;
                    }else {
                        throw new Exception();
                    }
                }else if (numericIdType.equals("entrez")) {
                    XdbIdDAO xdao = new XdbIdDAO();
                    List<Gene> genes = xdao.getGenesByXdbId(3,symbol,speciesTypeKey);
                    if (genes.size() == 1) {
                        Gene gene = genes.get(0);
                        this.addToMap(gene);
                        log.add("<span style='color:blue;'>Converted EntrezGene ID <b>" + symbol + "</b> to Gene Symbol <b>" + gene.getSymbol() + "</b>");
                        logUnformatted.add("Converted EntrezGene ID " + symbol + " to Gene Symbol " + gene.getSymbol() );
                        continue;
                    }else {
                        throw new Exception();
                    }
                }else if (numericIdType.equals("kegg")) {
                    XdbIdDAO xdao = new XdbIdDAO();
                    List<Gene> genes = xdao.getGenesByXdbId(23,symbol,speciesTypeKey);

                    String msg = "";
                    for (Gene g: genes) {
                        this.addToMap(g);

                        if (msg.equals("")) {
                            msg += g.getSymbol();
                        }else {
                            msg += ", " + g.getSymbol();
                        }
                    }

                    if (!msg.equals("")) {
                        log.add("<span style='color:blue;'>Annotated to KEGG Pathway " + symbol + ": <b>" + msg + "</b></span>");
                        logUnformatted.add("Annotated to KEGG Pathway " + symbol + ": " + msg );
                    }
                    continue;
                }else if(numericIdType.equals("hgnc")){
                    XdbIdDAO xdao = new XdbIdDAO();
                    List<Gene> genes = xdao.getGenesByXdbId(21,symbol.toUpperCase(),speciesTypeKey);

                    String msg = "";
                    for (Gene g: genes) {
                        this.addToMap(g);

                        if (msg.equals("")) {
                            msg += g.getSymbol();
                        }else {
                            msg += ", " + g.getSymbol();
                        }
                    }


                    continue;
                }

            }catch (Exception ignored) {
                //ignored.printStackTrace();
            }

            //check if an ontology term
            p = Pattern.compile("\\w+\\:\\d\\d\\d\\d\\d\\d\\d");
            m = p.matcher(symbol);
            if (m.matches()) {
                List<Gene> genes = gdao.getActiveAnnotatedGenes(symbol.toUpperCase(),speciesTypeKey);

                Term t = null;
                if (genes.size() > 0) {
                    OntologyXDAO odao = new OntologyXDAO();
                    t = odao.getTerm(symbol.toUpperCase());
                }


                String msg = "";
                for (Gene g: genes) {
                    this.addToMap(g);

                    if (msg.equals("")) {
                        msg += g.getSymbol();
                    }else {
                        msg += ", " + g.getSymbol();
                    }
                }

                if (!msg.equals("")) {
                    log.add("<span style='color:blue;'>Annotated to " + t.getTerm() + " (" + symbol + "): <b>" + msg + "</b></span>");
                }
                continue;
            }

            Map map = MapManager.getInstance().getReferenceAssembly(speciesTypeKey);

            //check if a dbsnp id
            p = Pattern.compile("^rs\\d+?");
            m = p.matcher(symbol);
            if (m.matches()) {

                List<String> gSymbols = gdao.getGeneSymbolMapping(symbol.toLowerCase(),map.getKey(), map.getDbsnpVersion());

                List<Gene> genes = new ArrayList<Gene>();

                String msg = "";
                String color="blue";
                for (String sym: gSymbols) {
                    if (sym.indexOf("|") != -1) {
                        msg="Mapped to intergenic region";
                        color="red";
                    }else if (sym.indexOf("*") != -1) {
                        String[] str = sym.split("\\*");
                        Gene g = gdao.getGenesBySymbol(str[0],speciesTypeKey);
                        if (g==null) {
                            msg="Gene for " + sym + " not found";
                            color="red";
                        }else {
                            genes.add(g);
                        }

                        g = gdao.getGenesBySymbol(str[1],speciesTypeKey);

                        if (g==null) {
                            msg="Gene for " + sym + " not found";
                            color="red";
                        }else {
                            genes.add(g);
                        }
                    }else {
                        Gene g = gdao.getGenesBySymbol(sym,speciesTypeKey);
                        if (g==null) {
                            msg="Gene for " + sym + " not found";
                            color="red";
                        }else {
                            genes.add(g);
                        }
                    }
                }

                for (Gene g: genes) {

                    if (g != null) {
                        this.addToMap(g);
                    }else {
                        continue;
                    }

                    if (msg.equals("")) {
                        msg += g.getSymbol();
                    }else {
                        msg += ", " + g.getSymbol();
                    }
                }

                if (!msg.equals("")) {
                    log.add("<span style='color:" + color + ";'>Converted DBSNP ID " + symbol + ": <b>" + msg + "</b></span>");
                    logUnformatted.add("Converted DBSNP ID " + symbol + ": " + msg );
                }
                continue;
            }


            List<IndexRow> lst = sdao.findGenomicObject(symbol, speciesTypeKey);

            if (lst.size() > 1) {
                for (IndexRow row: lst) {
                    if (row.getDataType().equals("symbol")) {
                        lst = new ArrayList<IndexRow>();
                        lst.add(row);
                        break;
                    }
                }
            }

            for (IndexRow row: lst) {
                String newSymbol="";
                String type="";

                if (seenIds.contains(row.getRgdId())) {
                    continue;
                }

                if (row.getObjectType().equals("GENES")) {
                    Gene g = gdao.getGene(row.getRgdId());
                    newSymbol = g.getSymbol();
                    type = "gene";

                    this.addToMap(g);
                }

                if (!newSymbol.equals("") && !symbol.toLowerCase().equals(newSymbol.toLowerCase())) {
                    log.add("<span style='color:blue;'>Converted <b>" + symbol + "</b> to RGD " + type + " <b>" + newSymbol + "</b> (rule:" + row.getDataType() + ")</span>");
                    logUnformatted.add("Converted " + symbol + " to RGD " + type + " " + newSymbol + " (rule:" + row.getDataType() + ")");
                }
            }

            if (lst.size()==0) {
                log.add("<span style='color:red'>Could not find rgd object for symbol " + symbol + " and species " + SpeciesType.getTaxonomicName(speciesTypeKey) + "</span>");
                logUnformatted.add("Could not find rgd object for symbol " + symbol + " and species " + SpeciesType.getTaxonomicName(speciesTypeKey));
                this.mapped.add(symbol);
            }

        }

    }


    public void mapProteinSymbols(List<String> symbols, int speciesTypeKey, String numericIdType)  throws Exception{

        ProteinDAO pdao = new ProteinDAO();

        List<Protein> proteins = pdao.getProteinListByUniProtIdOrSymbol(symbols,speciesTypeKey);
        List<String> hgncIds=new ArrayList<>();
        List<String> otherIdentifiers=new ArrayList<>();

        HashMap pMap = new HashMap();

        for (Protein p: proteins) {
            pMap.put(p.getUniprotId(), null);
            pMap.put(p.getSymbol(), null);

        }

        this.mapped.addAll(proteins);

        List<String> listMinusProteins = new ArrayList();

        for (String symbol: symbols) {
            if (!pMap.containsKey(symbol.toUpperCase())){
                listMinusProteins.add(symbol);
            }

        }

        for(String identifier:listMinusProteins){
            if(identifier.toLowerCase().contains("hgnc")){
                hgncIds.add(identifier);
            }else{
                identifier=(identifier.toLowerCase().contains("rgd:"))?identifier.toLowerCase().replace("rgd:",""):identifier;
                otherIdentifiers.add(identifier);
            }
        }
        this.mapSymbols(hgncIds,speciesTypeKey,"hgnc");
        this.mapSymbols(otherIdentifiers,speciesTypeKey,numericIdType);


    }



}