package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.variants.VariantMapQuery;
import edu.mcw.rgd.dao.spring.variants.VariantSampleQuery;
import edu.mcw.rgd.datamodel.Sample;
import edu.mcw.rgd.datamodel.VariantResult;
import edu.mcw.rgd.datamodel.VariantResultBuilder;
import edu.mcw.rgd.datamodel.VariantSearchBean;
import edu.mcw.rgd.datamodel.variants.VariantMapData;
import edu.mcw.rgd.datamodel.variants.VariantSampleDetail;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;


public class VariantDAO extends AbstractDAO {

    // overrides data source to be CarpeNovo data source for this class
    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getCarpeNovoDataSource();
    }

    // overrides data source to be CarpeNovo data source for this class
    public Connection getConnection() throws Exception{
        return getDataSource().getConnection();
    }

    public List<VariantResult> getVariantsNewTbaleStructure(VariantSearchBean vsb) throws Exception {

        String csTable=vsb.getConScoreTable();
        String sql="select  v.*,vmd.*, vsd.*,vt.*, p.*,cs.* ,gl.gene_symbols as region_name, " +
                " rseqData.seq_data as full_ref_aa, " +
                " nseqData.seq_data as full_ref_nuc" +
                " from variant v " +
                " left outer join variant_map_data vmd on (vmd.rgd_id=v.rgd_id) " +
                " left outer join variant_sample_detail vsd on (vsd.rgd_id=v.rgd_id) " +
                " left outer join variant_transcript vt on v.rgd_id=vt.variant_rgd_id  " +
                " left outer join rgd_sequences rseq on rseq.seq_key=vt.full_ref_aa_seq_key " +
                " left outer join seq_data rseqData on rseq.seq_data_md5=rseqData.data_md5 " +
                " left outer join rgd_sequences nseq on nseq.seq_key=vt.full_ref_nuc_seq_key " +
                " left outer join seq_data nseqData on nseq.seq_data_md5=nseqData.data_md5 " +
                " left outer join polyphen  p on (v.rgd_id =p.variant_rgd_id)   " +
                " left outer join "+ csTable + " cs on (cs.position=vmd.start_pos and cs.chr=vmd.chromosome)     " +
                "  left outer join gene_loci gl on (gl.map_key=vmd.map_key and gl.chromosome=vmd.chromosome and gl.pos=vmd.start_pos)         " +
                "   where  " +
                "                v.rgd_id in (" ;
        //   "63409322)";
        String ids= String.valueOf(vsb.getVariantId());
        sql=sql+ids;
        sql=sql+")";
            System.out.println(sql);
        List<VariantResult> vrList = new ArrayList<VariantResult>();
        Connection conn = null;
        Statement stmt;
        ResultSet rs;

        try {

            conn = DataSourceFactory.getInstance().getCarpeNovoDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            long lastVariant = 0;

            VariantResultBuilder vrb = null;

            while (rs.next()) {
                long variantId = rs.getLong("rgd_id");
                if (variantId != lastVariant) {
                    if (lastVariant != 0) {
                        vrList.add(vrb.getVariantResult());
                    }
                    lastVariant = variantId;
                    vrb = new VariantResultBuilder();

                    vrb.mapVariant(rs);
                    vrb.mapConservation(rs);
                }

                if (vrb != null) {
                    vrb.mapTranscript(rs);
                }
                if (vrb != null) {
                    vrb.mapPolyphen(rs);
                }
                // vrb.mapDBSNP(rs, vsb);
             //   vrb.mapClinVar(rs);
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

        edu.mcw.rgd.dao.impl.VariantDAO.lastQuery = sql;
        logger.debug("found vrList size " + vrList.size());
        return vrList;


    }
    public int getMapKeyByVariantId(int variantId) throws Exception {
        String sql="select distinct(vmd.map_key) from variant_map_data vmd where rgd_id=? ";
        IntListQuery q=new IntListQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        List<Integer> results= execute(q, variantId);
        if(results!=null && results.size()>0) return results.get(0);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        VariantDAO variantDAO=new VariantDAO();

        int cnt = variantDAO.getVariantsCountWithGeneLocation(372,"20", 1, Integer.MAX_VALUE);
        System.out.println("cnt="+cnt);

        VariantSearchBean vsb=new VariantSearchBean(60);
        vsb.setVariantId(69050686);
       List<VariantResult> results=variantDAO.getVariantsNewTbaleStructure(vsb);
       System.out.println("RESULSTS SIZE:"+ results.size());
        System.out.println("DONE!!");
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

    public List<VariantMapData> getVariantsByRgdId(int rgdId) throws Exception{
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where v.rgd_id=?";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId);
    }

    public VariantMapData getVariantByRsId(String rsId) throws Exception {
        String sql = "SELECT v.*,vmd.* FROM variant v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        List<VariantMapData> vmds = q.execute(rsId);
        if (vmds.isEmpty())
            return  null;
        return vmds.get(0);
    }

    public List<VariantMapData> getAllVariantByRsId(String rsId) throws Exception {
        String sql = "SELECT v.*,vmd.* FROM variant v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rsId);
    }

    public List<VariantMapData> getAllVariantByRsIdAndMapKey(String rsId, int mapKey) throws Exception {
        String sql = "SELECT v.*,vmd.* FROM variant v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vmd.map_key=? order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rsId,mapKey);
    }

    public List<VariantMapData> getVariantsWithGeneLocation(int mapKey, String chrom, int start, int stop) throws Exception{
        String sql = "select * from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ?";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop);
    }

    public List<VariantMapData> getVariantsWithGeneLocationLimited(int mapKey, String chrom, int start, int stop, int offset) throws Exception{
        String sql = "select * from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ? offset ? rows fetch next 1000 rows only";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop, offset);
    }

    public List<VariantMapData> getActiveVariantsWithGeneLocationLimited(int mapKey, String chrom, int start, int stop, int offset) throws Exception{
        String sql = "select v.*, vm.* from variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and vm.map_key=? and vm.chromosome=? " +
                "and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vm.start_pos between ? and ? offset ? rows fetch next 1000 rows only";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop, offset);
    }

    public List<VariantMapData> getVariantsWithTranscriptLocationNameLimited(int mapKey, String chrom, int start, int stop, String locName, int offset) throws Exception{
        String loc = "";
        if (locName.equals("Exon"))
            loc = "EXON";
        else
            loc="INTRON";
        String sql = "select * from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.map_key=? and v.rgd_id in (\n" +
                "select distinct variant_rgd_id as rgd_id from variant_transcript where location_name like '%"+loc+"%' and variant_rgd_id in " +
                "(select v.rgd_id as rgd_id from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id  and vm.chromosome=? and vm.start_pos between ? and ?) ) " +
                "offset ? rows fetch next 1000 rows only";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop, offset);
    }

    public List<VariantMapData> getActiveVariantsWithTranscriptLocationNameLimited(int mapKey, String chrom, int start, int stop, String locName, int offset) throws Exception{
        String loc = "";
        if (locName.equals("Exon"))
            loc = "EXON";
        else
            loc="INTRON";
        String sql = "select v.*,vm.* from variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and vm.map_key=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and v.rgd_id in (\n" +
                "select distinct variant_rgd_id as rgd_id from variant_transcript where location_name like '%"+loc+"%' and variant_rgd_id in " +
                "(select v.rgd_id as rgd_id from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id  and vm.chromosome=? and vm.start_pos between ? and ?) ) " +
                "offset ? rows fetch next 1000 rows only";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop, offset);
    }

    public Integer getVariantsWithTranscriptLocationNameCount(int mapKey, String chrom, int start, int stop, String locName) throws Exception{
        String sql = "";
        int cnt = 0;
        if (locName.equals("Exon"))
            sql = "select count(*) as CNT from variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and vm.map_key=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and v.rgd_id in (" +
                    "select distinct variant_rgd_id as rgd_id from variant_transcript where location_name like '%EXON%' and variant_rgd_id in " +
                    "(select v.rgd_id as rgd_id from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id  and vm.chromosome=? and vm.start_pos between ? and ?) )";
        else
            sql = "select count(*) as CNT from variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and vm.map_key=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and v.rgd_id in (" +
                    "select distinct variant_rgd_id as rgd_id from variant_transcript where location_name like '%INTRON%' and variant_rgd_id in " +
                    "(select v.rgd_id as rgd_id from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id  and vm.chromosome=? and vm.start_pos between ? and ?) )";
        Connection con = null;
        try {
            con = DataSourceFactory.getInstance().getCarpeNovoDataSource().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mapKey);
            ps.setString(2, chrom);
            ps.setInt(3, start);
            ps.setInt(4, stop);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cnt = rs.getInt(1);
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            }
            catch (Exception ignored){  }
        }
