package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StrainQuery;
import edu.mcw.rgd.dao.spring.StrainStatusQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Strain;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.SqlUpdate;

import java.util.List;
import java.io.InputStream;
import java.sql.*;

/**
 * DAO code to query and update STRAINS table
 */
public class StrainDAO extends AbstractDAO {

    /**
     * get list of all active strains, for any species
     * @return list of Strain objects
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> getActiveStrains() throws Exception {
        String query = "select s.*, r.SPECIES_TYPE_KEY from strains s, RGD_IDS r " +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID";
        return executeStrainQuery(query);
    }

    /**
     * get strains associated with given rgd object
     * @param associatedObjectRgdId rgd id of associated object
     * @return List of Strain objects associated with another rgd object
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> getAssociatedStrains(int associatedObjectRgdId) throws Exception {
        String query = "select s.*, ri.species_type_key from rgd_strains_rgd rsr, strains s, rgd_ids ri\n" +
                "where rsr.rgd_id=? and rsr.strain_key=s.strain_key and s.rgd_id = ri.rgd_id";
        return executeStrainQuery(query, associatedObjectRgdId);
    }

    /**
     * get list of strains of given type
     * @param strainType one of strain types
     * @return List of Strain objects belonging to given type
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> getStrainsByType(String strainType) throws Exception {

        String query = "select s.*, ri.SPECIES_TYPE_KEY from STRAINS s, RGD_IDS ri\n" +
                "where strain_type_name_lc=? and s.rgd_id = ri.rgd_id";
        return executeStrainQuery(query, strainType.toLowerCase());

    }

    public List<Strain> getActiveStrainsSortedBySymbol(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "SELECT g.*, r.species_type_key \n" +
                "FROM strains g, rgd_ids r, maps_data md \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id \n"+
                " AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=? " +
                " order by g.strain_symbol";

        return executeStrainQuery(query, chr, stopPos, startPos, mapKey);
    }

    /**
     * get list of strains having given substring in the strain symbol
     * @param strainSymbol one of strain symbols
     * @return List of Strain objects belonging to given type
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> getSubStrains(String strainSymbol) throws Exception {

        String query = "select s.*, ri.SPECIES_TYPE_KEY from STRAINS s, RGD_IDS ri\n" +
                "where strain_symbol_lc like '" + strainSymbol.toLowerCase() + "/%' and s.rgd_id = ri.rgd_id and ri.object_status='ACTIVE'";
        return executeStrainQuery(query);
    }



    /**
     * get list of active strains for those strains with chromosome positions on a given assembly
     * @param mapKey map key
     * @return List of Strain Objects
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> getMappedStrains(int mapKey) throws Exception{

        String query = "SELECT s.*,r.species_type_key \n" +
                "FROM strains s, rgd_ids r \n" +
                "WHERE EXISTS (\n" +
                "    SELECT 1 FROM maps_data m WHERE m.rgd_id=s.rgd_id AND m.map_key=?\n" +
                ") \n" +
                "AND s.rgd_id=r.rgd_id AND r.object_status='ACTIVE'";
        return executeStrainQuery(query, mapKey);
    }


    /**
     * get strain by strain rgd id
     * @param rgdId strain rgd id
     * @return Strain object
     * @throws Exception when strain rgd id is invalid
     * @deprecated Exceptions should not be used as substitute for normal flow logic; NULL should be returned instead
     */
    public Strain getStrain(int rgdId) throws Exception {
        String query = "SELECT s.*,r.species_type_key FROM strains s, rgd_ids r where r.RGD_ID=s.RGD_ID and r.RGD_ID=?";
        List<Strain> strains = executeStrainQuery(query, rgdId);
        if (strains.size() == 0) {
            throw new StrainDAOException("Strain RGD:" + rgdId + " not found");
        }
        return strains.get(0);
    }

    /**
     * get strains by list of rgd ids
     * @param rgdIdsList
     * @return
     * @throws Exception
     */
    public List<Strain> getStrains(List<Integer> rgdIdsList) throws Exception {
        String query = "SELECT s.*,r.species_type_key FROM strains s, rgd_ids r "+
                "where r.RGD_ID=s.RGD_ID "+
                "and r.RGD_ID in ("+Utils.buildInPhrase(rgdIdsList)+")";
        return executeStrainQuery(query);
    }

    /**
     * get strain by strain key
     * @param strainKey strain key                    
     * @return Strain object
     * @throws Exception when something really bad happens in spring framework
     */
    public Strain getStrainByKey(int strainKey) throws Exception {
        String query = "SELECT s.*,r.species_type_key FROM strains s, rgd_ids r WHERE r.rgd_id=s.rgd_id AND s.strain_key=?";
        List<Strain> strains = executeStrainQuery(query, strainKey);
        return strains.size()==0 ? null : strains.get(0);
   }

