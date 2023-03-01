package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GeneVariation;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * mapping sql query to work with lists of GeneVariation objects
 */
public class GeneVariationQuery extends MappingSqlQuery {

    public GeneVariationQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeneVariation obj = new GeneVariation();

        obj.setKey(rs.getInt("gene_key"));
        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setGeneVariationType(rs.getString("gene_variation_type"));
        obj.setVariationKey(rs.getInt("variation_key"));
        obj.setVariationRgdId(rs.getInt("variation_rgd_id"));
        obj.setNotes(rs.getString("notes"));

        try {
            obj.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored) {

        }

        return obj;
    }

}
