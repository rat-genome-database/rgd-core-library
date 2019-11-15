package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MappedGene;
import edu.mcw.rgd.datamodel.MappedGenePosition;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA. <br>
 * User: jdepons <br>
 * Date: Jan 17, 2008 <br>
 * Time: 10:08:19 AM
 * <p>
 * mapping sql query to work with lists of Gene objects with positions
 */
public class MappedGenePositionQuery extends GeneQuery {

    public MappedGenePositionQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedGenePosition mg = new MappedGenePosition();

        mg.setRgdId(rs.getInt("rgd_id"));
        mg.setSymbol(rs.getString("symbol"));
        mg.setStart(rs.getLong("start_pos"));
        mg.setStop(rs.getLong("stop_pos"));
        mg.setChromosome(rs.getString("chromosome"));
        mg.setMapKey(rs.getInt("map_key"));
        mg.setStrand(rs.getString("strand"));

        return mg;
    }

    public static List<MappedGenePosition> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedGenePositionQuery q = new MappedGenePositionQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
