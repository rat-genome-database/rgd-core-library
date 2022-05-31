package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.datamodel.VariantResult;
import edu.mcw.rgd.datamodel.VariantResultBuilder;
import edu.mcw.rgd.datamodel.VariantSearchBean;
import edu.mcw.rgd.datamodel.variants.VariantTranscript;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class VariantDAO extends AbstractDAO {

    public int getMapKeyByVariantId(int variantId) throws Exception {
        String sql="select distinct(vmd.map_key) from variant_map_data vmd where rgd_id=? ";
        IntListQuery q=new IntListQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        List<Integer> results= execute(q, variantId);
        if(results!=null && results.size()>0) return results.get(0);
        return 0;
    }


}
