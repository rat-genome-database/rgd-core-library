package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.MappedSSLPQuery;
import edu.mcw.rgd.dao.spring.SSLPQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.MappedSSLP;
import edu.mcw.rgd.datamodel.SSLP;

import java.util.List;

/**
 * @author jdepons
 * @since May 19, 2008
 */
public class SSLPDAO extends AbstractDAO {

    /**
     * get all active SSLP objects
     * @return list of SSLP objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getActiveSSLPs() throws Exception {

        String query = "select s.*, r.SPECIES_TYPE_KEY from SSLPs s, RGD_IDS r " +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID";

        return executeSSLPQuery(query);
    }

    public List<SSLP> getActiveSSLPs(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "select s.*, r.SPECIES_TYPE_KEY \n" +
                "from SSLPs s, RGD_IDS r , maps_data md\n" +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID and md.rgd_id=s.rgd_id and md.chromosome=? and md.start_pos<=? and md.stop_pos>=? and md.map_key=?";

        return executeSSLPQuery(query, chr, stopPos, startPos, mapKey);
    }
    public List<MappedSSLP> getActiveMappedSSLPs(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "select s.*, r.SPECIES_TYPE_KEY,md.* \n" +
                "from SSLPs s, RGD_IDS r , maps_data md\n" +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID " +
                "and md.rgd_id=s.rgd_id and md.chromosome=? and md.start_pos<=? " +
                "and md.stop_pos>=? and md.map_key=? order by md.start_pos";

        return MappedSSLPQuery.run(this,query, chr, stopPos, startPos, mapKey);
    }

    public List<MappedSSLP> getActiveMappedSSLPsByType(String type) throws Exception{
        String sql = """
                select s.*, r.SPECIES_TYPE_KEY,md.*
                    from SSLPs s, RGD_IDS r , maps_data md
                    where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID
                    and md.rgd_id=s.rgd_id and s.sslp_type=?""";
        return MappedSSLPQuery.run(this, sql, type);
    }

    /**
     * get all active SSLP objects for given species
     * @param speciesType species type key
     * @return list of SSLP objects for given species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getActiveSSLPs(int speciesType) throws Exception {

        String query = "select s.*, r.SPECIES_TYPE_KEY from SSLPs s, RGD_IDS r " +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=s.RGD_ID and r.SPECIES_TYPE_KEY=?";

        return executeSSLPQuery(query, speciesType);
    }

    /**
     * get all active SSLP objects for DB_SNP (SSLP symbol must be a valid rsId symbol)
     * @param speciesTypeKey species type key
     * @return list of SSLP objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getActiveSSLPsForDbSnp(int speciesTypeKey) throws Exception {

        String query = "SELECT s.*, r.species_type_key FROM sslps s, rgd_ids r \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=?\n" +
                "  AND regexp_like(rgd_name_lc,'^rs[0-9]+$')";

        return executeSSLPQuery(query, speciesTypeKey);
    }

    /**
     * get a sslp by rgd_id
     * @param rgdId rgd_id
     * @return a sslp with matching rgd_id
     * @throws Exception throws SslpDAOException if there is no SSLP with given rgd_id; throw other exception types on error in DAO framework
     */
    public SSLP getSSLP(int rgdId) throws Exception {

        List<SSLP> sslps = getSSLPs(rgdId);
        if (sslps==null || sslps.size() == 0) {
            throw new SslpDAOException("SSLP " + rgdId + " not found");
        }
        return sslps.get(0);
    }

