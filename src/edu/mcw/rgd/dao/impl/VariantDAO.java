package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.VariantMapper;
import edu.mcw.rgd.dao.spring.variants.VariantMapQuery;
import edu.mcw.rgd.dao.spring.variants.VariantSampleQuery;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.variants.VariantMapData;
import edu.mcw.rgd.datamodel.variants.VariantSampleDetail;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;
import java.util.Map;

/**
 * @author GKowalski
 * @since Aug 26, 2009
 */
public class VariantDAO extends JdbcBaseDAO {

    public static String lastQuery = "";

    public String getVariantTable(int sampleId) {
        if( sampleId<100 ) {
            return "variant_clinvar";
        }
        if( sampleId>=6000 && sampleId<=6999 ) {
            return "variant_dog";
        }
        return "variant";
    }

    public String getVariantTranscriptTable(int sampleId) {
        if( sampleId<100 ) {
            return "variant_transcript_clinvar";
        }
        if( sampleId>=6000 && sampleId<=6999 ) {
            return "variant_transcript_dog";
        }

        return "variant_transcript";
    }
    public String getPolyphenTable(int sampleId) {

        if( sampleId>=6000 && sampleId<=6999 ) {
            return "polyphen_dog";
        }

        return "polyphen";
    }
    /**
     * @param vsb
     * @return
     */
    public List<VariantResult> getVariantResults(VariantSearchBean vsb) {

        if (vsb.getPositionSQL().equals("") && vsb.getGenesSQL().equals("") && vsb.getVariantIdSql().equals("")) {
            return new ArrayList<VariantResult>();
        }

        String sql = " ";
        String sqlFrom = "select ";

        sql += vsb.getVariantTable(vsb.getMapKey()) + " v ";
        sqlFrom += "v.* ";
        String[] results = vsb.getTableJoinSQL(sqlFrom,false);
        sql += results[1];
        sqlFrom = results[0] + " from \n";  // We're done updating the sql From clause

        sql += vsb.getPositionSQL();
        sql += vsb.getDepthSQL();
        sql += vsb.getSampleSQL();
        sql += vsb.getGenicStatusSQL();
        sql += vsb.getAAChangeSQL();
        sql += vsb.getZygositySQL();
        sql += vsb.getLocationSQL();
        sql += vsb.getNearSpliceSiteSQL();
        sql += vsb.getIsPrematureStopSQL();
        sql += vsb.getIsReadthroughSQL();
        sql += vsb.getPolyphenSQL();
        sql += vsb.getConScoreSQL();
        sql += vsb.getClinicalSignificanceSQL();
        sql += vsb.getDBSNPSQL();
        sql += vsb.getAlleleCountSQL();
        sql += vsb.getPsudoautosomalSQL();
        sql += vsb.getGenesSQL();
        sql += vsb.getReadDepthSql();
        sql += vsb.getVariantIdSql();
        sql += vsb.getVariantTypeSQL();
        sql += vsb.getIsFrameshiftSQL();

        sql += "    order by v.start_pos ";
        sql = sqlFrom + sql;

        logger.debug("Running Search SQL getVariantResults() : \n" + sql + "\n");

        List<VariantResult> vrList = new ArrayList<VariantResult>();
        Connection conn = null;
        Statement stmt;
        ResultSet rs;

        try {

            conn = this.getDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            long lastVariant = 0;

            VariantResultBuilder vrb = null;

            while (rs.next()) {
                long variantId = rs.getLong("variant_id");
                if (variantId != lastVariant) {
                    if (lastVariant != 0) {
                        vrList.add(vrb.getVariantResult());
                    }
                    lastVariant = variantId;
                    vrb = new VariantResultBuilder();

                    vrb.mapVariant(rs);
                    vrb.mapConservation(rs);
                }

                vrb.mapTranscript(rs);
                vrb.mapPolyphen(rs);
                vrb.mapDBSNP(rs, vsb);
                vrb.mapClinVar(rs);
            }

            if (vrb != null) {
                vrList.add(vrb.getVariantResult());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
                logger.error("Exception closing connection");
            }
        }

        VariantDAO.lastQuery = sql;
        logger.debug("found vrList size " + vrList.size());
        return vrList;

    }


