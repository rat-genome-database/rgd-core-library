package edu.mcw.rgd.dao.spring.phenominerExpectedRanges;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.dao.impl.PhenominerStrainGroupDao;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerExpectedRange;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerExpectedRangeQuery extends MappingSqlQuery {
    public PhenominerExpectedRangeQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected PhenominerExpectedRange mapRow(ResultSet rs, int i) throws SQLException {
        OntologyXDAO dao= new OntologyXDAO();
        PhenominerStrainGroupDao sdao= new PhenominerStrainGroupDao();
        PhenominerExpectedRange range= new PhenominerExpectedRange();
        range.setExpectedRangeId(rs.getInt("EXPECTED_RANGE_ID"));
        range.setExpectedRangeName(rs.getString("EXPECTED_RANGE_NAME"));
        String clinicalMeasurementOntId=rs.getString("CLINICAL_MEASUREMENT_ONT_ID");
        range.setClinicalMeasurementOntId(clinicalMeasurementOntId);
        try {
            range.setClinicalMeasurement(dao.getTermByAccId(clinicalMeasurementOntId).getTerm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        range.setStrainGroupId(rs.getInt("STRAIN_GROUP_ID"));
        range.setAgeLowBound(rs.getInt("AGE_DAYS_FROM_DOB_LOW_BOUND"));
        range.setAgeHighBound(rs.getInt("AGE_DAYS_FROM_DOB_HIGH_BOUND"));
        range.setSex(rs.getString("SEX"));
        range.setRangeValue(rs.getDouble("RANGE_VALUE"));
        range.setRangeSD(rs.getDouble("RANGE_SD"));
        range.setRangeLow(rs.getDouble("RANGE_LOW"));
        range.setRangeHigh(rs.getDouble("RANGE_HIGH"));
        range.setUnits(rs.getString("RANGE_UNITS"));
        String trait_ont_id=rs.getString("TRAIT_ONT_ID");
        try {
            if(trait_ont_id!=null) {
                range.setTrait(dao.getTermByAccId(trait_ont_id).getTerm());
                range.setTraitOntId(trait_ont_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            range.setStrainGroupName(sdao.getStrainGroupName(rs.getInt("STRAIN_GROUP_ID")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return range;
    }
}
