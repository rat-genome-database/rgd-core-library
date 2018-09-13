package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneQuery;
import edu.mcw.rgd.dao.spring.OrthologQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.Ortholog;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.MappingSqlQuery;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Represents gene orthologs
 */
public class OrthologDAO extends AbstractDAO {

    /**
     * get ortholog given its unique key
     *
     * @param key ortholog unique key (GENETOGENE_KEY)
     * @return Ortholog object or null if key is invalid
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Ortholog getOrthologByKey(int key) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.genetogene_key=? ";

        List<Ortholog> list = executeOrthologQuery(sql, key);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * get all active gene orthologs for given species
     *
     * @param speciesTypeKey species type key
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getAllOrthologs(int speciesTypeKey) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND s.species_type_key=? \n" +
                "AND s.object_status='ACTIVE' AND d.object_status='ACTIVE'";

        return executeOrthologQuery(sql, speciesTypeKey);
    }

    /**
     * get all active gene orthologs for given pair of species
     *
     * @param speciesTypeKey1 species type key for first species
     * @param speciesTypeKey2 species type key for second species
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getAllOrthologs(int speciesTypeKey1, int speciesTypeKey2) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND s.object_status='ACTIVE' AND s.species_type_key=? \n" +
                "  AND o.dest_rgd_id=d.rgd_id AND d.object_status='ACTIVE' AND d.species_type_key=?";

        return executeOrthologQuery(sql, speciesTypeKey1, speciesTypeKey2);
    }

    /**
     * get ortholog count for given pair of species
     *
     * @param speciesTypeKey1 species type key for first species
     * @param speciesTypeKey2 species type key for second species
     * @return count of orthologs
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getOrthologCount(int speciesTypeKey1, int speciesTypeKey2) throws Exception {

        String sql = "SELECT COUNT(*) \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND s.object_status='ACTIVE' AND s.species_type_key=? \n" +
                "  AND o.dest_rgd_id=d.rgd_id AND d.object_status='ACTIVE' AND d.species_type_key=?";

        return getCount(sql, speciesTypeKey1, speciesTypeKey2);
    }

    /**
     * get all orthologs for given source
     *
     * @param source          ortholog source (XREF_DATA_SRC)
     * @param includeInactive include orthologs associated with non ACTIVE genes
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologs(String source, boolean includeInactive) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.xref_data_src=? \n";
        if (!includeInactive)
            sql += "AND s.object_status='ACTIVE' AND d.object_status='ACTIVE'";

        return executeOrthologQuery(sql, source);
    }

    /**
     * get gene orthologs for given source rgd id (exclude non-active genes)
     *
     * @param rgdId rgd id
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsForSourceRgdId(int rgdId) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.src_rgd_id=? AND s.object_status='ACTIVE' AND d.object_status='ACTIVE'";

        return executeOrthologQuery(sql, rgdId);
    }

    /**
     * get gene orthologs for given source rgd id and dest rgd id(exclude non-active genes)
     *
     * @param srcRgdId  source rgd id
     * @param destRgdId destination rgd id
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologs(int srcRgdId, int destRgdId) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.src_rgd_id=? AND o.dest_rgd_id=? " +
                "AND s.object_status='ACTIVE' AND d.object_status='ACTIVE'";

        return executeOrthologQuery(sql, srcRgdId, destRgdId);
    }

    /**
     * get gene orthologs for given destination rgd id
     *
     * @param rgdId rgd id
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsForDestRgdId(int rgdId) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.dest_rgd_id=? AND s.object_status='ACTIVE' AND d.object_status='ACTIVE'";

        return executeOrthologQuery(sql, rgdId);
    }

    /**
     * get gene orthologs for given destination rgd id
     *
     * @param rgdId rgd id
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsForDestRgdId(int rgdId, int speciesTypeKey) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key " +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.dest_rgd_id=? AND s.object_status='ACTIVE' AND d.object_status='ACTIVE' and s.species_type_key=" + speciesTypeKey;

        return executeOrthologQuery(sql, rgdId);
    }


    /**
     * get all active genes without any orthologs for given species
     * Note: splices and alleles are excluded from the list
     *
     * @param speciesTypeKey species type key
     * @return List of Gene objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGenesWithoutOrthologs(int speciesTypeKey) throws Exception {

        String sql = "SELECT g.*,r.species_type_key FROM genes g,rgd_ids r \n" +
                "WHERE g.rgd_id=r.rgd_id AND object_status='ACTIVE' AND r.species_type_key=? \n" +
                "AND NOT EXISTS(SELECT 1 FROM genetogene_rgd_id_rlt WHERE src_rgd_id=r.rgd_id)";

        GeneQuery q = new GeneQuery(this.getDataSource(), sql);
        return execute(q, speciesTypeKey);
    }

    /**
     * get all orthologs that are have same associations for two different XREF_DATA_SRC
     *
     * @return List of Ortholog objects: all orthologs from source 1 (XREF_DATA_SRC=srefDataSrc1)
     * that have corresponding orthologs from source 2 (XREF_DATA_SRC=srefDataSrc2)
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getDuplicateOrthologs(String xrefDataSrc1, String xrefDataSrc2) throws Exception {

        String sql = "SELECT o1.* \n" +
                "FROM genetogene_rgd_id_rlt o1,genetogene_rgd_id_rlt o2 \n" +
                "WHERE o1.src_rgd_id=o2.src_rgd_id \n" +
                "AND o1.dest_rgd_id=o2.dest_rgd_id \n" +
                "AND o1.xref_data_src=? \n" +
                "AND o2.xref_data_src=?";

        return executeOrthologQuery(sql, xrefDataSrc1, xrefDataSrc2);
    }

    /**
     * get gene orthologs for given ortholog group id
     *
     * @param groupId group id
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsForGroupId(int groupId) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.group_id=?";

        return executeOrthologQuery(sql, groupId);
    }

    /**
     * get gene orthologs modified before given date
     *
     * @param modifiedDate ortholog modification date
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsModifiedBefore(Date modifiedDate) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.last_modified_date<?";

        return executeOrthologQuery(sql, modifiedDate);
    }

    /**
     * get gene orthologs for given pair of species that were modified before given date
     *
     * @param modifiedDate    ortholog modification date
     * @param speciesTypeKey1 species type key1
     * @param speciesTypeKey2 species type key2
     * @return List of Ortholog objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Ortholog> getOrthologsModifiedBefore(Date modifiedDate, int speciesTypeKey1, int speciesTypeKey2) throws Exception {

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key \n" +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d \n" +
                "WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND o.last_modified_date<?\n" +
                " AND( (s.species_type_key=? AND d.species_type_key=?) OR (s.species_type_key=? AND d.species_type_key=?) )";

        return executeOrthologQuery(sql, modifiedDate, speciesTypeKey1, speciesTypeKey2, speciesTypeKey2, speciesTypeKey1);
    }

    /**
     * check if two genes are orthologous
     *
     * @param srcRgdId  rgd id if first gene
     * @param destRgdId rgd id if second gene
     * @return lowest value of genetogene_key if there is an orthology between two genes; 0 otherwise
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int areGenesOrthologous(int srcRgdId, int destRgdId) throws Exception {

        String sql = "SELECT MIN(genetogene_key) \n" +
                "FROM genetogene_rgd_id_rlt o \n" +
                "WHERE o.src_rgd_id=? AND o.dest_rgd_id=?";

        return getCount(sql, srcRgdId, destRgdId);
    }

    /**
     * insert a list of orthologs; Note: ortholog genetogene_key will be taken from sequence GENETOGENE_RGD_ID_RLT_SEQ
     *
     * @param orthologs list of orthologs to insert
     * @return count of rows inserted
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertOrthologs(List<Ortholog> orthologs) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO genetogene_rgd_id_rlt " +
                        "(genetogene_key, src_rgd_id, dest_rgd_id, group_id, xref_data_src, xref_data_set, ortholog_type_key, " +
                        " percent_homology, ref_key, created_by, created_date, last_modified_by, last_modified_date) " +
                        "VALUES(?,?,?,?,?,?,?, ?,?,?,?,?,?)",
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
                        Types.FLOAT, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.INTEGER, Types.TIMESTAMP});

        su.compile();

        for (Ortholog o : orthologs) {

            o.setKey(this.getNextKeyFromSequence("GENETOGENE_RGD_ID_RLT_SEQ"));

            su.update(
                    o.getKey(), o.getSrcRgdId(), o.getDestRgdId(), o.getGroupId(),
                    o.getXrefDataSrc(), o.getXrefDataSet(), o.getOrthologTypeKey(),
                    o.getPercentHomology(), o.getRefKey(), o.getCreatedBy(), o.getCreatedDate(),
                    o.getLastModifiedBy(), o.getLastModifiedDate()
            );
        }

        return executeBatch(su);
    }

    /**
     * delete a list of orthologs; Note: ortholog key will be used
     *
     * @param orthologs list of orthologs to  delete
     * @return count of rows deleted
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteOrthologs(List<Ortholog> orthologs) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "DELETE FROM genetogene_rgd_id_rlt " +
                        "WHERE genetogene_key=?",
                new int[]{Types.INTEGER});

        su.compile();

        for (Ortholog o : orthologs) {

            su.update(new Object[]{o.getKey()});
        }

        return executeBatch(su);
    }

    /**
     * update last modified date and user (last_modified_by) for a list of orthologs
     *
     * @param orthologs list of orthologs to be updated
     * @return count of rows updated
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateLastModified(List<Ortholog> orthologs, int lastModifiedBy) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "UPDATE genetogene_rgd_id_rlt " +
                        "SET last_modified_by=?, last_modified_date=SYSTIMESTAMP " +
                        "WHERE genetogene_key=?",
                new int[]{Types.INTEGER, Types.INTEGER});

        su.compile();

        for (Ortholog o : orthologs) {

            su.update(new Object[]{lastModifiedBy, o.getKey()});
        }

        return executeBatch(su);
    }

    /**
     * get ortholog type keys mapped to ortholog type names
     *
     * @return map of ortholog type names keyed by ortholog type keys
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Map<Integer, String> getOrthologTypes() throws Exception {


        final Map<Integer, String> map = new HashMap<>();

        String sql = "SELECT ortholog_type_key,ortholog_type FROM ortholog_types";
        MappingSqlQuery q = new MappingSqlQuery(this.getDataSource(), sql) {
            protected Object mapRow(ResultSet rs, int i) throws SQLException {
                map.put(rs.getInt("ortholog_type_key"), rs.getString("ortholog_type"));
                return null;
            }
        };
        q.compile();
        q.execute();
        return map;
    }

    /**
     * get ortholog type name given ortholog type key
     *
     * @param orthologTypeKey ortholog type key
     * @return ortholog type name or null if ortholog type key is invalid
     * @throws Exception when unexpected error in spring framework occurs
     */
    public String getOrthologTypeName(int orthologTypeKey) throws Exception {

        String sql = "SELECT ortholog_type FROM ortholog_types WHERE ortholog_type_key=?";
        List<String> list = StringListQuery.execute(this, sql, orthologTypeKey);
        return list.isEmpty() ? null : list.get(0);
    }

