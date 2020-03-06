package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Map;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * <p>
 * Returns a row from MAPS table
 */
public class MapQuery extends MappingSqlQuery {

    public MapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Map map = new Map();
        map.setDescription(rs.getString("map_description"));
        map.setKey(rs.getInt("map_key"));
        map.setMethodKey(rs.getInt("method_key"));
        map.setName(rs.getString("map_name"));
        map.setRgdId(rs.getInt("rgd_id"));
        map.setUnit(rs.getString("map_unit"));
        map.setVersion(rs.getString("map_version"));
        map.setNotes(rs.getString("notes"));
        map.setPrimaryRefAssembly(rs.getString("primary_ref_assembly_ind").equals("Y")?true:false);
        map.setSpeciesTypeKey(rs.getInt("species_type_key"));        
        map.setDbsnpVersion(rs.getString("dbsnp_version"));
        map.setRank(rs.getInt("rank"));
        map.setUcscAssemblyId(rs.getString("ucsc_assembly_id"));
        map.setRefSeqAssemblyAcc(rs.getString("refseq_assembly_acc"));
        map.setRefSeqAssemblyName(rs.getString("refseq_assembly_name"));
        map.setSource(rs.getString("source"));

        return map;
    }

}