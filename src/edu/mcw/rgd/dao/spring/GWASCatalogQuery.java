package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Eva;
import edu.mcw.rgd.datamodel.GWASCatalog;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GWASCatalogQuery extends MappingSqlQuery {

    public GWASCatalogQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        GWASCatalog gc = new GWASCatalog();
        gc.setGwasId(rs.getInt("GWAS_ID"));
        gc.setPmid(rs.getString("PMID"));
        gc.setDiseaseTrait(rs.getString("DISEASE_TRAIT"));
        gc.setInitialSample(rs.getString("INIT_SAMPLE_SIZE"));
        gc.setReplicateSample(rs.getString("REPLICATE_SAMPLE_SIZE"));
        gc.setRegion(rs.getString("REGION"));
        gc.setChr(rs.getString("CHROMOSOME"));
        gc.setPos(Integer.getInteger(rs.getString("POS") ) );
        gc.setReportedGenes(rs.getString("REPORT_GENES"));
        gc.setMappedGene(rs.getString("MAPPED_GENES"));
        gc.setStrongSnpRiskallele(rs.getString("SNP_RISK_ALLELE"));
        gc.setSnps(rs.getString("SNPS"));
        gc.setCurSnpId(rs.getString("CUR_SNP_ID"));
        gc.setContext(rs.getString("CONTEXT"));
        gc.setRiskAlleleFreq(rs.getString("RISK_ALLELE_FREQ"));
        gc.setpVal(rs.getString("P_VALUE"));
        gc.setpValMlog(rs.getString("P_VALUE_MLOG"));
        gc.setSnpPassQc(rs.getString("SNP_PASSING_QC"));
        gc.setMapTrait(rs.getString("MAPPED_TRAIT"));
        gc.setEfoId(rs.getString("EFO_IDS"));
        gc.setStudyAcc(rs.getString("STUDY_ACC"));
        return gc;
    }
    public static List<GWASCatalog> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GWASCatalogQuery q = new GWASCatalogQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
