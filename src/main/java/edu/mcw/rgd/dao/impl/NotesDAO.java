package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.NotesQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Note;
import edu.mcw.rgd.datamodel.Reference;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Types;

import org.springframework.jdbc.core.SqlParameter;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
 * <p>
 * Represents a row in NOTES table.
 */
public class NotesDAO extends AbstractDAO {


    public Note getNoteByKey(int key) throws Exception {
        String query = "select n.*, nri.ref_key, r.rgd_id as ref_rgd_id from notes n, note_ref_id nri , references r where n.note_key = nri.note_key(+) and n.note_key=" + key + " and nri.ref_key = r.ref_key(+)";

        //String query = "select n.*, nri.ref_key from notes n, note_ref_id nri where n.note_key = nri.note_key(+) and n.note_key=" + key;
        List<Note> notes = new ArrayList<Note>();
        NotesQuery gq = new NotesQuery(this.getDataSource(), query);
        gq.compile();
        notes = gq.execute();

        if (notes.size() == 0 ) {
            throw new Exception("Note key " + key + " not found");
        }
        return notes.get(0);
    }

    public List<Note> getNotes(int rgdId) throws Exception {
        String query = "SELECT n.*, nri.ref_key, r.rgd_id as ref_rgd_id "
            +"FROM notes n, note_ref_id nri, references r "
            +"WHERE n.note_key = nri.note_key(+) and nri.ref_key=r.ref_key(+) and n.rgd_id=?";
        NotesQuery gq = new NotesQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.compile();
        return gq.execute(new Object[]{rgdId});
    }

    public List<Note> getNotes(int rgdId, String noteTypeName) throws Exception {
        String query = "SELECT n.*, nri.ref_key, r.rgd_id as ref_rgd_id "
            +"FROM notes n, note_ref_id nri, references r "
            +"WHERE n.note_key = nri.note_key(+) and nri.ref_key=r.ref_key(+) and n.rgd_id=? and n.notes_type_name_lc=?";
        NotesQuery gq = new NotesQuery(this.getDataSource(), query);
        gq.declareParameter(new SqlParameter(Types.INTEGER));
        gq.declareParameter(new SqlParameter(Types.VARCHAR));
        gq.compile();
        return gq.execute(new Object[]{rgdId, noteTypeName.toLowerCase()});
    }



    /**
     * update or insert a note; if note key is 0 or negative, new note will be inserted; otherwise the note will be updated
     *
     * @param note
     * @throws Exception
     */
    public void updateNote(Note note) throws Exception{

        String sql;
        if (note.getKey() <=0) {
            sql = "insert into notes (notes, notes_type_name_lc, public_y_n, rgd_id,creation_date ,note_key) " +
                                                               " values (?,?,?,?,?,?)";
            note.setKey(this.getNextKey("notes","note_key"));
            note.setCreationDate(new Date());
        }else {
            sql = "update notes set notes=?, notes_type_name_lc=?, " +
                "public_y_n=?, rgd_id=?, creation_date=? where note_key=?";
        }
        
        update(sql, note.getNotes(), note.getNotesTypeName(), note.getPublicYN(), note.getRgdId(),
                new java.sql.Date(note.getCreationDate().getTime()), note.getKey());

        this.updateNoteReference(note);
    }
    
    public void deleteNote(int noteKey) throws Exception {
       this.deleteNoteReference(noteKey);

       String sql = "delete from notes where note_key=?";
       update(sql, noteKey);
    }

    public void deleteNoteReference(int noteKey) throws Exception {
        String sql = "delete from note_ref_id where note_key=?";
        update(sql, noteKey);
    }


    public List getNotesTypes(String objectType) throws Exception{

        String sql = "SELECT notes_type_name_lc FROM note_types WHERE note_object=?";
        return StringListQuery.execute(this, sql, objectType);
    }

    private void updateNoteReference(Note note) throws Exception {

        this.deleteNoteReference(note.getKey());

        if (note.getRefId() == null) {
            return;
        }

        Reference r = new ReferenceDAO().getReference(note.getRefId());

        String sql = "insert into note_ref_id (note_key,ref_key) values(?,?)";
        update(sql, note.getKey(), r.getKey());
    }
}