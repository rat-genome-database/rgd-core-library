package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Gene;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jdepons
 * @since Jan 17, 2008
 * <p>
 * mapping sql query to work with lists of Gene objects
 */
public class GeneQueryLazyLoad extends MappingSqlQuery {

    public GeneQueryLazyLoad(DataSource ds, String query) {
        super(ds, query);        
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Gene gene = new Gene();

        //gene.setKey(rs.getInt("gene_key"));
        gene.setSymbol(rs.getString("gene_symbol"));
        //gene.setName(rs.getString("full_name"));
        //gene.setDescription(rs.getString("gene_desc"));
        //gene.setNotes(rs.getString("notes"));
        gene.setRgdId(rs.getInt("rgd_id"));
        //gene.setType(rs.getString("gene_type_lc"));
        //gene.setNomenReviewDate(rs.getDate("nomen_review_date"));
        //gene.setRefSeqStatus(rs.getString("refseq_status"));
        //gene.setNcbiAnnotStatus(rs.getString("ncbi_annot_status"));
        //gene.setAgrDescription(rs.getString("agr_desc"));
        //gene.setMergedDescription(rs.getString("merged_desc"));
        //gene.setGeneSource(rs.getString("gene_source"));
        //gene.setEnsemblGeneSymbol(rs.getString("ensembl_gene_symbol"));
        //gene.setEnsemblGeneType(rs.getString("ensembl_gene_type"));
        //gene.setEnsemblFullName(rs.getString("ensembl_full_name"));
        //gene.setNomenSource(rs.getString("nomen_source"));


        try {
            gene.setSpeciesTypeKey(rs.getInt("species_type_key"));
            //gene.setTaglessAlleleSymbol(rs.getString("tagless_allele_symbol"));
        }catch (Exception ignored) {
        }

        return gene;
    }

    public static List<Gene> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        GeneQueryLazyLoad q = new GeneQueryLazyLoad(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
