package edu.mcw.rgd.datamodel.ontologyx;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 6/27/11, Time: 10:45 AM <br>
 * <p>
 * an edge in a DAG (directed acyclic graph) between two ontology terms;
 * corresponds to a row in ONT_DAG table
 * <p>
 * should this object rather be named 'TermRelationship'?
 */
public class TermDagEdge implements Dumpable {

    private String parentTermAcc;
    private String childTermAcc;
    private Relation rel; // child-to-parent relationship

    // optional, can be null
    private String parentTermName;
    private String childTermName;
    private Date createdDate;
    private Date lastModifiedDate;

    public String getParentTermAcc() {
        return parentTermAcc;
    }

    public void setParentTermAcc(String parentTermAcc) {
        this.parentTermAcc = parentTermAcc;
    }

    public String getChildTermAcc() {
        return childTermAcc;
    }

    public void setChildTermAcc(String childTermAcc) {
        this.childTermAcc = childTermAcc;
    }

    public Relation getRel() {
        return rel;
    }

    public String getRelId() {
        return Relation.getRelIdFromRel(rel);
    }

    public void setRel(Relation rel) {
        this.rel = rel;
    }

    public String getParentTermName() {
        return parentTermName;
    }

    public void setParentTermName(String parentTermName) {
        this.parentTermName = parentTermName;
    }

    public String getChildTermName() {
        return childTermName;
    }

    public void setChildTermName(String childTermName) {
        this.childTermName = childTermName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("PARENT_TERM_ACC", parentTermAcc)
            .put("CHILD_TERM_ACC", childTermAcc)
            .put("REL", rel.toString())
            .put("PARENT_TERM_NAME", parentTermName)
            .put("CHILD_TERM_NAME", childTermName)
            .put("CREATED_DATE", createdDate)
            .put("MODIFIED_DATE", lastModifiedDate)
            .dump();
    }
}
