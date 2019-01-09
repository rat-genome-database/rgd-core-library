package edu.mcw.rgd.util;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains the coordinates for the PseudoAutosomal Region (PAR).
 *
 * @author Brandon Wilk &lt;bwilk@mcw.edu&gt;
 */
public class PseudoAutosomalRegion {

    ArrayList<HashMap<String, Long>> XPARpositions = new ArrayList<HashMap<String, Long>>();
    ArrayList<HashMap<String, Long>> YPARpositions = new ArrayList<HashMap<String, Long>>();

    /**
     * Creates an object that knows the coordinates of the two Pseudo Autosomal
     * regions. It knows the coordinates only for build 36, but in the future
     * will be updated for the newer builds.
     */
    public PseudoAutosomalRegion() {
        buildPAR("36");
    }

    /**
     * build PAR list which each element is a Map with the stop and start of
     * one region of the PAR
     */
    private void buildPAR(String build) {
        HashMap<String, Long> LocMap = new HashMap<String, Long>();
        if (build.contains("36")) {
            //coordinates for region one on both X and Y for NCBI build 36
            LocMap.put("Start", 1l);
            LocMap.put("Stop", 2709520l);
            XPARpositions.add(LocMap);
            YPARpositions.add(LocMap);

            //coordinates for region two on X chromosome for NCBI build 36
            LocMap = new HashMap<String, Long>();
            LocMap.put("Start", 154584238l);
            LocMap.put("Stop", 154913754l);
            XPARpositions.add(LocMap);

            //coordinates for region two on Y chromosome for NCBI build 36
            LocMap = new HashMap<String, Long>();
            LocMap.put("Start", 57443438l);
            LocMap.put("Stop", 57772954l);
            YPARpositions.add(LocMap);
        } else if (build.contains("37")) {
            //coordinates for region one on X chromosome for GRCh build 37
            LocMap.put("Start", 60001l);
            LocMap.put("Stop", 2699520l);
            XPARpositions.add(LocMap);

            //coordinates for region one on Y chromosome for GRCh build 37
            LocMap = new HashMap<String, Long>();
            LocMap.put("Start", 10001l);
            LocMap.put("Stop", 2649520l);
            YPARpositions.add(LocMap);

            //coordinates for region two on X chromosome for GRCh build 37
            LocMap = new HashMap<String, Long>();
            LocMap.put("Start", 154931044l);
            LocMap.put("Stop", 155260560l);
            XPARpositions.add(LocMap);

            //coordinates for region two on Y chromosome for GRCh build 37
            LocMap = new HashMap<String, Long>();
            LocMap.put("Start", 59034050l);
            LocMap.put("Stop", 59363566l);
            YPARpositions.add(LocMap);
        }
    }

    /**
     * Check to see if the variant denoted by the start and stop positions
     * passed into this function is within the Pseudoautosomal region (PAR).
     * This will be true if it is in one of the PARs, false otherwise
     */
    public boolean inPAR(String chrm, Long pos) {
        boolean par = false;
        if (chrm.equals("X") || chrm.toUpperCase().contains("PAR")) {//if the variant is in one of the sex chromosomes
            for (HashMap<String, Long> pMap : XPARpositions) {
                //check to see if the variant is within the coordinate range of either PAR
                if (pos.compareTo(pMap.get("Start")) >= 0 && pos.compareTo(pMap.get("Stop")) <= 0) {
                    //variant is within the PAR
                    par = true;
                }
            }
        } else if (chrm.equals("Y")) {//if the variant is in one of the sex chromosomes
            for (HashMap<String, Long> pMap : YPARpositions) {
                //check to see if the variant is within the coordinate range of either PAR
                if (pos.compareTo(pMap.get("Start")) >= 0 && pos.compareTo(pMap.get("Stop")) <= 0) {
                    //variant is within the PAR
                    par = true;
                }
            }
        }
        return par;
    }
}