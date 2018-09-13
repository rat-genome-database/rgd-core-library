package edu.mcw.rgd.datamodel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdepons on 4/5/2016.
 */
public class WatchedTerm {

    private String username;
    private String accId;
    private String term;
    int watchingRatGenes;
    int watchingMouseGenes;
    int watchingHumanGenes;
    int watchingRatQTLS;
    int watchingMouseQTLS;
    int watchingHumanQTLS;
    int watchingStrains;
    int watchingRatVariants;

    public static final String GENES_RAT_LABEL="New Gene Annotation (" + SpeciesType.getTaxonomicName(3) + ")";
    public static final String GENES_MOUSE_LABEL="New Gene Annotation (" + SpeciesType.getTaxonomicName(2) + ")";
    public static final String GENES_HUMAN_LABEL="New Gene Annotation (" + SpeciesType.getTaxonomicName(1) + ")";
    public static final String QTLS_RAT_LABEL="New QTL Annotation (" + SpeciesType.getTaxonomicName(3) + ")";
    public static final String QTLS_MOUSE_LABEL="New QTL Annotation (" + SpeciesType.getTaxonomicName(2) + ")";
    public static final String QTLS_HUMAN_LABEL="New QTL Annotation (" + SpeciesType.getTaxonomicName(1) + ")";
    public static final String STRAINS_LABEL="New Strain Annotation";
    public static final String VARIANTS_RAT_LABEL="New Variant Annotation (" + SpeciesType.getTaxonomicName(3) + ")";

    public static List<String> getAllWatchedLabels() {
        ArrayList retList = new ArrayList();

        retList.add(GENES_RAT_LABEL);
        retList.add(GENES_MOUSE_LABEL);
        retList.add(GENES_HUMAN_LABEL);
        retList.add(QTLS_RAT_LABEL);
        retList.add(QTLS_MOUSE_LABEL);
        retList.add(QTLS_HUMAN_LABEL);
        retList.add(STRAINS_LABEL);
        retList.add(VARIANTS_RAT_LABEL);

        return retList;

    }

    public static String getAllWatchedLabelsAsJSON() {

        Gson gson = new Gson();
        return gson.toJson(WatchedObject.getAllWatchedLabels());
    }


    public List<String> getActiveWatchedLabels() {
        ArrayList retList = new ArrayList();


        if (this.watchingRatGenes==1) {
            retList.add(GENES_RAT_LABEL);
        }
        if (this.watchingMouseGenes==1) {
            retList.add(GENES_MOUSE_LABEL);
        }
        if (this.watchingHumanGenes==1) {
            retList.add(GENES_HUMAN_LABEL);
        }

        if (this.watchingRatQTLS==1) {
            retList.add(QTLS_RAT_LABEL);
        }
        if (this.watchingMouseQTLS==1) {
            retList.add(QTLS_MOUSE_LABEL);
        }
        if (this.watchingHumanQTLS==1) {
            retList.add(QTLS_HUMAN_LABEL);
        }

        if (this.watchingStrains == 1) {
            retList.add(STRAINS_LABEL);
        }

        if (this.watchingRatVariants==1) {
            retList.add(VARIANTS_RAT_LABEL);
        }

        return retList;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public int getWatchingRatGenes() {
        return watchingRatGenes;
    }

    public void setWatchingRatGenes(int watchingRatGenes) {
        this.watchingRatGenes = watchingRatGenes;
    }

    public int getWatchingMouseGenes() {
        return watchingMouseGenes;
    }

    public void setWatchingMouseGenes(int watchingMouseGenes) {
        this.watchingMouseGenes = watchingMouseGenes;
    }

    public int getWatchingHumanGenes() {
        return watchingHumanGenes;
    }

    public void setWatchingHumanGenes(int watchingHumanGenes) {
        this.watchingHumanGenes = watchingHumanGenes;
    }

    public int getWatchingRatQTLS() {
        return watchingRatQTLS;
    }

    public void setWatchingRatQTLS(int watchingRatQTLS) {
        this.watchingRatQTLS = watchingRatQTLS;
    }

    public int getWatchingMouseQTLS() {
        return watchingMouseQTLS;
    }

    public void setWatchingMouseQTLS(int watchingMouseQTLS) {
        this.watchingMouseQTLS = watchingMouseQTLS;
    }

    public int getWatchingHumanQTLS() {
        return watchingHumanQTLS;
    }

    public void setWatchingHumanQTLS(int watchingHumanQTLS) {
        this.watchingHumanQTLS = watchingHumanQTLS;
    }

    public int getWatchingStrains() {
        return watchingStrains;
    }

    public void setWatchingStrains(int watchingStrains) {
        this.watchingStrains = watchingStrains;
    }

    public int getWatchingRatVariants() {
        return watchingRatVariants;
    }

    public void setWatchingRatVariants(int watchingRatVariants) {
        this.watchingRatVariants = watchingRatVariants;
    }
}
