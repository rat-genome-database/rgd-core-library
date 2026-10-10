package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.TestPlanItem;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps rows of TEST_PLAN_ITEMS to TestPlanItem objects.
 */
public class TestPlanItemQuery extends MappingSqlQuery<TestPlanItem> {

    public TestPlanItemQuery(DataSource ds, String query) {
        super(ds, query);
    }

    @Override
    protected TestPlanItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        TestPlanItem i = new TestPlanItem();
        i.setItemId(rs.getString("item_id"));
        i.setArea(rs.getString("area"));
        i.setName(rs.getString("name"));
        i.setPath(rs.getString("path"));
        i.setCheckText(rs.getString("check_text"));
        i.setCovers(rs.getString("covers"));
        i.setCuration("Y".equals(rs.getString("curation")));
        i.setNoCompare("Y".equals(rs.getString("no_compare")));
        i.setSortOrder(rs.getInt("sort_order"));
        i.setAssignee(rs.getString("assignee"));
        i.setStatus(rs.getString("status"));
        i.setNotes(rs.getString("notes"));
        i.setSignedBy(rs.getString("signed_by"));
        i.setSignedDate(rs.getTimestamp("signed_date"));
        i.setLastModifiedBy(rs.getString("last_modified_by"));
        i.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        return i;
    }
}
