package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.HistogramRecord;
import edu.mcw.rgd.datamodel.pheno.HeatMapRecord;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: WLiu
 * Date: 8/2/12
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class HeatMapQuery extends MappingSqlQuery {
    public HeatMapQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        HeatMapRecord hmRecord = new HeatMapRecord();
        hmRecord.rowTerm = rs.getString(1);
        hmRecord.colTerm = rs.getString(2);
        hmRecord.value = rs.getDouble(3);
        if (rs.wasNull()) hmRecord.value = null;
        return hmRecord;
    }

}
