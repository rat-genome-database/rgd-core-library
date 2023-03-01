package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PatientPhenotype;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 5/12/14
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatientPhenotypeQuery extends MappingSqlQuery {
    public PatientPhenotypeQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        PatientPhenotype patientPhenotype = new PatientPhenotype();
        patientPhenotype.setMrn(rs.getString("mrn"));
        patientPhenotype.setPhenotype(rs.getString("phenotype"));
        return patientPhenotype;
    }

}
