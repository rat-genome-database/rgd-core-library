package edu.mcw.rgd.dao.impl.variants;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.variants.VariantTranscriptQuery;
import edu.mcw.rgd.datamodel.variants.VariantTranscript;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.*;

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

    /**
     * transcripts (with their PolyPhen predictions) for many variants on one map, in one query per 1000 variants
     * @return lists keyed by variant rgd id; variants without transcripts have no entry
     */
    public Map<Long, List<VariantTranscript>> getVariantTranscripts(Collection<Long> rgdIds, int mapKey) throws Exception {
        Map<Long, List<VariantTranscript>> results = new HashMap<>();
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(rgdIds));
        for( int i=0; i<ids.size(); i+=1000 ) {
            List<Long> chunk = ids.subList(i, Math.min(i+1000, ids.size()));
            String sql=" select t.*, p.prediction from variant_transcript t left outer join " +
                    "                polyphen p on (t.variant_rgd_id=p.variant_rgd_id and t.transcript_rgd_id=p.transcript_rgd_id)\n" +
                    " inner join transcripts tr on tr.transcript_rgd_id=t.transcript_rgd_id" +
                    "                where t.variant_rgd_id in (" + Utils.concatenate(chunk, ",") + ")" +
                    "                and t.map_key=?";
            VariantTranscriptQuery q=new VariantTranscriptQuery(DataSourceFactory.getInstance().getCarpeNovoDataSource(), sql);
            q.declareParameter(new SqlParameter(Types.INTEGER));
            for( VariantTranscript t: q.execute(mapKey) ) {
                results.computeIfAbsent(t.getVariantId(), k -> new ArrayList<>()).add(t);
            }
        }
        return results;
    }
}