    /**
     * get all sslps by rgd_id
     * @param rgdId rgd_id
     * @return a list of sslps with matching rgd_id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getSSLPs(int rgdId) throws Exception {
        String query = "select s.*, r.SPECIES_TYPE_KEY from sslps s, RGD_IDS r where r.RGD_ID=s.RGD_ID and r.RGD_ID=?";

        return executeSSLPQuery(query, rgdId);
    }

    public List<SSLP> getSSLPsByType(String type) throws Exception{
        String sql = "select s.*,r.species_type_key from sslps s, rgd_ids r where r.rgd_id=s.rgd_id and s.sslp_type=?";

        return executeSSLPQuery(sql, type);
    }

    /**
     * get list of active sslps given sslp name and species
     * @param sslpName sslp name (case is not important)
     * @param speciesTypeKey species type key
     * @return list of matching active sslps
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getActiveSSLPsByName(String sslpName, int speciesTypeKey) throws Exception {

        String query = "select s.*,r.SPECIES_TYPE_KEY from SSLPS s, RGD_IDS r "+
                "where r.RGD_ID=s.RGD_ID and s.RGD_NAME_LC=? AND r.SPECIES_TYPE_KEY=? AND r.OBJECT_STATUS='ACTIVE'";

        return executeSSLPQuery(query, sslpName.toLowerCase(), speciesTypeKey);
    }

    /**
     * get list of active sslps given sslp name only
     * @param sslpName sslp name (case is not important)
     * @return list of matching active sslps
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getActiveSSLPsByNameOnly(String sslpName) throws Exception {

        String query = "SELECT s.*,r.species_type_key FROM sslps s, rgd_ids r "+
                "WHERE r.rgd_id=s.rgd_id AND s.rgd_name_lc=? AND r.object_status='ACTIVE'";

        return executeSSLPQuery(query, sslpName.toLowerCase());
    }

    /**
     * get list of sslps for given gene
     * @param geneKey gene key
     * @return list of SSLP objects associated with given gene
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<SSLP> getSSLPsForGene(int geneKey) throws Exception {

        String query = "SELECT s.*,r.species_type_key FROM sslps s, rgd_ids r, rgd_gene_sslp gs "+
                "WHERE gs.gene_key=? AND gs.sslp_key=s.sslp_key AND r.rgd_id=s.rgd_id";

        return executeSSLPQuery(query, geneKey);
    }

    /**
     * Update SSLP object given RGD ID
     *
     * @param sslp SSLP object with RGD ID set
     * @return number of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateSSLP(SSLP sslp) throws Exception{

        String sql = "UPDATE sslps SET sslp_key=?, rgd_name=?, rgd_name_lc=LOWER(?), expected_size=?, notes=?, " +
                "sslp_type=?, seq_template=?, seq_forward=?, seq_reverse=? WHERE rgd_id=?";

        return update(sql, sslp.getKey(), sslp.getName(), sslp.getName(), sslp.getExpectedSize(), sslp.getNotes(),
                sslp.getSslpType(), sslp.getTemplateSeq(), sslp.getForwardSeq(), sslp.getReverseSeq(), sslp.getRgdId());
    }

    /**
     * insert new SSLP object; SSLP KEY will be automatically generated
     * @param sslp SSLP object to be inserted
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertSSLP(SSLP sslp) throws Exception{

        String sql = "INSERT INTO sslps (sslp_key, rgd_name, rgd_name_lc, expected_size, notes, sslp_type, " +
                "seq_template, seq_forward, seq_reverse, rgd_id) VALUES (?,?,LOWER(?),?,?,?,?,?,?,?)";

        sslp.setKey(this.getNextKey("SSLPS","SSLP_KEY"));

        return update(sql, sslp.getKey(), sslp.getName(), sslp.getName(), sslp.getExpectedSize(), sslp.getNotes(),
                sslp.getSslpType(), sslp.getTemplateSeq(), sslp.getForwardSeq(), sslp.getReverseSeq(), sslp.getRgdId());
    }

    /**
     * delete sslp given rgd id
     * @param sslpRgdId sslp rgd id
     * @return count of rows affected
     * @throws Exception
     */
    public int deleteSSLP(int sslpRgdId) throws Exception{

        String sql = "DELETE FROM sslps WHERE RGD_ID=?";
        return update(sql, sslpRgdId);
    }

    /**
     * associate gene with sslp
     * @param geneKey gene key
     * @param sslpkey sslp key
     * @return 1 of association has been made; 0 if such association already exists
     * @throws Exception when unexpected error in spring framework occurs
     * @deprecated use AssociationDAO().associateGeneWithSslp() instead
     */
    public int makeSslpGeneLinkage(int geneKey, int sslpkey) throws Exception{

        return new AssociationDAO().associateGeneWithSslp(geneKey, sslpkey, "RGD");
    }

    /**
     * get list of all known sslp types
     * @return string array containing all known sslp types
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<String> getSslpTypes() throws Exception {

        return StringListQuery.execute(this, "SELECT sslp_type FROM sslp_types");
    }


    /// to differentiate between ours and the framework's exceptions
    public class SslpDAOException extends Exception {

        public SslpDAOException(String msg) {
            super(msg);
        }
    }

    /// SSLP query implementation helper
    public List<SSLP> executeSSLPQuery(String query, Object ... params) throws Exception {
        SSLPQuery q = new SSLPQuery(this.getDataSource(), query);
        return execute(q, params);
    }
}
