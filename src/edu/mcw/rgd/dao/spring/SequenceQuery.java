package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.datamodel.Sequence2;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author mtutaj
 * @since Aug 1, 2018
 * returns sequences for RGD objects
 */
public class SequenceQuery extends MappingSqlQuery {

    public SequenceQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Sequence2 obj = new Sequence2();

        obj.setSeqKey(rs.getInt("SEQ_KEY"));
        obj.setRgdId(rs.getInt("RGD_ID"));
        obj.setSeqType(rs.getString("SEQ_TYPE"));
        obj.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
        obj.setSeqMD5(rs.getString("SEQ_DATA_MD5"));
        obj.setSeqData(rs.getString("SEQ_DATA"));

        return obj;
    }

    public static List<Sequence2> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        SequenceQuery q = new SequenceQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
