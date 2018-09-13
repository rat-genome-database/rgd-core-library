package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ConservationScore;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 10/13/11
 * Time: 2:54 PM
 */
public class ConservationScoreMapper implements RowMapper<ConservationScore> {
    public ConservationScore mapRow(ResultSet rs, int rowNum) throws SQLException {

        ConservationScore cs = new ConservationScore();
        cs.setScore(rs.getBigDecimal("SCORE"));
        cs.setChromosome(rs.getString("CHR"));
        cs.setPosition(rs.getInt("POSITION"));
        return cs;
    }

}
