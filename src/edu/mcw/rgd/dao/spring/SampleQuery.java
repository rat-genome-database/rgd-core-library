package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Sample;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Returns a row from SAMPLE table (RATCNxxx schema)
 */
public class SampleQuery extends MappingSqlQuery {

    static DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public SampleQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Sample s = new Sample();
        String analysisDateStr = formatter.format(rs.getDate("analysis_time"));

        s.setId(rs.getInt("sample_id"));
        s.setAnalysisName(rs.getString("analysis_name"));
        s.setAnalystDate(analysisDateStr);
        s.setDescription(rs.getString("description"));
        s.setDirectory(rs.getString("REMOTE_DATA_LOAD_DIR"));
        s.setPatientId(rs.getInt("patient_id"));
        s.setSequencer(rs.getString("sequencer"));
        s.setGender(rs.getString("gender"));

        s.setGrantNumber(rs.getString("grant_number"));
        s.setSequencedBy(rs.getString("sequenced_by"));
        s.setWhereBred(rs.getString("where_bred"));
        s.setSecondaryAnalysisSoftware(rs.getString("secondary_analysis_software"));
        s.setMapKey(rs.getInt("map_key"));
        s.setDbSnpSource(rs.getString("dbsnp_source"));
        s.setStrainRgdId(rs.getInt("strain_rgd_id"));
        try{
            s.setRefRgdId(rs.getInt("ref_rgd_id"));
        } catch(Exception ignore) {

        }
        return s;
    }

}
