package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneBinAssigneeQuery;
import edu.mcw.rgd.dao.spring.GeneBinCountGenesQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinAssignee;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinCountGenes;

import java.util.List;

public class GeneBinAssigneeDAO extends AbstractDAO {

    /** Session whose bin definitions are used as the template when creating a new session. */
    public static final String TEMPLATE_SESSION = "default";

    /**
     *
     * @param sessionId Binning session to read from
     * @return Entire List of Assignee Details for the session
     * @throws Exception
     */
    public List<GeneBinAssignee> getAllAssignees(String sessionId) throws Exception {
        String GET_ALL_ASSIGNEES = "select * from GENEBIN_ASSIGNEE where SESSION_ID=?";
        List<GeneBinAssignee> assignees = GeneBinAssigneeQuery.execute(this, GET_ALL_ASSIGNEES, sessionId);
        return assignees;
    }

    /**
     *
     * @param termAcc
     * @param sessionId Binning session to read from
     * @return The assignee of a specific bin category
     * @throws Exception
     */

    public List<GeneBinAssignee> getAssigneeName(String termAcc, String sessionId) throws Exception{
        String GET_ASSIGNEE_NAME = "select * from GENEBIN_ASSIGNEE where TERM_ACC=? AND SESSION_ID=?";
        List<GeneBinAssignee> assigneeName = GeneBinAssigneeQuery.execute(this, GET_ASSIGNEE_NAME, termAcc, sessionId);
        return assigneeName;
    }

    /**
     *
     * @param termAcc
     * @param sessionId Binning session to read from
     * @return The assignee of a specific bin category
     * @throws Exception
     */

    public List<GeneBinAssignee> getTerm(String termAcc, String sessionId) throws Exception{
        String GET_ASSIGNEE_NAME = "select * from GENEBIN_ASSIGNEE where TERM_ACC=? AND SESSION_ID=?";
        List<GeneBinAssignee> assigneeName = GeneBinAssigneeQuery.execute(this, GET_ASSIGNEE_NAME, termAcc, sessionId);
        return assigneeName;
    }

    /**
     * Update the name of the curator for a specific bin category
     * @param assigneeName
     * @param termAcc
     * @param sessionId Binning session to update
     * @throws Exception
     */

