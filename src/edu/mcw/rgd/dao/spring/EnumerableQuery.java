package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.Enumerable;
import edu.mcw.rgd.datamodel.pheno.Study;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author hsnalabolu
 * @since Jan 23, 2020
 * Returns a row from Phenominer_Enumerables table
 */
public class EnumerableQuery extends MappingSqlQuery {

    public EnumerableQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Enumerable e = new Enumerable();
        e.setType(rs.getInt("type"));
        e.setLabel(rs.getString("label"));
        e.setValue(rs.getString("value"));
        e.setDescription(rs.getString("description"));
        e.setOntId(rs.getInt("onto_id"));
        e.setValueInt(rs.getString("value_int"));

        return e;
    }

}
