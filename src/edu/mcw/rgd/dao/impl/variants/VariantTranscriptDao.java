package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.variants.VariantTranscriptQuery;
import edu.mcw.rgd.datamodel.variants.VariantTranscript;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.List;

public class VariantTranscriptDao extends AbstractDAO {
    public List<edu.mcw.rgd.datamodel.variants.VariantTranscript> getVariantTranscripts(long rgdId, int mapKey) throws Exception {
        String sql=" select t.*, p.prediction from variant_transcript t left outer join " +
                "                polyphen p on (t.variant_rgd_id=p.variant_rgd_id and t.transcript_rgd_id=p.transcript_rgd_id)\n" +
                " inner join transcripts tr on tr.transcript_rgd_id=t.transcript_rgd_id" +
                "                where t.variant_rgd_id=? " +
                "                and t.map_key=?";
        VariantTranscriptQuery q=new VariantTranscriptQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.declareParameter(new SqlParameter(Types.INTEGER));

        return q.execute(rgdId, mapKey);
    }
}
