package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneBinAssigneeQuery;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinAssignee;

import java.util.List;

public class GeneBinAssigneeDAO extends AbstractDAO {
    /**
     *
     * @return Entire List of Assignee Details
     * @throws Exception
     */
    public List<GeneBinAssignee> getAllAssignees() throws Exception {
        String GET_ALL_ASSIGNEES = "select * from GENEBIN_ASSIGNEE";
        List<GeneBinAssignee> assignees = GeneBinAssigneeQuery.execute(this, GET_ALL_ASSIGNEES);
        return assignees;
    }

    /**
     *
     * @param termAcc
     * @return The assignee of a specific bin category
     * @throws Exception
     */

    public List<GeneBinAssignee> getAssigneeName(String termAcc) throws Exception{
        String GET_ASSIGNEE_NAME = "select * from GENEBIN_ASSIGNEE where TERM_ACC=?";
        List<GeneBinAssignee> assigneeName = GeneBinAssigneeQuery.execute(this, GET_ASSIGNEE_NAME, termAcc);
        return assigneeName;
    }

    /**
     *
     * @param termAcc
     * @return The assignee of a specific bin category
     * @throws Exception
     */

    public List<GeneBinAssignee> getTerm(String termAcc) throws Exception{
        String GET_ASSIGNEE_NAME = "select * from GENEBIN_ASSIGNEE where TERM_ACC=?";
        List<GeneBinAssignee> assigneeName = GeneBinAssigneeQuery.execute(this, GET_ASSIGNEE_NAME, termAcc);
        return assigneeName;
    }

    /**
     * Update the name of the curator for a specific bin category
     * @param assigneeName
     * @param termAcc
     * @throws Exception
     */

    public void updateAssigneeName(String assigneeName, String termAcc) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET assignee=? WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, assigneeName, termAcc);
        return;
    }

    /**
     * Mark a bin category as completed
     * @param termAcc
     * @throws Exception
     */

    public void updateCompletedStatus(String termAcc) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET completed=1 WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, termAcc);
        return;
    }

    /**
     * Update the total number of genes only in the parent bin category
     * @param termAcc
     * @param totalGenes
     * @throws Exception
     */

    public void updateTotalGenes(String termAcc, int totalGenes) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET total_genes=? WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, totalGenes, termAcc);
        return;
    }

    /**
     * Insert a new record into the GeneBin Assignee table -- used for intialization purpose ONLY!!
     * @param termAcc
     * @param term
     * @param parent
     * @throws Exception
     */

    public void insertAssignee(String termAcc, String term, int parent) throws Exception{
        String INSERT_NEW_ASSIGNEE = "INSERT INTO GENEBIN_ASSIGNEE(TERM_ACC, TERM, PARENT) VALUES (?,?,?)";
        update(INSERT_NEW_ASSIGNEE, termAcc, term, parent);
        return;
    }

    // Get all records including subsets for a term
    public List<GeneBinAssignee> getAssigneeRecordsWithSubsets(String termAcc) throws Exception {
        String sql = "SELECT * FROM GENEBIN_ASSIGNEE WHERE TERM_ACC=? ORDER BY SUBSET_NUM";
        return GeneBinAssigneeQuery.execute(this, sql, termAcc);
    }

    public void insertSubsetRecord(String termAcc, String term, int subsetNum, int totalGenes) throws Exception {
        String sql = "INSERT INTO GENEBIN_ASSIGNEE (TERM_ACC, TERM, SUBSET_NUM, TOTAL_GENES) VALUES (?,?,?,?)";
        update(sql, termAcc, term, subsetNum, totalGenes);
    }

    public void updateSubsetTotalGenes(String termAcc, int subsetNum, int totalGenes) throws Exception {
        String sql = "UPDATE GENEBIN_ASSIGNEE SET TOTAL_GENES=? WHERE TERM_ACC=? AND SUBSET_NUM=?";
        update(sql, totalGenes, termAcc, subsetNum);
    }

}
