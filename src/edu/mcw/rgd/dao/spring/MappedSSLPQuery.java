package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MappedGene;
import edu.mcw.rgd.datamodel.MappedSSLP;
import edu.mcw.rgd.datamodel.SSLP;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: 2/13/20
 */
public class MappedSSLPQuery extends GeneQuery {

    public MappedSSLPQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedSSLP mg = new MappedSSLP();

        mg.setSSLP((SSLP) super.mapRow(rs, rowNum));

        mg.setStart(rs.getLong("start_pos"));
        mg.setStop(rs.getLong("stop_pos"));
        mg.setChromosome(rs.getString("chromosome"));
        mg.setMapKey(rs.getInt("map_key"));
        mg.setStrand(rs.getString("strand"));

        return mg;
    }

    public static List<MappedSSLP> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedSSLPQuery q = new MappedSSLPQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
