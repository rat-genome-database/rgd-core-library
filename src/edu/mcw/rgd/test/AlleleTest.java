package edu.mcw.rgd.dao.impl;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import edu.mcw.rgd.dao.impl.NomenclatureDAO;
import edu.mcw.rgd.dao.spring.GeneQuery;
import edu.mcw.rgd.datamodel.Gene;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 31, 2007
 * Time: 10:05:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlleleTest extends TestCase {

    public AlleleTest(String testName) {
        super(testName);
    }

    public void estAllele() throws Exception{
      System.out.println("Hello2");
    }

    public void testAllele2() throws Exception{

      GeneDAO geneDAO=new GeneDAO();
       List<Gene> alleleList = geneDAO.getVariantFromGene(3689);    //use rgdId
       Iterator it = alleleList.iterator();

        while (it.hasNext()) {
            Gene variant = (Gene) it.next();
            String variantName = variant.getName();
            String variantSymbol = variant.getSymbol();
            String newGeneSymbol = "newSymbol"; //symbol in rgdweb
            String newGeneName = "newName"; //name in rgdweb
            if(variant.getType().equals("allele")){
                String patternStr = "<i>";
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(variantSymbol);
                boolean matchFound = matcher.find();
                if(matchFound){
                    String oldAlleleSymbola[]=variantSymbol.split("<i>");
                    variantSymbol= newGeneSymbol+"<i>"+oldAlleleSymbola[1];
                }
                else{
                    String oldAlleleSymbola[]=variantSymbol.split("<sup>");
                    variantSymbol= newGeneSymbol+"<sup>"+oldAlleleSymbola[1];                    
                }
                String oldAlleleNamea[]=variantName.split("mutation");
                variantName= newGeneName+"; mutation"+oldAlleleNamea[1];
            }
            else if(variant.getType().equals("splice")){
                String oldSpliceSymbola[]=variantSymbol.split("_v");
                variantSymbol= newGeneSymbol+"_v"+oldSpliceSymbola[1];
                String oldSpliceNamea[]=variantName.split("variant");
                variantName= newGeneName+", variant"+oldSpliceNamea[1];
            }
            variant.setSymbol(variantSymbol);
            variant.setName(variantName);
            geneDAO.updateGene(variant);
        } 
    }
}