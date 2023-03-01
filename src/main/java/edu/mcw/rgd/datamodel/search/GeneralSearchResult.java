package edu.mcw.rgd.datamodel.search;

import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.SpeciesType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 13, 2008
 * Time: 1:42:25 PM
 */
public class GeneralSearchResult {
              
    private int total = 0;

    // map of object-key to search-result-count
    private Map<Integer,Integer> ratCounts = new HashMap<>();
    private Map<Integer,Integer> mouseCounts = new HashMap<>();
    private Map<Integer,Integer> humanCounts = new HashMap<>();
    private Map<Integer,Integer> chinchillaCounts = new HashMap<>();
    private Map<Integer,Integer> bonoboCounts = new HashMap<>();
    private Map<Integer,Integer> dogCounts = new HashMap<>();
    private Map<Integer,Integer> squirrelCounts = new HashMap<>();
    private int referenceCount = 0;

    public int getTotal() {
        return total;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void incrementHitCount(int objectKey, int speciesTypeKey) {

        // references are species-agnostic: handle them first
        if( objectKey== RgdId.OBJECT_KEY_REFERENCES ) {
            referenceCount++;
            total++;
            return;
        }

        // find map for species
        Map<Integer,Integer> map = speciesTypeKey == SpeciesType.RAT ? ratCounts
                : speciesTypeKey == SpeciesType.MOUSE ? mouseCounts
                : speciesTypeKey == SpeciesType.HUMAN ? humanCounts
                : speciesTypeKey == SpeciesType.CHINCHILLA ? chinchillaCounts
                : speciesTypeKey == SpeciesType.BONOBO ? bonoboCounts
                : speciesTypeKey == SpeciesType.DOG ? dogCounts
                : speciesTypeKey == SpeciesType.SQUIRREL ? squirrelCounts
                : null;
        if( map != null ) {
            // increment the count
            Integer oldCount = map.get(objectKey);
            if( oldCount==null )
                oldCount = 0;
            map.put(objectKey, 1+oldCount);
            total++;
        }
    }

    public int getHitCount(int objectKey, int speciesTypeKey) {

        // find map for species
        Map<Integer,Integer> map = speciesTypeKey == SpeciesType.RAT ? ratCounts
                : speciesTypeKey == SpeciesType.MOUSE ? mouseCounts
                : speciesTypeKey == SpeciesType.HUMAN ? humanCounts
                : speciesTypeKey == SpeciesType.CHINCHILLA ? chinchillaCounts
                : speciesTypeKey == SpeciesType.BONOBO ? bonoboCounts
                : speciesTypeKey == SpeciesType.DOG ? dogCounts
                : speciesTypeKey == SpeciesType.SQUIRREL ? squirrelCounts
                : null;
        if( map != null ) {
            Integer count = map.get(objectKey);
            return count==null ? 0 : count;
        }
        return 0;
    }

    public int[] getHitCounts(int objectKey) {

        int[] hitCounts = new int[8];
        if( objectKey==RgdId.OBJECT_KEY_REFERENCES ) {
            hitCounts[0] = referenceCount;
        }
        hitCounts[1] = getHitCount(objectKey, 1);
        hitCounts[2] = getHitCount(objectKey, 2);
        hitCounts[3] = getHitCount(objectKey, 3);
        hitCounts[4] = getHitCount(objectKey, 4);
        hitCounts[5] = getHitCount(objectKey, 5);
        hitCounts[6] = getHitCount(objectKey, 6);
        hitCounts[7] = getHitCount(objectKey, 7);
        return hitCounts;
    }

    // map of ontology terms: acc-id => ont-id; f.e. 'GO:0051099' => 'BP'
    public Map<String,String> ontologyTerms = new HashMap<>();
    // map of ontology terms hit counts: ont-id => hit-count; f.e. 'BP' => 123
    public Map<String,Integer> ontTermHits = new HashMap<>();

}
