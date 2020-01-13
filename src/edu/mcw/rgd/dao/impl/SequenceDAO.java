package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntStringMapQuery;
import edu.mcw.rgd.dao.spring.SequenceQuery;
import edu.mcw.rgd.datamodel.Sequence;
import edu.mcw.rgd.process.Utils;

import java.util.List;

/**
 * @author mtutaj
 * @since Jun 4, 2010
 * handle sequences
 */
public class SequenceDAO extends AbstractDAO {

    /**
     * update a sequence identified by seq_key
     * @return count of rows affected
     * @throws Exception if something goes wrong
     */
    public int updateSequence(Sequence seq) throws Exception {

        // see if the data to be inserted is already there (reuse seq data, if possible)
        if( Utils.isStringEmpty(seq.getSeqMD5()) ) {
            seq.setSeqMD5( Utils.generateMD5(seq.getSeqData()) );
        }

        // insert the sequence itself
        String sql = "UPDATE rgd_sequences SET rgd_id=?,seq_type=?,seq_data_md5=? WHERE seq_key=?";
        return update(sql, seq.getRgdId(), seq.getSeqType(), seq.getSeqMD5(), seq.getSeqKey());
    }

    /**
     * inserts a sequence for object identified by rgd id;
     * if the sequence data is already in database, it is reused
     * @return unique sequence key
     * @throws Exception if something goes wrong
     */
    public int insertSequence(Sequence seq) throws Exception {

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
     * @return List of Sequence objects
     * @throws Exception if something goes wrong
     */
    public List<Sequence> getObjectSequences(int rgdId) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.*,d.seq_data FROM rgd_sequences s,seq_data d "+
                "WHERE s.rgd_id=? AND s.seq_data_md5=d.data_md5";
        return SequenceQuery.execute(this, query, rgdId);
    }

    /**
     * return list of sequences of given type for given rgd object
     * @param rgdId object rgd id, like SSLP rgd id
     * @return List of Sequence objects
     * @throws Exception if something goes wrong
     */
    public List<Sequence> getObjectSequences(int rgdId, String seqType) throws Exception {

        // retrieve all sequences for given rgd object
        String query = "SELECT s.*,d.seq_data FROM rgd_sequences s,seq_data d "+
                "WHERE s.rgd_id=? AND s.seq_type=? AND s.seq_data_md5=d.data_md5";
        return SequenceQuery.execute(this, query, rgdId, seqType);
    }

    public List<IntStringMapQuery.MapPair> getMD5ForObjectSequences(int objectKey, String seqType) throws Exception {

        String query = "SELECT s.rgd_id,s.seq_data_md5 FROM rgd_sequences s,rgd_ids r "+
                "WHERE s.rgd_id=r.rgd_id AND r.object_key=? AND s.seq_type=?";
        return IntStringMapQuery.execute(this, query, objectKey, seqType);
    }

    public List<IntStringMapQuery.MapPair> getMD5ForObjectSequences(int objectKey, int speciesTypeKey, String seqType) throws Exception {

        String query = "SELECT s.rgd_id,s.seq_data_md5 FROM rgd_sequences s,rgd_ids r "+
                "WHERE s.rgd_id=r.rgd_id AND r.object_key=? AND r.species_type_key=? AND s.seq_type=?";
        return IntStringMapQuery.execute(this, query, objectKey, speciesTypeKey, seqType);
    }

    public int deleteSequence(Sequence seq) throws Exception {
        String sql = "DELETE FROM rgd_sequences WHERE seq_key=? AND rgd_id=?";
        return update(sql, seq.getSeqKey(), seq.getRgdId());
    }

}
