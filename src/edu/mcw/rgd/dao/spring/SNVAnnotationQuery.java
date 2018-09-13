package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ontology.Annotation;
import edu.mcw.rgd.datamodel.ontology.SNVAnnotation;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * <p>
 * Returns a row from the Annotation table
 */
public class SNVAnnotationQuery extends MappingSqlQuery {

    public SNVAnnotationQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        SNVAnnotation annot = new SNVAnnotation();
        annot.setKey(rs.getInt("full_annot_key"));
        annot.setTerm(rs.getString("term"));
        annot.setAnnotatedObjectRgdId(rs.getInt("annotated_object_rgd_id"));
        annot.setRgdObjectKey(rs.getInt("rgd_object_key"));
        annot.setDataSrc(rs.getString("data_src"));
        annot.setObjectSymbol(rs.getString("object_symbol"));
        annot.setRefRgdId(rs.getInt("ref_rgd_id"));
        annot.setWithInfo(rs.getString("with_info"));
        annot.setAspect(rs.getString("aspect"));
        annot.setObjectName(rs.getString("object_name"));
        try {
        annot.setNotes(rs.getString("notes"));
        }catch (Exception e) {

        }
        annot.setQualifier(rs.getString("qualifier"));
        annot.setRelativeTo(rs.getString("relative_to"));
        annot.setEvidence(rs.getString("evidence"));
        annot.setCreatedDate(rs.getTimestamp("created_date"));
        annot.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        annot.setExpRgdId(rs.getInt("exp_rgd_id"));
        if (rs.wasNull()) annot.setExpRgdId(null);
        annot.setTermAcc(rs.getString("term_acc"));
        annot.setCreatedBy(rs.getInt("created_by"));
        if (rs.wasNull()) annot.setCreatedBy(null);
        annot.setLastModifiedBy(rs.getInt("last_modified_by"));
        if (rs.wasNull()) annot.setLastModifiedBy(null);
        annot.setXrefSource(rs.getString("xref_source"));
        annot.setGeneRgdId(rs.getInt("geneRgdId"));
        try {
            annot.setConcreteTermAccId(rs.getString("childTerm"));
        }catch(Exception e) {

        }
        try {
            annot.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored) {
        }

        return annot;
    }

}