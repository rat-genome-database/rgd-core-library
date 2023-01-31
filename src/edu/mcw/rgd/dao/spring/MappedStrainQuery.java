package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.MappedStrain;
import edu.mcw.rgd.datamodel.Strain;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MappedStrainQuery extends StrainQuery{
    public MappedStrainQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        MappedStrain mg = new MappedStrain();

        mg.setStrain((Strain) super.mapRow(rs, rowNum));

        mg.setStart(rs.getLong("start_pos"));
        mg.setStop(rs.getLong("stop_pos"));
        mg.setChromosome(rs.getString("chromosome"));
        mg.setMapKey(rs.getInt("map_key"));
        mg.setStrand(rs.getString("strand"));

        return mg;
    }

    public static List<MappedStrain> run(AbstractDAO dao, String sql, Object... params) throws Exception {
        MappedStrainQuery q = new MappedStrainQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
