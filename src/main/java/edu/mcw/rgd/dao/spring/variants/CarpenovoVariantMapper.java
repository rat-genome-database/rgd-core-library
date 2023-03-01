package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.VariantInfo;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarpenovoVariantMapper extends MappingSqlQuery {
    public CarpenovoVariantMapper(DataSource ds, String sql){
        super(ds, sql);
    }
    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        VariantInfo obj= new VariantInfo();
        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setObjectType(rs.getString("variant_type"));
        obj.setSymbol(rs.getString("rgd_id"));
        obj.setName(rs.getString("rgd_id"));


        try { obj.setSpeciesTypeKey(rs.getInt("species_type_key")); }catch (Exception ignored) { }
        try { obj.setObjectStatus(rs.getString("object_status")); }catch (Exception ignored) { }
        try { obj.setObjectKey(rs.getInt("object_key")); }catch (Exception ignored) { }

        return obj;
    }
}
