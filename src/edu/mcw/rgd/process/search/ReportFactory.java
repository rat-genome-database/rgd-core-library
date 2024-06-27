package edu.mcw.rgd.process.search;

import edu.mcw.rgd.dao.impl.AssociationDAO;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.search.RankedIndexItem;
import edu.mcw.rgd.reporting.Report;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.datamodel.search.GeneralSearchResult;
import edu.mcw.rgd.dao.impl.SearchDAO;
import edu.mcw.rgd.dao.impl.ReportDAO;
import edu.mcw.rgd.process.mapping.MapManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 25, 2008
 * Time: 4:21:58 PM
 * <p>
 * factory class for building Report objects
 */
public class ReportFactory {

    private static ReportFactory fac = new ReportFactory();

    private ReportFactory(){

    }

    public static ReportFactory getInstance() {
        return fac;
    }

    public Report getGeneReport(SearchBean sb) throws Exception{
        //Stamp.it("enter Gene Report");
        SearchDAO dao = new SearchDAO();

        Report report = new Report();
        ReportDAO gdao = new ReportDAO();

        try {
        if (sb.getTerm().endsWith("]")) {
            report = gdao.getOverlappingGeneReport(sb);
        }else {
            Map rankedList =  dao.getRankedRGDIds(sb, "GENES");

            if( !rankedList.isEmpty() || sb.isSearchable() ) {
                report = gdao.getActiveGeneReport(rankedList, sb);
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }

        Record header = new Record();
        header.append("Relevance");

        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }

        header.append("Symbol");
        header.append("Name");
        header.append("Description");
        header.append("Chr");

        edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
        if (m != null && m.getUnit().equals("band")) {
            header.append("Fish Band");
            header.append("");
        }else if (m != null && (m.getUnit().toLowerCase().contains("cm") || m.getUnit().toLowerCase().contains("cr"))) {
            header.append("cM/cR");
            report.addSortMapping(6, Report.NUMERIC_SORT);
            header.append("");
        }else {
            header.append("Start");
            report.addSortMapping(6, Report.NUMERIC_SORT);
            header.append("Stop");
        }

        header.append("Species");
        header.append("Annotations");
        header.append("Match");
        header.append("Type");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(5, Report.CHROMOSOME_SORT);
        report.addSortMapping(7, Report.NUMERIC_SORT);
        report.addSortMapping(9, Report.NUMERIC_SORT);

        //Stamp.it("end Gene Report");

        return report;
    }

    public Report getQTLReport(SearchBean sb) throws Exception{
        //Stamp.it("enter QTL Report");
        SearchDAO dao = new SearchDAO();

        ReportDAO gdao = new ReportDAO();
        Report report = new Report();

        if (sb.getTerm().endsWith("]")) {
            report = gdao.getOverlappingQTLReport2(sb);
        }else {
            Map<Integer, RankedIndexItem> rankedList = dao.getRankedRGDIds(sb, "QTLS");

            // add qtls with matching curated referecnes to the list of ranked objects
            Map<Integer, RankedIndexItem> refRankedList = dao.getRankedRGDIds(sb, "REFERENCES");
            remapReferenceRgdIds(refRankedList, RgdId.OBJECT_KEY_QTLS);
            mergeRankedLists(rankedList, refRankedList);

            if( !rankedList.isEmpty() || sb.isSearchable() ) {
                report = gdao.getActiveQTLReport(rankedList, sb);
            }
        }

        Record header = new Record();
        header.append("Relevance");

        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }

        header.append("Symbol");
        header.append("Name");
        header.append("LOD");
        header.append("P Value");
        header.append("Trait");
        header.append("Sub Trait");
        header.append("Chr");


        edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
        if (m != null && m.getUnit().equals("band")) {
            header.append("Fish Band");
            header.append("");
        }else if (m != null && (m.getUnit().toLowerCase().contains("cm") || m.getUnit().toLowerCase().contains("cr"))) {
            header.append("cM/cR");
            report.addSortMapping(9, Report.NUMERIC_SORT);
            header.append("");
        }else {
            header.append("Start");
            report.addSortMapping(9, Report.NUMERIC_SORT);
            header.append("Stop");
        }


        header.append("Species");
        header.append("Annotations");
        header.append("Match");
        header.append("Type");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(4, Report.NUMERIC_SORT);
        report.addSortMapping(5, Report.NUMERIC_SORT);
        report.addSortMapping(8, Report.CHROMOSOME_SORT);
        report.addSortMapping(10, Report.NUMERIC_SORT);
        report.addSortMapping(12, Report.NUMERIC_SORT);
        //Stamp.it("end QTL Report");

        return report;
    }

