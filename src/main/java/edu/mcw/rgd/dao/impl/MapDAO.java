package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;

/**
 * @author jdepons
 * @since Jun 24, 2008
 */
@SuppressWarnings("unchecked")
public class MapDAO extends AbstractDAO {

    /**
     * get all active NCBI maps for all species
     * @return list of all active maps
     * @throws Exception when unexpected error in spring framework occurs
     * @deprecated
     */
    public List<Map> getActiveMaps() throws Exception {
        return getActiveMaps("NCBI");
    }

    /**
     * get all active maps for all species, for given assembly source
     * @param source assembly source: 'NCBI','Ensembl', or null if any
     * @return list of all active maps
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getActiveMaps(String source) throws Exception {

        if( source==null ) {
            String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                    "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status='ACTIVE'";
            return executeMapQuery(sql);
        } else {
            String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                    "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status='ACTIVE' AND m.source=?";
            return executeMapQuery(sql, source);
        }
    }

    /**
     * get all active maps for given species and assembly source
     * @param speciesTypeKey species type key
     * @param source assembly source: 'NCBI','Ensembl',or null
     * @return List of Map objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getActiveMaps(int speciesTypeKey, String source) throws Exception {

        if( source==null ) {
            String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                    "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status='ACTIVE' AND i.species_type_key=?";
            return executeMapQuery(sql, speciesTypeKey);
        } else {
            String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                    "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status='ACTIVE' AND i.species_type_key=? AND m.source=?";
            return executeMapQuery(sql, speciesTypeKey, source);
        }
    }

    /**
     * get all active maps by order rank DESC
     * @return List of Map objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getActiveMapsByRankASC() throws Exception {


            String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                    "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status='ACTIVE' " +
                    "order by m.rank asc";
            return executeMapQuery(sql);

    }

    public int getSpeciesTypeKeyForMap(int mapKey) throws Exception{

        String sql = "SELECT species_type_key FROM maps m, rgd_ids ri WHERE m.rgd_id=ri.rgd_id AND m.map_key=?";
        return getCount(sql, mapKey);
    }


    /**
     * get all active maps for given species
     * @param speciesTypeKey species type key
     * @return list of all active maps for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getMaps(int speciesTypeKey) throws Exception {

        String sql = "select m.*, i.species_type_key from rgd_ids i, maps m " +
                     "where i.rgd_id = m.rgd_id and i.object_key=10 and i.object_status = 'ACTIVE' and i.species_type_key=? AND m.source='NCBI'";
        return executeMapQuery(sql, speciesTypeKey);
    }

    /**
     * get all active maps for given species
     * @param speciesTypeKey species type key
     * @param mapUnit map unit
     * @return list of all active maps for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getMaps(int speciesTypeKey, String mapUnit) throws Exception {
        return getMaps(speciesTypeKey,mapUnit,"NCBI");
    }

    /**
     * get all active maps for given species
     * @param speciesTypeKey species type key
     * @param mapUnit map unit
     * @param source source of assembly
     * @return list of all active maps for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getMaps(int speciesTypeKey, String mapUnit, String source) throws Exception {

        String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                "WHERE i.rgd_id = m.rgd_id AND i.object_key=10 AND i.object_status = 'ACTIVE' "+
                "  AND i.species_type_key=? AND m.map_unit=? AND m.source=? ORDER BY rank";
        return executeMapQuery(sql, speciesTypeKey, mapUnit,source);
    }
    /**
     * get primary reference assemblies for all species
     * @return list of primary reference assemblies for all species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getPrimaryRefAssemblies() throws Exception {
        return getPrimaryRefAssemblies("NCBI");
    }

    /**
     * get primary reference assemblies for all species
     * @return list of primary reference assemblies for all species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Map> getPrimaryRefAssemblies(String source) throws Exception {

        String sql = "select m.*, i.species_type_key from rgd_ids i, maps m " +
                     "where i.rgd_id = m.rgd_id and m.primary_ref_assembly_ind='Y' and m.source=?";
        return executeMapQuery(sql,source);
    }

    /**
     * get primary ref assembly for given species
     * @param speciesTypeKey species type key
     * @return primary ref assembly for given species
     * @throws Exception throws MapDAOException either if there is no primary ref assembly for the given species
     *   or if there are multiple primary ref assemblies for the species; Exception is also thrown when unexpected
     *   error in spring framework occurs
     */
    public Map getPrimaryRefAssembly(int speciesTypeKey) throws Exception {
        return getPrimaryRefAssembly(speciesTypeKey,"NCBI");
    }

