package edu.mcw.rgd.datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 11/8/11
 * Time: 1:30 PM
 */
public class Xdb implements ObjectWithName {
    private int key;
    private String name;
    private String url;
    private String notes;
    private Map<Integer,String> speciesURL = new HashMap<>();


    public void addURL(int speciesTypeKey, String url) {
        this.speciesURL.put(speciesTypeKey,url);
    }

    public String getSpeciesURL(int speciesTypeKey) {
        return this.speciesURL.get(speciesTypeKey);
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl(int speciesTypeKey) {
        String u = getSpeciesURL(speciesTypeKey);
        return u!=null ? u : getUrl();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * generate <a href="URL_WITH_ACCID">TEXT</a> html link given accession id and text
     * @param accId accession id
     * @param text text to be shown
     * @return html 'A' link
     */
    public String getALink(String accId, String text) {
        return "<A HREF=\""+getUrl()+accId+"\">"+text+"</A>";
    }

    /**
     * generate <a href="URL_WITH_ACCID">TEXT</a> html link given accession id and text
     * @param accId accession id
     * @param text text to be shown
     * @param speciesTypeKey species type key
     * @return html 'A' link
     */
    public String getALink(String accId, String text, int speciesTypeKey) {
        return "<A HREF=\""+getSpeciesURL(speciesTypeKey)+accId+"\">"+text+"</A>";
    }

    @Override
    public String toString() {
        return "XDB_KEY:"+this.getKey()+", NAME:"+this.getName()+", URL:"+this.getUrl();
    }
}
