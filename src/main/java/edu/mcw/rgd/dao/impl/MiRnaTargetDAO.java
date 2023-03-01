package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneQuery;
import edu.mcw.rgd.dao.spring.MiRnaTargetQuery;
import edu.mcw.rgd.dao.spring.MiRnaTargetStatQuery;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MiRnaTarget;
import edu.mcw.rgd.datamodel.MiRnaTargetStat;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 5/4/15
 * Time: 2:30 PM
 * <P>
 * API to access data in MIRNA_TARGETS table
 */
public class MiRnaTargetDAO extends AbstractDAO {

    /**
     * get gene targets for given miRna gene
     * @param miRnaRgdId rgd id for miRna gene
     * @return gene targets for given miRna gene
     * @throws Exception
     */
    public List<MiRnaTarget> getTargets(int miRnaRgdId) throws Exception {

        String sql = "SELECT m.* FROM mirna_targets m " +
                     "WHERE mirna_rgd_id=?";
        MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
        return execute(q, miRnaRgdId);
    }

    /**
     * get gene targets for given miRna gene;
     * note: if targetType parameter is null, return gene targets of any type
     * @param miRnaRgdId rgd id for miRna gene
     * @param targetType target type: 'predicted','confirmed', or null
     * @return gene targets for given miRna gene
     * @throws Exception
     */
    public List<MiRnaTarget> getTargets(int miRnaRgdId, String targetType) throws Exception {

        if( targetType==null ) {
            return getTargets(miRnaRgdId);
        } else {
            String sql = "SELECT m.* FROM mirna_targets m " +
                         "WHERE mirna_rgd_id=? AND target_type=?";
            MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
            return execute(q, miRnaRgdId, targetType);
        }
    }

    public List<Gene> getTargetGenes(String targetType, int speciesTypeKey) throws Exception {

        String sql = "SELECT g.* FROM genes g "+
                "WHERE rgd_id IN(SELECT gene_rgd_id FROM mirna_targets m,rgd_ids r " +
                "WHERE target_type=? AND gene_rgd_id=r.rgd_id AND r.species_type_key=?)";
        GeneQuery q = new GeneQuery(this.getDataSource(), sql);
        return execute(q, targetType, speciesTypeKey);
    }

    public List<Gene> getMiRnaGenes(String targetType, int speciesTypeKey) throws Exception {

        String sql = "SELECT g.* FROM genes g "+
                "WHERE rgd_id IN(SELECT mirna_rgd_id FROM mirna_targets m,rgd_ids r " +
                     "WHERE target_type=? AND mirna_rgd_id=r.rgd_id AND r.species_type_key=?)";
        GeneQuery q = new GeneQuery(this.getDataSource(), sql);
        return execute(q, targetType, speciesTypeKey);
    }

    public List<MiRnaTarget> getMiRnaGenes(int geneRgdId) throws Exception {

        String sql = "SELECT m.* FROM mirna_targets m " +
                     "WHERE gene_rgd_id=?";
        MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
        return execute(q, geneRgdId);
    }

    public List<MiRnaTarget> getMiRnaGenes(int geneRgdId, String targetType) throws Exception {

        String sql = "SELECT m.* FROM mirna_targets m " +
                "WHERE gene_rgd_id=? AND target_type=?";
        MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
        return execute(q, geneRgdId, targetType);
    }

    public List<MiRnaTarget> getData(int geneRgdId, int miRnaRgdId, String targetType) throws Exception {

        String sql = "SELECT m.* FROM mirna_targets m " +
                     "WHERE gene_rgd_id=? AND mirna_rgd_id=? AND target_type=?";
        MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
        return execute(q, geneRgdId, miRnaRgdId, targetType);
    }

    /**
     * get map of gene rgd ids to gene symbols for gene targets of given mirna gene
     * @param miRnaRgdId gene rgd id of mirna gene
     * @param targetType target type: 'predicted' or 'confirmed'
     * @return Map of gene rgd ids to gene symbols
     * @throws Exception
     */
    public Map<Integer, String> getSymbolsForMiRnaTargets(int miRnaRgdId, String targetType) throws Exception {
        String sql = "SELECT DISTINCT g.rgd_id,g.gene_symbol FROM genes g,mirna_targets t\n" +
                "WHERE gene_rgd_id=g.rgd_id AND mirna_rgd_id=? and target_type=?";
        StringMapQuery q = new StringMapQuery(this.getDataSource(), sql);

        Map<Integer, String> results = new HashMap<>();
        for(StringMapQuery.MapPair pair: (List<StringMapQuery.MapPair>)execute(q, miRnaRgdId, targetType) ) {
            results.put(Integer.parseInt(pair.keyValue), pair.stringValue);
        }
        return results;
    }

    public int getCountOfData(int speciesTypeKey) throws Exception {

        String sql = "SELECT COUNT(0) FROM mirna_targets m "+
            "WHERE EXISTS(SELECT 1 FROM rgd_ids r WHERE m.mirna_rgd_id=r.rgd_id AND species_type_key=?)";
        return getCount(sql, speciesTypeKey);
    }

    public List<MiRnaTarget> getDataModifiedBefore(int speciesTypeKey, Date cutOffDate) throws Exception {

        String sql = "SELECT m.* FROM mirna_targets m " +
                     "WHERE modified_date<? "+
                     "AND EXISTS(SELECT 1 FROM rgd_ids r WHERE m.mirna_rgd_id=r.rgd_id AND species_type_key=?)";
        MiRnaTargetQuery q = new MiRnaTargetQuery(this.getDataSource(), sql);
        return execute(q, cutOffDate, speciesTypeKey);
    }

