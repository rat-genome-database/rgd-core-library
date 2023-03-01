package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.WatchedObject;
import edu.mcw.rgd.datamodel.WatchedTerm;
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
public class WatchedTermQuery extends MappingSqlQuery {

    public WatchedTermQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        WatchedTerm wt = new WatchedTerm();

        wt.setUsername(rs.getString("username"));
        wt.setAccId(rs.getString("acc_id"));
        wt.setTerm(rs.getString("term"));
        wt.setWatchingRatGenes(rs.getInt("genes_rat"));
        wt.setWatchingMouseGenes(rs.getInt("genes_mouse"));
        wt.setWatchingHumanGenes(rs.getInt("genes_human"));
        wt.setWatchingRatQTLS(rs.getInt("qtls_rat"));
        wt.setWatchingMouseQTLS(rs.getInt("qtls_mouse"));
        wt.setWatchingHumanQTLS(rs.getInt("qtls_human"));
        wt.setWatchingStrains(rs.getInt("strains"));
        wt.setWatchingRatVariants(rs.getInt("variants_rat"));

        return wt;
    }
}