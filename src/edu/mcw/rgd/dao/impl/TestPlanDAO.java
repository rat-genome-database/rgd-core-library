package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.TestPlanItemQuery;
import edu.mcw.rgd.datamodel.TestPlanItem;
import org.springframework.jdbc.object.MappingSqlQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Site test plan for the Oracle -> PostgreSQL switch: the pages and tools to test, who tests each one,
 * the result, and the tester's sign-off (tables TEST_PLAN_ITEMS and TEST_PLAN_HISTORY).
 * People are identified by their GitHub login (the curation sign-in); testers are listed in TEST_PLAN_TESTERS.
 */
public class TestPlanDAO extends AbstractDAO {

    /** all items, in test-plan order */
    public List<TestPlanItem> getItems() throws Exception {
        String sql = "SELECT * FROM test_plan_items ORDER BY sort_order";
        return execute(new TestPlanItemQuery(getDataSource(), sql));
    }

    /** one item, or null when there is no item with this id */
    public TestPlanItem getItem(String itemId) throws Exception {
        String sql = "SELECT * FROM test_plan_items WHERE item_id=?";
        List<TestPlanItem> items = execute(new TestPlanItemQuery(getDataSource(), sql), itemId);
        return items.isEmpty() ? null : items.get(0);
    }

    /**
     * changes to an item, newest first
     * @return list of {date as 'YYYY-MM-DD HH24:MI', GitHub login, what changed}
     */
    public List<String[]> getHistory(String itemId) throws Exception {
        String sql = "SELECT TO_CHAR(change_date,'YYYY-MM-DD HH24:MI') AS changed, changed_by, action "
                + "FROM test_plan_history WHERE item_id=? ORDER BY change_date DESC, history_key DESC";
        MappingSqlQuery<String[]> q = new MappingSqlQuery<String[]>(getDataSource(), sql) {
            @Override
            protected String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new String[]{rs.getString("changed"), rs.getString("changed_by"), rs.getString("action")};
            }
        };
        q.declareParameter(new org.springframework.jdbc.core.SqlParameter(Types.VARCHAR));
        q.compile();
        return q.execute(itemId);
    }

    /**
     * people who can be assigned items (table TEST_PLAN_TESTERS): GitHub login -> name, ordered by name
     */
    public Map<String, String> getTesters() throws Exception {
        String sql = "SELECT github_login, name FROM test_plan_testers ORDER BY name";
        Map<String, String> testers = new LinkedHashMap<>();
        MappingSqlQuery<String[]> q = new MappingSqlQuery<String[]>(getDataSource(), sql) {
            @Override
            protected String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new String[]{rs.getString("github_login"), rs.getString("name")};
            }
        };
        q.compile();
        for (String[] row : q.execute()) {
            testers.put(row[0], row[1]);
        }
        return testers;
    }

    /** assign an item to a tester (GitHub login), or clear the assignee with null */
    public void assign(String itemId, String assignee, String changedBy) throws Exception {
        String sql = "UPDATE test_plan_items SET assignee=?, last_modified_by=?, last_modified_date=LOCALTIMESTAMP(0) "
                + "WHERE item_id=?";
        update(sql, assignee, changedBy, itemId);
        logChange(itemId, changedBy, assignee == null ? "cleared the assignee" : "assigned to " + assignee);
    }

    /**
     * set the result of an item; any status other than Passed withdraws the sign-off
     * @param notes new notes, or null to keep the current notes
     */
    public void setStatus(String itemId, String status, String notes, String changedBy) throws Exception {
        if (!Arrays.asList(TestPlanItem.STATUSES).contains(status)) {
            throw new IllegalArgumentException("unknown test plan status: " + status);
        }
        String sql = "UPDATE test_plan_items SET status=?, notes=COALESCE(CAST(? AS TEXT), notes), "
                + "signed_by=CASE WHEN ?='Passed' THEN signed_by END, "
                + "signed_date=CASE WHEN ?='Passed' THEN signed_date END, "
                + "last_modified_by=?, last_modified_date=LOCALTIMESTAMP(0) WHERE item_id=?";
        update(sql, status, notes, status, status, changedBy, itemId);
        logChange(itemId, changedBy, "set " + status);
    }

    /** replace an item's notes */
    public void setNotes(String itemId, String notes, String changedBy) throws Exception {
        String sql = "UPDATE test_plan_items SET notes=?, last_modified_by=?, last_modified_date=LOCALTIMESTAMP(0) "
                + "WHERE item_id=?";
        update(sql, notes, changedBy, itemId);
        logChange(itemId, changedBy, "updated the notes");
    }

    /**
     * sign off an item; only its assignee can, and only once it has Passed
     * @return true if the item was signed off
     */
    public boolean signOff(String itemId, String tester) throws Exception {
        String sql = "UPDATE test_plan_items SET signed_by=?, signed_date=LOCALTIMESTAMP(0), "
                + "last_modified_by=?, last_modified_date=LOCALTIMESTAMP(0) "
                + "WHERE item_id=? AND status='Passed' AND assignee=? AND signed_by IS NULL";
        boolean signed = update(sql, tester, tester, itemId, tester) == 1;
        if (signed) {
            logChange(itemId, tester, "signed off");
        }
        return signed;
    }

    /** withdraw a sign-off */
    public void withdrawSignOff(String itemId, String changedBy) throws Exception {
        String sql = "UPDATE test_plan_items SET signed_by=NULL, signed_date=NULL, "
                + "last_modified_by=?, last_modified_date=LOCALTIMESTAMP(0) WHERE item_id=?";
        update(sql, changedBy, itemId);
        logChange(itemId, changedBy, "withdrew the sign-off");
    }

    private void logChange(String itemId, String changedBy, String action) throws Exception {
        String sql = "INSERT INTO test_plan_history (item_id, changed_by, change_date, action) "
                + "VALUES (?, ?, LOCALTIMESTAMP(0), ?)";
        update(sql, itemId, changedBy, action);
    }
}
