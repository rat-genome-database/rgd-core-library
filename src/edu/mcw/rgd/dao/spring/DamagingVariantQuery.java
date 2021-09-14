package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.DamagingVariant;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DamagingVariantQuery extends MappingSqlQuery {
    public DamagingVariantQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        DamagingVariant dv = new DamagingVariant();

        dv.setRgdId(rs.getInt("VAR_RGD_ID"));
        dv.setRefNuc(rs.getString("REF_NUC"));
        dv.setVariantType(rs.getString("VARIANT_TYPE"));
        dv.setVarNuc(rs.getString("VAR_NUC"));
        dv.setRsId(rs.getString("RS_ID"));
        dv.setClinvarId(rs.getString("CLINVAR_ID"));
        dv.setSpeciesTypeKey(rs.getInt("SPECIES_TYPE_KEY"));
        dv.setGeneSymbol(rs.getString("GENE_SYMBOL"));
        dv.setChromosome(rs.getString("CHROMOSOME"));
        dv.setPaddingBase(rs.getString("PADDING_BASE"));
        dv.setEndPos(rs.getInt("END_POS"));
        dv.setStartPos(rs.getInt("START_POS"));
        dv.setGenicStatus(rs.getString("GENIC_STATUS"));
        dv.setMapKey(rs.getInt("MAP_KEY"));

        return dv;
    }

    public static List<DamagingVariant> execute(AbstractDAO dao, DataSource src, String sql, Object... params) throws Exception {
        DamagingVariantQuery dvq = new DamagingVariantQuery(src,sql);
        return dao.execute(dvq,params);
    }
}
