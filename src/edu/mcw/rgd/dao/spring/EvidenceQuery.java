package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.annotation.Evidence;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 9/16/13
 * Time: 9:53 AM
 * query to use with Evidence datamodel class
 */
public class EvidenceQuery extends MappingSqlQuery {

    public EvidenceQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Evidence obj = new Evidence();
        obj.setEvidence(rs.getString("evidence"));
        obj.setName(rs.getString("evidence_name"));
        obj.setDescription(rs.getString("description"));
        obj.setManualCuration(rs.getString("manual_curation_flag"));
        obj.setEcoId(rs.getString("eco_id"));
        return obj;
    }
}