    /// Ortholog query implementation helper
    public List<Ortholog> executeOrthologQuery(String query, Object... params) throws Exception {
        OrthologQuery q = new OrthologQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    public Map<String, Integer> getOrthologCounts(int mapKey, int speciesTypeKey, String chr) throws Exception {
       /* String sql="SELECT dest.species_type_key, count(dest.species_type_key) " +
                "from genetogene_rgd_id_rlt rlt, rgd_ids src, rgd_ids dest, maps_data m " +
                " where dest.rgd_id=dest_rgd_id and src.rgd_id=src_rgd_id " +
                "and m.rgd_id=src.rgd_id and m.map_key=?" +
                " and src.species_type_key=? and src.object_status='ACTIVE' and dest.object_status='ACTIVE' ";*/


        String sql = "SELECT d.species_type_key, COUNT(d.species_type_key) " +
                "FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d " +
                "WHERE o.src_rgd_id=s.rgd_id " +
                "AND o.dest_rgd_id=d.rgd_id " +
                "AND s.species_type_key=? " +
                "AND s.object_status='ACTIVE' AND d.object_status='ACTIVE' " +
                "AND EXISTS( SELECT 1 FROM maps_data m WHERE s.rgd_id=m.rgd_id AND m.map_key=? ";

        if (chr != null) {
            sql = sql + " and m.chromosome=?";
        }
        sql = sql + ") group by d.species_type_key";
        Map<String, Integer> counts = new HashMap<>();
        try (Connection conn = this.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(2, mapKey);
            stmt.setInt(1, speciesTypeKey);
            if (chr != null) {
                stmt.setString(3, chr);
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sKey = rs.getString("species_type_key");
                int count = rs.getInt("count(d.species_type_key)");

                counts.put(sKey, count);
                //    System.out.println(sKey+" || "+ count);
            }

        }

        return counts;
    }

    public int getGeneCountWithOrthologs(int mapKey, int speciesTypeKey, String chr) throws Exception {
        String sql = "SELECT count(distinct(r.rgd_id)) as count FROM genes g,rgd_ids r, maps_data m WHERE g.rgd_id=r.rgd_id AND m.rgd_id=g.rgd_id and m.map_key=? AND object_status='ACTIVE' AND r.species_type_key=? AND EXISTS(SELECT 1 FROM genetogene_rgd_id_rlt WHERE src_rgd_id=r.rgd_id) ";
        if (chr != null) {
            sql = sql + "and m.chromosome=?";
        }
        if (chr != null) {
            return getCount(sql, mapKey, speciesTypeKey, chr);
        } else
            return getCount(sql, mapKey, speciesTypeKey);


    }

    public int getGeneCountWithOutOrthologs(int mapKey, int speciesTypeKey, String chr) throws Exception {
        String sql = "SELECT count(distinct(r.rgd_id)) as count FROM genes g,rgd_ids r, maps_data m WHERE g.rgd_id=r.rgd_id AND m.rgd_id=g.rgd_id and m.map_key=? AND object_status='ACTIVE' AND r.species_type_key=? AND NOT EXISTS(SELECT 1 FROM genetogene_rgd_id_rlt WHERE src_rgd_id=r.rgd_id) ";
        if (chr != null) {
            sql = sql + "and m.chromosome=?";
        }
        if (chr != null) {
            return getCount(sql, mapKey, speciesTypeKey, chr);
        } else {
            return getCount(sql, mapKey, speciesTypeKey);
        }

    }

    public static void main(String[] args) throws Exception {
        OrthologDAO dao = new OrthologDAO();
        dao.getOrthologCounts(631, 6, "1");
    }


    public HashMap<Integer, HashMap<Integer, List<Integer>>> getOrthologMapping(String accId, List<Integer> speciesTypeKeys, List<String> evidenceCodes) throws Exception {

        String speciesInClause = "(";
        boolean first = true;
        for (Integer species : speciesTypeKeys) {
            if (first) {
                speciesInClause += species;
                first = false;
            } else {
                speciesInClause += "," + species;
            }
        }
        speciesInClause += ")";

        String evidenceInClause = "(";
        first = true;
        for (String evidence : evidenceCodes) {
            if (first) {
                evidenceInClause += "'" + evidence + "'";
                first = false;
            } else {
                evidenceInClause += ",'" + evidence + "'";
            }
        }
        evidenceInClause += ")";

        String sql = "SELECT o.*,s.species_type_key src_species_type_key,d.species_type_key dest_species_type_key";
        sql += " FROM genetogene_rgd_id_rlt o, rgd_ids s,rgd_ids d " +
                " WHERE o.src_rgd_id=s.rgd_id AND o.dest_rgd_id=d.rgd_id AND s.object_status='ACTIVE' AND d.object_status='ACTIVE' " +
                " and s.species_type_key in " + speciesInClause + " AND o.dest_rgd_id in ( " +
                " SELECT a.annotated_object_rgd_id FROM full_annot a,rgd_ids r WHERE term_acc='" + accId + "' AND annotated_object_rgd_id=r.rgd_id " +
                " AND r.species_type_key in " + speciesInClause +
                " AND object_status='ACTIVE' and rgd_object_key=1 and evidence in " + evidenceInClause + ")";


        System.out.println(sql);
        Connection conn = this.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        HashMap<Integer, HashMap<Integer, List<Integer>>> mapping = new HashMap<Integer, HashMap<Integer, List<Integer>>>();

        while (rs.next()) {
            HashMap<Integer, List<Integer>> orthoMap = null;
            List<Integer> orthoList = null;

            //see if mapping already exists
            int rgdId = rs.getInt("dest_rgd_id");
            int orthoId = rs.getInt("src_rgd_id");
            int orthoSpecies = rs.getInt("src_species_type_key");

            if (mapping.containsKey(rgdId)) {
                orthoMap = mapping.get(rgdId);

                if (orthoMap.containsKey(orthoSpecies)) {
                    orthoList = orthoMap.get(orthoSpecies);
                } else {
                    orthoList = new ArrayList<Integer>();
                }

            } else {
                orthoMap = new HashMap<Integer, List<Integer>>();
                orthoList = new ArrayList<Integer>();
            }

            orthoList.add(orthoId);

            orthoMap.put(orthoSpecies, orthoList);
            mapping.put(rgdId, orthoMap);

        }

        conn.close();

        return mapping;
    }

}
