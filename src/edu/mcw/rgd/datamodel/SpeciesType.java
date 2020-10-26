package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.MapDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * @author jdepons
 * @since Feb 8, 2008
 * <p>
 * Translates species type from a string to and int and vice versa.  Integer values
 * reflect what is in the species type field in the rgd_ids table
 */
public final class SpeciesType {

    public static final int HUMAN = 1;
    public static final int MOUSE = 2;
    public static final int RAT = 3;
    public static final int CHINCHILLA = 4;
    public static final int BONOBO = 5;
    public static final int DOG = 6;
    public static final int SQUIRREL = 7;
    public static final int ZEBRAFISH = 8;
    public static final int PIG = 9;
    public static final int FRUITFLY = 10;
    public static final int ROUNDWORM = 11;
    public static final int YEAST = 12;
    public static final int VERVET = 13;
    public static final int NAKED_MOLE_RAT = 14;

    public static final int ALL = 0;
    public static final int UNKNOWN = -1;

    /// SINGLETON  ###
    /// lazy initialization with double check locking
    /// safe in multithreaded env
    private static SpeciesType __instance = null;

    private SpeciesType() {
        loadDataFromDatabase();
    }

    public static SpeciesType getInstance() {
        if( __instance==null ) {
            synchronized( SpeciesType.class ) {
                if( __instance==null ) {
                    __instance = new SpeciesType();
                }
            }
        }
        return __instance;
    }
    /// SINGLETON ### end

    /**
     * Parses a common or taxonomic name and returns the species type key.  This method returns
     * -1 if the name can not be matched.
     * @param type a string denoting species
     * @return one of (1:HUMAN, 2:MOUSE, 3:RAT, ..., 0:ALL); -1 if species name could not be parsed
     */
    public static int parse(String type)  {

        if( type==null || type.isEmpty() )
            return SpeciesType.UNKNOWN;

        // iterate through all known species, looking for species type key, taxonomic name and common name
        for( int speciesTypeKey: getInstance()._getSpeciesTypeKeys() ) {
            SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);

            // look for common name
            if( type.compareToIgnoreCase(info.commonName)==0  ||
                // look for genebank common name
                type.compareToIgnoreCase(info.genebankCommonName)==0  ||
                // look for taxonomic name
                type.compareToIgnoreCase(info.taxonomicName)==0  ||
                // look for species type key
                type.compareTo(Integer.toString(speciesTypeKey))==0  ||
                // look for taxonomic id: f.e. 'taxon:10090' or 'NCBITaxon:10090'
                (type.startsWith("taxon:") && type.equals("taxon:"+info.taxonomyId)) ||
                (type.startsWith("NCBITaxon:") && type.equals("NCBITaxon:"+info.taxonomyId)) )
            {
                return speciesTypeKey;
            }
        }

