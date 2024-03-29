package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.AliasQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jdepons
 * @since May 19, 2008
 */
public class AliasDAO extends AbstractDAO {

    public Alias getAliasByKey(int key) throws Exception {
        String query = "SELECT a.*, r.species_type_key FROM aliases a, rgd_ids r WHERE r.rgd_id=a.rgd_id AND a.alias_key=?";
        List<Alias> aliases = AliasQuery.execute(this, query, key);

        if (aliases.size() == 0) {
            throw new AliasDAOException("Alias " + key + " not found");
        }
        return aliases.get(0);
    }

    /**
     * get list of aliases for given RGD_ID
     * @param rgdId RGD_ID
     * @return list of aliases associated with given RGD_ID
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getAliases(int rgdId) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM aliases a, rgd_ids r WHERE a.rgd_id=? AND a.rgd_id=r.rgd_id";
        return AliasQuery.execute(this, query, rgdId);
    }

    public List<Alias> getAliases(List<Integer> rgdIdsList) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM aliases a, rgd_ids r WHERE a.rgd_id in ("+
                Utils.buildInPhrase(rgdIdsList) + ") AND a.rgd_id=r.rgd_id";
        return AliasQuery.execute(this, query);
    }

    /**
     * get list of aliases of given type for given RGD_ID
     * @param rgdId RGD_ID
     * @param aliasType alias type
     * @return list of aliases of given type associated with given RGD_ID
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getAliases(int rgdId, String aliasType) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM aliases a, rgd_ids r WHERE a.rgd_id=? AND a.rgd_id=r.rgd_id AND a.alias_type_name_lc=LOWER(?)";
        return AliasQuery.execute(this, query, rgdId, aliasType);
    }

    /**
     * get list of aliases of given types for given RGD_ID
     * @param rgdId RGD_ID
     * @param aliasTypes alias types
     * @return list of aliases of given types associated with given RGD_ID
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getAliases(int rgdId, String[] aliasTypes) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM aliases a, rgd_ids r WHERE a.rgd_id=? AND a.rgd_id=r.rgd_id "+
                "AND a.alias_type_name_lc IN("+Utils.concatenate(aliasTypes,",","'")+')';
        return AliasQuery.execute(this, query, rgdId);
    }

    /**
     * get list of aliases of given type
     * @param aliasType alias type
     * @return list of aliases of given type
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getAliasesByType(String aliasType) throws Exception {
        String query = "SELECT a.*,r.species_type_key FROM aliases a, rgd_ids r WHERE a.rgd_id=r.rgd_id AND a.alias_type_name_lc=LOWER(?)";
        return AliasQuery.execute(this, query, aliasType);
    }

    /**
     * get list of all active aliases for given object type (exclude zebrafish aliases)
     * @param objType object type; one of RgdId.OBJECT_KEY constants
     * @return list of Alias objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getActiveAliases(int objType) throws Exception {

        String query = "SELECT a.*,i.species_type_key "+
                "FROM aliases a, rgd_ids i " +
                "WHERE a.rgd_id=i.rgd_id AND i.object_status='ACTIVE' AND i.object_key=? "+
                "AND i.species_type_key<>8";
        return AliasQuery.execute(this, query, objType);
    }

    /**
     * get list of all active array id aliases from Ensembl for given object type and species
     * @param objType object type; one of RgdId.OBJECT_KEY constants
     * @param speciesTypeKey species type key
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getActiveArrayIdAliasesFromEnsembl(int objType, int speciesTypeKey) throws Exception {
        String query  = "SELECT a.*,r.species_type_key FROM aliases a,rgd_ids r\n" +
                "WHERE a.rgd_id=r.rgd_id AND object_status='ACTIVE' AND object_key=? AND species_type_key=?\n" +
                "  AND alias_type_name_lc LIKE 'array%ensembl'";
        return AliasQuery.execute(this, query, objType, speciesTypeKey);
    }

    /**
     * Update alias in the data store based on alias key
     *
     * @param alias Alias object to be updated
     * @throws Exception if something wrong happens in spring framework
     * @return count of rows affected
     */
    public int updateAlias(Alias alias) throws Exception{

        String sql = "UPDATE aliases SET rgd_id=?, alias_type_name_lc=LOWER(?), alias_value=?, " +
                "alias_value_lc=LOWER(?), notes=? WHERE alias_key=?";

        return update(sql, alias.getRgdId(), alias.getTypeName(), alias.getValue(), alias.getValue(), alias.getNotes(), alias.getKey());
    }

    /**
     * insert new alias
     * @param alias Alias object to be inserted
     * @throws Exception if something wrong happens in spring framework
     * @return count of rows affected
     */
    public int insertAlias(Alias alias) throws Exception{

        List<Alias> aliases = new ArrayList<>(1);
        aliases.add(alias);
        return insertAliases(aliases);
    }

    /**
     * insert alias list into ALIASES table
     * @param aliases list of aliases to be inserted
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int insertAliases(List<Alias> aliases) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO aliases (alias_key, alias_type_name_lc, alias_value, alias_value_lc, notes, rgd_id) " +
                "SELECT ?,LOWER(?),?,LOWER(?),?,? FROM dual "+
                "WHERE NOT EXISTS (SELECT 1 FROM aliases "+
                        " WHERE rgd_id=? AND alias_value=? AND alias_type_name_lc=LOWER(?))",
                new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
                    Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
        su.compile();

        int aliasKey = this.getNextKey("aliases","alias_key");
        for( Alias alias: aliases ) {
            alias.setKey(aliasKey++);

            su.update(alias.getKey(), alias.getTypeName(), alias.getValue(), alias.getValue(), alias.getNotes(),
                alias.getRgdId(), alias.getRgdId(), alias.getValue(), alias.getTypeName());
        }

        return executeBatch(su);
    }

    /**
     * delete an alias given alias key
     * @param aliasKey alias key
     * @throws Exception if something wrong happens in spring framework
     * @return count of rows affected
     */
    public int deleteAlias(int aliasKey) throws Exception{

        String sql = "DELETE FROM aliases WHERE alias_key=?";
        return update(sql, aliasKey);
    }

