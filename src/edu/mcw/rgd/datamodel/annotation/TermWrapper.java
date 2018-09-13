package edu.mcw.rgd.datamodel.annotation;

import edu.mcw.rgd.datamodel.ontologyx.Term;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 3/30/12
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TermWrapper {
    private Term term;
    public ArrayList refs = new ArrayList();


    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public void addReference(GeneWrapper gw) {
        if (!this.refs.contains(gw)) {
            this.refs.add(gw);
        }

    }

}
