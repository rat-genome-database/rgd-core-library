package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.ProteinStructure;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Jun 26, 2014
 * <p>
 * Returns a row from PROTEIN_STRUCTURES+PROTEIN_STRUCTURE_GENES table join
 */
public class ProteinStructureQuery extends MappingSqlQuery {

    public ProteinStructureQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        ProteinStructure obj = new ProteinStructure();
        obj.setKey(rs.getInt("ps_key"));
        obj.setName(rs.getString("name"));
        obj.setModeller(rs.getString("modeller"));
        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setProteinAaRange(rs.getString("protein_aa_range"));
        obj.setProteinAccId(rs.getString("protein_acc_id"));
        obj.setVideoUrl(rs.getString("video_url"));
        return obj;
    }
}
