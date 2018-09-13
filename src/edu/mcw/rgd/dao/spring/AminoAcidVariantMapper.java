package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.AminoAcidVariant;
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
        aav.setId(rs.getLong("VARIANT_TRANSCRIPT_ID"));
        aav.setVariantId(rs.getLong("VARIANT_ID"));
        aav.setTranscriptRGDId(rs.getInt("TRANSCRIPT_RGD_ID"));
        aav.setReferenceAminoAcid(rs.getString("REF_AA"));

        aav.setVariantAminoAcid(rs.getString("VAR_AA"));
        aav.setGeneSpliceStatus(rs.getString("GENESPLICE_STATUS"));
        aav.setPolyPhenStatus(rs.getString("POLYPHEN_STATUS"));
        aav.setSynonymousFlag(rs.getString("SYN_STATUS"));
        aav.setLocation(rs.getString("LOCATION_NAME"));
        aav.setTranscriptSymbol(rs.getString("ACC_ID"));
        aav.setNearSpliceSite(rs.getString("NEAR_SPLICE_SITE"));
        aav.setTripletError(rs.getString("TRIPLET_ERROR"));

        aav.setAASequence(rs.getString("full_ref_aa"));
        aav.setAaPosition(rs.getInt("full_ref_aa_pos"));
        aav.setDNASequence(rs.getString("full_ref_nuc"));
        aav.setDnaPosition(rs.getInt("full_ref_nuc_pos"));

        return aav;
    }
}

