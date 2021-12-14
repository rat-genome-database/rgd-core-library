package edu.mcw.rgd.dao.spring;


import edu.mcw.rgd.datamodel.Variant;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since 10/13/11
 */
public class VariantMapper extends MappingSqlQuery {

    public VariantMapper(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return mapRow(rs);
    }

    static public Variant mapRow(ResultSet rs) throws SQLException {
        Variant v = new Variant();
        v.setId(rs.getLong("RGD_ID"));
        v.setChromosome(rs.getString("CHROMOSOME"));
        v.setSampleId(rs.getInt("SAMPLE_ID"));
        v.setStartPos(rs.getLong("START_POS"));
        v.setEndPos(rs.getLong("END_POS"));
        v.setVariantFrequency(rs.getInt("VAR_FREQ"));
        v.setDepth(rs.getInt("TOTAL_DEPTH"));
        v.setQualityScore(rs.getInt("QUALITY_SCORE"));
        v.setReferenceNucleotide(rs.getString("REF_NUC"));
        v.setVariantNucleotide(rs.getString("VAR_NUC"));
        v.setZygosityStatus(rs.getString("ZYGOSITY_STATUS"));
        v.setZygosityInPseudo(rs.getString("ZYGOSITY_IN_PSEUDO"));
        v.setZygosityNumberAllele(rs.getInt("ZYGOSITY_NUM_ALLELE"));
        v.setZygosityPercentRead(rs.getInt("ZYGOSITY_PERCENT_READ"));
        v.setZygosityPossibleError(rs.getString("ZYGOSITY_POSS_ERROR"));
        v.setZygosityRefAllele(rs.getString("ZYGOSITY_REF_ALLELE"));
        v.setGenicStatus(rs.getString("GENIC_STATUS"));
   //     v.setHgvsName(rs.getString("HGVS_NAME"));
        v.setRgdId(rs.getInt("RGD_ID"));
        v.setVariantType(rs.getString("VARIANT_TYPE"));
        v.setPaddingBase(rs.getString("PADDING_BASE"));
        try{
            v.setRegionName(rs.getString("GENE_SYMBOL"));
        }catch(SQLException e){
            v.setRegionName(null);
        }
        return v;
    }
}
