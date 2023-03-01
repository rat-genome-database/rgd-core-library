package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.variants.SampleManager;
import edu.mcw.rgd.datamodel.variants.VariantObject;
import edu.mcw.rgd.datamodel.variants.VariantSampleDetail;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantQuery  extends MappingSqlQuery {
    public VariantQuery (DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        VariantObject obj=new VariantObject();
        obj.setId(rs.getInt("rgd_id"));
        obj.setRsId(rs.getString("rs_id"));
        obj.setReferenceNucleotide(rs.getString("ref_nuc"));
        obj.setVariantNucleotide(rs.getString("var_nuc"));
        obj.setVariantType(rs.getString("variant_type"));
        obj.setClinvarId(rs.getString("clinvar_id"));
        obj.setSpeciesTypeKey(rs.getInt("species_type_key"));
        obj.setChromosome(rs.getString("chromosome"));
        obj.setPaddingBase(rs.getString("padding_base"));
        obj.setStartPos(rs.getLong("start_pos"));
        obj.setEndPos(rs.getLong("end_pos"));
        obj.setGenicStatus(rs.getString("genic_status"));
        obj.setMapKey(rs.getInt("map_key"));
        obj.setPolyphenStatus(rs.getString("PREDICTION"));
        obj.setConScore(rs.getBigDecimal("SCORE"));

        obj.setFullRefAAPos(rs.getInt("full_ref_aa_pos"));
        obj.setFullRefNucPos(rs.getInt("full_ref_nuc_pos"));
        obj.setTranscriptRgdId(rs.getInt("transcript_rgd_id"));
        obj.setRefAA(rs.getString("ref_aa"));
        obj.setVarAA(rs.getString("var_aa"));
     //   obj.setGenespliceStatus(rs.getString("genespliceStatus"));
        obj.setLocationName(rs.getString("location_name"));
        obj.setSynStatus(rs.getString("syn_status"));
        obj.setNearSpliceSite(rs.getString("near_splice_site"));
        obj.setFullRefNucSeqKey(rs.getInt("full_ref_nuc_seq_key"));
        obj.setFullRefAASeqKey(rs.getInt("full_ref_aa_seq_key"));
        obj.setTripletError(rs.getString("triplet_error"));
        obj.setFrameShift(rs.getString("frameshift"));
        VariantSampleDetail vsd=new VariantSampleDetail();
        vsd.setId(rs.getInt("rgd_id"));
        vsd.setSource(rs.getString("source"));
        vsd.setSampleId(rs.getInt("sample_id"));
        vsd.setDepth(rs.getInt("total_depth"));
        vsd.setVariantFrequency(rs.getInt("var_freq"));
        vsd.setZygosityStatus(rs.getString("zygosity_status"));
        vsd.setZygosityPercentRead(rs.getInt("zygosity_percent_read"));
        vsd.setZygosityPossibleError(rs.getString("zygosity_poss_error"));
        vsd.setZygosityRefAllele(rs.getString("zygosity_ref_allele"));
        vsd.setZygosityNumberAllele(rs.getInt("zygosity_num_allele"));
        vsd.setZygosityInPseudo(rs.getString("zygosity_in_pseudo"));
        vsd.setQualityScore(rs.getInt("quality_score"));
        try {
            vsd.setAnalysisName(SampleManager.getInstance().getSampleName(rs.getInt("sample_id")).getAnalysisName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.setVsd(vsd);
        return obj;
    }
}
