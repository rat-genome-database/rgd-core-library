package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.HgncFamily;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Returns a row from the hgnc_family table.
 */
public class HgncFamilyQuery extends MappingSqlQuery {

    public HgncFamilyQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        HgncFamily f = new HgncFamily();
        f.setFamilyId(rs.getInt("family_id"));
        f.setAbbreviation(rs.getString("abbreviation"));
        f.setName(rs.getString("name"));
        f.setExternalNote(rs.getString("external_note"));
        f.setPubmedIds(rs.getString("pubmed_ids"));
        f.setDescComment(rs.getString("desc_comment"));
        f.setDescLabel(rs.getString("desc_label"));
        f.setDescSource(rs.getString("desc_source"));
        f.setDescGo(rs.getString("desc_go"));
        f.setTypicalGene(rs.getString("typical_gene"));
        return f;
    }

    public static List<HgncFamily> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        HgncFamilyQuery q = new HgncFamilyQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
