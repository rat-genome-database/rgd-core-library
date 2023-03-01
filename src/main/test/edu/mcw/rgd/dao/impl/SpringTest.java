package edu.mcw.rgd.test;

import edu.mcw.rgd.datamodel.SpeciesType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.mcw.rgd.dao.impl.NomenclatureDAO;

/**
 * @author jdepons
 * @since Dec 31, 2007
 */
public class SpringTest extends TestCase {

    public SpringTest(String testName) {
        super(testName);

        testSpeciesTypeKey2();
        testSpeciesTypeKey();
    }

    public void testSpeciesTypeKey() {

        List<Integer> speciesTypeKeys = new ArrayList<>();
        for( int pass=0; pass<100; pass++ ) {
            for( int sp=1; sp<=12; sp++ ) {
                speciesTypeKeys.add(sp);
            }
        }
        Collections.shuffle(speciesTypeKeys);

        System.out.println("starting parallel test");

        speciesTypeKeys.parallelStream().forEach( sp -> {
            try {
                boolean b1 = SpeciesType.isSearchable(sp);
                boolean b2 = SpeciesType.isSearchable2(sp);
                String problem = b1!=b2 ? "### " : "";
                System.out.println(problem+sp+"  isSearchable="+ b1+"  "+b2);
            } catch (Exception e) {

            }
        });

        System.out.println("end parallel test");
    }

    public void testSpeciesTypeKey2() {

        String[] species = {"rat", "Mouse", "human", "yeast", "Dog", "Sus scrofa"};
        List<String> speciesList = new ArrayList<>();
        for( int pass=0; pass<100; pass++ ) {
            for( int u=0; u<species.length; u++ ) {
                speciesList.add(species[u]);
            }
        }
        Collections.shuffle(speciesList);

        System.out.println("starting parallel test2");

        speciesList.parallelStream().forEach( sp -> {
            try {
                int speciesTypeKey = SpeciesType.parse(sp);
                System.out.println(speciesTypeKey);
            } catch (Exception e) {

            }
        });

        System.out.println("end parallel test 2");
    }

    public void testGetNomenclatureEvents() throws Exception{
       NomenclatureDAO dao = new NomenclatureDAO();
        List list = dao.getNomenclatureEvents(3172);        
        System.out.println(list.size());
    }
}
