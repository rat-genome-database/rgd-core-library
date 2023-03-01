package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.VariantInfo;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 2/22/12
 * <p>
 * mapping sql query to work with lists of VariantInfo objects
 */
public class VariantQuery extends GenomicElementQuery {

    public VariantQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        VariantInfo obj = new VariantInfo();

        // read data from genomic elements table
        mapRowForGenomicElement(obj, rs);

        // read data from clinvar table
        mapVariants(rs, obj);

        return obj;
    }

    static public void mapVariants(ResultSet rs, VariantInfo obj) throws SQLException {

        // read data from clinvar table
        obj.setClinicalSignificance(rs.getString("clinical_significance"));
        obj.setDateLastEvaluated(rs.getDate("date_last_evaluated"));
        obj.setReviewStatus(rs.getString("review_status"));
        obj.setMethodType(rs.getString("method_type"));
        obj.setNucleotideChange(rs.getString("nucleotide_change"));
        obj.setTraitName(rs.getString("trait_name"));
        obj.setAgeOfOnset(rs.getString("age_of_onset"));
        obj.setPrevalence(rs.getString("prevalence"));
        obj.setMolecularConsequence(rs.getString("molecular_consequence"));
        obj.setSubmitter(rs.getString("submitter"));
    }
}
