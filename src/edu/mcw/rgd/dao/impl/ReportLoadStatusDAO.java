package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.ReportLoadStatusQuery;
import edu.mcw.rgd.datamodel.ReportLoadStatus;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

public class ReportLoadStatusDAO extends AbstractDAO {

    @Override
    public DataSource getDataSource() throws Exception {
        return DataSourceFactory.getInstance().getRgdRagDataSource();
    }

    public List<ReportLoadStatus> getByTypeAndSpecies(String reportType, int speciesKey) throws Exception {
        String sql = "SELECT * FROM report_load_status WHERE report_type=? AND species_key=? ORDER BY report_load_status_id";
        ReportLoadStatusQuery q = new ReportLoadStatusQuery(this.getDataSource(), sql);
        return execute(q, reportType, speciesKey);
    }

    public List<ReportLoadStatus> getPendingByTypeAndSpecies(String reportType, int speciesKey) throws Exception {
        String sql = "SELECT * FROM report_load_status WHERE report_type=? AND species_key=? AND status IN ('pending','processing') ORDER BY report_load_status_id";
        ReportLoadStatusQuery q = new ReportLoadStatusQuery(this.getDataSource(), sql);
        return execute(q, reportType, speciesKey);
    }

    public List<ReportLoadStatus> getPendingByTypeSpeciesAndMapKey(String reportType, int speciesKey, int mapKey) throws Exception {
        String sql = "SELECT * FROM report_load_status WHERE report_type=? AND species_key=? AND map_key=? AND status IN ('pending','processing') ORDER BY report_load_status_id";
        ReportLoadStatusQuery q = new ReportLoadStatusQuery(this.getDataSource(), sql);
        return execute(q, reportType, speciesKey, mapKey);
    }

    public List<Integer> getCompletedRgdIds(String reportType) throws Exception {
        String sql = "SELECT rgd_id FROM report_load_status WHERE report_type=? AND status='completed'";
        return IntListQuery.execute(this, sql, reportType);
    }

    public int getCountByStatus(String reportType, int speciesKey, String status) throws Exception {
        String sql = "SELECT COUNT(*) FROM report_load_status WHERE report_type=? AND species_key=? AND status=?";
        return getCount(sql, reportType, speciesKey, status);
    }

    public int getCountByStatusAndMapKey(String reportType, int speciesKey, int mapKey, String status) throws Exception {
        String sql = "SELECT COUNT(*) FROM report_load_status WHERE report_type=? AND species_key=? AND map_key=? AND status=?";
        return getCount(sql, reportType, speciesKey, mapKey, status);
    }

    public int getTotalCount(String reportType, int speciesKey) throws Exception {
        String sql = "SELECT COUNT(*) FROM report_load_status WHERE report_type=? AND species_key=?";
        return getCount(sql, reportType, speciesKey);
    }

    public int getTotalCountByMapKey(String reportType, int speciesKey, int mapKey) throws Exception {
        String sql = "SELECT COUNT(*) FROM report_load_status WHERE report_type=? AND species_key=? AND map_key=?";
        return getCount(sql, reportType, speciesKey, mapKey);
    }

    public void insert(ReportLoadStatus r) throws Exception {
        String sql = "INSERT INTO report_load_status (rgd_id, report_type, species_key, map_key, symbol, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        update(sql, r.getRgdId(), r.getReportType(), r.getSpeciesKey(), r.getMapKey(), r.getSymbol(), r.getStatus());
    }

    public int insertBatch(List<ReportLoadStatus> list) throws Exception {
        String sql = "INSERT INTO report_load_status (rgd_id, report_type, species_key, map_key, symbol, status) " +
                "VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (rgd_id, report_type) DO NOTHING";

        BatchSqlUpdate bsu = new BatchSqlUpdate(this.getDataSource(), sql);
        bsu.declareParameter(new SqlParameter(Types.INTEGER));
        bsu.declareParameter(new SqlParameter(Types.VARCHAR));
        bsu.declareParameter(new SqlParameter(Types.INTEGER));
        bsu.declareParameter(new SqlParameter(Types.INTEGER));
        bsu.declareParameter(new SqlParameter(Types.VARCHAR));
        bsu.declareParameter(new SqlParameter(Types.VARCHAR));
        bsu.compile();

        for (ReportLoadStatus r : list) {
            bsu.update(r.getRgdId(), r.getReportType(), r.getSpeciesKey(), r.getMapKey(), r.getSymbol(), r.getStatus());
        }
        return executeBatch(bsu);
    }

    public void updateStatus(int reportLoadStatusId, String status, String fileName, String errorMessage) throws Exception {
        String sql = "UPDATE report_load_status SET status=?, file_name=?, error_message=?, updated_at=? WHERE report_load_status_id=?";
        update(sql, status, fileName, errorMessage, new Timestamp(System.currentTimeMillis()), reportLoadStatusId);
    }

    public int deleteByTypeSpeciesAndMapKey(String reportType, int speciesKey, int mapKey) throws Exception {
        String sql = "DELETE FROM report_load_status WHERE report_type=? AND species_key=? AND map_key=?";
        return update(sql, reportType, speciesKey, mapKey);
    }

    public int deleteByTypeAndSpecies(String reportType, int speciesKey) throws Exception {
        String sql = "DELETE FROM report_load_status WHERE report_type=? AND species_key=?";
        return update(sql, reportType, speciesKey);
    }

    public ReportLoadStatus getProcessingRecord(String reportType, int speciesKey) throws Exception {
        String sql = "SELECT * FROM report_load_status WHERE report_type=? AND species_key=? AND status='processing' LIMIT 1";
        ReportLoadStatusQuery q = new ReportLoadStatusQuery(this.getDataSource(), sql);
        List<ReportLoadStatus> results = execute(q, reportType, speciesKey);
        return results.isEmpty() ? null : results.get(0);
    }

    public int resetFailed(String reportType, int speciesKey, int mapKey) throws Exception {
        String sql;
        if (mapKey > 0) {
            sql = "UPDATE report_load_status SET status='pending', error_message=NULL, updated_at=? " +
                    "WHERE report_type=? AND species_key=? AND map_key=? AND status='failed'";
            return update(sql, new Timestamp(System.currentTimeMillis()), reportType, speciesKey, mapKey);
        } else {
            sql = "UPDATE report_load_status SET status='pending', error_message=NULL, updated_at=? " +
                    "WHERE report_type=? AND species_key=? AND status='failed'";
            return update(sql, new Timestamp(System.currentTimeMillis()), reportType, speciesKey);
        }
    }

    /** Returns any record with pending/processing/failed status, or null if no active/incomplete batch. */
    public ReportLoadStatus getActiveBatchInfo() throws Exception {
        String sql = "SELECT * FROM report_load_status WHERE status IN ('pending', 'processing', 'failed') LIMIT 1";
        ReportLoadStatusQuery q = new ReportLoadStatusQuery(this.getDataSource(), sql);
        List<ReportLoadStatus> results = execute(q);
        return results.isEmpty() ? null : results.get(0);
    }
}
