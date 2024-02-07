package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.MappedQTLQuery;
import edu.mcw.rgd.dao.spring.QTLQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.MappedQTL;
import edu.mcw.rgd.datamodel.QTL;

import java.sql.Types;
import java.util.Collection;
import java.util.List;

import edu.mcw.rgd.datamodel.RgdId;
import org.springframework.jdbc.object.BatchSqlUpdate;

/**
 * @author jdepons
 * @since May 19, 2008
 * API to manipulate data in QTLS table
 */
public class QTLDAO extends AbstractDAO {

    public List<QTL> getActiveQTLs() throws Exception {
        String query = "select q.*, r.SPECIES_TYPE_KEY from QTLs q, RGD_IDS r " +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id";
        return executeQtlQuery(query);
    }

    public List<QTL> getActiveQTLs(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "select q.*, r.SPECIES_TYPE_KEY \n" +
                "from QTLs q, RGD_IDS r , maps_data md\n" +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=q.RGD_ID and md.rgd_id=q.rgd_id and md.chromosome=? and md.start_pos<=? and md.stop_pos>=? and md.map_key=?";
        return executeQtlQuery(query, chr, stopPos, startPos, mapKey);
    }

    public List<MappedQTL> getActiveMappedQTLs(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "select q.*, r.SPECIES_TYPE_KEY,md.* \n" +
                "from QTLs q, RGD_IDS r , maps_data md\n" +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=q.RGD_ID and md.rgd_id=q.rgd_id and md.chromosome=? and md.start_pos<=? and md.stop_pos>=? and md.map_key=? order by md.start_pos";
        return MappedQTLQuery.run(this,query, chr, stopPos, startPos, mapKey);
    }

    public List<QTL> getActiveQTLs(int speciesType) throws Exception {
        String query = "select q.*, r.SPECIES_TYPE_KEY from QTLs q, RGD_IDS r " +
                "where r.OBJECT_STATUS='ACTIVE' and r.RGD_ID=q.RGD_ID and r.SPECIES_TYPE_KEY=?";
        return executeQtlQuery(query, speciesType);
    }

    public QTL getQTL(int qtlRgdId) throws Exception {
        String query = "select q.*, r.SPECIES_TYPE_KEY from qtls q, RGD_IDS r where r.RGD_ID=q.RGD_ID and r.RGD_ID=?";

        List<QTL> qtls = executeQtlQuery(query, qtlRgdId);
        if (qtls.size() == 0) {
            throw new QTLDAOException("QTL " + qtlRgdId + " not found");
        }
        return qtls.get(0);
    }

    public List<QTL> getActiveQTLSortedBySymbol(String chr, long startPos, long stopPos, int mapKey) throws Exception {
        String query = "SELECT DISTINCT g.*, r.species_type_key \n" +
                "FROM qtls g, rgd_ids r, maps_data md \n" +
                "WHERE r.object_status='ACTIVE' AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id \n"+
                " AND md.chromosome=? AND md.start_pos<=? AND md.stop_pos>=? AND md.map_key=? " +
                " order by g.qtl_symbol";

        return executeQtlQuery(query, chr, stopPos, startPos, mapKey);
    }

    /**
     * get qtl by qtl key
     * @param qtlKey qtl key
     * @return QTL object
     * @throws Exception when something wrong happens in the spring framework
     */
    public QTL getQTLByKey(int qtlKey) throws Exception {
        String query = "SELECT q.*,r.species_type_key FROM qtls q, RGD_IDS r where r.RGD_ID=q.RGD_ID and q.qtl_key=?";

        List<QTL> qtls = executeQtlQuery(query, qtlKey);
        if (qtls.size() == 0) {
            throw new QTLDAOException("QTL " + qtlKey + " not found");
        }
        return qtls.get(0);
    }