    public int deleteDataModifiedBefore(int speciesTypeKey, Date cutOffDate) throws Exception {

        String sql = "DELETE FROM mirna_targets m WHERE modified_date<? "+
                " AND EXISTS(SELECT 1 FROM rgd_ids r WHERE m.mirna_rgd_id=r.rgd_id AND species_type_key=?)"+
                " AND ROWNUM<10000";
        int totalRowsAffected = 0, rowsAffected;
        do {
            rowsAffected = update(sql, cutOffDate, speciesTypeKey);
            totalRowsAffected += rowsAffected;
        }
        while( rowsAffected>0 );
        return totalRowsAffected;
    }

    public int updateModifiedDate(int key) throws Exception {

        String sql = "UPDATE mirna_targets SET modified_date=SYSDATE WHERE mirna_target_key=?";
        return update(sql, key);
    }

    public int updateModifiedDate(List<Integer> keys) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "UPDATE mirna_targets SET modified_date=SYSDATE WHERE mirna_target_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( Integer key: keys ) {
            su.update(key);
        }
        return executeBatch(su);
    }

    public int insert(List<MiRnaTarget> list) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "INSERT INTO mirna_targets "+
            "(mirna_target_key,gene_rgd_id,mirna_rgd_id,target_type,mirna_symbol,method_name,"+
            " result_type,data_type,support_type,pmid,created_date,modified_date,transcript_acc,"+
            " transcript_biotype,isoform,amplification,utr_start,utr_end,target_site,score,"+
            " normalized_score,energy) "+
            "VALUES(mirna_targets_seq.NEXTVAL,?,?,?,?,?, ?,?,?,?,SYSDATE,SYSDATE,?, ?,?,?,?,?,?,?, ?,?)",
                new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.DOUBLE,
                    Types.DOUBLE, Types.DOUBLE});
        su.compile();

        for( MiRnaTarget obj: list ) {
            su.update(obj.getGeneRgdId(), obj.getMiRnaRgdId(), obj.getTargetType(), obj.getMiRnaSymbol(),
                obj.getMethodName(), obj.getResultType(), obj.getDataType(), obj.getSupportType(),
                obj.getPmid(), obj.getTranscriptAcc(), obj.getTranscriptBioType(), obj.getIsoform(),
                obj.getAmplification(), obj.getUtrStart(), obj.getUtrEnd(), obj.getTargetSite(),
                obj.getScore(), obj.getNormalizedScore(), obj.getEnergy());
        }
        return executeBatch(su);
    }


    /*--- MIRNA TARGET STAT ---*/

    public List<MiRnaTargetStat> getStats(int rgdId) throws Exception {

        String sql = "SELECT * FROM mirna_target_stats WHERE rgd_id=?";
        MiRnaTargetStatQuery q = new MiRnaTargetStatQuery(this.getDataSource(), sql);
        return execute(q, rgdId);
    }

    public int updateStatsModifiedDate(List<Integer> keys) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "UPDATE mirna_target_stats SET last_modified_date=SYSDATE WHERE mirna_stat_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( Integer key: keys ) {
            su.update(key);
        }
        return executeBatch(su);
    }

    public int deleteStats(List<Integer> keys) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "DELETE FROM mirna_target_stats WHERE mirna_stat_key=?",
                new int[]{Types.INTEGER});
        su.compile();

        for( Integer key: keys ) {
            su.update(key);
        }
        return executeBatch(su);
    }

    public int insertStats(Collection<MiRnaTargetStat> list) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "INSERT INTO mirna_target_stats "+
                "(mirna_stat_key,rgd_id,stat_name,stat_value,created_date,last_modified_date)"+
                "VALUES(mirna_target_stats_seq.NEXTVAL,?,?,?,SYSDATE,SYSDATE)",
                new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
        su.compile();

        for( MiRnaTargetStat obj: list ) {
            su.update(obj.getRgdId(), obj.getName(), obj.getValue());
        }
        return executeBatch(su);
    }

    public int updateStats(Collection<MiRnaTargetStat> list) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "UPDATE mirna_target_stats "+
                "SET stat_value=?,last_modified_date=SYSDATE "+
                "WHERE mirna_stat_key=?",
                new int[]{Types.VARCHAR, Types.INTEGER});
        su.compile();

        for( MiRnaTargetStat obj: list ) {
            su.update(obj.getValue(), obj.getKey());
        }
        return executeBatch(su);
    }

    public List<MiRnaTargetStat> getStatsModifiedBefore(int speciesTypeKey, Date cutOffDate) throws Exception {

        String sql = "SELECT s.* FROM mirna_target_stats s WHERE last_modified_date<? "+
                "AND EXISTS(SELECT 1 FROM rgd_ids r WHERE s.rgd_id=r.rgd_id AND species_type_key=?)";
        MiRnaTargetStatQuery q = new MiRnaTargetStatQuery(this.getDataSource(), sql);
        return execute(q, cutOffDate, speciesTypeKey);
    }

    public int deleteStatsModifiedBefore(int speciesTypeKey, Date cutOffDate) throws Exception {

        String sql = "DELETE FROM mirna_target_stats s WHERE last_modified_date<? "+
                " AND EXISTS(SELECT 1 FROM rgd_ids r WHERE s.rgd_id=r.rgd_id AND species_type_key=?)"+
                " AND ROWNUM<10000";
        int totalRowsAffected = 0, rowsAffected;
        do {
            rowsAffected = update(sql, cutOffDate, speciesTypeKey);
            totalRowsAffected += rowsAffected;
        }
        while( rowsAffected>0 );
        return totalRowsAffected;
    }
}
