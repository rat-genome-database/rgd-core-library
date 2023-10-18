package edu.mcw.rgd.process.generator;

import edu.mcw.rgd.dao.impl.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.ontologyx.TermWithStats;
import edu.mcw.rgd.process.mapping.MapManager;
import edu.mcw.rgd.process.mapping.ObjectMapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author jdepons
 * @since 2/6/15
 */
public class GeneratorCommandParser {

    int mapKey;
    int oKey;
    int speciesType;
    HashMap messages = new HashMap();
    List objectMapperLog = new ArrayList();


    public GeneratorCommandParser(int mapKey, int oKey) {
       this.mapKey=mapKey;
       this.oKey=oKey;
       this.speciesType = SpeciesType.getSpeciesTypeKeyForMap(mapKey);
    }


    public HashMap getLog() {
        return messages;
    }

    public List getObjectMapperLog() {
        return objectMapperLog;
    }

    public List parseObjects(String command) throws Exception {

        List allGenes = new ArrayList();

        if (command.toLowerCase().startsWith("chr")) {
            String chr = "";
            String start = "";
            String stop="";
            if (command.indexOf(":") != -1) {
                chr = command.substring(3, command.indexOf(":"));
                start = command.substring(command.indexOf(":") + 1, command.indexOf(".."));
                stop = command.substring(command.indexOf("..") + 2);

                if (oKey==1) {
                    GeneDAO gdao = new GeneDAO();
                    List<Gene> genes = gdao.getActiveGenesSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop),mapKey);

                    for (Gene gene: genes) {
                        allGenes.add(gene);
                    }
                }else if (oKey==6) {
                    QTLDAO qdao = new QTLDAO();
                    List<QTL> qtls = qdao.getActiveQTLSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop), mapKey);

