package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MiRnaTargetStat;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mtutaj on 8/15/2016.
 */
public class MiRnaTargetStatQuery extends MappingSqlQuery {

    public MiRnaTargetStatQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MiRnaTargetStat obj = new MiRnaTargetStat();
        obj.setKey(rs.getInt("mirna_stat_key"));
        obj.setRgdId(rs.getInt("rgd_id"));

        obj.setName(rs.getString("stat_name"));
        obj.setValue(rs.getString("stat_value"));

        obj.setCreatedDate(rs.getDate("created_date"));
        obj.setLastModifiedDate(rs.getDate("last_modified_date"));
        return obj;
    }
}
