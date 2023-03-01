package edu.mcw.rgd.process.mapping;

import edu.mcw.rgd.dao.impl.MapDAO;
import edu.mcw.rgd.datamodel.Chromosome;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.process.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jdepons
 * @since Jun 27, 2008
 * Hold references to maps in the system.  The maps are loaded from the maps table.
 * This class is a singleton.
 * Initialization-on-demand holder idiom is used for thread-safe lazy loading of this singleton.
 */
public class MapManager {

    private java.util.Map<Integer,List<Map>> mapHash = new ConcurrentHashMap<>(); // species_type_key => List<Map>
    private java.util.Map<Integer, Map> keyedHash = new ConcurrentHashMap<>(); // map_key => Map
    private java.util.Map<Integer, Map> primaryMapsForNCBI = new ConcurrentHashMap<>(); // species_type_key => Map
    private java.util.Map<Integer, Map> primaryMapsForEnsembl = new ConcurrentHashMap<>(); // species_type_key => Map

    private java.util.Map<Integer,List<Chromosome>> chromosomeHash = new ConcurrentHashMap<>(); // contains the chromosomes
    /**
     * Returns the map manager instance.
     *
     * @return
     * @throws Exception
     */
    public static MapManager getInstance() throws Exception {

        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final MapManager INSTANCE = new MapManager();
    }

    /**
     * loads data for given species
     * @param speciesTypeKey species type key
     * @return
     */
    Exception loadForSpecies(int speciesTypeKey) throws Exception{
        MapDAO dao = new MapDAO();
        List<Map> maps;
        try {
            maps = dao.getActiveMaps(speciesTypeKey, null);
        } catch(Exception e) {
            e.printStackTrace();
            return e;
        }

        mapHash.putIfAbsent(speciesTypeKey, maps);

        for( Map map: maps ) {
            keyedHash.putIfAbsent(map.getKey(), map);

            if( map.isPrimaryRefAssembly() ) {
                if( map.getSource().equals("NCBI") ) {
                    primaryMapsForNCBI.putIfAbsent(speciesTypeKey, map);
                } else if( map.getSource().equals("Ensembl") ) {
                    primaryMapsForEnsembl.putIfAbsent(speciesTypeKey, map);
                }
            }

            if (!chromosomeHash.containsKey(map.getKey())) {
                //ensure map is loaded
                chromosomeHash.put(map.getKey(),dao.getChromosomes(map.getKey()));
            }
        }
        return null;
    }

    public List<Chromosome> getChromosomes(int mapKey) {
        Map m = getMap(mapKey);
        return chromosomeHash.get(mapKey);
    }


    public List<Map> getAllMaps(int speciesTypeKey) throws Exception{

        List<Map> list = mapHash.get(speciesTypeKey);
        if( list==null ) {
            Exception e = loadForSpecies(speciesTypeKey);
            if( e!=null ) {
                throw new Exception("Problem loading maps for species type " + speciesTypeKey, e);
            }
            list = mapHash.get(speciesTypeKey);
        }
        return list;
    }

    /**
     * get all maps for given species having specified map unit, like 'bp'
     * @param speciesTypeKey species type key
     * @param mapUnit map unit, like 'bp', 'cM' etc
     * @return list of all maps matching the specified criteria, or empty list
     * @throws Exception
     */
    public List<Map> getAllMaps(int speciesTypeKey, String mapUnit) throws Exception{

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

    /**
     * Returns a map based on map key passed in
     *
     * @param mapKey map key
     * @return Map object
     */
    public Map getMap(int mapKey) {
        Map map = keyedHash.get(mapKey);
        if( map == null ) {
            // map not found: make sure maps for given species are up-to-date
            try {
                int speciesTypeKey = new MapDAO().getSpeciesTypeKeyForMap(mapKey);
                loadForSpecies(speciesTypeKey);
                map = keyedHash.get(mapKey);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Returns true if the mapKey is a valid key for the species type
     * @param mapKey map key
     * @param speciesTypeKey species type key
     * @return true if given map key is valid for given species
     */
    public boolean isInMap(int mapKey, int speciesTypeKey) throws Exception {

        for( Map map: getAllMaps(speciesTypeKey)) {
            if (map.getKey() == mapKey) {
                return true;
            }
        }
        return false;
    }

    /// source: NCBI or Ensembl
    public Map getReferenceAssembly(int speciesTypeKey, String source) throws Exception{

        java.util.Map<Integer, Map> primaryMap;
        if( source.equals("NCBI") ) {
            primaryMap = primaryMapsForNCBI;
        } else if( source.equals("Ensembl") ) {
            primaryMap = primaryMapsForEnsembl;
        } else {
            return null;
        }

        Map map = primaryMap.get(speciesTypeKey);
        if( map==null ) {
            Exception e = loadForSpecies(speciesTypeKey);
            if( e!=null ) {
                throw new Exception("Problem loading reference assembly for species " + speciesTypeKey+" and assembly source ="+source, e);
            }
            map = primaryMap.get(speciesTypeKey);
        }
        return map;
    }

    public Map getReferenceAssembly(int speciesTypeKey) throws Exception{
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
