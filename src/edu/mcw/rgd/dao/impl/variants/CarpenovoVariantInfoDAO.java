package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.VariantInfoDAO;
import edu.mcw.rgd.dao.spring.VariantQuery;
import edu.mcw.rgd.dao.spring.variants.CarpenovoVariantMapper;
import edu.mcw.rgd.datamodel.VariantInfo;

import java.util.List;

public class CarpenovoVariantInfoDAO extends AbstractDAO {
    public VariantInfo getVariant(int rgdId) throws Exception {

        String query = "SELECT * FROM variant v inner join variant_map_data vmd on vmd.rgd_id=v.rgd_id"+
                " WHERE v.rgd_id=? ";
        CarpenovoVariantMapper q = new CarpenovoVariantMapper(DataSourceFactory.getInstance().getCarpeNovoDataSource(), query);

        List<VariantInfo> rows = execute(q, rgdId);
        if( rows.isEmpty() ) {
            return null;
        }
        return rows.get(0);
    }
}
