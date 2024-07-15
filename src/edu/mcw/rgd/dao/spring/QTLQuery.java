package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.mcw.rgd.datamodel.QTL;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * query to return a row from QTLS table; in addition it returns species type key
 */
public class QTLQuery extends MappingSqlQuery {

    public QTLQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        QTL qtl = new QTL();

        qtl.setKey(rs.getInt("qtl_key"));
        qtl.setSymbol(rs.getString("qtl_symbol"));
        qtl.setName(rs.getString("qtl_name"));
        qtl.setPeakOffset(rs.getInt("peak_offset"));
        if (rs.wasNull()) qtl.setPeakOffset(null);
        qtl.setChromosome(rs.getString("chromosome"));
        qtl.setLod(rs.getDouble("lod"));
        if (rs.wasNull()) qtl.setLod(null);
        qtl.setPValue(rs.getDouble("p_value"));
        if (rs.wasNull()) qtl.setPValue(null);
        qtl.setVariance(rs.getDouble("variance"));
        if (rs.wasNull()) qtl.setVariance(null);
        qtl.setNotes(rs.getString("notes"));
        qtl.setRgdId(rs.getInt("rgd_id"));
        qtl.setFlank1RgdId(rs.getInt("flank_1_rgd_id"));
        if (rs.wasNull()) qtl.setFlank1RgdId(null);
        qtl.setFlank2RgdId(rs.getInt("flank_2_rgd_id"));
        if (rs.wasNull()) qtl.setFlank2RgdId(null);
        qtl.setPeakRgdId(rs.getInt("peak_rgd_id"));
        if (rs.wasNull()) qtl.setPeakRgdId(null);
        qtl.setInheritanceType(rs.getString("inheritance_type"));
        qtl.setLodImage(rs.getString("lod_image"));
        qtl.setLinkageImage(rs.getString("linkage_image"));
        qtl.setSourceUrl(rs.getString("source_url"));
        qtl.setMostSignificantCmoTerm(rs.getString("most_significant_cmo_term"));
        qtl.setSpeciesTypeKey(rs.getInt("species_type_key"));
        qtl.setPeakRsId(rs.getString("PEAK_RS_ID"));
        qtl.setpValueMlog(rs.getDouble("P_VAL_MLOG"));
        if (rs.wasNull()) qtl.setpValueMlog(null);
        return qtl;
    }

    public static List<QTL> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        QTLQuery q = new QTLQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
