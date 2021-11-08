package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SyntenyQuery;
import edu.mcw.rgd.datamodel.SyntenicRegion;

import java.util.List;

/**
 * User: mtutaj
 * Date: Nov 08, 2021
 */
public class SyntenyDAO extends AbstractDAO {

    public List<SyntenicRegion> getBlocks(int backboneMapKey, String chromosome, int start, int stop, int mapKey) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? " +
                "ORDER BY chromosome2, start_pos2";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome);
    }

    public List<SyntenicRegion> getBlocks(int backboneMapKey, String chromosome, int start, int stop, int mapKey, int chainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level=?" +
                "ORDER BY chromosome2, start_pos2";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, chainLevel);
    }
}
