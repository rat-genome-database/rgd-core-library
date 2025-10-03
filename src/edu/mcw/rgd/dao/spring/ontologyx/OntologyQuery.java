package edu.mcw.rgd.dao.spring.ontologyx;

import edu.mcw.rgd.datamodel.ontologyx.Ontology;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.<br>
 * User: mtutaj<br>
 * Date: Jun 8, 2011<br>
 * Time: 9:28:22 AM<br>
 * <p>
 * represents a row in ONTOLOGIES table -- an Ontology object
 */
public class OntologyQuery extends MappingSqlQuery {

    public OntologyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ontology ont = new Ontology();
        ont.setId(rs.getString("ONT_ID"));
        ont.setName(rs.getString("ONT_NAME"));
        ont.setDescription(rs.getString("ONT_DESC"));
        ont.setAspect(rs.getString("ASPECT"));
        ont.setPublic(rs.getInt("IS_PUBLIC")>0 ? true : false);
        ont.setOboHeader(rs.getString("OBO_HEADER"));
        ont.setHomePage(rs.getString("HOME_PAGE"));
        ont.setLogoUrl(rs.getString("LOGO_URL"));
        ont.setRootTermAcc(rs.getString("ROOT_TERM_ACC"));
        try {
            ont.setRefRgdId(rs.getInt("REF_RGD_ID"));
        }catch (Exception ignored){}
        return ont;
    }
}

