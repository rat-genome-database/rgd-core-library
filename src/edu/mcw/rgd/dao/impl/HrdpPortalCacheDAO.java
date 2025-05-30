package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.HrdpPortalCacheQuery;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.HrdpPortalCache;

import java.util.List;

public class HrdpPortalCacheDAO extends AbstractDAO {

    public int insertHrdpPortalData(HrdpPortalCache hrdp) throws Exception{
        String sql= """
                INSERT INTO HRDP_PORTAL_CACHE(strain_id,strain_symbol,group_name,parent_ont_id,child_ont_ids,parent_sample_ids,child_sample_ids,has_parent_pheno_count,has_child_pheno_count,has_parent_sample_count,has_child_sample_count,has_phenominer,has_variant_visualizer,available_strain_id,available_strain_symbol)
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        return update(sql,hrdp.getStrainId(),hrdp.getStrainSymbol(),hrdp.getGroupName(),hrdp.getParentOntId(),hrdp.getChildOntIds(),hrdp.getParentSampleIds(),hrdp.getChildSampleIds(),hrdp.getHasParentPhenoCount(),hrdp.getHasChildPhenoCount(),hrdp.getHasParentSampleCount(),hrdp.getHasChildSampleCount(),hrdp.getHasPhenominer(),hrdp.getHasVariantVisualizer(),hrdp.getAvailableStrainId(),hrdp.getAvailableStrainSymbol());
    }

    public int updateHrdpPortalData(HrdpPortalCache hrdp) throws Exception{
        String sql= """
                UPDATE HRDP_PORTAL_CACHE
                SET strain_symbol=?,parent_ont_id=?,child_ont_ids=?,parent_sample_ids=?,child_sample_ids=?,has_parent_pheno_count=?,
                has_child_pheno_count=?,has_parent_sample_count=?,has_child_sample_count=?,has_phenominer=?,has_variant_visualizer=?,available_strain_id=?,available_strain_symbol=?
                Where strain_id=? and group_name=?
                """;
        return update(sql,hrdp.getStrainSymbol(),hrdp.getParentOntId(),hrdp.getChildOntIds(),hrdp.getParentSampleIds(),hrdp.getChildSampleIds(),hrdp.getHasParentPhenoCount(),hrdp.getHasChildPhenoCount(),hrdp.getHasParentSampleCount(),hrdp.getHasChildSampleCount(),hrdp.getHasPhenominer(),hrdp.getHasVariantVisualizer(),hrdp.getAvailableStrainId(),hrdp.getAvailableStrainSymbol(),hrdp.getStrainId(),hrdp.getGroupName());
    }

    public List<HrdpPortalCache> getHrdpStrainsByGroupName(String groupName) throws Exception{
        String sql = """
                Select * from HRDP_PORTAL_CACHE where group_name=? order by strain_symbol
                """;
        return HrdpPortalCacheQuery.execute(this,sql,groupName);
    }
   public boolean checkStrainExists(int strainId, String groupName) throws Exception{
        String sql = """
                SELECT COUNT(*) FROM hrdp_portal_cache WHERE strain_id = ? AND group_name = ?
                """;
        List<Integer> count = IntListQuery.execute(this,sql,strainId,groupName);
        return count.get(0).intValue()>0;
   }
   
    /**
     * Responsible for portals field in strain report pages
     * @param availStrainId
     * @return returns true if the strain id exists in available_strain_id column
     * @throws Exception
     */
    public boolean checkAvailableStrainExists(String availStrainId) throws Exception {

        String sql= """
                SELECT strain_id
                FROM HRDP_PORTAL_CACHE
               WHERE REGEXP_LIKE(available_strain_id, '(^|,)' || :availStrainId || '(,|$)')               
                """;

        List<String> strain = StringListQuery.execute(this,sql,availStrainId);
        return strain.size()>0;
    }
}
