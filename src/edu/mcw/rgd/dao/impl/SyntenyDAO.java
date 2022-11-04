package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SyntenyQuery;
import edu.mcw.rgd.datamodel.SyntenicRegion;
import edu.mcw.rgd.process.mapping.MapManager;

import java.util.List;

/**
 * User: mtutaj
 * Date: Nov 08, 2021
 */
public class SyntenyDAO extends AbstractDAO {

    public List<SyntenicRegion> getBlocks(int backboneMapKey, String chromosome, int start, int stop, int mapKey) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome);
    }

    public List<SyntenicRegion> getBlocks(int backboneMapKey, String chromosome, int start, int stop, int mapKey, int chainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, chainLevel);
    }

    public List<SyntenicRegion> getBlocks(int backboneMapKey, String chromosome, int start, int stop, int mapKey, int minChainLevel, int maxChainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level BETWEEN ? and ? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, minChainLevel, maxChainLevel);
    }


    public List<SyntenicRegion> getSizedBlocks(int backboneMapKey, String chromosome, int start, int stop, int backboneBlockMinSize, int mapKey) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, backboneBlockMinSize);
    }

    public List<SyntenicRegion> getSizedBlocks(int backboneMapKey, String chromosome, int start, int stop, int backboneBlockMinSize, int mapKey, int chainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level=? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, chainLevel, backboneBlockMinSize);
    }

    public List<SyntenicRegion> getSizedBlocks(int backboneMapKey, String chromosome, int start, int stop, int backboneBlockMinSize, int mapKey, int minChainLevel, int maxChainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level BETWEEN ? and ? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, minChainLevel, maxChainLevel, backboneBlockMinSize);
    }


    public List<SyntenicRegion> getGaps(int backboneMapKey, String chromosome, int start, int stop, int mapKey) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome);
    }

    public List<SyntenicRegion> getGaps(int backboneMapKey, String chromosome, int start, int stop, int mapKey, int chainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, chainLevel);
    }

    public List<SyntenicRegion> getGaps(int backboneMapKey, String chromosome, int start, int stop, int mapKey, int minChainLevel, int maxChainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level BETWEEN ? and ? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, minChainLevel, maxChainLevel);
    }


    public List<SyntenicRegion> getSizedGaps(int backboneMapKey, String chromosome, int start, int stop, int backboneGapMinSize, int mapKey) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, backboneGapMinSize);
    }

    public List<SyntenicRegion> getSizedGaps(int backboneMapKey, String chromosome, int start, int stop, int backboneGapMinSize, int mapKey, int chainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level=? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, chainLevel, backboneGapMinSize);
    }

    public List<SyntenicRegion> getSizedGaps(int backboneMapKey, String chromosome, int start, int stop, int backboneGapMinSize, int mapKey, int minChainLevel, int maxChainLevel) throws Exception {
        String query = "SELECT * FROM synteny_ucsc_gaps WHERE map_key1=? AND map_key2=? " +
                " AND start_pos1<? AND stop_pos1 > ? AND chromosome1=? AND chain_level BETWEEN ? and ? AND pos_len1>=? " +
                "ORDER BY chromosome1, start_pos1";
        SyntenyQuery q = new SyntenyQuery(this.getDataSource(), query);
        return execute(q, backboneMapKey, mapKey, stop, start, chromosome, minChainLevel, maxChainLevel, backboneGapMinSize);
    }
}
