package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.PortalCatQuery;
import edu.mcw.rgd.dao.spring.PortalQuery;
import edu.mcw.rgd.dao.spring.PortalTermSetQuery;
import edu.mcw.rgd.datamodel.Portal;
import edu.mcw.rgd.datamodel.PortalCat;
import edu.mcw.rgd.datamodel.PortalTermSet;
import edu.mcw.rgd.datamodel.PortalVersion;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
 * API to handle PORTAL1 table
 */
public class PortalDAO extends AbstractDAO {

    /**
     * get all available portals
     *
     * @return list of Portal objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Portal> getPortals() throws Exception {

        String query = "SELECT p.* FROM portal1 p ORDER BY p.url_name";
        PortalQuery q = new PortalQuery(this.getDataSource(), query);
        return execute(q);
    }

    /**
     * get portals given rgd object is associated with
     *
     * @param rgdId RGD_ID being checked for association with portals
     * @return list of Portal objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Portal> getPortals(int rgdId) throws Exception {
        String query = "select p.* from portal_objects po, portal1 p where po.portal_key=p.portal_key and po.rgd_id=? order by p.url_name";
        PortalQuery q = new PortalQuery(this.getDataSource(), query);
        return execute(q, rgdId);
    }

    /**
     * get a portal given url name; null if there is no portal with such an url
     *
     * @param urlName url name
     * @return Portal object or null
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Portal getPortalByUrl(String urlName) throws Exception {

        String query = "SELECT * FROM portal1 WHERE url_name=?";
        PortalQuery q = new PortalQuery(this.getDataSource(), query);

        List<Portal> portals = execute(q, urlName);
        if (portals.isEmpty())
            return null;
        else
            return portals.get(0);
    }

    /**
     * Return a list of portals given object (gene, qtl, sslp or strain) is associated to.
     * <p>
     * Details: terms annotated to the object are compared against portal termsets -- a portal is returned
     * if at least one annotated term belongs to the termset of the portal, including the termset child terms
     * <p>
     * Note: use this method if you need current accurate data; on production server, use much faster version
     *   getPortalsForObjectCached taking advantage of precomputed data in PORTAL_OBJECTS table
     *
     * @param rgdId object rgd id (makes sense for genes, qtls, sslps and strains)
     * @return list of matching portals
     * @throws Exception on spring framework dao failure
     * @see #getPortalsForObjectCached(int) getPortalsForObjectCached
     */
    public List<Portal> getPortalsForObject(int rgdId) throws Exception {

        String query =
                "SELECT * FROM portal1 WHERE portal_key IN(\n" +
                        "  SELECT portal_key FROM portal_termset1 t\n" +
                        "  WHERE t.term_acc IN (\n" +
                        "    SELECT child_term_acc FROM ont_dag\n" +
                        "    START WITH child_term_acc IN(\n" +
                        "      SELECT term_acc FROM full_annot WHERE annotated_object_rgd_id=?)\n" +
                        "    CONNECT BY PRIOR parent_term_acc=child_term_acc)\n" +
                        "   )";

        PortalQuery q = new PortalQuery(this.getDataSource(), query);
        return execute(q, rgdId);
    }

    /**
     * Return a list of portals the given object (gene, qtl, sslp or strain) is associated to.
     * <p>
     * Details: portals assigned to object are read from PORTAL_OBJECTS table precomputed weekly
     * <p>
     * Note: in general, this method is much faster than live method getPortalsForObject()
     *
     * @param rgdId object rgd id (makes sense for genes, qtls, sslps and strains)
     * @return list of matching portals
     * @throws Exception on spring framework dao failure
     * @see #getPortalsForObject(int) getPortalsForObject
     */
    public List<Portal> getPortalsForObjectCached(int rgdId) throws Exception {

        String query = "SELECT * FROM portal1 WHERE portal_key IN(\n" +
                        "  SELECT portal_key FROM portal_objects WHERE rgd_id=?\n" +
                        ")";

        PortalQuery q = new PortalQuery(this.getDataSource(), query);
        return execute(q, rgdId);
    }

    public List<Portal> getPortalsForTerm(String termAcc) throws Exception {

        String query = "select * from portal1 p1, portal_termset1 pt1 where pt1.term_acc=? and p1.portal_key=pt1.portal_key";

        PortalQuery q = new PortalQuery(this.getDataSource(), query);
        return execute(q, termAcc.toUpperCase());
    }

