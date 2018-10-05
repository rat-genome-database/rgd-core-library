package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntStringMapQuery;
import edu.mcw.rgd.dao.spring.SequenceQuery;
import edu.mcw.rgd.datamodel.Sequence;
import edu.mcw.rgd.datamodel.Sequence2;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author mtutaj
 * @since Jun 4, 2010
 * handle sequences
 */
public class SequenceDAO extends AbstractDAO {

    /**
     * create a new primer sequence associated with given rgd_id
     * @param rgdId rgd_id
     * @param seqType sequence type key
     * @param desc description
     * @param notes additional notes
     * @return unique sequence key for the new sequence
     * @throws Exception if something goes wrong
     */
    @Deprecated
    public int createSequence(int rgdId, int seqType, String desc, String notes) throws Exception {

        String sql = "insert into SEQUENCES (SEQUENCE_KEY, SEQUENCE_DESC, NOTES, RGD_ID, SEQUENCE_TYPE_KEY) VALUES(?,?,?,?,?)";

        int key = this.getNextKey("SEQUENCES","SEQUENCE_KEY");
        update(sql, key, desc, notes, rgdId, seqType);
        return key;
    }

    /**
     * inserts a sequence for object identified by rgd id;
     * if the sequence data is already in database, it is reused
     * @return unique sequence key
     * @throws Exception if something goes wrong
     */
    public int insertSequence(Sequence2 seq) throws Exception {

        // see if the data to be inserted is already there (reuse seq data, if possible)
        String md5 = seq.getSeqMD5();
        if( Utils.isStringEmpty(md5) ) {
            md5 = Utils.generateMD5(seq.getSeqData());
        }

        String sql = "SELECT MAX(1) FROM seq_data WHERE data_md5=?";
        boolean seqIsInDb = getCount(sql, md5) > 0;
        if( !seqIsInDb ) {
            sql = "INSERT INTO seq_data (data_md5,seq_data) VALUES(?,?)";
            update(sql, md5, seq.getSeqData());
        }
        seq.setSeqMD5(md5);

        // insert the sequence itself
        int key = getNextKeyFromSequence("RGD_SEQUENCES_SEQ");
        seq.setSeqKey(key);
        sql = "INSERT INTO rgd_sequences (seq_key,rgd_id,seq_type,seq_data_md5) VALUES(?,?,?,?)";
        update(sql, key, seq.getRgdId(), seq.getSeqType(), md5);
        return key;
    }

