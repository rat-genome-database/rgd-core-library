package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.OmimQuery;
import edu.mcw.rgd.datamodel.Omim;

import java.util.List;

/**
 * @author mtutaj
 * API to manipulate OMIM table
 */
public class OmimDAO extends AbstractDAO {

    /**
     * get OMIM record given OMIM id
     * @return OMIM object
     * @throws Exception when unexpected error in spring framework occurs
     */
    public Omim getOmimByNr(String mimNr) throws Exception {

        String sql = "SELECT * FROM omim WHERE mim_number=?";
        List<Omim> rows = OmimQuery.execute(this, sql, mimNr);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public void insertOmim( Omim omim ) throws Exception {
        String sql = "INSERT INTO omim (mim_number,phenotype,created_date,status,last_modified_date) VALUES(?,?,SYSDATE,?,SYSDATE)";
        update(sql, omim.getMimNumber(), omim.getPhenotype(), omim.getStatus());
    }

    public void updateOmim( Omim omim ) throws Exception {
        String sql = "UPDATE omim SET phenotype=?,status=?,last_modified_date=SYSDATE WHERE mim_number=?";
        update(sql, omim.getPhenotype(), omim.getStatus(), omim.getMimNumber());
    }
}
