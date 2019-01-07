package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author jdepons
 * @since Jan 17, 2008
 */
public class GeneCountOverlapQuery extends MappingSqlQuery {

    public GeneCountOverlapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList al = new ArrayList();

        al.add(rs.getString("term1"));
        al.add(rs.getString("term2"));
        al.add(rs.getInt("annotated_object_rgd_Id"));

        return al;
    }
}
