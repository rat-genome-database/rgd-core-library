package edu.mcw.rgd.datamodel.annotation;

import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.ontologyx.Term;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 3/30/12
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneWrapper {

    private Gene gene;
    public ArrayList refs = new ArrayList();
    private HashMap roots = new HashMap();


    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }


    public void addTermReference(TermWrapper t) {
        if (!refs.contains(t)) {
            this.refs.add(t);
        }
    }

    public void addTermReference(TermWrapper t, Term root) {
        this.addTermReference(t);

            ArrayList al = null;
            if (this.roots.containsKey(t)) {
                al = (ArrayList) this.roots.get(t);
            }else {
                al = new ArrayList();

            }

            if (root != null && !al.contains(root)) {
                al.add(root);
            }

            this.roots.put(t, al);
    }

    public ArrayList getRoots(TermWrapper t) {
        return (ArrayList) roots.get(t);
    }
}
