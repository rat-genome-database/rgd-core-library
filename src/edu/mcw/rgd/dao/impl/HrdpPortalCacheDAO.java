package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.HrdpPortalCacheQuery;
import edu.mcw.rgd.datamodel.HrdpPortalCache;

import java.util.List;

public class HrdpPortalCacheDAO extends AbstractDAO {

    public int insertHrdpPortalData(HrdpPortalCache hrdp) throws Exception{
        String sql= """
                INSERT INTO HRDP_PORTAL_CACHE(strain_id,strain_symbol,group_name,parent_ont_id,child_ont_ids,parent_sample_ids,child_sample_ids,has_parent_pheno_count,has_child_pheno_count,has_parent_sample_count,has_child_sample_count,has_phenominer,has_variant_visualizer)
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        return update(sql,hrdp.getStrainId(),hrdp.getStrainSymbol(),hrdp.getGroupName(),hrdp.getParentOntId(),hrdp.getChildOntIds(),hrdp.getParentSampleIds(),hrdp.getChildSampleIds(),hrdp.getHasParentPhenoCount(),hrdp.getHasChildPhenoCount(),hrdp.getHasParentSampleCount(),hrdp.getHasChildSampleCount(),hrdp.getHasPhenominer(),hrdp.getHasVariantVisualizer());
    }

    public List<HrdpPortalCache> getHrdpStrainsByGroupName(String groupName) throws Exception{
        String sql = """
                Select * from HRDP_PORTAL_CACHE where group_name=?
                """;
        return HrdpPortalCacheQuery.execute(this,sql,groupName);
    }
}
