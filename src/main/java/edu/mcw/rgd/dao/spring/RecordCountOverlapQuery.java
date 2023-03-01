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
public class RecordCountOverlapQuery extends MappingSqlQuery {

    public RecordCountOverlapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList al = new ArrayList();

        al.add(rs.getString("term1"));
        al.add(rs.getString("term2"));
        al.add(rs.getInt("rid"));
        al.add(rs.getInt("study_id"));
        al.add(rs.getString("study_name"));
        //al.add(rs.getInt("experiment_id"));
        //al.add(rs.getString("experiment_name"));

        return al;
    }
}
