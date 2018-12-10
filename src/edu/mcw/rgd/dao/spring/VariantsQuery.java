package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Variants;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 7, 2018
 * Time: 11:37:40 AM
 * <p>
 * query to return a row from Variants table; in addition it returns species type key
 */
public class VariantsQuery extends MappingSqlQuery {

    public VariantsQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Variants variants = new Variants();

        variants.setRgdId(rs.getInt("rgd_id"));
        variants.setType(rs.getString("so_acc_id"));
        variants.setName(rs.getString("name"));
        variants.setDescription(rs.getString("description"));
        variants.setRef_nuc(rs.getString("ref_nuc"));
        variants.setVar_nuc(rs.getString("var_nuc"));
        variants.setNotes(rs.getString("notes"));
        variants.setSpeciesTypeKey(rs.getInt("species_type_key"));
        return variants;
    }

}
