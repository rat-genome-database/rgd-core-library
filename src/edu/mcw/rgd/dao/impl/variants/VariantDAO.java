package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.CountQuery;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.VariantMapper;
import edu.mcw.rgd.dao.spring.variants.VariantMapQuery;
import edu.mcw.rgd.dao.spring.variants.VariantSampleQuery;
import edu.mcw.rgd.datamodel.Variant;
import edu.mcw.rgd.datamodel.VariantResult;
import edu.mcw.rgd.datamodel.VariantResultBuilder;
import edu.mcw.rgd.datamodel.VariantSearchBean;
import edu.mcw.rgd.datamodel.variants.VariantMapData;
import edu.mcw.rgd.datamodel.variants.VariantSampleDetail;
import edu.mcw.rgd.datamodel.variants.VariantTranscript;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class VariantDAO extends AbstractDAO {
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
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where v.rs_id=?";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        List<VariantMapData> vmds = q.execute(rsId);
        if (vmds.isEmpty())
            return  null;
        return vmds.get(0);
    }

    public List<VariantMapData> getAllVariantByRsId(String rsId) throws Exception {
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where v.rs_id=? order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rsId);
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
    public Integer getVariantsCountWithGeneLocation(int mapKey, String chrom, int start, int stop) throws Exception{
        String sql = "select count(*) as CNT from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ?";
//        Connection con = DataSourceFactory.getInstance().getCarpeNovoDataSource().getConnection();
//        PreparedStatement ps = con.prepareStatement(sql);
//        ps.setInt(1,mapKey);
//        ps.setString(2,chrom);
//        ps.setInt(3,start);
//        ps.setInt(4,stop);
//        ResultSet rs = ps.executeQuery();
//        int cnt = 0;
//        while(rs.next()){
//            cnt =rs.getInt(1);
//        }
        CountQuery q = new CountQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        List<Integer> results = execute(q, mapKey,chrom,start,stop);
        return results.get(0);
    }
}
