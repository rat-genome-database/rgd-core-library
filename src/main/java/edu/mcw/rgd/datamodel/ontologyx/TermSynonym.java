package edu.mcw.rgd.datamodel.ontologyx;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.process.Dumper;
import edu.mcw.rgd.process.Utils;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: Mar 24, 2011
 * Time: 5:08:40 PM
 * <p>
 * represents a term synonym; one term could have multiple synonyms
 */
public class TermSynonym implements Dumpable {

    private int key; // unique key
    private String accId; // term accession id
    private String name; // synonym name
    private String type; // synonym type
    private String dbxrefs;
    private String source;
    private Date createdDate;
    private Date lastModifiedDate;

    public String getFriendlyType() {

        if (type.equals("narrow_synonym")) {
            return "Narrow Synonyms";
        }
        if (type.equals("related_synonym")) {
            return "Related Synonyms";
        }
        if (type.equals("alt_id")) {
            return "Alternate IDs";
        }
        if (type.equals("xref_analog")) {
            return "Xref Analogs";
        }
        if (type.equals("broad_synonym")) {
            return "Broad Synonyms";
        }
        if (type.equals("xref")) {
            return "Xrefs";
        }
        if (type.equals("synonym")) {
            return "Synonyms";
        }
        if (type.equals("xref_mesh")) {
            return "Xref Mesh";
        }
        if (type.equals("consider")) {
            return "Consider";
        }
        if (type.equals("replaced_by")) {
            return "Replaced By";
        }
        if (type.equals("primary_id")) {
            return "Primary IDs";
        }
        if (type.equals("external_ontology")) {
            return "External Ontologys";
        }
        if (type.equals("cyclic_relationship")) {
            return "Cyclic Relationships";
        }
        if (type.equals("xref_casrn")) {
            return "Xref Casrn";
        }
        if (type.equals("exact_synonym")) {
            return "Exact Synonyms";
        }
        if (type.equals("only_in_taxon")) {
            return "Only In Taxon";
        }
        if (type.equals("never_in_taxon")) {
            return "Never In Taxon";
        }

        return type;

    }


    /**
     * two TermSynonyms objects qre equal if they have the same accession id, name and type
     * @param obj TermSynonym object
     * @return true if two TermSynonym objects are equal; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        TermSynonym syn = (TermSynonym) obj;
        return Utils.stringsAreEqualIgnoreCase(this.getTermAcc(), syn.getTermAcc())
            && Utils.stringsAreEqualIgnoreCase(this.getName(), syn.getName())
            && Utils.stringsAreEqualIgnoreCase(this.getType(), syn.getType());
    }

    @Override
    public int hashCode() {
        return (this.getTermAcc()==null ? 0 : this.getTermAcc().hashCode())
            ^  (this.getName()==null ? 0 : this.getName().hashCode())
            ^  (this.getType()==null ? 0 : this.getType().hashCode());
    }

    @Override
    public String toString() {
        return accId+"|"+type+"|"+name;
    }

    public String getTermAcc() {
        return accId;
    }

    public void setTermAcc(String termAcc) {
        this.accId = termAcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbXrefs() {
        return dbxrefs;
    }

    public void setDbXrefs(String dbxrefs) {
        this.dbxrefs = dbxrefs;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
            .put("KEY", key)
            .put("TERM_ACC", accId)
            .put("TYPE", type)
            .put("NAME", name)
            .put("DBXREFS", dbxrefs)
            .put("SOURCE", source)
            .put("CREATED", createdDate)
            .put("LAST_MOD", lastModifiedDate)
            .dump();
    }
}