    /**
     * delete all aliases for given rgd id
     * @param rgdid rgd id
     * @throws Exception if something wrong happens in spring framework
     * @return number of deleted aliases
     */
    public int deleteAliases(int rgdid) throws Exception{

        String sql = "DELETE FROM aliases WHERE rgd_id=?";
        return update(sql, rgdid);
    }

    /**
     * delete all aliases for given alias type name
     * @param aliasTypeName alias type name
     * @throws Exception if something wrong happens in spring framework
     * @return number of deleted aliases
     */
    public int deleteAliases(String aliasTypeName) throws Exception{

        String sql = "DELETE FROM aliases WHERE alias_type_name_lc=LOWER(?)";
        return update(sql, aliasTypeName);
    }

    /**
     * delete a list of aliases; note: all aliases must have set ALIAS_KEY
     * @param aliases list of Alias objects
     * @throws Exception if something wrong happens in spring framework
     * @return number of deleted aliases
     */
    public int deleteAliases(List<Alias> aliases) throws Exception{

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "DELETE FROM aliases WHERE alias_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( Alias alias: aliases ) {
            su.update(alias.getKey());
        }

        return executeBatch(su);
    }

    /**
     * get aliases that are the same as a gene name or gene symbol
     * @return List of Alias objects
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Alias> getRedundantGeneAliases() throws Exception {
        String sql = "SELECT a.*,i.species_type_key FROM aliases a,rgd_ids i " +
                "WHERE EXISTS(SELECT 1 FROM genes g WHERE a.rgd_id=g.rgd_id AND (alias_value_lc=gene_symbol_lc OR alias_value_lc=full_name_lc))" +
                " AND i.rgd_id=a.rgd_id";
        return AliasQuery.execute(this, sql);
    }

    /**
     * get alias for given rgd object and alias value
     * @param rgdId rgd id
     * @param aliasValue alias value
     * @return Alias object
     * @throws Exception if something wrong happens in spring framework
     */
    public Alias getAliasByValue(int rgdId, String aliasValue) throws Exception{
        String query = "SELECT * FROM aliases WHERE rgd_id=? AND alias_value_lc=LOWER(?)";
        List<Alias> aliases = AliasQuery.execute(this, query, rgdId, aliasValue);
        if(aliases.isEmpty()){
            return null;
        }else{
            return aliases.get(0);
        }
    }

    public Alias getAliasByValue(String aliasValue) throws Exception{
        String query = "SELECT * FROM aliases WHERE alias_value_lc=LOWER(?)";
        List<Alias> aliases = AliasQuery.execute(this, query, aliasValue);
        if(aliases.isEmpty()){
            return null;
        }else{
            return aliases.get(0);
        }
    }



    /**
     * delete all aliases for given rgd object and a list of values
     * @param rgdId rgd id
     * @param valueList list of values
     * @throws Exception if something wrong happens in spring framework
     */
    public void deleteAlias(int rgdId, List valueList) throws Exception{

        if (valueList == null || valueList.size() < 1) {
            return; // nothing to do
        }

        String query = "delete from aliases where rgd_id = " + rgdId;
        Iterator it = valueList.iterator();

        int count=0;
        while (it.hasNext()) {
            if (count==0) {
                query = query + " and (";
            }else {
                query = query + " or ";
            }

            String value = (String) it.next();
            query = query + " alias_value_lc = '" + value.toLowerCase().replaceAll("'","''") + "'";
            count++;
        }
        if (count > 0) {
            query = query + ")";
        }

        update(query);
    }


    /**
     * Moves all aliases from one rgd id to another.
     *
     * @param fromRGDId old rgd id
     * @param toRGDId new rgd id
     * @throws Exception if something wrong happens in spring framework
     */
    public void reassignAliases(int fromRGDId, int toRGDId) throws Exception {
        String sql = "UPDATE aliases SET rgd_id=? WHERE rgd_id=?";
        update(sql, toRGDId, fromRGDId);
    }

    /**
     * get list of alias types defined in the database
     * @return list of available alias types
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getAliasTypes() throws Exception {

        String query = "SELECT alias_type_name_lc FROM alias_types";
        return StringListQuery.execute(this, query);
    }

    /**
     * get list of alias types defined in the database, excluding types related to array ids
     * @return list of available alias types
     * @throws Exception if something wrong happens in spring framework
     */
    public List<String> getAliasTypesExcludingArrayIds() throws Exception {

        String query = "SELECT alias_type_name_lc FROM alias_types WHERE alias_type_name_lc NOT LIKE 'array_id%'";
        return StringListQuery.execute(this, query);
    }

    /**
     * insert a new alias type
     * Note: alias type is always converted to lowercase before insert
     *
     * @param aliasType alias type to be inserted
     * @param notes notes about alias type to be inserted
     * @throws Exception if something wrong happens in spring framework
     * @return count of rows affected
     */
    public int insertAliasType(String aliasType, String notes) throws Exception{

        String sql = "INSERT INTO alias_types (alias_type_name_lc,notes) VALUES(LOWER(?),?)";
        return update(sql, aliasType, notes);
    }

    /**
     * AliasDAOException should be thrown by AliasDAO methods to differentiate between ours and the framework's exceptions
     */
    public class AliasDAOException extends Exception {

        public AliasDAOException(String msg) {
            super(msg);
        }
    }
}