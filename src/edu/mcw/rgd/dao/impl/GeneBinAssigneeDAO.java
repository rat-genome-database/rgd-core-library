package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneBinAssigneeQuery;
import edu.mcw.rgd.datamodel.GeneBin.GeneBinAssignee;

import java.util.List;

public class GeneBinAssigneeDAO extends AbstractDAO {
    public List<GeneBinAssignee> getAllAssignees() throws Exception {
        String GET_ALL_ASSIGNEES = "select * from GENEBIN_ASSIGNEE";

        List<GeneBinAssignee> assignees = GeneBinAssigneeQuery.execute(this, GET_ALL_ASSIGNEES);
        return assignees;
    }

    public List<GeneBinAssignee> getAssigneeName(String termAcc, String term) throws Exception{
        String GET_ASSIGNEE_NAME = "select * from GENEBIN_ASSIGNEE where TERM_ACC=? and TERM=?";
        List<GeneBinAssignee> assigneeName = GeneBinAssigneeQuery.execute(this, GET_ASSIGNEE_NAME, termAcc, term);
        return assigneeName;
    }

    public void updateAssigneeName(String assigneeName, String termAcc) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET assignee=? WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, assigneeName, termAcc);
        return;
    }

    public void updateCompletedStatus(String termAcc) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET completed=1 WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, termAcc);
        return;
    }


    public void updateTotalGenes(String termAcc, int totalGenes) throws Exception{
        String UPDATE_ASSIGNEE_NAME = "UPDATE GENEBIN_ASSIGNEE SET total_genes=? WHERE term_acc=?";
        update(UPDATE_ASSIGNEE_NAME, totalGenes, termAcc);
        return;
    }

}
