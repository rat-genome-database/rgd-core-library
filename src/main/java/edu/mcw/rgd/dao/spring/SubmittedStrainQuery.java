package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.models.SubmittedStrain;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 7/28/2016.
 */
public class SubmittedStrainQuery extends MappingSqlQuery {

    public SubmittedStrainQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected SubmittedStrain mapRow(ResultSet rs, int i) throws SQLException {
        SubmittedStrain s= new SubmittedStrain();
        s.setSubmittedStrainKey(rs.getInt("submitted_strain_key"));
        s.setStrainSymbol(rs.getString("strain_symbol"));
        s.setStrainType(rs.getString("strain_type"));
        s.setGeneticStatus(rs.getString("genetic_status"));
        s.setBackgroundStrain(rs.getString("background_strain"));
        s.setMethod(rs.getString("modification_method"));
        s.setOrigin(rs.getString("origin"));
        s.setSource(rs.getString("source"));
        s.setNotes(rs.getString("notes"));
        s.setResearchUse(rs.getString("research_use"));
        s.setIlarCode(rs.getString("ILAR_CODE"));
       // s.setIsAvailable(rs.getString("IS_AVAILABLE"));
        s.setAvailabilityContactEmail(rs.getString("AVAILABILTIY_CONTACT_EMAIL"));
        s.setAvailabilityContactUrl(rs.getString("AVAILABILTTY_CONTACT_URL"));
        s.setGeneSymbol(rs.getString("gene_symbol"));
        s.setAlleleSymbol(rs.getString("allele_symbol"));
        s.setGeneRgdId(rs.getInt("gene_rgd_id"));
        s.setAlleleRgdId(rs.getInt("allele_rgd_id"));
        s.setLastName(rs.getString("last_name"));
        s.setFirstName(rs.getString("first_name"));
        s.setEmail(rs.getString("email"));
        s.setPiName(rs.getString("pi"));
        s.setOrganization(rs.getString("organization"));
        s.setCreated_date(rs.getDate("created_date"));
        s.setLast_updated_date(rs.getDate("last_updated_date"));
        s.setModifiedBy(rs.getString("modified_by"));
        s.setApprovalStatus(rs.getString("approval_status"));
        s.setReference(rs.getString("reference_id"));
        s.setDisplayStatus(rs.getString("display_status"));
        s.setPiEmail(rs.getString("pi_email"));
        s.setImageUrl(rs.getString("image_url"));
        s.setStrainRgdId(rs.getInt("strain_rgd_id"));
        return s;

    }
}
