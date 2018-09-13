package edu.mcw.rgd.datamodel.search;

import java.util.*;

/**
 * Created by jthota on 2/16/2018.
 */
public class ElasticMappings {


    public static List<String> fields ;
    static{
        List<String> fieldsSet=  new ArrayList<>(Arrays.asList(
                "symbol", "name", "description", "synonyms",
                "term", "term_def",
                "title", "citation", "refAbstract", "author",
                "source", "origin", "trait", "subTrait",
                "xdbIdentifiers", "protein_acc_ids", "transcriptIds", "xdata",
                "promoters","type"
        ));
        fields= Collections.unmodifiableList(fieldsSet);
    }

    public static Map<String, Float> categories;
    static {
        Map<String, Float> categoriesMap=new HashMap<>();
        categoriesMap.put("Gene", 1F);
        categoriesMap.put("Strain", 1F);
        categoriesMap.put("QTL", 1F);
        categoriesMap.put("Variant", 1F);
        categoriesMap.put("SSLP", 1F);
        categories= Collections.unmodifiableMap(categoriesMap);
    }

    public static Map<String, Float> boostValues;

    static{
        Map<String, Float> map=new HashMap<>();
         map.put("name",1F);
         map.put("description",1F );
         map.put("synonyms", 1F);
         map.put("term", 1F);
         map.put("term_def",1F );
         map.put("title", 1F);
         map.put("author", 1F);
         map.put("citation",1F);
         map.put("refAbstract",1F);
         map.put("origin", 1F);
        boostValues=Collections.unmodifiableMap(map);
    }
    public static List<String> clusterUrls;
    static{
        List<String> urls= new ArrayList<>(Arrays.asList(
                "http://gray01.rgd.mcw.edu:9200",
                "http://gray02.rgd.mcw.edu:9200",
                "http://gray03.rgd.mcw.edu:9200",
                "http://gray04.rgd.mcw.edu:9200",
                "http://gray05.rgd.mcw.edu:9200",
                "http://gray06.rgd.mcw.edu:9200",
                "http://gray07.rgd.mcw.edu:9200",
                "http://gray08.rgd.mcw.edu:9200"

        ));
        clusterUrls= Collections.unmodifiableList(urls);
    }
}
