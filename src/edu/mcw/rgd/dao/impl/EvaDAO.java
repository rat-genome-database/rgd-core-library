package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.EvaQuery;
import edu.mcw.rgd.datamodel.Eva;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.List;

/**
 * Created by llamers on 1/23/2020.
 */
public class EvaDAO extends AbstractDAO{
    public EvaDAO(){}

    public List<Eva> getEvaObjectsFromMapKey(int mapKey) throws Exception {
        String query  = "SELECT * FROM eva where map_key=?";
        Eva test = new Eva();
        return EvaQuery.execute(this, query, mapKey);
    }
    public List<Eva> getEvaObjectsFromMapKeyAndChromosome(int mapKey, String chromosome) throws Exception {
        String query  = "SELECT * FROM eva where map_key=? AND chromosome=?";
        Eva test = new Eva();
        return EvaQuery.execute(this, query, mapKey, chromosome);
    }
    public int deleteEva(int EvaKey) throws Exception{
        String sql = "DELETE FROM EVA WHERE EVA_ID=?";
        return update(sql, EvaKey);
    }
    public int deleteEvaBatch(Collection<Eva> tobeDeleted) throws Exception {
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),"DELETE FROM EVA WHERE EVA_ID=?",
                new int[] {Types.INTEGER});

        for(Eva eva : tobeDeleted)
            su.update(eva.getEvaId());

        return executeBatch(su);
    }
    public int insertEva(Collection<Eva> tobeInserted) throws Exception {
        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), "INSERT INTO EVA (EVA_ID, CHROMOSOME, POS, RS_ID, " +
                "REF_NUC, VAR_NUC, SO_TERM_ACC, MAP_KEY, PADDING_BASE) values (?,?,?,?,?,?,?,?,?)",
                new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.INTEGER, Types.VARCHAR});
        su.compile();

        for( Eva eva: tobeInserted ) {
            int evaId = this.getNextKeyFromSequence("EVA_SEQ");
            eva.setEvaid(evaId);

            su.update(eva.getEvaId(), eva.getChromosome(), eva.getPos(), eva.getRsId(),eva.getRefNuc(),
                    eva.getVarNuc(), eva.getSoTerm(), eva.getMapkey(), eva.getPadBase());
        }

        return executeBatch(su);
    }
    public List<Eva> getEvaByRsId(String rsId, int mapKey) throws Exception{
        String sql = "select * from EVA where rs_id=? and map_key=?";
        return EvaQuery.execute(this,sql,rsId,mapKey);
    }
}
