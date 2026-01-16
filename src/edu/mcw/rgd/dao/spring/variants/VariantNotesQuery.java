package edu.mcw.rgd.dao.spring.variants;

import edu.mcw.rgd.datamodel.variants.VariantNotes;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantNotesQuery extends MappingSqlQuery {
    public VariantNotesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        VariantNotes obj = new VariantNotes();
        obj.setRgdId(rs.getInt("RGD_ID"));
        obj.setNotes(rs.getString("NOTES"));
        return obj;
    }
}
