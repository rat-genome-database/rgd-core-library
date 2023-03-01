package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.SSLPSAlleleQuery;
import edu.mcw.rgd.datamodel.SslpsAllele;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
 */
public class SslpsAlleleDAO extends AbstractDAO {

    /**
     * get sslps_allele objects for given sslp key
     * @param sslpKey being checked for allele
     * @return list of SslpsAllele objects
     * @throws Exception thrown on database error
     */
    public List<SslpsAllele> getSslpsAlleleByKey(int sslpKey) throws Exception {
        String query = "SELECT s.strain_symbol,s.rgd_id,size1,size2,sa.notes,sa.allele_key,sa.sslp_key," +
                "sa.strain_key FROM SSLPS_ALLELES sa,strains s WHERE SA.STRAIN_KEY=s.STRAIN_KEY AND SA.SSLP_KEY=? "+
                "ORDER BY s.strain_symbol_lc";

        SSLPSAlleleQuery q = new SSLPSAlleleQuery(this.getDataSource(), query);
        return execute(q, sslpKey);
    }

    /**
     * insert new sslp allele
     * @param allele SslpsAllele object
     * @throws Exception
     */
    public void insertSslpsAllele(SslpsAllele allele) throws Exception{

        List<SslpsAllele> alleleArr = new ArrayList<SslpsAllele>(1);
        alleleArr.add(allele);
        insertAlleles(alleleArr);
    }

    /**
     * insert allele list into SSLPS_ALLELE  table
     * @param alleleList list of sslp_alleles to be inserted
     * @return count of rows affected
     * @throws Exception
     */
    public int insertAlleles(List<SslpsAllele> alleleList) throws Exception {

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO SSLPS_ALLELES (ALLELE_KEY,SIZE1,SIZE2,SSLP_KEY, STRAIN_KEY, NOTES)"+
                "SELECT SSLPS_ALLELES_SEQ.NEXTVAL,?,?,?,?,? FROM dual "+
                "WHERE NOT EXISTS (SELECT 1 FROM SSLPS_ALLELES WHERE sslp_key=? AND strain_key=?)",
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
                        Types.INTEGER, Types.INTEGER});
        su.compile();

        for( SslpsAllele allele: alleleList ) {
            Integer size1 = allele.getSize1()>0 ? allele.getSize1() : null;
            Integer size2 = allele.getSize2()>0 ? allele.getSize2() : null;
            su.update(size1, size2, allele.getSslpKey(), allele.getStrainKey(), allele.getNotes(),
                allele.getSslpKey(), allele.getStrainKey());
        }

        return executeBatch(su);
    }

    /**
     * Update allele based on allele_key
     *
     * @param allele SslpsAllele object to be updated
     * @throws Exception
     */
    public void updateAllele(SslpsAllele allele) throws Exception{

        String sql = "UPDATE sslps_alleles SET SIZE1=?, SIZE2=?, NOTES=? WHERE allele_key=?";

        Integer size1 = allele.getSize1()>0 ? allele.getSize1() : null;
        Integer size2 = allele.getSize2()>0 ? allele.getSize2() : null;
        update(sql, size1, size2, allele.getNotes(), allele.getKey());
    }

    /**
     * Delete allele based on allele_key
     *
     * @param allele SslpsAllele object to be deleted
     * @throws Exception
     */
    public void deleteAllele(SslpsAllele allele) throws Exception{

        String sql = "DELETE FROM sslps_alleles WHERE allele_key=?";
        update(sql, allele.getKey());
    }
}