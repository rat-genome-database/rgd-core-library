package edu.mcw.rgd.dao.spring;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcw.rgd.datamodel.Alias;
import edu.mcw.rgd.datamodel.Note;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jan 17, 2008
 * Time: 10:08:19 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Returns a row from the Alias table
 */
public class NotesQuery extends MappingSqlQuery {

    public NotesQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();
        note.setKey(rs.getInt("note_key"));
        note.setCreationDate(rs.getDate("creation_date"));
        note.setNotes(rs.getString("notes"));
        note.setNotesTypeName(rs.getString("notes_type_name_lc"));
        note.setPublicYN(rs.getString("public_y_n"));
        note.setRgdId(rs.getInt("rgd_id"));
        note.setRefId(rs.getInt("ref_rgd_id"));

        if (rs.wasNull()) {
            note.setRefId(null);
        }

        return note;
    }
}