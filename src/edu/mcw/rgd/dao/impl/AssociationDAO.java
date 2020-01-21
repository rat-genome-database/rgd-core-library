package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.*;

import java.sql.*;
import java.util.*;
import java.util.Map;

/**
 * @author jdepons
 * @since May 20, 2008
 */
public class AssociationDAO extends AbstractDAO {

    //qtl   strain, gene, reference
    //gene  reference, strain, gene, qtl, sslp
    //strain   reference, strain  , gene, sslp qtl 
    // sslp   gene
    //reference

    GeneDAO geneDAO = null;
    QTLDAO qtlDAO = null;
    SSLPDAO sslpDAO = null;
    StrainDAO strainDAO = null;
    ReferenceDAO refDAO = null;

    // lazy creation of DAO objects
    public GeneDAO getGeneDAO() {
        if( geneDAO==null )
            geneDAO = new GeneDAO();
        return geneDAO;
    }

    public QTLDAO getQtlDAO() {
        if( qtlDAO==null )
            qtlDAO = new QTLDAO();
        return qtlDAO;
    }

    public SSLPDAO getSslpDAO() {
        if( sslpDAO==null )
            sslpDAO = new SSLPDAO();
        return sslpDAO;
    }

    public StrainDAO getStrainDAO() {
        if( strainDAO==null )
            strainDAO = new StrainDAO();
        return strainDAO;
    }

    public ReferenceDAO getReferenceDAO() {
        if( refDAO==null )
            refDAO = new ReferenceDAO();
        return refDAO;
    }

    /**
     * returns qtl associations for each given qtl key.. also gives you the association description.
     * for eg.. given a qtl key.. it returns a hashmap of all related qtls which also includes
     * @param qtlKey qtl key
     * @return hashMap with key as qtlRGDID and values as a pipe separated " ||"  string of values
     * species_type_key, QTL_REL_DESC, ref_rgd_id, RGD_ID , QTL_SYMBOL
     * @throws Exception
     */
    public Map<Integer, String> getQtlToQtlAssociations(int qtlKey) throws Exception{

        Map<Integer, String> relQTLHash = new HashMap<>();

        String getRelQTLString =
            "select id.species_type_key, t.QTL_REL_DESC, ref.rgd_id REFRGDID,q1.RGD_ID QTLRGDID, q1.QTL_SYMBOL" +
            " from related_qtls r, qtls q, QTL_REL_TYPES t, references ref, qtls q1, RGD_IDS id" +
            " where (" +
            " (r.qtl_key1=q.qtl_key OR r.qtl_key2=q.qtl_key)" +
            " and r.qtl_rel_type_key = t.qtl_rel_type_key" +
            " and r.ref_key = ref.ref_key" +
            " and q.qtl_key=?" +
            " ) and (" +
            " q1.QTL_KEY =" +
            " Case r.QTL_KEY1" +
            " WHEN ? THEN r.QTL_KEY2" +
                " ELSE r.QTL_KEY1 END" +
            " and q1.rgd_id = id.rgd_id)";

        try( Connection qtlConn = this.getConnection() ){
            PreparedStatement getRelQTL = qtlConn.prepareStatement(getRelQTLString);
            getRelQTL.setInt(1, qtlKey);
            getRelQTL.setInt(2, qtlKey);

            ResultSet relQTL = getRelQTL.executeQuery();
            while(relQTL.next()){
                String value = relQTL.getInt(1)+"||"+relQTL.getString(2)+"||"+relQTL.getInt(3)+"||"+
                        relQTL.getInt(4)+"||"+relQTL.getString(5);
                relQTLHash.put(Integer.parseInt(relQTL.getString(4)),value);
            }
        }
        return relQTLHash;
    }


    /**
     * insert rgdid to strain association
     * @param strainRgdId
     * @param associationRgdId
     * @throws Exception
     */
    public void insertStrainAssociation(int strainRgdId, int associationRgdId) throws Exception{

        String sql = "INSERT INTO rgd_strains_rgd (strain_key,rgd_id) "+
                "SELECT strain_key,? FROM strains WHERE rgd_id=?";
        update(sql, associationRgdId, strainRgdId);
    }


    public void removeStrainAssociation(int strainRgdId, int associationRgdId) throws Exception{

        String sql = "DELETE FROM rgd_strains_rgd WHERE strain_key=(SELECT strain_key FROM strains WHERE rgd_id=?) AND rgd_id=?";
        update(sql, strainRgdId, associationRgdId);
    }

