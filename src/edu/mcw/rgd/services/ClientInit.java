package edu.mcw.rgd.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

public class ClientInit {
    private static volatile ElasticsearchClient client = null;
    private static volatile ElasticsearchTransport transport = null;
    private static volatile RestClient restClient = null;
    private static final Logger log = LogManager.getLogger(ClientInit.class);

    private static final int ES_PORT = 9200;
    private static final String ES_SCHEME = "http";
    private static final String PROPERTIES_PATH = "/data/properties/elasticsearchProps.properties";
    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int SOCKET_TIMEOUT_MS = 120_000;

    public static synchronized void init() throws UnknownHostException {
        if (client == null) {
            log.info("Initializing Elasticsearch Client...");
            client = createClient();
            log.info("Initialized Elasticsearch Client");
        }
    }

    private static ElasticsearchClient createClient() throws UnknownHostException {
        RestClientBuilder builder;
        if (RgdContext.isProduction() || RgdContext.isPipelines()) {
            Properties props = getProperties();
            if (props.isEmpty()) {
                throw new RuntimeException("Failed to load Elasticsearch properties from " + PROPERTIES_PATH);
            }
            builder = RestClient.builder(
                    new HttpHost((String) props.get("HOST1"), ES_PORT, ES_SCHEME),
                    new HttpHost((String) props.get("HOST2"), ES_PORT, ES_SCHEME),
                    new HttpHost((String) props.get("HOST3"), ES_PORT, ES_SCHEME),
                    new HttpHost((String) props.get("HOST4"), ES_PORT, ES_SCHEME),
                    new HttpHost((String) props.get("HOST5"), ES_PORT, ES_SCHEME)
            );
        } else {
            builder = RestClient.builder(
                    new HttpHost("localhost", ES_PORT, ES_SCHEME)

            );
        }
        builder.setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder
                        .setConnectTimeout(CONNECT_TIMEOUT_MS)
                        .setSocketTimeout(SOCKET_TIMEOUT_MS)
        );
        restClient = builder.build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper(JacksonConfiguration.MAPPER));
        return new ElasticsearchClient(transport);
    }

    public static void setClient(ElasticsearchClient client) {
        ClientInit.client = client;
    }

    public static synchronized void destroy() throws IOException {
        if (transport != null) {
            transport.close();
            transport = null;
            restClient = null;
            client = null;
            log.info("Destroyed Elasticsearch Client");
        } else {
            log.info("Elasticsearch Client is null, nothing to destroy");
        }
    }

    public static synchronized ElasticsearchClient getClient() throws UnknownHostException {
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
