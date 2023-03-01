package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.datamodel.NomenclatureEvent;
import edu.mcw.rgd.dao.spring.NomenclatureEventsQuery;
import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.process.Utils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 28, 2007
 * Time: 9:45:53 AM
 * <p>
 * Provides methods to perform updates to nomenclature against the data store.
 */
public class NomenclatureDAO extends AbstractDAO {

    /**
    * Constants class used to hold dates used by the nomenclature manager interface
    */
    public static final java.util.Date NOMENDATE_TODAY = new java.util.Date();
    public static final java.util.Date NOMENDATE_UNTOUCHABLE = new GregorianCalendar(2200,9,1).getTime(); // Oct 1, 2200
    public static final java.util.Date NOMENDATE_REVIEWABLE = new GregorianCalendar(2100,9,1).getTime();  // Oct 1, 2100
    public static final java.util.Date NOMENDATE_ONEYEAR = Utils.addOneYear(NOMENDATE_TODAY).getTime();
    public static final java.util.Date NOMENDATE_START = new GregorianCalendar(1974,9,1).getTime();  // Oct 1, 1974
    public static final java.util.Date NOMENDATE_TOMORROW = Utils.getTomorrow(NOMENDATE_TODAY).getTime();

    /**
     * Returns all nomenclature events based on an RGD ID ordered from the most recent ones
     *
     * @param rgdId nomenclature rgd id
     * @return list of all nomenclature events for given rgd id
     * @throws Exception when something really bad happens in spring framework
     */
    public List<NomenclatureEvent> getNomenclatureEvents(int rgdId) throws Exception {
        String query = "SELECT * FROM nomen_events WHERE rgd_id=? ORDER BY event_date DESC";
        NomenclatureEventsQuery q = new NomenclatureEventsQuery(this.getDataSource(), query);
        return execute(q, rgdId);
    }


    /**
     * Returns all nomenclature events based on an RGD ID ordered from the most recent ones
     *
     * @param rgdId nomenclature rgd id
     * @return list of all nomenclature events for given rgd id
     * @throws Exception when something really bad happens in spring framework
     */
    public List<NomenclatureEvent> getNomenclatureEvents(int rgdId, Date from, Date to) throws Exception {
        String query = "SELECT * FROM nomen_events WHERE rgd_id=? AND event_date BETWEEN ? AND ? ORDER BY event_date DESC";
        NomenclatureEventsQuery q = new NomenclatureEventsQuery(this.getDataSource(), query);
        return execute(q, rgdId, from, to);
    }


    /**
     * Creates a new nomenclature event in the datastore.
     * @param event NomenclatureEvent object
     * @throws Exception
     */
    public void createNomenEvent(NomenclatureEvent event) throws Exception {

        String sql = "INSERT INTO nomen_events (nomen_event_key,rgd_id,symbol,name,ref_key," +
                "nomen_status_type,description,event_date,notes,original_rgd_id," +
                "previous_symbol,previous_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        event.setNomenEventKey(this.getNextKey("nomen_events", "nomen_event_key"));
        update(sql, event.getNomenEventKey(), event.getRgdId(), event.getSymbol(), event.getName(),
                event.getRefKey(), event.getNomenStatusType(), event.getDesc(), event.getEventDate(),
                event.getNotes(), event.getOriginalRGDId(), event.getPreviousSymbol(), event.getPreviousName());
    }


}



