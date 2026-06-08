package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.EmbedStatusQuery;
import edu.mcw.rgd.datamodel.EmbedStatus;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

public class EmbedStatusDAO extends AbstractDAO {

    @Override
    public DataSource getDataSource() throws Exception {
        return DataSourceFactory.getInstance().getRgdRagDataSource();
    }

    public void insert(String filePath, String fileName, String status) throws Exception {
        String sql = "INSERT INTO embed_status (file_path, file_name, status) VALUES (?, ?, ?)";
        update(sql, filePath, fileName, status);
    }

    public void updateStatus(String filePath, String status, String errorMessage, int chunkCount) throws Exception {
        String sql = "UPDATE embed_status SET status=?, error_message=?, chunk_count=?, updated_at=? WHERE file_path=?";
        update(sql, status, errorMessage, chunkCount, new Timestamp(System.currentTimeMillis()), filePath);
    }

    public EmbedStatus getByFilePath(String filePath) throws Exception {
        String sql = "SELECT * FROM embed_status WHERE file_path=?";
        EmbedStatusQuery q = new EmbedStatusQuery(this.getDataSource(), sql);
        List<EmbedStatus> results = execute(q, filePath);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<EmbedStatus> getByStatus(String status) throws Exception {
        String sql = "SELECT * FROM embed_status WHERE status=? ORDER BY file_path";
        EmbedStatusQuery q = new EmbedStatusQuery(this.getDataSource(), sql);
        return execute(q, status);
    }

    public List<EmbedStatus> getFailed() throws Exception {
        return getByStatus("FAILED");
    }

    public int getCountByStatus(String status) throws Exception {
        String sql = "SELECT COUNT(*) FROM embed_status WHERE status=?";
        return getCount(sql, status);
    }

    public int getTotalCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM embed_status";
        return getCount(sql);
    }

    public int deleteByFilePath(String filePath) throws Exception {
        String sql = "DELETE FROM embed_status WHERE file_path=?";
        return update(sql, filePath);
    }

    public int resetFailed() throws Exception {
        String sql = "UPDATE embed_status SET status='PENDING', error_message=NULL, updated_at=? WHERE status='FAILED'";
        return update(sql, new Timestamp(System.currentTimeMillis()));
    }
}
