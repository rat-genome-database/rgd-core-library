package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.OmimQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.datamodel.Omim;

import java.util.List;

/**
 * @author mtutaj
 * API to manipulate OMIM and OMIM_PHENOTYPIC_SERIES tables
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
        String sql = "INSERT INTO omim (mim_number,phenotype,created_date,status,mim_type,last_modified_date) VALUES(?,?,SYSDATE,?,?,SYSDATE)";
        update(sql, omim.getMimNumber(), omim.getPhenotype(), omim.getStatus(), omim.getMimType());
    }

    public void updateOmim( Omim omim ) throws Exception {
        String sql = "UPDATE omim SET phenotype=?,status=?,mim_type=?,last_modified_date=SYSDATE WHERE mim_number=?";
        update(sql, omim.getPhenotype(), omim.getStatus(), omim.getMimType(), omim.getMimNumber());
    }

    public String getOmimPhenotype(String mimNumber) throws Exception {

        List<String> r = StringListQuery.execute(this, "SELECT phenotype FROM omim WHERE mim_number=?", mimNumber);
        if( r.isEmpty() ) {
            return null;
        }
        return r.get(0);
    }

    /// OMIM PHENOTYPIC SERIES

    public List<StringMapQuery.MapPair> getPhenotypicSeriesMappings() throws Exception {
        String sql = "SELECT phenotypic_series_number,phenotype_mim_number FROM omim_phenotypic_series";
        return StringMapQuery.execute(this, sql);
    }

    public void insertPhenotypicSeriesMapping( String psNumber, String phenotypeMimNumber ) throws Exception {
        String sql = "INSERT INTO omim_phenotypic_series (phenotypic_series_number, phenotype_mim_number) VALUES(?,?)";
        this.update(sql, psNumber, phenotypeMimNumber);
    }

    public void deletePhenotypicSeriesMapping( String psNumber, String phenotypeMimNumber ) throws Exception {
        String sql = "DELETE FROM omim_phenotypic_series WHERE phenotypic_series_number=? AND phenotype_mim_number=?";
        this.update(sql, psNumber, phenotypeMimNumber);
    }

    public List<String> getPhenotypicSeriesIdsNotInRgd() throws Exception {
        String sql = """
            SELECT phenotypic_series_number FROM omim_phenotypic_series
            MINUS
            SELECT synonym_name FROM ont_synonyms WHERE term_acc like 'DOID:%' AND synonym_name like 'MIM:PS%'
            """;
        return StringListQuery.execute(this, sql);
    }
}
