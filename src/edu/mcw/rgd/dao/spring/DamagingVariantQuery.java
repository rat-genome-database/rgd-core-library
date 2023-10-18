package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.DamagingVariant;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DamagingVariantQuery extends MappingSqlQuery {
    public DamagingVariantQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        DamagingVariant v = new DamagingVariant();

//        v.setRgdId(rs.getInt("VAR_RGD_ID"));
//        v.setRefNuc(rs.getString("REF_NUC"));
//        v.setVariantType(rs.getString("VARIANT_TYPE"));
//        v.setVarNuc(rs.getString("VAR_NUC"));
//        v.setRsId(rs.getString("RS_ID"));
//        v.setClinvarId(rs.getString("CLINVAR_ID"));
//        v.setSpeciesTypeKey(rs.getInt("SPECIES_TYPE_KEY"));
//        v.setGeneSymbol(rs.getString("GENE_SYMBOL"));
//        v.setChromosome(rs.getString("CHROMOSOME"));
//        v.setPaddingBase(rs.getString("PADDING_BASE"));
//        v.setEndPos(rs.getInt("END_POS"));
//        v.setStartPos(rs.getInt("START_POS"));
//        v.setGenicStatus(rs.getString("GENIC_STATUS"));
//        v.setMapKey(rs.getInt("MAP_KEY"));

        v.setRgdId(rs.getInt("VAR_RGD_ID"));
        v.setChromosome(rs.getString("CHROMOSOME"));
        v.setSampleId(rs.getInt("SAMPLE_ID"));
        v.setStartPos(rs.getInt("START_POS"));
        v.setEndPos(rs.getInt("END_POS"));
        v.setVariantFrequency(rs.getInt("VAR_FREQ"));
        v.setDepth(rs.getInt("TOTAL_DEPTH"));
        v.setQualityScore(rs.getInt("QUALITY_SCORE"));
        v.setRefNuc(rs.getString("REF_NUC"));
        v.setVarNuc(rs.getString("VAR_NUC"));
        v.setZygosityStatus(rs.getString("ZYGOSITY_STATUS"));
        v.setZygosityInPseudo(rs.getString("ZYGOSITY_IN_PSEUDO"));
        v.setZygosityNumberAllele(rs.getInt("ZYGOSITY_NUM_ALLELE"));
        v.setZygosityPercentRead(rs.getInt("ZYGOSITY_PERCENT_READ"));
        v.setZygosityPossibleError(rs.getString("ZYGOSITY_POSS_ERROR"));
        v.setZygosityRefAllele(rs.getString("ZYGOSITY_REF_ALLELE"));
        v.setGenicStatus(rs.getString("GENIC_STATUS"));
        //v.setHgvsName(rs.getString("HGVS_NAME"));
        v.setVariantType(rs.getString("VARIANT_TYPE"));
        v.setPaddingBase(rs.getString("PADDING_BASE"));
        try{
            v.setGeneSymbol(rs.getString("GENE_SYMBOL"));
        }catch(SQLException e){
            v.setGeneSymbol(null);
        }

        return v;
    }

    public static List<DamagingVariant> execute(AbstractDAO dao, DataSource src, String sql, Object... params) throws Exception {
        DamagingVariantQuery dvq = new DamagingVariantQuery(src,sql);
        return dao.execute(dvq,params);
    }
}
