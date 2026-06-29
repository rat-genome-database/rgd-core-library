package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneBinCountGenesQuery;
import edu.mcw.rgd.dao.spring.GeneBinQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
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
        // Reads from FULL_ANNOT / FULL_ANNOT_INDEX -- session independent, no SESSION_ID filter
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
     * @param sessionId Binning session the gene would belong to
     * @return a boolean which represents the presence of a gene in the GENEBIN Table for this session
     * @throws Exception SQLExceptions
     */
    public boolean existsGene(int rgdId, String geneSymbol, String sessionId) throws Exception {

        // check if gene is already present in the table for this session
        int exists = getCount("SELECT COUNT(GENE_SYMBOL) FROM GENEBIN WHERE (RGD_ID=? or GENE_SYMBOL=?) AND SESSION_ID=?" , rgdId, geneSymbol, sessionId);
        return exists>0;
    }

    /**
     *
     * @param rgdId Unique for each gene
     * @param geneSymbol Gene name -- Multiple species can have the same gene name
     * @param term Name of the bin category
     * @param termAcc Unique for each term
     * @param sessionId Binning session the gene is inserted into
     * @return an int if there was a successful insertion of the gene in the GENEBIN Table
     * @throws Exception SQLExceptions
     */
    public int insertGeneInBin(int rgdId, String geneSymbol, String term, String termAcc, String childTermAcc, String sessionId) throws Exception {
        if(!this.existsGene(rgdId, geneSymbol, sessionId)){
            String INSERT_GENE = "insert into GENEBIN(RGD_ID, GENE_SYMBOL, TERM, TERM_ACC, CHILD_TERM_ACC, SESSION_ID) values (?,?,?,?,?,?)";
            return update(INSERT_GENE, rgdId, geneSymbol, term, termAcc, childTermAcc, sessionId);
        }
        return 0;
    }

    /**
     *
     * @param termAcc Unique for each term
     * @param sessionId Binning session to read from
     * @return list of genes from the GENEBIN given the bin category
     * @throws Exception SQLExceptions
     */
    public List<GeneBin> getGenes(String termAcc, String sessionId) throws Exception{
        String GET_GENES = "select * from GENEBIN where TERM_ACC=? AND SESSION_ID=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, termAcc, sessionId);
        return genes;
    }

    public List<GeneBin> getGenesOfDecendents(String childTermAcc, String sessionId) throws Exception{
        String GET_GENES = "select * from GENEBIN where CHILD_TERM_ACC=? AND SESSION_ID=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, childTermAcc, sessionId);
        return genes;
    }

    /**
     *
     * @param rgdId Unique for each gene
     * @param sessionId Binning session to read from
     * @return genes if already present in the GENEBIN Table for this session
     * @throws Exception SQLExceptions
     */

    public List<GeneBin> getGenesByRgdId(int rgdId, String sessionId) throws Exception{
        String GET_GENES = "select * from GENEBIN where RGD_ID=? AND SESSION_ID=?";
        List<GeneBin> genes =  GeneBinQuery.execute(this, GET_GENES, rgdId, sessionId);
        return genes;
    }

    public List<GeneBinCountGenes> getGeneCounts(String sessionId) throws Exception{
        String GET_GENES_COUNT = "select TERM_ACC, count(RGD_ID) as TOTAL_GENES from GENEBIN where SESSION_ID=? group by TERM_ACC";
        List<GeneBinCountGenes> geneCounts = GeneBinCountGenesQuery.execute(this, GET_GENES_COUNT, sessionId);
        return  geneCounts;
    }

    public List<GeneBinCountGenes> getGeneChildCounts(String sessionId) throws Exception{
        String GET_GENES_COUNT = "select CHILD_TERM_ACC as TERM_ACC, count(CHILD_TERM_ACC) as TOTAL_GENES from GENEBIN where SESSION_ID=? group by CHILD_TERM_ACC";
        List<GeneBinCountGenes> geneCounts = GeneBinCountGenesQuery.execute(this, GET_GENES_COUNT, sessionId);
        return  geneCounts;
    }

    public void deleteAllGeneBins(String sessionId) throws Exception{
        String sql = "Delete from genebin where SESSION_ID=?";
        update(sql, sessionId);
    }

    public void updateGeneChildTerm(int rgdId, String newChildTermAcc, String sessionId) throws Exception {
        String sql = "UPDATE GENEBIN SET CHILD_TERM_ACC=? WHERE RGD_ID=? AND SESSION_ID=?";
        update(sql, newChildTermAcc, rgdId, sessionId);
    }

    public List<String> getChildTermsForParent(String parentTermAcc, String sessionId) throws Exception {
        String sql = "SELECT DISTINCT CHILD_TERM_ACC FROM GENEBIN WHERE TERM_ACC=? AND SESSION_ID=?";
        return StringListQuery.execute(this, sql, parentTermAcc, sessionId);
    }

    /**
     * Rename a session by moving all of its binned genes to a new session id.
     * @param oldSessionId existing session name
     * @param newSessionId new session name
     * @throws Exception
     */
    public void renameSession(String oldSessionId, String newSessionId) throws Exception {
        update("UPDATE GENEBIN SET SESSION_ID=? WHERE SESSION_ID=?", newSessionId, oldSessionId);
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
