package edu.mcw.rgd.datamodel.solr;

import org.apache.solr.common.SolrInputDocument;

class PubmedSolrDoc extends SolrInputDocument {

    private static final long serialVersionUID = 1L;

    public void addField(String fieldName, Object value) {
        if (value != null) super.addField(fieldName, value);
    }

}