    /**
     * get primary ref assembly for given species
     * @param speciesTypeKey species type key
     * @return primary ref assembly for given species
     * @throws Exception throws MapDAOException either if there is no primary ref assembly for the given species
     *   or if there are multiple primary ref assemblies for the species; Exception is also thrown when unexpected
     *   error in spring framework occurs
     */
    public Map getPrimaryRefAssembly(int speciesTypeKey, String source) throws Exception {

        String sql = "SELECT m.*, i.species_type_key FROM rgd_ids i, maps m " +
                     "WHERE i.rgd_id=m.rgd_id AND m.primary_ref_assembly_ind='Y' AND i.species_type_key=? and source=?";
        List<Map> maps = executeMapQuery(sql, speciesTypeKey,source);
        if( maps.isEmpty() )
            throw new MapDAOException("No primary reference assembly found for species type key "+speciesTypeKey+" and source "+source);
        if( maps.size()>1 )
            throw new MapDAOException("Multiple primary reference assemblies found for species type key "+speciesTypeKey+" and source "+source);
        return maps.get(0);
    }

    /**
     * get map by map key
     * @param mapKey map key
     * @return Map object by map key
     * @throws Exception throws MapDAOException if there is no map with such map key; throws Exception when unexpected error in spring framework occurs
     */
    public Map getMap(int mapKey) throws Exception{
        Map map = getMapByKey(mapKey);
        if( map==null )
            throw new MapDAOException("No map for map key "+mapKey);
        return map;
    }

    /**
     * get map by map key
     * @param mapKey map key
     * @return Map object or null if invalid map key
     * @throws Exception throws Exception when unexpected error in spring framework occurs
     */
    public Map getMapByKey(int mapKey) throws Exception{
        String sql = "SELECT m.*,r.species_type_key FROM maps m,rgd_ids r WHERE map_key=? AND m.rgd_id=r.rgd_id";
        List<Map> maps = executeMapQuery(sql, mapKey);
        if( maps.isEmpty() )
            return null;
        return maps.get(0);
    }

    public List<MapData> getMapDataWithinRange(int lowBound, int highBound, String chr, int mapKey, int range) throws Exception{

        int startRange = lowBound-range;
        int stopRange = highBound+range;

        String query = "SELECT m.*, r.species_type_key FROM maps_data m, rgd_ids r, genes g WHERE " +
                "m.CHROMOSOME=? " +
                "and m.MAP_KEY=? " +
                "and m.STOP_POS>=? " +
                "and m.START_POS<=? " +
                "and m.RGD_ID=r.RGD_ID " +
                "and g.RGD_ID=r.RGD_ID " +
                "and r.OBJECT_KEY=1 " +
                "and r.OBJECT_STATUS='ACTIVE' " +
                "ORDER BY g.gene_symbol_lc";

        return executeMapDataQuery(query, chr, mapKey, startRange, stopRange);
    }


