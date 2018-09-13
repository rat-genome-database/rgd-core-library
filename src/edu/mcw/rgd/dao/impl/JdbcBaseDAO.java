package edu.mcw.rgd.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: GKowalski
 * Date: Jun 8, 2009
 * Time: 8:29:07 AM
 */
public class JdbcBaseDAO {
    /**
     * Logger for this class and subclasses
     */

    protected JdbcTemplate jdbcTemplate;
    protected DataSource dataSource;
    protected final Log logger = LogFactory.getLog(getClass());

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
