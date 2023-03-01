package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.HistogramRecord;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 2/9/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramQuery extends MappingSqlQuery {

    public HistogramQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        HistogramRecord histRecord = new HistogramRecord();
        histRecord.value = rs.getString(1);
        histRecord.count = rs.getLong(2);
        return histRecord;
    }
}
