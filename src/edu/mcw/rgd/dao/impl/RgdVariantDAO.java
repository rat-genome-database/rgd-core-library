package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdVariantQuery;
import edu.mcw.rgd.datamodel.RgdVariant;

import java.util.List;

import edu.mcw.rgd.datamodel.RgdId;

/**
 * @author hsnalabolu
 * @since Dec 7, 2018
 * API to manipulate data in VARIANTS table
 */
public class RgdVariantDAO extends AbstractDAO {
    /**
     * get variant by variant rgd id
     * @param variantRgdId variant rgd id
     * @return RgdVariant object or null if variant rgd id is invalid
     * @throws Exception when something wrong happens in the spring framework
     */
      public RgdVariant getVariant(int variantRgdId) throws Exception {
        String query = "SELECT v.*,r.species_type_key FROM variants v, rgd_ids r WHERE r.rgd_id=v.rgd_id AND r.rgd_id=?";

        List<RgdVariant> variants = executeVariantsQuery(query, variantRgdId);
        if( variants.isEmpty() ) {
            return null;
        }
        return variants.get(0);
    }

    /**
     * get all active variants for given species
     * @param speciesTypeKey species type key
     * @return List of RgdVariant objects (could be empty, never null)
     * @throws Exception when something wrong happens in the spring framework
     */
    public List<RgdVariant> getVariantsForSpecies(int speciesTypeKey) throws Exception {

        String query = "SELECT v.*,r.species_type_key FROM variants v, rgd_ids r WHERE r.rgd_id=v.rgd_id AND object_status='ACTIVE' AND r.species_type_key=?";

        return executeVariantsQuery(query, speciesTypeKey);
    }

    /**
     * Update variant in the datastore based on rgdID
     *
     * @param variant RgdVariant object
     * @throws Exception when something wrong happens in the spring framework
     */
    public void updateVariant(RgdVariant variant) throws Exception{

        String sql = "UPDATE variants SET SO_ACC_ID=?,name=?,description=?,ref_nuc=?,var_nuc=?,notes=?,last_modified_date=current_date  where RGD_ID=?";

        update(sql, variant.getType(),variant.getName(),
                variant.getDescription(), variant.getRefNuc(), variant.getVarNuc(),
                variant.getNotes(),variant.getRgdId());
    }

    /**
     * insert a qtl into the data store
     * note: rgd_id are generated automatically
     * @param variant
     * @throws Exception
     */
    public void insertVariant(RgdVariant variant, String objectStatus, int speciesTypeKey) throws Exception{

        RGDManagementDAO dao = new RGDManagementDAO();
        RgdId id = null;
        try {
            // create new qtl rgd id
            id = dao.createRgdId(RgdId.OBJECT_KEY_RGDVARIANT, objectStatus, speciesTypeKey);
            variant.setRgdId(id.getRgdId());
            //System.out.println("Rgd id:" + id.getRgdId());
            String sql = "INSERT INTO variants (RGD_ID,SO_ACC_ID, NAME, " +
                    "DESCRIPTION, REF_NUC, VAR_NUC, NOTES, LAST_MODIFIED_DATE) " +
                    "VALUES (?,?,?,?,?,?,?,current_date)";

            update(sql,  variant.getRgdId(), variant.getType(),variant.getName(),
                    variant.getDescription(), variant.getRefNuc(), variant.getVarNuc(),
                    variant.getNotes());
        }
        catch(Exception e) {
            // rollback changes if something goes wrong
            if( id!=null )
                dao.deleteRgdId(id.getRgdId());
            throw e;
        }
    }



    /// QTL query implementation helper
    public List<RgdVariant> executeVariantsQuery(String query, Object ... params) throws Exception {
        RgdVariantQuery q = new RgdVariantQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    public List<RgdVariant> getVariantsFromGeneRgdId(int rgdId) throws Exception{
        String query = "select v.*, ri.species_type_key from rgd_associations a, variants v, rgd_ids ri where a.detail_rgd_id=? and a.master_rgd_id=v.rgd_id and v.rgd_id=ri.rgd_id";
        return executeVariantsQuery(query, rgdId);
    }
}
