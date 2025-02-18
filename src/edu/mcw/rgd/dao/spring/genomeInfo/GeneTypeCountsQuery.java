package edu.mcw.rgd.dao.spring.genomeInfo;

import edu.mcw.rgd.datamodel.genomeInfo.GeneTypeCounts;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneCountsQuery extends MappingSqlQuery<GeneTypeCounts> {

    public GeneCountsQuery(DataSource ds, String sql){
        super(ds,sql);
    }
    @Override
    protected GeneTypeCounts mapRow(ResultSet rs, int rowNum) throws SQLException {
        GeneTypeCounts geneCounts=new GeneTypeCounts();
        geneCounts.setGeneTypeLc(rs.getString("gene_type_lc"));
        geneCounts.setChromosome(rs.getString("chromosome"));
        geneCounts.setCount(rs.getInt("tot"));
        geneCounts.setMapKey(rs.getInt("map_key"));
        return null;
    }
}
