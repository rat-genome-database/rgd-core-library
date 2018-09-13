package edu.mcw.rgd.datamodel.search;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 28, 2008
 * Time: 9:39:01 AM
 * <p>
 * Contains a list of common words used by the search.
 * Words in this list are not indexed or searchable
 */
public final class CommonWords {

    private static HashMap ignore = new HashMap();

    /**
     * Returns true if term is contained in the common words list
     * @param term
     * @return
     */
    public static boolean isCommon(String term) {
        if (term.trim().length() < 2) {
            return true;
        }

        if (ignore.get(term) == null) {
           return false;
       }
       return true;
    }
        
    static {
        ignore.put("an", "true");
        ignore.put("and", "true");
        ignore.put("are", "true");
        ignore.put("as", "true");
        ignore.put("at", "true");
        ignore.put("be", "true");
        ignore.put("been", "true");
        ignore.put("by", "true");
        ignore.put("for", "true");
        ignore.put("from", "true");
        ignore.put("has", "true");
        ignore.put("have", "true");
        ignore.put("in", "true");
        ignore.put("is", "true");
        ignore.put("it", "true");
        ignore.put("may", "true");
        ignore.put("of", "true");
        ignore.put("on", "true");
        ignore.put("or", "true");
        ignore.put("the", "true");
        ignore.put("they", "true");
        ignore.put("to", "true");
        ignore.put("was", "true");
        ignore.put("which", "true");
        ignore.put("that", "true");
        ignore.put("there", "true");
        ignore.put("these", "true");
        ignore.put("this", "true");
        ignore.put("were", "true");
        ignore.put("when", "true");
        ignore.put("where", "true");
        ignore.put("with", "true");

        ignore.put("role", "true");
        ignore.put("plays", "true");
        ignore.put("play", "true");
        ignore.put("act", "true");
        ignore.put("acts", "true");

        ignore.put("id", "true");
        ignore.put("pmid", "true");
        ignore.put("egid", "true");
        ignore.put("gene", "true");
        ignore.put("strain", "true");
        ignore.put("strains", "true");
        ignore.put("genes", "true");
        ignore.put("qtl", "true");
        ignore.put("qtls", "true");
        ignore.put("reference", "true");
        ignore.put("references", "true");
        ignore.put("marker", "true");
        ignore.put("markers", "true");
        ignore.put("pubmed", "true");
        ignore.put("xdb", "true");
        ignore.put("uniprot", "true");
        ignore.put("interpro", "true");
        ignore.put("entrez", "true");
        ignore.put("entrezgene", "true");
        ignore.put("pfam", "true");
        ignore.put("geneid", "true");
    }
}
