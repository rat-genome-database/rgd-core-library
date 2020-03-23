package edu.mcw.rgd.process.generator;

import edu.mcw.rgd.process.mapping.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jdepons on 3/10/2017.
 */
public class OLGAResult {

    private int mapKey;
    ArrayList<String> operators = new ArrayList<String>();
    ArrayList<String> accIds = new ArrayList<String>();
    ArrayList<ArrayList<String>> objectSymbols = new ArrayList<ArrayList<String>>();
    LinkedHashMap<Integer, Object> resultSet = new LinkedHashMap<Integer, Object>();
    HashMap exclude = new HashMap();
    int oKey;
    ObjectMapper om = new ObjectMapper();
    HashMap messages = new HashMap();
    List omLog = new ArrayList();


    public int getMapKey() {
        return mapKey;
    }

    public ArrayList<String> getOperators() {
        return operators;
    }

    public ArrayList<String> getAccIds() {
        return accIds;
    }

    public ArrayList<ArrayList<String>> getObjectSymbols() {
        return objectSymbols;
    }

    public LinkedHashMap<Integer, Object> getResultSet() {
        return resultSet;
    }

    public HashMap getExclude() {
        return exclude;
    }

    public int getoKey() {
        return oKey;
    }

    public ObjectMapper getOm() {
        return om;
    }

    public HashMap getMessages() {
        return messages;
    }

    public List getOmLog() {
        return omLog;
    }
}
