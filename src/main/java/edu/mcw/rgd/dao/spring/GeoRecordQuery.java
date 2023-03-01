package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GeoRecord;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by hsnalabolu on 5/8/2020.
 */
public class GeoRecordQuery extends MappingSqlQuery {
    public GeoRecordQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeoRecord rec = new GeoRecord();

        rec.setKey(rs.getInt("KEY"));
        rec.setGeoAccessionId(rs.getString("GEO_ACCESSION_ID"));
        rec.setStudyTitle(rs.getString("STUDY_TITLE"));
        rec.setSubmissionDate(rs.getString("SUBMISSION_DATE"));
        rec.setPubmedId(rs.getString("PUBMED_ID"));
        rec.setPlatformId(rs.getString("PLATFORM_ID"));
        rec.setPlatformName(rs.getString("PLATFORM_NAME"));
        rec.setPlatformTechnology(rs.getString("PLATFORM_TECHNOLOGY"));
        rec.setTotalNumberOfSamples(rs.getString("TOTAL_NUMBER_OF_SAMPLES"));
        rec.setNumberOfRatSamples(rs.getString("NUMBER_OF_RAT_SAMPLES"));
        rec.setContributors(rs.getString("CONTRIBUTORS"));
        rec.setSupplementaryFiles(rs.getString("SUPPLEMENTARY_FILES"));
        rec.setOverallDesign(rs.getString("OVERALL_DESIGN"));
        rec.setSampleAccessionId(rs.getString("SAMPLE_ACCESSION_ID"));
        rec.setSampleTitle(rs.getString("SAMPLE_TITLE"));
        rec.setSampleOrganism(rs.getString("SAMPLE_ORGANISM"));
        rec.setSampleSource(rs.getString("SAMPLE_SOURCE"));
        rec.setSampleAge(rs.getString("SAMPLE_AGE"));
        rec.setSampleGender(rs.getString("SAMPLE_GENDER"));
        rec.setSampleCellType(rs.getString("SAMPLE_CELL_TYPE"));
        rec.setSampleExtractionProtocol(rs.getString("SAMPLE_EXTRACT_PROTOCOL"));
        rec.setSampleTreatmentProtocol(rs.getString("SAMPLE_TREATMENT_PROTOCOL"));
        rec.setSummary(rs.getString("SUMMARY"));
        rec.setSampleDataProcessing(rs.getString("SAMPLE_DATA_PROCESSING"));
        rec.setSampleSupplementaryFiles(rs.getString("SAMPLE_SUPPLEMENTARY_FILES"));
        rec.setSampleCharacteristics(rs.getString("SAMPLE_CHARACTERISTICS"));
        rec.setSampleRelation(rs.getString("SAMPLE_RELATION"));
        rec.setSampleStrain(rs.getString("SAMPLE_STRAIN"));
        rec.setSampleCellLine(rs.getString("SAMPLE_CELL_LINE"));
        rec.setSampleGrowthProtocol(rs.getString("SAMPLE_GROWTH_PROTOCOL"));
        rec.setStudyRelation(rs.getString("STUDY_RELATION"));
        rec.setSampleTissue(rs.getString("SAMPLE_TISSUE"));
        rec.setRgdTissueTermAcc(rs.getString("RGD_TISSUE_TERM_ACC"));
        rec.setRgdCellTermAcc(rs.getString("RGD_CELL_TERM_ACC"));
        rec.setRgdStrainTermAcc(rs.getString("RGD_STRAIN_TERM_ACC"));
        rec.setRgdStrainRgdId(rs.getInt("RGD_STRAIN_RGD_ID"));
        rec.setCurationStatus(rs.getString("CURATION_STATUS"));
        return rec;
    }

}