    /**
     * return an active qtl given its symbol and species;
     * NOTE: there should be only one active qtl with given symbol for one species
     * @param symbol qtl symbol
     * @param speciesType species type key
     * @return QTL object, or null if there is no such qtl symbol for given species
     * @throws Exception QTLDAOException if there are multiple qtls for given symbol and species; or spring exception if something wrong ion spring framework
     */
    public QTL getQTLBySymbol(String symbol, int speciesType) throws Exception {
        String query = "SELECT q.*, r.species_type_key "+
                "FROM qtls q, rgd_ids r "+
                "WHERE r.rgd_id=q.rgd_id AND r.species_type_key=? AND q.qtl_symbol_lc=? AND r.object_status='ACTIVE'";

        List<QTL> qtls = executeQtlQuery(query, speciesType, symbol.toLowerCase());
        if (qtls.size() > 1) {
            throw new QTLDAOException("Multiple QTLS for symbol " + symbol + " and species "+speciesType);
        }
        if( qtls.size()==1 )
            return qtls.get(0);
        return null;
    }

    /**
     * get list of active qtls given rgd id is a marker to
     * @param rgdId rgd id
     * @return list of qtls, possibly empty
     * @throws Exception when something wrong happens in the spring framework
     */
    public List<QTL> isMarkerFor(int rgdId) throws Exception {

        String sql = "SELECT q.*, r.species_type_key FROM qtls q, rgd_ids r "+
                "WHERE r.rgd_id=q.rgd_id AND r.object_status='ACTIVE' "+
                "AND (flank_1_rgd_id=? OR flank_2_rgd_id=? OR peak_rgd_id=?)";
        return executeQtlQuery(sql, rgdId, rgdId, rgdId);
    }

    /**
     * Update qtl in the datastore based on rgdID
     *
     * @param qtl QTL object
     * @throws Exception when something wrong happens in the spring framework
     */
    public void updateQTL(QTL qtl) throws Exception{

        String sql = "UPDATE qtls SET qtl_key=?, qtl_symbol=?, qtl_name=?, qtl_symbol_lc=LOWER(?), qtl_name_lc=LOWER(?), " +
                "PEAK_OFFSET=?, CHROMOSOME=?, LOD=?, P_VALUE=?, VARIANCE=?,  NOTES=?, " +
                "FLANK_1_RGD_ID=?, FLANK_2_RGD_ID=?, PEAK_RGD_ID=?, INHERITANCE_TYPE=?, " +
                "LOD_IMAGE=?, LINKAGE_IMAGE=?, SOURCE_URL=?, MOST_SIGNIFICANT_CMO_TERM=?," +
                " PEAK_RS_ID=?, P_VAL_MLOG=? where RGD_ID=?";

        update(sql, qtl.getKey(), qtl.getSymbol(), qtl.getName(), qtl.getSymbol(), qtl.getName(),
            qtl.getPeakOffset(), qtl.getChromosome(), qtl.getLod(), qtl.getPValue(), qtl.getVariance(),
            qtl.getNotes(), qtl.getFlank1RgdId(), qtl.getFlank2RgdId(), qtl.getPeakRgdId(), qtl.getInheritanceType(),
            qtl.getLodImage(), qtl.getLinkageImage(), qtl.getSourceUrl(), qtl.getMostSignificantCmoTerm(),
                qtl.getPeakRsId(), qtl.getpValueMlog(), qtl.getRgdId());
    }

    /**
     * insert a qtl into the data store
     * note: qtl_key is generated automatically; rgd_id must be specified
     * @param qtl qtl object
     * @throws Exception
     */
    public void insertQTL(QTL qtl) throws Exception{

        String sql = "INSERT INTO qtls (qtl_key, qtl_symbol, qtl_name, qtl_symbol_lc, qtl_name_lc, " +
                "peak_offset, chromosome, lod, p_value, variance, notes, " +
                "FLANK_1_RGD_ID, FLANK_2_RGD_ID, PEAK_RGD_ID, INHERITANCE_TYPE, LOD_IMAGE, " +
                "LINKAGE_IMAGE, SOURCE_URL, MOST_SIGNIFICANT_CMO_TERM, RGD_ID, PEAK_RS_ID, P_VAL_MLOG) "+
                "VALUES (?,?,?,LOWER(?),LOWER(?), ?,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)";

        update(sql, this.getNextKey("QTLS","QTL_KEY"), qtl.getSymbol(), qtl.getName(), qtl.getSymbol(), qtl.getName(),
                qtl.getPeakOffset(), qtl.getChromosome(), qtl.getLod(), qtl.getPValue(), qtl.getVariance(), qtl.getNotes(),
                qtl.getFlank1RgdId(), qtl.getFlank2RgdId(), qtl.getPeakRgdId(), qtl.getInheritanceType(), qtl.getLodImage(),
                qtl.getLinkageImage(), qtl.getSourceUrl(), qtl.getMostSignificantCmoTerm(), qtl.getRgdId(),qtl.getPeakRsId(), qtl.getpValueMlog());
    }

