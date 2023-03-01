package edu.mcw.rgd.process.search;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import static edu.mcw.rgd.datamodel.search.ElasticMappings.clusterUrls;

/**
 * Created by jthota on 2/16/2018.
 */
public class ElasticNode {
       public String getNodeURL(){
          for (String str : clusterUrls) {

            try{
                URL url1 = new URL(str);
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                if(conn.getResponseMessage().equals("OK")) {
                    conn.disconnect();
                    return str.substring(0, str.lastIndexOf(":")).replace("http://", "");
                 }
                conn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public List<String> getNodeURLs(){
           List<String> hostNames= new ArrayList<>();
          for (String str : clusterUrls) {

            try{
                URL url1 = new URL(str);
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                if(conn.getResponseMessage().equals("OK")) {

                    hostNames.add(str.substring(0, str.lastIndexOf(":")).replace("http://", ""));
                 }
                conn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return hostNames;
    }
}