        // all match attempts failed
        return SpeciesType.UNKNOWN;
    }

    public static String getNCBIAssemblyDescriptionForSpecies(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? "" : info.ncbiGenomeUrl;
    }


    public static String getImageUrl(int speciesTypeKey) {
        if (speciesTypeKey == SpeciesType.HUMAN) {
            return "humanS.jpg";
        }
        if (speciesTypeKey == SpeciesType.MOUSE) {
            return "mouseS.jpg";
        }
        if (speciesTypeKey == SpeciesType.RAT) {
            return "ratS.png";
        }
        if (speciesTypeKey == SpeciesType.CHINCHILLA) {
            return "chinchillaS.jpg";
        }
        if (speciesTypeKey == SpeciesType.BONOBO) {
            return "bonoboS.jpg";
        }
        if (speciesTypeKey == SpeciesType.DOG) {
            return "dogS.jpg";
        }
        if (speciesTypeKey == SpeciesType.SQUIRREL) {
            return "squirrelS.jpg";
        }
        if (speciesTypeKey == SpeciesType.PIG) {
            return "pig.png";
        }
        if (speciesTypeKey == SpeciesType.VERVET) {
            return "green-monkeyS.png";
        }
        if (speciesTypeKey == SpeciesType.NAKED_MOLE_RAT) {
            return "mole-ratS.png";
        }

        return "";
    }


    public static int getSpeciesTypeKeyForMap(int mapKey) {

        try {
            MapDAO mdao = new MapDAO();

            return mdao.getSpeciesTypeKeyForMap(mapKey);
        } catch (Exception e ) {
            return -1;
        }
    }


    /**
     * Returns the common name based on the species type key passed in, f.e. 'Rat'
     * @param speciesTypeKey  species type key
     * @return common name
     */
    public static String getCommonName(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? "" : info.commonName;
    }

    /**
     * Returns the genebank common name based on the species type key passed in, f.e. ''
     * @param speciesTypeKey  species type key
     * @return Genebank common name
     */
    public static String getGenebankCommonName(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? "" : info.genebankCommonName;
    }

    /**
     * Returns the taxonomic name based on the species type key passed in, f.e. 'Rattus norvegicus'
     * @param speciesTypeKey  species type key
     * @return taxonomic name
     */
    public static String getTaxonomicName(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? "" : info.taxonomicName;
    }

    /**
     * Returns the taxonomic id based on the species type key passed in, f.e. '10116'
     * @param speciesTypeKey  species type key
     * @return taxonomic id or 0 if species type key is invalid
     */
    public static int getTaxonomicId(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? 0 : info.taxonomyId;
    }

    /**
     * Returns the organism genus based on the species type key passed in, f.e. 'Rattus'
     * @param speciesTypeKey  species type key
     * @return organism genus
     */
    public static String getOrganismGenus(int speciesTypeKey) {
        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info==null ? "" : info.genus;
    }

    /**
     * return true if species type key is valid
     * @param speciesTypeKey species type key
     * @return true if species type key is valid
     */
    static public boolean isValidSpeciesTypeKey(int speciesTypeKey) {

        return getInstance()._getSpeciesInfo(speciesTypeKey)!=null;
    }

    /**
     * return true if objects of this species should be indexed by Elastic Search
     * @param speciesTypeKey species type key
     * @return true if species is available in RGD search
     */
    static public boolean isSearchable(int speciesTypeKey) {

        SpeciesInfo info = getInstance()._getSpeciesInfo(speciesTypeKey);
        return info != null && info.isSearchable;
    }

    // patch because isSearchable() method does not work sometimes
    static public boolean isSearchable2(int speciesTypeKey) throws Exception {
        String sql = "SELECT is_searchable FROM species_types WHERE species_type_key=?";
        try( Connection conn = DataSourceFactory.getInstance().getDataSource().getConnection() ) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, speciesTypeKey);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ) {
                boolean isSearchable = rs.getInt("is_searchable")!=0;
                return isSearchable;
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        throw new Exception("database problem when querying SPECIES_TYPES table");
    }

    /**
     * get list of type ids for all known species
     * @return collection of integers
     */
    static public Collection<Integer> getSpeciesTypeKeys() {

        return getInstance()._getSpeciesTypeKeys();
    }


    /// internal preloaded data for performance
    ///
    private Map<Integer, SpeciesInfo> _map = null;

    private SpeciesInfo _getSpeciesInfo(int speciesTypeKey) {
        return _map.get(speciesTypeKey);
    }

    private Collection<Integer> _getSpeciesTypeKeys() {
        return _map.keySet();
    }

    private void loadDataFromDatabase() {
        _map = new HashMap<>();

        // create special species type for any species
        SpeciesInfo info = new SpeciesInfo();
        info.speciesTypeKey = SpeciesType.ALL;
        info.taxonomyId = 0;
        info.genus = "All";
        info.species = "species";
        info.commonName = "All";
        info.genebankCommonName = "";
        info.taxonomicName = info.genus + " " + info.species;
        _map.put(info.speciesTypeKey, info);

        try( Connection conn = DataSourceFactory.getInstance().getDataSource().getConnection() ){

            String sql = "SELECT * FROM species_types";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                info = new SpeciesInfo();
                info.speciesTypeKey = rs.getInt("species_type_key");
                info.taxonomyId = rs.getInt("taxonomy_id");
                info.species = rs.getString("organism_species");
                info.genus = rs.getString("organism_genus");
                info.commonName = rs.getString("common_name");
                info.genebankCommonName = rs.getString("genebank_common_name");
                info.ncbiGenomeUrl = XDBIndex.getInstance().getXDB(61).getUrl(info.speciesTypeKey);
                info.taxonomicName = info.genus + " " + info.species;
                info.isSearchable = rs.getInt("is_searchable") != 0;
                _map.put(info.speciesTypeKey, info);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    class SpeciesInfo {
        public int speciesTypeKey;
        public String genus;
        public String species;
        public String commonName;
        public String genebankCommonName;
        public int taxonomyId;
        public String taxonomicName;
        public String ncbiGenomeUrl;
        public boolean isSearchable;
    }
}
