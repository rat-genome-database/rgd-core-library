package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import edu.mcw.rgd.process.describe.DescriptionGenerator;
import edu.mcw.rgd.reporting.Report;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.datamodel.search.RankedIndexItem;
import edu.mcw.rgd.process.search.SearchBean;
import edu.mcw.rgd.process.mapping.MapManager;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 23, 2008
 * Time: 4:22:39 PM
 */
public class ReportDAO extends AbstractDAO {


    private String buildMappingForMapKey(SearchBean sb) throws Exception {

        // map keys for current reference assemblies for rat, mouse and human
        MapManager mm = MapManager.getInstance();

        StringBuilder query = new StringBuilder();
        if (sb.getMap() > 0 && sb.getSpeciesType() > 0) {
            query.append(" AND m.map_key=").append(sb.getMap());
        } else {
            String refMapKeys = mm.getReferenceAssembly(SpeciesType.RAT).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.MOUSE).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.HUMAN).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.CHINCHILLA).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.BONOBO).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.DOG).getKey()
                    +","+mm.getReferenceAssembly(SpeciesType.SQUIRREL).getKey();
            query.append(" AND m.map_key IN(").append(refMapKeys).append(") ");
        }

        if (!sb.getChr().equals("")) {
            query.append(" AND m.chromosome = '").append(sb.getChr()).append("'");
        }
        return query.toString();
    }

    private String buildMappingForPos(SearchBean sb) throws Exception {

        StringBuilder query = new StringBuilder();

        if (!sb.getChr().equals("")) {
            query.append(" AND m.chromosome = '").append(sb.getChr()).append("'");
        }

        if (sb.getStart() != -1) {
            query.append(" AND (m.start_pos >= ").append(sb.getStart())
                    .append(" OR m.stop_pos >= ").append(sb.getStart()).append(")");
        }

        if (sb.getStop() != -1) {
            query.append(" AND (m.stop_pos <= ").append(sb.getStop())
                  .append(" OR m.start_pos <= ").append(sb.getStop()).append(")");
        }
        return query.toString();
    }

    static String buildOntologyFilter(SearchBean sb) throws Exception {

        String ontFilter = null;

        if( !Utils.isStringEmpty(sb.getTermAccId1()) ) {
            ontFilter = buildOntologyFilter(sb.getTermAccId1());
        }

        if( !Utils.isStringEmpty(sb.getTermAccId2()) ) {
            if( ontFilter==null )
                ontFilter = buildOntologyFilter(sb.getTermAccId2());
            else
                ontFilter += buildOntologyFilter(sb.getTermAccId2());
        }

        return ontFilter;
    }

    static private String buildOntologyFilter(String termAcc) {
        return " AND g.rgd_id IN("+
            "SELECT annotated_object_rgd_id FROM full_annot WHERE term_acc IN("+
            "SELECT '"+termAcc+"' FROM dual "+
            "UNION ALL "+
            "SELECT child_term_acc FROM ont_dag d "+
            "START WITH parent_term_acc='"+termAcc+"' "+
            "CONNECT BY PRIOR child_term_acc=parent_term_acc "+
            ")) ";
    }

    public Map<String,String> getAnnotationCounts(Map rgdIds) throws Exception {
        HashMap<String,String> annotCounts = new HashMap<>();
        if( rgdIds==null || rgdIds.isEmpty() )
            return annotCounts;

        try( Connection conn = this.getConnection() ) {

            String query = "select annotated_object_rgd_id, count(*) as count from full_annot where ";
            query = query + " annotated_object_rgd_id in (" + Utils.buildInPhrase(rgdIds.keySet()) + ") group by annotated_object_rgd_id";

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                annotCounts.put(rs.getString("annotated_object_rgd_id"), rs.getString("count"));
            }
        }

        return annotCounts;
    }

    /**
     * analyzes search term of SearchBean to determine searched object rgd id;
     * searched term must end with [qtl] or [marker] or [sslp] or [term]
     * @param sb SearchBean
     * @return rgd id or 0 if invalid searched object
     * @throws Exception
     */
    public int getSearchedObjectRgdId(SearchBean sb) throws Exception {

        int rgdId = 0;
        String termLc = sb.getTerm().toLowerCase();

        if (termLc.endsWith("[qtl]")) {
            String term = sb.getTerm().substring(0, sb.getTerm().indexOf("["));
            QTLDAO qdao = new QTLDAO();
            QTL qtl = qdao.getQTLBySymbol(term, sb.getSpeciesType());

            if (qtl != null) {
                rgdId = qtl.getRgdId();
            }
        }
        else if (termLc.endsWith("[gene]")) {
            String term = sb.getTerm().substring(0, sb.getTerm().indexOf("["));
            GeneDAO qdao = new GeneDAO();
            Gene gene = qdao.getGenesBySymbol(term, sb.getSpeciesType());

            if (gene != null) {
                rgdId = gene.getRgdId();
            }
        }
        else if (termLc.endsWith("[marker]") || termLc.endsWith("[sslp]")
                || termLc.endsWith("[term]")) {
            String term = sb.getTerm().substring(0, sb.getTerm().indexOf("["));
            SSLPDAO qdao = new SSLPDAO();
            List sslpList = qdao.getActiveSSLPsByName(term, sb.getSpeciesType());

            if (!sslpList.isEmpty()) {
                SSLP sslp = (SSLP) sslpList.get(0);
                rgdId = sslp.getRgdId();
            }
        }
        return rgdId;
    }

    public Report getOverlappingGeneReport(SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;
        MapManager mm = MapManager.getInstance();

        int rgdId = getSearchedObjectRgdId(sb);
        if( rgdId==0 )
            return report;

        int mapKey = sb.getMap();
        if (mapKey == -1 && sb.getSpeciesType()!=SpeciesType.ALL ) {
            edu.mcw.rgd.datamodel.Map activeMap = mm.getReferenceAssembly(sb.getSpeciesType());
            mapKey = activeMap.getKey();
            sb.setMap(mapKey);
        }

        try {

            conn = this.getConnection();

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                String query = "select g.rgd_id,g.gene_symbol,g.full_name,md.chromosome,md.start_pos,md.stop_pos,md.fish_band,md.abs_position " +
                        "from Genes g, RGD_IDS r , maps_data md " +
                        "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=g.RGD_ID and md.rgd_id=g.rgd_id " +
                        "and md.chromosome=? " +
                        "and md.start_pos<=? " +
                        "and md.stop_pos>=? " +
                        "and md.map_key=?";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();

                int count = 1000;
                while (rs.next()) {
                    Record r = new Record();
                    int thisRgdId = rs.getInt("rgd_id");
                    String symbol = rs.getString("gene_symbol");

                    r.append(count + "");
                    r.append(thisRgdId + "");
                    r.append(symbol);
                    r.append(rs.getString("full_name"));
                    r.append("");

                    handleMapPosition(rs, sb, r);

                    r.append(SpeciesType.getCommonName(sb.getSpeciesType()));

                    r.append("n/a");
                    r.append("Region");
                    r.append("gene");

                    report.append(r);
                    count--;
                }

            }
            return report;

        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * return count of rows possibly returned by calling getOverlappingGeneReport()
     * @param rgdId rgd id
     * @param mapKey map key
     * @return count of rows in overlapping gene report
     * @throws Exception exception
     */
    public int getOverlappingGeneReportSize(int rgdId, int mapKey) throws Exception {

        Connection conn = null;
        try {

            conn = this.getConnection();
            int count = 0;

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                String query = "select count(*) " +
                        "from Genes g, RGD_IDS r , maps_data md " +
                        "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=g.RGD_ID and md.rgd_id=g.rgd_id " +
                        "and md.chromosome=? " +
                        "and md.start_pos<=? " +
                        "and md.stop_pos>=? " +
                        "and md.map_key=?";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();
                count += rs.getInt(1);
            }

            return count;
        } finally {
            try {
                if( conn!=null ) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Report getActiveGeneReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();
        RankedIndexItem iiEmpty = new RankedIndexItem();
        iiEmpty.setSpeciesTypeKey(sb.getSpeciesType());

        Map<String,String> annotCounts = this.getAnnotationCounts(rgdIds);

        String query = "SELECT g.*, m.* FROM genes g LEFT JOIN maps_data m " +
                "ON g.rgd_id = m.rgd_id " + buildMappingForMapKey(sb);

        if( !rgdIds.isEmpty() )
            query += " WHERE g.rgd_id IN (" + Utils.buildInPhrase(rgdIds.keySet()) + ") \n";
        else {
            // rgdIds.size()==0  meaning that the search from filter as specified by sb.required, sb.negated sb.optional returned 0 results
            int searchFilterKeywordCount = sb.getRequired()==null ? 0 : sb.getRequired().size();
            searchFilterKeywordCount += sb.getNegated()==null ? 0 : sb.getNegated().size();
            searchFilterKeywordCount += sb.getOptional()==null ? 0 : sb.getOptional().size();

            if( searchFilterKeywordCount>0 ) {
                // there were some search terms specified, but they returned no hits
                return report;
            }
            // there were no search terms specified, so no filter should be specified
            query += " WHERE 1=1 \n";
        }
        query += this.buildMappingForPos(sb);

        try( Connection conn = this.getConnection() ){

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");
                String symbol = rs.getString("gene_symbol");

                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                if( ii==null )
                    ii = iiEmpty;

                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(symbol);
                r.append(rs.getString("full_name"));
                r.append(getGeneDescription(rs.getString("gene_desc"), rgdId, ii.getSpeciesTypeKey()));

                handleMapPosition(rs, sb, r);

                r.append(SpeciesType.getCommonName(ii.getSpeciesTypeKey()));
                r.append(annotCounts.get(rgdId + ""));
                r.append(ii.buildSourceString());

                String type = "gene, " + rs.getString("gene_type_lc");
                String refSeqStatus = rs.getString("refseq_status");
                if( refSeqStatus!=null )
                    type += ", " + refSeqStatus + " [RefSeq]";
                r.append(type);

                report.append(r);
            }
        }

        return report;
    }

    /**
     * return gene description: for rat genes by default use auto-generated description;
     * for mouse and human use RefSeq as default gene description
     * @param geneDesc gene description from GENES table
     * @param rgdId gene rgd id
     * @param speciesTypeKey species type key
     * @return gene description
     */
    String getGeneDescription(String geneDesc, int rgdId, int speciesTypeKey) throws Exception {

        // rat genes have always automatically generated description;
        // human and mouse genes only if there is no description from RefSeq
        String description = null;

        DescriptionGenerator dg = new DescriptionGenerator();
        String autoDescription = dg.buildDescription(rgdId);
        if (autoDescription.trim().equals("")) {
            autoDescription = geneDesc;
        }

        if( speciesTypeKey==SpeciesType.RAT )
            description = autoDescription;
        else {
            description = geneDesc;
            if( description==null || description.trim().isEmpty() )
                description = autoDescription;
        }
        return description;
    }

    public Report getActiveQTLReport(Map rgdIds, SearchBean sb) throws Exception {

        Report report = new Report();
        Connection conn = null;
        RankedIndexItem iiEmpty = new RankedIndexItem();
        iiEmpty.setSpeciesTypeKey(sb.getSpeciesType());

        try {

            Map annotCounts = this.getAnnotationCounts(rgdIds);
            conn = this.getConnection();

            // qtl trait and subtrait: VT and CMO annotations if available
            //  if not, notes of type 'qtl_trait' and 'qtl_subtrait'
            String query = "SELECT g.*, m.* \n"+
                    ",NVL2(a1.term_acc, a1.term||' ('||a1.term_acc||')', n1.notes) trait_name \n"+
                    ",NVL2(a2.term_acc, a2.term||' ('||a2.term_acc||')', n2.notes) sub_trait_name \n"+
                    "FROM qtls g \n"+
                    "LEFT JOIN maps_data m ON g.rgd_id = m.rgd_id "+buildMappingForMapKey(sb)+"\n"+
                    "LEFT JOIN full_annot a1 ON a1.annotated_object_rgd_id=g.rgd_id AND a1.aspect='V'\n" +
                    "LEFT JOIN full_annot a2 ON a2.annotated_object_rgd_id=g.rgd_id AND a2.aspect='L'\n" +
                    "LEFT JOIN notes n1 ON n1.rgd_id=g.rgd_id AND n1.notes_type_name_lc='qtl_trait'\n" +
                    "LEFT JOIN notes n2 ON n2.rgd_id=g.rgd_id AND n2.notes_type_name_lc='qtl_subtrait'\n";

            if( !rgdIds.isEmpty() )
                query += " WHERE g.rgd_id IN (" + Utils.buildInPhrase(rgdIds.keySet()) + ") \n";
            else
                query += " WHERE 1=1 \n";
            query += buildMappingForPos(sb);

            // add ontology filter to the query
            String ontologyFilter = buildOntologyFilter(sb);
            if( ontologyFilter!=null )
                query += ontologyFilter;

            logger.debug("REPORT DAO::getActiveQTLReport");
            logger.debug(query);

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            Map<Integer,Record> records = new HashMap<>();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");

                // this block is to prevent creation of multiple lines with same QTL and different traits/subtraits
                // there should be one line per QTL
                Record rPrev = records.get(rgdId);
                String traitName = Utils.defaultString(rs.getString("trait_name"));
                String subtraitName = Utils.defaultString(rs.getString("sub_trait_name"));
                if( rPrev!=null ) {
                    // merge trait names and subtrait names
                    String oldTraitName = rPrev.get(6);
                    if( !oldTraitName.isEmpty() && !oldTraitName.contains(traitName) ) {
                        traitName = oldTraitName + ", "+traitName;
                    }
                    String oldSubtraitName = rPrev.get(7);
                    if( !oldSubtraitName.isEmpty() && !oldSubtraitName.contains(subtraitName) ) {
                        subtraitName = oldSubtraitName + ", "+subtraitName;
                    }

                    rPrev.set(6, traitName);
                    rPrev.set(7, subtraitName);
                    continue;
                }

                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                if( ii==null )
                    ii = iiEmpty;

                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("qtl_symbol"));
                r.append(rs.getString("qtl_name"));
                r.append(rs.getString("lod"));

                String pVal = rs.getString("p_value");
                String pValMlog = rs.getString("P_VAL_MLOG");
                Formatter formatter = new Formatter();
                Double pValf = null;
                try {
                    if (pVal.length()>=10) {
                        pValf = Double.parseDouble(pVal);
                    }
                } catch (Exception e) {
                    pValf = null;
                }
                if (!Utils.isStringEmpty(pValMlog)) {
                    try {
                        double w = Double.parseDouble(pValMlog);
                        int x = (int) Math.ceil(w);
                        double y = x - w;
                        int z = (int) Math.round(Math.pow(10, y));
                        String convertedPVal = z + "e-" + x;
                        r.append(convertedPVal);
                    }
                    catch (Exception e){
                        if (pValf != null) {
                            formatter.format("%1.0e", pValf);
                            r.append(formatter + "");
                        }
                        else
                            r.append(pVal);
                    }
                }
                else {
                    if (pValf != null) {
                        formatter.format("%1.0e", pValf);
                        r.append(formatter + "");
                    }
                    else
                        r.append(pVal);
                }


                r.append(traitName);
                r.append(subtraitName);

                handleMapPosition(rs, sb, r);

                r.append(SpeciesType.getCommonName(ii.getSpeciesTypeKey()));
                r.append((String) annotCounts.get(rgdId + ""));
                r.append(ii.buildSourceString());
                r.append("qtl");

                report.append(r);
                records.put(rgdId, r);
            }

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
        return report;

    }

    public Report getOverlappingQTLReport2(SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        int rgdId = getSearchedObjectRgdId(sb);
        if( rgdId==0 )
            return report;

        int mapKey = sb.getMap();
        if (mapKey == -1 && sb.getSpeciesType()!=SpeciesType.ALL) {
            mapKey = MapManager.getInstance().getReferenceAssembly(sb.getSpeciesType()).getKey();
            sb.setMap(mapKey);
        }
        HashMap<Integer,qtlReport> qtlMap = new HashMap<>();
        try {

            conn = this.getConnection();

            // qtl trait and subtrait: VT and CMO annotations if available
            //  if not, notes of type 'qtl_trait' and 'qtl_subtrait'
            String query = "SELECT q.*, r.*, md.* \n" +
                ",NVL2(a1.term_acc, a1.term||' ('||a1.term_acc||')', n1.notes) trait_name \n" +
                ",NVL2(a2.term_acc, a2.term||' ('||a2.term_acc||')', n2.notes) sub_trait_name \n" +
                "FROM qtls q \n" +
                "JOIN RGD_IDS r ON r.OBJECT_STATUS='ACTIVE' AND r.RGD_ID=q.RGD_ID\n" +
                "JOIN maps_data md ON md.rgd_id=q.rgd_id AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=?\n" +
                "LEFT JOIN full_annot a1 ON a1.annotated_object_rgd_id=q.rgd_id AND a1.aspect='V'\n" +
                "LEFT JOIN full_annot a2 ON a2.annotated_object_rgd_id=q.rgd_id AND a2.aspect='L'\n" +
                "LEFT JOIN notes n1 ON n1.rgd_id=q.rgd_id AND n1.notes_type_name_lc='qtl_trait'\n" +
                "LEFT JOIN notes n2 ON n2.rgd_id=q.rgd_id AND n2.notes_type_name_lc='qtl_subtrait' order by q.rgd_id";

            logger.debug("REPORT DAO::getOverlappingQTLReport");
            logger.debug(query);

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    int thisRgdId = rs.getInt("rgd_id");
                    if (qtlMap.get(thisRgdId)==null) {
                        qtlReport qr = new qtlReport();
                        qr.rgdId = thisRgdId;
                        qr.symbol = rs.getString("qtl_symbol");
                        qr.name = rs.getString("qtl_name");
                        qr.lod = rs.getString("lod");
                        String pVal = rs.getString("p_value");
                        String pValMlog = rs.getString("P_VAL_MLOG");
                        Formatter formatter = new Formatter();
                        Double pValf = null;
                        // if longer than 10 then make double
                        try {
                            if (pVal.length()>=10) {
                                    pValf = Double.parseDouble(pVal);
                            }
                        } catch (Exception e) {
                            pValf = null;
                        }
                        String myPval = "";
                        if (!Utils.isStringEmpty(pValMlog)) {
                            try {
                                double w = Double.parseDouble(pValMlog);
                                int x = (int) Math.ceil(w);
                                double y = x - w;
                                int z = (int) Math.round(Math.pow(10, y));
                                String convertedPVal = z + "e-" + x;
                                myPval = convertedPVal;
                            }
                            catch (Exception e){
                                if (pValf != null) {
                                    formatter.format("%1.0e", pValf);
                                    myPval = formatter + "";
                                }
                                else
                                    myPval = pVal;
                            }
                        }
                        else {
                            if (pValf != null) {
                                formatter.format("%1.0e", pValf);
                                myPval = formatter + "";
                            }
                            else
                                myPval = pVal;
                        }
                        if (!Utils.isStringEmpty(myPval)) {
                            myPval = myPval.replace(".E", "e");
                            qr.pVal = myPval;
                        }
                        String t = rs.getString("trait_name");
                        String st = rs.getString("sub_trait_name");
                        if (!Utils.isStringEmpty(t))
                            qr.trait = t;
                        if (!Utils.isStringEmpty(st))
                            qr.subTrait = st;
                        handleMapPosition(rs,sb,qr);
                        qr.species = SpeciesType.getCommonName(rs.getInt("species_type_key"));
                        qtlMap.put(qr.rgdId, qr);

                    }
                    else{
                        qtlReport qr = qtlMap.get(thisRgdId);
                        String t = rs.getString("trait_name");
                        String st = rs.getString("sub_trait_name");
                        if (Utils.isStringEmpty(qr.trait) && !Utils.isStringEmpty(t))
                            qr.trait = t;
                        else if (!Utils.isStringEmpty(t))
                            qr.trait = ", "+t;
                        if (Utils.isStringEmpty(qr.subTrait) && !Utils.isStringEmpty(st))
                            qr.subTrait = st;
                        else if (!Utils.isStringEmpty(st))
                            qr.subTrait = ", "+st;

                    }
                }

                createQtlReport(qtlMap,report);

            }

        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return report;

    }

    public Report getOverlappingQTLReport(SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        int rgdId = getSearchedObjectRgdId(sb);
        if( rgdId==0 )
            return report;

        int mapKey = sb.getMap();
        if (mapKey == -1 && sb.getSpeciesType()!=SpeciesType.ALL) {
            mapKey = MapManager.getInstance().getReferenceAssembly(sb.getSpeciesType()).getKey();
            sb.setMap(mapKey);
        }
        HashMap<Integer,qtlReport> qtlMap = new HashMap<>();
        try {

            conn = this.getConnection();

            // qtl trait and subtrait: VT and CMO annotations if available
            //  if not, notes of type 'qtl_trait' and 'qtl_subtrait'
            String query = "SELECT q.*, r.*, md.* \n" +
                    ",NVL2(a1.term_acc, a1.term||' ('||a1.term_acc||')', n1.notes) trait_name \n" +
                    ",NVL2(a2.term_acc, a2.term||' ('||a2.term_acc||')', n2.notes) sub_trait_name \n" +
                    "FROM qtls q \n" +
                    "JOIN RGD_IDS r ON r.OBJECT_STATUS='ACTIVE' AND r.RGD_ID=q.RGD_ID\n" +
                    "JOIN maps_data md ON md.rgd_id=q.rgd_id AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=?\n" +
                    "LEFT JOIN full_annot a1 ON a1.annotated_object_rgd_id=q.rgd_id AND a1.aspect='V'\n" +
                    "LEFT JOIN full_annot a2 ON a2.annotated_object_rgd_id=q.rgd_id AND a2.aspect='L'\n" +
                    "LEFT JOIN notes n1 ON n1.rgd_id=q.rgd_id AND n1.notes_type_name_lc='qtl_trait'\n" +
                    "LEFT JOIN notes n2 ON n2.rgd_id=q.rgd_id AND n2.notes_type_name_lc='qtl_subtrait'";

            logger.debug("REPORT DAO::getOverlappingQTLReport");
            logger.debug(query);

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();

                int count=1000;
                while (rs.next()) {
                    Record r = new Record();
                    int thisRgdId = rs.getInt("rgd_id");
                    r.append(count + "");
                    r.append(thisRgdId + "");
                    r.append(rs.getString("qtl_symbol"));
                    r.append(rs.getString("qtl_name"));
                    r.append(rs.getString("lod"));

                    String pVal = rs.getString("p_value");
                    String pValMlog = rs.getString("P_VAL_MLOG");
                    Formatter formatter = new Formatter();
                    Double pValf = null;
                    // if longer than 10 then make double
                    try {
                        if (pVal.length()>=10) {
                                pValf = Double.parseDouble(pVal);
                        }
                    } catch (Exception e) {
                        pValf = null;
                    }
                    String myPval = "";
                    if (!Utils.isStringEmpty(pValMlog)) {
                        try {
                            double w = Double.parseDouble(pValMlog);
                            int x = (int) Math.ceil(w);
                            double y = x - w;
                            int z = (int) Math.round(Math.pow(10, y));
                            String convertedPVal = z + "e-" + x;
                            myPval = convertedPVal;
                        }
                        catch (Exception e){
                            if (pValf != null) {
                                formatter.format("%1.0e", pValf);
                                myPval = formatter + "";
                            }
                            else
                                myPval = pVal;
                        }
                    }
                    else {
                        if (pValf != null) {
                            formatter.format("%1.0e", pValf);
                            myPval = formatter + "";
                        }
                        else
                            myPval = pVal;
                    }
                    if (!Utils.isStringEmpty(myPval)) {
                        myPval = myPval.replace(".E", "e");
                        r.append(myPval);
                    }
                    else
                        r.append("");

                    r.append(rs.getString("trait_name"));
                    r.append(rs.getString("sub_trait_name"));

                    handleMapPosition(rs, sb, r);

                    r.append(SpeciesType.getCommonName(rs.getInt("species_type_key")));
                    r.append("n/a");
                    r.append("region");
                    r.append("qtl");

                    report.append(r);
                }
            }

        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return report;

    }

    public Report getActiveReferenceReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        if (rgdIds == null || rgdIds.size() == 0)
            return report;

        try {

            String query = "select g.*, x.acc_id from references g, rgd_acc_xdb x " +
                    "where g.rgd_id=x.rgd_id(+) and x.xdb_key(+)=2 and g.rgd_id in (" + Utils.buildInPhrase(rgdIds.keySet()) + ")";

            conn = this.getConnection();

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");
                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("title"));
                r.append(rs.getString("citation"));
                r.append(rs.getString("abstract"));
                //r.append(rs.getString("volume"));
                //r.append(rs.getString("issue"));
                //r.append(rs.getString("pages"));
                r.append(rs.getString("acc_id"));

                java.sql.Date dt = rs.getDate("pub_date");
                r.append(dt==null ? null : dt.toString());

                report.append(r);
            }

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
        return report;
    }

    public Report getActiveSSLPReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;
        RankedIndexItem iiEmpty = new RankedIndexItem();
        iiEmpty.setSpeciesTypeKey(sb.getSpeciesType());

        try {

            conn = this.getConnection();

            String query = "SELECT g.*, m.* FROM sslps g LEFT JOIN maps_data m " +
                    "ON g.rgd_id = m.rgd_id " + buildMappingForMapKey(sb);

            if( !rgdIds.isEmpty() )
                query += " WHERE g.rgd_id IN (" + Utils.buildInPhrase(rgdIds.keySet()) + ") \n";
            else
                query += " WHERE 1=1 \n";
            query += buildMappingForPos(sb);

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");

                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                if( ii==null )
                    ii = iiEmpty;

                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("rgd_name"));
                r.append(rs.getString("expected_size"));

                handleMapPosition(rs, sb, r);

                r.append(SpeciesType.getCommonName(ii.getSpeciesTypeKey()));
                r.append(ii.buildSourceString());
                r.append("sslp");

                report.append(r);
            }

            return report;

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    public Report getOverlappingSSLPReport( SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        int rgdId = getSearchedObjectRgdId(sb);
        if( rgdId==0 )
            return report;

        int mapKey = sb.getMap();
        if (mapKey == -1 && sb.getSpeciesType()!=SpeciesType.ALL) {
            edu.mcw.rgd.datamodel.Map activeMap = MapManager.getInstance().getReferenceAssembly(sb.getSpeciesType());
            mapKey = activeMap.getKey();
            sb.setMap(mapKey);
        }

        try {

            conn = this.getConnection();

            String query = "select s.rgd_id,s.rgd_name,s.expected_size,r.species_type_key,"+
                    "md.chromosome,md.start_pos,md.stop_pos,md.fish_band,md.abs_position " +
                "from sslps s, rgd_ids r, maps_data md " +
                "where r.object_status='ACTIVE' and r.rgd_id=s.rgd_id and md.rgd_id=s.rgd_id " +
                "and md.chromosome=? " +
                "and md.start_pos<=? " +
                "and md.stop_pos>=? " +
                "and md.map_key=?";

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();

                int count=1000;
                while (rs.next()) {
                    Record r = new Record();
                    r.setObjectKey(RgdId.OBJECT_KEY_SSLPS);

                    int thisRgdId = rs.getInt("rgd_id");
                    r.append(count + "");
                    r.append(thisRgdId + "");
                    r.append(rs.getString("rgd_name"));
                    r.append(rs.getString("expected_size"));

                    handleMapPosition(rs, sb, r);

                    r.append(SpeciesType.getCommonName(rs.getInt("species_type_key")));
                    r.append("region");
                    r.append("sslp");

                    report.append(r);
                }
            }

            return report;

        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Report getActiveStrainReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        if (rgdIds == null || rgdIds.size() == 0)
            return report;

        try {

            Map annotCounts = this.getAnnotationCounts(rgdIds);

            String query = "select g.* from strains g " +
                    "where rgd_id in (" + Utils.buildInPhrase(rgdIds.keySet()) + ")";

            conn = this.getConnection();

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");
                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("strain_symbol"));
                r.append(rs.getString("full_name"));
                r.append(rs.getString("origin"));
                r.append(rs.getString("source"));
                r.append(rs.getString("strain_type_name_lc"));
                r.append((String) annotCounts.get(rgdId + ""));
                r.append(ii.buildSourceString());

                report.append(r);
            }
        } finally {
            try {
                if( conn!=null ) conn.close();
            } catch (Exception ignored) {
            }
        }
        return report;
    }

    public Report getActiveGenomicElementReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();
        Connection conn = null;

        if (rgdIds == null || rgdIds.size() == 0) return report;

        try {

            conn = this.getConnection();

            String query = "SELECT g.*, m.* FROM genomic_elements g LEFT JOIN maps_data m " +
                    "ON g.rgd_id = m.rgd_id " + buildMappingForMapKey(sb);

            query += " WHERE g.rgd_id IN (" + Utils.buildInPhrase(rgdIds.keySet()) + ") ";
            query += buildMappingForPos(sb);

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");
                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("symbol"));
                r.append(rs.getString("name"));
                r.append(rs.getString("description"));

                handleMapPosition(rs, sb, r);

                r.append(SpeciesType.getCommonName(ii.getSpeciesTypeKey()));
                r.append(ii.buildSourceString());
                r.append("genomic_element");

                report.append(r);
            }

            return report;

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // return list of regions for given rgdId and mapKey; one per chromosome
    List<String[]> getOverlappingRegions(int rgdId, int mapKey) throws Exception {

        List<String[]> results = new ArrayList<String[]>();
        Connection conn = null;
        try {
            conn = this.getConnection();

            // due to inconsistencies in data, some objects could have multiple locations on the same map
            // or even locations on multiple chromosomes
            // or multiple loci
            String query = "select md.chromosome,min(start_pos) start_pos,max(stop_pos) stop_pos "+
                    "from RGD_IDS r, maps_data md "+
                    "where r.OBJECT_STATUS='ACTIVE' and md.rgd_id=r.rgd_id "+
                    "and md.map_key=? and r.rgd_id=? "+
                    "group by chromosome";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, mapKey);
            ps.setInt(2, rgdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                String minStartPos = rs.getString(2);
                String maxStartPos = rs.getString(3);
                if( minStartPos!=null && maxStartPos!=null ) {
                    results.add(new String[]{rs.getString(1), minStartPos, maxStartPos});
                }
            }
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private void handleMapPosition(ResultSet rs, SearchBean sb, Record r) throws Exception {

        r.append(rs.getString("chromosome"));

        String mapUnit = null;
        if( sb.getSpeciesType()!=0 ) {
            edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
            if( m!=null )
                mapUnit = m.getUnit();
        }
        if( mapUnit==null || mapUnit.equals("bp")) {
            r.append(rs.getString("start_pos"));
            r.append(rs.getString("stop_pos"));
        } else if (mapUnit.equals("band")) {
            r.append(rs.getString("fish_band"));
            r.append("");
        } else {
            r.append(rs.getString("abs_position"));
            r.append("");
        }
    }

    private void handleMapPosition(ResultSet rs, SearchBean sb, qtlReport r) throws Exception {

        r.chr = rs.getString("chromosome");

        String mapUnit = null;
        if( sb.getSpeciesType()!=0 ) {
            edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
            if( m!=null )
                mapUnit = m.getUnit();
        }
        r.mapUnit = mapUnit;
        if( mapUnit==null || mapUnit.equals("bp")) {
            r.start = rs.getString("start_pos");
            r.stop = rs.getString("stop_pos");
        } else if (mapUnit.equals("band")) {
            r.band = rs.getString("fish_band");
//            r.append("");
        } else {
            r.absPos = rs.getString("abs_position");
//            r.append("");
        }
    }

    public Report getActiveVariantReport(Map rgdIds, SearchBean sb) throws Exception {
        Report report = new Report();

        if (rgdIds == null || rgdIds.size() == 0) return report;

        try( Connection conn = this.getConnection() ) {

            Map<String,String> annotCounts = this.getAnnotationCounts(rgdIds);

            String query = "SELECT g.object_type,g.symbol,g.name,g.rgd_id,v.trait_name,v.clinical_significance,m.* "+
                    "FROM genomic_elements g \n"+
                    "LEFT JOIN maps_data m ON g.rgd_id = m.rgd_id " + buildMappingForMapKey(sb) +
                    " INNER JOIN clinvar v ON g.rgd_id=v.rgd_id " +
                    " WHERE g.rgd_id IN (" + Utils.buildInPhrase(rgdIds.keySet()) + ") " +
                    buildMappingForPos(sb);

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Record r = new Record();
                int rgdId = rs.getInt("rgd_id");
                RankedIndexItem ii = (RankedIndexItem) rgdIds.get(rgdId);
                r.append(ii.getRank() + "");
                r.append(rgdId + "");
                r.append(rs.getString("symbol"));
                r.append(rs.getString("object_type"));
                r.append(rs.getString("name"));
                r.append(rs.getString("trait_name"));
                r.append(rs.getString("clinical_significance"));

                handleMapPosition(rs, sb, r);

                r.append(SpeciesType.getCommonName(ii.getSpeciesTypeKey()));
                r.append(annotCounts.get(rgdId + ""));
                r.append(ii.buildSourceString());

                report.append(r);
            }

            return report;

        }
    }

    public Report getOverlappingVariantReport(SearchBean sb) throws Exception {
        Report report = new Report();

        int rgdId = getSearchedObjectRgdId(sb);
        if( rgdId==0 )
            return report;

        int mapKey = sb.getMap();
        if (mapKey == -1 && sb.getSpeciesType()!=SpeciesType.ALL) {
            mapKey = MapManager.getInstance().getReferenceAssembly(sb.getSpeciesType()).getKey();
            sb.setMap(mapKey);
        }

        try( Connection conn = this.getConnection() ) {

            for( String[] row: getOverlappingRegions(rgdId, mapKey) ) {

                String chr = row[0];
                int startPos = Integer.parseInt(row[1]);
                int stopPos = Integer.parseInt(row[2]);

                String query = "select ge.*, r.*, md.* , v.* " +
                    "from genomic_elements ge, rgd_ids r, maps_data md, clinvar v " +
                    "where r.object_status='ACTIVE' and r.rgd_id=ge.rgd_id and md.rgd_id=ge.rgd_id and ge.rgd_id=v.rgd_id " +
                    "and md.chromosome=? " +
                    "and md.start_pos<=? " +
                    "and md.stop_pos>=? " +
                    "and md.map_key=?";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, chr);
                ps.setInt(2, stopPos);
                ps.setInt(3, startPos);
                ps.setInt(4, mapKey);

                ResultSet rs = ps.executeQuery();

                int count=1000;
                while (rs.next()) {
                    Record r = new Record();
                    int thisRgdId = rs.getInt("rgd_id");
                    r.append(count + "");
                    r.append(thisRgdId + "");
                    r.append(rs.getString("object_type"));
                    r.append(rs.getString("name"));
                    r.append(rs.getString("clinical_significance"));

                    handleMapPosition(rs, sb, r);

                    r.append(SpeciesType.getCommonName(rs.getInt("species_type_key")));
                    r.append("");
                    r.append("");
                    r.append("variant");

                    report.append(r);
                }
            }

        }
        return report;

    }

    public void createQtlReport(HashMap<Integer, qtlReport> qtlMap, Report report) throws Exception{
        int count = 1000;
        for (Integer id : qtlMap.keySet()){
            qtlReport qr = qtlMap.get(id);
            Record r = new Record();
            r.append(count + "");
            r.append(qr.rgdId+"");
            r.append(qr.symbol);
            r.append(qr.name);
            r.append(qr.lod);
            r.append(qr.pVal);
            if (qr.trait != null && qr.trait.startsWith(",")){
                qr.trait = qr.trait.substring(2);
            }
            r.append(qr.trait);
            if (qr.subTrait != null && qr.subTrait.startsWith(",")){
                qr.subTrait = qr.subTrait.substring(2);
            }
            r.append(qr.subTrait);
            r.append(qr.chr);
            String mapUnit = qr.mapUnit;
            if( mapUnit==null || mapUnit.equals("bp")) {
                r.append(qr.start);
                r.append(qr.stop);
            } else if (mapUnit.equals("band")) {
                r.append(qr.band);
                r.append("");
            } else {
                r.append(qr.absPos);
                r.append("");
            }
            r.append(qr.species);
            r.append("n/a");
            r.append("region");
            r.append("qtl");

            report.append(r);
        }
    }
    public class qtlReport{
        int rgdId;
        String symbol;
        String name;
        String lod;
        String pVal;
        String trait = "";
        String subTrait = "";
        String chr;
        String start;
        String stop;
        String band;
        String absPos;
        String species;
        String annots;
        String match;
        String type;
        String mapUnit;
    }
}
