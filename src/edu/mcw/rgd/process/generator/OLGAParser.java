package edu.mcw.rgd.process.generator;

import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.QTL;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.Strain;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jdepons on 3/10/2017.
 */
public class OLGAParser {


    public OLGAResult parse(int oKey, int mapKey, String oql) throws Exception {

        OLGAResult or = new OLGAResult();

        ArrayList<HashMap<String, String>> excludeMap = new ArrayList<HashMap<String, String>>();
        List newList = new ArrayList();
        ArrayList<String> urlParts = new ArrayList<String>();

        HashMap<String, Object> allObjects = new HashMap<String, Object>();
        if (!oql.equals("")) {
            String[] tmpArray = oql.split("\\|");

            for (int i = 0; i < tmpArray.length; i++) {
                if (oKey != 1 && (tmpArray[i].substring(1).startsWith("lst")) && oKey==5) { //TEST Lines of CODE:oKey==5 added to if
                    urlParts.add(tmpArray[i]);
                }else {
                    urlParts.add(tmpArray[i]);
                }
            }
        }

        if (urlParts.size() > 0) {

            for (int i = 0; i < urlParts.size(); i++) {

                String accIdBlock = urlParts.get(i);
                or.operators.add(accIdBlock.substring(0, 1));

                //get the minus genes
                String[] minGenesArray = accIdBlock.split("\\*");

                HashMap minGenes = new HashMap();
                for (int j = 1; j < minGenesArray.length; j++) {
                    minGenes.put(minGenesArray[j], null);
                }

                String accId = minGenesArray[0].substring(1);
                or.accIds.add(accId);
                excludeMap.add(minGenes);
            }

            List<String> allGenes = new ArrayList<String>();

            for (int i = 0; i < or.accIds.size(); i++) {

                String accId = (String) or.accIds.get(i);
                //String operation=accId.substring(0,1);

                GeneratorCommandParser gcp = new GeneratorCommandParser(mapKey, oKey);

                //      lalala
              //  allGenes.addAll(gcp.parse(accId));

                //List log = gcp.getObjectMapperLog();
                or.messages.putAll(gcp.getLog());

                List objects = gcp.parseObjects(accId);

                Iterator it = objects.iterator();

                if (oKey == 1) {
                    while (it.hasNext()) {
                        Gene g = (Gene) it.next();
                        allObjects.put(g.getSymbol(), g);
                        allGenes.add(g.getSymbol());
                    }
                } else if (oKey == 6) {
                    //qtl
                    while (it.hasNext()) {
                        QTL q = (QTL) it.next();
                        allObjects.put(q.getSymbol(), q);
                        allGenes.add(q.getSymbol());
                    }
                } else if (oKey == 5) {
                    //strain
                    while (it.hasNext()) {
                        Strain s = (Strain) it.next();
                        allObjects.put(s.getSymbol(), s);
                        allGenes.add(s.getSymbol());
                    }

                }

                or.messages.putAll(gcp.getLog());
                ArrayList<String> genes = new ArrayList<String>();
                HashMap exclusions = (HashMap) excludeMap.get(i);

                for (String gene : allGenes) {
                    if (!exclusions.containsKey(gene)) {
                        genes.add(gene);
                    }
                }

                or.objectSymbols.add(genes);
                allGenes = new ArrayList<String>();
                or.omLog.addAll(gcp.getObjectMapperLog());
            }

            newList = or.objectSymbols.get(0);
            for (int i = 1; i < or.accIds.size(); i++) {

                String operator = or.operators.get(i);

                if (operator.equals("~")) {
                    newList = ListUtils.union(newList, or.objectSymbols.get(i));
                }

                if (operator.equals("^")) {
                    newList = ListUtils.subtract(newList, or.objectSymbols.get(i));
                }

                if (operator.equals("!")) {
                    newList = ListUtils.intersection(newList, or.objectSymbols.get(i));
                }
            }
        }else{

        }


        Iterator it = newList.iterator();
        HashMap seen = new HashMap();

        while (it.hasNext()) {
            String gene = (String) it.next();

            if (!seen.containsKey(gene)) {
                or.resultSet.put(gene, allObjects.get(gene));
                seen.put(gene, null);
            }
        }

        return or;
    }
public static void main(String[] args) throws Exception {

   String oq1="~lst:F344-<i>Prkdc<sup>em1Kyo</sup></i>[F344-<i>Prkdc<sup>em2Kyo</sup>Il2rg<sup>em6Kyo</sup></i>[TM-<i>Prkdc<sup>em4Kyo</sup>Il2rg<sup>em5Kyo</sup></i>";
   OLGAParser parser=new OLGAParser();
    OLGAResult result= parser.parse(5,360, oq1);
    System.out.println("Object Symbol: " + result.getObjectSymbols());
}
}