    public int insertQTLBatch(Collection<QTL> qtls) throws Exception{

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),"INSERT INTO qtls (qtl_key, qtl_symbol, qtl_name, qtl_symbol_lc, qtl_name_lc, " +
                "peak_offset, chromosome, lod, p_value, variance, notes, FLANK_1_RGD_ID, FLANK_2_RGD_ID, PEAK_RGD_ID, INHERITANCE_TYPE, LOD_IMAGE, " +
                "LINKAGE_IMAGE, SOURCE_URL, MOST_SIGNIFICANT_CMO_TERM, RGD_ID, PEAK_RS_ID, P_VAL_MLOG) "+
                "VALUES (?,?,?,LOWER(?),LOWER(?), ?,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)",
                new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.INTEGER,
                        Types.DOUBLE, Types.DOUBLE, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.DOUBLE});
        su.compile();
        for (QTL qtl : qtls) {
            int qtlKey = this.getNextKeyFromSequence("QTL_KEY_SEQ");
            qtl.setKey(qtlKey);
            su.update(qtl.getKey(), qtl.getSymbol(), qtl.getName(), qtl.getSymbol(), qtl.getName(),
                    qtl.getPeakOffset(), qtl.getChromosome(), qtl.getLod(), qtl.getPValue(), qtl.getVariance(), qtl.getNotes(),
                    qtl.getFlank1RgdId(), qtl.getFlank2RgdId(), qtl.getPeakRgdId(), qtl.getInheritanceType(), qtl.getLodImage(),
                    qtl.getLinkageImage(), qtl.getSourceUrl(), qtl.getMostSignificantCmoTerm(), qtl.getRgdId(), qtl.getPeakRsId(), qtl.getpValueMlog());
        }
        return executeBatch(su);
    }

    /**
     * insert a qtl into the data store
     * note: qtl_key and rgd_id are generated automatically
     * @param qtl
     * @throws Exception
     */
    public void insertQTL(QTL qtl, String objectStatus, int speciesTypeKey) throws Exception{

        RGDManagementDAO dao = new RGDManagementDAO();
        RgdId id = null;
        try {
            // create new qtl rgd id
            id = dao.createRgdId(RgdId.OBJECT_KEY_QTLS, objectStatus, speciesTypeKey);
            qtl.setRgdId(id.getRgdId());

            // create new qtl
            insertQTL(qtl);
        }
        catch(Exception e) {
            // rollback changes if something goes wrong
            if( id!=null )
                dao.deleteRgdId(id.getRgdId());
            throw e;
        }
    }

    public List<String> getInheritanceTypes() throws Exception{

        String sql = "SELECT inheritance_key FROM inheritance_types";
        return StringListQuery.execute(this, sql);
    }

    // to differentiate between ours and the framework's exceptions
    public class QTLDAOException extends Exception {

        public QTLDAOException(String msg) {
            super(msg);
        }
    }

    /// QTL query implementation helper
    public List<QTL> executeQtlQuery(String query, Object ... params) throws Exception {
        return QTLQuery.execute(this, query, params);
    }

    public List<QTL> getActiveGWASQtls() throws Exception{
        String sql = "select q.*, r.SPECIES_TYPE_KEY from QTLs q, RGD_IDS r WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id and qtl_symbol like 'GWAS%'";
        return executeQtlQuery(sql);
    }
}
