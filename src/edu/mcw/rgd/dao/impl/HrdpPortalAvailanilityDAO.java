package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;

public class HrdpPortalAvailanilityDAO extends AbstractDAO {

    public int getAvailableStrainByPrimaryStrainId(int id) throws Exception {
       String sql = """
               Select available_strain_id from hrdp_portal_availability
               where primary_strain_id=?
               """;
       return IntListQuery.execute(this,sql,id).get(0);
    }
}
