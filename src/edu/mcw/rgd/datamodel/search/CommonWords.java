package edu.mcw.rgd.datamodel.search;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jdepons
 * @since May 28, 2008
 * <p>
 * Contains a list of common words used by the search.
 * Words in this list are not indexed or searchable
 */
public final class CommonWords {

    private static Set<String> ignore = new HashSet<>();

    /**
     * Returns true if term is contained in the common words list
     * @param term
     * @return
     */
    public static boolean isCommon(String term) {
        if (term.trim().length() < 2) {
            return true;
        }

        return ignore.contains(term);
    }
        
    static {
        ignore.add("an");
        ignore.add("and");
        ignore.add("are");
        ignore.add("as");
        ignore.add("at");
        ignore.add("be");
        ignore.add("been");
        ignore.add("by");
        ignore.add("for");
        ignore.add("from");
        ignore.add("has");
        ignore.add("have");
        ignore.add("in");
        ignore.add("is");
        ignore.add("it");
        ignore.add("may");
        ignore.add("of");
        ignore.add("on");
        ignore.add("or");
        ignore.add("the");
        ignore.add("they");
        ignore.add("to");
        ignore.add("was");
        ignore.add("which");
        ignore.add("that");
        ignore.add("there");
        ignore.add("these");
        ignore.add("this");
        ignore.add("were");
        ignore.add("when");
        ignore.add("where");
        ignore.add("with");

        ignore.add("role");
        ignore.add("plays");
        ignore.add("play");
        ignore.add("act");
        ignore.add("acts");

        ignore.add("id");
        ignore.add("pmid");
        ignore.add("egid");
        ignore.add("gene");
        ignore.add("strain");
        ignore.add("strains");
        ignore.add("genes");
        ignore.add("qtl");
        ignore.add("qtls");
        ignore.add("reference");
        ignore.add("references");
        ignore.add("marker");
        ignore.add("markers");
        ignore.add("pubmed");
        ignore.add("xdb");
        ignore.add("uniprot");
        ignore.add("interpro");
        ignore.add("entrez");
        ignore.add("entrezgene");
        ignore.add("pfam");
        ignore.add("geneid");

        ignore.add("**"); // patch to filter out some sql injection attacks
    }
}
