package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;

public class VariantDAO extends AbstractDAO {
    public void getVariantsBySampleId(int sampleId){
        String sql="select * from variant where rgd_id in (select rgd_id from variant_sample_detail where sample_id=?)";

    }
}
