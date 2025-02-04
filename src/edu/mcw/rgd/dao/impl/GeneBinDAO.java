package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneBinCountGenesQuery;
import edu.mcw.rgd.dao.spring.GeneBinQuery;
import edu.mcw.rgd.datamodel.GeneBin.GeneBin;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinCountGenes;

import java.util.List;

public class GeneBinDAO extends AbstractDAO {

    /**
     *
     * @param termAcc Unique for each term
     * @param rgdId Unique for each gene
     * @return list of genes if the gene from the FULL_ANNOT and its FULL_ANNOT_INDEX table
     * @throws Exception SQLExceptions
     */

    public List<GeneBin> getGeneBinPair(int rgdId, String termAcc) throws  Exception{
        String GET_BIN_DETAILS ="select fa.ANNOTATED_OBJECT_RGD_ID as RGD_ID, fa.OBJECT_SYMBOL as GENE_SYMBOL, fai.term_acc, fa.term " +
                "from full_annot fa, full_annot_index fai where fa.full_annot_key=fai.full_annot_key " +
                "and fa.annotated_object_rgd_id=? and fai.term_acc=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_BIN_DETAILS, rgdId, termAcc);
        return genes;
    }

    /**
     *
     * @param rgdId Unique for each gene
     * @param geneSymbol Gene name -- Multiple species can have the same gene name
     * @return a boolean which represents the presence of a gene in the GENEBIN Table
     * @throws Exception SQLExceptions
     */
    public boolean existsGene(int rgdId, String geneSymbol) throws Exception {

        // check if gene is already present in the table
        int exists = getCount("SELECT COUNT(GENE_SYMBOL) FROM GENEBIN WHERE RGD_ID=? or GENE_SYMBOL=?" , rgdId, geneSymbol);
        return exists>0;
    }

    /**
     *
     * @param rgdId Unique for each gene
     * @param geneSymbol Gene name -- Multiple species can have the same gene name
     * @param term Name of the bin category
     * @param termAcc Unique for each term
     * @return an int if there was a successful insertion of the gene in the GENEBIN Table
     * @throws Exception SQLExceptions
     */
    public int insertGeneInBin(int rgdId, String geneSymbol, String term, String termAcc, String childTermAcc) throws Exception {
        if(!this.existsGene(rgdId, geneSymbol)){
            String INSERT_GENE = "insert into GENEBIN(RGD_ID, GENE_SYMBOL, TERM, TERM_ACC, CHILD_TERM_ACC) values (?,?,?,?,?)";
            return update(INSERT_GENE, rgdId, geneSymbol, term, termAcc, childTermAcc);
        }
        return 0;
    }

    /**
     *
     * @param termAcc Unique for each term
     * @return list of genes from the GENEBIN given the bin category
     * @throws Exception SQLExceptions
     */
    public List<GeneBin> getGenes(String termAcc) throws Exception{
        String GET_GENES = "select * from GENEBIN where TERM_ACC=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, termAcc);
        return genes;
    }

    public List<GeneBin> getGenesOfDecendents(String childTermAcc) throws Exception{
        String GET_GENES = "select * from GENEBIN where CHILD_TERM_ACC=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, childTermAcc);
        return genes;
    }

    /**
     *
     * @param rgdId Unique for each gene
     * @return genes if already present in the GENEBIN Table
     * @throws Exception SQLExceptions
     */

    public List<GeneBin> getGenesByRgdId(int rgdId) throws Exception{
        String GET_GENES = "select * from GENEBIN where RGD_ID=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, rgdId);
        return genes;
    }

    public List<GeneBinCountGenes> getGeneCounts() throws Exception{
        String GET_GENES_COUNT = "select TERM_ACC, count(RGD_ID) as TOTAL_GENES from GENEBIN group by TERM_ACC";
        List<GeneBinCountGenes> geneCounts = GeneBinCountGenesQuery.execute(this, GET_GENES_COUNT);
        return  geneCounts;
    }

    public List<GeneBinCountGenes> getGeneChildCounts() throws Exception{
        String GET_GENES_COUNT = "select CHILD_TERM_ACC as TERM_ACC, count(CHILD_TERM_ACC) as TOTAL_GENES from GENEBIN group by CHILD_TERM_ACC";
        List<GeneBinCountGenes> geneCounts = GeneBinCountGenesQuery.execute(this, GET_GENES_COUNT);
        return  geneCounts;
    }

    public void deleteAllGeneBins() throws Exception{
        String sql = "Delete from genebin";
        update(sql);
    }

    public void updateGeneChildTerm(int rgdId, String newChildTermAcc) throws Exception {
        String sql = "UPDATE GENEBIN SET CHILD_TERM_ACC=? WHERE RGD_ID=?";
        update(sql, newChildTermAcc, rgdId);
    }
    /**
     * Exception class for GeneBinDao
     */
    public class GeneBinDAOException extends Exception {
        public GeneBinDAOException(String msg) {
            super(msg);
        }
    }
}
