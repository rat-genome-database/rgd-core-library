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
    private static volatile RestHighLevelClient client = null;
    private static final Logger log = LogManager.getLogger(ClientInit.class);

    private static final int ES_PORT = 9200;
    private static final String ES_SCHEME = "http";
    private static final String PROPERTIES_PATH = "/data/properties/elasticsearchProps.properties";

    public static synchronized void init() throws UnknownHostException {
        if (client == null) {
            log.info("Initializing Elasticsearch Client...");
            client = createClient();
            log.info("Initialized Elasticsearch Client");
        }
    }

    private static RestHighLevelClient createClient() throws UnknownHostException {
        if (RgdContext.isProduction() || RgdContext.isPipelines()) {
            Properties props = getProperties();
            if (props.isEmpty()) {
                throw new RuntimeException("Failed to load Elasticsearch properties from " + PROPERTIES_PATH);
            }
            return new RestHighLevelClientBuilder(
                    RestClient.builder(
                            new HttpHost((String) props.get("HOST1"), ES_PORT, ES_SCHEME),
                            new HttpHost((String) props.get("HOST2"), ES_PORT, ES_SCHEME),
                            new HttpHost((String) props.get("HOST3"), ES_PORT, ES_SCHEME),
                            new HttpHost((String) props.get("HOST4"), ES_PORT, ES_SCHEME),
                            new HttpHost((String) props.get("HOST5"), ES_PORT, ES_SCHEME)
                    ).build()).setApiCompatibilityMode(true).build();
        } else {
            return new RestHighLevelClientBuilder(
                    RestClient.builder(
                            new HttpHost("travis.rgd.mcw.edu", ES_PORT, ES_SCHEME)
                    ).build()).setApiCompatibilityMode(true).build();
        }
    }

    public static void setClient(RestHighLevelClient client) {
        ClientInit.client = client;
    }

    public static synchronized void destroy() throws IOException {
        if (client != null) {
            client.close();
            InternalThreadLocalMap.remove();
            client = null;
            log.info("Destroyed Elasticsearch Client");
        } else {
            log.info("Elasticsearch Client is null, nothing to destroy");
        }
    }

    public static synchronized RestHighLevelClient getClient() throws UnknownHostException {
        if (client == null) {
            log.info("Client is null, initiating new client...");
            init();
        }
        return client;
    }

    static Properties getProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_PATH)) {
            props.load(fis);
        } catch (Exception e) {
            log.error("Failed to load Elasticsearch properties from " + PROPERTIES_PATH, e);
        }
        return props;
    }

    public static void main(String[] args) throws IOException {
        ClientInit.init();
        ClientInit.destroy();
    }
}
