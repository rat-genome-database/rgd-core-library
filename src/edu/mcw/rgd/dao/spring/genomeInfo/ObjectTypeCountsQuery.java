package edu.mcw.rgd.dao.spring.genomeInfo;

import edu.mcw.rgd.datamodel.Edge;
import edu.mcw.rgd.datamodel.genomeInfo.ObjectTypeCounts;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectTypeCountsQuery extends MappingSqlQuery<ObjectTypeCounts> {

    public ObjectTypeCountsQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected ObjectTypeCounts mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectTypeCounts geneCounts=new ObjectTypeCounts();
        try {
            geneCounts.setObjectNameLc(rs.getString("object_name"));
        }catch (Exception ignored){}
        try{
        geneCounts.setChromosome(rs.getString("chromosome"));
        }catch (Exception ignored){}
        try {
            geneCounts.setCount(rs.getInt("tot"));
        }catch (Exception ignored){}
        try {
            geneCounts.setMapKey(rs.getInt("map_key"));
        }catch (Exception ignored){}
        try{
            geneCounts.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored){}
        return geneCounts;
    }
}
