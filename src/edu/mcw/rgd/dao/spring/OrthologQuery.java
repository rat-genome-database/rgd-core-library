package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Ortholog;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapping sql query to extract orthology objects
 * Note: sql query must supply src_species_type_key and dest_species_type_key by joining with RGD_IDS table
 *       in addition to columns found in GENETOGENE_RGD_ID_RLT table!!!
 */
public class OrthologQuery extends MappingSqlQuery {

    public OrthologQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Ortholog obj = new Ortholog();

        obj.setKey(rs.getInt("genetogene_key"));
        obj.setSrcRgdId(rs.getInt("src_rgd_id"));
        obj.setDestRgdId(rs.getInt("dest_rgd_id"));
        obj.setGroupId(rs.getInt("group_id"));
        if( rs.wasNull() )
            obj.setGroupId(null);
        obj.setXrefDataSet(rs.getString("xref_data_set"));
        obj.setXrefDataSrc(rs.getString("xref_data_src"));
        obj.setOrthologTypeKey(rs.getInt("ortholog_type_key"));
        obj.setPercentHomology(rs.getDouble("percent_homology"));
        if( rs.wasNull() )
            obj.setPercentHomology(null);
        obj.setRefKey(rs.getInt("ref_key"));
        if( rs.wasNull() )
            obj.setRefKey(null);
        obj.setCreatedBy(rs.getInt("created_by"));
        obj.setCreatedDate(rs.getTimestamp("created_date"));
        obj.setLastModifiedBy(rs.getInt("last_modified_by"));
        obj.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        try {
            obj.setSrcSpeciesTypeKey(rs.getInt("src_species_type_key"));
        } catch(Exception ignore) {
            obj.setSrcSpeciesTypeKey(0);
        }
        try {
            obj.setDestSpeciesTypeKey(rs.getInt("dest_species_type_key"));
        } catch(Exception ignore) {
            obj.setDestSpeciesTypeKey(0);
        }
        return obj;
    }
}