//        CountQuery q = new CountQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
//        List<Integer> results = execute(q, mapKey,chrom,start,stop);
        return cnt;
    }

    public Integer getVariantsCountWithGeneLocation(int mapKey, String chrom, int start, int stop) throws Exception{
        String sql = "select count(*) as CNT from variant v, variant_map_data vm, RGD_IDS r  where v.rgd_id=vm.rgd_id and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ?";
        int cnt = getCount(sql, mapKey,chrom,start,stop);
        return cnt;
    }

    public List<Long> getVariantStartPositionByPositionAndMapKey(int mapKey, String chrom, int start, int stop) throws Exception{
        String sql = "select start_pos from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ?";
        List<Long> pos = new ArrayList<>();
        Connection con = null;
        try {
            con = DataSourceFactory.getInstance().getCarpeNovoDataSource().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,mapKey);
            ps.setString(2,chrom);
            ps.setInt(3,start);
            ps.setInt(4,stop);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                pos.add(rs.getLong(1));
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            }
            catch (Exception ignored){  }
        }
        return pos;
    }

    public int insertSample(Sample sample) throws Exception{
        String sql = "INSERT INTO SAMPLE (SAMPLE_ID, ANALYSIS_NAME, ANALYSIS_TIME, DESCRIPTION, PATIENT_ID, SEQUENCER, GENDER, GRANT_NUMBER," +
                " SEQUENCED_BY, WHERE_BRED, SECONDARY_ANALYSIS_SOFTWARE, MAP_KEY, DBSNP_SOURCE, STRAIN_RGD_ID, REF_RGD_ID)" +
                " VALUES (?,?,SYSTIMESTAMP,?,?,?,?,?,?,?,?,?,?,?,?)";
        return update(sql,sample.getId(),sample.getAnalysisName(),sample.getDescription(),sample.getPatientId(),sample.getSequencer(),sample.getGender(),
                sample.getGrantNumber(), sample.getSequencedBy(),sample.getWhereBred(), sample.getSecondaryAnalysisSoftware(), sample.getMapKey(),
                sample.getDbSnpSource(),sample.getStrainRgdId(),sample.getRefRgdId());
    }
}
