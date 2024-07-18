package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StringListQuery;

public class HrdpPortalAvailabilityDAO extends AbstractDAO {

    public String getAvailableStrainByPrimaryStrainId(int id, String subGroupName) throws Exception {
       String sql = """
               Select available_strain_id from hrdp_portal_availability
               where primary_strain_id=? and sub_group_name=?
               """;
       return StringListQuery.execute(this,sql,id,subGroupName).size()>0?StringListQuery.execute(this,sql,id,subGroupName).get(0):null;
    }
}
