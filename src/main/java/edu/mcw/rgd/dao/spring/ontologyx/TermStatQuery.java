package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.TermStat;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mtutaj on 9/1/2017.
 */
public class TermStatQuery extends MappingSqlQuery<TermStat> {

    public TermStatQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected TermStat mapRow(ResultSet rs, int rowNum) throws SQLException {

        TermStat ts = new TermStat();

        ts.setTermAcc(rs.getString("term_acc"));
        ts.setLastModifiedDate(rs.getDate("last_modified_date"));
        ts.setSpeciesTypeKey(rs.getInt("species_type_key"));
        ts.setObjectKey(rs.getInt("object_key"));
        ts.setWithChildren(rs.getInt("with_children"));
        ts.setStatName(rs.getString("stat_name"));
        ts.setStatValue(rs.getInt("stat_value"));
        ts.setFilter(rs.getString("filter"));

        return ts;
    }

}