package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.MiRnaTarget;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 5/4/15
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MiRnaTargetQuery extends MappingSqlQuery {

    public MiRnaTargetQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        MiRnaTarget obj = new MiRnaTarget();
        obj.setKey(rs.getInt("mirna_target_key"));
        obj.setGeneRgdId(rs.getInt("gene_rgd_id"));
        obj.setMiRnaRgdId(rs.getInt("mirna_rgd_id"));

        obj.setTargetType(rs.getString("target_type"));
        obj.setMiRnaSymbol(rs.getString("mirna_symbol"));
        obj.setMethodName(rs.getString("method_name"));
        obj.setResultType(rs.getString("result_type"));
        obj.setDataType(rs.getString("data_type"));
        obj.setSupportType(rs.getString("support_type"));
        obj.setPmid(rs.getString("pmid"));

        obj.setCreatedDate(rs.getDate("created_date"));
        obj.setModifiedDate(rs.getDate("modified_date"));

        obj.setTranscriptAcc(rs.getString("transcript_acc"));
        obj.setTranscriptBioType(rs.getString("transcript_biotype"));
        obj.setIsoform(rs.getString("isoform"));
        obj.setAmplification(rs.getString("amplification"));
        obj.setTargetSite(rs.getString("target_site"));

        obj.setUtrStart(rs.getInt("utr_start"));
        if( rs.wasNull() )
            obj.setUtrStart(null);
        obj.setUtrEnd(rs.getInt("utr_end"));
        if( rs.wasNull() )
            obj.setUtrEnd(null);
        obj.setScore(rs.getDouble("score"));
        if( rs.wasNull() )
            obj.setScore(null);
        obj.setNormalizedScore(rs.getDouble("normalized_score"));
        if( rs.wasNull() )
            obj.setNormalizedScore(null);
        obj.setEnergy(rs.getDouble("energy"));
        if( rs.wasNull() )
            obj.setEnergy(null);
        return obj;
    }
}
