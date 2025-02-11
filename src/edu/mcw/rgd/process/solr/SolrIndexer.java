package edu.mcw.rgd.process.solr;

import edu.mcw.rgd.dao.impl.solr.SolrDocsDAO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public class SolrIndexer {
    private static final String SOLR_URL = "http://hansen.rgd.mcw.edu:8983/solr/"; // Solr URL
    private static final int BATCH_SIZE = 1000; // Size of each batch
    private static final int COMMIT_INTERVAL = 10000; // Commit after every 10,000 docs

    public static void batchIndexer(int year, String solrCollectionName) throws Exception {
        SolrDocsDAO solrDocsDAO=new SolrDocsDAO();

        SolrClient solrClient= new HttpSolrClient.Builder(SOLR_URL+solrCollectionName)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        solrClient.deleteByQuery("*");
        solrClient.commit();
        //   List<SolrInputDocument> documents=solrDocsDAO.getLimitedSolrDocs(1000);
        List<SolrInputDocument> documents=solrDocsDAO.getSolrDocs(year);
        try{
            addDocumentsToSolr(solrClient, documents);
            solrClient.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                solrClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void addDocumentsToSolr(SolrClient solrClient, List<SolrInputDocument> batch) throws Exception {
        UpdateResponse response = solrClient.add(batch);
        System.out.println("Indexed batch of " + batch.size() + " documents, response status: " + response.getStatus());
    }
    public static void main(String[] args) throws Exception {
        batchIndexer(2025, "core_db");
    }
}