    /**
     * get all positions for the object with given rgd id
     * @param rgdId object rgd id
     * @return list of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapData(int rgdId) throws Exception{
        String query = "SELECT * FROM maps_data WHERE rgd_id=? ORDER BY map_key DESC";
        return executeMapDataQuery(query, rgdId);
    }

    /**
     * Returns maps by rank
     * @param rgdId object rgd id
     * @return list of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapDataByRank(int rgdId) throws Exception{
        String query = " SELECT * FROM maps_data md \n" +
                " inner join maps m on m.map_key=md.map_key WHERE md.rgd_id=? ORDER BY m.rank ASC";
        return executeMapDataQuery(query, rgdId);
    }

    /**
     * get all positions for the objects with given rgd ids
     * @param rgdIds list of rgd id
     * @return list of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapData(List<Integer> rgdIds) throws Exception{
        String query = "SELECT * FROM maps_data WHERE rgd_id IN("+ Utils.buildInPhrase(rgdIds)+")";
        return executeMapDataQuery(query);
    }

    /**
     * get all positions on specific map for the object with given rgd id
     * @param rgdId object rgd id
     * @param mapKey map key
     * @return list of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapData(int rgdId, int mapKey) throws Exception{

        String query = "SELECT * FROM maps_data WHERE rgd_id=? AND map_key=?";
        return executeMapDataQuery(query, rgdId, mapKey);
    }

    /**
     * get all positions on specific map for the object with given rgd id, for specific pipeline
     * @param rgdId object rgd id
     * @param mapKey map key
     * @param srcPipeline source pipeline
     * @return list of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapData(int rgdId, int mapKey, String srcPipeline) throws Exception{

        String query = "SELECT * FROM maps_data WHERE rgd_id=? AND map_key=? AND src_pipeline=?";
        return executeMapDataQuery(query, rgdId, mapKey, srcPipeline);
    }

    /**
     * get map position given map data key
     * @param mapDataKey map data key
     * @return MapData object when key is valid
     * @throws Exception MapDAOException when key is invalid; spring dao exception if something really bad occurs in spring framework
     */
    public MapData getMapDataByKey(int mapDataKey) throws Exception{
        String query = "SELECT * FROM maps_data WHERE maps_data_key=?";
        List list = executeMapDataQuery(query, mapDataKey);

        if (list.size() == 0) {
            throw new MapDAOException("Map Data for key " + mapDataKey + " not found");
        }

        return (MapData) list.get(0);
    }

    /**
     * return all positions for given map and chromosome
     * @param chromosome chromosome
     * @param mapKey map key
     * @return List of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapDataByMapKeyChr(String chromosome, int mapKey) throws Exception {

        String query = "SELECT * FROM maps_data WHERE map_key=? AND chromosome=?";
        return executeMapDataQuery(query, mapKey, chromosome);
    }

    /**
     * return all positions for given map, chromosome and object type
     * @param chromosome chromosome
     * @param mapKey map key
     * @param objectKey object key
     * @return List of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapDataByMapKeyChr(String chromosome, int mapKey, int objectKey) throws Exception {

        String query = "SELECT md.* FROM maps_data md,rgd_ids r "+
                "WHERE map_key=? AND chromosome=? AND object_key=? AND r.rgd_id=md.rgd_id";
        return executeMapDataQuery(query, mapKey, chromosome, objectKey);
    }

    /**
     * return all positions for given map, and object type
     * @param mapKey map key
     * @param objectKey object key
     * @param srcPipeline source pipeline
     * @return List of MapData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<MapData> getMapDataByMapKeyObject(int mapKey, int objectKey, String srcPipeline) throws Exception {

        String query = "SELECT md.* FROM maps_data md,rgd_ids r "+
                "WHERE map_key=? AND object_key=? AND r.rgd_id=md.rgd_id AND src_pipeline=?";
        return executeMapDataQuery(query, mapKey, objectKey, srcPipeline);
    }

    /**
     * update a single MapData object
     * @param md MapData object
     * @return number of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateMapData(MapData md) throws Exception{

        List<MapData> mapDataList = new ArrayList<>(1);
        mapDataList.add(md);
        return updateMapData(mapDataList);
    }

    /**
     * update multiple MapData object
     * @param mdList list of MapData objects
     * @return number of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateMapData(List<MapData> mdList) throws Exception{

        String sql = "UPDATE maps_data SET f_or_p=?, " +
                "chromosome=?, fish_band=?, abs_position=?, lod=?, notes=?, map_key=?, rgd_id=?, " +
                "rgd_id_up=?, rgd_id_dw=?, start_pos=?, stop_pos=?, multiple_chromosome=?, strand=?, " +
                "maps_data_position_method_id=?, src_pipeline=? WHERE maps_data_key=?";

        return upsertMapData(sql, mdList);
    }

    /**
     * create a new genomic coordinates in MAPS_DATA table
     * @param md parameters of new MAPS_DATA object with genomic coordinates
     * @return number of rows affected
     * @throws Exception if something unexpected happens in the framework
     */
    public int insertMapData(MapData md) throws Exception{

        List<MapData> mapDataList = new ArrayList<>(1);
        mapDataList.add(md);
        return insertMapData(mapDataList);
    }

