package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.MappedOrtholog;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class MappedOrthologQuery extends GeneQuery {

    public MappedOrthologQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedOrtholog obj = new MappedOrtholog();
        obj.setKey(rs.getInt("genetogene_key"));
        obj.setSrcRgdId(rs.getInt("src_rgd_id"));
        obj.setDestRgdId(rs.getInt("dest_rgd_id"));
        obj.setSrcSpeciesTypeKey(rs.getInt("src_species_type_key"));
        obj.setDestSpeciesTypeKey(rs.getInt("dest_species_type_key"));
        obj.setSrcGeneSymbol(rs.getString("src_gene_symbol"));
        obj.setDestGeneSymbol(rs.getString("dest_gene_symbol"));
        obj.setSrcStartPos(rs.getLong("src_start_pos"));
        obj.setSrcStopPos(rs.getLong("src_stop_pos"));
        obj.setSrcChromosome(rs.getString("src_chromosome"));
        obj.setSrcStrand(rs.getString("src_strand"));
        obj.setDestStartPos(rs.getLong("dest_start_pos"));
        obj.setDestStopPos(rs.getLong("dest_stop_pos"));
        obj.setDestChromosome(rs.getString("dest_chromosome"));
        obj.setDestStrand(rs.getString("dest_strand"));
        return obj;
    }

    public static List<MappedOrtholog> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedOrthologQuery q = new MappedOrthologQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
