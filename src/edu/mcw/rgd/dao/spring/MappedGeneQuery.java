package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MappedGene;

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
public class MappedGeneQuery extends GeneQuery {

    public MappedGeneQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedGene mg = new MappedGene();

        mg.setGene((Gene) super.mapRow(rs, rowNum));

        mg.setStart(rs.getLong("start_pos"));
        mg.setStop(rs.getLong("stop_pos"));
        mg.setChromosome(rs.getString("chromosome"));
        mg.setMapKey(rs.getInt("map_key"));
        mg.setStrand(rs.getString("strand"));

        return mg;
    }

    public static List<MappedGene> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedGeneQuery q = new MappedGeneQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
