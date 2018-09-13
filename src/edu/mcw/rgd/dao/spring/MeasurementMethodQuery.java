package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.ontologyx.Ontology;
import edu.mcw.rgd.datamodel.pheno.MeasurementMethod;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the Alias table
 */
public class MeasurementMethodQuery extends MappingSqlQuery {

    public MeasurementMethodQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MeasurementMethod mm =  new MeasurementMethod();
        mm.setId(rs.getInt("measurement_method_id"));
        mm.setDuration(rs.getString("measurement_duration_in_secs"));
        mm.setNotes(rs.getString("measurement_method_notes"));
        mm.setAccId(rs.getString("measurement_method_ont_id"));
        mm.setSite(rs.getString("measurement_site"));
        mm.setPiType(rs.getString("measurement_method_pi_type"));
        mm.setPiTimeValue(rs.getInt("meas_method_pi_time_value"));
        if (rs.wasNull()) {
            mm.setPiTimeValue(null);
        }
        mm.setPiTypeUnit(rs.getString("meas_method_pi_time_unit"));

        return mm;
    }

}