    /**
     * insert a list of MapData objects
     * @param mds list of MapData objects to be inserted
     * @return number of rows affected
     * @throws Exception if something unexpected happens in the framework
     */
    public int insertMapData(List<MapData> mds) throws Exception{

        String sql = "INSERT INTO maps_data (f_or_p, " +
                "chromosome, fish_band, abs_position, lod, notes, map_key, rgd_id, rgd_id_up, rgd_id_dw, " +
                "start_pos, stop_pos, multiple_chromosome, strand, maps_data_position_method_id, src_pipeline, maps_data_key) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        int mdKey = getNextKey("MAPS_DATA", "MAPS_DATA_KEY");
        for( MapData md: mds ) {
            md.setKey(mdKey++);
        }
        return upsertMapData(sql, mds);
    }

    private int upsertMapData(String sql, List<MapData> mds) throws Exception{

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql,
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.DOUBLE, Types.DOUBLE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
                Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                Types.INTEGER, Types.VARCHAR, Types.INTEGER});
        su.compile();

        for( MapData md: mds ) {
            su.update(md.getFOrP(), md.getChromosome(), md.getFishBand(),
                md.getAbsPosition(), md.getLod(), md.getNotes(), md.getMapKey(), md.getRgdId(), md.getRgdIdUp(),
                md.getRgdIdDown(), md.getStartPos(), md.getStopPos(), md.getMultipleChromosome(), md.getStrand(),
                md.getMapsDataPositionMethodId(), md.getSrcPipeline(), md.getKey());
        }
        return executeBatch(su);
    }

    /**
     * delete map data given map data key
     * @param mapsDataKey map data key
     * @return number of rows deleted
     * @throws Exception
     */
    public int deleteMapData(int mapsDataKey) throws Exception{

        String sql = "DELETE FROM maps_data WHERE maps_data_key=?";
        return update(sql, mapsDataKey);
    }

    /**
     * delete map data given a list of map data key
     * @param mapDataList list of map data objects
     * @return number of rows deleted
     * @throws Exception
     */
    public int deleteMapData(List<MapData> mapDataList) throws Exception{

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "DELETE FROM maps_data WHERE maps_data_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( MapData md: mapDataList ) {
            su.update(md.getKey());
        }
        return executeBatch(su);
    }

    /**
     * return a map of chromosome sizes for given assembly map
     * @param mapKey map key
     * @return hashmap of chromosome sizes
     * @throws Exception when something really bad happens
     */
    public java.util.Map<String,Integer> getChromosomeSizes(int mapKey) throws Exception {

        final TreeMap<String,Integer> map = new TreeMap<String,Integer>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int val1 = Chromosome.getOrdinalNumber(o1);
                int val2 = Chromosome.getOrdinalNumber(o2);
                return val1-val2;
            }
        });

        String sql = "SELECT chromosome,seq_length FROM chromosomes WHERE map_key=? ORDER BY chromosome";
        JdbcTemplate jt = new JdbcTemplate(this.getDataSource());
        jt.query(sql, new Object[]{mapKey}, new int[]{Types.INTEGER}, new RowMapper() {
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                map.put(resultSet.getString(1), resultSet.getInt(2));
                return null;
            }
        });

        return map;
    }



    /**
     * return chromosomes for given assembly map
     * @param mapKey map key
     * @return list of Chromosome objects
     * @throws Exception when something really bad happens
     */
    @Deprecated
    public List<Chromosome> getChromosomes(int mapKey) throws Exception {

        String sql = "SELECT * FROM chromosomes WHERE map_key=? ORDER BY lpad( chromosome, 2 )";

        //if chinchilla or squirrel scaffolds
        if (mapKey==720 || mapKey==44) {
            sql = "SELECT * FROM chromosomes WHERE map_key=? ORDER BY chromosome";
        }

        ChromosomeQuery q = new ChromosomeQuery(this.getDataSource(), sql);
        List<Chromosome> chrList = execute(q, mapKey);

        List<Chromosome> intList = new ArrayList<Chromosome>();
        List<Chromosome> strList = new ArrayList<Chromosome>();

        for (Chromosome chr: chrList) {

            try {
                Integer.parseInt(chr.getChromosome());
                intList.add(chr);
            }catch (Exception e) {
                strList.add(chr);
            }

        }

        for (Chromosome chr: strList) {
            intList.add(chr);
        }

        return intList;

    }

    /**
     * get chromosome info for given chromosome and an assembly
     * @param mapKey map key
     * @param chr chromosome
     * @return Chromosome object or null
     * @throws Exception when something really bad happens
     */
    public Chromosome getChromosome(int mapKey, String chr) throws Exception {
        String sql = "SELECT * FROM chromosomes WHERE map_key=? AND chromosome=?";
        ChromosomeQuery q = new ChromosomeQuery(this.getDataSource(), sql);
        List<Chromosome> chromosomes = execute(q, mapKey, chr);
        if( chromosomes.isEmpty() )
            return null;
        return chromosomes.get(0);
    }

    /**
     * get chromosome info by refseq-id, f.e. 'NC_005116.3'
     * @param refSeqId refSeqId
     * @return Chromosome object or null
     * @throws Exception when something really bad happens
     */
    public Chromosome getChromosome(String refSeqId) throws Exception {
        String sql = "SELECT * FROM chromosomes WHERE refseq_id=?";
        ChromosomeQuery q = new ChromosomeQuery(this.getDataSource(), sql);
        List<Chromosome> chromosomes = execute(q, refSeqId);
        if( chromosomes.isEmpty() )
            return null;
        return chromosomes.get(0);
    }

    public void insertChromosome(Chromosome chr) throws Exception{

        String sql =
            "INSERT INTO chromosomes "+
            "(refseq_id,genbank_id,seq_length,gap_length,gap_count,contig_count," +
                    "map_key,chromosome) "+
            "VALUES(?,?,?,?,?,?,?,?)";

        upsertChromosome(chr, sql);
    }

    public void updateChromosome(Chromosome chr) throws Exception{

        String sql =
            "UPDATE chromosomes "+
            "SET refseq_id=?,genbank_id=?,seq_length=?,gap_length=?,gap_count=?,contig_count=? "+
            "WHERE map_key=? AND chromosome=?";

        upsertChromosome(chr, sql);
    }

    private void upsertChromosome(Chromosome chr, String sql) throws Exception {

        update(sql, chr.getRefseqId(), chr.getGenbankId(), chr.getSeqLength(), chr.getGapLength(),
            chr.getGapCount(), chr.getContigCount(), chr.getMapKey(), chr.getChromosome() );
    }


    /**
     * return CytoBand information for given assembly map
     * @param mapKey map key
     * @return list of CytoBand objects
     * @throws Exception when something really bad happens
     */
    @Deprecated
    public List<CytoBand> getCytoBands(int mapKey) throws Exception {

        String sql = "SELECT * FROM maps_cytobands WHERE map_key=? ORDER BY chromosome,start_pos";
        CytoBandQuery q = new CytoBandQuery(this.getDataSource(), sql);
        return execute(q, mapKey);
    }

    /**
     * get cyto band info for given chromosome and an assembly
     * @param mapKey map key
     * @param chr chromosome
     * @return CytoBand object or null
     * @throws Exception when something really bad happens
     */
    public List<CytoBand> getCytoBand(int mapKey, String chr) throws Exception {
        String sql = "SELECT * FROM maps_cytobands WHERE map_key=? AND chromosome=?";
        CytoBandQuery q = new CytoBandQuery(this.getDataSource(), sql);
        return execute(q, mapKey, chr);
    }

    /**
     * get cyto band info for given band name, chromosome and an assembly
     * @param mapKey map key
     * @param chr chromosome
     * @return CytoBand object or null
     * @throws Exception when something really bad happens
     */
    public CytoBand getCytoBand(int mapKey, String chr, String bandName) throws Exception {
        String sql = "SELECT * FROM maps_cytobands WHERE map_key=? AND chromosome=? AND band_name=?";
        CytoBandQuery q = new CytoBandQuery(this.getDataSource(), sql);
        List<CytoBand> cytoBands = execute(q, mapKey, chr, bandName);
        if( cytoBands.isEmpty() )
            return null;
        else
            return cytoBands.get(0);
    }

    public void insertCytoBand(CytoBand cb) throws Exception{

        String sql =
            "INSERT INTO maps_cytobands "+
            "(start_pos,stop_pos,giemsa_stain," +
                    "map_key,chromosome,band_name) "+
            "VALUES(?,?,?,?,?,?)";

        upsertCytoBand(cb, sql);
    }

    public void updateCytoBand(CytoBand cb) throws Exception{

        String sql =
            "UPDATE maps_cytobands "+
            "SET start_pos=?,stop_pos=?,giemsa_stain=? "+
            "WHERE map_key=? AND chromosome=? AND band_name=?";

        upsertCytoBand(cb, sql);
    }

    private void upsertCytoBand(CytoBand cb, String sql) throws Exception {

        update(sql, cb.getStartPos(), cb.getStopPos(), cb.getGiemsaStain(),
                cb.getMapKey(), cb.getChromosome(), cb.getBandName() );
    }