    /**
     * Return count of variants based on settings in VariantSearchBean
     *
     * @param vsb
     * @return
     */
    public int getVariantCount(VariantSearchBean vsb) {
        logger.debug("here2");

        if (vsb.getPositionSQL().equals("") && vsb.getGenesSQL().equals("")) {
            return 0;
        }

        String sql = " ";
        String sqlFrom = "select ";

        sql += vsb.getVariantTable(vsb.getMapKey()) + " v ";
        sqlFrom += " count(*) as count ";
        String[] results = vsb.getTableJoinSQL(sqlFrom, true);
        sql += results[1];
        //sqlFrom = results[0] + " from \n";  // We're done updating the sql From clause
        sqlFrom += " from ";  // We're done updating the sql From clause

        sql += vsb.getPositionSQL();
        sql += vsb.getDepthSQL();
        sql += vsb.getSampleSQL();
        sql += vsb.getScoreSql();
        sql += vsb.getGenicStatusSQL();
        sql += vsb.getAAChangeSQL();
        sql += vsb.getZygositySQL();
        sql += vsb.getLocationSQL();
        sql += vsb.getNearSpliceSiteSQL();
        sql += vsb.getIsPrematureStopSQL();
        sql += vsb.getIsReadthroughSQL();
        sql += vsb.getDBSNPSQL();
        sql += vsb.getPolyphenSQL();
        sql += vsb.getConScoreSQL();
        sql += vsb.getClinicalSignificanceSQL();
        sql += vsb.getAlleleCountSQL();
        sql += vsb.getPsudoautosomalSQL();
        sql += vsb.getGenesSQL();
        sql += vsb.getReadDepthSql();
        sql += vsb.getVariantIdSql();
        sql += vsb.getVariantTypeSQL();
        sql += vsb.getIsFrameshiftSQL();

        sql += "    order by v.variant_id";
        sql = sqlFrom + sql;

        logger.debug("\n\n" + sql + "\n\n");
        return getCount(sql);
    }


    /**
     * Return number of positions that have variants based on VariantSearchBean
     * @param vsb
     * @return
     */
    public int getPositionCount(VariantSearchBean vsb) {
        String sql = " ";
        String sqlFrom = "select ";

        sql += vsb.getVariantTable(vsb.getMapKey()) + " v ";
        sqlFrom += " count(distinct(start_pos)) as count ";
        String[] results = vsb.getTableJoinSQL(sqlFrom, true);
        sql += results[1];
        sqlFrom += " from ";  // We're done updating the sql From clause

        sql += vsb.getPositionSQL();
        sql += vsb.getDepthSQL();
        sql += vsb.getSampleSQL();
        sql += vsb.getScoreSql();
        sql += vsb.getGenicStatusSQL();
        sql += vsb.getAAChangeSQL();
        sql += vsb.getZygositySQL();
        sql += vsb.getLocationSQL();
        sql += vsb.getNearSpliceSiteSQL();
        sql += vsb.getIsPrematureStopSQL();
        sql += vsb.getIsReadthroughSQL();
        sql += vsb.getDBSNPSQL();
        sql += vsb.getPolyphenSQL();
        sql += vsb.getConScoreSQL();
        sql += vsb.getClinicalSignificanceSQL();
        sql += vsb.getAlleleCountSQL();
        sql += vsb.getPsudoautosomalSQL();
        sql += vsb.getGenesSQL();
        sql += vsb.getReadDepthSql();
        sql += vsb.getVariantIdSql();
        sql += vsb.getVariantTypeSQL();
        sql += vsb.getIsFrameshiftSQL();

        sql += "    order by v.variant_id";
        sql = sqlFrom + sql;

        logger.debug("\n\n" + sql + "\n\n");
        return getCount(sql);
    }