    /**
     * insert a new portal version for given portal
     *
     * @param portalKey      portal key
     * @param portalBuildNum portal build number
     * @return PortalVersion object
     * @throws Exception when unexpected error in spring framework occurs
     */
    public PortalVersion insertPortalVersion(int portalKey, long portalBuildNum) throws Exception {

        PortalVersion pver = new PortalVersion();
        pver.setPortalBuildNum(portalBuildNum);
        pver.setPortalKey(portalKey);
        pver.setPortalVerStatus("InProgress");
        pver.setPortalVerId(this.getNextKeyFromSequence("PORTAL_VER1_SEQ"));

        String sql = "INSERT INTO portal_ver1 " +
                "( portal_ver_id, portal_key, date_last_updated, portal_ver_status, " +
                " portal_build_num, chart_xml_cc, chart_xml_mp, chart_xml_bp) VALUES " +
                "( ?, ?, SYSDATE, ?, ?, EMPTY_CLOB(), EMPTY_CLOB(), EMPTY_CLOB() ) ";

        update(sql, pver.getPortalVerId(), pver.getPortalKey(),
                pver.getPortalVerStatus(), pver.getPortalBuildNum());

        return pver;
    }

    /**
     * update a portal version for given portal; date_last_updated will be set to SYSDATE
     *
     * @param pver PortalVersion object
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updatePortalVersion(PortalVersion pver) throws Exception {

        String sql = "UPDATE portal_ver1 " +
                "SET portal_key=?, date_last_updated=SYSDATE, portal_ver_status=?, portal_build_num=?, " +
                "chart_xml_cc=?, chart_xml_mp=?, chart_xml_bp=? " +
                "WHERE portal_ver_id=?";

        return update(sql, pver.getPortalKey(), pver.getPortalVerStatus(), pver.getPortalBuildNum(),
                pver.getChartXmlCc(), pver.getChartXmlMp(), pver.getChartXmlBp(),
                pver.getPortalVerId());
    }

    /**
     * Mark last portals as archived and new one as active
     *
     * @param portalVer PortalVersion object that must be made active; all other versions must be made archived
     * @return count of portals made as archived
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int archiveOldPortalVersions(PortalVersion portalVer) throws Exception {

        // archive older versions
        String sql = "UPDATE PORTAL_VER1 set PORTAL_VER_STATUS = 'Archived' where portal_key = ? and PORTAL_VER_STATUS = 'Active'";

        int archivedCount = update(sql, portalVer.getPortalKey());

        // make the current version active
        sql = "UPDATE PORTAL_VER1 set PORTAL_VER_STATUS = 'Active', DATE_LAST_UPDATED = SYSDATE where portal_ver_id = ?";

        update(sql, portalVer.getPortalVerId());

        return archivedCount;
    }

    /**
     * get a list of top level terms (top level term set) for given portal
     *
     * @param portalKey portal key
     * @return List of PortalTermSet objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<PortalTermSet> getTopLevelPortalTermSet(int portalKey) throws Exception {

        String sql = "SELECT * FROM portal_termset1 pt WHERE portal_key = ? AND parent_termset_id IS NULL";
        PortalTermSetQuery q = new PortalTermSetQuery(this.getDataSource(), sql);
        return execute(q, portalKey);
    }

    /**
     * get a list of child term sets for a given top-level term set for a portal
     *
     * @param parentTermSetId parent term set id
     * @param portalKey       portal key
     * @return List of PortalTermSet objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<PortalTermSet> getPortalTermSet(int parentTermSetId, int portalKey) throws Exception {

        String sql = "SELECT * FROM portal_termset1 pt WHERE portal_key=? AND parent_termset_id=?";
        PortalTermSetQuery q = new PortalTermSetQuery(this.getDataSource(), sql);
        return execute(q, portalKey, parentTermSetId);
    }

    /**
     * get summary information for the latest portal version
     *
     * @param portalKey portal key uniquely identifying the portal
     * @return PortalCat object with the summary information for portal
     * @throws Exception when unexpected error in spring framework occurs
     */
    public PortalCat getSummaryForPortal(int portalKey) throws Exception {

        String sql = "SELECT c.* FROM portal1 p, portal_ver1 v, portal_cat1 c " +
                "WHERE p.portal_key=? AND p.portal_key=v.portal_key AND v.portal_ver_id=c.portal_ver_id AND parent_cat_id IS NULL";
        PortalCatQuery q = new PortalCatQuery(this.getDataSource(), sql);

        List<PortalCat> results = execute(q, portalKey);
        if (results.isEmpty())
            return null;
        return results.get(0);
    }

