package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.RgdVariant;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 7, 2018
 * Time: 11:37:40 AM
 * <p>
 * query to return a row from Variants table; in addition it returns species type key
 */
public class RgdVariantQuery extends MappingSqlQuery {

    public RgdVariantQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        RgdVariant variant = new RgdVariant();

        variant.setRgdId(rs.getInt("rgd_id"));
        variant.setType(rs.getString("so_acc_id"));
        variant.setName(rs.getString("name"));
        variant.setDescription(rs.getString("description"));
        variant.setRefNuc(rs.getString("ref_nuc"));
        variant.setVarNuc(rs.getString("var_nuc"));
        variant.setNotes(rs.getString("notes"));
        variant.setSpeciesTypeKey(rs.getInt("species_type_key"));
        try {
            variant.setSoAccId(rs.getString("so_acc_id"));
        }catch (Exception e){}
        return variant;
    }

}
