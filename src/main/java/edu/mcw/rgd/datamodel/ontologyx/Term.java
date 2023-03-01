package edu.mcw.rgd.datamodel.ontologyx;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 17, 2011
 * Time: 9:53:38 AM
 * <p>Bean class for an ontology term
 */
public class Term implements Dumpable {

    String ontologyId;
    String accId;
    String term;
    int isObsolete;
    String createdBy;
    Date creationDate;
    Date modificationDate;
    String definition;
    String comment;
    List<TermXRef> xrefs; // list of dbxrefs for term definition

    /**
     * terms are equal if all their properties are equal, except the modification date
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        Term t = (Term) obj;

        return Utils.stringsAreEqual(this.getAccId(), t.getAccId())
            && Utils.stringsAreEqual(this.getOntologyId(), t.getOntologyId())
            && Utils.stringsAreEqual(this.getTerm(), t.getTerm())
            && Utils.stringsAreEqual(this.getDefinition(), t.getDefinition())
            && Utils.stringsAreEqual(this.getComment(), t.getComment())
            && Utils.stringsAreEqual(this.getCreatedBy(), t.getCreatedBy())
            && Utils.datesAreEqual(this.getCreationDate(), t.getCreationDate())
            && this.getObsolete()==t.getObsolete();
    }

    public String getOntologyId() {
        return ontologyId;
    }

    public void setOntologyId(String ontologyId) {
        this.ontologyId = ontologyId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getObsolete() {
        return isObsolete;
    }

    public boolean isObsolete() {
        return isObsolete>0;
    }

    public void setObsolete(int obsolete) {
        isObsolete = obsolete!=0 ? 1 : 0;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TermXRef> getXRefs() {
        return xrefs;
    }

    public void setXRefs(List<TermXRef> xrefs) {
        this.xrefs = xrefs;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("ONT_ID", ontologyId)
            .put("ACC_ID", accId)
            .put("TERM", term)
            .put("IS_OBSOLETE", isObsolete)
            .put("CREATED_BY", createdBy)
            .put("CREATION_DATE", creationDate)
            .put("MODIFIED_DATE", modificationDate)
            .put("DEFINITION", definition)
            .put("COMMENT", comment)
            .dump();
    }
}
