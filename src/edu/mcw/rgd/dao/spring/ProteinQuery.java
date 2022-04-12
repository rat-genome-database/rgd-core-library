package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Protein;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Returns a row from the PROTEINS table
 */
public class ProteinQuery extends MappingSqlQuery {

    public ProteinQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Protein obj = new Protein();
        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setSymbol(rs.getString("protein_symbol"));
        obj.setName(rs.getString("protein_name"));
        obj.setUniprotId(rs.getString("uniprot_id"));
        obj.setSrcPipeline(rs.getString("src_pipeline"));
        obj.setCreatedDate(rs.getDate("created_date"));

        try {
            obj.setSpeciesTypeKey(rs.getInt("species_type_key"));
            obj.setCanonical(rs.getInt("is_canonical")!=0);
        }catch (Exception ignored) {
        }
        return obj;
    }

}
