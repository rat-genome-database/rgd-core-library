package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.prediction.PolyPhenPrediction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 10/18/11
 * Time: 12:42 PM
 */
public class PolyPhenMapper extends MappingSqlQuery implements RowMapper<PolyPhenPrediction>  {
    public PolyPhenMapper(){}
    public PolyPhenMapper(DataSource ds, String query) {
        super(ds, query);
    }

    public PolyPhenPrediction mapRow(ResultSet rs, int rowNum) throws SQLException {

        PolyPhenPrediction php = new PolyPhenPrediction();
        php.setId(rs.getLong("POLYPHEN_ID"));
        php.setScore1(rs.getString("SCORE1"));
        php.setScore2(rs.getString("SCORE2"));
        php.setDiff(rs.getString("SCORE_DELTA"));
        php.setNumObserved(rs.getString("NUM_OBSERV"));
        php.setPrediction(rs.getString("PREDICTION"));
        php.setBasis(rs.getString("BASIS"));
        php.setProteinId(rs.getString("PROTEIN_ID"));
        php.setInvertedFlag(rs.getString("INVERTED_FLAG"));

        php.setEffect(rs.getString("EFFECT"));
        php.setSite(rs.getString("SITE"));
        php.setNumStructureFilt(rs.getString("NUM_STRUCT_FILT"));
        php.setPdbId(rs.getString("PDB_ID"));
        php.setPosition(rs.getInt("POSITION"));

        return php;
    }
}
