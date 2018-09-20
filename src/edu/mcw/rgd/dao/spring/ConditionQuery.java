package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.pheno.Condition;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 */

/**
 * Returns a row from EXPERIMENT_CONDITION table
 */
public class ConditionQuery extends MappingSqlQuery {

    public ConditionQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Condition c = new Condition();
        c.setId(rs.getInt("experiment_condition_id"));
        c.setExperimentRecordId(rs.getInt("experiment_record_id"));
        c.setOrdinality(rs.getInt("exp_cond_ordinality"));
        if (rs.wasNull()) {
            c.setOrdinality(null);
        }
        c.setUnits(rs.getString("exp_cond_assoc_units"));
        c.setValueMin(rs.getString("exp_cond_assoc_value_min"));
        c.setValueMax(rs.getString("exp_cond_assoc_value_max"));

        c.setDurationLowerBound(rs.getDouble("exp_cond_dur_sec_low_bound"));
        c.setDurationUpperBound(rs.getDouble("exp_cond_dur_sec_high_bound"));

        c.setNotes(rs.getString("exp_cond_notes"));
        c.setOntologyId(rs.getString("exp_cond_ont_id"));
        c.setApplicationMethod(rs.getString("exp_cond_application_method"));
        
        return c;
    }

}