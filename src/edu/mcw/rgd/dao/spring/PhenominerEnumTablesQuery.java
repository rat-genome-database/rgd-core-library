package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.phenominerEnumTable;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhenominerEnumTablesQuery extends MappingSqlQuery {
    public PhenominerEnumTablesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        phenominerEnumTable pEnumTable = new phenominerEnumTable();
        pEnumTable.setType(rs.getInt("type"));
        pEnumTable.setLabel(rs.getString("label"));
        pEnumTable.setValue(rs.getString("value"));
        pEnumTable.setDescription(rs.getString("description"));
        return pEnumTable;
    }
}
