package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ConservationScore;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 10/13/11
 * Time: 2:54 PM
 */
public class ConservationScoreMapper extends MappingSqlQuery implements RowMapper<ConservationScore>  {
    public ConservationScoreMapper(DataSource ds, String query){
        super(ds, query);

    }
    public ConservationScore mapRow(ResultSet rs, int rowNum) throws SQLException {

        ConservationScore cs = new ConservationScore();
        cs.setScore(rs.getBigDecimal("SCORE"));
        cs.setChromosome(rs.getString("CHR"));
        cs.setPosition(rs.getInt("POSITION"));
        return cs;
    }

}
