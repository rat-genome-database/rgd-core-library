package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.variants.VariantIndex;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


public class VariantIndexQuery extends MappingSqlQuery<VariantIndex> {
    public VariantIndexQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected VariantIndex mapRow(ResultSet rs, int rowNum) throws SQLException {

        VariantIndex vi = new VariantIndex();
        vi.setCategory("Variant");
        vi.setVariant_id(rs.getLong("rgd_id"));
        vi.setChromosome(rs.getString("chromosome"));
        vi.setPaddingBase(rs.getString("padding_base"));
        vi.setEndPos(rs.getLong("end_pos"));
        vi.setRefNuc(rs.getString("ref_nuc"));
        vi.setStartPos(rs.getLong("start_pos"));
        vi.setVariantType(rs.getString("variant_type"));
        vi.setVarNuc(rs.getString("var_nuc"));
        vi.setMapKey(rs.getInt("map_key"));
        try {
            vi.setRsId(rs.getString("rs_id"));
        }catch (Exception e){

        }
        return vi;
    }
}
