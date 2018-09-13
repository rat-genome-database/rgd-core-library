package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * <p>
 * Utility class to return a single integer value from a sql query.
 * <b>Usage:</b>
 * <pre>
 * String query = "SELECT count(*) from study";
 * CountQuery q = new CountQuery(this.getDataSource(), query);
 * q.compile();
 * int countValue = q.getCount();
 * </pre>
 * </p>
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
