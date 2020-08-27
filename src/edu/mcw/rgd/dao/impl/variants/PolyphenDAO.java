package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.PolyPhenMapper;
import edu.mcw.rgd.datamodel.prediction.PolyPhenPrediction;

import java.util.List;

public class PolyphenDAO extends AbstractDAO {
    public List<PolyPhenPrediction> getPloyphenDataByVariantId(int variantId) throws Exception{
        String sql="select * from polyphen where variant_rgd_id=?";
        PolyPhenMapper query=new PolyPhenMapper(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        return execute(query, variantId);
    }
}
