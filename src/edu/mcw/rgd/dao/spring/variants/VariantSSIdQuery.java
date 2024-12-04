package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.variants.VariantSSId;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantSSIdQuery extends MappingSqlQuery {
    public VariantSSIdQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        VariantSSId obj = new VariantSSId();
        obj.setVariantRgdId(rs.getInt("VARIANT_RGD_ID"));
        obj.setSSId(rs.getString("SS_ID"));
        obj.setStrainRgdId(rs.getInt("STRAIN_RGD_ID"));
        return obj;
    }
}
