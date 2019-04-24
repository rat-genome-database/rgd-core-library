package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.pheno.GeneExpressionRecordValue;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 10/31/2018
 * Represents a row in GENE_EXPRESSION_VALUES table.
 */
public class GeneExpressionRecordValueQuery  extends MappingSqlQuery {

    public GeneExpressionRecordValueQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        GeneExpressionRecordValue rec = new GeneExpressionRecordValue();
        rec.setId(rs.getInt("gene_expression_value_id"));
        rec.setExpressedObjectRgdId(rs.getInt("expressed_object_rgd_id"));
        rec.setExpressionMeasurementAccId(rs.getString("expression_measurement_ont_id"));
        rec.setNotes(rs.getString("expression_value_notes"));
        rec.setGeneExpressionRecordId(rs.getInt("gene_expression_exp_record_id"));
        rec.setExpressionValue(rs.getDouble("expression_value"));
        rec.setExpressionUnit(rs.getString("expression_unit"));
        rec.setMapKey(rs.getInt("map_key"));
        return rec;
    }
}
