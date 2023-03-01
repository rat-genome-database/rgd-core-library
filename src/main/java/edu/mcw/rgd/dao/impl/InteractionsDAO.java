package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.InteractionQuery;
import edu.mcw.rgd.dao.spring.ProteinQuery;
import edu.mcw.rgd.datamodel.Interaction;
import edu.mcw.rgd.datamodel.Protein;
import edu.mcw.rgd.process.Utils;

import java.util.*;


/**
 * Created by jthota on 2/26/2016.
 */
public class InteractionsDAO  extends AbstractDAO{

    /**
     * Insert Interaction Record
     * @param pi Interaction object
     * @return returns 1 or 0 based on the insertion
     * @throws Exception
     */
    public int insert(Interaction pi) throws Exception{
        String sql = "insert into interactions(interaction_key, rgd_id_1, rgd_id_2, interaction_type, created_date, last_modified_date) "+
                "SELECT ?,?,?,?,SYSDATE,SYSDATE FROM dual "+
                "WHERE NOT EXISTS (select  rgd_id_1, rgd_id_2, interaction_type from interactions where rgd_id_1=? and rgd_id_2=? and interaction_type=?)";
        return update(sql, pi.getInteractionKey(),pi.getRgdId1(), pi.getRgdId2(), pi.getInteractionType(),pi.getRgdId1(), pi.getRgdId2(), pi.getInteractionType());
    }

    /**
     * To get Interaction Key of the interaction.
     * @param interaction
     * @return interaction_key
     * @throws Exception
     */
    public Integer getInteractionKey(Interaction interaction) throws Exception {
        String sql = "select interaction_key from interactions where  rgd_id_1=? and rgd_id_2=? and interaction_type=?";
        IntListQuery q = new IntListQuery(this.getDataSource(), sql);
        List keys = execute(q, interaction.getRgdId1(), interaction.getRgdId2(), interaction.getInteractionType());
        return keys.size()==0 ? 0 : (int) keys.get(0);
    }

    /**
     * Delete Unmodified interactions.
     * @param key interaction_key
     * @return
     * @throws Exception
     */
    public int deleteUnmodifiedInteractions(int key) throws Exception{
        String sql = "delete interactions  where interaction_key= ?";
        return update(sql, key);
    }

    public List<Interaction> getInteractionsModifiedBeforeTimeStamp(Date date) throws Exception{
        String sql = "select * from interactions where last_modified_date<?";
        InteractionQuery q = new InteractionQuery(this.getDataSource(), sql);
        return execute(q, date);
    }

    public int updateLastModifiedDate(int interactionKey) throws Exception{
        String sql = "update interactions set last_modified_date = SYSDATE  where interaction_key=?";
        return update(sql, interactionKey);
    }


   /*public List<Protein> getInteractorsByUniprotId(String uniprotId) throws Exception {
       String sql = "select rgd_id_1, rgd_id_2 from interactions " +
               "where rgd_id_1 in (select rgd_id from proteins where uniprot_id=?) or rgd_id_2 in (select rgd_id from proteins where uniprot_id =?)";
       ProteinQuery q = new ProteinQuery(this.getDataSource(), sql);
       return execute(q, uniprotId, uniprotId);
    }

    /**
     * Returns a list of interactors of a given interactor
     * @param rgdId
     * @return
    */
   /* public List<Protein> getInteractorsByRgdId(int rgdId) throws Exception{
     //   String sql ="select rgd_id_1, gd_id_2 from interactions where rgd_id_1 =? or rgd_id_2  =?";
        String sql="(select i1.rgd_id_1, i1.rgd_id_2 from interactions i1" +
                "where i1.rgd_id_1 =? ) " +
                "union all " +
                "(Select i2.rgd_id_1, i2.rgd_id_2 from interactions i2 where i2.rgd_id_2 =?" +
                "AND i2.interaction_key not in (" +
                "select i.interaction_key from interactions i where i.rgd_id_1 <> ? ))";

        ProteinQuery q = new ProteinQuery(this.getDataSource(), sql);
        return execute(q, rgdId, rgdId, rgdId);
    }
*/
    public List<Interaction> getInteractionsByRgdId(int rgdId) throws Exception{
     //   String sql="select * from interactions where rgd_id_1=? or rgd_id_2=?";

        String sql="(select i1.rgd_id_1, i1.rgd_id_2 from interactions i1" +
                "where i1.rgd_id_1 =? ) " +
                "union all " +
                "(Select i2.rgd_id_1, i2.rgd_id_2 from interactions i2 where i2.rgd_id_2 =?" +
                "AND i2.interaction_key not in (" +
                "select i.interaction_key from interactions i where i.rgd_id_1 <> ? ))";


        InteractionQuery q= new InteractionQuery(this.getDataSource(), sql);
        return execute(q, rgdId, rgdId, rgdId);
    }

