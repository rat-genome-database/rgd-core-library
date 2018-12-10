package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.VariantsQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Variants;

import java.util.List;

import edu.mcw.rgd.datamodel.RgdId;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 7, 2018
 * Time: 9:50:52 AM
 * <p>
 * API to manipulate data in Variants table
 */
public class VariantsDAO extends AbstractDAO {
    /**
     * get variant by variant rgd id
     * @param rgdId variant rgd id
     * @return Variants object
     * @throws Exception when variant rgd id is invalid
     * @deprecated Exceptions should not be used as substitute for normal flow logic; NULL should be returned instead
     */
      public Variants getVariant(int variantRgdId) throws Exception {
        String query = "select v.*,r.species_type_key from variants v, RGD_IDS r where r.RGD_ID=v.RGD_ID and r.RGD_ID=?";

        List<Variants> variants = executeVariantsQuery(query, variantRgdId);
        if (variants.size() == 0) {
            throw new Exception("Variant " + variantRgdId + " not found");
        }
        return variants.get(0);
    }

    /**
     * Update variant in the datastore based on rgdID
     *
     * @param variant Variants object
     * @throws Exception when something wrong happens in the spring framework
     */
    public void updateVariant(Variants variants) throws Exception{

        String sql = "UPDATE variants SET SO_ACC_ID=?,name=?,description=?,ref_nuc=?,var_nuc=?,notes=?,last_modified_date=current_date  where RGD_ID=?";

        update(sql, variants.getType(),variants.getName(),
                variants.getDescription(), variants.getRef_nuc(), variants.getVar_nuc(),
                variants.getNotes(),variants.getRgdId());
    }

    /**
     * insert a qtl into the data store
     * note: rgd_id are generated automatically
     * @param variant
     * @throws Exception
     */
    public void insertVariant(Variants variants, String objectStatus, int speciesTypeKey) throws Exception{

        RGDManagementDAO dao = new RGDManagementDAO();
        RgdId id = null;
        try {
            // create new qtl rgd id
            id = dao.createRgdId(RgdId.OBJECT_KEY_VARIANT, objectStatus, speciesTypeKey);
            variants.setRgdId(id.getRgdId());
            System.out.println("Rgd id:" + id.getRgdId());
            String sql = "INSERT INTO variants (RGD_ID,SO_ACC_ID, NAME, " +
                    "DESCRIPTION, REF_NUC, VAR_NUC, NOTES, LAST_MODIFIED_DATE) " +
                    "VALUES (?,?,?,?,?,?,?,current_date)";

            update(sql,  variants.getRgdId(), variants.getType(),variants.getName(),
                    variants.getDescription(), variants.getRef_nuc(), variants.getVar_nuc(),
                    variants.getNotes());
        }
        catch(Exception e) {
            // rollback changes if something goes wrong
            if( id!=null )
                dao.deleteRgdId(id.getRgdId());
            throw e;
        }
    }



    /// QTL query implementation helper
    public List<Variants> executeVariantsQuery(String query, Object ... params) throws Exception {
        VariantsQuery q = new VariantsQuery(this.getDataSource(), query);
        return execute(q, params);
    }
}
