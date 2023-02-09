package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.PhenominerUnitTable;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kthorat
 * @since Nov 11 2020
 * Returns a row from PHENOMINER UNIT Tables query
 */

public class PhenominerUnitTablesQuery extends MappingSqlQuery{
    public PhenominerUnitTablesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        PhenominerUnitTable pUnitTable = new PhenominerUnitTable();
        //pUnitTable.setI(rs.getInt(1));
        pUnitTable.setOntId(rs.getString("ont_id"));
        try {
            pUnitTable.setStdUnit(rs.getString("standard_unit"));
        } catch (Exception var5) {
        }        pUnitTable.setUnitFrom(rs.getString("unit_from"));
        pUnitTable.setUnitTo(rs.getString("unit_to"));
        pUnitTable.setTermSpecificScale(rs.getFloat("term_specific_scale"));
        pUnitTable.setZeroOffset(rs.getInt("zero_offset"));
        return pUnitTable;
    }
}
