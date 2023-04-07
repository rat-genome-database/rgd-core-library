package edu.mcw.rgd.dao.impl;
import edu.mcw.rgd.dao.AbstractDAO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class AccessLogDAO extends AbstractDAO {

    public int log(String type, String description) throws Exception {
        String sql = "insert into ACCESS_LOG (log_type,log_date,description) values (?,?,?)";
        java.util.Date today = new java.sql.Date(new java.util.Date().getTime());

        return updateFb(sql,type, today, description);
    }
    public int log(String type, String description, String ipAddress) throws Exception {
        String sql = "insert into ACCESS_LOG (log_type,log_date,description,ip_address) values (?,?,?,?)";
        java.util.Date today = new java.sql.Date(new java.util.Date().getTime());

        return updateFb(sql,type, today, description, ipAddress);
    }

    public int log(String type, String description, HttpServletRequest request) throws Exception {
        String sql = "insert into ACCESS_LOG (log_type,log_date,description,ip_address) values (?,?,?,?)";
        java.util.Date today = new java.sql.Date(new java.util.Date().getTime());

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return updateFb(sql,type, today, description, ipAddress);
    }


}