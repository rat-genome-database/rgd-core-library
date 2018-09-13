package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.SubmittedStrainAvailabilityQuery;
import edu.mcw.rgd.datamodel.models.SubmittedStrainAvailabiltiy;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by jthota on 8/12/2016.
 */
public class SubmittedStrainAvailablityDAO extends AbstractDAO {

    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getCurationDataSource();
    }

    public int insert(SubmittedStrainAvailabiltiy sa) throws Exception{

        String sql="insert into submitted_strain_availability(submitted_strain_key, availability_type) values (?,?)";
        return update(sql, sa.getSubmittedStrainKey(), sa.getAvailabilityType());
    }

    public List getAvailabilityByStrainKey(int strainKey) throws Exception{
        String sql="select * from submitted_strain_availability where submitted_strain_key=?";
        SubmittedStrainAvailabilityQuery q= new SubmittedStrainAvailabilityQuery(this.getDataSource(), sql);
        return execute(q, strainKey);
    }

    public int delete(int submitted_strain_key) throws Exception{
        String sql= "delete submitted_strain_availability where submitted_strain_key=?";
        return update(sql, submitted_strain_key);
    }
}
