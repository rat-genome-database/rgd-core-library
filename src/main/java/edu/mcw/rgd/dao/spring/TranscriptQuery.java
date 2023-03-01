package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Transcript;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Jun 18, 2010
 * Time: 4:33:09 PM
 */
public class TranscriptQuery extends MappingSqlQuery {

    public TranscriptQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transcript tr = new Transcript();

        tr.setRgdId(rs.getInt("transcript_rgd_id"));
        tr.setAccId(rs.getString("acc_id"));
        tr.setGeneRgdId(rs.getInt("gene_rgd_id"));
        tr.setDateCreated(rs.getTimestamp("created_date"));
        tr.setNonCoding(rs.getString("is_non_coding_ind").equals("Y"));
        tr.setRefSeqStatus(rs.getString("refseq_status"));
        tr.setProteinAccId(rs.getString("protein_acc_id"));
        tr.setPeptideLabel(rs.getString("peptide_label"));
        tr.setType(rs.getString("biotype"));
        return tr;
    }

}
