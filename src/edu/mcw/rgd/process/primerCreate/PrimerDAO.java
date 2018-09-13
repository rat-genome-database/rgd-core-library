package edu.mcw.rgd.process.primerCreate;

import edu.mcw.rgd.dao.AbstractDAO;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: 9/8/13
 * Time: 10:17 PM
 * <p>
 * DAO for primer data from Ensembl mysql core database
 */
public class PrimerDAO extends AbstractDAO{

    public static String DBVER = "75_37";

    public ArrayList<EnsemblVariation> getGenicVariants(EnsemblGene g, String posChr, int posStart, int posStop) throws Exception{

        String commaSeparatedTr = "";
        if(g.getEnsemblTranscripts().size()>0){
            for(EnsemblTranscript t : g.getEnsemblTranscripts()){
                commaSeparatedTr+="'"+t.getEnsTranscriptName()+"',";
            }
        }
        commaSeparatedTr = commaSeparatedTr.substring(0, (commaSeparatedTr.length()-1));

        ArrayList<EnsemblVariation> acc = new ArrayList<EnsemblVariation>();

        Connection conn = null;
        try {
            conn =  getEnsemblConnection();
        }catch (Exception connectionException){
            connectionException.printStackTrace();
        }

        String sql = "SELECT DISTINCT \n" +
            "vf.variation_name, vf.variation_id, vf.consequence_types, vf.seq_region_id, sq.name, vf.seq_region_start, " +
            "vf.seq_region_end, vf.seq_region_strand, vf.source_id, vf.allele_string, s.name \n" +
            "FROM homo_sapiens_variation_"+DBVER+".variation_feature vf \n" +
            "JOIN homo_sapiens_variation_"+DBVER+".transcript_variation tv \n" +
            "USING (variation_feature_id) \n" +
            "JOIN homo_sapiens_variation_"+DBVER+".source s \n" +
            "USING (source_id) \n" +
            "JOIN homo_sapiens_variation_"+DBVER+".seq_region sq \n" +
            "USING (seq_region_id) \n" +
            "LEFT JOIN homo_sapiens_variation_"+DBVER+".failed_variation fv \n" +
            "ON (vf.variation_id = fv.variation_id) \n" +
            "WHERE tv.feature_stable_id IN (" + commaSeparatedTr + ") \n" +
            "AND fv.variation_id IS NULL \n" +
                " AND sq.name=?\n" +
                " AND vf.seq_region_start>=?\n" +
                " AND vf.seq_region_end<=?\n";

        logger.debug(sql+", Chr"+posChr+":"+posStart+".."+posStop);
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, posChr);
        ps.setInt(2, posStart);
        ps.setInt(3, posStop);