    public List<Interaction> getInteractionsByUniprotId(String uniprotId) throws Exception{
        String sql="(select i1.* from interactions i1" +
                "where i1.rgd_id_1 in (select rgd_id from proteins where uniprot_id=?)) " +
                "union all " +
                "(Select i2.* from interactions i2 where i2.rgd_id_2 in" +
                "(select rgd_id from proteins where uniprot_id=?) " +
                "AND i2.interaction_key not in (" +
                "select i.interaction_key from interactions i where i.rgd_id_1 not in (select rgd_id from proteins where uniprot_id=?)))";
        InteractionQuery q= new InteractionQuery(this.getDataSource(), sql);
        return execute(q, uniprotId, uniprotId, uniprotId);
    }

    public List<Interaction> getAllInteractionsBySpecies(int speciesTypeKey) throws Exception {
        String sql="(select i1.* from interactions i1 where  " +
                "i1.rgd_id_1 in (select p1.rgd_id from proteins p1, rgd_ids r where r.rgd_id=p1.rgd_id and r.species_type_key=?) )" +
                "union all " +
                "(select i2.* from interactions i2 where i2.rgd_id_2 in  " +
                "(select p1.rgd_id from proteins p1, rgd_ids r where r.rgd_id=p1.rgd_id and r.species_type_key=?)" +
                "and i2.interaction_key not in ( " +
                "select i.interaction_key from interactions i where " +
                "i.rgd_id_1 in (select p1.rgd_id from proteins p1, rgd_ids r where r.rgd_id=p1.rgd_id and r.species_type_key=?) " +
                ")" +
                ") " ;
        InteractionQuery q = new InteractionQuery(this.getDataSource(), sql);
        return this.execute(q, new Object[]{speciesTypeKey,speciesTypeKey,speciesTypeKey});
    }
    public List<Interaction> getInteractionsByRgdIdsList(List<Integer> rgdIdsList) throws Exception {
        String sql = "(SELECT i1.* FROM interactions i1 WHERE " +
                "i1.rgd_id_1 IN (" + Utils.concatenate(rgdIdsList, ",") + ") ) " +
                "union all" +
                "(select i2.* from interactions i2 where i2.rgd_id_2 IN (" + Utils.concatenate(rgdIdsList, ",") + ")" +
                " AND i2.interaction_key not in (" +
                "select i.interaction_key from interactions i where " +
                "i.rgd_id_1 in (" + Utils.concatenate(rgdIdsList, ",") + ")"+
                ")" +
                ")";
        InteractionQuery q = new InteractionQuery(this.getDataSource(), sql);
        return this.execute(q, new Object[0]);
    }

