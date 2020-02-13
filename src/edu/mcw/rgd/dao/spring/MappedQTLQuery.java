package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.MappedGene;
import edu.mcw.rgd.datamodel.MappedQTL;
import edu.mcw.rgd.datamodel.QTL;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: 2/13/20
 */
public class MappedQTLQuery extends QTLQuery {

    public MappedQTLQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedQTL mg = new MappedQTL();

        mg.setQTL((QTL) super.mapRow(rs, rowNum));

        mg.setStart(rs.getLong("start_pos"));
        mg.setStop(rs.getLong("stop_pos"));
        mg.setChromosome(rs.getString("chromosome"));
        mg.setMapKey(rs.getInt("map_key"));
        mg.setStrand(rs.getString("strand"));

        return mg;
    }

    public static List<MappedQTL> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedQTLQuery q = new MappedQTLQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