    /**
     * return list of sequences for given rgd object
     * @param rgdId object rgd id, like SSLP rgd id
     * @return List of Sequence2 objects
     * @throws Exception if something goes wrong
     */
    public List<Sequence2> getObjectSequences2(int rgdId) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.*,d.seq_data FROM rgd_sequences s,seq_data d "+
                "WHERE s.rgd_id=? AND s.seq_data_md5=d.data_md5";
        return SequenceQuery.execute(this, query, rgdId);
    }

    /**
     * return list of sequences of given type for given rgd object
     * @param rgdId object rgd id, like SSLP rgd id
     * @return List of Sequence2 objects
     * @throws Exception if something goes wrong
     */
    public List<Sequence2> getObjectSequences2(int rgdId, String seqType) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.*,d.seq_data FROM rgd_sequences s,seq_data d "+
                "WHERE s.rgd_id=? AND s.seq_type=? AND s.seq_data_md5=d.data_md5";
        return SequenceQuery.execute(this, query, rgdId, seqType);
    }

    public List<IntStringMapQuery.MapPair> getMD5ForObjectSequences(int objectKey, int speciesTypeKey, String seqType) throws Exception {

        String query = "SELECT s.rgd_id,s.seq_data_md5 FROM rgd_sequences s,rgd_ids r "+
                "WHERE s.rgd_id=r.rgd_id AND r.object_key=? AND r.species_type_key=? AND s.seq_type=?";
        return IntStringMapQuery.execute(this, query, objectKey, speciesTypeKey, seqType);
    }

    public int deleteSequence(Sequence2 seq) throws Exception {
        String sql = "DELETE FROM rgd_sequences WHERE seq_key=? AND rgd_id=?";
        return update(sql, seq.getSeqKey(), seq.getRgdId());
    }

    /**
     * bind a sequence object to another rgd object, like SSLP object
     * @param seqKey unique sequence key
     * @param seqType sequence type key
     * @param rgdId rgd id of object this sequence object will be bound to
     * @throws Exception if something goes wrong
     */
    @Deprecated
    public void bindSequence(int seqKey, int seqType, int rgdId) throws Exception {

        String sql = "insert into RGD_SEQ_RGD_ID (SEQ_REL_TYPE, RGD_ID, SEQUENCE_KEY) select ?,?,? from dual "+
                "where not exists (select SEQ_REL_TYPE, RGD_ID, SEQUENCE_KEY from RGD_SEQ_RGD_ID" +
                                    " where SEQ_REL_TYPE=? and RGD_ID=? and SEQUENCE_KEY=?) ";
        SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQ_REL_TYPE
        su.declareParameter(new SqlParameter(Types.INTEGER)); // RGD_ID
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQUENCE_KEY
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQ_REL_TYPE
        su.declareParameter(new SqlParameter(Types.INTEGER)); // RGD_ID
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQUENCE_KEY

        su.compile();
        su.update(seqType, rgdId, seqKey, seqType, rgdId, seqKey);
    }
    
    /**
     * unbind a sequence object from another rgd object, like SSLP object
     * @param seqKey unique sequence key
     * @param seqType sequence type key
     * @param rgdId rgd id of object this sequence object will be unbound from
     * @throws Exception if something goes wrong
     * @return count of rows affected
     */
    @Deprecated
    public int unbindSequence(int seqKey, int seqType, int rgdId) throws Exception {

        String sql = "DELETE FROM RGD_SEQ_RGD_ID "+
                "WHERE SEQ_REL_TYPE=? AND RGD_ID=? AND SEQUENCE_KEY=?";
        SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQ_REL_TYPE
        su.declareParameter(new SqlParameter(Types.INTEGER)); // RGD_ID
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQUENCE_KEY

        su.compile();
        return su.update(seqType, rgdId, seqKey);
    }


     /**
     * insert new primer pair; return new primer key
     * @param primerName primer name
     * @param primerDesc primer description
     * @param forwardSeq primer forward sequence
     * @param reverseSeq primer reverse sequence
     * @param notes additional notes
     * @param seqKey unique key of sequence associated with this primer pair
     * @return new primer pair key
     * @throws Exception if something goes wrong
     */
    public int insertPrimerPairs(String primerName, String primerDesc, String forwardSeq, String reverseSeq, String notes, int seqKey) throws Exception {

        String sql = "INSERT INTO seq_primer_pairs (primer_key, primer_name, primer_desc, forward_seq, reverse_seq, "+
            "notes, sequence_key, expected_size) VALUES(?,?,?,?,?,?,?,NULL)";
        SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

        su.declareParameter(new SqlParameter(Types.INTEGER)); // PRIMER_KEY
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // PRIMER_NAME
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // PRIMER_DESC
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // FORWARD_SEQ
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // REVERSE_SEQ
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // NOTES
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQUENCE_KEY

        su.compile();
        int key = this.getNextKey("SEQ_PRIMER_PAIRS", "PRIMER_KEY");
        su.update(key, primerName, primerDesc, forwardSeq, reverseSeq, notes, seqKey);
        return key;
    }

     /**
     * update a sequence primer pair
     * @param seq sequence object to be updated
     * @return count of rows affected
     * @throws Exception if something goes wrong
     */
    public int updatePrimerPairs(Sequence seq) throws Exception {

        String sql = "UPDATE seq_primer_pairs SET primer_name=?, primer_desc=?, notes=?, expected_size=?, " +
                "forward_seq=?, reverse_seq=?, sequence_key=? WHERE primer_key=?";
        SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

        su.declareParameter(new SqlParameter(Types.VARCHAR)); // PRIMER_NAME
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // PRIMER_DESC
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // NOTES
        su.declareParameter(new SqlParameter(Types.INTEGER)); // EXPECTED_SIZE
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // FORWARD_SEQ
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // REVERSE_SEQ
        su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQUENCE_KEY
        su.declareParameter(new SqlParameter(Types.INTEGER)); // PRIMER_KEY

        su.compile();
        return su.update(seq.getPrimerName(), seq.getPrimerDesc(), seq.getNotes(), seq.getExpectedSize(),
                seq.getForwardSeq(), seq.getReverseSeq(), seq.getSeqKey(), seq.getPrimerKey());
    }

    /**
     * return list of all primer-pair sequences and seq clones for given rgd object
     * @param rgdId object rgd id, like SSLP rgd id
     * @return List of Sequence objects
     * @throws Exception if something goes wrong
     */
    @Deprecated
    public List<Sequence> getObjectSequences(int rgdId) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.sequence_key,s.sequence_desc,s.notes,s.rgd_id,s.sequence_type_key,pp.forward_seq,pp.reverse_seq,c.sequence,pp.primer_key "+
            "FROM sequences s,seq_primer_pairs pp,seq_clones c,rgd_seq_rgd_id r "+
            "WHERE r.rgd_id=? AND r.sequence_key=s.sequence_key "+
                "AND s.sequence_key=c.sequence_key(+) "+
                "AND s.sequence_key=pp.sequence_key(+)";

        JdbcTemplate jt = new JdbcTemplate(this.getDataSource());
        List<Sequence> rows = jt.query(query, new Object[]{rgdId}, new int[]{Types.INTEGER}, new RowMapper(){
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                Sequence seq = new Sequence();
                seq.setSeqKey(rs.getInt(1));
                seq.setSeqDesc(rs.getString(2));
                seq.setNotes(rs.getString(3));
                seq.setRgdId(rs.getInt(4));
                seq.setSeqTypeKey(rs.getInt(5));
                seq.setForwardSeq(rs.getString(6));
                seq.setReverseSeq(rs.getString(7));
                seq.setCloneSeq(rs.getString(8));
                seq.setPrimerKey(rs.getInt(9));
                return seq;
            }
        });
        return rows;
    }

    /**
     * return list of all primer-pair sequences and seq clones for given rgd object
     * @param rgdId object rgd id, like SSLP rgd id
     * @param seqTypeKey sequence type key, f.e. 12 for RefSeq protein
     * @return List of Sequence objects
     * @throws Exception if something goes wrong
     */
    @Deprecated
    public List<Sequence> getObjectSequences(int rgdId, int seqTypeKey) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.sequence_key,s.sequence_desc,s.notes,s.rgd_id,s.sequence_type_key,pp.forward_seq,pp.reverse_seq,c.sequence,pp.primer_key "+
            "FROM sequences s,seq_primer_pairs pp,seq_clones c,rgd_seq_rgd_id r "+
            "WHERE r.rgd_id=? AND r.sequence_key=s.sequence_key "+
                "AND s.sequence_key=c.sequence_key(+) "+
                "AND s.sequence_key=pp.sequence_key(+)";

        JdbcTemplate jt = new JdbcTemplate(this.getDataSource());
        List<Sequence> rows = jt.query(query, new Object[]{rgdId}, new int[]{Types.INTEGER}, new RowMapper(){
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                Sequence seq = new Sequence();
                seq.setSeqKey(rs.getInt(1));
                seq.setSeqDesc(rs.getString(2));
                seq.setNotes(rs.getString(3));
                seq.setRgdId(rs.getInt(4));
                seq.setSeqTypeKey(rs.getInt(5));
                seq.setForwardSeq(rs.getString(6));
                seq.setReverseSeq(rs.getString(7));
                seq.setCloneSeq(rs.getString(8));
                seq.setPrimerKey(rs.getInt(9));
                return seq;
            }
        });
        return rows;
    }

    /**
    * insert seq clone
    * @param seqClone sequence clone
    * @param seqKey sequence key
    * @param cloneName name of the clone
    * @return count of rows affected
    * @throws Exception if something goes wrong
    */
    @Deprecated
   public int insertSeqClone(int seqKey, String cloneName, String seqClone) throws Exception {

       String sql = "INSERT INTO seq_clones (clone_key,clone_name,sequence_key,sequence) " +
               "VALUES(?,?,?,?)";
       SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

       su.declareParameter(new SqlParameter(Types.INTEGER)); // clone_key
       su.declareParameter(new SqlParameter(Types.VARCHAR)); // clone_name
       su.declareParameter(new SqlParameter(Types.INTEGER)); // seq_key
       su.declareParameter(new SqlParameter(Types.VARCHAR)); // sequence

       su.compile();
       return su.update(this.getNextKey("SEQ_CLONES", "CLONE_KEY"), cloneName, seqKey, seqClone);
   }

   /**
    * update seq clone for object owning the sequence
    * @param seqClone sequence clone
    * @param objRgdId rgd id for object owning the sequence
    * @return count of rows affected
    * @throws Exception if something goes wrong
    */
   @SuppressWarnings("unused")
   @Deprecated
   public int updateSeqClone(int objRgdId, String seqClone) throws Exception {

       String sql = "UPDATE seq_clones c SET sequence=? " +
               "WHERE EXISTS (SELECT 1 FROM rgd_seq_rgd_id r WHERE c.sequence_key=r.sequence_key AND r.rgd_id=?)";
       SqlUpdate su = new SqlUpdate(this.getDataSource(), sql);

       su.declareParameter(new SqlParameter(Types.VARCHAR)); // SEQ_CLONE
       su.declareParameter(new SqlParameter(Types.INTEGER)); // SEQ RGD ID

       su.compile();
       return su.update(seqClone, objRgdId);
   }

    /**
     * update sequence data for a sequence
     * @param seqData new data for sequence
     * @param seqKey sequence key
     * @return count of rows affected
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unused")
    @Deprecated
    public int updateSeqCloneByKey(int seqKey, String seqData) throws Exception {

        String sql = "UPDATE seq_clones SET sequence=? WHERE sequence_key=?";
        return update(seqData, seqKey);
    }
}
