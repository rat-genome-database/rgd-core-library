package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.StudySampleMetadata;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudySampleMetadataQuery extends MappingSqlQuery {

    public StudySampleMetadataQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        StudySampleMetadata metadata = new StudySampleMetadata();
        // Sample fields
        metadata.setGeoSampleAcc(rs.getString("GEO_SAMPLE_ACC"));
        metadata.setExperimentId(rs.getInt("EXPERIMENT_ID"));
        if (rs.wasNull()) {
            metadata.setExperimentId(null);
        }
        metadata.setOrdinality(rs.getInt("EXP_COND_ORDINALITY"));
        if (rs.wasNull()) {
            metadata.setOrdinality(null);
        }
        metadata.setSex(rs.getString("SEX"));
        metadata.setComputedSex(rs.getString("COMPUTED_SEX"));
        metadata.setLifeStage(rs.getString("LIFE_STAGE"));
        metadata.setAgeDaysFromDobLowBound(rs.getDouble("AGE_DAYS_FROM_DOB_LOW_BOUND"));
        if (rs.wasNull()) {
            metadata.setAgeDaysFromDobLowBound(null);
        }
        metadata.setAgeDaysFromDobHighBound(rs.getDouble("AGE_DAYS_FROM_DOB_HIGH_BOUND"));
        if (rs.wasNull()) {
            metadata.setAgeDaysFromDobHighBound(null);
        }
        // Ontology term names (from JOINs)
        metadata.setTissue(rs.getString("Tissue"));
        metadata.setStrain(rs.getString("Strain"));
        metadata.setCellType(rs.getString("Cell_Type"));
        metadata.setExperimentalConditions(rs.getString("Experimental_Conditions"));

        // Experimental condition fields
        metadata.setExpCondAssocUnits(rs.getString("EXP_COND_ASSOC_UNITS"));

        metadata.setExpCondAssocValueMin(rs.getDouble("EXP_COND_ASSOC_VALUE_MIN"));
        if (rs.wasNull()) {
            metadata.setExpCondAssocValueMin(null);
        }

        metadata.setExpCondAssocValueMax(rs.getDouble("EXP_COND_ASSOC_VALUE_MAX"));
        if (rs.wasNull()) {
            metadata.setExpCondAssocValueMax(null);
        }

        metadata.setExpCondDurSecLowBound(rs.getDouble("EXP_COND_DUR_SEC_LOW_BOUND"));
        if (rs.wasNull()) {
            metadata.setExpCondDurSecLowBound(null);
        }

        metadata.setExpCondDurSecHighBound(rs.getDouble("EXP_COND_DUR_SEC_HIGH_BOUND"));
        if (rs.wasNull()) {
            metadata.setExpCondDurSecHighBound(null);
        }

        metadata.setExpCondApplicationMethod(rs.getString("EXP_COND_APPLICATION_METHOD"));
        metadata.setExpCondNotes(rs.getString("EXP_COND_NOTES"));

        return metadata;
    }
    public static List<StudySampleMetadata> execute(AbstractDAO dao, String sql, Object... params) throws  Exception {
        StudySampleMetadataQuery q = new StudySampleMetadataQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
