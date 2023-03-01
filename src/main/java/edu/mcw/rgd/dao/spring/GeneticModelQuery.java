package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.impl.StrainDAO;
import edu.mcw.rgd.datamodel.Strain;
import edu.mcw.rgd.datamodel.models.GeneticModel;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 9/2/2016.
 */
public class GeneticModelQuery extends MappingSqlQuery {

    public GeneticModelQuery(DataSource ds, String query){
        super(ds, query);
    }
    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        GeneticModel strain= new GeneticModel();
        strain.setGene(rs.getString("full_name"));
        strain.setGeneSymbol(rs.getString("gene_symbol"));
        strain.setAlleleSymbol(rs.getString("allele_symbol"));
        strain.setStrainSymbol(rs.getString("strain_symbol"));
        strain.setGeneRgdId(rs.getInt("gene_rgd_id"));
        strain.setAlleleRgdId(rs.getInt("allele_rgd_id"));
        strain.setStrainRgdId(rs.getInt("strain_rgd_id"));
        strain.setSource(rs.getString("source"));
        strain.setBackgroundStrainRgdId(rs.getInt("background_strain_rgd_id"));
        strain.setMethod(rs.getString("modification_method"));
        try {
            if(rs.getInt("background_strain_rgd_id")!=0) {
                strain.setBackgroundStrain(this.getBackgroundStrainSymbol(rs.getInt("background_strain_rgd_id")));
            }
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return strain;
    }
    public String getBackgroundStrainSymbol(int backgroundStrainRgdId) throws Exception {
        StrainDAO sdao= new StrainDAO();

        Strain s=sdao.getStrain(backgroundStrainRgdId);
        return s.getSymbol();
    }
}
