package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.variants.VariantTranscript;
import org.springframework.jdbc.object.MappingSqlQuery;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantTranscriptQuery extends MappingSqlQuery<VariantTranscript> {
    public VariantTranscriptQuery(DataSource ds, String query) {
        super(ds, query);
    }
    @Override
    protected VariantTranscript mapRow(ResultSet rs, int i) throws SQLException {
        VariantTranscript t= new VariantTranscript();

        t.setFullRefAAPos(rs.getInt("full_ref_aa_pos"));
        t.setFullRefNucPos(rs.getInt("full_ref_nuc_pos"));
        t.setTranscriptRgdId(rs.getInt("transcript_rgd_id"));
        t.setRefAA(rs.getString("ref_aa"));
        t.setVarAA(rs.getString("var_aa"));
     //   t.setGenespliceStatus(rs.getString("genespliceStatus"));
        t.setLocationName(rs.getString("location_name"));
     ///   t.setPolyphenStatus(rs.getString("polyphen_status"));
        t.setSynStatus(rs.getString("syn_status"));
        t.setNearSpliceSite(rs.getString("near_splice_site"));
        t.setFullRefNucSeqKey(rs.getInt("full_ref_nuc_seq_key"));
        t.setFullRefAASeqKey(rs.getInt("full_ref_aa_seq_key"));
        t.setTripletError(rs.getString("triplet_error"));
        t.setFrameShift(rs.getString("frameshift"));
        t.setPolyphenStatus(rs.getString("PREDICTION"));
        try{
            t.setTranscriptSymbol(rs.getString("acc_id"));
        }catch (Exception ignored){}
        try{
            t.setProteinSymbol(rs.getString("protein_acc_id"));
        }catch (Exception ignored){}
        return t;

    }
}