    /**
     * Return a VariantResult object preset with only the variant and conservation score
     *
     * @param vsb VariantSearchBean object
     * @return VariantResult object
     */
    public List<VariantResult> getVariantAndConservation(VariantSearchBean vsb) {

        if (vsb.getPositionSQL().equals("") && vsb.getGenesSQL().equals("")) {
            return Collections.emptyList();
        }

        logger.debug("Started Searching in  getVariantAndCon(VariantSearchBean vsb) ");
        String sql = " ";
        String sqlFrom = "select ";


        sql += vsb.getVariantTable(vsb.getMapKey()) + " v ";

        sqlFrom += "v.* ";

        if (vsb.getGeneMap().size() > 0) {
            sql += " inner join gene_loci gl on (gl.map_key=" + vsb.getMapKey() + " and gl.chromosome=v.chromosome and gl.pos=v.start_pos) ";
        }


        if (vsb.hasOnlyTranscript()) {
            sql += " inner join "+vsb.getVariantTranscriptTable(vsb.getMapKey())+" vt on v.variant_id=vt.variant_id ";
            sqlFrom += ",vt.* ";
            sql += " inner join transcripts t on ( vt.transcript_rgd_id = t.transcript_rgd_id ) ";


        }
        if (vsb.hasPolyphen()) {
            sql += " inner join "+vsb.getPolyphenTable()+" p on (v.variant_id=p.variant_id and p.protein_status='100 PERC MATCH') ";
            sqlFrom += ",p.* ";
        }

        if (vsb.hasDBSNP()) {

            if (vsb.hasDBSNP() || vsb.getNovelDBSNP()) {
                sql += " left outer JOIN sample s ON (v.sample_id=s.sample_id AND s.MAP_KEY=" + vsb.getMapKey() + ") " +

                        " left outer JOIN  db_snp dbs ON  ( v.START_POS = dbs.POSITION " +
                        "    AND v.CHROMOSOME = dbs.CHROMOSOME  " +
                        "    AND v.VAR_NUC = dbs.ALLELE  " +
                        "    AND dbs.MAP_KEY = s.MAP_KEY AND dbs.source=s.dbsnp_source) \n ";
            }

            sqlFrom += ",dbs.* , dbs.snp_name as MCW_DBS_SNP_NAME";
        }

        if (vsb.hasConScore()) {
            sql += " inner join  " + vsb.getConScoreTable() + " cs on (cs.chr=v.chromosome and cs.position = v.start_pos) ";
            sqlFrom += ",cs.* ";
        }else {
            sql += " left outer join " + vsb.getConScoreTable() + " cs on (cs.chr=v.chromosome and cs.position = v.start_pos) ";
            sqlFrom += ",cs.*";
        }

        if (vsb.hasClinicalSignificance()) {
            sql += " inner join clinvar cv on (cv.rgd_id=v.rgd_id) ";
            sqlFrom += ",cv.* ";
        }else {
            sql += " left outer join clinvar cv on (cv.rgd_id=v.rgd_id) ";
            sqlFrom += ",cv.*";
        }

        sql += " where 1=1  ";

        sql += vsb.getPositionSQL();
        sql += vsb.getDepthSQL();
        sql += vsb.getSampleSQL();
        sql += vsb.getScoreSql();
        sql += vsb.getGenicStatusSQL();
        sql += vsb.getAAChangeSQL();
        sql += vsb.getZygositySQL();
        sql += vsb.getLocationSQL();
        sql += vsb.getNearSpliceSiteSQL();
        sql += vsb.getIsPrematureStopSQL();
        sql += vsb.getIsReadthroughSQL();
        sql += vsb.getDBSNPSQL();
        sql += vsb.getPolyphenSQL();
        sql += vsb.getConScoreSQL();
        sql += vsb.getClinicalSignificanceSQL();
        sql += vsb.getAlleleCountSQL();
        sql += vsb.getPsudoautosomalSQL();
        sql += vsb.getGenesSQL();
        sql += vsb.getGeneMapSQL();
        sql += vsb.getReadDepthSql();
        sql += vsb.getVariantIdSql();
        sql += vsb.getVariantTypeSQL();
        sql += vsb.getIsFrameshiftSQL();

        String sub = sql;


        if (vsb.isShowDifferences()) {
            sql += "and (v.chromosome, v.start_pos, v.var_nuc) in (";
            sql += "select chromosome, start_pos, var_nuc from (select v.chromosome, v.start_pos,v.var_nuc, count(*) from " + sub;
            sql += " group by v.chromosome,v.start_pos,v.var_nuc having count(*) < " + vsb.sampleIds.size() + "))";
        }

        sql += "    order by v.variant_id";


        sql = sqlFrom + " from " +  sql;

        logger.debug("Running Search SQL getVariantResults() : \n" + sql + "\n");

        List<VariantResult> vrList = new ArrayList<VariantResult>();
        try( Connection conn = this.getDataSource().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            long lastVariant = 0;

            VariantResultBuilder vrb = new VariantResultBuilder();
            boolean found= false;
            while (rs.next()) {
                found=true;
                long variantId = rs.getLong("variant_id");
                if (variantId != lastVariant) {
                    if (lastVariant != 0) {
                        vrList.add(vrb.getVariantResult());
                    }
                    lastVariant = variantId;

                    vrb = new VariantResultBuilder();

                    vrb.mapVariant(rs);
                    vrb.mapConservation(rs);
                }
            }

            //if (vrb.getVariantResult() != null) {
            if (found) {
                vrList.add(vrb.getVariantResult());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        VariantDAO.lastQuery = sql;
        logger.debug("found vrList size " + vrList.size());
        return vrList;
    }


    /**
     *
     * @param vsb VariantSearchBean object
     * @return
     * @throws Exception
     */
    public Map<String, Map<String, Integer>> getVariantToGeneCountMap(VariantSearchBean vsb) throws Exception{

        String sql = "SELECT gene_symbols as gene_symbol, sample_id, count(*) as count FROM (" +
                "select /*+parallel*/distinct v.variant_id, v.sample_id, gl.gene_symbols from ";

        sql += vsb.getVariantTable(vsb.getMapKey()) +  " v ";

        if (vsb.getGeneMap().size() > 0) {
            sql += " inner join gene_loci gl on (gl.map_key=" + vsb.getMapKey() + " and gl.chromosome=v.chromosome and gl.pos=v.start_pos) ";
        }else {
            sql += " inner join  gene_loci gl ";
            sql += " on (v.chromosome=gl.chromosome and v.start_pos = gl.pos and gl.map_key=" + vsb.getMapKey() + " )";
        }

        String[] results = vsb.getTableJoinSQL("", true); // We don't need to auto generate the select statement
        sql += results[1];
        sql += vsb.getPositionSQL();
        sql += vsb.getDepthSQL();
        sql += vsb.getSampleSQL();
        sql += vsb.getScoreSql();
        sql += vsb.getGenicStatusSQL();
        sql += vsb.getAAChangeSQL();
        sql += vsb.getZygositySQL();
        sql += vsb.getLocationSQL();
        sql += vsb.getNearSpliceSiteSQL();
        sql += vsb.getIsPrematureStopSQL();
        sql += vsb.getIsReadthroughSQL();
        sql += vsb.getDBSNPSQL();
        sql += vsb.getPolyphenSQL();
        sql += vsb.getConScoreSQL();
        sql += vsb.getClinicalSignificanceSQL();
        sql += vsb.getAlleleCountSQL();
        sql += vsb.getPsudoautosomalSQL();
        //sql += vsb.getGenesSQL();
        sql += vsb.getGeneMapSQL();
        sql += vsb.getReadDepthSql();
        sql += vsb.getVariantTypeSQL();
        sql += vsb.getIsFrameshiftSQL();
        sql += "   ) group by gene_symbols, sample_id order by sample_id, gene_symbols ";

        logger.debug("getVariantToGeneCountMap: "+sql);
        VariantDAO.lastQuery = sql;

        Map<String, Map<String,Integer>> tMap = new TreeMap<>();

        try(Connection conn = getDataSource().getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String sampleId = rs.getString("sample_id");
                Map<String, Integer> map = tMap.get(sampleId);
                if( map == null ) {
                    map = new HashMap<String, Integer>();
                    tMap.put(sampleId, map);
                }

                String geneSymbol = rs.getString("gene_symbol");
                if( geneSymbol!=null ) {
                    map.put(geneSymbol, rs.getInt("count"));
                }else {
                    System.out.println("ERROR *** rerun GeneLoci Pipeline! ***");
                }
            }
        }

        return tMap;
    }


    /**
     * For the List of Variants create new variants of update the variant if it
     * has a non-zero id  returns the number of variants created
     *
     * @param variantList list of Variant objects
     * @return count of rows inserted
     */
    public int saveVariants(List<Variant> variantList, int sampleId) {

        String tableName = getVariantTable(sampleId);
        String sqlUpdate = "UPDATE "+tableName+" SET\n" +
                " chromosome=?, end_pos=?, ref_nuc=?, sample_id=?, start_pos=?,\n" +
                " total_depth=?, var_freq=?, quality_score=?, rgd_id=?, hgvs_name=?,\n" +
                " variant_type=?, var_nuc=?, zygosity_status=?, genic_status=?, zygosity_percent_read=?,\n" +
                " zygosity_num_allele=?, zygosity_poss_error=?, zygosity_ref_allele=?, zygosity_in_pseudo=?, padding_base=?\n" +
                "WHERE variant_id=?";
        String sqlInsert = "INSERT INTO "+tableName+" (\n" +
                " chromosome, end_pos, ref_nuc, sample_id, start_pos,\n" +
                " total_depth, var_freq, quality_score, rgd_id, hgvs_name,\n" +
                " variant_type, var_nuc, zygosity_status, genic_status, zygosity_percent_read,\n" +
                " zygosity_num_allele, zygosity_poss_error, zygosity_ref_allele, zygosity_in_pseudo, padding_base, variant_id)\n" +
                "VALUES(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, VARIANT_SEQ.NEXTVAL)";

        int countInserted = 0;
        try(Connection conn = this.getDataSource().getConnection() ) {

            for (Variant variantToSave : variantList) {

                String sql = variantToSave.getId() > 0 ? sqlUpdate : sqlInsert;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, variantToSave.getChromosome());
                ps.setLong(2, variantToSave.getEndPos());
                ps.setString(3, variantToSave.getReferenceNucleotide());
                ps.setInt(4, variantToSave.getSampleId());
                ps.setLong(5, variantToSave.getStartPos());

                ps.setInt(6, variantToSave.getDepth());
                ps.setInt(7, variantToSave.getVariantFrequency());
                ps.setInt(8, variantToSave.getQualityScore());
                ps.setInt(9, variantToSave.getRgdId());
                ps.setString(10, variantToSave.getHgvsName());

                ps.setString(11, variantToSave.getVariantType());
                ps.setString(12, variantToSave.getVariantNucleotide());
                ps.setString(13, variantToSave.getZygosityStatus());
                ps.setString(14, variantToSave.getGenicStatus());
                ps.setInt(15, variantToSave.getZygosityPercentRead());

                ps.setInt(16, variantToSave.getZygosityNumberAllele());
                ps.setString(17, variantToSave.getZygosityPossibleError());
                ps.setString(18, variantToSave.getZygosityRefAllele());
                ps.setString(19, variantToSave.getZygosityInPseudo());
                ps.setString(20, variantToSave.getPaddingBase());

                if( sql.equals(sqlUpdate) ) {
                    ps.setLong(21, variantToSave.getId());
                } else {
                    countInserted++;
                }
                ps.execute();
                ps.close();
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return countInserted;
    }

    /**
     * For the List of Variants create new variants of update the variant if it
     * has a non-zero id  returns the number of variants created
     *
     * @param variantList
     */
    public int insertVariants(List<Variant> variantList, int sampleId) {

        String tableName = getVariantTable(sampleId);

        BatchSqlUpdate bsu = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO "+tableName+" (\n" +
                        " VARIANT_ID, CHROMOSOME, END_POS, REF_NUC, SAMPLE_ID,\n" +
                        " START_POS, TOTAL_DEPTH, VAR_FREQ, QUALITY_SCORE, RGD_ID,\n" +
                        " HGVS_NAME, VARIANT_TYPE, VAR_NUC, ZYGOSITY_STATUS, GENIC_STATUS,\n" +
                        " ZYGOSITY_PERCENT_READ, ZYGOSITY_NUM_ALLELE, ZYGOSITY_POSS_ERROR, ZYGOSITY_REF_ALLELE, ZYGOSITY_IN_PSEUDO,\n" +
                        " PADDING_BASE)\n" +
                        "VALUES (\n" +
                        "  VARIANT_SEQ.NEXTVAL,?,?,?,?,\n" +
                        "  ?,?,?,?,?,\n" +
                        "  ?,?,?,?,?,\n" +
                        "  ?,?,?,?,?,\n" +
                        "  ?)",
                new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.INTEGER,
                        Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR
                }, 10000);
        bsu.compile();

        for( Variant v: variantList ) {
            bsu.update(v.getChromosome(), v.getEndPos(), v.getReferenceNucleotide(), v.getSampleId(),
                    v.getStartPos(), v.getDepth(), v.getVariantFrequency(), v.getQualityScore(), v.getRgdId(),
                    v.getHgvsName(), v.getVariantType(), v.getVariantNucleotide(), v.getZygosityStatus(), v.getGenicStatus(),
                    v.getZygosityPercentRead(), v.getZygosityNumberAllele(), v.getZygosityPossibleError(),
                    v.getZygosityRefAllele(), v.getZygosityInPseudo(), v.getPaddingBase()
            );
        }
        bsu.flush();

        // compute nr of rows affected
        int totalRowsAffected = 0;
        for( int rowsAffected: bsu.getRowsAffected() ) {
            totalRowsAffected += rowsAffected;
        }
        return totalRowsAffected;
    }

    public List<Variant> getVariants(int sampleId, String chr, long start, long stop) {

        // obsolete: old variant table structure
        //String sql = "SELECT * FROM "+getVariantTable(sampleId)+" WHERE sample_id=? AND start_pos >= ? AND start_pos <= ? AND chromosome=?";

        String sql ="SELECT v.rgd_id VARIANT_ID,m.chromosome,d.sample_id,m.start_pos,m.end_pos,d.var_freq,d.total_depth,d.quality_score,v.ref_nuc,v.var_nuc"
                +",d.zygosity_status,d.zygosity_in_pseudo,d.zygosity_num_allele,d.zygosity_percent_read,d.zygosity_poss_error,d.zygosity_ref_allele,m.genic_status"
                +",null HGVS_NAME,v.rgd_id,v.variant_type,m.padding_base "
                +"FROM variant v, variant_sample_detail d, variant_map_data m "
                +"WHERE d.rgd_id=m.rgd_id AND v.rgd_id=m.rgd_id AND sample_id=? AND start_pos BETWEEN ? AND  ? AND chromosome=?";


        VariantMapper q = new VariantMapper(getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(sampleId, start, stop, chr);
    }

    /**
     * get the list of damaging or possibly damaging variants associated with the gene
     * @param rgdId,mapKey
     * @return variants associated with gene in an assembly
     * @throws Exception when unexpected error occurs
     */
    public List<Variant> getDamagingVariantsForGeneByAssembly(int rgdId, String mapKey) throws Exception {
        String sql = "SELECT * from VARIANT v" +
                " left outer join VARIANT_MAP_DATA   vmd on vmd.rgd_id=v.rgd_id " +
                " left outer join variant_sample_detail vsd on vsd.rgd_id=v.rgd_id " +
                "where v.rgd_ID in (select distinct(variant_rgd_ID) from POLYPHEN where VARIANT_RGD_ID in "+
                " (select VARIANT_RGD_ID from VARIANT_TRANSCRIPT where TRANSCRIPT_RGD_ID IN "+
                "(select TRANSCRIPT_RGD_ID from TRANSCRIPTS where GENE_RGD_ID =? ))"+
                "AND PREDICTION LIKE '%damaging') and vsd.SAMPLE_ID IN (SELECT UNIQUE(SAMPLE_ID) "+
                "from SAMPLE where MAP_KEY = ?) ORDER BY START_POS,END_POS,REF_NUC,VAR_NUC";
        VariantMapper q = new VariantMapper(getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId, mapKey);
    }

    public List<String> getGeneAssemblyOfDamagingVariants(int rgdId) throws Exception {

        //String sql = "select UNIQUE(s.MAP_KEY) from VARIANT v,SAMPLE s, MAPS m where VARIANT_ID in (select distinct(VARIANT_ID) from POLYPHEN where VARIANT_ID in \n" +
        //        "(select VARIANT_ID from VARIANT_TRANSCRIPT \n" +
        //        "where TRANSCRIPT_RGD_ID IN (select TRANSCRIPT_RGD_ID from TRANSCRIPTS where GENE_RGD_ID = " + rgdId + "))" +
        //        "AND PREDICTION LIKE '%damaging') and v.SAMPLE_ID = s.SAMPLE_ID and s.MAP_KEY = m.MAP_KEY";

        String sql = "SELECT DISTINCT(map_key) FROM polyphen p,variant_transcript v,transcripts t \n"+
            "WHERE prediction like '%damaging' AND v.variant_rgd_id=p.variant_rgd_id AND v.transcript_rgd_id=t.transcript_rgd_id \n"+
            "AND gene_rgd_id="+rgdId;
        return getList(sql);
    }

    public List<String> getAssemblyOfDamagingVariants(int strainRgdId) throws Exception {

        String sql = "select UNIQUE(MAP_KEY) from SAMPLE where STRAIN_RGD_ID = " + strainRgdId;
        return getList(sql);
    }

    /**
     * get the list of damaging or possibly damaging variants associated with the strain
     * @param rgdId
     * @return variants associated with strain
     * @throws Exception when unexpected error occurs
     */
    public int getCountofDamagingVariantsForStrainByAssembly(int rgdId,String mapKey) throws Exception {

        String sql = "select /*+ PARALLEL*/ count(DISTINCT(p.VARIANT_RGD_ID)) as count " +
                "from POLYPHEN p inner join VARIANT v "+
                " on p.VARIANT_ID = v.VARIANT_ID and p.PREDICTION LIKE '%damaging'  " +
                " inner join VARIANT_MAP_DATA  vmd on vmd.rgd_id=v.rgd_id " +
                " inner join VARIANT_SAMPLE_DETAIL vsd on vsd.rgd_id=v.rgd_id and vsd.total_depth > 8 "+
                " inner join SAMPLE s on vsd.SAMPLE_ID = s.SAMPLE_ID and STRAIN_RGD_ID =" + rgdId +" and MAP_KEY ="+mapKey;
        return getCount(sql);
    }
    /**
     * get the list of damaging or possibly damaging variants associated with the sample
     * @param sampleId
     * @return variants associated with sample
     * @throws Exception when unexpected error occurs
     */
    public int getCountofDamagingVariantsForSample(int sampleId, String mapKey) throws Exception {
        String sql = "select /*+ PARALLEL*/ count(DISTINCT(p.VARIANT_RGD_ID)) as count " +
                "from POLYPHEN p inner join VARIANT v \n"+
                " on p.VARIANT_RGD_ID = v.RGD_ID and p.PREDICTION LIKE '%damaging'  " +
                " inner join VARIANT_MAP_DATA  vmd on vmd.rgd_id=v.rgd_id " +
                " inner join VARIANT_SAMPLE_DETAIL vsd on vsd.rgd_id=v.rgd_id  and vsd.total_depth > 8"+
                "inner join SAMPLE s on vsd.SAMPLE_ID = s.SAMPLE_ID and s.SAMPLE_ID =" + sampleId +" and s.MAP_KEY ="+mapKey;

        return getCount(sql);
    }

    /**
     * get the list of damaging or possibly damaging variants associated with the sample
     * @param sampleId
     * @param mapKey
     * @return variants associated with sample
     * @throws Exception when unexpected error occurs
     */
    public List<Variant> getDamagingVariantsForSampleByAssembly(int sampleId, int mapKey) throws Exception {
        String sql = "select /*+ PARALLEL*/ distinct(v.RGD_ID),v.*, vmd.*, vsd.*,p.GENE_SYMBOL from VARIANT v\n" +
                " inner join VARIANT_MAP_DATA  vmd on vmd.rgd_id=v.rgd_id " +
                " inner join VARIANT_SAMPLE_DETAIL vsd on vsd.rgd_id=v.rgd_id and vsd.total_depth > 8 "+
                "inner join SAMPLE s on vsd.SAMPLE_ID = s.SAMPLE_ID and s.SAMPLE_ID=? and s.MAP_KEY =? \n" +
                "inner join POLYPHEN p on v.RGD_ID = p.VARIANT_RGD_ID and p.PREDICTION LIKE '%damaging'  \n" +
                "ORDER BY vmd.CHROMOSOME,vmd.START_POS,vmd.END_POS,v.REF_NUC,v.VAR_NUC";
        VariantMapper q = new VariantMapper(getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(sampleId,mapKey);
    }


    /**
     * get the list of damaging or possibly damaging variants associated with the strain
     * @param rgdId,mapKey
     * @return variants associated with strain
     * @throws Exception when unexpected error occurs
     */
    public List<Variant> getDamagingVariantsForStrainByAssembly(int rgdId,int mapKey) throws Exception {
        String sql = "select /*+ PARALLEL*/ distinct(v.VARIANT_ID),v.*,p.GENE_SYMBOL from VARIANT v " +
                "inner join SAMPLE s on v.SAMPLE_ID = s.SAMPLE_ID and s.STRAIN_RGD_ID=? and s.MAP_KEY =?\n" +
                "inner join POLYPHEN p on v.VARIANT_ID = p.VARIANT_ID and p.PREDICTION LIKE '%damaging' and v.total_depth > 8 " +
                "ORDER BY CHROMOSOME,START_POS,END_POS,REF_NUC,VAR_NUC";
        VariantMapper q = new VariantMapper(getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId,mapKey);
    }


    /**
     * For the List of Variants only some properties are updated used by Zygosity and depth load
     *
     * @param variantList
     */
    public void updateVariantsForDepthAndZygo(List<Variant> variantList) {

        List batchArgs = new ArrayList(variantList.size());
        for( Variant v: variantList ) {
            batchArgs.add(new Object[]{
                    v.getDepth(),
                    v.getVariantNucleotide(),
                    v.getZygosityStatus(),
                    v.getZygosityPercentRead(),
                    v.getZygosityInPseudo(),
                    v.getZygosityNumberAllele(),
                    v.getZygosityPossibleError(),
                    v.getZygosityRefAllele(),
                    v.getId()});
        }
        String sql = "UPDATE variant SET total_depth=?, "+
                "var_nuc=?, "+
                "zygosity_status=?, "+
                "zygosity_percent_read=?, "+
                "zygosity_in_pseudo=?, "+
                "zygosity_num_allele=?, "+
                "zygosity_poss_error=?, "+
                "zygosity_ref_allele=? "+
                "WHERE variant_id=?";

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    int getCount(String sql) {
        try (Connection conn = this.getDataSource().getConnection() ){

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            return rs.getInt("count");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    List<String> getList(String sql) {
        List<String> res = new ArrayList<>();
        try (Connection conn = this.getDataSource().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(rs.getString(1));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public List<Variant> getVariantsBySampleId(int sampleId,String chr, String tableName) throws Exception {
        //  String sql="select * from variant_dog where sample_id=?";
        String sql="select * from "+tableName+" where sample_id=? and chromosome=?";
        VariantMapper mapper= new VariantMapper(this.getDataSource(), sql);
        mapper.declareParameter(new SqlParameter(Types.INTEGER));
        mapper.declareParameter(new SqlParameter(Types.VARCHAR));
        return mapper.execute(sampleId, chr);
    }

    public String getGeneSymbolByVariantId(long id) throws Exception {
        String query = "select GENE_SYMBOL from POLYPHEN where VARIANT_ID ="+id;
        return getList(query).get(0);
    }

    public List<VariantSampleDetail> getVariantSampleDetail(int rgdId) throws Exception{
        String sql = "SELECT * FROM variant_sample_detail WHERE rgd_id=?";
        VariantSampleQuery q = new VariantSampleQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId);
    }

    public VariantMapData getVariant(int rgdId) throws Exception{
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where v.rgd_id=?";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<VariantMapData> vmds = q.execute(rgdId);
        if (vmds.isEmpty())
            return null;
        return vmds.get(0);
    }
}