    /**
     * get strain by strain symbol
     * @param strSymbol strain symbol
     * @return Strain object if match found, or null if no strain with such a symbol
     * @throws Exception when something really bad happens in spring framework
     */
    public Strain getStrainBySymbol(String strSymbol) throws Exception {
        String query = "SELECT s.*, r.species_type_key FROM strains s, rgd_ids r WHERE r.rgd_id=s.rgd_id AND " +
                "r.object_status='ACTIVE' AND s.strain_symbol_lc=?";
        List<Strain> strains = executeStrainQuery(query, strSymbol.toLowerCase());
        return strains.size()==0 ? null : strains.get(0);
    }

    /**
     * get list of active strains given rgd id is a marker to
     * @param rgdId rgd id
     * @return list of strains, possibly empty
     * @throws Exception when something really bad happens in spring framework
     */
    public List<Strain> isMarkerFor(int rgdId) throws Exception {

        String sql = "SELECT s.*, r.species_type_key FROM strains s, rgd_ids r, rgd_strains_rgd a "+
                "WHERE r.rgd_id=s.rgd_id AND r.object_status='ACTIVE' "+
                "AND a.rgd_id=? AND a.strain_key=s.strain_key";
        return executeStrainQuery(sql, rgdId);
    }

    /**
     * Update strain in the datastore based on rgdID
     *
     * @param strain Strain object
     * @throws Exception
     */
    public void updateStrain(Strain strain) throws Exception{

        String sql = "UPDATE strains SET strain_key=?, strain_symbol=?, strain_symbol_lc=LOWER(?), " +
                "full_name=?, full_name_lc=LOWER(?), strain=?, strain_lc=LOWER(?), substrain=?, substrain_lc=LOWER(?), "+
                "GENETICS=?,  INBRED_GEN=?, ORIGIN=?, COLOR=?, CHR_ALTERED=?, SOURCE=?, NOTES=?, strain_type_name_lc=LOWER(?), "+
                "IMAGE_URL=?, RESEARCH_USE=?, genetic_status=?, background_strain_rgd_id=?, "+
                "modification_method=? WHERE rgd_id=?";

        update(sql, strain.getKey(), strain.getSymbol(), strain.getSymbol(), strain.getName(), strain.getName(),
                strain.getStrain(), strain.getStrain(), strain.getSubstrain(), strain.getSubstrain(), strain.getGenetics(),
                strain.getInbredGen(), strain.getOrigin(), strain.getColor(), strain.getChrAltered(), strain.getSource(),
                strain.getNotes(), strain.getStrainTypeName(), strain.getImageUrl(), strain.getResearchUse(),
                strain.getGeneticStatus(), strain.getBackgroundStrainRgdId(), strain.getModificationMethod(),
                strain.getRgdId());
    }

    /**
     * insert new strain object
     *
     * @param strain Strain object
     * @throws Exception
     */
    public void insertStrain(Strain strain) throws Exception{

        String sql = "INSERT INTO strains (strain_key, strain_symbol, strain_symbol_lc, " +
                "FULL_NAME, FULL_NAME_LC, STRAIN, STRAIN_LC, SUBSTRAIN, SUBSTRAIN_LC, GENETICS, INBRED_GEN, " +
                "ORIGIN, COLOR, CHR_ALTERED, SOURCE, NOTES, STRAIN_TYPE_NAME_LC, image_url, research_use, "+
                "genetic_status, background_strain_rgd_id, modification_method, rgd_id) " +
                "VALUES (?,?,LOWER(?), ?,LOWER(?),?,LOWER(?),?,LOWER(?),?,?, ?,?,?,?,?,LOWER(?),?,?, ?,?,?,?)";

        update(sql, this.getNextKey("STRAINS","STRAIN_KEY"), strain.getSymbol(), strain.getSymbol(),
                strain.getName(), strain.getName(), strain.getStrain(), strain.getStrain(),
                strain.getSubstrain(), strain.getSubstrain(), strain.getGenetics(), strain.getInbredGen(),
                strain.getOrigin(), strain.getColor(), strain.getChrAltered(), strain.getSource(), strain.getNotes(),
                strain.getStrainTypeName(), strain.getImageUrl(), strain.getResearchUse(), strain.getGeneticStatus(),
                strain.getBackgroundStrainRgdId(), strain.getModificationMethod(), strain.getRgdId());
    }

    /**
     * insert new strain attachment
     *
     * @throws Exception
     */
    public void insertStrainAttachment(int strainId,String type,InputStream data,String contentType,String fileName,String login) throws Exception{

        String sql = "insert into strain_files(strain_id,file_type,file_data,content_type,file_name,modified_by) values (?,?,?,?,?,?) ";
        Connection conn = null;
        PreparedStatement stmt;
        conn = this.getDataSource().getConnection();
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1,strainId);
        stmt.setString(2,type);
        stmt.setBlob(3,data);
        stmt.setString(4,contentType);
        stmt.setString(5,fileName);
        stmt.setString(6,login);