    public int getInteractionCountByRgdIdsList(List<Integer> rgdIdsList) throws Exception {

        // original query
        // note: it yields the same results but it is up to 50% slower than the query below  --mt
        //
        // String sql= "SELECT COUNT(*) FROM interactions WHERE rgd_id_1 IN ("+Utils.concatenate(rgdIdsList,",")+") "+
        //        " OR rgd_id_2 IN ("+Utils.concatenate(rgdIdsList, ",")+")";

        String sql="select count(*) from (" +
            "(SELECT i1.* FROM interactions i1 WHERE i1.rgd_id_1 IN (" + Utils.concatenate(rgdIdsList, ",") + ")) " +
            "UNION ALL " +
            "(SELECT i2.* FROM interactions i2 WHERE i2.rgd_id_2 IN (" + Utils.concatenate(rgdIdsList, ",") + ")" +
            "        AND i2.interaction_key NOT IN (" +
            "            SELECT i.interaction_key FROM interactions i WHERE i.rgd_id_1 IN (" + Utils.concatenate(rgdIdsList, ",") + ")" +
            "        )" +
            "))";

        return getCount(sql);
    }

    public int getInteractionCountByGeneRgdId(int geneRgdId) throws Exception {

        String sql = "select count(*) from (" +
            "(SELECT i1.* FROM interactions i1 WHERE i1.rgd_id_1 IN (SELECT master_rgd_id FROM rgd_associations WHERE assoc_type='protein_to_gene' AND detail_rgd_id=?)) " +
            "UNION ALL " +
            "(SELECT i2.* FROM interactions i2 WHERE i2.rgd_id_2 IN (SELECT master_rgd_id FROM rgd_associations WHERE assoc_type='protein_to_gene' AND detail_rgd_id=?)" +
            " AND i2.interaction_key NOT IN ( " +
            "   SELECT i.interaction_key FROM interactions i WHERE i.rgd_id_1 IN (SELECT master_rgd_id FROM rgd_associations WHERE assoc_type='protein_to_gene' AND detail_rgd_id=?)" +
            ")))";

        return getCount(sql, geneRgdId, geneRgdId, geneRgdId);
    }

    public List<Interaction> getInteractions() throws Exception{
        String sql = "SELECT * FROM interactions";
        InteractionQuery q = new InteractionQuery(this.getDataSource(), sql);
        return execute(q);
    }

    public List<Interaction> getInteractionsByRgdIdsList(List<Integer> rgdIdsList, Date createdFrom, Date createdTo) throws Exception {

    /*    String sql= "SELECT * FROM interactions WHERE created_date between ? and ? and (rgd_id_1 IN ("+Utils.concatenate(rgdIdsList,",")+") "+
                " OR rgd_id_2 IN("+Utils.concatenate(rgdIdsList, ",")+"))";*/

        String sql = "(SELECT i1.* FROM interactions i1 WHERE created_date between ? and ? and " +
                "i1.rgd_id_1 IN (" + Utils.concatenate(rgdIdsList, ",") + ") ) " +
                "union all" +
                "(select i2.* from interactions i2 where created_date between ? and ? and i2.rgd_id_2 IN (" + Utils.concatenate(rgdIdsList, ",") + ")" +
                " AND i2.interaction_key not in (" +
                "select i.interaction_key from interactions i where created_date between ? and ? and " +
                "i.rgd_id_1 in (" + Utils.concatenate(rgdIdsList, ",") + ")" +
                ")" +
                ")";
        InteractionQuery q= new InteractionQuery(this.getDataSource(), sql);
        //System.out.println(sql);

        return execute(q, createdFrom, createdTo, createdFrom, createdTo, createdFrom, createdTo);
    }


    /** get count of interactions where one or both interactions are for given species
     *
     * @param speciesTypeKey species type key
     * @return
     * @throws Exception
     */
    public int getInteractionCountForSpecies(int speciesTypeKey) throws Exception {
        String sql = "SELECT COUNT(*) FROM interactions \n"+
                "WHERE EXISTS (SELECT 1 FROM rgd_ids r WHERE rgd_id_1=r.rgd_id AND r.species_type_key=?)\n"+
                " OR EXISTS (SELECT 1 FROM rgd_ids r WHERE rgd_id_2=r.rgd_id AND r.species_type_key=?)";
        return getCount(sql, speciesTypeKey, speciesTypeKey);
    }

}
