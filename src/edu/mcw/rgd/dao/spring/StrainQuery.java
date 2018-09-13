package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Strain;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 */
public class StrainQuery extends MappingSqlQuery {

    public StrainQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Strain st = new Strain();

        st.setKey(rs.getInt("strain_key"));
        st.setSymbol(rs.getString("strain_symbol"));
        st.setName(rs.getString("full_name"));
        st.setStrain(rs.getString("strain"));
        st.setSubstrain(rs.getString("substrain"));
        st.setGenetics(rs.getString("genetics"));
        st.setInbredGen(rs.getString("inbred_gen"));
        st.setOrigin(rs.getString("origin"));
        st.setColor(rs.getString("color"));
        st.setChrAltered(rs.getString("chr_altered"));
        st.setSource(rs.getString("source"));
        st.setNotes(rs.getString("notes"));
        st.setRgdId(rs.getInt("rgd_id"));
        st.setStrainTypeName(rs.getString("strain_type_name_lc"));
        st.setSpeciesTypeKey(rs.getInt("species_type_key"));
        st.setImageUrl(rs.getString("image_url"));
        st.setResearchUse(rs.getString("research_use"));
        st.setGeneticStatus(rs.getString("genetic_status"));
        st.setBackgroundStrainRgdId(rs.getInt("background_strain_rgd_id"));
        if( rs.wasNull() ) {
            st.setBackgroundStrainRgdId(null);
        }
        st.setModificationMethod(rs.getString("modification_method"));

        return st;
    }

}
