package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GeneLoci;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 12/20/2019.
 */
public class GeneLociQuery extends MappingSqlQuery {
    public GeneLociQuery(DataSource ds, String sql){
        super(ds, sql);
    }
    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GeneLoci gl= new GeneLoci();
        gl.setChromosome(rs.getString("chromosome"));
        gl.setMapKey(rs.getInt("map_key"));
        gl.setPosition(rs.getLong("POS"));
        gl.setGenicStatus(rs.getString("GENIC_STATUS"));
        gl.setGeneSymbols(rs.getString("GENE_SYMBOLS"));
        return gl;
    }
}
