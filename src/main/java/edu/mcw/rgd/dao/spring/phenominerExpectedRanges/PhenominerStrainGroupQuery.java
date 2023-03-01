package edu.mcw.rgd.dao.spring.phenominerExpectedRanges;

import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerStrainGroup;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerStrainGroupQuery extends MappingSqlQuery {
    public PhenominerStrainGroupQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected PhenominerStrainGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhenominerStrainGroup strainGroup= new PhenominerStrainGroup();
        strainGroup.setId(rs.getInt("strain_group_id"));
        strainGroup.setName(rs.getString("strain_group_name"));
        strainGroup.setStrain_ont_id(rs.getString("strain_ont_id"));
        return strainGroup;
    }
}
