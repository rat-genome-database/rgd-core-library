package edu.mcw.rgd.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import edu.mcw.rgd.datamodel.RgdIndex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class IndexAdmin {
    public static final Logger log = LogManager.getLogger(IndexAdmin.class);

    public void createIndex(String mappings, String type) throws Exception {
        ElasticsearchClient client = ClientInit.getClient();
        String aliasName = RgdIndex.getIndex();

        boolean existsAlias = client.indices().existsAlias(b -> b.name(aliasName)).value();
        if (existsAlias) {
            for (String index : RgdIndex.getIndices()) {
                boolean indexHasAlias = client.indices()
                        .existsAlias(b -> b.name(aliasName).index(index))
                        .value();
                if (!indexHasAlias) {
                    RgdIndex.setNewAlias(index);
                    boolean indexExists = client.indices().exists(b -> b.index(index)).value();
                    if (indexExists) {
                        client.indices().delete(b -> b.index(index));
                        log.info(index + " deleted");
                    }
                    createNewIndex(index, mappings, type);
                } else {
                    RgdIndex.setOldAlias(index);
                }
            }
        } else {
            String firstIndex = aliasName + "1";
            boolean indexExists = client.indices().exists(b -> b.index(firstIndex)).value();
            if (indexExists) {
                client.indices().delete(b -> b.index(firstIndex));
                log.info(firstIndex + " deleted");
            }
            createNewIndex(firstIndex, mappings, type);
        }
    }

    public void createNewIndex(String index, String _mappings, String type) throws Exception {
        String mappings = null;
        if (_mappings != null && !_mappings.isEmpty()) {
            String path = "data/" + _mappings + ".json";
            mappings = new String(Files.readAllBytes(Paths.get(path)));
        }
        log.info("CREATING NEW INDEX..." + index);
        int replicates = 0;
        int shards = 1;
        if (RgdContext.isProduction() || RgdContext.isPipelines()) {
            replicates = 1;
        }
        String analyzers = null;
        try {
            analyzers = new String(Files.readAllBytes(Paths.get("data/analyzers.json")));
        } catch (Exception ignored) {
        }

        final String finalMappings = mappings;
        final String finalAnalyzers = analyzers;
        final String finalShards = String.valueOf(shards);
        final String finalReplicates = String.valueOf(replicates);

        CreateIndexResponse response = ClientInit.getClient().indices().create(c -> {
            c.index(index);
            c.settings(s -> {
                if (finalAnalyzers != null) {
                    s.withJson(new StringReader(finalAnalyzers));
                }
                s.numberOfShards(finalShards);
                s.numberOfReplicas(finalReplicates);
                return s;
            });
            if (finalMappings != null) {
                c.mappings(m -> m.withJson(new StringReader(finalMappings)));
            }
            return c;
        });

        log.info(index + " created on " + new Date());
        RgdIndex.setNewAlias(index);
    }

    public int updateIndex() throws Exception {
        if (RgdIndex.getIndex() != null) {
            log.info("Updating " + RgdIndex.getIndex() + "...");
            boolean indicesExists = ClientInit.getClient().indices()
                    .exists(b -> b.index(RgdIndex.getIndex())).value();
            if (indicesExists) {
                RgdIndex.setNewAlias(RgdIndex.getIndex());
                return 1;
            } else {
                log.info("Cannot Update. " + RgdIndex.getIndex() + " does not exists. Use REINDEX option to create index");
                return 0;
            }
        } else {
            log.info("INDEX cannot be null");
            return 0;
        }
    }
}
