package edu.mcw.rgd.datamodel;

import java.util.Date;

/**
 * Created by jthota on 6/30/2017.
 */
public class SearchLog {
    private String searchTerm;
    private String category;
    private long results;
    private Date date;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getResults() {
        return results;
    }

    public void setResults(long results) {
        this.results = results;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
