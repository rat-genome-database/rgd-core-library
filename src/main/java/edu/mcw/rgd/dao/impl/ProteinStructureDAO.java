package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.ProteinStructureQuery;
import edu.mcw.rgd.datamodel.ProteinStructure;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 6/26/14
 * Time: 3:05 PM
 * <p>code to access data in tables PROTEIN_STRUCTURES and PROTEIN_STRUCTURE_GENES
 */
public class ProteinStructureDAO extends AbstractDAO {

    public List<ProteinStructure> getProteinStructuresForGene(int geneRgdId) throws Exception {

        String query = "SELECT * FROM protein_structure_genes psg,protein_structures ps " +
                "WHERE psg.ps_key=ps.ps_key AND rgd_id=?";
        ProteinStructureQuery q = new ProteinStructureQuery(this.getDataSource(), query);
        return execute(q, geneRgdId);
    }
}
