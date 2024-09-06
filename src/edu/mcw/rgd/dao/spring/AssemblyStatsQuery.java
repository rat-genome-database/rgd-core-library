package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.AssemblyStats;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssemblyStatsQuery extends MappingSqlQuery<AssemblyStats> {
    public AssemblyStatsQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected AssemblyStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssemblyStats stats=new AssemblyStats();
        stats.setMapKey(rs.getInt("map_key"));
        stats.setContigL50(rs.getString("contigL50"));
        stats.setContigN50(rs.getString("contigN50"));
        stats.setGapsBetweenScaffolds(rs.getString("GAPS_BETWEEN_SCAFFOLDS"));
        stats.setNumberOfContigs(rs.getInt("NUMBER_OF_CONTIGS"));
        stats.setScaffoldL50(rs.getString("SCAFFOLDL50"));
        stats.setScaffoldN50(rs.getString("SCAFFOLDN50"));
        stats.setNumberOfScaffolds(rs.getInt("NUMBER_OF_SCAFFOLDS"));
        stats.setTotalNumberOfChromosome(rs.getInt("TOTAL_NUMBER_OF_CHROMOSOME"));
        stats.setTotalUngappedLength(rs.getString("TOTAL_UNGAPPED_LENGTH"));
        stats.setTotalSequenceLength(rs.getString("TOTAL_SEQUENCE_LENGTH"));

        return stats;
    }

}
