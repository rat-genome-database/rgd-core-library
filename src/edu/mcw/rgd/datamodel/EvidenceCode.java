package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.EvidenceQuery;
import edu.mcw.rgd.datamodel.annotation.Evidence;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Feb 8, 2008
 * Time: 9:28:21 AM
 * <p>
 * represents an evidence code of an annotation
 */
public final class EvidenceCode {

    // internally all evidence codes are stored in a map indexed by evidence codes
    static private java.util.Map<String,Evidence> _evidences = null;

    static void loadEvidenceCodes() throws Exception {

        _evidences = new HashMap<String, Evidence>();

        String sql = "SELECT * FROM evidence_codes";
        EvidenceQuery q = new EvidenceQuery(DataSourceFactory.getInstance().getDataSource(), sql);
        q.compile();
        for( Evidence ev: (List<Evidence>)q.execute()) {
            _evidences.put(ev.getEvidence(), ev);
        }
    }

    static void ensureEvidenceCodesAreLoaded() throws Exception {

        if( _evidences==null )
            loadEvidenceCodes();
    }

    public static Evidence getEvidence(String code) throws Exception {
        code = code.toUpperCase();
        ensureEvidenceCodesAreLoaded();

        return _evidences.get(code);
    }

    /**
     * get friendly name for an evidence code
     * @param code evidence code
     * @return friendly name for given evidence code
     * @throws Exception
     */
    public static String getName(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return ev==null ? "" : ev.getName();
    }

    public static String getDescription(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return ev==null ? "" : ev.getDescription();
    }

    public static boolean isManualInSameSpecies(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return (!(ev == null || ev.getManualCuration() == null)) && ev.getManualCuration().equals("same_species");
    }

    public static boolean isManualInOtherSpecies(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return (!(ev == null || ev.getManualCuration() == null)) && ev.getManualCuration().equals("other_species");
    }

    /**
     * check if evidence code is used in manual curation
     * @param code evidence code
     * @return true if this evidence code is used in manual curation
     * @throws Exception
     */
    public static boolean isManual(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return ev != null && ev.getManualCuration() != null;
    }

    public static String getEcoId(String code) throws Exception {
        Evidence ev = getEvidence(code);
        return ev==null ? null : ev.getEcoId();
    }
}