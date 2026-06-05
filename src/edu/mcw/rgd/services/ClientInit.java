package edu.mcw.rgd.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.File;
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
    // Override the props location for non-Linux dev boxes via -DelasticsearchProps=... or env ELASTICSEARCH_PROPS.
    private static final String PROPERTIES_PATH_PROPERTY = "elasticsearchProps";
    private static final String PROPERTIES_PATH_ENV = "ELASTICSEARCH_PROPS";
    private static final String USERNAME_KEY = "USERNAME";
    private static final String PASSWORD_KEY = "PASSWORD";
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
        Properties props = getProperties();
        RestClientBuilder builder;
        if (RgdContext.isProduction() || RgdContext.isPipelines()) {
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
                    new HttpHost("travis.rgd.mcw.edu", ES_PORT, ES_SCHEME)

            );
        }
        builder.setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder
                        .setConnectTimeout(CONNECT_TIMEOUT_MS)
                        .setSocketTimeout(SOCKET_TIMEOUT_MS)
        );

        // Elasticsearch 9 has security enabled by default, so the cluster rejects
        // unauthenticated requests with "security_exception: missing authentication
        // credentials". Attach HTTP basic auth read from the properties file.
        CredentialsProvider credentialsProvider = buildCredentialsProvider(props);
        if (credentialsProvider != null) {
            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        } else {
            log.warn("No Elasticsearch credentials ({}/{}) found in {}; requests will be unauthenticated "
                    + "and will fail against a security-enabled cluster.", USERNAME_KEY, PASSWORD_KEY, PROPERTIES_PATH);
        }

        restClient = builder.build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper(JacksonConfiguration.MAPPER));
        return new ElasticsearchClient(transport);
    }

    private static CredentialsProvider buildCredentialsProvider(Properties props) {
        String username = props.getProperty(USERNAME_KEY);
        String password = props.getProperty(PASSWORD_KEY);
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
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
        String path = getPropertiesPath();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (Exception e) {
            log.error("Failed to load Elasticsearch properties from " + path, e);
        }
        return props;
    }

    private static String getPropertiesPath() {
        String override = System.getProperty(PROPERTIES_PATH_PROPERTY);
        if (override == null || override.isEmpty()) {
            override = System.getenv(PROPERTIES_PATH_ENV);
        }
        return (override != null && !override.isEmpty()) ? override : PROPERTIES_PATH;
    }

    public static void main(String[] args) throws IOException {
        ClientInit.init();
        ClientInit.destroy();
    }
}
