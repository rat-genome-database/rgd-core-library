package edu.mcw.rgd.services;

import io.netty.util.internal.InternalThreadLocalMap;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

public class ClientInit {
    private static RestHighLevelClient client=null;
    private static ClientInit esClientFactory=null;
    private static final Logger log = LogManager.getLogger(ClientInit.class);
    public static void init() throws UnknownHostException {
        if(esClientFactory==null){
            esClientFactory=new ClientInit();
            log.info("Initializing Elasticsearch Client...");
            if(client==null){
                log.info("CREATING NEW CLIENT...");
                System.out.println("CREATING NEW CLIENT...");
                client=getInstance();
                log.info("Initialized elasticsearch client ...");
                System.out.println("Initialized elasticsearch client ...");
            }
        }

    }
    private static RestHighLevelClient getInstance() throws UnknownHostException {

        if(RgdContext.isProduction() || RgdContext.isPipelines()) {
            Properties props = getProperties();
            return new RestHighLevelClientBuilder(
                    RestClient.builder(
                            new HttpHost((String) props.get("HOST1"), 9200, "http"),
                            new HttpHost((String) props.get("HOST2"), 9200, "http"),
                            new HttpHost((String) props.get("HOST3"), 9200, "http"),
                            new HttpHost((String) props.get("HOST4"), 9200, "http"),
                            new HttpHost((String) props.get("HOST5"), 9200, "http")

                    ).build()).setApiCompatibilityMode(true).build();
        }
        else
            return new RestHighLevelClientBuilder(
                    RestClient.builder(
                            new HttpHost("travis.rgd.mcw.edu", 9200, "http")
                    ).build()).setApiCompatibilityMode(true).build();
    }
    public static void setClient(RestHighLevelClient client) {
        ClientInit.client = client;
    }

    public static synchronized void destroy() throws IOException {


        if(client!=null) {

            client.close();
            InternalThreadLocalMap.remove();
            client=null;
            esClientFactory=null;
            System.out.println("destroyed Elasticsearch Client");
        }else  System.out.println("Elasticsearch Client is null to destroy");
    }
    public static RestHighLevelClient getClient() throws UnknownHostException {
        if(client==null){
            System.out.println("CLIENT IS NULL, INITIATING NEW CLIENT ....");
            init();
        }
        return client;
    }


    static Properties getProperties(){
        Properties props= new Properties();
        FileInputStream fis=null;


        try{

            fis=new FileInputStream("/data/properties/elasticsearchProps.properties");
            props.load(fis);

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
    public static void main(String args[]) throws IOException {

        ClientInit.init();
        ClientInit.destroy();
    }
}