    /**
     * insert a new portal category; PORTAL_CAT_ID is always filled from PORTAL_CAT1_SEQ sequence;
     * if cat.getParentCatId is null, it will be left null; however, if cat.getParentCatId is 0, it will be set
     * to cat.getPortalCatId (for top-level term sets PARENT_CAT_ID=PORTAL_CAT_ID)
     *
     * @param cat PortalCat object to be inserted
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertPortalCat(PortalCat cat) throws Exception {

        cat.setPortalCatId(this.getNextKeyFromSequence("PORTAL_CAT1_SEQ"));

        if (cat.getParentCatId() != null && cat.getParentCatId() == 0) {
            cat.setParentCatId(cat.getPortalCatId());
        }

        String sql = "INSERT INTO portal_cat1 " +
                "(portal_cat_id, portal_ver_id, category_name, parent_cat_id, summary_table_html, " +
                " gviewer_xml_rat, gviewer_xml_mouse, gviewer_xml_human, category_term_acc, " +
                " gene_info_html, qtl_info_html, strain_info_html, annot_obj_cnt_rat, " +
                " annot_obj_cnt_w_children_rat, annot_obj_cnt_w_children_mouse, annot_obj_cnt_w_children_human, " +
                " chart_xml_cc, chart_xml_mp, chart_xml_bp) VALUES " +
                "( ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, " +
                "?, ?, ?, ?, " +
                "?, ?, ?, " +
                "?, ?, ?) ";

        return update(sql,
                cat.getPortalCatId(), cat.getPortalVerId(), cat.getCategoryName(), cat.getParentCatId(), cat.getSummaryTableHtml(),
                cat.getgViewerXmlRat(), cat.getgViewerXmlMouse(), cat.getgViewerXmlHuman(), cat.getCategoryTermAcc(),
                cat.getGeneInfoHtml(), cat.getQtlInfoHtml(), cat.getStrainInfoHtml(), cat.getAnnotObjCntRat(),
                cat.getAnnotObjCntWithChildrenRat(), cat.getAnnotObjCntWithChildrenMouse(), cat.getAnnotObjCntWithChildrenHuman(),
                cat.getChartXmlCc(), cat.getChartXmlMp(), cat.getChartXmlBp()
        );
    }

    /**
     * # Cleanup the data in tables PORTAL_CAT1 and PORTAL_VER1 from old versions.
     * #
     * # Should be run after all portal processing is complete to get rid of old, no longer active,
     * # versions of the data to save up space in database (if not cleaned up, it could take up
     * # many gigabytes of space in the database, what will make Stacy and Kent unhappy).
     *
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int cleanupOldPortalVersions() throws Exception {

        int rowsAffected = 0;
        String sql = "DELETE FROM PORTAL_CAT1 pc WHERE pc.PORTAL_VER_ID IN " +
                "(SELECT pv.PORTAL_VER_ID FROM PORTAL_VER1 pv WHERE pv.PORTAL_VER_STATUS != 'Active')";

        rowsAffected += update(sql);

        sql = "DELETE FROM PORTAL_VER1 pv WHERE pv.PORTAL_VER_STATUS != 'Active'";
        rowsAffected += update(sql);

        return rowsAffected;
    }

    /**
     * merge versions of two portals; active version of source portal;
     * for example active version of 'gomp' portal should be merged with 'go' portal
     * (fromUrl='gomp', toUrl='go')
     *
     * @param fromUrl url of from portal
     * @param toUrl   url of to portal
     * @return count of affected rows
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int mergePortalVersions(String fromUrl, String toUrl) throws Exception {

        String sql = "UPDATE portal_cat1 pc \n" +
                "SET pc.PORTAL_VER_ID = ( \n" +
                "     SELECT portal_ver_id FROM portal_ver1 \n" +
                "       WHERE portal_key = (SELECT portal_key FROM portal1 WHERE url_name = ? ) \n" +
                "         AND portal_ver_status = 'Active' \n" +
                "    ) \n" +
                "WHERE PORTAL_VER_ID IN \n" +
                "    ( \n" +
                "     SELECT portal_ver_id \n" +
                "     FROM portal_ver1 \n" +
                "     WHERE portal_key = (SELECT portal_key FROM portal1 WHERE url_name = ? ) \n" +
                "       AND portal_ver_status = 'Active' \n" +
                "    )";

        return update(sql, toUrl, fromUrl);
    }

    /**
     * delete rows from PORTAL_OBJECTS table that are older than given cutoff date
     *
     * @param cutoffDate cut-off date: records older than the cutoff date are deleted
     * @return count of rows affected
     * @throws Exception if something wrong happens in spring framework
     */
    public int deleteStalePortalObjects(Date cutoffDate) throws Exception {

        String sql = "DELETE FROM portal_objects WHERE modification_date<?";
        return update(sql, cutoffDate);
    }

    /**
     * return keys for portals associated with given rgd object (genes, qtls, ...)
     *
     * @param rgdId object rgd id (genes, qtls, ...)
     * @return list of portal keys
     * @throws Exception if something wrong happens in spring framework
     */
    public List<Integer> getPortalKeysForObject(int rgdId) throws Exception {

        String query = "SELECT portal_key FROM portal_objects WHERE rgd_id=?";
        return IntListQuery.execute(this, query, rgdId);
    }
}
