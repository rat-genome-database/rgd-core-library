package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdIdQuery;
import edu.mcw.rgd.datamodel.Identifiable;
import edu.mcw.rgd.datamodel.MapData;
import edu.mcw.rgd.datamodel.Protein;
import edu.mcw.rgd.datamodel.RgdId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Dec 28, 2007
 * Time: 9:45:44 AM
 * <p>
 * Provides methods to perform RGD Management tasks against the data store.
 */
public class RGDManagementDAO extends AbstractDAO {

    /**
     * Withdraws an Identifiable object.  This method updates the rgd_ids table.
     *
     * @param obj object to be withdrawn
     * @throws Exception
     */
    public void withdraw(Identifiable obj) throws Exception {
        changeStatus(obj, "WITHDRAWN");
    }

    /**
     * Retires an Identifiable object.  This method updates the rgd_ids table.
     *
     * @param obj object to be retired
     * @throws Exception
     */
    public void retire(Identifiable obj) throws Exception {
        changeStatus(obj, "RETIRED");
    }

    private void changeStatus(Identifiable obj, String status) throws Exception {
        String sql = "UPDATE rgd_ids SET object_status=?, last_modified_date=SYSDATE WHERE rgd_id=?";
        update(sql, status, obj.getRgdId());
    }

    /**
     * Records an RGD history event.  Events occur when an RGD ID of an existing object is changed.
     * Information is stored in the rgd_id_history table
     *
     * @param fromRgdId old rgd id
     * @param toRgdId new rgd id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void recordIdHistory(int fromRgdId, int toRgdId) throws Exception {
        Connection conn = null;
        try {

            conn = this.getConnection();
            String sql = "insert into rgd_id_history (history_key, old_rgd_id, new_rgd_id, last_modified_date, created_date) values (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.getNextKey("rgd_id_history", "history_key" ));
            ps.setInt(2, fromRgdId);
            ps.setInt(3, toRgdId);
            java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
            ps.setDate(4, now);
            ps.setDate(5, now);

            ps.execute();
        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }

    }

    /**
     * get new_rgd_id from RGD_ID_HISTORY for given rgd_id
     * @param rgdId rgd id of an object to be examined
     * @return new_rgd_id taken from RGD_ID_HISTORY or 0, if there is no history record
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getRgdIdFromHistory(int rgdId) throws Exception {

        String query = "select NEW_RGD_ID from RGD_ID_HISTORY where OLD_RGD_ID=?";
        Connection conn = null;
        int newRgdId = 0;
        try {
            conn = this.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, rgdId);
            ResultSet rs = ps.executeQuery();
            if( rs.next() ) {
                newRgdId = rs.getInt(1);
            }
            rs.close();
        } finally {
            try {
                if( conn!=null )
                    conn.close();
            } catch (Exception ignored) {
            }
        }
        return newRgdId;
    }

    /**
     * get ACTIVE new_rgd_id from RGD_ID_HISTORY for given rgd_id;
     * the history is searched recursively until an active rgd object is found;
     * if there is no active rgd id in the object history, 0 is returned
     * @param rgdId rgd id of an object to be examined
     * @return new_rgd_id of an active object taken from RGD_ID_HISTORY or 0, if there is no history record
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getActiveRgdIdFromHistory(int rgdId) throws Exception {

        // check history for this rgd id
        int newRgdId = getRgdIdFromHistory(rgdId);
        if( newRgdId==0 )
            return 0; // no history for this rgd id

        // there is a history -- check the status
        RgdId id = getRgdId(newRgdId);
        if( id.getObjectStatus().equals("ACTIVE") ) {
            // the history record is active -- return it
            return newRgdId;
        }
        // the historical record is not active -- check its history now
        if( rgdId==newRgdId )
            return 0; // protection against self-recursion, where new_rgd_id=old_rgd_id
        return getActiveRgdIdFromHistory(newRgdId);
    }

    /**
     * convenience method to create an RGD object (gene, sslp, strain, qtl, reference, cell line, protein or genomic element)
     * given rgd id of this object;
     * if rgd id points to object of other type than gene, sslp, strain, qtl, reference, cell line, protein or genomic element,
     * null is returned
     * @param rgdId rgd id of object
     * @return Gene, SSLP, Strain, QTL, Reference, CellLine, GenomicElement or null
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Object getObject(int rgdId) throws Exception {

        RgdId id = this.getRgdId(rgdId);
        return getObject(rgdId, id.getObjectKey());
    }

    /**
     * convenience method to create an RGD object (gene, sslp, strain, qtl, reference, cell line, protein or genomic element)
     * given rgd id of this object;
     * if rgd id points to object of other type than gene, sslp, strain, qtl, reference, cell line, protein or genomic element,
     * null is returned
     * @param rgdId rgd id of object
     * @return Gene, SSLP, Strain, QTL, Reference, CellLine, GenomicElement or null
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Object getObject(int rgdId, int objectKey) throws Exception {

        Object obj;

        switch (objectKey) {
            case RgdId.OBJECT_KEY_GENES:
                obj = new GeneDAO().getGene(rgdId);
                break;
            case RgdId.OBJECT_KEY_SSLPS:
                obj = new SSLPDAO().getSSLP(rgdId);
                break;
            case RgdId.OBJECT_KEY_QTLS:
                obj = new QTLDAO().getQTL(rgdId);
                break;
            case RgdId.OBJECT_KEY_STRAINS:
                obj = new StrainDAO().getStrain(rgdId);
                break;
            case RgdId.OBJECT_KEY_REFERENCES:
                obj = new ReferenceDAO().getReference(rgdId);
                break;
            case RgdId.OBJECT_KEY_CELL_LINES:
                obj = new CellLineDAO().getCellLine(rgdId);
                break;
            case RgdId.OBJECT_KEY_PROTEINS:
                obj = new ProteinDAO().getProtein(rgdId);
                break;
            default:
                obj = new GenomicElementDAO().getElement(rgdId);
                break;
        }

        return obj;
    }

    /**
     * get RgdId object given rgd id
     * <p>
     *     Note: exception will be thrown when rgd id is invalid. Please use getRgdId2() instead if you prefer to handle nulls.
     * </p>
     * @param rgdId rgd id
     * @return RgdId object
     * @throws Exception when rgdId is invalid or when there is a problem in spring framework
     * @deprecated use getRgdId2 instead (exceptions should not be used to handle business logic)
     */
    public RgdId getRgdId(int rgdId) throws Exception {

        RgdId id = getRgdId2(rgdId);
        if( id==null )
            throw new Exception("RGD ID " + rgdId + " not found" );
        return id;
    }

