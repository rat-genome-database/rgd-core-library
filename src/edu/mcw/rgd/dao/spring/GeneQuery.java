package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.mcw.rgd.datamodel.Gene;

/**
 * Created by IntelliJ IDEA. <br>
 * User: jdepons <br>
 * Date: Jan 17, 2008 <br>
 * Time: 10:08:19 AM
 * <p>
 * mapping sql query to work with lists of Gene objects
 */
public class GeneQuery extends MappingSqlQuery {

    public GeneQuery(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Gene gene = new Gene();

        gene.setKey(rs.getInt("gene_key"));
        gene.setSymbol(rs.getString("gene_symbol"));
        gene.setName(rs.getString("full_name"));
        gene.setDescription(rs.getString("gene_desc"));
        gene.setProduct(rs.getString("product"));
        gene.setFunction(rs.getString("function"));
        gene.setNotes(rs.getString("notes"));
        gene.setRgdId(rs.getInt("rgd_id"));
        gene.setType(rs.getString("gene_type_lc"));
        gene.setNomenReviewDate(rs.getDate("nomen_review_date"));
        gene.setRefSeqStatus(rs.getString("refseq_status"));
        gene.setNcbiAnnotStatus(rs.getString("ncbi_annot_status"));

        try {
            gene.setSpeciesTypeKey(rs.getInt("species_type_key"));
        }catch (Exception ignored) {
        }

        return gene;
    }

    public static List<Gene> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneQuery q = new GeneQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
