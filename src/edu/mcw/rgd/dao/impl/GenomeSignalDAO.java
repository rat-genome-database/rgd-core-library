package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * @author jdepons
 * @since Jun 24, 2008
 */
@SuppressWarnings("unchecked")
public class GenomeSignalDAO extends AbstractDAO {

    /**
     * get all active NCBI maps for all species
     * @return list of all active maps
     * @throws Exception when unexpected error in spring framework occurs
     * @deprecated
     */
    public List<GenomeSignal> getGenomeSignal(String chromosome, int setId) throws Exception {
        String query = "select * from genome_signal where chr=? and set_id=?";

        return executeGenomeSignalQuery(query, chromosome, setId);

    }

    public List<GenomeSignal> getGenomeSignal(String chromosome, int setId, long highBound, long lowBound) throws Exception {
        String query = "select * from genome_signal where chr=? and set_id=? and position > ? and position < ?";

        return executeGenomeSignalQuery(query, chromosome, setId,lowBound,highBound);

    }

    public void insertSignal(GenomeSignal gs) throws Exception{

        String sql = "insert into GENOME_SIGNAL (chr, signal_value, position, set_id) " +
                " VALUES (LOWER(?),?,?,?)";

        update(sql, gs.getChromosome(),gs.getSignalValue(),gs.getPosition(),gs.getSetId());
    }

    /// Gene query implementation helper
    public List<GenomeSignal> executeGenomeSignalQuery(String query, Object ... params) throws Exception {
        return GenomeSignalQuery.execute(this, query, params);
    }


}