    // similar to getRgdId, but it does not throw an exception if rgd_id is not found
    // it returns null instead
    public RgdId getRgdId2(int rgdId) throws Exception {
        String query = "select * from RGD_IDS where RGD_ID=?";
        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), query);
        List<RgdId> ids = execute(q, rgdId);

        if (ids.size() > 0) {
            return ids.get(0);
        } else {
            return null;
        }
    }
    // Added to get list of RgdId objects for a list of rgdIds for TESTING on 06/22/2017 by JYOTHI
    public List<RgdId> getListOfRgdId2(List<Integer> rgdIds) throws Exception {
        String query = "select * from RGD_IDS where RGD_ID in (";
        boolean first=true;
        for(int rgdid:rgdIds){
            if(first) {
                query = query + rgdid;
                first=false;
            }else{
                query=query + ", "+ rgdid;
            }
        }
        query=query+")";

        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), query);
        List<RgdId> ids = execute(q);

        if (ids.size() > 0) {
            return ids;
        } else {
            return null;
        }
    }

    /**
     * get RgdId objects for given object key
     * @param objectKey object key
     * @return list of RgdId objects
     * @throws Exception when there is a problem in spring framework
     */
    public List<RgdId> getRgdIds(int objectKey) throws Exception {

        String query = "SELECT * FROM rgd_ids WHERE object_key=?";

        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), query);
        return execute(q, objectKey);
    }

    /**
     * returns list of RGDId objects that overlap a given chr/start/stop position with a given map key
     * @param mp
     * @return list of rgdid objects
     * @throws Exception
     */

    /* Pushkala 28th August 2012: this query needs to be fixed to get better results.. it
    doesnt address the following possibilities:
        ----------------------------------         map data object in question
              -----------------------              when map data object is smaller
    ----------------------                         partial overlap of map dta object
                        ------------------------   partial overlap of map data object

     */
    public List<RgdId> getOverLappingRgdIds(MapData mp) throws Exception{

        String query = "select r.*, md.* from RGD_IDS r , maps_data md \n" +
                "where r.OBJECT_STATUS='ACTIVE' \n" +
                "and r.RGD_ID=md.RGD_ID \n" +
                "and md.chromosome=? \n" +
                "and md.start_pos<=?\n" +
                "and md.stop_pos>=?  \n" +
                "and md.map_key=?";

        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), query);
        return execute(q, mp.getChromosome(),mp.getStartPos(),mp.getStopPos(),mp.getMapKey());
    }


    public List<RgdId> getNeighbouringRgdIds(MapData md, int range) throws Exception{

        int lowerRange = md.getStartPos();
        int upperRange = md.getStopPos();

        if(range!=0){
            lowerRange = md.getStartPos()-range; //lower range would be any region upstream of the start position.
            upperRange = md.getStopPos()+range;  //upperRange would be any region downstream of the stop position.
        }

        String query = "select r.*, md.* from RGD_IDS r, MAPS_DATA md \n" +
                "where \n" +
                "md.START_POS BETWEEN ? AND ? \n" +
                "and md.CHROMOSOME=? \n" +
                "and md.MAP_KEY=? \n" +
                "and r.RGD_ID=md.RGD_ID";

        RgdIdQuery q = new RgdIdQuery(this.getDataSource(), query);
        return execute(q, lowerRange, upperRange, md.getChromosome(), md.getMapKey());
    }
            

    /**
     * Update properties of rgd id object
     *
     * @param rgdId  rgd id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void updateRgdId(RgdId rgdId) throws Exception {

        String sql = "update Rgd_Ids set object_key=?, created_date=?, released_date=?, " +
                "last_modified_date=?, rgd_flag=?, notes=?, object_status=?, species_type_key=? where RGD_ID=?";

        update(sql, rgdId.getObjectKey(), rgdId.getCreatedDate(), rgdId.getReleasedDate(),
                rgdId.getLastModifiedDate(), rgdId.getRgdFlag(), rgdId.getNotes(), rgdId.getObjectStatus(),
                rgdId.getSpeciesTypeKey(), rgdId.getValue());
    }

    /**
     * create a new rgd_id object
     * @param objectKey object key
     * @param objectStatus object status: 'ACTIVE', 'RETIRED', 'WITHDRAWN'
     * @param speciesTypeKey species type key: 1, 2, 3
     * @return RgdId object
     * @throws Exception when unexpected error in spring framework occurs
     * @deprecated see similar function createRgdId(int objectKey, String objectStatus, String notes, int speciesTypeKey) that allows you to insert also the notes
     */
    public RgdId createRgdId(int objectKey, String objectStatus, int speciesTypeKey) throws Exception {

        return createRgdId(objectKey, objectStatus, null, speciesTypeKey);
    }

    /**
     * create a new rgd_id object
     * @param objectKey object key
     * @param objectStatus object status: 'ACTIVE', 'RETIRED', 'WITHDRAWN'
     * @param notes additional notes about the application creating this rgd id
     * @param speciesTypeKey species type key: 1, 2, 3
     * @return newly created RgdId object; generated unique rgd_id value is returned
     * @throws Exception when unexpected error in spring framework occurs
     */
    public RgdId createRgdId(int objectKey, String objectStatus, String notes, int speciesTypeKey) throws Exception {

        int rgdId = this.getNextKey("rgd_ids", "rgd_id");

        // references do not have species type key defined;
        // by specifying 0, foreign key will be violated, because there is no species with species_type_key=0
        // you must specify NULL
        Integer speciesKey = speciesTypeKey==0 ? null : speciesTypeKey;

        String sql =  "INSERT INTO rgd_ids (object_key, created_date, notes, " +
                "last_modified_date, object_status, species_type_key, rgd_id) " +
                "VALUES (?,SYSDATE,?,SYSDATE,?,?,?)";

        update(sql, objectKey, notes, objectStatus, speciesKey, rgdId);

        return this.getRgdId(rgdId);
    }

    /**
     * update last modified date for specified rgd id
     * @param rgdId rgd id
     * @throws Exception when unexpected error in spring framework occurs
     */
    public void updateLastModifiedDate(int rgdId) throws Exception {
        String sql = "update rgd_ids set last_modified_date=SYSDATE where rgd_id=?";
        update(sql, rgdId);
    }

    public void deleteRgdId(int rgdId) throws Exception {
        String sql = "delete from rgd_ids where rgd_id = ?";
        update(sql, rgdId);
    }
}
