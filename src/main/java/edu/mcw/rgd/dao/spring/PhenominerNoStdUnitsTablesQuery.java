package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.phenominerEnumTable;
import edu.mcw.rgd.datamodel.pheno.phenominerNoStdUnitTable;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhenominerNoStdUnitsTablesQuery  extends MappingSqlQuery {
    public PhenominerNoStdUnitsTablesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        phenominerNoStdUnitTable pNoStdUnitTable = new phenominerNoStdUnitTable();
        pNoStdUnitTable.setOntId(rs.getString("CLINICAL_MEASUREMENT_ONT_ID"));
        pNoStdUnitTable.setTerm(rs.getString("TERM"));
        pNoStdUnitTable.setMeasurementUnit(rs.getString("MEASUREMENT_UNITS"));
        return pNoStdUnitTable;
    }
}
