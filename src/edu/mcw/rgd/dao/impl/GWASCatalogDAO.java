package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.GWASCatalogQuery;
import edu.mcw.rgd.datamodel.GWASCatalog;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.List;

public class GWASCatalogDAO extends AbstractDAO {
    public GWASCatalogDAO() {}

    public List<GWASCatalog> getFullCatalog() throws Exception {
        String query = "select * from GWAS_CATALOG";
        return GWASCatalogQuery.execute(this,query);
    }

    public int deleteGWASBatch(Collection<GWASCatalog> toBeDeleted) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),"DELETE FROM GWAS_CATALOG WHERE GWAS_ID=?",
                new int[] {Types.INTEGER});

        for(GWASCatalog gc : toBeDeleted)
            su.update(gc.getGwasId());

        return executeBatch(su);
    }
    public int insertGWASBatch(Collection<GWASCatalog> toBeInserted) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),"insert into gwas_catalog (gwas_id, pmid, disease_trait, init_sample_size, replicate_sample_size, region, chromosome, pos, report_genes, mapped_genes, snp_risk_allele, " +
                "snps, cur_snp_id, context, risk_allele_freq, p_value, p_value_mlog, snp_passing_qc, mapped_trait, efo_ids, study_acc, or_or_beta) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                        Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.FLOAT,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
        su.compile();
        for (GWASCatalog gc : toBeInserted){
            int gwasId = this.getNextKeyFromSequence("GWAS_CAT_SEQ");
            gc.setGwasId(gwasId);

            su.update(gc.getGwasId(),gc.getPmid(),gc.getDiseaseTrait(),gc.getInitialSample(), gc.getReplicateSample(),gc.getRegion(),gc.getChr(),gc.getPos(),
            gc.getReportedGenes(),gc.getMappedGene(),gc.getStrongSnpRiskallele(),gc.getSnps(),gc.getCurSnpId(),gc.getContext(),gc.getRiskAlleleFreq(),gc.getpValStr(),gc.getpValMlog(),
            gc.getSnpPassQc(),gc.getMapTrait(),gc.getEfoId(),gc.getStudyAcc(),gc.getOrBeta());
        }
        return executeBatch(su);
    }
    public int updateGWASBatch(Collection<GWASCatalog> toBeUpdated) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "update GWAS_CATALOG set VARIANT_RGD_ID=? where GWAS_ID=?",
                new int[]{Types.INTEGER, Types.INTEGER});
        su.compile();
        for (GWASCatalog gc : toBeUpdated){
            su.update(gc.getVariantRgdId(),gc.getGwasId());
        }
        return executeBatch(su);
    }
    
    public GWASCatalog getGWASCatalogByVariantRgdId(int rgdId) throws Exception{
        String sql = "select * from gwas_catalog where variant_rgd_id=?";
        GWASCatalogQuery q = new GWASCatalogQuery(DataSourceFactory.getInstance().getDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<GWASCatalog> list = q.execute(rgdId);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
}
