package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;


public abstract class MappingQuery<T> extends MappingSqlQuery<T> {
    public MappingQuery(DataSource ds, String query) {
        super(ds, query);
    }

    Integer getInt(ResultSet rs, String column) throws SQLException {
        if (!hasColumn(rs, column)) {
            return null;
        }
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    Double getDouble(ResultSet rs, String column) throws SQLException {
        if (!hasColumn(rs, column)) {
            return null;
        }
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : value;
    }


    String getString(ResultSet rs, String column) throws SQLException {
        if (!hasColumn(rs, column)) {
            return "";
        }
        String value = rs.getString(column); // <-- FIXED
        return rs.wasNull() ? "" : value;
    }

    Date getTimeStamp(ResultSet rs, String column) throws SQLException {
      Date  value= rs.getTimestamp(column);
      return rs.wasNull()?new Date():value;
    }
    private boolean hasColumn(ResultSet rs, String column) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            if (md.getColumnName(i).equalsIgnoreCase(column)) {
                return true;
            }
        }
        return false;
    }
    int getIntOrZero(ResultSet rs, String column) throws SQLException {
        Integer value = getInt(rs, column);
        return value != null ? value : 0;
    }

    double getDoubleOrZero(ResultSet rs, String column) throws SQLException {
        Double value = getDouble(rs, column);
        return value != null ? value : 0.0;
    }

}