    /**
     * return list of objects associated with given strain rgd id
     * @param strainRGDID strain rgd id
     * @return list of objects: it could be either Gene, Strain, SSLP or int for other objects
     * @throws Exception when something bad happens in spring framework
     */
    public List getStrainAssociations(int strainRGDID) throws Exception{

        List acc = new ArrayList();
        int strainKey = getStrainDAO().getStrain(strainRGDID).getKey();

        Connection conn = null;
        try {

            conn =  this.getConnection();
            String sql = "select rsr.rgd_id,r.object_key from rgd_strains_rgd rsr, rgd_ids r " +
                    "where rsr.strain_key=? and rsr.rgd_id=r.rgd_id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,strainKey);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int rgdId = rs.getInt("rgd_id");
                int objectKey = rs.getInt("object_key");
                if (objectKey==RgdId.OBJECT_KEY_GENES) {
                    acc.add(getGeneDAO().getGene(rgdId));
                }else if (objectKey==RgdId.OBJECT_KEY_SSLPS) {
                    acc.add(getSslpDAO().getSSLP(rgdId));
                }else if (objectKey==RgdId.OBJECT_KEY_STRAINS) {
                    acc.add(getStrainDAO().getStrain(rgdId));
                }
                else {
                    acc.add(rgdId);
                }
            }

        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
            }
        }
        return acc;
    }

    /**
     * return list of objects associated with given strain rgd id
     * @param strainRGDID strain rgd id
     * @param objectKey object key; #see edu.mcw.rgd.datamodel.RgdId
     * @return list of objects: it could be either Gene, Strain, SSLP or int for other objects
     * @throws Exception when something bad happens in spring framework
     */
    public List getStrainAssociations(int strainRGDID, int objectKey) throws Exception{

        List acc = new ArrayList();

        int strainKey = getStrainDAO().getStrain(strainRGDID).getKey();

        Connection conn = null;
        try {

            conn =  this.getConnection();
            String sql = "select rsr.rgd_id from rgd_strains_rgd rsr, rgd_ids r " +
                    "where rsr.strain_key=? and rsr.rgd_id=r.rgd_id and r.object_key=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,strainKey);
            ps.setInt(2,objectKey);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int rgdId = rs.getInt("rgd_id");
                if (objectKey==RgdId.OBJECT_KEY_GENES) {
                    acc.add(getGeneDAO().getGene(rgdId));
                }else if (objectKey==RgdId.OBJECT_KEY_SSLPS) {
                    acc.add(getSslpDAO().getSSLP(rgdId));
                }else if (objectKey==RgdId.OBJECT_KEY_STRAINS) {
                    acc.add(getStrainDAO().getStrain(rgdId));
                }
                else {
                    acc.add(rgdId);
                }
            }

        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
            }
        }
        return acc;
    }

    /**
     * return association info for genes associated with given strain rgd id
     * @param strainRgdId strain rgd id
     * @return list of Strain2MarkerAssociation objects
     * @throws Exception when something bad happens in spring framework
     */
    public List<Strain2MarkerAssociation> getStrain2GeneAssociations(int strainRgdId) throws Exception{

        String query = "SELECT a.strain_key assoc_key,a.marker_type assoc_type,a.region_name assoc_subtype, "+
                " s.rgd_id master_rgd_id,r.rgd_id detail_rgd_id,null creation_date,r.gene_symbol src_pipeline "+
                "FROM rgd_strains_rgd a,strains s,genes r " +
                "WHERE s.rgd_id=? AND s.strain_key=a.strain_key AND a.rgd_id=r.rgd_id "+
                "ORDER BY a.region_name,a.marker_type";

        return getStrain2ObjectAssociations(strainRgdId, query);
    }

    /**
     * return association info for sslps associated with given strain rgd id
     * @param strainRgdId strain rgd id
     * @return list of Strain2MarkerAssociation objects
     * @throws Exception when something bad happens in spring framework
     */
    public List<Strain2MarkerAssociation> getStrain2SslpAssociations(int strainRgdId) throws Exception{

        String query = "SELECT a.strain_key assoc_key,a.marker_type assoc_type,a.region_name assoc_subtype, "+
                " s.rgd_id master_rgd_id,r.rgd_id detail_rgd_id,null creation_date,r.rgd_name src_pipeline "+
                "FROM rgd_strains_rgd a,strains s,sslps r " +
                "WHERE s.rgd_id=? AND s.strain_key=a.strain_key AND a.rgd_id=r.rgd_id "+
                "ORDER BY a.region_name,a.marker_type";

        return getStrain2ObjectAssociations(strainRgdId, query);
    }

    /**
     * return association info for strains associated with given strain rgd id
     * @param strainRgdId strain rgd id
     * @return list of Strain2MarkerAssociation objects
     * @throws Exception when something bad happens in spring framework
     */
    public List<Strain2MarkerAssociation> getStrain2StrainAssociations(int strainRgdId) throws Exception{

        String query = "SELECT a.strain_key assoc_key,a.marker_type assoc_type,a.region_name assoc_subtype, "+
                " s.rgd_id master_rgd_id,r.rgd_id detail_rgd_id,null creation_date,r.strain_symbol src_pipeline "+
                "FROM rgd_strains_rgd a,strains s,strains r " +
                "WHERE s.rgd_id=? AND s.strain_key=a.strain_key AND a.rgd_id=r.rgd_id "+
                "ORDER BY a.region_name,a.marker_type";

        return getStrain2ObjectAssociations(strainRgdId, query);
    }

    private List<Strain2MarkerAssociation> getStrain2ObjectAssociations(int strainRgdId, String query) throws Exception{

        Strain2MarkerAssociationQuery q = new Strain2MarkerAssociationQuery(this.getDataSource(), query);
        return execute(q, strainRgdId);
    }

    /**
     * update strain2sslp association
     * @param assoc Strain2MarkerAssociation object
     * @throws Exception when something bad happens in spring framework
     */
    public void updateStrainAssociation(Strain2MarkerAssociation assoc) throws Exception {

        String sql = "UPDATE rgd_strains_rgd SET marker_type=?,region_name=? "+
                "WHERE rgd_id=? AND strain_key=(SELECT strain_key FROM strains WHERE rgd_id=?)";
        upsertStrainAssociation(assoc, sql);
    }

    /**
     * insert strain2sslp association
     * @param assoc Strain2MarkerAssociation object
     * @throws Exception when something bad happens in spring framework
     */
    public void insertStrainAssociation(Strain2MarkerAssociation assoc) throws Exception {

        String sql = "INSERT INTO rgd_strains_rgd (marker_type,region_name,rgd_id,strain_key) "+
                "SELECT ?,?,?,strain_key FROM strains WHERE rgd_id=?";
        upsertStrainAssociation(assoc, sql);
    }

    private void upsertStrainAssociation(Strain2MarkerAssociation assoc, String sql) throws Exception {

        update(sql, assoc.getMarkerType(), assoc.getRegionName(), assoc.getMarkerRgdId(), assoc.getStrainRgdId());
    }

    public List<QTL> getQTLAssociationsForStrain(int strainRgdId) throws Exception{

        String sql = "SELECT q.*,i.species_type_key  FROM qtls q, rgd_ids i\n" +
                "WHERE q.rgd_id=i.rgd_id AND i.object_status='ACTIVE' AND qtl_key IN \n" +
                "   (SELECT qtl_key FROM rgd_qtl_strain WHERE strain_key=(SELECT strain_key FROM strains WHERE rgd_id=?))";
        return QTLQuery.execute(this, sql, strainRgdId);
    }

    /**
     * get strains associated with given qtl
     * @param qtlRGDID qtl rgd id
     * @return List of Strain-s associated with given qtl
     * @throws Exception when something bad happens in spring framework
     */
    public List<Strain> getStrainAssociationsForQTL(int qtlRGDID) throws Exception{

        String query = "SELECT s.*, r.species_type_key "
            +"FROM strains s, rgd_ids r, rgd_qtl_strain qs, qtls q "
            +"WHERE r.rgd_id=s.rgd_id AND s.strain_key=qs.strain_key AND q.qtl_key=qs.qtl_key AND q.rgd_id=?";
        StrainQuery gq = new StrainQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.compile();
        return gq.execute(qtlRGDID);
    }

    /**
     * removes strain association for given qtl
     * @param strainRGDId rgd id for strain to be de-associated
     * @param qtlRGDId rgd id for qtl
     * @throws Exception when something bad happens in spring framework
     */
    public void removeStrainToQTLAssociation(int strainRGDId, int qtlRGDId) throws Exception{

        int qtlKey = getQtlDAO().getQTL(qtlRGDId).getKey();

        int strainKey = getStrainDAO().getStrain(strainRGDId).getKey();

        Connection conn = null;
        try {

            conn =  this.getConnection();
            String sql = "delete from rgd_qtl_strain where qtl_key=? and strain_key=? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,qtlKey);
            ps.setInt(2,strainKey);

            ps.execute();
        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
            }
        }
    }

    public void insertStrainToQTLAssociation(int strainRGDId, int qtlRGDId) throws Exception{

        int qtlKey = getQtlDAO().getQTL(qtlRGDId).getKey();

        int strainKey = getStrainDAO().getStrain(strainRGDId).getKey();

        Connection conn = null;
        try {

            conn =  this.getConnection();
            String sql = "insert into rgd_qtl_strain (strain_key, qtl_key) values (?,?) ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,strainKey);
            ps.setInt(2,qtlKey);

            ps.execute();
        } finally {
            try {
               conn.close();
            }catch (Exception ignored) {
            }
        }
    }

    /**
     * get object rgd ids associated with given reference
     * @param refRgdId rgd id of a reference
     * @return List of rgd ids of active objects associated with this reference
     * @throws Exception if something unexpected happens in the framework
     * @see #getElementsAssociatedWithReference(int) getElementsAssociatedWithReference
     */
    public List<Integer> getObjectsAssociatedWithReference(int refRgdId) throws Exception {

        String query = "SELECT r.rgd_id FROM rgd_ref_rgd_id r, rgd_ids rs, references rf "+
                "WHERE rf.rgd_id=? AND rf.ref_key=r.ref_key AND rs.rgd_id=r.rgd_id AND rs.object_status='ACTIVE'";

        return IntListQuery.execute(this, query, refRgdId);
    }

    /**
     * get object rgd ids of given kind associated with given reference
     * @param refRgdId rgd id of a reference
     * @param objectKey object key
     * @return List of rgd ids of active objects associated with this reference
     * @throws Exception if something unexpected happens in the framework
     * @see #getElementsAssociatedWithReference(int) getElementsAssociatedWithReference
     */
    public List<Integer> getObjectsAssociatedWithReference(int refRgdId, int objectKey) throws Exception {

        String query = "SELECT r.rgd_id FROM rgd_ref_rgd_id r, rgd_ids rs, references rf "+
                "WHERE rf.rgd_id=? AND rf.ref_key=r.ref_key AND rs.rgd_id=r.rgd_id AND rs.object_status='ACTIVE' AND rs.object_key=?";

        return IntListQuery.execute(this, query, refRgdId, objectKey);
    }

    /**
     * get object rgd ids of given kind associated with given reference
     * @param refRgdId rgd id of a reference
     * @param objectKey object key
     * @param speciesTypeKey species type key for the returned objects
     * @return List of rgd ids of active objects associated with this reference
     * @throws Exception if something unexpected happens in the framework
     * @see #getElementsAssociatedWithReference(int) getElementsAssociatedWithReference
     */
    public List<Integer> getObjectsAssociatedWithReference(int refRgdId, int objectKey, int speciesTypeKey) throws Exception {

        if( speciesTypeKey==0 )
            return getObjectsAssociatedWithReference(refRgdId, objectKey);

        String query = "SELECT r.rgd_id FROM rgd_ref_rgd_id r, rgd_ids rs, references rf "+
                "WHERE rf.rgd_id=? AND rf.ref_key=r.ref_key AND rs.rgd_id=r.rgd_id AND rs.object_status='ACTIVE' "+
                "AND rs.object_key=? AND rs.species_type_key=?";

        return IntListQuery.execute(this, query, refRgdId, objectKey, speciesTypeKey);
    }

    /**
     * get active objects associated with given reference
     * @param refRgdId rgd id of a reference
     * @return List of GenomicElement objects for objects associated with this reference
     * @throws Exception if something unexpected happens in the framework
     * @see #getObjectsAssociatedWithReference(int) getObjectsAssociatedWithReference
     */
    public List<GenomicElement> getElementsAssociatedWithReference(int refRgdId) throws Exception {

        String sql =
        "SELECT rs.species_type_key,rs.object_status,rs.object_key,v.* "+
        "FROM rgd_ref_rgd_id r, rgd_ids rs, references rf,genomic_elements_view v "+
        "WHERE rf.rgd_id=? "+
        "AND rf.ref_key=r.ref_key AND rs.rgd_id=r.rgd_id AND rs.object_status='ACTIVE' "+
        "AND rs.rgd_id=v.rgd_id ";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        return execute(q, refRgdId);
    }

    /**
     * get list of references associated with given object
     * @param rgdId object rgd id
     * @return list of Reference objects
     * @throws Exception if something unexpected happens in the framework
     */
    public List<Reference> getReferenceAssociations(int rgdId) throws Exception{

        return getReferenceDAO().getReferencesForObject(rgdId);
    }

    public void removeReferenceAssociation(int rgdId, int refRgdId) throws Exception{

        int refKey = getReferenceDAO().getReference(refRgdId).getKey();

        String sql = "delete from rgd_ref_rgd_id where ref_key=? and rgd_id=?";
        update(sql, refKey, rgdId);
    }

    /**
     * insert reference association for given object
     * @param rgdId rgd id of object the reference will be associated with
     * @param refRgdId rgd id of the reference to be associated with
     * @throws Exception when something wrong happens in spring framework
     */
    public void insertReferenceeAssociation(int rgdId, int refRgdId) throws Exception{

        int refKey = getReferenceDAO().getReference(refRgdId).getKey();
        insertReferenceAssociationByKey(refKey, rgdId);
    }

    /**
     * insert reference association for given object
     * @param refKey reference key of the reference to be associated with
     * @param objectRgdId rgd id of object the reference will be associated with
     * @return number of rows inserted; return 0 if association already exists
     * @throws Exception when something wrong happens in spring framework
     */
    public int insertReferenceAssociationByKey(int refKey, int objectRgdId) throws Exception{

        String sql = "INSERT INTO rgd_ref_rgd_id (ref_key, rgd_id) SELECT ?,? FROM dual "+
                "WHERE NOT EXISTS (SELECT 1 FROM rgd_ref_rgd_id WHERE ref_key=? AND rgd_id=?)";

        return update(sql, refKey, objectRgdId, refKey, objectRgdId);
    }

    /**
     * insert reference author association for given author key and ref key
     * @param refKey
     * @param authorKey
     * @param authorOrder
     * @return number of rows inserted; return 0 if association already exists
     * @throws Exception
     */
   public int insertRefAuthorAssociation(int refKey, int authorKey, int authorOrder) throws Exception{
        return refDAO.insertRefAuthorAssociation(refKey, authorKey, authorOrder);
   }

    /**
     * return genes associated with given qtl
     * @param rgdId qtl rgd id
     * @return genes associated with given qtl
     * @throws Exception when something wrong happens in spring framework
     */
    public List<Gene> getGeneAssociationsByQTL(int rgdId) throws Exception{

        String query = "SELECT g.* FROM genes g, qtls q, rgd_gene_qtl gq "+
                "WHERE q.rgd_id=? AND q.qtl_key=gq.qtl_key AND g.gene_key=gq.gene_key";
        return GeneQuery.execute(this, query, rgdId);
    }

    /**
     * return qtls associated with given gene
     * @param geneKey gene key
     * @return list of qtls associated with the given gene
     * @throws Exception when something wrong happens in spring framework
     */
    public List<QTL> getQtlAssociationsByGene(int geneKey) throws Exception{

        String query = "SELECT q.*,r.species_type_key FROM qtls q, rgd_gene_qtl rq, rgd_ids r "+
                "WHERE rq.gene_key=? AND q.qtl_key=rq.qtl_key AND r.rgd_id=q.rgd_id";
        return getQtlDAO().executeQtlQuery(query, geneKey);
    }

    public void removeGeneAssociationsByQTL(int qtlRGDId, int geneRGDId) throws Exception{

        int qtlKey = getQtlDAO().getQTL(qtlRGDId).getKey();
        int geneKey = getGeneDAO().getGene(geneRGDId).getKey();

        String sql = "delete from rgd_gene_qtl where qtl_key=? and gene_key=? ";
        update(sql, qtlKey, geneKey);
    }

    /**
     * insert association between gene and qtl (candidate genes on qtl report page)
     * @param qtlRGDId qtl rgd id
     * @param geneRGDId gene rgd id
     * @throws Exception when something wrong happens in spring framework
     */
    public void insertGeneAssociationsByQTL(int qtlRGDId, int geneRGDId) throws Exception{

        int qtlKey = getQtlDAO().getQTL(qtlRGDId).getKey();
        int geneKey = getGeneDAO().getGene(geneRGDId).getKey();

        String sql = "INSERT INTO rgd_gene_qtl (gene_key, qtl_key) VALUES (?,?)";
        update(sql, geneKey, qtlKey);
    }

    /**
     * associate gene with sslp
     * @param geneKey gene key
     * @param sslpKey sslp key
     * @param srcPipeline source pipeline: 'RGD' if curated in RGD
     * @return 1 of association has been made; 0 if such association already exists
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int associateGeneWithSslp(int geneKey, int sslpKey, String srcPipeline) throws Exception{

        String sql = "INSERT INTO rgd_gene_sslp (gene_key, sslp_key, src_pipeline) SELECT ?,?,? FROM dual "+
                "WHERE NOT EXISTS (SELECT 1 FROM rgd_gene_sslp WHERE gene_key=? AND sslp_key=? AND src_pipeline=?)";
        return update(sql, geneKey, sslpKey, srcPipeline, geneKey, sslpKey, srcPipeline);
    }

    /** return all genes associated with given sslp, given sslp key
     * @param sslpKey key for sslp
     * @return List of Genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getGeneAssociationsBySslp(int sslpKey) throws Exception {
        String query =
                "SELECT g.*,r.species_type_key "+
                "FROM genes g, rgd_ids r, rgd_gene_sslp gs "+
                "WHERE gs.sslp_key=? AND g.gene_key=gs.gene_key AND g.rgd_id=r.rgd_id";
        return GeneQuery.execute(this, query, sslpKey);
    }

    /**
     * remove associations of a gene with sslp (sslp as marker for gene)
     * @param sslpRGDId sslp rgd id
     * @param geneRGDId gene rgd id
     * @throws Exception
     */
    public void removeGeneAssociationsBySslp(int sslpRGDId, int geneRGDId) throws Exception{

        String sql = "DELETE FROM rgd_gene_sslp "+
                "WHERE sslp_key=(SELECT sslp_key FROM sslps WHERE rgd_id=?) "+
                "AND gene_key=(SELECT gene_key FROM genes WHERE rgd_id=?) ";
        update(sql, sslpRGDId, geneRGDId);
    }

    public void associateNoteWithRef(int refKey, int notesKey) throws Exception{

        String sql = "insert into note_ref_id (note_key, ref_key) values (?,?)";
        update(sql, notesKey, refKey);
    }

    /**
     * insert a new association into RGD_ASSOCIATIONS table; assoc_key will be automatically taken from database sequence
     * <p>Note: assoc_type and assoc_subtype must be lower-case and src_pipeline uppercase</p>
     * @param assoc Association object to be inserted
     * @return value of generated assoc_key
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertAssociation( Association assoc ) throws Exception {

        String sql = "INSERT INTO rgd_associations (assoc_key, assoc_type, assoc_subtype, master_rgd_id, detail_rgd_id," +
                            "creation_date, src_pipeline) " +
                "SELECT ?,?,?,?,?,SYSDATE,? FROM dual " +
                "WHERE NOT EXISTS(SELECT 1 FROM rgd_associations WHERE assoc_type=? AND master_rgd_id=? AND detail_rgd_id=?)";

        assoc.setAssocKey(this.getNextKeyFromSequence("rgd_associations_seq"));

        update(sql, assoc.getAssocKey(), assoc.getAssocType(), assoc.getAssocSubType(), assoc.getMasterRgdId(),
            assoc.getDetailRgdId(), assoc.getSrcPipeline(),
            assoc.getAssocType(), assoc.getMasterRgdId(), assoc.getDetailRgdId());

        return assoc.getAssocKey();
    }

    /**
     * update association in RGD_ASSOCIATIONS table
     * <p>Note: assoc_type and assoc_subtype must be lower-case and src_pipeline uppercase</p>
     * @param assoc Association object to be updated
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateAssociation( Association assoc ) throws Exception {

        String sql = "UPDATE rgd_associations SET "+
                        "assoc_type=?, assoc_subtype=?, master_rgd_id=?, detail_rgd_id=?," +
                        "creation_date=?, src_pipeline=? " +
                    "WHERE assoc_key=?";
        return update(sql, assoc.getAssocType(), assoc.getAssocSubType(), assoc.getMasterRgdId(),
                assoc.getDetailRgdId(), assoc.getCreationDate(), assoc.getSrcPipeline(), assoc.getAssocKey());
    }

    /**
     * delete an association given assoc key
     * @param assocKey value of association key
     * @return count of rows affected: 1 on successful delete, 0 if invalid assoc key
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteAssociationByKey( int assocKey ) throws Exception {

        String sql = "DELETE FROM rgd_associations WHERE assoc_key=?";
        return update(sql, assocKey);
    }

    /**
     * delete association(s) given master rgd id, detail rgd id and assoc type
     * @param masterRgdId master rgd id
     * @param detailRgdId detail rgd id
     * @param assocType assoc type
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteAssociations( int masterRgdId, int detailRgdId, String assocType ) throws Exception {

        String sql = "DELETE FROM rgd_associations "+
                    "WHERE master_rgd_id=? AND detail_rgd_id=? AND assoc_type=?";
        return update(sql, masterRgdId, detailRgdId, assocType);
    }

    /**
     * return list of all association for given master rgd id
     * @param masterRgdId master rgd id
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsForMasterRgdId(int masterRgdId) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a " +
                "WHERE a.master_rgd_id=?";

        return executeAssocQuery(query, masterRgdId);
    }

    /**
     * return list of all association for given master rgd id and association type
     * @param masterRgdId master rgd id
     * @param assocType association type
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsForMasterRgdId(int masterRgdId, String assocType) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a, rgd_ids r " +
                "WHERE a.master_rgd_id=? AND a.assoc_type=? AND rgd_id=detail_rgd_id AND object_status='ACTIVE'";

        return executeAssocQuery(query, masterRgdId, assocType);
    }

    public List<Association> getAssociationsForMRgdIdList(List<Integer> mRgdIdList, String assocType) throws Exception {

        String query = "SELECT a.* FROM rgd_associations a, rgd_ids r " +
                "WHERE a.master_rgd_id in (" +
                Utils.buildInPhrase(mRgdIdList) +
                ") AND a.assoc_type=? AND rgd_id=detail_rgd_id AND object_status='ACTIVE'";

        return executeAssocQuery(query, assocType);
    }
    /**
     * return list of active associated genes for given master rgd id and association type
     * @param masterRgdId master rgd id
     * @param assocType association type
     * @return list of Gene objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Gene> getAssociatedGenesForMasterRgdId(int masterRgdId, String assocType) throws Exception {

        String query = "SELECT g.*,r.* "+
                "FROM genes g, rgd_associations a, rgd_ids r " +
                "WHERE a.master_rgd_id=? AND a.assoc_type=? AND g.rgd_id=detail_rgd_id AND object_status='ACTIVE' AND g.rgd_id=r.rgd_id";

        return GeneQuery.execute(this, query, masterRgdId, assocType);
    }

    public List<Gene> getAssociatedGenesForMasterRgdIdList(List<Integer> masterRgdIdList, String assocType) throws Exception {

        String query = "SELECT g.*,r.* "+
                "FROM genes g, rgd_associations a, rgd_ids r " +
                "WHERE a.master_rgd_id in (" + Utils.buildInPhrase(masterRgdIdList) +
                ") AND a.assoc_type=? AND g.rgd_id=detail_rgd_id AND object_status='ACTIVE' AND g.rgd_id=r.rgd_id";

        return GeneQuery.execute(this, query, assocType);
    }

    /**
     * return list of active associated strains for given master rgd id and association type
     * @param masterRgdId master rgd id
     * @param assocType association type
     * @return list of Strain objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Strain> getAssociatedStrainsForMasterRgdId(int masterRgdId, String assocType) throws Exception {

        String query = "SELECT s.*,r.* "+
                "FROM strains s, rgd_associations a, rgd_ids r " +
                "WHERE a.master_rgd_id=? AND a.assoc_type=? AND s.rgd_id=detail_rgd_id AND object_status='ACTIVE' AND s.rgd_id=r.rgd_id";

        StrainQuery q = new StrainQuery(this.getDataSource(), query);
        return execute(q, masterRgdId, assocType);
    }

    /**
     * return list of all association for given detail rgd id and association type
     * @param detailRgdId detail rgd id
     * @param assocType association type
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsForDetailRgdId(int detailRgdId, String assocType) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a, rgd_ids r " +
                "WHERE a.detail_rgd_id=? AND a.assoc_type=? AND rgd_id=master_rgd_id AND object_status='ACTIVE'";

        return executeAssocQuery(query, detailRgdId, assocType);
    }

    public List<Association> getAssociationsForDetailRgdIdList(List<Integer> detailRgdIdList, String assocType) throws Exception {

        String query = "SELECT a.* "+
            "FROM rgd_associations a, rgd_ids r " +
            "WHERE a.detail_rgd_id in (" +
            Utils.buildInPhrase(detailRgdIdList) +
            ") AND a.assoc_type=? AND rgd_id=master_rgd_id AND object_status='ACTIVE'";

        return executeAssocQuery(query,  assocType);
    }
    /**
     * return active genomic elements for given detail rgd id and association type
     * @param detailRgdId detail rgd id
     * @param assocType association type
     * @return list of GenomicElement objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<GenomicElement> getAssociatedGenomicElementsForDetailRgdId(int detailRgdId, String assocType) throws Exception {

        String query = "SELECT ge.*,r.* "+
                "FROM genomic_elements ge, rgd_associations a, rgd_ids r " +
                "WHERE a.detail_rgd_id=? AND a.assoc_type=? AND ge.rgd_id=master_rgd_id AND object_status='ACTIVE' AND ge.rgd_id=r.rgd_id";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), query);
        return execute(q, detailRgdId, assocType);
    }

    /**
     * return list of associations for given assoc type
     * @param assocType assoc type
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsByType(String assocType) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a " +
                "WHERE a.assoc_type=LOWER(?)";

        return executeAssocQuery(query, assocType);
    }

    /**
     * return list of associations for given assoc type and species
     * @param assocType assoc type
     * @param speciesTypeKey species type key for master rgd id
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsByType(String assocType, int speciesTypeKey) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a,rgd_ids r " +
                "WHERE a.assoc_type=LOWER(?) AND r.rgd_id=a.master_rgd_id AND r.species_type_key=?";

        return executeAssocQuery(query, assocType, speciesTypeKey);
    }

    /**
     * return list of associations for given assoc type and species
     * @param assocType assoc type
     * @param masterSpeciesTypeKey species type key for master rgd id
     * @param detailSpeciesTypeKey species type key for detail rgd id
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsByType(String assocType, int masterSpeciesTypeKey, int detailSpeciesTypeKey) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a,rgd_ids r1,rgd_ids r2 " +
                "WHERE a.assoc_type=LOWER(?) "+
                " AND r1.rgd_id=a.master_rgd_id AND r1.species_type_key=? "+
                " AND r2.rgd_id=a.detail_rgd_id AND r2.species_type_key=?";

        return executeAssocQuery(query, assocType, masterSpeciesTypeKey, detailSpeciesTypeKey);
    }

    /**
     * return count of associations for given assoc type and pair of species
     * @param assocType assoc type
     * @param masterSpeciesTypeKey species type key for master rgd id
     * @param detailSpeciesTypeKey species type key for detail rgd id
     * @return count of associations for given assoc type and pair of species
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getAssociationCountByType(String assocType, int masterSpeciesTypeKey, int detailSpeciesTypeKey) throws Exception {

        String query = "SELECT COUNT(*) "+
                "FROM rgd_associations a,rgd_ids r1,rgd_ids r2 " +
                "WHERE a.assoc_type=LOWER(?) "+
                " AND r1.rgd_id=a.master_rgd_id AND r1.species_type_key=? "+
                " AND r2.rgd_id=a.detail_rgd_id AND r2.species_type_key=?";

        return getCount(query, assocType, masterSpeciesTypeKey, detailSpeciesTypeKey);
    }

    /**
     * return list of associations for given assoc type and source
     * @param assocType assoc type
     * @param source source pipeline
     * @return list of Association objects; never null, but returned list could be empty
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<Association> getAssociationsByTypeAndSource(String assocType, String source) throws Exception {

        String query = "SELECT a.* "+
                "FROM rgd_associations a " +
                "WHERE a.assoc_type=LOWER(?) AND src_pipeline=?";

        return executeAssocQuery(query, assocType, source);
    }

    public List<Association> executeAssocQuery(String query, Object ... params) throws Exception {
        return AssociationQuery.execute(this, query, params);
    }
}

