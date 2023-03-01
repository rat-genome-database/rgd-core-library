package edu.mcw.rgd.datamodel.annotation;

import edu.mcw.rgd.datamodel.ontologyx.Term;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 4/2/12
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Relationship {

    private Term term;
    private Term root;

    public Relationship(Term t, Term base) {
        this.term = t;
        this.root= base;
    }


    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Term getRoot() {
        return root;
    }

    public void setRoot(Term root) {
        this.root = root;
    }
}
