package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.HrdpPortalCache;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HrdpPortalCacheQuery extends MappingSqlQuery {

    public HrdpPortalCacheQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        HrdpPortalCache hrdp = new HrdpPortalCache();
        hrdp.setStrainId(rs.getInt("STRAIN_ID"));
        hrdp.setStrainSymbol(rs.getString("STRAIN_SYMBOL"));
        hrdp.setGroupName(rs.getString("GROUP_NAME"));
        hrdp.setParentOntId(rs.getString("PARENT_ONT_ID"));
        hrdp.setChildOntIds(rs.getString("CHILD_ONT_IDS"));
        hrdp.setParentSampleIds(rs.getString("PARENT_SAMPLE_IDS"));
        hrdp.setChildSampleIds(rs.getString("CHILD_SAMPLE_IDS"));
        hrdp.setHasParentPhenoCount(rs.getInt("HAS_PARENT_PHENO_COUNT"));
        hrdp.setHasChildPhenoCount(rs.getInt("HAS_CHILD_PHENO_COUNT"));
        hrdp.setHasParentSampleCount(rs.getInt("HAS_PARENT_SAMPLE_COUNT"));
        hrdp.setHasChildSampleCount(rs.getInt("HAS_CHILD_SAMPLE_COUNT"));
        hrdp.setHasPhenominer(rs.getInt("HAS_PHENOMINER"));
        hrdp.setHasVariantVisualizer(rs.getInt("HAS_VARIANT_VISUALIZER"));
        return hrdp;
    }

    public static List<HrdpPortalCache> execute(AbstractDAO dao, String sql, Object... params) throws  Exception {
        HrdpPortalCacheQuery q = new HrdpPortalCacheQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }

}
