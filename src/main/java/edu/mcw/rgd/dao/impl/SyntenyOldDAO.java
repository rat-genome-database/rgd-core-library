package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SyntenyQuery;
import edu.mcw.rgd.datamodel.SyntenicRegion;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
 */
public class SyntenyOldDAO extends AbstractDAO {


    public List<SyntenicRegion> getAll(int backboneMapKey) throws Exception {
        String query = "select * from synteny where backbone_map_key=?";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey);
    }

    public List<SyntenicRegion> get(int backboneMapKey, String chromosome, int start, int stop, int mapKey) throws Exception {
        String query = "select * from synteny where backbone_map_key=? and map_key=? " +
                " and backbone_start_pos<? and backbone_stop_pos > ? and backbone_chr=? order by chr, start_pos";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome);
    }
}