////////////////////////
/// DB_SNP positions ///
////////////////////////
    /**
     * get positions of given DB_SNP for given assembly
     * @param rsId DB_SNP rsId, like 'rs8143345'
     * @param mapKey map key
     * @return list of map positions for this DB_SNP on the specified assembly (chromosome and position)
     * @throws Exception when something really bad happens
     */
    public List<IntStringMapQuery.MapPair> getDbSnpPositions(String rsId, int mapKey) throws Exception {

        String query = "SELECT DISTINCT d.position,d.chromosome FROM db_snp d,maps m " +
            "WHERE d.snp_name=? AND m.map_key=d.map_key AND m.dbsnp_version=d.source AND m.map_key=?";

        return IntStringMapQuery.execute(this, query, rsId, mapKey);
    }


    /// to differentiate between ours and the framework's exceptions
    public class MapDAOException extends Exception {

        public MapDAOException(String msg) {
            super(msg);
        }
    }

    /// Map query implementation helper
    public List<Map> executeMapQuery(String query, Object ... params) throws Exception {
        MapQuery q = new MapQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    /// MapData query implementation helper
    public List<MapData> executeMapDataQuery(String query, Object ... params) throws Exception {
        MapDataQuery q = new MapDataQuery(this.getDataSource(), query);
        return execute(q, params);
    }
}
