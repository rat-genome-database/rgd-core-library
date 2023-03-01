package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.InteractionAttributeQuery;
import edu.mcw.rgd.datamodel.Interaction;
import edu.mcw.rgd.datamodel.InteractionAttribute;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jthota on 3/14/2016.
 */
public class InteractionAttributesDAO extends AbstractDAO {

    public int insert(InteractionAttribute a) throws Exception{
        String sql = "insert into interaction_attributes "+
                "(attribute_key, interaction_key, attribute_name, attribute_value, created_date, last_modified_date) "+
                "select ?,?,?,?,SYSDATE, SYSDATE from dual "+
                "where not exists (select interaction_key, attribute_name, attribute_value from interaction_attributes "+
                                  "where  interaction_key=? and attribute_name=? and attribute_value=?) "+
                "and exists (select interaction_key from interactions where interaction_key=?)";
        return update(sql, a.getAttributeKey(), a.getInteractionKey(), a.getAttributeName(), a.getAttributeValue(),
                a.getInteractionKey(), a.getAttributeName(), a.getAttributeValue(), a.getInteractionKey());
    }

    public int updateLastModifiedDate(int attributeKey) throws Exception{
        String sql = "UPDATE interaction_attributes SET last_modified_date=SYSDATE WHERE attribute_key=?";
        return update(sql, attributeKey);
    }

    public int updateLastModifiedDate(List<Integer> attributeKeys) throws Exception{
        String sql = "UPDATE interaction_attributes SET last_modified_date=SYSDATE WHERE attribute_key=?";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql, new int[]{Types.INTEGER});
        su.compile();

        for( Integer attributeKey: attributeKeys ) {
            su.update(attributeKey);
        }

        return executeBatch(su);
    }

    public int updateAttributes(Interaction pi) throws Exception{
        List<InteractionAttribute> aList = pi.getInteractionAttributes();
        int key = pi.getInteractionKey();
        List<Integer> aKeys = new ArrayList<>(aList.size());
        int newRecordCount=0;
        for(InteractionAttribute a: aList){
            a.setInteractionKey(key);
            int aKey = this.getAttributeKey(a);
            if(aKey!=0){
                aKeys.add(aKey);
                continue;
            }
            aKey = this.getNextKey("interactionAttributes_seq");
            a.setAttributeKey(aKey);
            newRecordCount += this.insert(a);
        }
        updateLastModifiedDate(aKeys);
        return newRecordCount;
    }

    public int getAttributeKey(InteractionAttribute a) throws Exception{
        String sql = "SELECT attribute_key FROM interaction_attributes WHERE interaction_key=? AND attribute_name=? AND attribute_value=?";
        List<Integer> keys = IntListQuery.execute(this, sql, a.getInteractionKey(), a.getAttributeName(), a.getAttributeValue());
        return keys.isEmpty() ? 0 : keys.get(0);
    }

    public List<InteractionAttribute> getUnmodifiedAttributes(Date cutoffDate) throws Exception{
        String sql = "SELECT * FROM interaction_attributes WHERE last_modified_date < ?";
        InteractionAttributeQuery q = new InteractionAttributeQuery(this.getDataSource(), sql);
        return execute(q, cutoffDate);
    }

    public int deleteUnmodifiedAttributes(Date cutoffDate) throws Exception{
        String sql = "DELETE interaction_attributes WHERE last_modified_date < ?";
        return update(sql, cutoffDate);
    }

    public int getAttributeCount() throws Exception{
        String sql = "SELECT COUNT(*) FROM interaction_attributes";
        return getCount(sql);
    }

    public List<InteractionAttribute> getAttributes(int interaction_key) throws Exception{
        String sql = "select * from interaction_attributes where interaction_key=?";
        InteractionAttributeQuery q = new InteractionAttributeQuery(this.getDataSource(), sql);
        return execute(q, interaction_key);
    }

    public List<InteractionAttribute> getAttributes(List<Integer> interactionKeyList) throws Exception{
        String sql="select * from interaction_attributes where interaction_key in ("+
                Utils.buildInPhrase(interactionKeyList)+")";
        InteractionAttributeQuery q= new InteractionAttributeQuery(this.getDataSource(), sql);
        return execute(q);
    }
}
