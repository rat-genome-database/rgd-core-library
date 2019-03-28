package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.ProteinQuery;
import edu.mcw.rgd.datamodel.Protein;
import edu.mcw.rgd.process.Utils;

import java.util.Collections;
import java.util.List;

/**
 * @author mtutaj
 * API to manipulate PROTEINS table
 */
public class ProteinDAO extends AbstractDAO {

    /**
     * insert a new row in PROTEIN table
     * @param protein Protein object to be inserted into PROTEINS table
     * @throws Exception
     */
    public void insertProtein(Protein protein) throws Exception {

        String sql = "INSERT INTO proteins (rgd_id,uniprot_id,protein_symbol,protein_name,created_date,src_pipeline) "+
                "VALUES(?,?,?,?,SYSDATE,?)";
        update(sql, protein.getRgdId(), protein.getUniprotId(), protein.getSymbol(), protein.getName(),
                protein.getSrcPipeline());
    }

    /**
     * update a row in PROTEINS table, given protein rgd id
     * @param protein Protein object being updated
     * @throws Exception
     */
    public void updateProtein(Protein protein) throws Exception {

        String sql = "UPDATE proteins SET uniprot_id=?,protein_symbol=?,protein_name=?,src_pipeline=? "+
                "WHERE rgd_id=?";
        update(sql, protein.getUniprotId(), protein.getSymbol(), protein.getName(),
                protein.getSrcPipeline(), protein.getRgdId());
    }

    public Protein getProtein(int rgdId) throws Exception {

        String sql = "SELECT p.*,r.species_type_key FROM proteins p,rgd_ids r WHERE p.rgd_id=? AND p.rgd_id=r.rgd_id";
        List<Protein> results = executeProteinQuery(sql, rgdId);
        return results.isEmpty() ? null : results.get(0);
    }

    public Protein getProteinByUniProtId(String uniProtId) throws Exception {

        String sql = "SELECT p.*,r.species_type_key FROM proteins p,rgd_ids r WHERE uniprot_id=? AND p.rgd_id=r.rgd_id";
        List<Protein> results = executeProteinQuery(sql, uniProtId);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Protein> getProteinListByUniProtIdOrSymbol(List<String> uniProtIdOrProteinSymbol, int speciesTypeKey) throws Exception {

        if( uniProtIdOrProteinSymbol==null || uniProtIdOrProteinSymbol.isEmpty() ) {
            return Collections.emptyList();
        }
        String inList = Utils.concatenate(",", uniProtIdOrProteinSymbol, "toUpperCase", "'");
        String sql = "SELECT p.*,r.species_type_key " +
                "FROM proteins p,rgd_ids r WHERE p.rgd_id=r.rgd_id AND r.species_type_key=?" +
                " AND uniprot_id IN ("+inList+") " +
                "UNION " +
                "SELECT p.*,r.species_type_key " +
                "FROM proteins p,rgd_ids r WHERE p.rgd_id=r.rgd_id AND r.species_type_key=?" +
                " AND protein_symbol IN ("+inList+")";

        return executeProteinQuery(sql, speciesTypeKey, speciesTypeKey);
    }

    public List<Protein> getProteinsByRgdIdList(List<Integer> rgdIdsList) throws Exception {
        String sql= "SELECT p.*,r.species_type_key  FROM proteins p, rgd_ids r WHERE p.rgd_id=r.rgd_id and p.rgd_id  IN ("+ Utils.concatenate(rgdIdsList, ",")+") ";
        return executeProteinQuery(sql);
    }

    /// Protein query implementation helper
    public List<Protein> executeProteinQuery(String query, Object ... params) throws Exception {
        ProteinQuery q = new ProteinQuery(this.getDataSource(), query);
        return execute(q, params);
    }

    public int getRgdId(String uniprot_id) throws Exception{
        int rgdId = 0;
        Protein protein = getProteinByUniProtId(uniprot_id);
        if( protein != null ){
            rgdId = protein.getRgdId();
        }
        return rgdId;
    }

    public List<Protein> getProteins() throws Exception{
        String sql= "SELECT p.*,r.species_type_key  FROM proteins p, rgd_ids r WHERE p.rgd_id=r.rgd_id";
        return executeProteinQuery(sql);
    }

    public List<Protein> getProteins(int speciesTypeKey) throws Exception{
        String sql= "SELECT p.*,r.species_type_key  FROM proteins p, rgd_ids r WHERE p.rgd_id=r.rgd_id and r.species_type_key=?";
        return executeProteinQuery(sql, speciesTypeKey);
    }

    public List<Protein> getActiveProteins(int speciesTypeKey) throws Exception{
        String sql= "SELECT p.*,r.species_type_key FROM proteins p, rgd_ids r WHERE p.rgd_id=r.rgd_id AND r.species_type_key=? AND object_status='ACTIVE'";
        return executeProteinQuery(sql, speciesTypeKey);
    }

    public int getProteinsCount(int mapKey, String chr) throws Exception {
        String sql="SELECT COUNT(*) FROM rgd_associations WHERE " +
                "   assoc_type='protein_to_gene' " +
                "   AND detail_rgd_id IN  " +
                "   (SELECT g.rgd_id FROM genes g, rgd_ids r, maps_data m WHERE  " +
                "   g.rgd_id=r.rgd_id " +
                "   AND g.rgd_id=m.rgd_id " +
                "   AND r.object_status='ACTIVE' " +
                //"AND r.species_type_key=3 " +
                "AND m.map_key=?";

        if(chr!=null)
            sql=sql+  " AND m.chromosome=? " ;
        sql= sql+"  )";

        int count;
        if( chr!=null )
            count = getCount(sql, mapKey, chr);
        else
            count = getCount(sql, mapKey);
        return count;
    }

}
