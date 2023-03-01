package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.ontologyx.Ontology;
import edu.mcw.rgd.datamodel.pheno.MeasurementMethod;
import edu.mcw.rgd.datamodel.pheno.ClinicalMeasurement;

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
public class ClinicalMeasurementQuery extends MappingSqlQuery {

    public ClinicalMeasurementQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        ClinicalMeasurement cm =  new ClinicalMeasurement();
        cm.setId(rs.getInt("clinical_measurement_id"));
        cm.setNotes(rs.getString("clinical_measurement_notes"));
        cm.setAccId(rs.getString("clinical_measurement_ont_id"));
        cm.setFormula(rs.getString("formula"));
        cm.setAverageType(rs.getString("clinical_meas_average_type"));

        return cm;
    }

}