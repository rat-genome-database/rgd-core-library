package edu.mcw.rgd.datamodel.annotation;

import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.process.Stamp;
import org.apache.commons.collections.ListUtils;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 3/30/12
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class OntologyEnrichment {

    public HashMap termMap = new HashMap();
    public HashMap geneMap = new HashMap();

    public void addAssociation(Term term, Gene gene) throws Exception{
        this.addAssociation(term, gene, null);
    }

    public void addAssociation(Term term, Gene gene, Term baseTerm) throws Exception{

        TermWrapper tw = null;
        if (termMap.containsKey(term.getAccId())) {
            tw = (TermWrapper) termMap.get(term.getAccId());
        }else {
            tw = new TermWrapper();
            tw.setTerm(term);
            termMap.put(term.getAccId(), tw);
        }

        GeneWrapper gw = new GeneWrapper();
        if (geneMap.containsKey(gene.getRgdId())) {
            gw = (GeneWrapper) geneMap.get(gene.getRgdId());
        }else {
            gw = new GeneWrapper();
            gw.setGene(gene);
            geneMap.put(gene.getRgdId(), gw);
        }

        tw.addReference(gw);
        gw.addTermReference(tw, baseTerm);

    }

    public Map<String, TermWrapper> getTermWrappers() {
        return this.sortTermsByValue(this.termMap);
    }

    public Map<String, GeneWrapper> getGeneWrappers() {
        return this.sortGenesByValue(this.geneMap);
    }

    private Map<String, TermWrapper> sortTermsByValue(Map<String, TermWrapper> map) {
        List<Map.Entry<String, TermWrapper>> list = new LinkedList<Map.Entry<String, TermWrapper>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, TermWrapper>>() {

            public int compare(Map.Entry<String, TermWrapper> termWrapper1, Map.Entry<String, TermWrapper> termWrapper2) {
            //parameter are of type Object, so we have to downcast it to Employee objects

            TermWrapper t1 = (TermWrapper) termWrapper1.getValue();
            TermWrapper t2 = (TermWrapper) termWrapper2.getValue();

            if (t1.refs.size() > t2.refs.size()) {
                return -1;
            }else if (t1.refs.size() == t2.refs.size()) {
                return 0;
            }else {
                return 1;
            }
            }
        });

        Map<String, TermWrapper> result = new LinkedHashMap<String, TermWrapper>();
        for (Map.Entry<String, TermWrapper> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private Map<String, GeneWrapper> sortGenesByValue(Map<String, GeneWrapper> map) {
        List<Map.Entry<String, GeneWrapper>> list = new LinkedList<Map.Entry<String, GeneWrapper>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, GeneWrapper>>() {

            public int compare(Map.Entry<String, GeneWrapper> geneWrapper1, Map.Entry<String, GeneWrapper> geneWrapper2) {
            //parameter are of type Object, so we have to downcast it to Employee objects

            GeneWrapper t1 = (GeneWrapper) geneWrapper1.getValue();
            GeneWrapper t2 = (GeneWrapper) geneWrapper2.getValue();

            if (t1.refs.size() > t2.refs.size()) {
                return -1;
            }else if (t1.refs.size() == t2.refs.size()) {
                return 0;
            }else {
                return 1;
            }
            }
        });

        Map<String, GeneWrapper> result = new LinkedHashMap<String, GeneWrapper>();
        for (Map.Entry<String, GeneWrapper> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }



    public List<GeneWrapper> getOverlap(String term1, String term2, String term3) {
        TermWrapper tw1 = (TermWrapper) this.termMap.get(term1);
        TermWrapper tw2 = (TermWrapper) this.termMap.get(term2);
        TermWrapper tw3 = (TermWrapper) this.termMap.get(term3);

        List intersection = ListUtils.intersection(tw1.refs, tw2.refs);
        return ListUtils.intersection(intersection, tw3.refs);

    }


    public List<GeneWrapper> getOverlap(List<String> accIds) {

        List intersection = new ArrayList();

        boolean first = true;
        for (String id: accIds) {
            TermWrapper tw = (TermWrapper) this.termMap.get(id);
            if (first) {
                //System.out.println("term Wrapper = " + tw);
                intersection.addAll(tw.refs);
                first=false;
            }else {
                intersection = ListUtils.intersection(intersection, tw.refs);
            }
        }

        return intersection;
    }


    public List<GeneWrapper> getOverlap(TermWrapper tw, TermWrapper tw2, TermWrapper tw3) {
        List intersection = ListUtils.intersection(tw.refs, tw2.refs);
        return ListUtils.intersection(intersection, tw3.refs);
    }


    public List<GeneWrapper> getOverlap(String term1, String term2) {

        TermWrapper tw1 = (TermWrapper) this.termMap.get(term1);
        TermWrapper tw2 = (TermWrapper) this.termMap.get(term2);

        if (tw1 == null || tw2 == null) {
            return new ArrayList<GeneWrapper>();
        }

        return ListUtils.intersection(tw1.refs, tw2.refs);
    }

    public List<GeneWrapper> getOverlap(TermWrapper tw, TermWrapper tw2) {
        return ListUtils.intersection(tw.refs, tw2.refs);
    }

}

