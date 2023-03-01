package edu.mcw.rgd.datamodel.ontologyx;

import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mtutaj
 * @since May 4, 2011
 * provides additional information about the term
 */
public class TermWithStats extends Term {

    private Relation rel; // optional -- set in some contexts only
    private Map<String, Integer> statMap;
    private String filter;

    public TermWithStats(String filter) {
        this.filter = filter;
    }
    
    // copy constructor
    public TermWithStats(Term term) {
        this.setAccId(term.getAccId());
        this.setOntologyId(term.getOntologyId());
        this.setTerm(term.getTerm());
        this.setCreatedBy(term.getCreatedBy());
        this.setCreationDate(term.getCreationDate());
        this.setDefinition(term.getDefinition());
        this.setObsolete(term.getObsolete());
        this.setComment(term.getComment());
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    static public String getStatKey(TermStat ts) {
        return getStatKey(ts.getStatName(), ts.getSpeciesTypeKey(), ts.getObjectKey(), ts.getWithChildren(), ts.getFilter());
    }

    static public String getStatKey(String statName, int speciesTypeKey, int objectKey, int withChildren, String filter) {

        if( Utils.isStringEmpty(filter) ) {
            return statName+"|"+speciesTypeKey+"|"+objectKey+"|"+withChildren;
        }else {
            return statName+"|"+speciesTypeKey+"|"+objectKey+"|"+withChildren + "|" + filter;
        }
    }

    public int getStat(String statKey) {

        if( statMap==null ) {
            return 0;
        }
        Integer val = statMap.get(statKey);
        return val==null ? 0 : val;
    }

    public int getStat(String statName, int speciesTypeKey, int objectKey, int withChildren) {
        return getStat(statName, speciesTypeKey, objectKey, withChildren, null);
    }

    public int getStat(String statName, int speciesTypeKey, int objectKey, int withChildren, String filter) {

        return getStat(getStatKey(statName, speciesTypeKey, objectKey, withChildren, filter));
    }

    public TermStat makeTermStat(String termAcc, String statKey, int statValue, String filter) {
        TermStat ts = new TermStat();
        ts.setTermAcc(termAcc);
        String[] key = statKey.split("[\\|]");
        ts.setStatName(key[0]);
        ts.setSpeciesTypeKey(Integer.parseInt(key[1]));
        ts.setObjectKey(Integer.parseInt(key[2]));
        ts.setWithChildren(Integer.parseInt(key[3]));
        ts.setStatValue(statValue);
        ts.setFilter(filter);
        return ts;

    }

    public TermStat makeTermStat(String termAcc, String statKey, int statValue) {
        return makeTermStat(termAcc, statKey, statValue, null);
    }


    public Relation getRel() {
        return rel;
    }

    public void setRel(Relation rel) {
        this.rel = rel;
    }

    public void setStats(List<TermStat> stats) {

        if( stats==null ) {
            statMap = null;
        } else {
            if( statMap==null ) {
                statMap = new HashMap<>();
            } else {
                statMap.clear();
            }

            Map<String, Integer> speciesStats = null;
            Map<String, Integer> objectStats = null;
            int countForTerm = 0;
            int countForTermWithChildren = 0;

            for( TermStat ts: stats ) {
                statMap.put(getStatKey(ts), ts.getStatValue());
                if( ts.getSpeciesTypeKey()==0 || ts.getObjectKey()==0 || !ts.getStatName().equals("annotated_object_count")) {
                    continue; // skip already aggregated data
                }

                // compute species buckets
                // 1. given species, any object
                String key = TermWithStats.getStatKey("annotated_object_count", ts.getSpeciesTypeKey(), 0, ts.getWithChildren(), ts.getFilter());
                if( speciesStats==null ) {
                    speciesStats = new HashMap<>();
                }
                Integer count = speciesStats.get(key);
                if( count==null ) { count = 0; }
                speciesStats.put(key, count+ts.getStatValue());

                // 2. given object, any species
                key = TermWithStats.getStatKey("annotated_object_count", 0, ts.getObjectKey(), ts.getWithChildren(), ts.getFilter());
                if( objectStats==null ) {
                    objectStats = new HashMap<>();
                }
                count = objectStats.get(key);
                if( count==null ) { count = 0; }
                objectStats.put(key, count+ts.getStatValue());

                // 3. any object, any species
                if( ts.getWithChildren()!=0 ) {
                    countForTermWithChildren += ts.getStatValue();
                } else {
                    countForTerm += ts.getStatValue();
                }
            }

            // save stat aggregates
            if( speciesStats!=null ) {
                for( Map.Entry<String, Integer> entry: speciesStats.entrySet() ) {
                    statMap.put(entry.getKey(), entry.getValue());
                }
            }
            if( objectStats!=null ) {
                for( Map.Entry<String, Integer> entry: objectStats.entrySet() ) {
                    statMap.put(entry.getKey(), entry.getValue());
                }
            }
            if( countForTerm!=0 ) {
                statMap.put(TermWithStats.getStatKey("annotated_object_count", 0, 0, 0, getFilter()), countForTerm);
            }
            if( countForTermWithChildren!=0 ) {
                statMap.put(TermWithStats.getStatKey("annotated_object_count", 0, 0, 1, getFilter()), countForTermWithChildren);
            }
        }
    }

    public void addStat(String statName, int speciesTypeKey, int objectKey, int withChildren, int statValue, String filter) {
        if( statValue==0 )
            return;

        if( statMap==null )
            statMap = new HashMap<String, Integer>();

        statMap.put(getStatKey(statName, speciesTypeKey, objectKey, withChildren, filter), statValue);
    }

    public void removeStat(String statName, int speciesTypeKey, int objectKey, int withChildren, String filter) {
        if( statMap==null )
            return;

        statMap.remove(getStatKey(statName, speciesTypeKey, objectKey, withChildren, filter));
    }

    public Map<String, Integer> getStats() {
        return statMap;
    }

    public int getChildTermCount() {
        return getStat("child_term_count", 0, 0, 0);
    }

    public int getParentTermCount() {
        return getStat("parent_term_count", 0, 0, 0);
    }

    /**
     * get annotated gene/qtl/strain count for term and children, for given species
     * Note: Deprecated because method name does not indicate that other counts for some object types like variants
     * are excluded. Use other overloaded method instead
     * @param speciesTypeKey species type key
     * @return
     */
    @Deprecated
    public int getAnnotObjectCountForSpecies(int speciesTypeKey) {
        int cnt = getStat("annotated_object_count", speciesTypeKey, RgdId.OBJECT_KEY_GENES, 1);
        cnt += getStat("annotated_object_count", speciesTypeKey, RgdId.OBJECT_KEY_QTLS, 1);
        cnt += getStat("annotated_object_count", speciesTypeKey, RgdId.OBJECT_KEY_STRAINS, 1);
        return cnt;
    }

    public int getAnnotObjectCountForSpecies(int speciesTypeKey, boolean withVariants) {
        int cnt = getAnnotObjectCountForSpecies(speciesTypeKey);
        if( withVariants ) {
            cnt += getStat("annotated_object_count", speciesTypeKey, RgdId.OBJECT_KEY_VARIANTS, 1);
        }
        return cnt;
    }

    /**
     * get annotated object count for all species and all types of objects, for the term and child terms
     * @return
     */
    public int getAnnotObjectCountForTermAndChildren() {
        return getStat("annotated_object_count", 0, 0, 1);
    }

    public int getAnnotObjectCountForTerm() {
        return getStat("annotated_object_count", 0, 0, 0);
    }

    public int getAnnotObjectCountForTermAndChildren(int speciesTypeKey) {
        return getStat("annotated_object_count", speciesTypeKey, 0, 1);
    }

    public int getAnnotObjectCountForTerm(int speciesTypeKey) {
        return getStat("annotated_object_count", speciesTypeKey, 0, 0);
    }

    public int getRatGeneCountForTerm(String filter) {
        return getStat("annotated_object_count", SpeciesType.RAT, RgdId.OBJECT_KEY_GENES, 0, filter);
    }

    public int getRatGeneCountForTermAndChildren(String filter) {
        return getStat("annotated_object_count", SpeciesType.RAT, RgdId.OBJECT_KEY_GENES, 1, filter);
    }

    public int getRatGeneCountForTerm() {
        return getStat("annotated_object_count", SpeciesType.RAT, RgdId.OBJECT_KEY_GENES, 0);
    }

    public int getRatGeneCountForTermAndChildren() {
        return getStat("annotated_object_count", SpeciesType.RAT, RgdId.OBJECT_KEY_GENES, 1);
    }

    /**
     * get count of pathway diagrams for this term (and for its child terms if applicable)
     * @param withChildren if true include child terms in the count
     * @return count of pathway diagrams for this term (and for its child terms if 'withChildren' is set to 1)
     */
    public int getDiagramCount(int withChildren) {
        return getStat("diagram_count", 0, 0, withChildren);
    }
}
