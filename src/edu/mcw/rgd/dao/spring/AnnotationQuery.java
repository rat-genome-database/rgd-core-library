package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.ontology.Annotation;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * Returns a row from the Annotation table
 */
public class AnnotationQuery extends MappingSqlQuery {

    public AnnotationQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Annotation annot = new Annotation();
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
        annot.setEvidence(rs.getString("evidence"));
        annot.setCreatedDate(rs.getTimestamp("created_date"));
        annot.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        annot.setTermAcc(rs.getString("term_acc"));
        annot.setCreatedBy(rs.getInt("created_by"));
        if (rs.wasNull()) annot.setCreatedBy(null);
        annot.setLastModifiedBy(rs.getInt("last_modified_by"));
        if (rs.wasNull()) annot.setLastModifiedBy(null);
        annot.setXrefSource(rs.getString("xref_source"));

        try {
            annot.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored) {
        }

        annot.setAnnotationExtension(rs.getString("annotation_extension"));
        annot.setGeneProductFormId(rs.getString("gene_product_form_id"));
        annot.setOriginalCreatedDate(rs.getTimestamp("original_created_date"));

        annot.setQualifier2(rs.getString("qualifier2"));
        annot.setAssociatedWith(rs.getString("associated_with"));
        annot.setMolecularEntity(rs.getString("molecular_entity"));
        annot.setAlteration(rs.getString("alteration"));
        annot.setAlterationLocation(rs.getString("alteration_location"));
        annot.setVariantNomenclature(rs.getString("variant_nomenclature"));

        return annot;
    }

    public static List<Annotation> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        AnnotationQuery q = new AnnotationQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }

}