        stmt.execute();
        conn.close();
    }
    public void updateStrainAttachment(int strainId,String type,InputStream data,String contentType,String fileName,String login) throws Exception{

        String sql = "update strain_files set file_data =?,content_type=?,file_name=?,modified_by = ?,last_modified_date = sysdate where strain_id = ? and file_type= ? ";
        Connection conn = null;
        PreparedStatement stmt;
        conn = this.getDataSource().getConnection();
        stmt = conn.prepareStatement(sql);
        stmt.setBlob(1,data);
        stmt.setString(2,contentType);
        stmt.setString(3,fileName);
		stmt.setString(4,login);
        stmt.setInt(5,strainId);
        stmt.setString(6,type);
         stmt.execute();
        conn.close();
    }
    public String getContentType(int rgdId,String type) throws Exception{
        String sql ="select content_type from strain_files where strain_id = ? and file_type = ?";
        return getStringResult(sql,rgdId,type);
    }
    public String getFileName(int rgdId,String type) throws Exception{
        String sql ="select file_name from strain_files where strain_id = ? and file_type = ?";
        return getStringResult(sql,rgdId,type);
    }
    public Blob getStrainAttachment(int rgdId, String type) throws Exception {
        String sql = "select file_data from strain_files where strain_id = ? and file_type = ?";
        Connection conn = null;
        PreparedStatement stmt;
        ResultSet rs;
        conn = this.getDataSource().getConnection();
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1,rgdId);
        stmt.setString(2,type);
        rs = stmt.executeQuery();
        while (rs.next()) {
            Blob data = rs.getBlob("file_data");
            return data;
        }
        rs.close();
        stmt.close();
        conn.close();
        return null;
    }
    public List<Strain> getStrainsByGroupId(int strain_group_id,int speciesTypeKey) throws Exception {
        String sql= "select s.*, ? as species_type_key from STRAINS s,ont_terms o,PHENOMINER_STRAIN_GROUP p "+
                "where p.strain_group_id=? and p.strain_ont_id = o.term_acc and s.STRAIN_SYMBOL = o.term";

        return executeStrainQuery(sql, speciesTypeKey,strain_group_id);
    }

    /// Strain query implementation helper
    public List<Strain> executeStrainQuery(String query, Object ... params) throws Exception {
        StrainQuery q = new StrainQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    public List getStrainTypes() throws Exception{

        return StringListQuery.execute(this, "SELECT strain_type_name_lc FROM strain_types");
    }

    /**
     * get available strain modification methods
     * @return list of curator-approved modification methods
     * @throws Exception
     */
    public List<String> getModificationMethods() throws Exception{

        return StringListQuery.execute(this, "SELECT modification_method FROM strain_modification_methods");
    }

    /**
     * get all status entries for given strain sorted by relevance, i.e. most recent status is returned first
     * @param strainRgdId strain rgd id
     * @return strain status entries sorted by relevance
     * @throws Exception
     */
    public List<Strain.Status> getStatusLog(int strainRgdId) throws Exception {
        StrainStatusQuery q = new StrainStatusQuery(this.getDataSource(),
                "SELECT * FROM strain_status_log WHERE strain_rgd_id=? "+
                        "ORDER BY status_date DESC NULLS LAST, strain_status_log_key DESC");
        return execute(q, strainRgdId);
    }

    /**
     * insert a row into STRAIN_STATUS_LOG table
     * @param status Strain.Status object
     * @return value of STRAIN_STATUS_LOG_KEY
     * @throws Exception
     */
    public int insertStatusLog(Strain.Status status) throws Exception {

        status.key = getNextKeyFromSequence("STRAIN_STATUS_LOG_SEQ");
        String sql = "INSERT INTO strain_status_log (strain_status_log_key, strain_rgd_id, status_date, live_animals_y_n, "+
                "cryopreserved_embryo_y_n, cryopreserved_sperm_y_n, cryorecovery_y_n) VALUES(?,?,?,?,?,?,?)";
        update(sql, status.key, status.strainRgdId, status.statusDate,
                status.liveAnimals==null ? null : status.liveAnimals ? "Y" : "N",
                status.cryopreservedEmbryo==null ? null : status.cryopreservedEmbryo ? "Y" : "N",
                status.cryopreservedSperm==null ? null : status.cryopreservedSperm ? "Y" : "N",
                status.cryorecovery==null ? null : status.cryorecovery ? "Y" : "N");
        return status.key;
    }

    /**
     * StrainDAOException should be thrown by StrainDAO methods to differentiate between ours and the framework's exceptions
     */
    public class StrainDAOException extends Exception {

        public StrainDAOException(String msg) {
            super(msg);
        }
    }
}
