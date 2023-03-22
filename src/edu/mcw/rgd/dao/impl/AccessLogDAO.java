package edu.mcw.rgd.dao.impl;
import edu.mcw.rgd.dao.AbstractDAO;
import java.util.Date;

public class AccessLogDAO extends AbstractDAO {

    public int log(String type, String description) throws Exception {
        String sql = "insert into ACCESS_LOG (log_type,log_date,description) values (?,?,?)";
        java.util.Date today = new java.sql.Date(new java.util.Date().getTime());

        return update(sql,type, today, description);
    }
}