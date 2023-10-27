package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Experiment;
import edu.mcw.rgd.datamodel.pheno.Study;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class ExperimentQuery extends MappingSqlQuery {

    public ExperimentQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Experiment ex = new Experiment();
        ex.setId(rs.getInt("experiment_id"));
        ex.setName(rs.getString("experiment_name"));
        ex.setNotes(rs.getString("experiment_notes"));
        ex.setStudyId(rs.getInt("study_id"));
        ex.setTraitOntId(rs.getString("trait_ont_id"));
<<<<<<< HEAD
        ex.setTraitOntId2(rs.getString("trait_ont_id2"));
        ex.setTraitOntId3(rs.getString("trait_ont_id3"));
=======
>>>>>>> aadc3f40bceac60213d326ecf3ceb067cb2cdf7c
        ex.setLastModifiedBy(rs.getString("last_modified_by"));
        ex.setCreatedBy(rs.getString("created_by"));
        return ex;
    }

}
