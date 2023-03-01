package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.InteractionCountQuery;
import edu.mcw.rgd.datamodel.InteractionCount;
import edu.mcw.rgd.process.Utils;

import java.util.List;

/**
 * Created by jthota on 6/9/2016.
 */
public class InteractionCountsDAO extends AbstractDAO{

    public int insert(InteractionCount count) throws Exception{
        String sql = "INSERT INTO interaction_counts (rgd_id, interactions_count, created_date, last_modified_date) "+
                "select ?,?, SYSDATE, SYSDATE from dual  where not exists (select  rgd_id from interaction_counts where rgd_id=?)";
        return update(sql, count.getRgdId(), count.getCount(), count.getRgdId());
    }

    public int update(InteractionCount count) throws Exception{
        String sql= "UPDATE interaction_counts SET interactions_count=?, last_modified_date=SYSDATE WHERE rgd_id=? AND interactions_count<>?";
        return update(sql, count.getCount(), count.getRgdId(), count.getCount());
    }

    public List<InteractionCount> getInteractionCountsOfRgdIds(List<Integer> rgdIdsList) throws Exception{
        String sql= "SELECT * FROM interaction_counts WHERE rgd_id IN (" + Utils.concatenate(rgdIdsList,",")+")";
        InteractionCountQuery q= new InteractionCountQuery(this.getDataSource(), sql);
        return execute(q);
    }

    public InteractionCount getInteractionCount(int rgdid) throws Exception{
        String sql= "SELECT * FROM interaction_counts WHERE rgd_id=?";
        InteractionCountQuery q= new InteractionCountQuery(this.getDataSource(), sql);
        List<InteractionCount> counts=execute(q, rgdid);
        return counts.size()>0 ? counts.get(0) : null;
    }
}
