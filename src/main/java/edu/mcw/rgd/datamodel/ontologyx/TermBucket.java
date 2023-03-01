package edu.mcw.rgd.datamodel.ontologyx;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 2/10/12
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class TermBucket {

    private int count;
    private String term;
    private String accId;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}
