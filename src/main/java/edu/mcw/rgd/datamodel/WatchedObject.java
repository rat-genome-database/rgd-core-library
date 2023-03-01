package edu.mcw.rgd.datamodel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdepons on 4/5/2016.
 */
public class WatchedObject {
    String username;
    int rgdId;
    String symbol;
    int watchingNomenclature;
    int watchingGo;
    int watchingDisease;
    int watchingPhenotype;
    int watchingPathway;
    int watchingStrain;
    int watchingReference;
    //int watchingAlteredStrains;
    int watchingProtein;
    int watchingInteraction;
    int watchingRefseqStatus;
    int watchingExdb;

    public static final String NOMEN_LABEL="Nomenclature Changes";
    public static final String GO_LABEL="New GO Annotation";
    public static final String DISEASE_LABEL="New Disease Annotation";
    public static final String PHENOTYPE_LABEL="New Phenotype Annotation";
    public static final String PATHWAY_LABEL="New Pathway Annotation";
    public static final String STRAIN_LABEL="New Strain Annotation";
    public static final String REFERENCE_LABEL="New PubMed Reference";
   // public static final String ALTERED_STRAINS_LABEL="Altered Strains";
    public static final String PROTEIN_LABEL="New NCBI Transcript/Protein";
    public static final String INTERACTION_LABEL="New Protein Interaction";
    public static final String REFSEQ_STATUS_LABEL="RefSeq Status Has Changed";
    public static final String EXDB_LABEL="New External Database Link";


    public static List<String> getAllWatchedLabels() {
        ArrayList retList = new ArrayList();

        retList.add(NOMEN_LABEL);
        retList.add(REFSEQ_STATUS_LABEL);
        retList.add(GO_LABEL);
        retList.add(DISEASE_LABEL);
        retList.add(PHENOTYPE_LABEL);
        retList.add(PATHWAY_LABEL);
        retList.add(STRAIN_LABEL);
        retList.add(REFERENCE_LABEL);
       // retList.add(ALTERED_STRAINS_LABEL);
        retList.add(PROTEIN_LABEL);
        retList.add(INTERACTION_LABEL);
        retList.add(EXDB_LABEL);

        return retList;

    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public static String getAllWatchedLabelsAsJSON() {

        Gson gson = new Gson();
        return gson.toJson(WatchedObject.getAllWatchedLabels());
    }

    public List<String> getActiveWatchedLabels() {
        ArrayList retList = new ArrayList();


        if (this.watchingNomenclature==1) {
            retList.add(NOMEN_LABEL);
        }

        if (this.watchingGo==1) {
            retList.add(GO_LABEL);
        }

        if (this.watchingDisease == 1) {
            retList.add(DISEASE_LABEL);
        }

        if (this.watchingPhenotype==1) {
            retList.add(PHENOTYPE_LABEL);
        }

        if (this.watchingPathway==1) {
            retList.add(PATHWAY_LABEL);
        }

        if (this.watchingStrain==1) {
            retList.add(STRAIN_LABEL);
        }

        if (this.watchingReference==1) {
            retList.add(REFERENCE_LABEL);
        }

       // if (this.watchingAlteredStrains==1) {
       //     retList.add(ALTERED_STRAINS_LABEL);
       // }

        if (this.watchingProtein==1) {
            retList.add(PROTEIN_LABEL);
        }

        if (this.watchingInteraction==1) {
            retList.add(INTERACTION_LABEL);
        }

        if (this.watchingRefseqStatus==1) {
            retList.add(REFSEQ_STATUS_LABEL);
        }
        if (this.watchingExdb==1) {
            retList.add(EXDB_LABEL);
        }

        return retList;
    }

    public int getWatchingPhenotype() {
        return watchingPhenotype;
    }

    public void setWatchingPhenotype(int watchingPhenotype) {
        this.watchingPhenotype = watchingPhenotype;
    }

    public int getWatchingInteraction() {
        return watchingInteraction;
    }

    public void setWatchingInteraction(int watchingInteraction) {
        this.watchingInteraction = watchingInteraction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getWatchingNomenclature() {
        return watchingNomenclature;
    }

    public void setWatchingNomenclature(int wachingNomenclature) {
        this.watchingNomenclature = wachingNomenclature;
    }

    public int getWatchingGo() {
        return watchingGo;
    }

    public void setWatchingGo(int watchingGo) {
        this.watchingGo = watchingGo;
    }

    public int getWatchingDisease() {
        return watchingDisease;
    }

    public void setWatchingDisease(int watchingDisease) {
        this.watchingDisease = watchingDisease;
    }

    public int getWatchingPathway() {
        return watchingPathway;
    }

    public void setWatchingPathway(int watchingPathway) {
        this.watchingPathway = watchingPathway;
    }

    public int getWatchingReference() {
        return watchingReference;
    }

    public void setWatchingReference(int watchingReference) {
        this.watchingReference = watchingReference;
    }

   // public int getWatchingAlteredStrains() {
    //    return watchingAlteredStrains;
    //}

    public int getWatchingStrain() {
        return watchingStrain;
    }

    public void setWatchingStrain(int watchingStrain) {
        this.watchingStrain = watchingStrain;
    }

    public int getWatchingExdb() {
        return watchingExdb;
    }

    public void setWatchingExdb(int watchingExdb) {
        this.watchingExdb = watchingExdb;
    }

  //  public void setWatchingAlteredStrains(int watchingAlteredStrains) {
  //      this.watchingAlteredStrains = watchingAlteredStrains;
   // }

    public int getWatchingProtein() {
        return watchingProtein;
    }

    public void setWatchingProtein(int watchingProtein) {
        this.watchingProtein = watchingProtein;
    }

    public int getWatchingRefseqStatus() {
        return watchingRefseqStatus;
    }

    public void setWatchingRefseqStatus(int watchingRefseqStatus) {
        this.watchingRefseqStatus = watchingRefseqStatus;
    }
}
