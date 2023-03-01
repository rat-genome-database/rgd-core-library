package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PatientGene;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 5/12/14
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatientGeneQuery extends MappingSqlQuery {
    public PatientGeneQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        PatientGene patientGene = new PatientGene();
        patientGene.setMrn(rs.getString("mrn"));
        patientGene.setGene(rs.getString("gene"));
        return patientGene;
    }

}
