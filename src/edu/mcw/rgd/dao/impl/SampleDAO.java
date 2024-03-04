package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.SampleQuery;
import edu.mcw.rgd.datamodel.Sample;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.*;

/**
 * @author GKowalski
 * Date: Jul 21, 2009
 */
public class SampleDAO extends JdbcBaseDAO {

    public List<Sample> getSamples(String patientId) {

        return getSamples(Integer.parseInt(patientId));
    }

    public List<Sample> getSamples(int patientId) {

        String query = "SELECT * FROM sample WHERE patient_id=?";
        return runSamplesQuery(query, patientId);
    }

    public List<Sample> getSamplesOrderedByName(int patientId) {
        String query = "SELECT * FROM sample WHERE patient_id=? ORDER BY analysis_name";
        return runSamplesQuery(query, patientId);
    }

    public List<Sample> getSamplesByMapKey(int mapKey) {
        String query = "SELECT * FROM sample "+
                "WHERE patient_id IN(SELECT patient_id FROM patient WHERE map_key=?) "+
                " and (analysis_name != 'CDR' and analysis_name != 'CDS') " +
                "ORDER BY analysis_name";
        return runSamplesQuery(query, mapKey);
    }
    public List<Sample> getSamplesByMapKey(int mapKey, String population) {
        String query = "SELECT * FROM sample "+
                "WHERE patient_id IN(SELECT patient_id FROM patient WHERE map_key=?) "+
                " and (analysis_name != 'CDR' and analysis_name != 'CDS') " +
                " and analysis_name like '"+ population+"%'" +
                " ORDER BY analysis_name";
        return runSamplesQuery(query, mapKey);
    }
    public List<Sample> getLimitedSamplesByPopulation(int mapKey, String population, int limit) {
        String query = "SELECT * FROM sample "+
                "WHERE patient_id IN(SELECT patient_id FROM patient WHERE map_key=?) "+
                " and (analysis_name != 'CDR' and analysis_name != 'CDS') " +
                " and analysis_name like '"+ population+"%'" +
                " and ROWNUM < ? "+
                " ORDER BY analysis_name "
               ;
        SampleQuery q = new SampleQuery(this.getDataSource(), query);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        return q.execute(mapKey,limit);

    }
    public Sample getSampleBySampleId(int sampleId) {

        String query = "SELECT * FROM sample WHERE sample_id=?";
        List<Sample> samples = runSamplesQuery(query, sampleId);
        return !samples.isEmpty() ? samples.get(0) : null;
    }

    public List<Sample> getSampleBySampleId(List<Integer> sampleIds) {

        String query = "SELECT * FROM sample WHERE sample_id in (";

        boolean first = true;
        for (Integer id: sampleIds) {
            if (first) {
                query = query + id;
            }else {
                query += "," + id;
            }
            first=false;
        }
        query += ") order by analysis_name";

        // System.out.println(query);

        SampleQuery q = new SampleQuery(this.getDataSource(), query);
        q.compile();
        return q.execute();

    }

    private List<Sample> runSamplesQuery(String query, int param) {
        SampleQuery q = new SampleQuery(this.getDataSource(), query);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        return q.execute(param);
    }


    /**
     * Get all samples for a given patientId list in an Array
     *
     * @param patientIdArray
     * @return
     */
    public List<Sample> getSamples(String[] patientIdArray) {

        if (patientIdArray == null) return null;
        String patientIds = Utils.concatenate(patientIdArray, ",");
        String sql = "SELECT * FROM sample WHERE patient_id IN (" + patientIds + ")";
        SampleQuery q = new SampleQuery(this.getDataSource(), sql);
        q.compile();
        return q.execute();
    }

    public Sample getSample(int sampleId) {
        String sql = "SELECT * FROM sample WHERE sample_id=?";
        List<Sample> samples = runSamplesQuery(sql, sampleId);
        return samples.isEmpty() ? null : samples.get(0);
    }

    public Sample getSampleByStrainRgdId(int strainRgdId, int patientId) {
        String sql = "SELECT * FROM sample WHERE strain_rgd_id=? and patient_id=?";
        SampleQuery q = new SampleQuery(this.getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        List<Sample> samples = q.execute(strainRgdId, patientId);
       return samples.isEmpty() ? null : samples.get(0);
    }
    public Sample getSampleByStrainRgdIdNMapKey(int strainRgdId, int mapKey) {
        String sql = "SELECT * FROM sample WHERE strain_rgd_id=? and patient_id in (" +
                "select patient_id from patient where mapKey=?)";
        SampleQuery q = new SampleQuery(this.getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        List<Sample> samples = q.execute(strainRgdId, mapKey);
        return samples.isEmpty() ? null : samples.get(0);
    }

    public List<Sample> getSamplesByStrainRgdId(int strainRgdId) {
        String sql = "SELECT * FROM sample WHERE strain_rgd_id=?";
        SampleQuery q = new SampleQuery(this.getDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        List<Sample> samples = q.execute(strainRgdId);
        return samples;
    }

    public Sample getSampleByAnalysisNameAndMapKey(String analysisName, int mapKey) throws Exception{
        String sql = "select * from sample where analysis_name like ? and map_key=?";
        SampleQuery q = new SampleQuery(this.getDataSource(),sql);
        q.declareParameter(new SqlParameter(Types.VARCHAR));
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        List<Sample> samples = q.execute(analysisName, mapKey);
        if (samples == null || samples.isEmpty())
            return null;
        return samples.get(0);
    }
}