                    for (QTL qtl: qtls) {
                        allGenes.add(qtl);
                    }

                }else if (oKey==5) {
                    StrainDAO sdao = new StrainDAO();
                    List<Strain> strains = sdao.getActiveStrainsSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop),mapKey);

                    for (Strain strain: strains) {
                        allGenes.add(strain);
                    }

                }

            }
        }else if (command.toLowerCase().startsWith("lst")) {

            String lst = command.substring(4);
            String[] genes = lst.split("\\[");

            //List symbols=null;

            List geneList = new ArrayList();
            for (int k=0; k<genes.length; k++) {
                geneList.add(genes[k]);
            }

            try {

                ObjectMapper om = new ObjectMapper();
                om.mapSymbols(geneList, speciesType);

                objectMapperLog = om.getLog();

                List<Gene> mappedGenes = om.getMapped();
                Iterator it = mappedGenes.iterator();

                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof Gene) {
                        Gene g = (Gene) o;
                        allGenes.add(g);
                    }
                }

            }catch (Exception e) {
                messages.put(command,e.getMessage());

            }

        }else if (command.toLowerCase().startsWith("qtl")) {
            try {

                String symbol = command.substring(4);

                QTLDAO qdao = new QTLDAO();
                QTL qtl = qdao.getQTLBySymbol(symbol,speciesType);

                MapDAO mdao = new MapDAO();
                List<MapData> mdList = mdao.getMapData(qtl.getRgdId(),mapKey);
                MapData md = mdList.get(0);

                if (oKey==1) {
                    GeneDAO gdao = new GeneDAO();
                    List<Gene> genes = gdao.getActiveGenesSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (Gene g: genes) {
                        allGenes.add(g);
                    }
                }else if (oKey==6) {
                    List<QTL> qtls = qdao.getActiveQTLSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (QTL q: qtls) {
                        allGenes.add(q);
                    }

                }else if (oKey==5) {
                    StrainDAO sdao = new StrainDAO();
                    List<Strain> strains = sdao.getActiveStrainsSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (Strain strain: strains) {
                        allGenes.add(strain);
                    }

                }

            }catch (Exception e) {
                messages.put(command,"QTL Not Found<br>on assembly<br>" + MapManager.getInstance().getMap(mapKey).getName());

            }

        } else {

            ArrayList terms = new ArrayList();

            OntologyXDAO odao = new OntologyXDAO();
            TermWithStats tws = odao.getTermWithStatsCached(command,null);

            AnnotationDAO adao = new AnnotationDAO();
            int count = tws.getStat("annotated_object_count", speciesType, oKey, 1);

            terms.add(command);

            //check the size first
            if (count < 25000) {
                //allGenes = adao.getAnnotatedSymbols(terms, speciesType, oKey);
                if (oKey==1) {
                    allGenes = adao.getAnnotatedGenes(terms, speciesType);

                }else if (oKey==6) {
                    allGenes = adao.getAnnotatedQTLS(terms, speciesType);
                }else if (oKey==5) {
                    allGenes = adao.getAnnotatedStrains(terms, speciesType);
                }

            }else {
                messages.put(command,count + " annotations found.<br><br>Please select a<br>more specific term<br>");
            }
        }

        return allGenes;

    }


    public List<String> parse(String command) throws Exception {

        List<String> allGenes = new ArrayList<String>();

        if (command.toLowerCase().startsWith("chr")) {
            String chr = "";
            String start = "";
            String stop="";
            if (command.indexOf(":") != -1) {
                chr = command.substring(3, command.indexOf(":"));
                start = command.substring(command.indexOf(":") + 1, command.indexOf(".."));
                stop = command.substring(command.indexOf("..") + 2);

                if (oKey==1) {
                    GeneDAO gdao = new GeneDAO();
                    List<Gene> genes = gdao.getActiveGenesSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop),mapKey);

                    for (Gene gene: genes) {
                        allGenes.add(gene.getSymbol());
                    }
                }else if (oKey==6) {
                    QTLDAO qdao = new QTLDAO();
                    List<QTL> qtls = qdao.getActiveQTLSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop), mapKey);

                    for (QTL qtl: qtls) {
                        allGenes.add(qtl.getSymbol());
                    }

                }else if (oKey==5) {
                    StrainDAO sdao = new StrainDAO();
                    List<Strain> strains = sdao.getActiveStrainsSortedBySymbol(chr, Long.parseLong(start), Long.parseLong(stop),mapKey);

                    for (Strain strain: strains) {
                        allGenes.add(strain.getSymbol());
                    }

                }

            }
        }else if (command.toLowerCase().startsWith("lst")) {
            String lst = command.substring(4);
            String[] genes = lst.split("\\[");

            List objectList = new ArrayList();
            for (int k=0; k<genes.length; k++) {
               objectList.add(genes[k]);
            }
            if(oKey==1){
            try {
                ObjectMapper om = new ObjectMapper();
                om.mapSymbols(objectList, speciesType);

                objectMapperLog = om.getLog();

                List<Gene> mappedGenes = om.getMapped();
                Iterator it = mappedGenes.iterator();

                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof Gene) {
                        Gene g = (Gene) o;
                        allGenes.add(g.getSymbol());
                    }
                }

            }catch (Exception e) {
                messages.put(command,e.getMessage());

            }}
            if(oKey==5){
                allGenes.addAll(objectList);
            }
      /*    List symbols=null;
            String lst = command.substring(4);
            String[] genes = lst.split("\\[");

            List symbols=null;

            List geneList = new ArrayList();
            for (int k=0; k<genes.length; k++) {
                geneList.add(genes[k]);
            }

            try {
                ObjectMapper om = new ObjectMapper();
                om.mapSymbols(geneList, speciesType);

                objectMapperLog = om.getLog();

                List<Gene> mappedGenes = om.getMapped();
                Iterator it = mappedGenes.iterator();

                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof Gene) {
                        Gene g = (Gene) o;
                        allGenes.add(g.getSymbol());
                    }
                }

            }catch (Exception e) {
                messages.put(command,e.getMessage());

            }*/

        }else if (command.toLowerCase().startsWith("qtl")) {
            try {

                String symbol = command.substring(4);

                QTLDAO qdao = new QTLDAO();
                QTL qtl = qdao.getQTLBySymbol(symbol,speciesType);

                MapDAO mdao = new MapDAO();
                List<MapData> mdList = mdao.getMapData(qtl.getRgdId(),mapKey);

                MapData md = mdList.get(0);

                if (oKey==1) {
                    GeneDAO gdao = new GeneDAO();
                    List<Gene> genes = gdao.getActiveGenesSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (Gene g: genes) {
                        allGenes.add(g.getSymbol());
                    }
                }else if (oKey==6) {
                    List<QTL> qtls = qdao.getActiveQTLSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (QTL q: qtls) {
                        allGenes.add(q.getSymbol());
                    }

                }else if (oKey==5) {
                    StrainDAO sdao = new StrainDAO();
                    List<Strain> strains = sdao.getActiveStrainsSortedBySymbol(md.getChromosome(), md.getStartPos(), md.getStopPos(),mapKey);

                    for (Strain strain: strains) {
                        allGenes.add(strain.getSymbol());
                    }

                }

            }catch (Exception e) {
                messages.put(command,"QTL Not Found<br>on assembly<br>" + MapManager.getInstance().getMap(mapKey).getName());

            }

        } else {

            ArrayList terms = new ArrayList();

            //OntologyXDAO odao = new OntologyXDAO();

            AnnotationDAO adao = new AnnotationDAO();
            //TermWithStats tws = odao.getTermWithStatsCached(command,null);
            //int count=tws.getAnnotObjectCountForSpecies(speciesType);

            terms.add(command);

            //check the size first
           // if (count < 25000) {
                allGenes = adao.getAnnotatedSymbols(terms, speciesType, oKey);
          //  }else {
          //      messages.put(command,count + " genes annotated<br><br>Please select a<br>more specific term<br>(10,000 max)");
          //  }

        }

        return allGenes;

    }

}
