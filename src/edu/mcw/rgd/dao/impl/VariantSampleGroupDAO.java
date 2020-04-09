package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.VariantMapper;
import edu.mcw.rgd.datamodel.*;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;

/**
 * @author GKowalski
 * @since Aug 26, 2009
 */
public class VariantSampleGroupDAO extends AbstractDAO {

    public List<String> getAllSampleNames(int speciesTypeKey) throws Exception{
        String sql = "select distinct group_name from variant_sample_group";
        StringListQuery sl = new StringListQuery();
        return sl.execute(this.getDataSource(),sql);

    }

    public List<Integer> getVariantSamples(String groupName) throws Exception {
        String sql = "select * from variant_sample_group where group_name=" + groupName;
        IntListQuery il = new IntListQuery(this.getDataSource(),sql);
        return (List<Integer>) il.execute();

    }

}
