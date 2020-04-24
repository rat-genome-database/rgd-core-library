package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.HgvsNameQuery;
import edu.mcw.rgd.dao.spring.VariantQuery;
import edu.mcw.rgd.datamodel.HgvsName;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.VariantInfo;

import java.util.List;

/**
 * @author mtutaj
 * @since 2/12/14
 * represents a row from CLINVAR+GENOMIC_ELEMENT+RGD_IDS table join
 */
public class VariantInfoDAO extends GenomicElementDAO {

    public VariantInfo getVariant(int rgdId) throws Exception {

        String query = "SELECT g.*,r.*,v.* FROM rgd_ids r,genomic_elements g,clinvar v "+
                "WHERE r.rgd_id=? AND r.rgd_id=g.rgd_id AND g.rgd_id=v.rgd_id";
        VariantQuery q = new VariantQuery(this.getDataSource(), query);

        List<VariantInfo> rows = execute(q, rgdId);
        if( rows.isEmpty() ) {
            return null;
        }
        return rows.get(0);
    }

    /**
     * insert new variant object into CLINVAR table
     * <br>
     * Note: implicitly a row is inserted into GENOMIC_ELEMENTS table as well
     * @param obj VariantInfo object
     * @return count of rows affected
     * @throws Exception
     */
    public int insertVariantInfo(VariantInfo obj) throws Exception {

        // insert a row into GENOMIC_ELEMENTS table
        int r = insertElement(obj);

        // insert a row into CLINVAR table
        String sql =
            "INSERT INTO clinvar(clinical_significance,date_last_evaluated,review_status,method_type,"+
                    "nucleotide_change,trait_name,age_of_onset,prevalence,molecular_consequence,"+
                    "submitter,rgd_id) "+
            "VALUES(?,?,?,?, ?,?,?,?,?, ?,?)";

        return r + update(sql,
            obj.getClinicalSignificance(), obj.getDateLastEvaluated(), obj.getReviewStatus(), obj.getMethodType(),
            obj.getNucleotideChange(), obj.getTraitName(), obj.getAgeOfOnset(), obj.getPrevalence(),
            obj.getMolecularConsequence(), obj.getSubmitter(), obj.getRgdId()
        );
    }

    /**
     * update variant object in CLINVAR and GENOMIC_ELEMENTS tables
     * @param obj VariantInfo object
     * @return count of rows affected
     * @throws Exception
     */
    public int updateVariant(VariantInfo obj) throws Exception {

        // update a row in GENOMIC_ELEMENTS table
        int r = updateElement(obj);

        // update a row in VARIANT table
        String sql =
            "UPDATE clinvar "+
            "SET clinical_significance=?, date_last_evaluated=?, review_status=?, method_type=?, "+
            "    nucleotide_change=?, trait_name=?, age_of_onset=?, prevalence=?, molecular_consequence=?, "+
            "    submitter=? "+
            "WHERE rgd_id=?";

        return r + update(sql,
            obj.getClinicalSignificance(), obj.getDateLastEvaluated(), obj.getReviewStatus(), obj.getMethodType(),
            obj.getNucleotideChange(), obj.getTraitName(), obj.getAgeOfOnset(), obj.getPrevalence(),
            obj.getMolecularConsequence(), obj.getSubmitter(), obj.getRgdId()
        );
    }

    /**
     * get variant object(s) given symbol
     * @param symbol symbol of variant
     * @return List of VariantInfo object(s) matching the symbol
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<VariantInfo> getVariantsBySymbol(String symbol) throws Exception {

        String sql = "SELECT v.*,ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM clinvar v,genomic_elements ge, rgd_ids r "+
                "WHERE LOWER(ge.symbol)=LOWER(?) AND ge.rgd_id=r.rgd_id AND r.object_key=? AND v.rgd_id=ge.rgd_id";

        VariantQuery q = new VariantQuery(this.getDataSource(), sql);
        return execute(q, symbol, RgdId.OBJECT_KEY_VARIANTS);
    }

    /**
     * get variant object(s) given symbol
     * @param source source of variant, usually name of source pipeline
     * @return List of VariantInfo object(s) matching the symbol
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<VariantInfo> getVariantsBySource(String source) throws Exception {

        String sql = "SELECT v.*,ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM clinvar v,genomic_elements ge, rgd_ids r "+
                "WHERE ge.source=? AND ge.rgd_id=r.rgd_id AND r.object_key=? AND v.rgd_id=ge.rgd_id";

        VariantQuery q = new VariantQuery(this.getDataSource(), sql);
        return execute(q, source, RgdId.OBJECT_KEY_VARIANTS);
    }

    /**
     * get variant object(s) associated with given gene
     * @param geneRgdId RGD ID of a gene
     * @return List of VariantInfo object(s) associated with given gene
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<VariantInfo> getVariantsForGene(int geneRgdId) throws Exception {

        String sql = "SELECT v.*,ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM clinvar v, genomic_elements ge, rgd_ids r, rgd_associations a "+
                "WHERE ge.rgd_id=r.rgd_id AND r.object_key=? AND v.rgd_id=ge.rgd_id "+
                "AND assoc_type='variant_to_gene' AND a.detail_rgd_id=? AND ge.rgd_id=master_rgd_id";

        VariantQuery q = new VariantQuery(this.getDataSource(), sql);
        return execute(q, RgdId.OBJECT_KEY_VARIANTS, geneRgdId);
    }

    public List<HgvsName> getHgvsNames(int rgdId) throws Exception {

        String sql = "SELECT * FROM hgvs_names WHERE rgd_id=?";

        HgvsNameQuery q = new HgvsNameQuery(this.getDataSource(), sql);
        return execute(q, rgdId);
    }

    public int insertHgvsNames(List<HgvsName> hgvsNames) throws Exception {

        int rowsAffected = 0;
        for( HgvsName hgvsName: hgvsNames ) {
            rowsAffected += insertHgvsName(hgvsName);
        }
        return rowsAffected;
    }

    public int insertHgvsName(HgvsName hgvsName) throws Exception {

        String sql = "INSERT INTO hgvs_names (rgd_id,hgvs_name_type,hgvs_name) VALUES(?,?,?)";
        return update(sql, hgvsName.getRgdId(), hgvsName.getType(), hgvsName.getName());
    }

    public int deleteHgvsNames(List<HgvsName> hgvsNames) throws Exception {

        int rowsAffected = 0;
        for( HgvsName hgvsName: hgvsNames ) {
            rowsAffected += deleteHgvsName(hgvsName);
        }
        return rowsAffected;
    }

    public int deleteHgvsName(HgvsName hgvsName) throws Exception {

        String sql = "DELETE FROM hgvs_names WHERE rgd_id=? AND hgvs_name_type=? AND hgvs_name=?";
        return update(sql, hgvsName.getRgdId(), hgvsName.getType(), hgvsName.getName());
    }
}

