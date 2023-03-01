package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneticModelQuery;
import edu.mcw.rgd.datamodel.models.GeneticModel;

import java.util.List;

/**
 * Created by jthota on 9/2/2016.
 */
public class GeneticModelsDAO extends AbstractDAO {


    public List<GeneticModel> getStrains() throws Exception{
        String sql="SELECT gg.rgd_id as gene_rgd_id,gg.full_name, gg.gene_symbol, ga.rgd_id as allele_rgd_id, "+
                "ga.gene_symbol as allele_symbol, s.rgd_id as strain_rgd_id, s.strain_symbol, s.source, "+
                "s.modification_method, s.background_strain_rgd_id "+
                "FROM rgd_strains_rgd rs "+
                "JOIN rgd_ids r on r.rgd_id=rs.rgd_id "+
                "JOIN strains s on s.strain_key=rs.strain_key "+
                "JOIN rgd_ids r2 on r2.rgd_id=s.rgd_id "+
                "JOIN genes ga on ga.rgd_id=rs.rgd_id "+
                "JOIN genes_variations gv on gv.variation_key=ga.gene_key "+
                "JOIN genes gg on gg.gene_key=gv.gene_key "+
                "WHERE gv.gene_variation_type=\'allele\'"+
                " AND (s.source like \'%gerrc%\' or s.source like \'%physgenknockouts%\' or s.source like \'%PhysGen%\')"+
                " AND  s.strain_type_name_lc='mutant' AND r.object_status='ACTIVE' AND r2.object_status='ACTIVE' "+
                "ORDER BY s.source desc ";
        GeneticModelQuery q= new GeneticModelQuery(this.getDataSource(), sql);
        return this.execute(q);
    }
    public List<GeneticModel> getGerrcModels() throws Exception{
        String sql="SELECT gg.rgd_id as gene_rgd_id,gg.full_name, gg.gene_symbol, ga.rgd_id as allele_rgd_id, "+
                "ga.gene_symbol as allele_symbol, s.rgd_id as strain_rgd_id, s.strain_symbol, s.source, "+
                "s.modification_method, s.background_strain_rgd_id "+
                "FROM rgd_strains_rgd rs "+
                "JOIN rgd_ids r on r.rgd_id=rs.rgd_id "+
                "JOIN strains s on s.strain_key=rs.strain_key "+
                "JOIN rgd_ids r2 on r2.rgd_id=s.rgd_id "+
                "JOIN genes ga on ga.rgd_id=rs.rgd_id "+
                "JOIN genes_variations gv on gv.variation_key=ga.gene_key "+
                "JOIN genes gg on gg.gene_key=gv.gene_key "+
                "WHERE gv.gene_variation_type=\'allele\'"+
                " AND (s.source like \'%gerrc%\' or s.source like \'%physgenknockouts%\' or s.source like \'%PhysGen%\' or s.source like '%PGA%')"+
                " AND  s.strain_type_name_lc='mutant' AND r.object_status='ACTIVE' AND r2.object_status='ACTIVE' "+
                "ORDER BY s.source desc ";
        GeneticModelQuery q= new GeneticModelQuery(this.getDataSource(), sql);
        return this.execute(q);
    }
    public List<GeneticModel> getAllModels() throws Exception{
        String sql="SELECT gg.rgd_id as gene_rgd_id,gg.full_name, gg.gene_symbol, ga.rgd_id as allele_rgd_id, "+
                "ga.gene_symbol as allele_symbol, s.rgd_id as strain_rgd_id, s.strain_symbol, s.source, "+
                "s.modification_method, s.background_strain_rgd_id "+
                "FROM rgd_strains_rgd rs "+
                "JOIN rgd_ids r on r.rgd_id=rs.rgd_id "+
                "JOIN strains s on s.strain_key=rs.strain_key "+
                "JOIN rgd_ids r2 on r2.rgd_id=s.rgd_id "+
                "JOIN genes ga on ga.rgd_id=rs.rgd_id "+
                "JOIN genes_variations gv on gv.variation_key=ga.gene_key "+
                "JOIN genes gg on gg.gene_key=gv.gene_key "+
                "WHERE gv.gene_variation_type=\'allele\'"+
                " AND ( s.strain_type_name_lc='mutant' )" +
                " AND r.object_status='ACTIVE' AND r2.object_status='ACTIVE' "+
                "ORDER BY s.source desc ";
        GeneticModelQuery q= new GeneticModelQuery(this.getDataSource(), sql);
        return this.execute(q);
    }
    public List<GeneticModel> getAllModelsByGeneRgdId(int geneRgdId) throws Exception{
        String sql="SELECT gg.rgd_id as gene_rgd_id,gg.full_name, gg.gene_symbol, ga.rgd_id as allele_rgd_id, "+
                "ga.gene_symbol as allele_symbol, s.rgd_id as strain_rgd_id, s.strain_symbol, s.source, "+
                "s.modification_method, s.background_strain_rgd_id "+
                "FROM rgd_strains_rgd rs "+
                "JOIN rgd_ids r on r.rgd_id=rs.rgd_id "+
                "JOIN strains s on s.strain_key=rs.strain_key "+
                "JOIN rgd_ids r2 on r2.rgd_id=s.rgd_id "+
                "JOIN genes ga on ga.rgd_id=rs.rgd_id "+
                "JOIN genes_variations gv on gv.variation_key=ga.gene_key "+
                "JOIN genes gg on gg.gene_key=gv.gene_key "+
                "WHERE gv.gene_variation_type=\'allele\'"+
                " AND ( s.strain_type_name_lc='mutant' )" +
                " AND r.object_status='ACTIVE' AND r2.object_status='ACTIVE' and gg.rgd_id=? "+
                "ORDER BY s.source desc ";
        GeneticModelQuery q= new GeneticModelQuery(this.getDataSource(), sql);
        return this.execute(q, geneRgdId);
    }
    public List<GeneticModel> getNonExtinctStrains() throws Exception{
        String sql="SELECT gg.rgd_id as gene_rgd_id,gg.full_name, gg.gene_symbol, ga.rgd_id as allele_rgd_id, ga.gene_symbol as allele_symbol,\n" +
            " s.rgd_id as strain_rgd_id, s.strain_symbol, s.source , s.modification_method, s.background_strain_rgd_id ,x.*\n" +
            "FROM rgd_strains_rgd rs ,rgd_ids r,strains s,rgd_ids r2,genes ga,genes_variations gv,genes gg,\n" +
            " (SELECT l.*,row_number() OVER(PARTITION BY strain_rgd_id ORDER BY strain_status_log_key DESC) AS rn FROM strain_status_log l) x \n" +
            "WHERE r.rgd_id=rs.rgd_id \n" +
            " AND s.strain_key=rs.strain_key \n" +
            " AND r2.rgd_id=s.rgd_id \n" +
            " AND ga.rgd_id= rs.rgd_id \n" +
            " AND gv.variation_key= ga.gene_key \n" +
            " AND gg.gene_key= gv.gene_key \n" +
            " AND x.strain_rgd_id=s.rgd_id\n" +
            " AND gv.gene_variation_type='allele' and \n" +
            "   (s.source like '%gerrc%' or s.source like '%physgenknockouts%' or s.source like '%PhysGen%') \n" +
            "   AND  s.STRAIN_TYPE_NAME_LC='mutant'  AND r.object_status='ACTIVE' AND r2.object_status='ACTIVE' \n" +
            " AND (live_animals_y_n='Y' OR cryopreserved_embryo_y_n='Y' OR cryopreserved_sperm_y_n='Y' OR cryorecovery_y_n='Y')"+
            "ORDER BY s.source desc ";


        GeneticModelQuery q= new GeneticModelQuery(this.getDataSource(), sql);
        return this.execute(q);
    }

    public int insert(String gene, String geneSymbol, String alleleSymbol, String strain) throws Exception{
        String sql="insert into knockouts(gene, symbol, allele, strain) values(?,?,?,?)";
        return this.update(sql, gene, geneSymbol, alleleSymbol, strain);
    }

}
