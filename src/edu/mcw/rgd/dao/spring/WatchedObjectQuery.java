package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.WatchedObject;
import edu.mcw.rgd.datamodel.annotation.Evidence;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 9/16/13
 * Time: 9:53 AM
 * query to use with Evidence datamodel class
 */
public class WatchedObjectQuery extends MappingSqlQuery {

    public WatchedObjectQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        WatchedObject wo = new WatchedObject();

        wo.setUsername(rs.getString("username"));
        wo.setRgdId(rs.getInt("rgd_id"));
        wo.setWatchingNomenclature(rs.getInt("nomen"));
        wo.setWatchingGo(rs.getInt("go"));
        wo.setWatchingDisease(rs.getInt("disease"));
        wo.setWatchingPhenotype(rs.getInt("phenotype"));
        wo.setWatchingPathway(rs.getInt("pathway"));
        wo.setWatchingStrain(rs.getInt("strain"));
        wo.setWatchingReference(rs.getInt("reference"));
        //wo.setWatchingAlteredStrains(rs.getInt("altered_strains"));
        wo.setWatchingProtein(rs.getInt("protein"));
        wo.setWatchingInteraction(rs.getInt("interaction"));
        wo.setWatchingRefseqStatus(rs.getInt("refseq_status"));
        wo.setWatchingExdb(rs.getInt("exdb"));
        wo.setSymbol(rs.getString("gene_symbol"));

        return wo;
    }
}