    public void updateAssigneeName(String assigneeName, String termAcc, String sessionId) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET assignee=? WHERE term_acc=? AND SESSION_ID=?";
        update(UPDATE_ASSIGNEE_NAME, assigneeName, termAcc, sessionId);
        return;
    }

    /**
     * Mark a bin category as completed
     * @param termAcc
     * @param sessionId Binning session to update
     * @throws Exception
     */

    public void updateCompletedStatus(String termAcc, String sessionId) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET completed=1 WHERE term_acc=? AND SESSION_ID=?";
        update(UPDATE_ASSIGNEE_NAME, termAcc, sessionId);
        return;
    }

    /**
     * Update the total number of genes only in the parent bin category
     * @param termAcc
     * @param totalGenes
     * @param sessionId Binning session to update
     * @throws Exception
     */

    public void updateTotalGenes(String termAcc, int totalGenes, String sessionId) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET total_genes=? WHERE term_acc=? AND SESSION_ID=?";
        update(UPDATE_ASSIGNEE_NAME, totalGenes, termAcc, sessionId);
        return;
    }

    /**
     * Insert a new record into the GeneBin Assignee table
     * @param termAcc
     * @param term
     * @param parent
     * @param sessionId Binning session the row belongs to
     * @throws Exception
     */

    public void insertAssignee(String termAcc, String term, int parent, String sessionId) throws Exception{
        String INSERT_NEW_ASSIGNEE = "INSERT INTO GENEBIN_ASSIGNEE(TERM_ACC, TERM, PARENT, SESSION_ID) VALUES (?,?,?,?)";
        update(INSERT_NEW_ASSIGNEE, termAcc, term, parent, sessionId);
        return;
    }

    // Get all records including subsets for a term
    public List<GeneBinAssignee> getAssigneeRecordsWithSubsets(String termAcc, String sessionId) throws Exception {
        String sql = "SELECT * FROM GENEBIN_ASSIGNEE WHERE TERM_ACC Like ? AND SESSION_ID=? ORDER BY SUBSET_NUM";
        return GeneBinAssigneeQuery.execute(this, sql, termAcc, sessionId);
    }

    public void insertSubsetRecord(String termAcc, String term, int subsetNum, int totalGenes,int parent, String sessionId) throws Exception {
        String sql = "INSERT INTO GENEBIN_ASSIGNEE (TERM_ACC, TERM, SUBSET_NUM, TOTAL_GENES,PARENT, SESSION_ID) VALUES (?,?,?,?,?,?)";
        update(sql, termAcc, term, subsetNum, totalGenes, parent, sessionId);
    }
    public void updateSubsetRecord(String termAcc, String term, int subsetNum, int totalGenes, int parent, String sessionId) throws Exception {
        String sql = "UPDATE GENEBIN_ASSIGNEE SET TERM=?, SUBSET_NUM=?, TOTAL_GENES=?, PARENT=? WHERE TERM_ACC=? AND SESSION_ID=?";
        update(sql, term, subsetNum, totalGenes, parent, termAcc, sessionId);
    }

    public void updateSubsetTotalGenes(String termAcc, int subsetNum, int totalGenes, String sessionId) throws Exception {
        String sql = "UPDATE GENEBIN_ASSIGNEE SET TOTAL_GENES=? WHERE TERM_ACC=? AND SUBSET_NUM=? AND SESSION_ID=?";
        update(sql, totalGenes, termAcc, subsetNum, sessionId);
    }

    public void updateSubsetNum(String termAcc,int subsetNum, String sessionId) throws Exception{
        String sql = "UPDATE GENEBIN_ASSIGNEE SET SUBSET_NUM=? WHERE TERM_ACC=? AND SESSION_ID=?";
        update(sql,subsetNum,termAcc, sessionId);
    }

    public List<GeneBinCountGenes> getGeneChildCounts(String sessionId) throws Exception {
        String GET_GENES_COUNT = "select TERM_ACC, TOTAL_GENES from GENEBIN_ASSIGNEE where TERM_ACC like '%(%)%' AND SESSION_ID=?";
        List<GeneBinCountGenes> geneCounts = GeneBinCountGenesQuery.execute(this, GET_GENES_COUNT, sessionId);
        return geneCounts;
    }
    public void resetBin(String termAcc, String sessionId) throws Exception {
        String sql = "UPDATE GENEBIN_ASSIGNEE SET TOTAL_GENES=0, COMPLETED=0, ASSIGNEE=null WHERE TERM_ACC=? AND SESSION_ID=?";
        update(sql, termAcc, sessionId);
    }

    public void deleteSubsetRecords(String sessionId) throws Exception {
        String sql = "DELETE FROM GENEBIN_ASSIGNEE WHERE TERM_ACC LIKE '%(%)%' AND SESSION_ID=?";
        update(sql, sessionId);
    }

    // ---- Session management ----------------------------------------------

    /**
     * List the distinct binning sessions that currently exist.
     * Derived from GENEBIN_ASSIGNEE since every session is seeded with its bin definitions there.
     * @return list of session ids (names)
     * @throws Exception
     */
    public List<String> getSessions() throws Exception {
        String sql = "SELECT DISTINCT SESSION_ID FROM GENEBIN_ASSIGNEE WHERE SESSION_ID IS NOT NULL ORDER BY SESSION_ID";
        return StringListQuery.execute(this, sql);
    }

    /**
     * @param sessionId session name to check
     * @return true if the session already has bin definitions
     * @throws Exception
     */
    public boolean sessionExists(String sessionId) throws Exception {
        return getCount("SELECT COUNT(*) FROM GENEBIN_ASSIGNEE WHERE SESSION_ID=?", sessionId) > 0;
    }

    /**
     * Create a new binning session by copying the base bin definitions (term / accession / parent flag,
     * excluding dynamically generated subset rows) from the template session. State columns
     * (assignee, completed, total_genes) start clean.
     * @param sessionId name of the new session
     * @throws Exception
     */
    public void createSession(String sessionId) throws Exception {
        String sql = "INSERT INTO GENEBIN_ASSIGNEE (TERM_ACC, TERM, PARENT, SESSION_ID) " +
                "SELECT TERM_ACC, TERM, PARENT, ? FROM GENEBIN_ASSIGNEE " +
                "WHERE SESSION_ID=? AND TERM_ACC NOT LIKE '%(%)%'";
        update(sql, sessionId, TEMPLATE_SESSION);
    }

    /**
     * Delete all assignee rows (bin definitions, state and subsets) for a session.
     * @param sessionId session to remove
     * @throws Exception
     */
    public void deleteSession(String sessionId) throws Exception {
        update("DELETE FROM GENEBIN_ASSIGNEE WHERE SESSION_ID=?", sessionId);
    }

    /**
     * Rename a session by moving all of its assignee rows to a new session id.
     * @param oldSessionId existing session name
     * @param newSessionId new session name
     * @throws Exception
     */
    public void renameSession(String oldSessionId, String newSessionId) throws Exception {
        update("UPDATE GENEBIN_ASSIGNEE SET SESSION_ID=? WHERE SESSION_ID=?", newSessionId, oldSessionId);
    }

}
