package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jdepons
 * @since Jan 17, 2008
 */
public class GeneOverlapQuery extends MappingSqlQuery {

    public GeneOverlapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("gene_symbol");
    }
}
