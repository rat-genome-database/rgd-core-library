package edu.mcw.rgd.process.search;

import edu.mcw.rgd.datamodel.search.CommonWords;
import edu.mcw.rgd.process.Utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 27, 2008
 * Time: 3:58:18 PM
 */
public class SearchBean {

    /// maximum nr of 'required', 'negated', or 'optional' words that will be used in searching
    /// there was a case where the user entered 1000+ a term with 1000+ words and that almost killed our db
    public static final int MAX_WORDS_SEARCHED = 10;

    List<String> required;
    List<String> negated;
    List<String> optional;

    private String term = "";
    private int speciesType = 3;
    private String chr = "";
    private int map = -1;
    private int start = -1;
    private int stop = -1;
    private int fmt = 1;
    private int sort=-1;
    private int order=-1;
    private String obj = "";
    private String termAccId1; // limit results to term with descendants
    private String termAccId2; // limit results to yet another term with descendants
    private boolean isChinchilla; // if true, search is run on chinchilla website (ie it is limited to human and chinch only)

    /**
     * return
     * @return true if either a search term is specified, or chromosome or at least one ontology filter
     */
    public boolean isSearchable() {
        return (required!=null && required.isEmpty() ) ||
                !Utils.isStringEmpty(chr) ||
                !Utils.isStringEmpty(termAccId1) ||
                !Utils.isStringEmpty(termAccId2);
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public int getFmt() {
        return fmt;
    }

    public void setFmt(int fmt) {
        this.fmt = fmt;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<String> getOptional() {
        return optional;
    }

    public String getTerm() {
        return term;
    }

    public int getSpeciesType() {
        return speciesType;
    }

    public String getChr() {
        return chr;
    }

    public int getMap() {
        return map;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }

    public List<String> getRequired() {
        return required;
    }

    public List<String> getNegated() {
        return negated;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public void setSpeciesType(int speciesType) {
        this.speciesType = speciesType;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getTermAccId1() {
        return termAccId1;
    }

    public void setTermAccId1(String termAccId1) {
        this.termAccId1 = termAccId1;
    }

    public String getTermAccId2() {
        return termAccId2;
    }

    public void setTermAccId2(String termAccId2) {
        this.termAccId2 = termAccId2;
    }

    public void setTerm(String term) {
        this.term = term;
        required = new ArrayList<String>();
        negated = new ArrayList<String>();

        term = term.toLowerCase();

        String word = "";
        boolean isNegated = false;

        for (int i=0; i< term.length(); i++) {
            char c = term.charAt(i);

            if (c=='-' && word.isEmpty() ) {
                // for negated word to be recognized, '-' must be the 1st character of the term
                // or be preceded by a whitespace ( so you could search for terms like '(-)-noscapine')
                if( i==0 || Character.isWhitespace(c) )
                    isNegated = true;
                continue;
            }

            if (c=='\"') {
                int closeQuoteIndex = term.indexOf('\"', i+1);
                if (closeQuoteIndex != -1) {
                    word = term.substring(i+1, closeQuoteIndex);
                    i = closeQuoteIndex;

                    if (isNegated) {
                        addToNegated(word);
                        isNegated = false;
                    }else {
                        addToRequired(word);
                    }
                    word="";
                }
            } else {
                boolean found;
                // optimization: the original code below uses regular expressions what is inefficient
                //
                // Pattern pattern = Pattern.compile("[a-zA-Z_0-9\\*]");
                // Matcher matcher = pattern.matcher(Character.toString(c));
                // found = matcher.find();
                found = Character.isLetterOrDigit(c) || c=='_' || c=='*';

                if (found) {
                    word += c;
                }else {
                    if (!CommonWords.isCommon(word)) {
                        if (isNegated) {
                            addToNegated(word);
                            isNegated = false;
                        }else {
                            addToRequired(word);
                        }
                    }
                    word="";
                }
            }
        }

        if (!CommonWords.isCommon(word)) {
            if (isNegated) {
                addToNegated(word);
            }else {
                addToRequired(word);
            }
        }

        handleTermWithPunctuation(term);
    }

    void handleTermWithPunctuation(String term) {

        // term is already lower case
        // all single words have been already collected
        // but some terms, like 'gUwm54-14' or 'WF.WKY-(D5Wox8-D5Uwm62)/Uwm' should be searchable too
        // therefore we split the searched term by whitespace and add it to required words
        for( String word: term.split("[\\s]", -1) ) {
            if (!CommonWords.isCommon(word)) {
                if( !required.contains(word) ) {
                    addToRequired(word);
                }
            }
        }
    }

    public boolean isChinchilla() {
        return isChinchilla;
    }

    public void setChinchilla(boolean chinchilla) {
        isChinchilla = chinchilla;
    }

    void addToRequired(String word) {
        addToList(word, required);
    }

    void addToNegated(String word) {
        addToList(word, negated);
    }

    void addToOptional(String word) {
        addToList(word, optional);
    }

    void addToList(String word, List<String> list) {
        if( list!=null && list.size()<MAX_WORDS_SEARCHED ) {
            list.add(word);
        }
    }
}
