package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.SslpsAllele;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Jan 25, 2011
 * Time: 9:44:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class SSLPSAlleleQuery extends MappingSqlQuery {

    public SSLPSAlleleQuery(DataSource ds, String query) {
        super(ds, query);
    }

    
    protected Object mapRow(ResultSet rs, int i) throws SQLException {

        SslpsAllele allele = new SslpsAllele();
        allele.setKey(rs.getInt("allele_key"));
        allele.setNotes(rs.getString("notes"));
        allele.setSize1(rs.getInt("size1"));
        allele.setSize2(rs.getInt("size2"));
        allele.setSslpKey(rs.getInt("sslp_key"));
        allele.setStrainKey(rs.getInt("strain_key"));
        allele.setStrainSymbol(rs.getString("strain_symbol"));
        allele.setStrainRgdId(rs.getInt("rgd_id"));

        return allele;
    }
}
