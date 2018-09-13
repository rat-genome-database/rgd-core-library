package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SSLPQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.SSLP;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
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

        String query = "select s.*,r.SPECIES_TYPE_KEY from SSLPS s, RGD_IDS r "+
                "where r.RGD_ID=s.RGD_ID and s.RGD_NAME_LC=? AND r.OBJECT_STATUS='ACTIVE'";

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
                "where gs.gene_key=? AND gs.sslp_key=s.sslp_key AND r.rgd_id=s.rgd_id";

        return executeSSLPQuery(query, geneKey);
    }

    /**
     * Update qtl in the datastore based on rgdID
     *
     * @param sslp SSLP object
     * @return number of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateSSLP(SSLP sslp) throws Exception{

        String sql = "update SSLPS set SSLP_KEY=?, RGD_NAME=?, RGD_NAME_LC=LOWER(?), EXPECTED_SIZE=?, NOTES=?," +
                " SSLP_TYPE=? where RGD_ID=?";

        return update(sql, sslp.getKey(), sslp.getName(), sslp.getName(), sslp.getExpectedSize(), sslp.getNotes(),
                sslp.getSslpType(), sslp.getRgdId());
    }

    /**
     * insert new SSLP object; SSLP KEY will be automatically generated
     * @param sslp SSLP object to be inserted
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertSSLP(SSLP sslp) throws Exception{

        String sql = "insert into SSLPS (SSLP_KEY, RGD_NAME, RGD_NAME_LC, " +
                "EXPECTED_SIZE, NOTES, SSLP_TYPE, RGD_ID) values (?,?,LOWER(?),?,?,?,?)";

        sslp.setKey(this.getNextKey("SSLPS","SSLP_KEY"));

        return update(sql, sslp.getKey(), sslp.getName(), sslp.getName(), sslp.getExpectedSize(), sslp.getNotes(),
                sslp.getSslpType(), sslp.getRgdId());
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
