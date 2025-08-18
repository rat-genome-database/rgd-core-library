package edu.mcw.rgd.services;

import edu.mcw.rgd.datamodel.RgdIndex;
import edu.mcw.rgd.process.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndexAdmin {
    public static final Logger log= LogManager.getLogger(IndexAdmin.class);



    public void createIndex(String mappings, String type) throws Exception {

        GetAliasesRequest aliasesRequest=new GetAliasesRequest(RgdIndex.getIndex());
        boolean existsAlias = ClientInit.getClient().indices().existsAlias(aliasesRequest, RequestOptions.DEFAULT);
        if(existsAlias) {
            for (String index : RgdIndex.getIndices()) {
                aliasesRequest.indices(index);
                existsAlias = ClientInit.getClient().indices().existsAlias(aliasesRequest, RequestOptions.DEFAULT);
                if (!existsAlias) {
                    RgdIndex.setNewAlias(index);
                    GetIndexRequest request1 = new GetIndexRequest(index);
                    boolean indexExists = ClientInit.getClient().indices().exists(request1, RequestOptions.DEFAULT);

                    if (indexExists) {   /**** delete index if exists ****/

                        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
                        ClientInit.getClient().indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                        log.info(index + " deleted");
                    }
                    createNewIndex(index, mappings, type);
                }else {
                    RgdIndex.setOldAlias(index);
                }

            }
        }else{
            GetIndexRequest request1=new GetIndexRequest(RgdIndex.getIndex()+"1");
            boolean indexExists=ClientInit.getClient().indices().exists(request1, RequestOptions.DEFAULT);
            if (indexExists) {   /**** delete index if exists ****/

                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(RgdIndex.getIndex()+"1");
                ClientInit.getClient().indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                log.info(RgdIndex.getIndex()+"1" + " deleted");
            }
            createNewIndex(RgdIndex.getIndex()+"1",mappings, type);

        }



    }

    public void createNewIndex(String index, String _mappings, String type) throws Exception {

        String path="";
        String mappings=null;
        if(_mappings!=null && !_mappings.equals("")){
            path+="data/"+_mappings+".json";
            mappings=new String(Files.readAllBytes(Paths.get(path)));
          //  mappings= new String(Files.readAllBytes(Paths.get("resources/variant_mappings.json")));
        }
        log.info("CREATING NEW INDEX..." + index);
        int replicates=0;
        int shards=5;
//        if(!index.contains("dev") && !index.contains("test")){
        if(RgdContext.isProduction() || RgdContext.isPipelines()){
            replicates=1;
        }
        String analyzers=null;
        try {
          analyzers=  new String(Files.readAllBytes(Paths.get("data/analyzers.json")));
        }catch (Exception ignored){}

        /********* create index, put mappings and analyzers ****/
        CreateIndexRequest request=new CreateIndexRequest(index);
        if(analyzers!=null) {
            request.settings(Settings.builder()
                    .put("index.number_of_shards", shards)
                    .put("index.number_of_replicas", replicates)
                    .loadFromSource(analyzers, XContentType.JSON));
        }else{
            request.settings(Settings.builder()
                    .put("index.number_of_shards", shards)
                    .put("index.number_of_replicas", replicates));
        }
        if(mappings!=null)
        request.mapping(mappings, XContentType.JSON);
        org.elasticsearch.client.indices.CreateIndexResponse createIndexResponse = ClientInit.getClient().indices().create(request, RequestOptions.DEFAULT);

        log.info(index + " created on  " + new Date());

        RgdIndex.setNewAlias(index);
    }
    public int updateIndex() throws Exception {
        if(RgdIndex.getIndex()!=null) {
            log.info("Updating " + RgdIndex.getIndex() + "...");
            GetIndexRequest request=new GetIndexRequest(RgdIndex.getIndex());
            boolean indicesExists=ClientInit.getClient().indices().exists(request, RequestOptions.DEFAULT);
            if (indicesExists) {  /* CHECK IF INDEX NAME PROVIDED EXISTS*/

                RgdIndex.setNewAlias(RgdIndex.getIndex());

                return 1;
            } else {
                log.info("Cannot Update. " + RgdIndex.getIndex() + " does not exists. Use REINDEX option to create index");
                return 0;
            }
        }else {
            log.info("INDEX cannot be null");
            return 0;
        }
    }






}
