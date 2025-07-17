package edu.mcw.rgd.process.mapping;

import edu.mcw.rgd.dao.impl.MapDAO;
import edu.mcw.rgd.datamodel.Chromosome;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.Utils;

import java.util.*;

/**
 * @author jdepons
 * @since Jun 27, 2008
 * Hold references to maps in the system.  The maps are loaded from the maps table.
 * This class is a singleton.
 */
public class MapManager {

    private java.util.Map<Integer,List<Map>> mapHash = new HashMap<>(); // species_type_key => List<Map>
    private java.util.Map<Integer, Map> keyedHash = new HashMap<>(); // map_key => Map
    private java.util.Map<Integer, Map> primaryMapsForNCBI = new HashMap<>(); // species_type_key => Map
    private java.util.Map<Integer, Map> primaryMapsForEnsembl = new HashMap<>(); // species_type_key => Map

    private java.util.Map<Integer,List<Chromosome>> chromosomeHash = new HashMap<>(); // contains the chromosomes

    /// SINGLETON  ###
    /// lazy initialization with double check locking
    /// safe in multithreaded env

    private static MapManager __instance = null;

    private MapManager() {
        loadDataFromDatabase();
    }

    public static MapManager getInstance() throws Exception {
        if( __instance==null ) {
            synchronized( MapManager.class ) {
                if( __instance==null ) {
                    __instance = new MapManager();
                }
            }
        }
        return __instance;
    }
    /// SINGLETON ### end


    private void loadDataFromDatabase() {

        try {
            MapDAO dao = new MapDAO();
            List<Map> maps = dao.getActiveMaps(null);

            for( Map map: maps ) {
                int speciesTypeKey = map.getSpeciesTypeKey();
                keyedHash.put(map.getKey(), map);

                List<Map> spMaps = mapHash.get(speciesTypeKey);
                if( spMaps==null ) {
                    spMaps = new ArrayList<>();
                    mapHash.put(speciesTypeKey, spMaps);
                }
                spMaps.add(map);

                if( map.isPrimaryRefAssembly() ) {
                    if( map.getSource().equals("NCBI") ) {
                        primaryMapsForNCBI.put(speciesTypeKey, map);
                    } else if( map.getSource().equals("Ensembl") ) {
                        primaryMapsForEnsembl.put(speciesTypeKey, map);
                    }
                }

                if (!chromosomeHash.containsKey(map.getKey())) {
                    //ensure map is loaded
                    chromosomeHash.put(map.getKey(),dao.getChromosomes(map.getKey()));
                }
            }

            System.out.println("MapManager ok -- maps loaded -- "+maps.size()+" maps, for "+mapHash.size()+" species");
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public List<Chromosome> getChromosomes(int mapKey) {
        return chromosomeHash.get(mapKey);
    }

    public List<Map> getAllMaps(int speciesTypeKey) {
        return mapHash.get(speciesTypeKey);
    }

    /**
     * get all maps for given species having specified map unit, like 'bp'
     * @param speciesTypeKey species type key
     * @param mapUnit map unit, like 'bp', 'cM' etc
     * @return list of all maps matching the specified criteria, or empty list
     * @throws Exception
     */
    public List<Map> getAllMaps(int speciesTypeKey, String mapUnit) {

        if( Utils.isStringEmpty(mapUnit) ) {
            return Collections.emptyList();
        }

        List<Map> list = new ArrayList<>();
        for( Map map: getAllMaps(speciesTypeKey) ) {
            if( Utils.stringsAreEqualIgnoreCase(mapUnit, map.getUnit()) ) {
                list.add(map);
            }
        }
        return list;
    }

    public Map getMap(int mapKey) {
        return keyedHash.get(mapKey);
    }

    /**
     * Returns true if the mapKey is a valid key for the species type
     * @param mapKey map key
     * @param speciesTypeKey species type key
     * @return true if given map key is valid for given species
     */
    public boolean isInMap(int mapKey, int speciesTypeKey) {

        for( Map map: getAllMaps(speciesTypeKey)) {
            if (map.getKey() == mapKey) {
                return true;
            }
        }
        return false;
    }

    /// source: NCBI or Ensembl
    public Map getReferenceAssembly(int speciesTypeKey, String source) {

        java.util.Map<Integer, Map> primaryMap;
        if( source.equals("NCBI") ) {
            primaryMap = primaryMapsForNCBI;
        } else if( source.equals("Ensembl") ) {
            primaryMap = primaryMapsForEnsembl;
        } else {
            return null;
        }

        return primaryMap.get(speciesTypeKey);
    }

    public Map getReferenceAssembly(int speciesTypeKey) {
        return getReferenceAssembly(speciesTypeKey, "NCBI");
    }

    /**
     * return true if given map key is a map key for primary ref assembly
     * @param mapKey map key
     * @return true if given map key is a map key for primary ref assembly
     */
    public boolean isMapKeyForPrimaryRefAssembly(int mapKey) {

        // iterate through all species
        for( Map map: primaryMapsForNCBI.values() ) {
            if( map.getKey()==mapKey )
                return true;
        }
        for( Map map: primaryMapsForEnsembl.values() ) {
            if( map.getKey()==mapKey )
                return true;
        }
        return false;
    }
}