    public Report getStrainReport(SearchBean sb) throws Exception{
        //Stamp.it("enter Strain Report");
        SearchDAO dao = new SearchDAO();

        Map rankedList =  dao.getRankedRGDIds(sb, "STRAINS");

        ReportDAO gdao = new ReportDAO();

        Report report = new Report();

        if (sb.getRequired().size() > 0) {
            report = gdao.getActiveStrainReport(rankedList, sb);
        }

        Record header = new Record();
        header.append("Relevance");
        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }
        header.append("Symbol");
        header.append("Name");
        header.append("Origin");
        header.append("Source");
        header.append("Type");
        header.append("Annotations");
        header.append("Match");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(7, Report.NUMERIC_SORT);
        //Stamp.it("end Strain Report");

        return report;
    }

    public Report getMarkerReport(SearchBean sb) throws Exception{
        //Stamp.it("enter Marker Report");
        SearchDAO dao = new SearchDAO();


        ReportDAO gdao = new ReportDAO();

        Report report = new Report();

        if (sb.getTerm().toLowerCase().endsWith("]")) {
            report = gdao.getOverlappingSSLPReport(sb);
        }else {
            Map rankedList = dao.getRankedRGDIds(sb, "SSLPS");
            if( !rankedList.isEmpty() || sb.isSearchable() ) {
                report = gdao.getActiveSSLPReport(rankedList, sb);
            }
        }

        Record header = new Record();
        header.append("Relevance");
        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }
        header.append("Symbol");
        header.append("Expected Size");
        header.append("Chr");

        edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
        if (m != null && m.getUnit().equals("band")) {
            header.append("Fish Band");
            header.append("");
        }else if (m != null && (m.getUnit().toLowerCase().contains("cm") || m.getUnit().toLowerCase().contains("cr"))) {
            header.append("cM/cR");
            report.addSortMapping(5, Report.NUMERIC_SORT);
            header.append("");
        }else {
            header.append("Start");
            report.addSortMapping(5, Report.NUMERIC_SORT);
            header.append("Stop");
        }

        header.append("Species");
        header.append("Match");
        header.append("Type");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(4, Report.CHROMOSOME_SORT);
        report.addSortMapping(6, Report.NUMERIC_SORT);
        //Stamp.it("end marker Report");

        return report;
    }

    public Report getReferenceReport(SearchBean sb) throws Exception{
        SearchDAO dao = new SearchDAO();

        Map rankedList =  dao.getRankedRGDIds(sb, "REFERENCES");

        ReportDAO gdao = new ReportDAO();
        Report report = new Report();

        if (sb.getRequired().size() > 0) {
            report = gdao.getActiveReferenceReport(rankedList,sb);
        }
        
        Record header = new Record();
        header.append("Relevance");
        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }
        header.append("Title");
        header.append("Citation");
        header.append("Abstract");
        header.append("PubMed");
        header.append("Pub Date");

        report.insert(0, header);
        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);

        return report;
    }

    /**
     * converts reference rgd ids into qtls rgd ids having the mentioned references
     * @return count of reference rgd ids converted
     */
    private int remapReferenceRgdIds(Map<Integer, RankedIndexItem> rankedList, int objectKey) throws Exception {

        if( rankedList.isEmpty() )
            return 0;

        HashMap<Integer, RankedIndexItem> rankedList2 = new HashMap<>();

        AssociationDAO adao = new AssociationDAO();
        Iterator<Map.Entry<Integer,RankedIndexItem>> it = rankedList.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry<Integer,RankedIndexItem> entry = it.next();
            RankedIndexItem item = entry.getValue();
            if( item.getObjectType().equals("REFERENCES") ) {

                // any associated qtls?
                for( Integer qtlRgdId: adao.getObjectsAssociatedWithReference(entry.getKey(), objectKey, item.getSpeciesTypeKey()) ) {
                    if( rankedList.get(qtlRgdId)==null ) {
                        // referenced qtl was not on search result
                        //
                        // is referenced qtl already on rankedList2?
                        RankedIndexItem item2 = rankedList2.get(qtlRgdId);
                        if( item2!=null ) {
                            // referenced qtl is on rankedList2 -- update rank if needed
                            if( item2.getRank() < item.getRank() )
                                item2.setRank(item.getRank());
                        }
                        else {
                            // referenced qtl is not an rankedList2 -- add it
                            rankedList2.put(qtlRgdId, item);
                        }
                    }
                }
                it.remove();
            }
        }

        rankedList.putAll(rankedList2);
        return rankedList2.size();
    }

    public Report getGenomicElementReport(SearchBean sb) throws Exception {
        return getGenomicElementReport(sb, null);
    }

    public Report getPromoterReport(SearchBean sb) throws Exception {
        return getGenomicElementReport(sb, "onlyPromoters");
    }

    public Report getCellLineReport(SearchBean sb) throws Exception{
        return getGenomicElementReport(sb, "onlyCellLines");
    }

    Report getGenomicElementReport(SearchBean sb, String mode) throws Exception{
        SearchDAO dao = new SearchDAO();

        Report report = new Report();
        ReportDAO reportDAO = new ReportDAO();

        try {
            // merge PROMOTERS and CELL_LINES as genomic elements, depending on mode
            Map rankedList = (mode==null || mode.equals("onlyPromoters"))
                            ? dao.getRankedRGDIds(sb, "PROMOTERS")
                            : Collections.emptyMap();
            Map rankedList3 = (mode==null || mode.equals("onlyCellLines"))
                            ? dao.getRankedRGDIds(sb, "CELL_LINES")
                            : Collections.emptyMap();
            rankedList.putAll(rankedList3);

            if (sb.getRequired().size() > 0) {
                report = reportDAO.getActiveGenomicElementReport(rankedList, sb);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        Record header = new Record();
        header.append("Relevance");
        if (sb.isChinchilla()) {
            header.append("CRRD ID");
        }else {
            header.append("RGD ID");
        }
        header.append("Symbol");
        header.append("Name");
        header.append("Description");
        header.append("Chr");

        edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
        if (m != null && m.getUnit().equals("band")) {
            header.append("Fish Band");
            header.append("");
        }else if (m != null && (m.getUnit().toLowerCase().contains("cm") || m.getUnit().toLowerCase().contains("cr"))) {
            header.append("cM/cR");
            report.addSortMapping(6, Report.NUMERIC_SORT);
            header.append("");
        }else {
            header.append("Start");
            report.addSortMapping(6, Report.NUMERIC_SORT);
            header.append("Stop");
        }

        header.append("Species");
        header.append("Annotations");
        header.append("Match");
        header.append("Type");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(5, Report.CHROMOSOME_SORT);
        report.addSortMapping(7, Report.NUMERIC_SORT);
        report.addSortMapping(9, Report.NUMERIC_SORT);
        return report;
    }

    public Report getVariantReport(SearchBean sb) throws Exception{
        SearchDAO dao = new SearchDAO();

        Report report = new Report();
        ReportDAO gdao = new ReportDAO();

        if (sb.getTerm().endsWith("]")) {
            report = gdao.getOverlappingVariantReport(sb);
        }else {
            Map rankedList =  dao.getRankedRGDIds(sb, "VARIANTS");

            if( !rankedList.isEmpty() || sb.isSearchable() ) {
                report = gdao.getActiveVariantReport(rankedList, sb);
            }
        }

        Record header = new Record();
        header.append("Relevance");
        if (sb.isChinchilla()) {
            header.append("CRRD ID");

        }else {
            header.append("RGD ID");
        }
        header.append("Symbol");
        header.append("Variant Type");
        header.append("Name");
        header.append("Trait");
        header.append("Clinical Significance");
        header.append("Chr");

        edu.mcw.rgd.datamodel.Map m = MapManager.getInstance().getMap(sb.getMap());
        if (m != null && m.getUnit().equals("band")) {
            header.append("Fish Band");
            header.append("");
        }else {
            header.append("Start");
            report.addSortMapping(6, Report.NUMERIC_SORT);
            header.append("Stop");
        }

        header.append("Species");
        header.append("Annotations");
        header.append("Match");
        report.insert(0, header);

        report.addSortMapping(0, Report.NUMERIC_SORT);
        report.addSortMapping(1, Report.NUMERIC_SORT);
        report.addSortMapping(5, Report.CHROMOSOME_SORT);
        report.addSortMapping(7, Report.NUMERIC_SORT);
        report.addSortMapping(9, Report.NUMERIC_SORT);

        return report;
    }

    public GeneralSearchResult execute(SearchBean sb) throws Exception {
        //Stamp.it("enter general search execute Report");

        SearchDAO dao = new SearchDAO();
        GeneralSearchResult res= dao.runGeneralSearch(sb);
        //Stamp.it("end general search execute Report");
        return res;
    }

    public GeneralSearchResult searchOntologies(SearchBean sb) throws Exception {
        //Stamp.it("enter general search ontologies Report");

        SearchDAO dao = new SearchDAO();
        GeneralSearchResult res= dao.runOntologySearch(sb);
        //Stamp.it("end general search ontologies Report");
        return res;
    }

    private void mergeRankedLists(Map<Integer, RankedIndexItem> rankedList1, Map<Integer, RankedIndexItem> rankedList2) {

        // if an item from rankedList2 is not on rankedList1, it should be added
        // if an item from rankedList2 is on rankedList1, nothing is done
        for( Map.Entry<Integer, RankedIndexItem> entry: rankedList2.entrySet() ) {
            if( rankedList1.get(entry.getKey())==null )
                rankedList1.put(entry.getKey(), entry.getValue());
        }
    }
}
