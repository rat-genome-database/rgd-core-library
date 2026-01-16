package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.dao.spring.variants.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.datamodel.variants.*;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
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
    public List<ConservationScore> getConservationScores(long startPos, String chr, String tableName) throws Exception {
        String sql="select * from "+tableName+" where position=? and chr=?";
        ConservationScoreMapper q= new ConservationScoreMapper(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);

        return execute(q, startPos,chr);
    }
    public List<ConservationScore> getConservationScoresByPositionsList(int mapKey, String chr, List<Long> positions) throws Exception {
        VariantSearchBean vsb=new VariantSearchBean(mapKey);
        if(vsb.getConScoreTable()!=null && !vsb.getConScoreTable().equals("")) {
            String sql = "select * from " + vsb.getConScoreTable() + " where  chr=? and position in (" +
                    positions.stream().map(p -> p + "").collect(Collectors.joining(",")) + ")";
            ConservationScoreMapper q = new ConservationScoreMapper(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);

            return execute(q, chr);
        }
        return null;
    }
    public List<VariantIndex> getVariantIndexDocs(int mapKey, String chromosome, int limit, int offset) throws Exception {
        String sql="select v.*,vmd.*" ;
        sql+=   " from variant v " +
                " left outer join variant_map_data vmd on (vmd.rgd_id=v.rgd_id) " +
                " where  vmd.map_key=? and vmd.chromosome=?" ;
//                +
//                " Order by v.rgd_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        VariantIndexQuery query=new VariantIndexQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);

        return execute(query, mapKey, chromosome);
    }

    public List<edu.mcw.rgd.datamodel.variants.VariantTranscript> getVariantTranscripts(long rgdId, int mapKey) throws Exception {
        String sql=" select t.*, p.prediction from variant_transcript t left outer join " +
                "                polyphen p on (t.variant_rgd_id=p.variant_rgd_id and t.transcript_rgd_id=p.transcript_rgd_id)\n" +
                "                where t.variant_rgd_id=? " +
                "                and t.map_key=?";
        VariantTranscriptQuery q=new VariantTranscriptQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));

        return q.execute(rgdId, mapKey);
    }

    public List<VariantIndex> getVariantsNewTableStructure(  int mapKey, List<Integer> variantIdsList) throws Exception {

        String sql="select vsd.*,vt.*,t.acc_id as transcript_acc_id,t.protein_acc_id as protein_acc_id, p.prediction , g.rgd_id as gene_rgd_id, g.gene_symbol_lc as gene_symbol_lc, md.strand as strand\n" +
                "   from Variant_sample_detail vsd\n" +
                "   left outer join variant_transcript vt on ( vsd.rgd_id=vt.variant_rgd_id )   \n" +
                "   left outer join transcripts t on (t.transcript_rgd_id=vt.transcript_rgd_id)  \n" +
                "   left outer join genes g on (g.rgd_id=t.gene_rgd_id)  \n" +
                "   left outer join maps_data md on ( md.rgd_id=g.rgd_id)  \n" +
                "   left outer join polyphen  p on (vt.variant_rgd_id =p.variant_rgd_id and vt.transcript_rgd_id=p.transcript_rgd_id)   \n" +
                "          where \n" +
                "          vsd.sample_id in (select sample_id from sample where map_key=?)  \n" +
                "          and vsd.rgd_id in (" ;

        String ids=  variantIdsList.stream().map(Object::toString).collect(Collectors.joining(","));
        sql=sql+ids;
        sql=sql+") ";


        VariantIndexQuery query=new VariantIndexQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        return  execute(query, mapKey);
//        List<VariantIndex> variants=  execute(query, mapKey, mapKey,mapKey);
//        List<VariantIndex> vrList=new ArrayList<>();
//        Set<String> variantIds=new HashSet<>();
//        java.util.Map<String, VariantIndex>  sortedVariants=new HashMap<>();
//        Set<Long> variantIdsWithTrancripts=new HashSet<>();
//        for(VariantIndex variant:variants){
//            variantIdsWithTrancripts.add(variant.getVariant_id());
//            String key=variant.getVariant_id()+"-"+variant.getSampleId()+"-"+variant.getMapKey();
//            if(mapKey==38 || mapKey==17){
//                try {
//                    String clinvarSignificance = getClinvarInfo((int) variant.getVariant_id());
//                    if (clinvarSignificance != null && !clinvarSignificance.equals(""))
//                        variant.setClinicalSignificance(clinvarSignificance);
//                }catch (Exception e){
//                    System.out.println("NO CLINICAL SIGNIFICACE SAMPLE_ID:"+ variant.getSampleId() +" RGD_ID:"+variant.getVariant_id());
//                }
//            }
//            if(!variantIds.contains(key)){
//                variantIds.add(key);
//                sortedVariants.put(key,variant);
//            }else{
//                VariantIndex obj = sortedVariants.get(key);
//                List<VariantTranscript>vtranscripts=new ArrayList<>();
//                boolean exists = false;
//                if(variant.getVariantTranscripts()!=null)
//                    for(VariantTranscript transcript:variant.getVariantTranscripts()) {
//                        if (obj != null) {
//
//                            vtranscripts = obj.getVariantTranscripts();
//                            for (VariantTranscript variantTranscript : vtranscripts) {
//                                if (transcript.getTranscriptRgdId() == variantTranscript.getTranscriptRgdId()) {
//                                    exists = true;
//                                }
//                            }
//                            if (!exists) {
//                                vtranscripts.add(transcript);
//                                obj.setVariantTranscripts(vtranscripts);
//                            }
//                        }
//                    }
//                sortedVariants.put(key, obj);
//            }
//        }
//
//
//        for(Map.Entry e:sortedVariants.entrySet()){
//            vrList.add((VariantIndex) e.getValue());
//        }
//        System.out.println("varaiants size: "+ vrList.size());
//
//        Set<Long> variantIdsWithoutTranscripts=new HashSet<>();
//        if(variantIdsList.size()>variantIdsWithTrancripts.size()){
//            for(int id:variantIdsList){
//                if(!variantIdsWithTrancripts.contains((long)id)){
//                    variantIdsWithoutTranscripts.add((long) id);
//                }
//            }
//            System.out.println("Queried IDS:"+variantIdsList.size()+"\nIds without transcripts:"+ variantIdsWithoutTranscripts.size());
//            if(variantIdsWithoutTranscripts.size()>0) {
//                List<VariantIndex> variantsWithoutTranscripts = getVariantsWithoutTranscripts(mapKey, variantIdsWithoutTranscripts);
//                vrList.addAll(variantsWithoutTranscripts);
//            }
//        }
//        System.out.println("varaiants size include no transcript variants: "+ vrList.size());
//        return vrList;
    }

    public int getMapKeyByVariantId(int variantId) throws Exception {
        String sql="select distinct(vmd.map_key) from variant_map_data vmd where rgd_id=? ";
        IntListQuery q=new IntListQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        List<Integer> results= execute(q, variantId);
        if(results!=null && results.size()>0) return results.get(0);
        return 0;
    }

    public List<VariantSampleDetail> getVariantSampleDetail(int rgdId) throws Exception{
        String sql = "SELECT * FROM variant_sample_detail WHERE rgd_id=?";
        VariantSampleQuery q = new VariantSampleQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId);
    }

    public VariantSampleDetail getVariantSampleDetailByRGDIdSampleId(int rgdId, int sampleId) throws Exception{
        String sql = "SELECT * FROM variant_sample_detail  WHERE rgd_id=? AND sample_id=?";
        VariantSampleQuery q = new VariantSampleQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<VariantSampleDetail> samples = q.execute(rgdId, sampleId);
        if (samples.isEmpty())
            return null;
        return samples.get(0);
    }

    public int getVariantSampleDetailCount(int rgdId, int sampleId) throws Exception{
        String sql = "SELECT count(0) FROM variant_sample_detail  WHERE rgd_id=? AND sample_id=?";
        CountQuery q = new CountQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.getCount(new Object[]{rgdId,sampleId});
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

    public VariantMapData getVariant(int rgdId,int mapKey) throws Exception{
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where v.rgd_id=? and vmd.map_key=?";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<VariantMapData> vmds = q.execute(rgdId,mapKey);
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

    public VariantMapData getVariantByRgdId(int rgdId) throws Exception {
        String sql = """
                select * from (
                SELECT v.*,vm.CHROMOSOME,vm.PADDING_BASE,vm.END_POS,vm.START_POS,vm.GENIC_STATUS,vm.MAP_KEY
                FROM variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and v.rgd_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                UNION ALL
                SELECT v.*,vmd.CHROMOSOME,vmd.PADDING_BASE,vmd.END_POS,vmd.START_POS,vmd.GENIC_STATUS,vmd.MAP_KEY
                FROM variant_ext v, variant_map_data vmd,  RGD_IDS r where v.rgd_id=vmd.rgd_id and v.rgd_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                )""";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<VariantMapData> vmds = q.execute(rgdId, rgdId);
        if (vmds.isEmpty())
            return null;
        return vmds.get(0);
    }

    public List<VariantMapData> getAllVariantsByRgdId(int rgdId) throws Exception{
        String sql = """
                select * from (
                SELECT v.*,vm.CHROMOSOME,vm.PADDING_BASE,vm.END_POS,vm.START_POS,vm.GENIC_STATUS,vm.MAP_KEY
                FROM variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and v.rgd_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                UNION ALL
                SELECT v.*,vmd.CHROMOSOME,vmd.PADDING_BASE,vmd.END_POS,vmd.START_POS,vmd.GENIC_STATUS,vmd.MAP_KEY
                FROM variant_ext v, variant_map_data vmd,  RGD_IDS r where v.rgd_id=vmd.rgd_id and v.rgd_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                )""";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId,rgdId);
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

    public List<VariantMapData> getAllVariantExtByRsId(String rsId) throws Exception {
        String sql = "SELECT v.*,vmd.* FROM variant_ext v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rsId);
    }

    public List<VariantMapData> getAllVariantUnionByRsId(String rsId) throws Exception {
        String sql = """
                SELECT v.*,vmd.* FROM variant v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                UNION ALL
                SELECT v.*,vmd.* FROM variant_ext v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'""";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rsId,rsId);
    }

    public List<VariantMapData> getAllVariantByRsIdAndMapKey(String rsId, int mapKey) throws Exception {
        String sql = "SELECT v.*,vmd.* FROM variant v, variant_map_data vmd, RGD_IDS r  where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vmd.map_key=? order by vmd.chromosome, vmd.start_pos";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rsId,mapKey);
    }

    public List<VariantMapData> getVariantsByPosition(int mapKey, String chr, int start) throws Exception {
        String sql = "SELECT * FROM variant v inner join variant_map_data vmd on v.rgd_id=vmd.rgd_id where vmd.map_key=? and vmd.chromosome=? and vmd.start_pos=?";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey, chr, start);
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

    public List<VariantMapData> getVariantExtWithGeneLocation(int mapKey, String chrom, int start, int stop) throws Exception{
        String sql = """
                select * from (
                SELECT v.*,vm.CHROMOSOME,vm.PADDING_BASE,vm.END_POS,vm.START_POS,vm.GENIC_STATUS,vm.MAP_KEY\s
                FROM variant v, variant_map_data vm, RGD_IDS r\s
                where v.rgd_id=vm.rgd_id and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vm.map_key=? and vm.chromosome=? and vm.start_pos between ? and ?\s
                UNION ALL
                SELECT v.*,vmd.CHROMOSOME,vmd.PADDING_BASE,vmd.END_POS,vmd.START_POS,vmd.GENIC_STATUS,vmd.MAP_KEY\s
                FROM variant_ext v, variant_map_data vmd, RGD_IDS r\s
                where v.rgd_id=vmd.rgd_id and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE' and vmd.map_key=? and vmd.chromosome=? and vmd.start_pos between ? and ?\s
                )
                 order by chromosome, start_pos""";
        VariantMapQuery q= new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(mapKey,chrom,start,stop, mapKey,chrom,start,stop);
    }

    public List<VariantMapData> getAllActiveVariantsByRsId(String rsID) throws Exception {
        String sql = """
                select * from (
                SELECT v.*,vm.CHROMOSOME,vm.PADDING_BASE,vm.END_POS,vm.START_POS,vm.GENIC_STATUS,vm.MAP_KEY\s
                FROM variant v, variant_map_data vm, RGD_IDS r where v.rgd_id=vm.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'\s
                UNION ALL
                SELECT v.*,vmd.CHROMOSOME,vmd.PADDING_BASE,vmd.END_POS,vmd.START_POS,vmd.GENIC_STATUS,vmd.MAP_KEY\s
                FROM variant_ext v, variant_map_data vmd, RGD_IDS r where v.rgd_id=vmd.rgd_id and v.rs_id=? and r.rgd_id=v.rgd_id and r.OBJECT_STATUS='ACTIVE'
                )""";
        VariantMapQuery q = new VariantMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(),sql );
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        return q.execute(rsID,rsID);
    }

    public List<IntStringMapQuery.MapPair> getDistinctPosByRsIdAndMapKey(String rsID, int mapKey) throws Exception {
        String sql = """
                select distinct start_pos, chromosome  from (
                SELECT v.*,vm.CHROMOSOME,vm.PADDING_BASE,vm.END_POS,vm.START_POS,vm.GENIC_STATUS,vm.MAP_KEY
                FROM variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and v.rs_id=? and vm.map_key=?
                UNION ALL
                SELECT v.*,vmd.CHROMOSOME,vmd.PADDING_BASE,vmd.END_POS,vmd.START_POS,vmd.GENIC_STATUS,vmd.MAP_KEY
                FROM variant_ext v, variant_map_data vmd where v.rgd_id=vmd.rgd_id and v.rs_id=? and vmd.map_key=?
                )""";
        IntStringMapQuery q = new IntStringMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rsID,mapKey,rsID,mapKey);
    }

    public List<IntStringMapQuery.MapPair> getDistinctPosByRgdIdAndMapKey(int rgdId, int mapKey) throws Exception {
        String sql = "select distinct vm.start_pos, vm.chromosome from variant v, variant_map_data vm where v.rgd_id=vm.rgd_id and vm.rgd_id=? and vm.map_key=?";
        IntStringMapQuery q = new IntStringMapQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId,mapKey);
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

    public int insertSampleDetail(VariantSampleDetail vs) throws Exception{
        String sql = "INSERT INTO variant_sample_detail (RGD_ID,SAMPLE_ID,TOTAL_DEPTH,VAR_FREQ) VALUES (?,?,?,?)";
        return update(sql,vs.getId(),vs.getSampleId(),vs.getDepth(),vs.getVariantFrequency());
    }
    public void insertVariantExt(Collection<VariantMapData> mapsData)  throws Exception{
        BatchSqlUpdate sql1 = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO variant_ext (" +
                        " RGD_ID,REF_NUC, VARIANT_TYPE, VAR_NUC, RS_ID, CLINVAR_ID, SPECIES_TYPE_KEY)" +
                        "VALUES (?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.INTEGER});
        sql1.compile();
        for( VariantMapData v: mapsData) {
            long id = v.getId();
            sql1.update(id, v.getReferenceNucleotide(), v.getVariantType(), v.getVariantNucleotide(), v.getRsId(), v.getClinvarId(), v.getSpeciesTypeKey());

        }
        sql1.flush();
    }

    public void insertVariants(Collection<VariantMapData> mapsData)  throws Exception{
        BatchSqlUpdate sql1 = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO variant (" +
                        " RGD_ID,REF_NUC, VARIANT_TYPE, VAR_NUC, RS_ID, CLINVAR_ID, SPECIES_TYPE_KEY)" +
                        "VALUES (?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.INTEGER});
        sql1.compile();
        for( VariantMapData v: mapsData) {
            long id = v.getId();
            sql1.update(id, v.getReferenceNucleotide(), v.getVariantType(), v.getVariantNucleotide(), v.getRsId(), v.getClinvarId(), v.getSpeciesTypeKey());

        }
        sql1.flush();
    }

    public void insertVariantMapData(Collection<VariantMapData> mapsData)  throws Exception{
        BatchSqlUpdate sql2 = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO variant_map_data (" +
                        " RGD_ID,CHROMOSOME,START_POS,END_POS,PADDING_BASE,GENIC_STATUS,MAP_KEY)" +
                        "VALUES (?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER,Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,Types.VARCHAR, Types.INTEGER});
        sql2.compile();
        for( VariantMapData v: mapsData) {
            long id = v.getId();
            sql2.update(id, v.getChromosome(), v.getStartPos(), v.getEndPos(), v.getPaddingBase(), v.getGenicStatus(), v.getMapKey());
        }
        sql2.flush();
    }
    public int insertVariantSample(Collection<VariantSampleDetail> sampleData) throws Exception {
        BatchSqlUpdate bsu= new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO variant_sample_detail (" +
                        " RGD_ID,SOURCE,SAMPLE_ID,TOTAL_DEPTH,VAR_FREQ,ZYGOSITY_STATUS,ZYGOSITY_PERCENT_READ," +
                        "ZYGOSITY_POSS_ERROR,ZYGOSITY_REF_ALLELE,ZYGOSITY_NUM_ALLELE,ZYGOSITY_IN_PSEUDO,QUALITY_SCORE)" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER,Types.VARCHAR,Types.INTEGER, Types.INTEGER, Types.INTEGER,Types.VARCHAR, Types.INTEGER,
                        Types.VARCHAR,Types.VARCHAR, Types.INTEGER,Types.VARCHAR, Types.INTEGER});
        bsu.compile();
        for(VariantSampleDetail v: sampleData ) {
            bsu.update(v.getId(), v.getSource(), v.getSampleId(),v.getDepth(),v.getVariantFrequency(),v.getZygosityStatus(),v.getZygosityPercentRead(),
                    v.getZygosityPossibleError(),v.getZygosityRefAllele(),v.getZygosityNumberAllele(),v.getZygosityInPseudo(),v.getQualityScore());
        }
        bsu.flush();
        // compute nr of rows affected
        int totalRowsAffected = 0;
        for( int rowsAffected: bsu.getRowsAffected() ) {
            totalRowsAffected += rowsAffected;
        }
        return totalRowsAffected;
    }

    public int updateVariantSample(Collection<VariantSampleDetail> sampleDetails) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "UPDATE variant_sample_detail set SOURCE=?, TOTAL_DEPTH=?, VAR_FREQ=?, ZYGOSITY_STATUS=?, ZYGOSITY_PERCENT_READ=?, " +
                        "ZYGOSITY_POSS_ERROR=?, ZYGOSITY_REF_ALLELE=?, ZYGOSITY_NUM_ALLELE=?, ZYGOSITY_IN_PSEUDO=?, QUALITY_SCORE=? "+
                        "where RGD_ID=? and SAMPLE_ID=? ",
                new int[]{Types.VARCHAR, Types.INTEGER,Types.INTEGER,Types.VARCHAR,Types.INTEGER,Types.CHAR,
                        Types.CHAR, Types.INTEGER, Types.CHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER});
        su.compile();
        for (VariantSampleDetail v : sampleDetails){
            su.update(v.getSource(), v.getDepth(), v.getVariantFrequency(), v.getZygosityStatus(), v.getZygosityPercentRead(),
                    v.getZygosityPossibleError(), v.getZygosityRefAllele(), v.getZygosityNumberAllele(), v.getZygosityInPseudo(), v.getQualityScore(),
                    v.getId(), v.getSampleId());
        }
        su.flush();

        int totalRowsAffected = 0;
        for( int rowsAffected: su.getRowsAffected() ) {
            totalRowsAffected += rowsAffected;
        }
        return totalRowsAffected;
    }

    public int insertVariantRgdIds(Collection<VariantMapData> vmds) throws Exception{
        BatchSqlUpdate sql = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO VARIANT_RGD_IDS (RGD_ID) VALUES (?)", new int[]{Types.INTEGER},5000);
        sql.compile();
        for (VariantMapData vmd : vmds){
            long rgdId = vmd.getId();
            sql.update(rgdId);
        }
        return executeBatch(sql);
    }
    public int deleteSampleDetailByRgdIdAndSampleId(List<Integer> rgdIds, int sampleId) throws Exception {
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),"delete FROM variant_sample_detail WHERE rgd_id=? and sample_id=?",
                new int[] {Types.INTEGER, Types.INTEGER});
        for (int id : rgdIds){
            su.update(id, sampleId);
        }
        return executeBatch(su);
    }

    public int insertVariantSSIdsBatch(Collection<VariantSSId> ssIds) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "INSERT INTO VARIANT_SS_IDS (VARIANT_RGD_ID, SS_ID, STRAIN_RGD_ID) VALUES (?,?,?)",
                new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER});
        for (VariantSSId v : ssIds){
            su.update(v.getVariantRgdId(), v.getSSId(), v.getStrainRgdId());
        }
        return executeBatch(su);
    }

    public List<VariantSSId> getVariantSSIdsByRgdId(int rgdId) throws Exception{
        String sql = "SELECT * FROM VARIANT_SS_IDS WHERE VARIANT_RGD_ID=?";
        VariantSSIdQuery q = new VariantSSIdQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        return q.execute(rgdId);
    }

    public VariantSSId getVariantSSIdsByRgdIdSSId(int rgdId, String ssId) throws Exception{
        String sql = "SELECT * FROM VARIANT_SS_IDS WHERE VARIANT_RGD_ID=? AND SS_ID=?";
        VariantSSIdQuery q = new VariantSSIdQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        List<VariantSSId> ids = q.execute(rgdId, ssId);
        if (ids.isEmpty())
            return null;
        return ids.get(0);
    }

    public int deleteSSIdBatch(Collection<VariantMapData> vars) throws Exception {
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "delete from variant_ss_ids where variant_rgd_id=?", new int[] {Types.INTEGER});
        for (VariantMapData v : vars){
            su.update(v.getId());
        }
        return executeBatch(su);
    }

    public VariantNotes getVariantNotes(int rgdId) throws Exception{
        String sql = "SELECT * FROM VARIANT_NOTES WHERE RGD_ID=?";
        VariantNotesQuery q = new VariantNotesQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        List<VariantNotes> notes = q.execute(rgdId);
        if (notes.isEmpty())
            return null;
        return notes.get(0);
    }

    public int deleteVariantNotesBatch(List<Integer> rgdIds) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(DataSourceFactory.getInstance().getCarpeNovoDataSource(),
                "delete from VARIANT_NOTES where rgd_id=?", new int[] {Types.INTEGER});
        su.compile();
        for (Integer rgdId : rgdIds){
            su.update(rgdId);
        }
        return executeBatch(su);
    }

    public int updateVariantNotes(Collection<VariantNotes> notes) throws Exception{
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "update VARIANT_NOTES set NOTES=? where RGD_ID=?",
                new int[]{Types.INTEGER, Types.VARCHAR});
        su.compile();
        for (VariantNotes vn : notes){
            su.update(vn.getNotes(), vn.getRgdId());
        }
        return executeBatch(su);
    }
}