        try{
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EnsemblVariation ev = new EnsemblVariation();
                ev.setVarName(rs.getString("variation_name"));
                ev.setVid(rs.getString("variation_id"));
                ev.setConsequenceType(rs.getString("consequence_types"));
                ev.setvSeq_region_id(rs.getString("seq_region_id"));
                ev.setvChr(rs.getString("name"));
                int start = Integer.parseInt(rs.getString("seq_region_start"));
                int end = Integer.parseInt(rs.getString("seq_region_end"));
                String strand = rs.getString("seq_region_strand");
                if(g.geteGStrand().equalsIgnoreCase("-1") && (start>end) && (strand.equals("1"))){
                    ev.setVstart(end);
                    ev.setVstop(start);
                    ev.setvStrand(g.geteGStrand());
                }else if(g.geteGStrand().equalsIgnoreCase("1") && (start<end) && (strand.equals("1"))){
                    ev.setVstart(Integer.parseInt(rs.getString("seq_region_start")));
                    ev.setVstop(Integer.parseInt(rs.getString("seq_region_end")));
                    ev.setvStrand(g.geteGStrand());
                }else{
                    ev.setVstart(Integer.parseInt(rs.getString("seq_region_start")));
                    ev.setVstop(Integer.parseInt(rs.getString("seq_region_end")));
                    ev.setvStrand(rs.getString("seq_region_strand"));
                }
                ev.setAlleleString(rs.getString("allele_string"));
                ev.setvSource(rs.getString("source_id"));

                acc.add(ev);
            }

        }catch (Exception excecutionException){
            excecutionException.printStackTrace();
        }finally {
            try {
               conn.close();
            }catch (Exception closeConn) {
                closeConn.printStackTrace();
            }
        }
        return acc;

    }

    public ArrayList<EnsemblVariation> getOtherVariantsWithinBuffer(EnsemblVariation ev, int newStart, int newStop) throws Exception{

        ArrayList<EnsemblVariation> acc = new ArrayList<EnsemblVariation>();

        Connection conn = null;
        try {
            conn =  getEnsemblConnection();
        }catch (Exception connectionException){
            connectionException.printStackTrace();
        }

        String sql = "SELECT DISTINCT \n" +
            "vf.variation_name, vf.variation_id, vf.consequence_types, vf.seq_region_id, sq.name, vf.seq_region_start, " +
            "vf.seq_region_end, vf.seq_region_strand, vf.source_id, vf.allele_string, s.name \n" +
            "FROM homo_sapiens_variation_"+DBVER+".variation_feature vf \n" +
            "JOIN homo_sapiens_variation_"+DBVER+".source s \n" +
            "USING (source_id) \n" +
            "JOIN homo_sapiens_variation_"+DBVER+".seq_region sq \n" +
            "USING (seq_region_id) \n" +
            "LEFT JOIN homo_sapiens_variation_"+DBVER+".failed_variation fv \n" +
            "ON (vf.variation_id = fv.variation_id) \n" +
            "WHERE " +
                " vf.seq_region_start>="+ newStart +
                " AND vf.seq_region_end<="+ newStop +
                " AND vf.seq_region_id="+ev.getvSeq_region_id()+"\n" +
            "AND fv.variation_id IS NULL";

        logger.debug(sql);
        PreparedStatement ps = conn.prepareStatement(sql);

        try{
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EnsemblVariation bufferEv = new EnsemblVariation();
                bufferEv.setVarName(rs.getString("variation_name"));
                bufferEv.setVid(rs.getString("variation_id"));
                bufferEv.setConsequenceType(rs.getString("consequence_types"));
                bufferEv.setvSeq_region_id(rs.getString("seq_region_id"));
                bufferEv.setvChr(rs.getString("name"));
                bufferEv.setVstart(Integer.parseInt(rs.getString("seq_region_start")));
                bufferEv.setVstop(Integer.parseInt(rs.getString("seq_region_end")));
                bufferEv.setvStrand(rs.getString("seq_region_strand"));
                bufferEv.setAlleleString(rs.getString("allele_string"));
                bufferEv.setvSource(rs.getString("source_id"));

                acc.add(bufferEv);
            }

        }catch (Exception excecutionException){
            excecutionException.printStackTrace();
        }finally {
            try {
               conn.close();
            }catch (Exception closeConn) {
                closeConn.printStackTrace();
            }
        }
        return acc;

    }




    public ArrayList<EnsemblExon> getExonsForTranscript(EnsemblTranscript etObj) throws Exception{

        ArrayList<EnsemblExon> accEx = new ArrayList<EnsemblExon>();

        Connection conn = null;
        try {

            conn =  getEnsemblConnection();
        }catch (Exception connectionException){
            connectionException.printStackTrace();
        }
            String sql = "\n" +
                    "select e.exon_id, et.rank, e.seq_region_id, e.seq_region_start, e.seq_region_end, e.seq_region_strand, \n" +
                    "e.phase, e.stable_id \n" +
                    "from homo_sapiens_core_"+DBVER+".exon e\n" +
                    "join homo_sapiens_core_"+DBVER+".exon_transcript et\n" +
                    "using (exon_id) \n" +
                    "where et.transcript_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, etObj.getEnsTranscriptId());

        try{
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                EnsemblExon ex = new EnsemblExon();
                ex.setEnsExonId(rs.getString("exon_id"));
                ex.setExonNumber(rs.getString("rank"));
                ex.setEnsExonStart(rs.getInt("seq_region_start"));
                ex.setEnsExonStop(rs.getInt("seq_region_end"));
                ex.setEnsExonStrand(rs.getString("seq_region_strand"));
                ex.setEnsExonName(rs.getString("stable_id"));
                ex.setEnsExonPhase(rs.getString("phase"));
                ex.setExonParentTranscriptAccId(etObj.getEnsTranscriptName());
                accEx.add(ex);
            }

        }catch (Exception excecutionException){
            excecutionException.printStackTrace();
        }finally {
            try {
               conn.close();
            }catch (Exception closeConn) {
                closeConn.printStackTrace();
            }
        }
        return accEx;

    }


    public ArrayList<EnsemblTranscript> getTranscriptsForGene(EnsemblGene eg) throws Exception{

        ArrayList<EnsemblTranscript> acc = new ArrayList<EnsemblTranscript>();

        Connection conn = null;
        try {

            conn =  getEnsemblConnection();
        }catch (Exception connectionException){
            connectionException.printStackTrace();
        }
            logger.debug("getting transcripts for gene:"+ eg.getEnsGeneSymbol()+", GeneId="+eg.getGeneId());

            String sql = "select t.gene_id, t.transcript_id, t.stable_id, t.seq_region_id, t.seq_region_start, \n" +
                    "t.seq_region_end, x.dbprimary_acc \n" +
                    "from homo_sapiens_core_"+DBVER+".transcript t \n" +
                    "join homo_sapiens_core_"+DBVER+".object_xref ox \n" +
                    "on ox.ensembl_id=t.transcript_id \n" +
                    "join homo_sapiens_core_"+DBVER+".xref x \n" +
                    "using (xref_id) \n" +
                    "where x.external_db_id=3800 and \n" +
                    "t.gene_id=?";

            logger.debug(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, eg.getGeneId());
        try{
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EnsemblTranscript et = new EnsemblTranscript();
                et.setEnsGeneId(rs.getString("gene_id"));
                et.setEnsTranscriptId(rs.getString("transcript_id"));
                et.setEnsTranscriptName(rs.getString("stable_id"));
                et.setTrChr(rs.getString("seq_region_id"));
                et.setTrStart(rs.getInt("seq_region_start"));
                et.setTrStop(rs.getInt("seq_region_end"));
                et.setCcdsId(rs.getString("dbprimary_acc"));
                acc.add(et);

                logger.debug("   "+et.getEnsTranscriptId()+" "+et.getEnsTranscriptName());
            }

        }catch (Exception excecutionException){
            excecutionException.printStackTrace();
        }finally {
            try {
               conn.close();
            }catch (Exception closeConn) {
                closeConn.printStackTrace();
            }
        }
        logger.debug("   found transcripts:"+acc.size());
        return acc;
    }


    public EnsemblGene getGene(String ensId) throws Exception{

        EnsemblGene eg = new EnsemblGene();
        Connection conn = null;
        try {

            conn =  getEnsemblConnection();
        }catch (Exception connectionException){
            connectionException.printStackTrace();
        }
            logger.debug("getGene "+ensId);
            String sql = "select g.gene_id, g.stable_id, g.seq_region_id, sq.name, \n" +
                    "g.seq_region_start, g.seq_region_end, g.seq_region_strand,\n" +
                    "g.description\n" +
                    "from homo_sapiens_core_"+DBVER+".gene g\n" +
                    "JOIN homo_sapiens_variation_"+DBVER+".seq_region sq\n" +
                    "USING (seq_region_id) \n" +
                    "where g.stable_id=?";

            logger.debug(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ensId);
        try{
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eg.setGeneId(rs.getString("gene_id"));
                eg.setEnsGeneId(rs.getString("stable_id"));
                eg.setEnsGeneSymbol(rs.getString("stable_id"));
                eg.setEnsGeneName(rs.getString("description"));
                eg.setSeqRegionId(rs.getString("seq_region_id"));
                eg.setEgChr(rs.getString("name"));
                eg.seteGStart(Integer.parseInt(rs.getString("seq_region_start")));
                eg.setEgStop(Integer.parseInt(rs.getString("seq_region_end")));
                eg.seteGStrand(rs.getString("seq_region_strand"));
            }

        }catch (Exception excecutionException){
            excecutionException.printStackTrace();
        }finally {
            try {
               conn.close();
            }catch (Exception closeConn) {
                closeConn.printStackTrace();
            }
        }
        logger.debug("found gene ["+ eg.getEnsGeneName()+"] GeneId="+eg.getGeneId());
        return eg;
    }

    public Connection getEnsemblConnection() throws Exception{

        Connection conn = null;
        String url = "jdbc:mysql://ensembldb.ensembl.org:3306/";
        String dbName = "homo_sapiens_core_"+DBVER;
        String driver = "com.mysql.jdbc.Driver";
        String userName = "anonymous";
        String password = "";
        for( int i=0; i<3; i++ ) {

            try {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url + dbName, userName, password);
                logger.debug("Connected to the database"+conn);
                Statement stmt = conn.createStatement();
                stmt.executeQuery("SELECT 1");
                stmt.close();
                break;
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        return conn;
    }
}



