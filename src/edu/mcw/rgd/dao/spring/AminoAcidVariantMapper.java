package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.AminoAcidVariant;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 10/18/11
 * Time: 1:32 PM
 */
public class AminoAcidVariantMapper implements RowMapper<AminoAcidVariant> {

    public AminoAcidVariant mapRow(ResultSet rs, int rowNum) throws SQLException {

        AminoAcidVariant aav = new AminoAcidVariant();
        aav.setId(rs.getLong("TRANSCRIPT_RGD_ID"));
        aav.setVariantId(rs.getLong("RGD_ID"));
        aav.setTranscriptRGDId(rs.getInt("TRANSCRIPT_RGD_ID"));
        aav.setReferenceAminoAcid(rs.getString("REF_AA"));

        aav.setVariantAminoAcid(rs.getString("VAR_AA"));
      //  aav.setGeneSpliceStatus(rs.getString("GENESPLICE_STATUS"));
      //  aav.setPolyPhenStatus(rs.getString("POLYPHEN_STATUS"));
        aav.setPolyPhenStatus(rs.getString("PREDICTION"));
        aav.setSynonymousFlag(rs.getString("SYN_STATUS"));
        try {
            aav.setLocation(rs.getString("LOCATION_NAME"));
        }catch (Exception e){}
        aav.setTranscriptSymbol(rs.getString("ACC_ID"));
        try{
        aav.setNearSpliceSite(rs.getString("NEAR_SPLICE_SITE"));
        }catch (Exception e){}
        try {
            aav.setTripletError(rs.getString("TRIPLET_ERROR"));
        }catch (Exception e){}
        try {
            aav.setAASequence(rs.getString("full_ref_aa"));
        }catch (Exception e){}
        try{
        aav.setAaPosition(rs.getInt("full_ref_aa_pos"));
        }catch (Exception e){}
        try {
            aav.setDNASequence(rs.getString("full_ref_nuc"));
        }catch (Exception e){}
        try{
        aav.setDnaPosition(rs.getInt("full_ref_nuc_pos"));
        }catch (Exception e){}
        return aav;
    }
}

