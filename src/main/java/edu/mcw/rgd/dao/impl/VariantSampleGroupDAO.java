package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;

import java.util.*;

public class VariantSampleGroupDAO extends AbstractDAO {

    public List<String> getAllSampleNames(int speciesTypeKey) throws Exception{
        String sql = "select distinct group_name from variant_sample_group";
        StringListQuery sl = new StringListQuery(this.getDataSource(),sql);
        return sl.execute();
    }

    public List<Integer> getVariantSamples(String groupName) throws Exception {
        String sql = "select * from variant_sample_group where group_name='" + groupName + "'";
        IntListQuery il = new IntListQuery(this.getDataSource(),sql);
        return il.execute();
    }

}
